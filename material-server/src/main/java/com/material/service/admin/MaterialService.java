package com.material.service.admin;

import com.material.dto.admin.MaterialDTO;
import com.material.dto.admin.MaterialPageQueryDTO;
import com.material.entity.Material;
import com.material.result.PageResult;
import com.material.vo.admin.MaterialVO;

import java.util.List;

public interface MaterialService {

    /**
     * 新增物资
     * @param materialDTO
     */
    public void saveMaterial(MaterialDTO materialDTO);

    /**
     * 分页查询物资
     * @param materialPageQueryDTO
     * @return
     */
    PageResult pageQuery(MaterialPageQueryDTO materialPageQueryDTO);

    /**
     * 物资批量删除
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据物资id查询物资
     * @param id
     * @return
     */
    MaterialVO getById(Long id);

    /**
     * 根据id修改物资基本信息
     * @param materialDTO
     */
    void update(MaterialDTO materialDTO);

    /**
     * 根据分类id查询已启用的物资信息
     * @param categoryId
     * @return
     */
    List<Material> list(Long categoryId);

    /**
     * 物资启用、停用
     *
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);
}
