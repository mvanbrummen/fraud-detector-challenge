package io.mvanbrummen.service;


import io.mvanbrummen.model.CreditCardTransaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FraudDetectorTest {
    FraudDetector fraudDetector = new FraudDetector();

    @Test
    public void shouldReturnOneWhenTransactionsOverThreshold() {
        var result = fraudDetector.getFraudulentCreditCardHashes(List.of(
                        new CreditCardTransaction(
                                "TEST_CREDIT_CARD",
                                LocalDateTime.parse("2014-04-07T11:18:51"),
                                1001.0
                        )
                ),
                1000.0);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo("TEST_CREDIT_CARD");
    }

    @Test
    public void shouldReturnOneWhenTransactionsOverThresholdMultiple() {
        var result = fraudDetector.getFraudulentCreditCardHashes(List.of(
                        new CreditCardTransaction(
                                "TEST_CREDIT_CARD",
                                LocalDateTime.parse("2014-04-07T11:18:51"),
                                1001.0
                        ),
                        new CreditCardTransaction(
                                "TEST_CREDIT_CARD2",
                                LocalDateTime.parse("2014-04-07T11:18:51"),
                                343.0
                        ),
                        new CreditCardTransaction(
                                "TEST_CREDIT_CARD2",
                                LocalDateTime.parse("2014-04-07T14:18:51"),
                                943.0
                        )
                ),
                1000.0);

        assertThat(result).hasSize(2).contains("TEST_CREDIT_CARD", "TEST_CREDIT_CARD2");
    }

    @Test
    public void shouldReturnEmptyWhenTransactionsUnderThreshold() {
        var result = fraudDetector.getFraudulentCreditCardHashes(List.of(
                        new CreditCardTransaction(
                                "TEST_CREDIT_CARD",
                                LocalDateTime.parse("2014-04-07T11:18:51"),
                                100.0
                        )
                ),
                1000.0);

        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnEmptyWhenTransactionsOnDifferentDays() {
        var result = fraudDetector.getFraudulentCreditCardHashes(List.of(
                        new CreditCardTransaction(
                                "TEST_CREDIT_CARD",
                                LocalDateTime.parse("2014-04-07T11:18:51"),
                                100.0
                        ),
                        new CreditCardTransaction(
                                "TEST_CREDIT_CARD",
                                LocalDateTime.parse("2014-04-08T11:18:51"),
                                900.0
                        )
                ),
                1000.0);

        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnEmptyWhenTransactionsAreEmpty() {
        var result = fraudDetector.getFraudulentCreditCardHashes(List.of(),
                1000.0);

        assertThat(result).isEmpty();
    }
}