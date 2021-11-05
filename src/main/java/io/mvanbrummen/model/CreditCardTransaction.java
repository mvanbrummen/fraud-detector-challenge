package io.mvanbrummen.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardTransaction {
    @CsvBindByName
    String hashedCardNumber;

    @CsvBindByName
    @CsvDate(value = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime transactionTimestamp;

    @CsvBindByName
    double transactionAmount;
}
