package com.material.service.admin.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.material.constant.MessageConstant;
import com.material.constant.StatusConstant;
import com.material.dto.admin.MaterialDTO;
import com.material.dto.admin.MaterialPageQueryDTO;
import com.material.entity.Material;
import com.material.entity.Setmeal;
import com.material.exception.DeletionNotAllowedException;
import com.material.mapper.admin.MaterialMapper;
import com.material.mapper.admin.SetmealMapper;
import com.material.mapper.admin.SetmealMaterialMapper;
import com.material.result.PageResult;
import com.material.service.admin.MaterialService;
import com.material.vo.admin.MaterialVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MaterialServiceImpl implements MaterialService {

    @Resource
    MaterialMapper materialMapper;
    @Resource
    SetmealMaterialMapper setmealMaterialMapper;
    @Resource
    private SetmealMapper setmealMapper;

    /**
     * 新增物资
     *
     * @param materialDTO
     */
    @Override
    public void saveMaterial(MaterialDTO materialDTO) {
        Material material = new Material();
        BeanUtils.copyProperties(materialDTO,material);

        // 向物资表插入1条数据
        materialMapper.insert(material);


    }

    /**
     * 分页查询物资
     *
     * @param materialPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(MaterialPageQueryDTO materialPageQueryDTO) {
        PageHelper.startPage(materialPageQueryDTO.getPage(), materialPageQueryDTO.getPageSize());
        Page<MaterialVO> page = materialMapper.pageQuery(materialPageQueryDTO);//后绪步骤实现
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 物资批量删除
     *
     * @param ids
     */
    @Override
    @Transactional // 事务
    public void deleteBatch(List<Long> ids) {
        //判断当前物资是否能够删除---是否存在启用中的物资？？
        for (Long id : ids) {
            Material material = materialMapper.getById(id);
            if (material.getStatus() == StatusConstant.ENABLE) {
                //当前物资处于启用中，不能删除
                throw new DeletionNotAllowedException(MessageConstant.MATERIAL_ON_USE);
            }
        }

        // 判断当前菜品是否能够删除---是否被分类关联了？？
        // 后续可以将setmealIds放进返回值中，方便前端调查
        List<Long> setmealIds = setmealMaterialMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && setmealIds.size() > 0) {
            //当前菜品被套餐关联了，不能删除
            throw new DeletionNotAllowedException(MessageConstant.MATERIAL_BE_RELATED_BY_SETMEAL);
        }

        //删除菜品表中的菜品数据
        for (Long id : ids) {
            materialMapper.deleteById(id);//后绪步骤实现

        }
    }

    /**
     * 根据物资id查询物资
     *
     * @param id
     * @return
     */
    @Override
    public MaterialVO getById(Long id) {
        // 根据id查询物资数据
        Material material = materialMapper.getById(id);


        // 将查询到的数据封装到VO
        MaterialVO materialVO = new MaterialVO();
        BeanUtils.copyProperties(material, materialVO);

        return materialVO;
    }

    /**
     * 根据物资id修改物资基本信息
     *
     * @param materialDTO
     */
    @Override
    public void update(MaterialDTO materialDTO) {
        Material material = new Material();
        BeanUtils.copyProperties(materialDTO, material);

        //修改菜品表基本信息
        materialMapper.update(material);
    }

    /**
     * 根据分类id查询已启用的物资信息
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<Material> list(Long categoryId) {
        Material material = Material.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return  materialMapper.list(material);
    }

    /**
     * 物资启用、停用
     *
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Material dish = Material.builder()
                .id(id)
                .status(status)
                .build();
        materialMapper.update(dish);

        if (status == StatusConstant.DISABLE) {
            // 如果是停用操作，还需要将包含当前物资的套餐也停用
            List<Long> dishIds = new ArrayList<>();
            dishIds.add(id);
            // select setmeal_id from setmeal_material where material_id in (?,?,?)
            List<Long> setmealIds = setmealMaterialMapper.getSetmealIdsByDishIds(dishIds);
            // 判断是否有套餐，如果有，则把套餐也停用
            if (setmealIds != null && setmealIds.size() > 0) {
                for (Long setmealId : setmealIds) {
                    Setmeal setmeal = Setmeal.builder()
                            .id(setmealId)
                            .status(StatusConstant.DISABLE)
                            .build();
                    setmealMapper.update(setmeal);
                }
            }
        }
    }
}
