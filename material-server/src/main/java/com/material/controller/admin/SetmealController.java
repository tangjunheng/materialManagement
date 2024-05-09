package com.material.controller.admin;


import com.material.dto.admin.SetmealDTO;
import com.material.dto.admin.SetmealPageQueryDTO;
import com.material.result.PageResult;
import com.material.result.Result;
import com.material.service.admin.SetmealService;
import com.material.vo.admin.SetmealVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Tag(name = "套餐相关接口")
public class SetmealController {

    @Resource
    private SetmealService setmealService;

    @PostMapping
    @Operation(
            description = "新增套餐",
            summary = "新增套餐"
    )
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        setmealService.saveWithMaterial(setmealDTO);
        return Result.success();
    }

    /**
     * 分页查询套餐
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @Operation(
            description = "分页查询套餐",
            summary = "分页查询套餐"
    )
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除套餐
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @Operation(
            description = "批量删除套餐",
            summary = "批量删除套餐"
    )
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result delete(@RequestParam List<Long> ids) {
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据套餐id查询套餐与其关联表数据，用于修改页面回显数据
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(
            description = "根据套餐id查询套餐与其关联表数据",
            summary = "根据套餐id查询套餐与其关联表数据"
    )
    public Result<SetmealVO> getById(@PathVariable Long id) {
        SetmealVO setmealVO = setmealService.getByIdWithMaterial(id);
        return Result.success(setmealVO);
    }

    /**
     * 修改套餐
     *
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @Operation(
            description = "修改套餐",
            summary = "修改套餐"
    )
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        setmealService.update(setmealDTO);
        return Result.success();
    }

    /**
     * 套餐起售停售
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @Operation(
            description = "套餐启用停用",
            summary = "套餐启用停用"
    )
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result startOrStop(@PathVariable Integer status, Long id) {
        setmealService.startOrStop(status, id);
        return Result.success();
    }

}
