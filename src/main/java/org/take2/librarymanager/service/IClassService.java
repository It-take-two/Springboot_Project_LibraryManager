package org.take2.librarymanager.service;

import org.take2.librarymanager.model.Class;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IClassService extends IService<Class> {
    Class getClassById(Long classId);
}
