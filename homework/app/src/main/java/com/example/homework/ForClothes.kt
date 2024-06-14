package com.example.homework

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Dao
interface ClothesDao {
    @Insert
    suspend fun insertClothes(clothes: Clothes)

    @Query("SELECT * FROM clothes")
    suspend fun getAllClothes(): List<Clothes>

    @Query("SELECT * FROM clothes WHERE cl_category = :category")
    suspend fun getClothesByCategory(category: Int): List<Clothes>

    @Query("DELETE FROM clothes WHERE cl_id = :clothesId")
    suspend fun deleteClothesById(clothesId: Int)

    @Query("SELECT COUNT(*) FROM clothes")  // db에 저장된 아이템들의 전체 수
    suspend fun getClothesCount(): Int
}

@Database(entities = [Clothes::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clothesDao(): ClothesDao
}


object DatabaseClient {
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        if (INSTANCE == null) {
            synchronized(AppDatabase::class) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "clothes_database"
                ).build()
            }
        }
        return INSTANCE!!
    }
}

