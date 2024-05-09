package com.material.service.admin;


import com.material.dto.admin.SetmealDTO;
import com.material.dto.admin.SetmealPageQueryDTO;
import com.material.entity.Setmeal;
import com.material.result.PageResult;
import com.material.vo.admin.SetmealVO;
import com.material.vo.user.MaterialItemVO;

import java.util.List;

public interface SetmealService {

    /**
     * 新增套餐，同时需要保存套餐和物资的关联关系
     *
     * @param setmealDTO
     */
    void saveWithMaterial(SetmealDTO setmealDTO);

    /**
     * 分页查询
     *
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 批量删除套餐
     *
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询套餐和关联的菜品数据
     *
     * @param id
     * @return
     */
    SetmealVO getByIdWithMaterial(Long id);

    /**
     * 修改套餐
     *
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 套餐启用、停用
     *
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 动态条件(name、category_id、status)查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    List<MaterialItemVO> getMaterialItemById(Long id);
}
