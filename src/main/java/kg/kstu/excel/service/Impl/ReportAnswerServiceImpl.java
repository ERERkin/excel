package kg.kstu.excel.service.Impl;

import kg.kstu.excel.model.CardNumberAndCurrencyPairModel;
import kg.kstu.excel.model.ReportAnswerModel;
import kg.kstu.excel.model.ReportDailyModel;
import kg.kstu.excel.service.ExcelService;
import kg.kstu.excel.service.ReportAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportAnswerServiceImpl implements ReportAnswerService {
    @Autowired
    ExcelService excelService;

    @Override
    public List<ReportAnswerModel> getReportAnswerList(String startDate, String endDate) {
        List<ReportDailyModel> reportDailies = new ArrayList<>();
        List<ReportAnswerModel> reportAnswerModels = new ArrayList<>();
        try {
            reportDailies.addAll(excelService.readExcelByDate(new SimpleDateFormat("dd-MM-yyyy").parse(startDate),
                    new SimpleDateFormat("dd-MM-yyyy").parse(endDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Map<CardNumberAndCurrencyPairModel, Double> reportDailyMap = new HashMap<>();

        for (ReportDailyModel r:
             reportDailies) {
            CardNumberAndCurrencyPairModel cardNumberAndCurrencyPairModel = new CardNumberAndCurrencyPairModel(r.getCurr(),r.getCardNumber());
            reportDailyMap.merge(cardNumberAndCurrencyPairModel, r.getAmnt(), Double::sum);
        }
        System.out.println("Map");
        System.out.println(reportDailyMap);
        System.out.println(reportDailies);

        for(Map.Entry<CardNumberAndCurrencyPairModel, Double> entry : reportDailyMap.entrySet()) {
            CardNumberAndCurrencyPairModel key = entry.getKey();
            Double value = entry.getValue();
            Double amountConvert = null;
            Double amountKGS = null;
            Double amountKZT = null;
            Double amountUSD = null;
            Double amountEUR = null;
            if(key.getCurr().equals("KGS")){
                amountKGS = value;
                amountConvert = amountKGS;
            }else if(key.getCurr().equals("KZT")){
                amountKZT = value;
                amountConvert = amountKZT * 0.25;
            }else if(key.getCurr().equals("USD")){
                amountUSD = value;
                amountConvert = amountUSD * 84;
            }else {
                amountEUR = value;
                amountConvert = amountEUR * 105;
            }
            reportAnswerModels.add(
                    ReportAnswerModel.builder()
                            .cardNumber(key.getCardNumber())
                            .amountConvert(amountConvert)
                            .amountKGS(amountKGS)
                            .amountKZT(amountKZT)
                            .amountUSD(amountUSD)
                            .amountEUR(amountEUR)
                            .build()
            );
        }
        System.out.println(reportAnswerModels);

        return reportAnswerModels;
    }
}
