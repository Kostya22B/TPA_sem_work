package com.example.demo;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import io.camunda.zeebe.spring.common.exception.ZeebeBpmnError;

import java.util.HashMap;
import java.util.Map;

@Component
public class TranslationValidationWorker {

    private final static Logger LOG = LoggerFactory.getLogger(TranslationValidationWorker.class);

    @JobWorker(type = "validate_translation_request")
    public Map<String, Object> validateTranslationRequest(
            @Variable(name = "translationText") String text,
            @Variable(name = "translationLanguageInput") String sourceLanguage,
            @Variable(name = "translationLanguageOutput") String targetLanguage) {

        LOG.info("Validating translation request: text='{}', sourceLanguage='{}', targetLanguage='{}'",
                text, sourceLanguage, targetLanguage);

        Map<String, Object> variables = new HashMap<>();
        variables.put("isValidRequest", true);
        String validationErrorText = "";


        if (text.length() > 5000) {
            validationErrorText = "Validation failed: Text length exceeds 5000 characters.";
            LOG.error(validationErrorText);
            variables.put("validationErrorText", validationErrorText);
            variables.put("isValidRequest", false);
            }

        else if (sourceLanguage == null || targetLanguage == null || text == null || text.isEmpty()) {
            validationErrorText = "Validation failed: Text, Target or Source language is null.";
            LOG.error(validationErrorText);
            variables.put("validationErrorText", validationErrorText);
            variables.put("isValidRequest", false);
            }

        else if (sourceLanguage.equalsIgnoreCase(targetLanguage)) {
            validationErrorText = "Validation failed: Source and target languages are the same.";
            LOG.error(validationErrorText);
            variables.put("validationErrorText", validationErrorText);
            variables.put("isValidRequest", false);
            }

        else if (sourceLanguage.equalsIgnoreCase("EN") && targetLanguage.equalsIgnoreCase("en-US")) {
            validationErrorText = "Validation failed: Source language is EN and target language is en-US.";
            LOG.error(validationErrorText);
            variables.put("validationErrorText", validationErrorText);
            variables.put("isValidRequest", false);
        }

        // Все проверки пройдены
        LOG.info("Validation successful: text is valid, sourceLanguage={}, targetLanguage={}", sourceLanguage, targetLanguage);

        return variables;
    }
}