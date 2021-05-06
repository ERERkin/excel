package kg.kstu.excel.service.Impl;

import kg.kstu.excel.model.ReportAnswerModel;
import kg.kstu.excel.model.ReportDailyModel;
import kg.kstu.excel.service.ExcelService;
import kg.kstu.excel.service.ReportAnswerService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {
    @Autowired
    ReportAnswerService reportAnswerService;
    @Value("${excel_file_save_package}")
    String excelCreate;

    @Value("${excel_file_read_package}")
    String excelRead;

    @Override
    public List<ReportDailyModel> readExcel(String date) {
        File file = new File(excelRead + date + ".xlsx");
        List<ReportDailyModel> reportDailyModelList = new ArrayList<>();
        try (InputStream in = new FileInputStream(file))
        {
            Workbook workbook = new XSSFWorkbook(in);
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            System.out.println("Kuku0");
            Iterator<Row> rows = sheet.iterator();
            int rowNumber = 0;
            System.out.println(rows);
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0 ) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                //BlackList blackList = new BlackList();
                Integer id = 0;
                String deviceCode = "";
                Timestamp operDateTime = new Timestamp(0);
                String curr = "";
                Double amnt = 0.;
                String cardNumber = "";
                int cellIndex = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    String val = formatter.formatCellValue(sheet.getRow(rowNumber).getCell(cellIndex));
                    System.out.print(val + " ");

                    if(cellIndex == 0){
                        id = Integer.parseInt(val);
                    }
                    else if(cellIndex == 1){
                        deviceCode = val;
                    }
                    else if(cellIndex == 2){
                        try {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                            Date parsedDate = dateFormat.parse(val);
                            operDateTime = new java.sql.Timestamp(parsedDate.getTime());
                        } catch(Exception e) {
                            e.getStackTrace();
                        }
                    }
                    else if(cellIndex == 3){
                        curr = val;
                    }
                    else if(cellIndex == 4){
                        amnt = Double.parseDouble(val);
                    }
                    else if(cellIndex == 5){
                        cardNumber = val;
                    }

                    cellIndex++;
                }
                reportDailyModelList.add(ReportDailyModel.builder()
                        .id(id)
                        .deviceCode(deviceCode)
                        .operDateTime(operDateTime)
                        .curr(curr)
                        .amnt(amnt)
                        .cardNumber(cardNumber)
                        .build());
                rowNumber++;
                System.out.println();
            }
            workbook.close();
            System.out.println("Kuku1");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Kuku");
        System.out.println(reportDailyModelList);
        return reportDailyModelList;
    }

    @Override
    public List<ReportDailyModel> readExcelByDate(Date startDate, Date endDate) {
        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        System.out.println(startDate + " " + endDate);
        List<ReportDailyModel> reportDailyModelList = new ArrayList<>();
        for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
            reportDailyModelList.addAll(readExcel(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
            System.out.println(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        }
        System.out.println("List");
        System.out.println(start.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        System.out.println(readExcel(start.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
        System.out.println(reportDailyModelList);
        return reportDailyModelList;
    }

    @Override
    public void createFile(String startDate, String endDate) {

        try {
            String filename = excelCreate + startDate + "_" + endDate + ".xls" ;
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("FirstSheet");
            //sheet.setDefaultColumnWidth(35);
            sheet.setColumnWidth(0, 1500 * 7);
            sheet.setColumnWidth(1, 1500 * 5);
            sheet.setColumnWidth(2, 1500 * 5);
            sheet.setColumnWidth(3, 1500 * 5);
            sheet.setColumnWidth(4, 1500 * 5);
            sheet.setColumnWidth(5, 1500 * 5);
            XSSFRow row = sheet.createRow(0);
            row.createCell(0).setCellValue("Card Number");
            row.createCell(1).setCellValue("Convert to KGS");
            row.createCell(2).setCellValue("KGS");
            row.createCell(3).setCellValue("KZT");
            row.createCell(4).setCellValue("USD");
            row.createCell(5).setCellValue("EUR");
            int i = 1;
            List<ReportAnswerModel> reportAnswerModels = reportAnswerService.getReportAnswerList(startDate, endDate);
            System.out.println(reportAnswerModels);
            for(ReportAnswerModel reportAnswerModel : reportAnswerModels){
                row = sheet.createRow(i++);
                row.createCell(0).setCellValue(reportAnswerModel.getCardNumber());
                row.createCell(1).setCellValue(reportAnswerModel.getAmountConvert());
                row.createCell(2).setCellValue(reportAnswerModel.getAmountKGS() == null ? 0 : reportAnswerModel.getAmountKGS());
                row.createCell(3).setCellValue(reportAnswerModel.getAmountKZT() == null ? 0 : reportAnswerModel.getAmountKZT());
                row.createCell(4).setCellValue(reportAnswerModel.getAmountUSD() == null ? 0 : reportAnswerModel.getAmountUSD());
                row.createCell(5).setCellValue(reportAnswerModel.getAmountEUR() == null ? 0 : reportAnswerModel.getAmountEUR());
            }

            FileOutputStream fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);
            fileOut.close();
            System.out.println("Your excel file has been generated!");

        } catch ( Exception ex ) {
            System.out.println(ex);
        }
    }
}
