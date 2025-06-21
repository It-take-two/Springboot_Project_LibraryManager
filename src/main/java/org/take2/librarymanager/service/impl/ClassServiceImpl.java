package org.take2.librarymanager.service.impl;

import org.take2.librarymanager.model.Class;
import org.take2.librarymanager.mapper.ClassMapper;
import org.take2.librarymanager.service.IClassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements IClassService {
    @Override
    public Class getClassById(Long classId) {
        return baseMapper.selectById(classId);
    }
}
