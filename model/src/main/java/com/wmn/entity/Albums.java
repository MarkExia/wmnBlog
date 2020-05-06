package com.wmn.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wmn
 * @since 2020-03-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("albums")
public class Albums implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("class_id")
    @JSONField(serialize = false)
    private Integer classId;

    @TableId(value = "path")
    private String path;

    @JSONField(name = "url")
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
