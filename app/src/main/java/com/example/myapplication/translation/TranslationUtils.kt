package com.example.myapplication.translation

import com.google.cloud.translate.Language
import com.google.cloud.translate.Translate

class TranslationUtils {

    companion object {

        /*   private val languagesAccordance = hashMapOf(
               "English" to "en",
               "Russian" to "ru",
               "French" to "fr",
               "Czech" to "cs",
               "German" to "de"
           )
   */
        fun translate(
            translate: Translate,
            languageTitleAndCode: Map<String, String>,
            originalText: String,
            sourceLanguage: String,
            targetLanguage: String
        ): String {

            if (originalText != "") {
                if (sourceLanguage != "" && languageTitleAndCode.containsKey(sourceLanguage)) {
                    return if (targetLanguage != "" && languageTitleAndCode.containsKey(
                            targetLanguage
                        )
                    ) {
                        try {
                            val translation: com.google.cloud.translate.Translation =
                                translate.translate(
                                    originalText,
                                    Translate.TranslateOption.sourceLanguage(languageTitleAndCode[sourceLanguage]),
                                    Translate.TranslateOption.targetLanguage(languageTitleAndCode[targetLanguage])
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
                                    Translate.TranslateOption.sourceLanguage(languageTitleAndCode[sourceLanguage]),
                                    Translate.TranslateOption.targetLanguage(languageTitleAndCode["English"])
                                )
                            translation.translatedText;
                        } catch (e: Exception) {
                            ""
                        }
                    }
                } else {
                    return if (targetLanguage != "" && languageTitleAndCode.containsKey(
                            targetLanguage
                        )
                    ) {
                        try {
                            val translation: com.google.cloud.translate.Translation =
                                translate.translate(
                                    originalText,
                                    Translate.TranslateOption.targetLanguage(languageTitleAndCode[targetLanguage])
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
                                    Translate.TranslateOption.targetLanguage(languageTitleAndCode["English"])
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