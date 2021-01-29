package com.example.myapplication.database

import android.provider.BaseColumns

object TablesAndColumns {
    // Table contents are grouped together in an anonymous object.
    object LanguageEntry : BaseColumns {
         const val TABLE_NAME  = "language"
         const val COL_LANGUAGE_TITLE = "language_title"
         const val COL_SUPPORTS_TRANSLATION = "supports_translation"
    }
    object WordEntry : BaseColumns {
         const val TABLE_NAME  = "word"
         const val COL_ORIGINAL_WORD = "original_word"
         const val COL_TRANSLATED_WORD = "translated_word"
    }
    object SettEntry : BaseColumns {
         const val TABLE_NAME  = "sett"
         const val COL_SET_TITLE = "sett_title"
         const val COL_LANGUAGE_INPUT_ID = "languageInput_id"
         const val COL_LANGUAGE_OUTPUT_ID = "languageOutput_id"
         const val COL_WORDS_AMOUNT = "words_amount"
    }
    object SettWordEntry : BaseColumns {
         const val TABLE_NAME  = "sett_word"
         const val COL_SET_ID = "sett_id"
         const val COL_WORD_ID = "word_id"
    }
}