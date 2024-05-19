package com.example.translator

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope

import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

import retrofit2.http.Query


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv = findViewById<TextView>(R.id.result)
        val button = findViewById<Button>(R.id.btnTranslate)
        val textEnter = findViewById<TextView>(R.id.enterText)
        val textHistory = findViewById<TextView>(R.id.historyText)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dictionary.skyeng.ru")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val productApi = retrofit.create(ProductApi::class.java)

        button.setOnClickListener{

            if (textEnter.text.isEmpty()) {
                Toast.makeText(this@MainActivity, "Введите английское слово", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val result = productApi.getWord(textEnter.text.toString())

                    if (result.isNotEmpty()) {
                        val translations = result.flatMap { it.meanings }
                            .take(3)
                            .map { it.translation.text }
                            .joinToString(", ")
                        tv.text = translations
                    } else {
                        tv.text = "Не нашлось перевода"
                    }
                } catch (e: Exception) {
                    tv.text = "Ошибка: ${e.message}"
                }
            }
        }


    }



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
