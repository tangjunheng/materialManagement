package com.material.controller.user;


import com.material.constant.StatusConstant;
import com.material.entity.Setmeal;
import com.material.result.Result;
import com.material.service.admin.SetmealService;
import com.material.vo.user.MaterialItemVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Tag(name = "C端-套餐浏览接口")
public class SetmealController {
    @Resource
    private SetmealService setmealService;

    /**
     * 条件查询
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @Operation(
            description = "根据分类id查询套餐",
            summary = "根据分类id查询套餐"
    )
    @Cacheable(cacheNames = "setmealCache",key = "#categoryId") //key: setmealCache::23
    public Result<List<Setmeal>> list(Long categoryId) {
        Setmeal setmeal = new Setmeal();
        setmeal.setCategoryId(categoryId);
        setmeal.setStatus(StatusConstant.ENABLE);

        List<Setmeal> list = setmealService.list(setmeal);
        return Result.success(list);
    }

    /**
     * 根据套餐id查询包含的物资列表
     *
     * @param id
     * @return
     */
    @GetMapping("/material/{id}")
    @Operation(
            description = "根据套餐id查询包含的物资列表",
            summary = "根据套餐id查询包含的物资列表"
    )
    public Result<List<MaterialItemVO>> materialList(@PathVariable("id") Long id) {
        List<MaterialItemVO> list = setmealService.getMaterialItemById(id);
        return Result.success(list);
    }
}
