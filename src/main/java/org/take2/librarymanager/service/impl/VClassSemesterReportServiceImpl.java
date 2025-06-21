package org.take2.librarymanager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.take2.librarymanager.mapper.VClassSemesterReportMapper;
import org.take2.librarymanager.model.VClassSemesterReport;
import org.take2.librarymanager.service.IVClassSemesterReportService;
import java.util.List;

@Service
public class VClassSemesterReportServiceImpl extends ServiceImpl<VClassSemesterReportMapper, VClassSemesterReport>
        implements IVClassSemesterReportService {

    @Override
    public List<VClassSemesterReport> getAllReports() {
        return this.list();
    }
}
