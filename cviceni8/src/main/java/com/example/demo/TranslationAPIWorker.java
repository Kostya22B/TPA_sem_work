package com.example.demo;

import com.deepl.api.*;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import io.camunda.zeebe.spring.common.exception.ZeebeBpmnError;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TranslationAPIWorker {

    private final String authKey = "597a4738-fa95-4a5f-93dd-53a96fa1089e:fx"; // Укажите ваш API-ключ
    private final Translator translator = new Translator(authKey);

    @JobWorker(type = "Translation_worker")
    public Map<String, String> translate(
            @Variable(name = "translationText") String text,
            @Variable(name = "translationLanguageInput") String input,
            @Variable(name = "translationLanguageOutput") String output,
            @Variable(name = "translationFormality") String formality
    ) throws DeepLException, InterruptedException {

        try {
            // Проверяем, поддерживаются ли языки
            input = validateLanguage(input);
            output = validateLanguage(output);

            // Настройки формальности
            TextTranslationOptions options = new TextTranslationOptions();
            if ("Formal".equalsIgnoreCase(formality)) {
                options.setFormality(Formality.PreferMore);
            } else if ("Informal".equalsIgnoreCase(formality)) {
                options.setFormality(Formality.PreferLess);
            }

            // Перевод текста
            TextResult result = translator.translateText(text, input, output, options);

            // Возвращаем результат
            Map<String, String> translation = new HashMap<>();
            translation.put("translatedText", result.getText());
            return translation;

        } catch (DeepLException | InterruptedException e) {
            e.printStackTrace();
            throw new ZeebeBpmnError("TRANSLATION_FAILED", "Failed to translate by DeepL", null);
        }
    }

    // Метод для проверки и валидации языка
    private String validateLanguage(String language) {
        // Список поддерживаемых языков DeepL
        String[] supportedLanguages = {"BG", "CS", "DA", "DE", "EL", "EN", "en-US", "ES", "ET", "FI", "FR", "HU", "IT", "JA",
                "LT", "LV", "NL", "PL", "PT", "RO", "RU", "SK", "SL", "SV", "ZH"};

        for (String supportedLanguage : supportedLanguages) {
            if (supportedLanguage.equalsIgnoreCase(language)) {
                return language.toUpperCase();
            }
        }

        throw new IllegalArgumentException("Unsupported language: " + language);
    }
}
