package org.take2.librarymanager.controller;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import org.take2.librarymanager.model.VClassSemesterReport;
import org.take2.librarymanager.service.IVClassSemesterReportService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class VClassSemesterReportController {

    private final IVClassSemesterReportService reportService;

    /**
     * GET /report/list
     * 返回所有报表数据供前端展示（例如在数据表格中查看）
     */
    @GetMapping("/list")
    public List<VClassSemesterReport> getReportList() {
        return reportService.getAllReports();
    }

    /**
     * GET /report/export
     * 导出报表数据为 Excel 文件供下载
     */
    @GetMapping("/export")
    public void exportReport(HttpServletResponse response) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("报表数据");
            // 创建标题行
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "学期", "班级ID", "班级名称", "借阅次数", "读者数量", "最后借阅时间"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // 查询所有报表数据
            List<VClassSemesterReport> reports = reportService.getAllReports();
            int rowIdx = 1;
            for (VClassSemesterReport report : reports) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(report.getId());
                row.createCell(1).setCellValue(report.getSemester());
                row.createCell(2).setCellValue(report.getClassId());
                row.createCell(3).setCellValue(report.getClassName());
                row.createCell(4).setCellValue(report.getBorrowTimes());
                row.createCell(5).setCellValue(report.getReaderCount());
                String lastBorrowTime = report.getLastBorrowTime() == null ? "" : report.getLastBorrowTime().toString();
                row.createCell(6).setCellValue(lastBorrowTime);
            }

            // 设置响应的内容类型和下载头信息
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=报表数据.xlsx");

            // 写入 Excel 到响应流中
            try (OutputStream os = response.getOutputStream()) {
                workbook.write(os);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
