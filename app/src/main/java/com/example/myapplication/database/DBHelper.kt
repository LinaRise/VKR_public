package com.example.myapplication.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.myapplication.database.DBHelper.Companion.languagesCodes
import com.example.myapplication.database.TablesAndColumns.LanguageEntry
import com.example.myapplication.database.TablesAndColumns.SettEntry
import com.example.myapplication.database.TablesAndColumns.WordEntry
import com.example.myapplication.database.TablesAndColumns.StudyProgressEntry
import com.example.myapplication.database.TablesAndColumns.LanguageTranslationEntry


class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "wordsAndProgressDB.db"
        const val DATABASE_VERSION = 13


        private const val CREATE_TABLE_LANGUAGE =
            "CREATE TABLE ${LanguageEntry.TABLE_NAME} (${LanguageEntry.TABLE_NAME}${BaseColumns._ID} TEXT PRIMARY KEY);"

        private const val CREATE_TABLE_LANGUAGE_TRANSLATION =
            "CREATE TABLE ${LanguageTranslationEntry.TABLE_NAME} (${LanguageTranslationEntry.TABLE_NAME}${BaseColumns._ID} INTEGER PRIMARY KEY autoincrement,${LanguageTranslationEntry.COL_LANGUAGE_CODE} TEXT not null, ${LanguageTranslationEntry.COL_LANGUAGETR_CODE} TEXT not null, " +
                    "${LanguageTranslationEntry.COL_LANGUAGETR_IS_DEFAULT} INTEGER DEFAULT 0, ${LanguageTranslationEntry.COL_LANGUAGETR_TITLE} TEXT not null, " +
                    "FOREIGN KEY (${LanguageTranslationEntry.COL_LANGUAGE_CODE}) REFERENCES ${LanguageEntry.TABLE_NAME} (${LanguageEntry.TABLE_NAME}${BaseColumns._ID}) ON DELETE CASCADE);"

        private const val CREATE_TABLE_STUDY_PROGRESS =
            "CREATE TABLE ${StudyProgressEntry.TABLE_NAME} (${StudyProgressEntry.COL_DATE} TEXT PRIMARY KEY, ${StudyProgressEntry.COL_RIGHT_ANSWERS} INTEGER DEFAULT 0, ${StudyProgressEntry.COL_WRONG_ANSWERS} INTEGER DEFAULT 0);"

        private const val CREATE_TABLE_WORD =
            "CREATE TABLE ${WordEntry.TABLE_NAME}  (${WordEntry.TABLE_NAME}${BaseColumns._ID} INTEGER PRIMARY KEY autoincrement, ${WordEntry.COL_ORIGINAL_WORD} text not null, ${WordEntry.COL_TRANSLATED_WORD} TEXT, " +
                    "${WordEntry.COL_SET_ID} INTEGER not null, " +
                    "${WordEntry.COL_RECALL_POINT} INTEGER not null DEFAULT 0, " +
                    "FOREIGN KEY (${WordEntry.COL_SET_ID}) REFERENCES ${SettEntry.TABLE_NAME} (${SettEntry.TABLE_NAME}${BaseColumns._ID}) ON DELETE CASCADE);"

        private const val CREATE_TABLE_SET =
            "CREATE TABLE ${SettEntry.TABLE_NAME} ( ${SettEntry.TABLE_NAME}${BaseColumns._ID}  INTEGER PRIMARY KEY autoincrement, " +
                    "${SettEntry.COL_SET_TITLE} TEXT not null, ${SettEntry.COL_LANGUAGE_INPUT_ID} INTEGER not null, ${SettEntry.COL_LANGUAGE_OUTPUT_ID} INTEGER not null, ${SettEntry.COL_WORDS_AMOUNT} INTEGER not null, " +
                    "${SettEntry.COL_AUTO_SUGGEST} INTEGER not null DEFAULT 0, " +
                    "FOREIGN KEY (${SettEntry.COL_LANGUAGE_INPUT_ID}) REFERENCES ${LanguageEntry.TABLE_NAME} (${LanguageEntry.TABLE_NAME}${BaseColumns._ID}), " +
                    "FOREIGN KEY (${SettEntry.COL_LANGUAGE_OUTPUT_ID}) REFERENCES ${LanguageEntry.TABLE_NAME} (${LanguageEntry.TABLE_NAME}${BaseColumns._ID})," +
                    "UNIQUE (${SettEntry.COL_SET_TITLE} ,${SettEntry.COL_LANGUAGE_INPUT_ID},${SettEntry.COL_LANGUAGE_OUTPUT_ID}) );"

        val languagesCodes = listOf<String>(
            "af",
            "sq",
            "am",
            "ar",
            "hy",
            "az",
            "eu",
            "be",
            "bn",
            "bs",
            "bg",
            "ca",
            "ceb",
            "ny",
            "zh",
            "zh-TW",
            "co",
            "hr",
            "cs",
            "da",
            "nl",
            "en",
            "eo",
            "et",
            "tl",
            "fi",
            "fr",
            "fy",
            "gl",
            "ka",
            "de",
            "el",
            "gu",
            "ht",
            "ha",
            "haw",
            "he",
            "hi",
            "hmn",
            "hu",
            "is",
            "ig",
            "id",
            "ga",
            "it",
            "ja",
            "jw",
            "kn",
            "kk",
            "km",
            "rw",
            "ko",
            "ku",
            "ky",
            "lo",
            "la",
            "lv",
            "lt",
            "lb",
            "mk",
            "mg",
            "ms",
            "ml",
            "mt",
            "mi",
            "mr",
            "mn",
            "my",
            "ne",
            "no",
            "or",
            "ps",
            "fa",
            "pl",
            "pt",
            "pa",
            "ro",
            "ru",
            "sm",
            "gd",
            "sr",
            "st",
            "sn",
            "sd",
            "si",
            "sk",
            "sl",
            "so",
            "es",
            "su",
            "sw",
            "sv",
            "tg",
            "ta",
            "tt",
            "te",
            "th",
            "tr",
            "tk",
            "uk",
            "ur",
            "ug",
            "uz",
            "vi",
            "cy",
            "xh",
            "yi",
            "yo",
            "zu"
        )


        val languagesTrEN = HashMap<String, String>().apply {
            put("af", "Afrikaans")
            put("sq", "Albanian")
            put("am", "Amharic")
            put("ar", "Arabic")
            put("hy", "Armenian")
            put("az", "Azerbaijani")
            put("eu", "Basque")
            put("be", "Belarusian")
            put("bn", "Bengali")
            put("bs", "Bosnian")
            put("bg", "Bulgarian")
            put("ca", "Catalan")
            put("ceb", "Cebuano")
            put("ny", "Chichewa")
            put("zh", "Chinese (Simplified)")
            put("zh-TW", "Chinese (Traditional)")
            put("co", "Corsican")
            put("hr", "Croatian")
            put("cs", "Czech")
            put("da", "Danish")
            put("nl", "Dutch")
            put("en", "English")
            put("eo", "Esperanto")
            put("et", "Estonian")
            put("tl", "Filipino")
            put("fi", "Finnish")
            put("fr", "French")
            put("fy", "Frisian")
            put("gl", "Galician")
            put("ka", "Georgian")
            put("de", "German")
            put("el", "Greek")
            put("gu", "Gujarati")
            put("ht", "HaitianCreole")
            put("ha", "Hausa")
            put("haw", "Hawaiian")
            put("he", "Hebrew")
            put("hi", "Hindi")
            put("hmn", "Hmong")
            put("hu", "Hungarian")
            put("is", "Icelandic")
            put("ig", "Igbo")
            put("id", "Indonesian")
            put("ga", "Irish")
            put("it", "Italian")
            put("ja", "Japanese")
            put("jw", "Javanese")
            put("kn", "Kannada")
            put("kk", "Kazakh")
            put("km", "Khmer")
            put("rw", "Kinyarwanda")
            put("ko", "Korean")
            put("ku", "Kurdish (Kurmanji)")
            put("ky", "Kyrgyz")
            put("lo", "Lao")
            put("la", "Latin")
            put("lv", "Latvian")
            put("lt", "Lithuanian")
            put("lb", "Luxembourgish")
            put("mk", "Macedonian")
            put("mg", "Malagasy")
            put("ms", "Malay")
            put("ml", "Malayalam")
            put("mt", "Maltese")
            put("mi", "Maori")
            put("mr", "Marathi")
            put("mn", "Mongolian")
            put("my", "Myanmar (Burmese)")
            put("ne", "Nepali")
            put("no", "Norwegian")
            put("or", "Odia (Oriya)")
            put("ps", "Pashto")
            put("fa", "Persian")
            put("pl", "Polish")
            put("pt", "Portuguese")
            put("pa", "Punjabi")
            put("ro", "Romanian")
            put("ru", "Russian")
            put("sm", "Samoan")
            put("gd", "ScotsGaelic")
            put("sr", "Serbian")
            put("st", "Sesotho")
            put("sn", "Shona")
            put("sd", "Sindhi")
            put("si", "Sinhala")
            put("sk", "Slovak")
            put("sl", "Slovenian")
            put("so", "Somali")
            put("es", "Spanish")
            put("su", "Sundanese")
            put("sw", "Swahili")
            put("sv", "Swedish")
            put("tg", "Tajik")
            put("ta", "Tamil")
            put("tt", "Tatar")
            put("te", "Telugu")
            put("th", "Thai")
            put("tr", "Turkish")
            put("tk", "Turkmen")
            put("uk", "Ukrainian")
            put("ur", "Urdu")
            put("ug", "Uyghur")
            put("uz", "Uzbek")
            put("vi", "Vietnamese")
            put("cy", "Welsh")
            put("xh", "Xhosa")
            put("yi", "Yiddish")
            put("yo", "Yoruba")
            put("zu", "Zulu")
        }

        val languagesTrRU = HashMap<String, String>().apply {
            put("af", "Африкаанс")
            put("sq", "Албанский")
            put("am", "Амхарский")
            put("ar", "Арабский")
            put("hy", "Армянский")
            put("az", "Азербайджанский")
            put("eu", "Баскский")
            put("be", "Белорусский")
            put("bn", "Бенгальский")
            put("bs", "Боснийский")
            put("bg", "Болгарский")
            put("ca", "Каталонский")
            put("ceb", "Себуано")
            put("ny", "Чичева")
            put("zh", "Китайский упр")
            put("zh-TW", "Китайский тр")
            put("co", "Корсиканский")
            put("hr", "Хорватский")
            put("cs", "Чешский")
            put("da", "Датский")
            put("nl", "Нидерландский")
            put("en", "Английский")
            put("eo", "Эсперанто")
            put("et", "Эстонский")
            put("tl", "Филиппинский")
            put("fi", "Финский")
            put("fr", "Французский")
            put("fy", "Фризский")
            put("gl", "Галицкий")
            put("ka", "Грузинский")
            put("de", "Немецкий")
            put("el", "Греческий")
            put("gu", "Гуджарати")
            put("ht", "ГаитянскийКреольский")
            put("ha", "Хауса")
            put("haw", "Гавайский")
            put("he", "Иврит")
            put("hi", "Хинди")
            put("hmn", "Хмонг")
            put("hu", "Венгерский ")
            put("is", "Исландский")
            put("ig", "Игбо")
            put("id", "Индонезийский")
            put("ga", "Ирландский")
            put("it", "Итальянский")
            put("ja", "Японский")
            put("jw", "Яванский")
            put("kn", "Каннада")
            put("kk", "Казахский")
            put("km", "Кхмерский")
            put("rw", "Киньяруанда")
            put("ko", "Корейский")
            put("ku", "Курдский (курманджи)")
            put("ky", "Киргизский")
            put("lo", "Лаосский")
            put("la", "Латинский")
            put("lv", "Латышский язык")
            put("lt", "Литовский")
            put("lb", "Люксембургский")
            put("mk", "Македонский")
            put("mg", "Малагасийский")
            put("ms", "Малайский")
            put("ml", "Малаялам")
            put("mt", "Мальтийский")
            put("mi", "Маори")
            put("mr", "Маратхи")
            put("mn", "Монгольский")
            put("my", "Мьянма (бирманский)")
            put("ne", "Непальский")
            put("no", "Норвежский")
            put("or", "Одиа (Ория)")
            put("ps", "Пушту")
            put("fa", "Персидский")
            put("pl", "Польский")
            put("pt", "Португальский")
            put("pa", "Пенджаби")
            put("ro", "Румынский")
            put("ru", "Русский")
            put("sm", "Самоанский")
            put("gd", "Шотландский")
            put("sr", "Сербский")
            put("st", "Сесото")
            put("sn", "Шона")
            put("sd", "Синдхи")
            put("si", "Сингальский")
            put("sk", "Словацкий")
            put("sl", "Словенский")
            put("so", "Сомалийский")
            put("es", "Испанский")
            put("su", "Суданский")
            put("sw", "Суахили")
            put("sv", "Шведский")
            put("tg", "Таджикский")
            put("ta", "Тамильский")
            put("tt", "Татарский")
            put("te", "Телугу")
            put("th", "Тайский")
            put("tr", "Турецкий")
            put("tk", "Туркменский")
            put("uk", "Украинец")
            put("ur", "Урду")
            put("ug", "Уйгурский")
            put("uz", "Узбекский")
            put("vi", "Вьетнамский")
            put("cy", "Валлийский")
            put("xh", "Коса")
            put("yi", "Идиш")
            put("yo", "Йоруба")
            put("zu", "Зулусский")
        }

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_LANGUAGE)
        db.execSQL(CREATE_TABLE_LANGUAGE_TRANSLATION)
        db.execSQL(CREATE_TABLE_WORD)
        db.execSQL(CREATE_TABLE_SET)
        db.execSQL(CREATE_TABLE_STUDY_PROGRESS)

        db.beginTransaction()
        try {
            val values = ContentValues()
            for (code in languagesCodes) {
                values.put("${LanguageEntry.TABLE_NAME}${BaseColumns._ID}", code)
                db.insert(TablesAndColumns.LanguageEntry.TABLE_NAME, null, values)
            }
            db.setTransactionSuccessful()

        } finally {
            db.endTransaction()
        }

        db.beginTransaction()
        try {
            val values = ContentValues()
            for (code in languagesTrEN) {
                values.put(LanguageTranslationEntry.COL_LANGUAGE_CODE, code.key)
                values.put(LanguageTranslationEntry.COL_LANGUAGETR_CODE, "en")
                values.put(LanguageTranslationEntry.COL_LANGUAGETR_TITLE, code.value)
                values.put(LanguageTranslationEntry.COL_LANGUAGETR_IS_DEFAULT, 1)
                db.insert(TablesAndColumns.LanguageTranslationEntry.TABLE_NAME, null, values)
            }
            db.setTransactionSuccessful()

        } finally {
            db.endTransaction()
        }

        db.beginTransaction()
        try {
            val values = ContentValues()
            for (code in languagesTrRU) {
                values.put(LanguageTranslationEntry.COL_LANGUAGE_CODE, code.key)
                values.put(LanguageTranslationEntry.COL_LANGUAGETR_CODE, "ru")
                values.put(LanguageTranslationEntry.COL_LANGUAGETR_TITLE, code.value)
                values.put(LanguageTranslationEntry.COL_LANGUAGETR_IS_DEFAULT, 0)
                db.insert(TablesAndColumns.LanguageTranslationEntry.TABLE_NAME, null, values)
            }
            db.setTransactionSuccessful()

        } finally {
            db.endTransaction()
        }
