package kg.kstu.excel.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class ReportAnswerModel {
    String cardNumber;
    Double amountConvert;
    Double amountKGS;
    Double amountKZT;
    Double amountUSD;
    Double amountEUR;
}
