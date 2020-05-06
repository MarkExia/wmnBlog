package com.wmn.service.impl;

import com.wmn.entity.Article;
import com.wmn.mapper.ArticleMapper;
import com.wmn.service.IArticleService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wmn
 * @since 2019-09-25
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

}
