package com.wmn.VO;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AlbumsVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    @TableField("class_id")
    private Integer class_id;

    @TableField("path")
    private String path;


    @TableField("album_name")
    private String album_name;

}
