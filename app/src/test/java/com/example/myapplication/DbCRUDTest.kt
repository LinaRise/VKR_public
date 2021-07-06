package com.example.myapplication

import android.os.Build.VERSION_CODES.LOLLIPOP
import com.example.myapplication.database.DBHelper
import com.example.myapplication.database.repo.language.LanguageRepo
import com.example.myapplication.database.repo.sett.SettRepo
import com.example.myapplication.database.repo.word.WordRepo
import com.example.myapplication.entity.Sett
import com.example.myapplication.entity.Word
import com.google.cloud.translate.Language
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(
    sdk = [LOLLIPOP],
    packageName = "com.elyeproj.simpledb"
)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class DbCRUDTest {

    lateinit var dbHelper: DBHelper
    lateinit var mWordRepo: WordRepo
    lateinit var mSetRepo: SettRepo
    lateinit var mLangRepo: LanguageRepo

    @Before
    fun setup() {
        dbHelper = DBHelper(RuntimeEnvironment.application)
        dbHelper.clearDbAndRecreate()
        mWordRepo = WordRepo(dbHelper)
        mSetRepo = SettRepo(dbHelper)
        mLangRepo = LanguageRepo(dbHelper)

    }

    @Test
    /**
     * Вставка
     */
    @Throws(Exception::class)
    fun testDbInsertion() {
        val language1 = com.example.myapplication.entity.Language(0, "Russianl")
        val language2 = com.example.myapplication.entity.Language(0, "English")

        val langId1 = mLangRepo.create(language1)
        val langId2 = mLangRepo.create(language2)

        val sett = Sett(0, "Test", langId1, langId2, 0, 0)
        val settId = mSetRepo.create(sett)

        val word = Word(0, "Тест2", "Test", 2, settId)
        val wordId = mWordRepo.create(word)
        assertEquals(1, langId1)
        assertEquals(2, langId2)
        assertEquals(1, wordId)
        assertEquals(1, settId)
    }


    @Test
    @Throws(Exception::class)
    fun testDbUpdate() {

        var language1 = com.example.myapplication.entity.Language(0, "Russianl")
        val language2 = com.example.myapplication.entity.Language(0, "English")

        var langId1 = mLangRepo.create(language1)
        val langId2 = mLangRepo.create(language2)

        var sett = Sett(0, "Test", langId1, langId2, 0, 0)
        var settId = mSetRepo.create(sett)

        var word = Word(0, "Тест2", "Test", 2, settId)
        var wordId = mWordRepo.create(word)
        // Given
        language1 = com.example.myapplication.entity.Language(1, "Russian")
        langId1 = mLangRepo.update(language1)

        sett = Sett(1, "Test", langId1, 2, 1, 0)
        settId = mSetRepo.update(sett)

        word = Word(1, "Тест", "Test", 2, settId)
        wordId = mWordRepo.update(word)
        assertEquals("Russian", mLangRepo.get(langId1)?.languageTitle)
        assertEquals(1, mSetRepo.get(settId)?.wordsAmount)
        assertEquals("Тест", mWordRepo.get(wordId)?.originalWord)
    }


    @Test
    @Throws(Exception::class)
    fun testDbDelete() {

        val language1 = com.example.myapplication.entity.Language(0, "Russian")
        val language2 = com.example.myapplication.entity.Language(0, "English")

        var langId1 = mLangRepo.create(language1)
        val langId2 = mLangRepo.create(language2)

        val sett = Sett(0, "Test", langId1, langId2, 0, 0)
        var settId = mSetRepo.create(sett)

        val word = Word(0, "Тест", "Test", 2, settId)
        val wordId = mWordRepo.delete(word)
        // Given
        langId1 = mLangRepo.delete(language1)

        settId = mSetRepo.delete(sett)

        assertEquals(com.example.myapplication.entity.Language(), mLangRepo.get(langId1))
        assertEquals(Sett(), mSetRepo.get(settId))
        assertEquals(Word(), mWordRepo.get(wordId))
    }


    @After
    fun tearDown() {
        dbHelper.clearDb()
    }
}