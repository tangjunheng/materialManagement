package com.material.mapper.admin;


import com.material.entity.SetmealMaterial;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMaterialMapper {

    /**
     * 根据菜品id查询对应的套餐id
     *
     * @param materialIds
     * @return
     */
    //select setmeal_id from setmeal_material where dish_id in (1,2,3,4)
    List<Long> getSetmealIdsByMaterialIds(List<Long> materialIds);

    void insertBatch(List<SetmealMaterial> setmealMaterials);

    /**
     * 根据套餐id删除所有和该套餐相关的关联数据
     * @param setmealId
     */
    @Delete("delete from setmeal_material where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long setmealId);

    /**
     * 根据套餐id查询所有套餐和菜品的关联关系
     * @param setmealId
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealMaterial> getBySetmealId(Long setmealId);
}
