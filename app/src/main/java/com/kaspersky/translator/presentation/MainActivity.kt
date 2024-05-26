package com.kaspersky.translator.presentation

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.kaspersky.data.storage.dao.TranslationDao
import com.kaspersky.data.storage.database.AppDatabase
import com.kaspersky.translator.R
import com.kaspersky.translator.databinding.ActivityMainBinding
import com.kaspersky.translator.view_model.MyViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_favorite
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}



//class MainActivity : AppCompatActivity() {
//
//    private lateinit var viewModel: MyViewModel
//    private lateinit var adapter: ItemAdapter
//    private lateinit var bottomNavigation: BottomNavigationView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val querry_result = findViewById<TextView>(R.id.result)
//        val button = findViewById<Button>(R.id.btnTranslate)
//        val textEnter = findViewById<TextView>(R.id.enterText)
//
//
//        viewModel = MyViewModel(application)
//        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
//
//        adapter = ItemAdapter(emptyList()) { translation ->
//            toggleFavorite(translation)
//        }
//
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter = adapter
//
//
//        lifecycleScope.launch {
//            viewModel.loadHistory()
//        }
//
//        viewModel.translations.observe(this) { translations ->
//            adapter.updateItems(translations)
//        }
//
//        button.setOnClickListener {
//
//            val query = textEnter.text.toString().trim()
//
//            if (query.isEmpty() || !query.matches(Regex("^[a-zA-Z]+$"))) {
//                Toast.makeText(this@MainActivity, "Введите ОДНО английское слово", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            lifecycleScope.launch {
//
//                try {
//                    val result = viewModel.productApi.getWord(query)
//
//                    if (result.isNotEmpty()) {
//                        val translations = result.flatMap { it.meanings }
//                            .take(3)
//                            .map { it.translation.text }
//                            .joinToString(", ")
//                        querry_result.text = translations
//
//                        viewModel.saveTranslation(query, translations)
//                        viewModel.loadHistory()
//                    } else {
//                        querry_result.text = "Не нашлось перевода"
//                    }
//
//                } catch (e: Exception) {
//                    querry_result.text = "Ошибка: ${e.message}"
//                }
//            }
//        }
//
//
//
//        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
//                val visibleItemCount = layoutManager.childCount
//                val totalItemCount = layoutManager.itemCount
//                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
//
//                if (firstVisibleItemPosition + visibleItemCount >= totalItemCount && firstVisibleItemPosition >= 0) {
//                    viewModel.currentIndex += viewModel.batchSize
//                    lifecycleScope.launch {
//                        viewModel.loadHistory()
//
//                    }
//                }
//            }
//        })
//
//
//
//
//    }
//
//    private fun toggleFavorite(translation: TranslationEntity) {
//        lifecycleScope.launch {
//            viewModel.updateFavorite(translation.id, !translation.favorite)
//        }
//    }
//
//
//}
//
//class MyViewModel(application: Application) : ViewModel() {
//
//    var currentIndex = 10
//    val batchSize = 10
//
//    private val _translations = MutableLiveData<List<TranslationEntity>>()
//    val translations: LiveData<List<TranslationEntity>>
//        get() = _translations
//
//    private val _favorites = MutableLiveData<List<TranslationEntity>>()
//    val favorites: LiveData<List<TranslationEntity>>
//        get() = _favorites
//
//    private val db: AppDatabase = Room.databaseBuilder(
//        application.applicationContext,
//        AppDatabase::class.java, "translator-db"
//    ).build()
//
//    private val translationDao: TranslationDao = db.translationDao()
//
//
//    val productApi: ProductApi = Retrofit.Builder()
//        .baseUrl("https://dictionary.skyeng.ru")
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//        .create(ProductApi::class.java)
//
//
//    suspend fun saveTranslation(query: String, translation: String) {
//        val translationEntity = TranslationEntity(query = query, translation = translation)
//        translationDao.insert(translationEntity)
//    }
//
//    suspend fun loadHistory() {
//        val history = translationDao.getTranslations(0, currentIndex)
//        _translations.postValue(history)
//    }
//
//    suspend fun loadFavorites() {
//        val favoriteList = translationDao.getFavoriteTranslations()
//        _favorites.postValue(favoriteList)
//    }
//
//    suspend fun updateFavorite(id: Int, isFavorite: Boolean) {
//        translationDao.updateFavorite(id, isFavorite)
//        loadHistory()
//    }
//}
//
//class ItemAdapter(
//    private var itemList: List<TranslationEntity>,
//    private val onFavoriteClick: (TranslationEntity) -> Unit
//) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
//
//    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val querry: TextView = itemView.findViewById(R.id.querry)
//        val translates: TextView = itemView.findViewById(R.id.translates)
//        val btnFavorite: Button = itemView.findViewById(R.id.btnAddFavorite)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
//        val view = LayoutInflater
//            .from(parent.context)
//            .inflate(R.layout.list_item, parent, false)
//        return ItemViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
//        val item = itemList[position]
//        holder.querry.text = item.query
//        holder.translates.text = item.translation
//        holder.btnFavorite.text = if (item.favorite) "favorite" else "add"
//        holder.btnFavorite.setOnClickListener {
//            onFavoriteClick(item)
//            item.favorite = !item.favorite
//            notifyItemChanged(position)
//        }
//    }
//
//    override fun getItemCount() = itemList.size
//
//    fun updateItems(newItems: List<TranslationEntity>) {
//        itemList = newItems
//        notifyDataSetChanged()
//    }
//}
//
//@Entity(tableName = "translations")
//data class TranslationEntity(
//    @PrimaryKey(autoGenerate = true) val id: Int = 0,
//    val query: String,
//    val translation: String,
//    var favorite : Boolean = false
//)
//
//@Dao
//interface TranslationDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(translation: TranslationEntity)
//
////    @androidx.room.Query("SELECT * FROM translations")
////    suspend fun getTranslations(): List<TranslationEntity>
//
//    @androidx.room.Query("SELECT * FROM translations ORDER BY id DESC LIMIT :batchSize OFFSET :index")
//    suspend fun getTranslations(index: Int, batchSize: Int): List<TranslationEntity>
//
//    @androidx.room.Query("SELECT * FROM translations WHERE favorite = 1 ORDER BY id DESC")
//    suspend fun getFavoriteTranslations(): List<TranslationEntity>
//
//    @androidx.room.Query("UPDATE translations SET favorite = :isFavorite WHERE id = :id")
//    suspend fun updateFavorite(id: Int, isFavorite: Boolean)
//
//
//}
//
//@Database(entities = [TranslationEntity::class], version = 2)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun translationDao(): TranslationDao
//}
//
//interface ProductApi {
//    @GET("/api/public/v1/words/search")
//    suspend fun getWord(@Query("search") word: String): List<AllInfo>
//}
//
//@Serializable
//data class Meaning(
//    val id: Int,
//    val partOfSpeechCode: String,
//    val translation: Translation,
//    val previewUrl: String?,
//    val imageUrl: String?,
//    val transcription: String,
//    val soundUrl: String
//)
//
//@Serializable
//data class Translation(
//    val text: String,
//    val note: String?
//)
//
//@Serializable
//data class AllInfo(
//    val id: Int,
//    val text: String,
//    val meanings: List<Meaning>
//)
