package kg.kstu.excel.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class ReportDailyModel {
    Integer id;
    String deviceCode;
    Timestamp operDateTime;
    String curr;
    Double amnt;
    String cardNumber;
}
