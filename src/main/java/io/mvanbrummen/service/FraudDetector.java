package io.mvanbrummen.service;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import io.mvanbrummen.model.CreditCardTransaction;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FraudDetector implements CreditCardFraudDetector {
    private static final int DEFAULT_WINDOW_HOURS = 24;

    public List<String> getFraudulentCreditCardHashes(final List<CreditCardTransaction> creditCardTransactions, final double priceThreshold) {
        var groupedTransactions = new HashMap<String, List<GroupedTransaction>>();
        for (CreditCardTransaction creditCardTransaction : creditCardTransactions) {
            var creditCardNumberHash = creditCardTransaction.getHashedCardNumber();

            if (!groupedTransactions.containsKey(creditCardNumberHash)) {
                groupedTransactions.put(creditCardTransaction.getHashedCardNumber(),
                        Lists.newArrayList(
                                new GroupedTransaction(creditCardTransaction.getTransactionTimestamp(),
                                        creditCardTransaction.getTransactionAmount())
                        )
                );
            }

            // get the last grouped transaction since the CC transactions are ordered
            var groupedTransaction = Iterables.getLast(groupedTransactions.get(creditCardNumberHash));

            // check if the transactions timestamp is within this window
            if (creditCardTransaction.getTransactionTimestamp().isBefore(groupedTransaction.start.plusHours(DEFAULT_WINDOW_HOURS))) {
                groupedTransaction.total += creditCardTransaction.getTransactionAmount();
            } else {
                groupedTransactions.get(creditCardNumberHash).add(
                        new GroupedTransaction(creditCardTransaction.getTransactionTimestamp(),
                                creditCardTransaction.getTransactionAmount())
                );
            }
        }

        return getCreditCardHashesAboveThreshold(priceThreshold, groupedTransactions);
    }

    private List<String> getCreditCardHashesAboveThreshold(double priceThreshold, Map<String, List<GroupedTransaction>> groupedTransactions) {
        var fraudulentCreditHashes = new ArrayList<String>();
        groupedTransactions.forEach((key, value) -> value.stream()
                .filter(it -> it.total >= priceThreshold)
                .findFirst()
                .ifPresent(i -> fraudulentCreditHashes.add(key)));
        return fraudulentCreditHashes;
    }

    @Data
    @Builder
    private static class GroupedTransaction {
        private LocalDateTime start;
        private double total;
    }
}
