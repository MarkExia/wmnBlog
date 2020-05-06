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
public class UserCommentVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableField("user_id")
    private Integer user_id;

    @TableField("nickname")
    private String nickname;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date time;

    private String content;




}
