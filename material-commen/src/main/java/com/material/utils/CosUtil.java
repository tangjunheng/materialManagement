package com.material.utils;



import com.material.properties.CosProperties;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.model.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Data
public class CosUtil {



    private BasicCOSCredentials credentials;
    private ClientConfig clientConfig;
    private COSClient cosClient;
    private CosProperties cosProperties;
    /**
     * 初始化客户端
     * @param credentials
     * @param clientConfig
     */
    public CosUtil(BasicCOSCredentials credentials, ClientConfig clientConfig, CosProperties cosProperties){
        cosClient = new COSClient(credentials,clientConfig);
        this.cosProperties = cosProperties;
    }

    /**
     * 创建存储桶
     * @param bucketName
     */
    public Bucket queryBuckets(String bucketName) {
        String bucket = "bucketName"+"-1250000000"; //存储桶名称，格式：BucketName-APPID
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucket);
        // 设置 bucket 的权限为 Private(私有读写)、其他可选有 PublicRead（公有读私有写）、PublicReadWrite（公有读写）
        createBucketRequest.setCannedAcl(CannedAccessControlList.Private);
        // 创建存储桶
        Bucket bucketResult = cosClient.createBucket(createBucketRequest);
        return bucketResult;

    }

    /**
     * 查询存储桶
     */
    public List<Bucket> queryBuckets(){
        return cosClient.listBuckets();
    }

    /**
     * 物资图片上传
     * @param file 文件
     * @param key 文件名称
     * @return 文件路径
     */
    public String uploadMaterial(MultipartFile file, String key) throws IOException {

        // 配置上传图片的元数据
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());


        // bucketName 指定文件将要存放的存储桶
        // key 指定文件上传到 COS 上的路径，即对象键。对象键其实就是该文件存储在腾讯云的文件路径和文件名
        // 例如对象键为 folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        // file 要上传的文件
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosProperties.getMaterialBucketName(), key, file.getInputStream(),objectMetadata);
        cosClient.putObject(putObjectRequest);

        String path = cosProperties.getMaterialCosPath()+"/"+key;


        log.info("文件上传到:{}", path);
        return path;
    }


    /**
     * 用户图片上传
     * @param file 文件
     * @param key 文件名称
     * @return 文件路径
     */
    public String uploadUser(MultipartFile file, String key) throws IOException {

        // 配置上传图片的元数据
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());


        // bucketName 指定文件将要存放的存储桶
        // key 指定文件上传到 COS 上的路径，即对象键。对象键其实就是该文件存储在腾讯云的文件路径和文件名
        // 例如对象键为 folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        // file 要上传的文件
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosProperties.getUserBucketName(), key, file.getInputStream(),objectMetadata);
        cosClient.putObject(putObjectRequest);

        String path = cosProperties.getUserCosPath()+"/"+key;


        log.info("文件上传到:{}", path);
        return path;
    }

    /**
     * 删除物资对象
     * @param key
     */
    public void deleteMaterial(String key){
        cosClient.deleteObject(cosProperties.getUserBucketName(), key);
    }


    /**
     * 删除用户对象
     * @param key
     */
    public void deleteUser(String key){
        cosClient.deleteObject(cosProperties.getUserBucketName(), key);
    }







}
