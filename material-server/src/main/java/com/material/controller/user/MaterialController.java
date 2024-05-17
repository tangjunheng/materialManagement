package com.material.controller.user;


import com.material.constant.StatusConstant;
import com.material.entity.Material;
import com.material.result.Result;
import com.material.service.admin.MaterialService;
import com.material.vo.admin.MaterialVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/material")
@Slf4j
@Tag(name = "C端-物资浏览接口")
public class MaterialController {

    @Resource
    private MaterialService materialService;
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 根据分类id查询物资
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @Operation(
            description = "根据分类id查询物资",
            summary = "根据分类id查询物资"
    )
    public Result<List<MaterialVO>> list(Long categoryId) {
        //构造redis中的key，规则：material_分类id
        String key = "material_" + categoryId;

        //查询redis中是否存在菜品数据
        List<MaterialVO> materialVOList = (List<MaterialVO>) redisTemplate.opsForValue().get(key);
        if(materialVOList != null && materialVOList.size() > 0){
            //如果存在，直接返回，无须查询数据库
            return Result.success(materialVOList);
        }

        ////////////////////////////////////////////////////////
        // 根据分类id查询启用中的物资
        List<Material> list = materialService.list(categoryId);
        // 将物资类型转为VO类型
        materialVOList = new LinkedList<>();
        for (Material m : list) {
            MaterialVO vo = new MaterialVO();
            BeanUtils.copyProperties(m,vo);
            materialVOList.add(vo);
        }

        // 如果redis不存在，查询数据库，将查询到的数据放入redis中
        redisTemplate.opsForValue().set(key,materialVOList);

        return Result.success(materialVOList);
    }

}
