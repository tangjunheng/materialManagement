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
        // 根据分类id查询启用中的物资
        List<Material> list = materialService.list(categoryId);
        // 将物资类型转为VO类型
        List<MaterialVO> materialVOList = new ArrayList<>();
        for (Material m : list) {
            MaterialVO vo = new MaterialVO();
            BeanUtils.copyProperties(m,vo);
            materialVOList.add(vo);
        }

        return Result.success(materialVOList);
    }

}
