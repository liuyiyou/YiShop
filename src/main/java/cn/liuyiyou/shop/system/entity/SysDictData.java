package cn.liuyiyou.shop.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 字典数据表
 * </p>
 *
 * @author liuyiyou.cn
 * @since 2018-08-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysDictData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字典编码
     */
    @TableId(value = "dict_code", type = IdType.AUTO)
    private Integer dictCode;
    /**
     * 字典排序
     */
    private Integer dictSort;
    /**
     * 字典标签
     */
    private String dictLabel;
    /**
     * 字典键值
     */
    private String dictValue;
    /**
     * 字典类型
     */
    private String dictType;
    /**
     * 样式属性
     */
    private String cssClass;
    /**
     * 是否默认（Y是 N否）
     */
    private String isDefault;
    /**
     * 状态（0正常 1停用）
     */
    private Integer status;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 备注
     */
    private String remark;


}
