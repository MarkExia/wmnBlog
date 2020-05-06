package com.wmn.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wmn.entity.Comments;
import com.wmn.mapper.CommentsMapper;
import com.wmn.service.ICommentsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wmn
 * @since 2020-03-09
 */
@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments> implements ICommentsService {

}
