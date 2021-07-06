package com.example.myapplication.database

import android.provider.BaseColumns

object TablesAndColumns {
    object LanguageEntry : BaseColumns {
        const val TABLE_NAME = "language"
    }

    object LanguageTranslationEntry : BaseColumns {
        const val TABLE_NAME = "languageTr"
        const val COL_LANGUAGE_CODE = "language_code"
        const val COL_LANGUAGETR_CODE = "languageTr_code"
        const val COL_LANGUAGETR_IS_DEFAULT = "is_default"
        const val COL_LANGUAGETR_TITLE = "languageTR_title"
    }

    object WordEntry : BaseColumns {
        const val TABLE_NAME = "word"
        const val COL_ORIGINAL_WORD = "original_word"
        const val COL_TRANSLATED_WORD = "translated_word"
        const val COL_RECALL_POINT = "recall_point"
        const val COL_SET_ID = "sett_id"
    }

    object SettEntry : BaseColumns {
        const val TABLE_NAME = "sett"
        const val COL_SET_TITLE = "sett_title"
        const val COL_LANGUAGE_INPUT_ID = "languageInput_id"
        const val COL_LANGUAGE_OUTPUT_ID = "languageOutput_id"
        const val COL_WORDS_AMOUNT = "words_amount"
        const val COL_AUTO_SUGGEST = "has_auto_suggest"
    }

    object StudyProgressEntry : BaseColumns {
        const val TABLE_NAME = "study_progress"
        const val COL_DATE = "study_progress_date"
        const val COL_RIGHT_ANSWERS = "right_answers_count"
        const val COL_WRONG_ANSWERS = "wrong_answers_count"
    }

}