package com.material.mapper.admin;


import com.github.pagehelper.Page;
import com.material.annotation.AutoFill;
import com.material.dto.admin.SetmealPageQueryDTO;
import com.material.entity.Setmeal;
import com.material.enumeration.OperationType;
import com.material.vo.admin.SetmealVO;
import com.material.vo.user.MaterialItemVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     *
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 根据id修改套餐
     *
     * @param setmeal
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 新增套餐，并将主键id赋值到setmeal中
     *
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 分页查询，可以根据name、categoryId、status查找
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * 根据id删除套餐
     * @param setmealId
     */
    @Delete("delete from setmeal where id = #{id}")
    void deleteById(Long setmealId);

    /**
     * 根据套餐id查询套餐和套餐菜品关系
     * @param id
     * @return
     */
    SetmealVO getByIdWithMaterial(Long id);

    /**
     * 动态条件(name、category_id、status)查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询物资选项
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_material sd left join material d on sd.material_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<MaterialItemVO> getMaterialItemBySetmealId(Long setmealId);


}
