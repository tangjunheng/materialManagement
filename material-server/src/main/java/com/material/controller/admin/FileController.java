package com.material.controller.admin;


import com.material.constant.MessageConstant;
import com.material.mapper.admin.MaterialMapper;
import com.material.result.Result;
import com.material.utils.CosUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/file")
@Slf4j
public class FileController {
    @Resource
    private CosUtil cosUtil;

    @Resource
    private MaterialMapper materialMapper;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传：{}",file);

        try {
            // 原始文件名
            String originalFilename = file.getOriginalFilename();
            // 截取原始文件名的后缀   dfdfdf.png
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 构造新文件名称
            String objectName = UUID.randomUUID() + extension;

            //文件的请求路径
            String filePath = cosUtil.uploadMaterial(file, objectName);
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e);
        }

        return Result.error(MessageConstant.UPLOAD_FAILED);
    }

    @GetMapping("/delete")
    public Result<String> delete(String fileName){
        log.info("删除文件：{}",fileName);
        cosUtil.deleteMaterial(fileName);
        return Result.success();

    }

}
