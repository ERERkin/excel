package kg.kstu.excel.service;

import kg.kstu.excel.model.ReportAnswerModel;

import java.util.List;

public interface ReportAnswerService {
    List<ReportAnswerModel> getReportAnswerList(String startDate, String endDate);
}
