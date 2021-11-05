package io.mvanbrummen.service;

import io.mvanbrummen.model.CreditCardTransaction;

import java.util.List;

public interface CreditCardFraudDetector {

    List<String> getFraudulentCreditCardHashes(List<CreditCardTransaction> creditCardTransactions, double priceThreshold);

}
