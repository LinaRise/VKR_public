package com.example.myapplication.translation

import com.google.cloud.translate.Language
import com.google.cloud.translate.Translate

class TranslationUtils {

    companion object {

        /**
         * Метод для перевода текста
         * @param translate переменная библиотеки класса перевода
         * @param languageCodeAndTitle Map поддерживаемых языков перевода и их кодов
         * @param originalText текст для перевода
         * @param sourceLanguage язык с которого необходим перевод
         * @param targetLanguage язык на который нужно перевести
         * @return переведенны текст
         */
        fun translate(
            translate: Translate,
            languageCodeAndTitle: Map<String, String>,
            originalText: String,
            sourceLanguage: String,
            targetLanguage: String
        ): String {

            if (originalText != "") {
                if (sourceLanguage != "" && languageCodeAndTitle.containsKey(sourceLanguage)) {
                    return if (targetLanguage != "" && languageCodeAndTitle.containsKey(
                            targetLanguage
                        )
                    ) {
                        try {
                            val translation: com.google.cloud.translate.Translation =
                                translate.translate(
                                    originalText,
                                    Translate.TranslateOption.sourceLanguage(sourceLanguage),
                                    Translate.TranslateOption.targetLanguage(targetLanguage)
                                )
                            translation.translatedText;
                        } catch (e: Exception) {
                            ""
                        }

                    } else {
                        try {
                            val translation: com.google.cloud.translate.Translation =
                                translate.translate(
                                    originalText,
                                    Translate.TranslateOption.sourceLanguage(sourceLanguage),
                                    Translate.TranslateOption.targetLanguage("en")
                                )
                            translation.translatedText;
                        } catch (e: Exception) {
                            ""
                        }
                    }
                } else {
                    return if (targetLanguage != "" && languageCodeAndTitle.containsKey(
                            targetLanguage
                        )
                    ) {
                        try {
                            val translation: com.google.cloud.translate.Translation =
                                translate.translate(
                                    originalText,
                                    Translate.TranslateOption.targetLanguage(targetLanguage)
                                )
                            translation.translatedText;
                        } catch (e: Exception) {
                            ""
                        }
                    } else {
                        try {
                            val translation: com.google.cloud.translate.Translation =
                                translate.translate(
                                    originalText,
                                    Translate.TranslateOption.targetLanguage("en")
                                )
                            translation.translatedText;
                        } catch (e: Exception) {
                            ""
                        }
                    }
                }
            }
            return ""
        }
    }
}