//        db.execSQL(CREATE_TABLE_SET_WORD)
//        db.execSQL(INSERT_DEFAULT_LANGUAGES)
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        db.execSQL("PRAGMA foreign_keys=ON")
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//        database.execSQL("DROP TABLE IF EXISTS ${SettWordEntry.TABLE_NAME};")
        database.execSQL("DROP TABLE IF EXISTS ${SettEntry.TABLE_NAME}")
        database.execSQL("DROP TABLE IF EXISTS ${WordEntry.TABLE_NAME};")
        database.execSQL("DROP TABLE IF EXISTS ${LanguageEntry.TABLE_NAME};")
        database.execSQL("DROP TABLE IF EXISTS ${StudyProgressEntry.TABLE_NAME};")
        database.execSQL("DROP TABLE IF EXISTS ${LanguageTranslationEntry.TABLE_NAME};")
        onCreate(database)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun clearDbAndRecreate() {
        clearDb()
        onCreate(writableDatabase)
    }

    fun clearDb() {
        writableDatabase.execSQL("DROP TABLE IF EXISTS ${SettEntry.TABLE_NAME}")
        writableDatabase.execSQL("DROP TABLE IF EXISTS ${WordEntry.TABLE_NAME};")
        writableDatabase.execSQL("DROP TABLE IF EXISTS ${LanguageEntry.TABLE_NAME};")
        writableDatabase.execSQL("DROP TABLE IF EXISTS ${StudyProgressEntry.TABLE_NAME};")
    }

}