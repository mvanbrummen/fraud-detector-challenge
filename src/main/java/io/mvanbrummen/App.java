package io.mvanbrummen;

import com.opencsv.bean.CsvToBeanBuilder;
import io.mvanbrummen.model.CreditCardTransaction;
import io.mvanbrummen.service.FraudDetector;

import java.io.FileReader;

public class App {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: fraud-detector <input csv file> <prica threshold>");
            System.exit(1);
        }
        var csvFile = args[0];
        var priceThreshold = Double.valueOf(args[1]);

        var fraudDetector = new FraudDetector();

        try (FileReader reader = new FileReader(csvFile)) {
            var csvToBean = new CsvToBeanBuilder<CreditCardTransaction>(reader)
                    .withType(CreditCardTransaction.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            var creditCardTransactions = csvToBean.parse();

            var fraudulentCreditCardHashes = fraudDetector.getFraudulentCreditCardHashes(creditCardTransactions, priceThreshold);

            fraudulentCreditCardHashes.forEach(System.out::println);
        }
    }
}
