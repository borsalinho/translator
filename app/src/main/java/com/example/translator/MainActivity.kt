package com.example.translator


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Room

import androidx.room.RoomDatabase

import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

import retrofit2.http.Query


class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var translationDao: TranslationDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv = findViewById<TextView>(R.id.result)
        val button = findViewById<Button>(R.id.btnTranslate)
        val textEnter = findViewById<TextView>(R.id.enterText)
        val textHistory = findViewById<TextView>(R.id.historyText)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "translator-db"
        ).build()
        translationDao = db.translationDao()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dictionary.skyeng.ru")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val productApi = retrofit.create(ProductApi::class.java)

        lifecycleScope.launch {
            loadHistory()
        }

        button.setOnClickListener{

            val query = textEnter.text.toString().trim()

            if (query.isEmpty() || !query.matches(Regex("^[a-zA-Z]+$"))) {
                Toast.makeText(this@MainActivity, "Введите ОДНО английское слово", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            lifecycleScope.launch {

                try {
                    val result = productApi.getWord(query)

                    if (result.isNotEmpty()) {
                        val translations = result.flatMap { it.meanings }
                            .take(3)
                            .map { it.translation.text }
                            .joinToString(", ")
                        tv.text = translations

                        saveTranslation(query, translations)
                    } else {
                        tv.text = "Не нашлось перевода"
                    }
                    loadHistory()
                } catch (e: Exception) {
                    tv.text = "Ошибка: ${e.message}"
                }
            }
        }


    }

    private suspend fun saveTranslation(query: String, translation: String) {
        val translationEntity = TranslationEntity(query = query, translation = translation)
        translationDao.insert(translationEntity)
    }

    private suspend fun loadHistory() {
        val history = translationDao.getTranslations()
        val historyText = history
            .reversed()
            .joinToString("\n") { "${it.query}: ${it.translation}" }
        findViewById<TextView>(R.id.historyText).text = historyText
    }



}

@Entity(tableName = "translations")
data class TranslationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val query: String,
    val translation: String
)


@Dao
interface TranslationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(translation: TranslationEntity)

    @androidx.room.Query("SELECT * FROM translations")
    suspend fun getTranslations(): List<TranslationEntity>
}

@Database(entities = [TranslationEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun translationDao(): TranslationDao
}








interface ProductApi {
    @GET("/api/public/v1/words/search")
    suspend fun getWord(@Query("search") word: String): List<AllInfo>

}
@Serializable
data class Meaning(
    val id: Int,
    val partOfSpeechCode: String,
    val translation: Translation,
    val previewUrl: String?,
    val imageUrl: String?,
    val transcription: String,
    val soundUrl: String
)
@Serializable
data class Translation(
    val text: String,
    val note: String?
)
@Serializable
data class AllInfo(
    val id: Int,
    val text: String,
    val meanings: List<Meaning>
)
