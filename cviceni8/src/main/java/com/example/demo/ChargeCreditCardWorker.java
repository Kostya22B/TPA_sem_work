package com.example.demo;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ChargeCreditCardWorker {

    private final static Logger LOG = LoggerFactory.getLogger(ChargeCreditCardWorker.class);

//    @JobWorker(type = "charge-credit-card")
//    public Map<String, Double> chargeCreditCard(@Variable(name = "total") Double total) {
//        //20% surcharge for credit card payment
//        double amountCharged = total * 1.2;
//        LOG.info("charging credit card (including surcharge): {}", String.format("%,.2f", amountCharged));
//        return Map.of("amountCharged", amountCharged);
//    }
    @JobWorker(type = "validate_age")
    public Map<String, Object> validateAge(@Variable(name = "userAge") Integer userAge) {
        LOG.info("Validating age: {}", userAge);

        boolean isAgeValid = userAge != null && userAge >= 18;

        LOG.info("Age validation result: {}", isAgeValid);
        return Map.of("validatedAge", isAgeValid);
    }
    @JobWorker(type = "count_compound_interest")
    public Map<String, Object> countCompoundInterest(
            @Variable(name = "depositYears") Integer depositYears,
            @Variable(name = "depositAmount") Double depositAmount) {
        double interestRate = 77;
        double compoundInterestResult = depositAmount * Math.pow(1 + interestRate / 100, depositYears);

        LOG.info("User's compound interest result: {}", compoundInterestResult);

        return Map.of("compoundInterestResult", compoundInterestResult);
    }
    @JobWorker(type = "count_simple_interest")
    public Map<String, Object> countSimpleInterest(
            @Variable(name = "depositYears") Integer depositYears,
            @Variable(name = "depositAmount") Double depositAmount) {
        double interestRate = 0.2;
        double simpleInterestResult = depositAmount + (depositAmount * (interestRate / 100) * depositYears);

        LOG.info("User's simple interest result: {}", simpleInterestResult);

        return Map.of("simpleInterestResult", simpleInterestResult);
    }

}