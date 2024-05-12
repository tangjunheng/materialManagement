package com.material.service.admin.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.material.constant.MessageConstant;
import com.material.constant.StatusConstant;
import com.material.dto.admin.SetmealDTO;
import com.material.dto.admin.SetmealPageQueryDTO;
import com.material.entity.Material;
import com.material.entity.Setmeal;
import com.material.entity.SetmealMaterial;
import com.material.exception.DeletionNotAllowedException;
import com.material.exception.MaterialNotFoundException;
import com.material.exception.SetmealEnableFailedException;
import com.material.exception.StatusErrorException;
import com.material.mapper.admin.MaterialMapper;
import com.material.mapper.admin.SetmealMapper;
import com.material.mapper.admin.SetmealMaterialMapper;
import com.material.result.PageResult;
import com.material.service.admin.SetmealService;
import com.material.vo.admin.SetmealVO;
import com.material.vo.user.MaterialItemVO;
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
public class SetmealServiceImpl implements SetmealService {

    @Resource
    private SetmealMapper setmealMapper;
    @Resource
    private SetmealMaterialMapper setmealMaterialMapper;
    @Resource
    private MaterialMapper materialMapper;

    /**
     * 新增套餐，同时需要保存套餐和物资的关联关系
     *
     * @param setmealDTO
     */
    @Override
    @Transactional // 开启事务
    public void saveWithMaterial(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // TODO 先验证分类是否对应得上

        // 向套餐表插入数据
        setmealMapper.insert(setmeal);

        // 获取生成的套餐id
        Long setmealId = setmeal.getId();

        // 获取需要绑定的套餐物资数据
        List<SetmealMaterial> setmealMaterials = setmealDTO.getSetmealMaterials();
        // 获取materials对应ids
        List<Long> MaterialIds = new LinkedList<>();
        for (SetmealMaterial setmealMaterial : setmealMaterials){
            MaterialIds.add(setmealMaterial.getMaterialId());
        }
        // 查询物资信息
        List<Material> materials = materialMapper.getByIds(MaterialIds);
        // 判断物资是否存在
        if (materials.size() < setmealMaterials.size()) {
            throw new MaterialNotFoundException(MessageConstant.MATERIAL_NOT_FOUND);
        }
        //  Lambda 表达式对关联表进行遍历
        setmealMaterials.forEach(setmealMaterial -> {
            int i=0;
            // 添加套餐id
            setmealMaterial.setSetmealId(setmealId);
            // 添加物资id
            setmealMaterial.setMaterialId(materials.get(i).getId());
            // 添加物资数量
            setmealMaterial.setCopies(materials.get(i).getNumber());
            // 添加物资名称
            setmealMaterial.setName(materials.get(i).getName());
            i++;
        });

        //保存套餐和菜品的关联关系
        setmealMaterialMapper.insertBatch(setmealMaterials);
    }

    /**
     * 分页查询
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除套餐
     *
     * @param ids
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        ids.forEach(id -> {
            Setmeal setmeal = setmealMapper.getById(id);
            if(StatusConstant.ENABLE == setmeal.getStatus()){
                //起售中的套餐不能删除
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });

        ids.forEach(setmealId -> {
            //删除套餐表中的数据
            setmealMapper.deleteById(setmealId);
            //删除套餐菜品关系表中的数据
            setmealMaterialMapper.deleteBySetmealId(setmealId);
        });
    }

    /**
     * 根据套餐id查询套餐和关联的表数据
     *
     * @param setmealId
     * @return
     */
    @Override
    public SetmealVO getByIdWithMaterial(Long setmealId) {
        SetmealVO setmealVO = setmealMapper.getByIdWithMaterial(setmealId);

        return setmealVO;
    }

    /**
     * 修改套餐
     *
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // TODO 先验证分类是否对应得上

        // 1、修改套餐表，执行update
        setmealMapper.update(setmeal);

        // 获取套餐id
        Long setmealId = setmealDTO.getId();

        // 2、删除套餐和菜品的关联关系，操作setmeal_material表，执行delete
        setmealMaterialMapper.deleteBySetmealId(setmealId);

        // 2.5、获取需要绑定的套餐物资数据
        // 获取关联表数据
        List<SetmealMaterial> setmealMaterials = setmealDTO.getSetmealMaterials();
        // 获取materials对应ids
        List<Long> MaterialIds = new LinkedList<>();
        for (SetmealMaterial setmealMaterial : setmealMaterials){
            MaterialIds.add(setmealMaterial.getMaterialId());
        }
        // 查询物资信息
        List<Material> materials = materialMapper.getByIds(MaterialIds);
        // 判断物资是否存在
        if (materials.size() < setmealMaterials.size()) {
            throw new MaterialNotFoundException(MessageConstant.MATERIAL_NOT_FOUND);
        }


        setmealMaterials.forEach(setmealMaterial -> {
            int i=0;
            // 添加套餐id
            setmealMaterial.setSetmealId(setmealId);
            // 添加物资id
            setmealMaterial.setMaterialId(materials.get(i).getId());
            // 添加物资数量
            setmealMaterial.setCopies(materials.get(i).getNumber());
            // 添加物资名称
            setmealMaterial.setName(materials.get(i).getName());
            i++;
        });
        // 3、重新插入套餐和菜品的关联关系，操作setmeal_material表，执行insert
        setmealMaterialMapper.insertBatch(setmealMaterials);
    }

    /**
     * 套餐启用、停用
     *
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        // 判断status是否是0或1，如果不是就报错
        if (!status.equals(StatusConstant.DISABLE) && !status.equals(StatusConstant.ENABLE)){
            throw new StatusErrorException(MessageConstant.STATUS_NOT_FOUND);
        }

        //起售套餐时，判断套餐内是否有禁用物资，有禁用物资提示"套餐内包含未启用物资，无法启用"
        if(status.equals(StatusConstant.ENABLE)){
            //select a.* from material a left join setmeal_material b on a.id = b.material_id where b.setmeal_id = ?
            // 获取物资信息
            List<Material> materialList = materialMapper.getBySetmealId(id);
            // 判断套餐内是否有禁用物资
            if(materialList != null && materialList.size() > 0){
                materialList.forEach(dish -> {
                    if(dish.getStatus().equals(StatusConstant.DISABLE)){
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }

        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }

    /**
     * 动态条件(name、category_id、status)查询套餐
     *
     * @param setmeal
     * @return
     */
    @Override
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     *
     * @param id
     * @return
     */
    @Override
    public List<MaterialItemVO> getMaterialItemById(Long id) {
        return setmealMapper.getMaterialItemBySetmealId(id);
    }


}
