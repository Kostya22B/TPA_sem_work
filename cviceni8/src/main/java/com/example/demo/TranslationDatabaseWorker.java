package com.example.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TranslationDatabaseWorker {

    private static final String JSON_FILE = "translations.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Метод для сохранения перевода в JSON
    @JobWorker(type = "save_translation")
    public void saveTranslation(
            @Variable(name = "userEmail") String userEmail,
            @Variable(name = "translationText") String originalText,
            @Variable(name = "translatedText") String translatedText,
            @Variable(name = "translationLanguageInput") String sourceLanguage,
            @Variable(name = "translationLanguageOutput") String targetLanguage,
            @Variable(name = "translationFormality") String formality
    ) {
        try {
            List<Map<String, Object>> translations = loadTranslations();

            Map<String, Object> newTranslation = Map.of(
                    "id", translations.size() + 1,
                    "userEmail", userEmail,
                    "translationText", originalText,
                    "translatedText", translatedText,
                    "translationLanguageInput", sourceLanguage,
                    "translationLanguageOutput", targetLanguage,
                    "translationFormality", formality,
                    "timestamp", System.currentTimeMillis()
            );

            translations.add(newTranslation);

            saveTranslations(translations);

            System.out.println("Translation saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save translation", e);
        }
    }

    private List<Map<String, Object>> loadTranslations() throws IOException {
        File file = new File(JSON_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(file, new TypeReference<>() {});
    }

    private void saveTranslations(List<Map<String, Object>> translations) throws IOException {
        objectMapper.writeValue(new File(JSON_FILE), translations);
    }
}
