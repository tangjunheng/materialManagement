package com.material.service.admin.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.material.constant.MessageConstant;
import com.material.constant.StatusConstant;
import com.material.dto.admin.MaterialDTO;
import com.material.dto.admin.MaterialPageQueryDTO;
import com.material.entity.Category;
import com.material.entity.Material;
import com.material.entity.Setmeal;
import com.material.exception.CategoryNotFoundException;
import com.material.exception.DeletionNotAllowedException;
import com.material.exception.MaterialNotFoundException;
import com.material.exception.StatusErrorException;
import com.material.mapper.admin.CategoryMapper;
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
import java.util.LinkedList;
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
    @Resource
    private CategoryMapper categoryMapper;

    /**
     * 新增物资
     *
     * @param materialDTO
     */
    @Override
    public void saveMaterial(MaterialDTO materialDTO) {
        // 查看分类是否存在
        Category category = categoryMapper.getById(materialDTO.getCategoryId());
        if (category == null) {
            throw new CategoryNotFoundException(MessageConstant.CATEGORY_NOT_FOUND);
        }
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
        Page<MaterialVO> page = materialMapper.pageQuery(materialPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 物资批量删除
     *
     * @param materialIds
     */
    @Override
    @Transactional // 事务
    public void deleteBatch(List<Long> materialIds) {
        //判断当前物资是否能够删除---是否存在启用中的物资？？
        List<Material> materials = materialMapper.getByIds(materialIds);
        for (Material material : materials) {
            if (material.getStatus().equals(StatusConstant.ENABLE)) {
                //当前物资处于启用中，不能删除
                throw new DeletionNotAllowedException(MessageConstant.MATERIAL_ON_USE);
            }
        }

        // 判断当物资是否能够删除---是否被分类关联了？？
        List<Long> setmealIds = setmealMaterialMapper.getSetmealIdsByMaterialIds(materialIds);
        if (setmealIds != null && setmealIds.size() > 0) {
            //当前物资被套餐关联了，不能删除
            throw new DeletionNotAllowedException(MessageConstant.MATERIAL_BE_RELATED_BY_SETMEAL);
        }

        //删除物资表中的物资数据
        // TODO 后续可以将不能删除的setmealIds放进返回值中，方便前端调查
        materialMapper.deleteByIds(materialIds);


    }

    /**
     * 根据物资id查询物资
     *
     * @param materialIds
     * @return
     */
    @Override
    public List<MaterialVO> getById(List<Long> materialIds) {
        List<MaterialVO> materialVOs = new LinkedList<>();
        // 根据id查询物资数据
        List<Material> materials = materialMapper.getByIds(materialIds);
        // 判断是否查询到物资
        if (materials == null && materials.size() > 0) {
            throw new MaterialNotFoundException(MessageConstant.MATERIAL_NOT_FOUND);
        }


        for (Material material : materials){
            // 将查询到的数据封装到VO
            MaterialVO materialVO = new MaterialVO();
            BeanUtils.copyProperties(material, materialVO);
            materialVOs.add(materialVO);
        }

        return materialVOs;
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
    public List<Long> startOrStop(Integer status, Long id) {
        List<Long> setmealIds = new ArrayList<Long>();

        // 判断status是否是0或1，如果不是就报错
        if (!status.equals(StatusConstant.DISABLE) && !status.equals(StatusConstant.ENABLE)){
            throw new StatusErrorException(MessageConstant.STATUS_NOT_FOUND);
        }

        Material dish = Material.builder()
                .id(id)
                .status(status)
                .build();
        int count = materialMapper.update(dish);

        // 判断物资是否存在
        if (count == 0) {
            throw new MaterialNotFoundException(MessageConstant.MATERIAL_NOT_FOUND);
        }

        if (status.equals(StatusConstant.DISABLE)) {
            // 如果是停用操作，还需要将包含当前物资的套餐也停用
            List<Long> materialId = new ArrayList<>();
            materialId.add(id);
            // select setmeal_id from setmeal_material where material_id in (?,?,?)
            setmealIds = setmealMaterialMapper.getSetmealIdsByMaterialIds(materialId);
            // 判断是否有套餐，如果有，则把套餐也停用
            if (setmealIds != null && setmealIds.size() > 0) {
                log.info("物资id为{}，正在把id为{}的套餐一起禁用",id,setmealIds);
                for (Long setmealId : setmealIds) {
                    Setmeal setmeal = Setmeal.builder()
                            .id(setmealId)
                            .status(StatusConstant.DISABLE)
                            .build();
                    setmealMapper.update(setmeal);
                }
            }
        }

        return setmealIds;
    }
}
