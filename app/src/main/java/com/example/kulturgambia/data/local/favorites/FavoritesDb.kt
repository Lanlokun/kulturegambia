package com.example.kulturgambia.data.local.favorites


import android.content.Context
import androidx.room.*

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val cultureId: String
)

@Dao
interface FavoritesDao {
    @Query("SELECT cultureId FROM favorites")
    suspend fun getAllIds(): List<String>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE cultureId = :id)")
    suspend fun isFavorite(id: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(entity: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE cultureId = :id")
    suspend fun remove(id: String)
}

@Database(entities = [FavoriteEntity::class], version = 1, exportSchema = false)
abstract class FavoritesDatabase : RoomDatabase() {
    abstract fun dao(): FavoritesDao

    companion object {
        @Volatile private var INSTANCE: FavoritesDatabase? = null

        fun get(context: Context): FavoritesDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    FavoritesDatabase::class.java,
                    "kultur_gambia_favorites.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
