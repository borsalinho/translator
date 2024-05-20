package com.example.translator


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)


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
            loadHistory(recyclerView)
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
                    loadHistory(recyclerView)
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

    private suspend fun loadHistory(recyclerView : RecyclerView) {
        val history = translationDao.getTranslations().reversed()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ItemAdapter(history)

    }



}
//------------------------------------------------


class ItemAdapter(private val itemList: List<TranslationEntity>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val querry: TextView = itemView.findViewById(R.id.querry)
        val translates: TextView = itemView.findViewById(R.id.translates)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.querry.text = item.query
        holder.translates.text = item.translation
    }

    override fun getItemCount() = itemList.size
}






//---------------------------------------------------

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




//-----------------------------------



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
