package com.example.demo;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import io.camunda.zeebe.spring.common.exception.ZeebeBpmnError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ChargeCreditCardWorker {

    private final static Logger LOG = LoggerFactory.getLogger(ChargeCreditCardWorker.class);
    @JobWorker(type = "validate_age")
    public Map<String, Object> validateAge(@Variable(name = "userAge") Integer userAge) {
        LOG.info("Validating age: {}", userAge);

        boolean isAgeValid = userAge != null && userAge >= 18;
        if(!isAgeValid) {
            throw new ZeebeBpmnError("Invalid user",
                    "Age must be greater than 18 or equal to it",
                    null);
        }
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
    @JobWorker(type = "validate_deposit")
    public Map<String, Object> validateDeposit(
            @Variable(name = "depositAmount") Double depositAmount,
            @Variable(name = "depositYears") Double depositYears
            ) {
        if (depositAmount == null || depositAmount <= 0) {
            throw new ZeebeBpmnError("InvalidDeposit",
                    "Deposit amount must be greater than 0",
                    null);
        }
        if (depositYears > 3 ||  depositYears < 15) {
            throw new ZeebeBpmnError("InvalidDeposit",
                    "Deposit years must be more than 3 and less than 15",
                    null);
        }
        System.out.println("Amount " + depositAmount + "Years " + depositYears);
        return Map.of("isDepositValid", true);
    }


}