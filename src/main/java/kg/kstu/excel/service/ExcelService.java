package kg.kstu.excel.service;

import kg.kstu.excel.model.ReportDailyModel;

import java.util.Date;
import java.util.List;

public interface ExcelService {
    List<ReportDailyModel> readExcel(String date);

    List<ReportDailyModel> readExcelByDate(Date start, Date end);

    void createFile(String startDate, String endDate);
}
