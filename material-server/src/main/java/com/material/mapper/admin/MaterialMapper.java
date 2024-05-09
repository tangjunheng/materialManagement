package com.material.mapper.admin;


import com.github.pagehelper.Page;
import com.material.annotation.AutoFill;
import com.material.dto.admin.MaterialPageQueryDTO;
import com.material.entity.Material;
import com.material.enumeration.OperationType;
import com.material.vo.admin.MaterialVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MaterialMapper {

    /**
     * 物资添加
     * @param material
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Material material);

    /**
     * 物资分页查询,可以根据name、categoryId、status查找
     * @param materialPageQueryDTO
     * @return
     */
    Page<MaterialVO> pageQuery(MaterialPageQueryDTO materialPageQueryDTO);

    /**
     * 根据id获取物资信息
     * @param id
     * @return
     */
    @Select("select * from material where id = #{id}")
    Material getById(Long id);

    /**
     * 根据id删除物资
     * @param id
     */
    @Delete("delete from material where id = #{id}")
    void deleteById(Long id);

    /**
     * 根据物资id动态修改物资数据
     * @param material
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Material material);

    /**
     * 动态条件查询已启用的物资信息
     * @param material
     * @return
     */
    List<Material> list(Material material);

    /**
     * 根据套餐id查询菜品
     * @param setmealId
     * @return
     */
    @Select("select a.* from material a left join setmeal_material b on a.id = b.material_id where b.setmeal_id = #{setmealId}")
    List<Material> getBySetmealId(Long setmealId);
}
