package com.wmn.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wmn.entity.Albums;
import com.wmn.mapper.AlbumsMapper;
import com.wmn.service.IAlbumsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wmn
 * @since 2020-03-13
 */
@Service
public class AlbumsServiceImpl extends ServiceImpl<AlbumsMapper, Albums> implements IAlbumsService {

}
