package com.material.controller.admin;

import com.material.dto.admin.MaterialDTO;
import com.material.dto.admin.MaterialPageQueryDTO;
import com.material.entity.Material;
import com.material.result.PageResult;
import com.material.result.Result;
import com.material.service.admin.MaterialService;
import com.material.vo.admin.MaterialVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/material")
@Slf4j
@Tag(name = "物资相关接口")
public class MaterialController {

    @Resource
    MaterialService materialService;

    /**
     * 新增物资
     * @param materialDTO
     * @return
     */
    @PostMapping("/add")
    @Operation(
            description = "新增物资",
            summary = "新增物资"
    )
    public Result save(@RequestBody MaterialDTO materialDTO) {
        log.info("新增物资：{}", materialDTO);
        materialService.saveMaterial(materialDTO);
        return Result.success();
    }

    /**
     * 物资分页查询
     * @param materialPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @Operation(
            description = "物资分页查询",
            summary = "物资分页查询"
    )
    public Result<PageResult> page(MaterialPageQueryDTO materialPageQueryDTO) {
        log.info("物资分页查询:{}", materialPageQueryDTO);
        PageResult pageResult = materialService.pageQuery(materialPageQueryDTO);//后绪步骤定义
        return Result.success(pageResult);
    }

    @DeleteMapping("/delete")
    @Operation(
            description = "物资批量删除",
            summary = "物资批量删除"
    )
    public Result delete(@RequestParam List<Long> ids) {
        log.info("物资批量删除：{}", ids);
        materialService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据id查询物资
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(
            description = "根据id查询物资",
            summary = "根据id查询物资"
    )
    public Result<MaterialVO> getById(@PathVariable Long id) {
        log.info("根据id查询物资：{}", id);
        MaterialVO materialVO = materialService.getById(id);
        return Result.success(materialVO);
    }

    /**
     * 修改物资信息
     * @param materialDTO
     * @return
     */
    @PutMapping
    @Operation(
            description = "修改物资信息",
            summary = "修改物资信息"
    )
    public Result update(@RequestBody MaterialDTO materialDTO) {
        log.info("修改物资信息：{}", materialDTO);
        materialService.update(materialDTO);
        return Result.success();
    }

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
    public Result<List<Material>> list(Long categoryId){
        log.info("根据分类id查询物资：{}", categoryId);
        List<Material> list = materialService.list(categoryId);
        return Result.success(list);
    }
}
