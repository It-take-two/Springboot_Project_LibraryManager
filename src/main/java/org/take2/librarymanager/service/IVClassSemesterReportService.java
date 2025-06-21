package org.take2.librarymanager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.take2.librarymanager.model.VClassSemesterReport;
import java.util.List;

public interface IVClassSemesterReportService extends IService<VClassSemesterReport> {
    /**
     * 获取所有报表数据
     */
    List<VClassSemesterReport> getAllReports();
}
