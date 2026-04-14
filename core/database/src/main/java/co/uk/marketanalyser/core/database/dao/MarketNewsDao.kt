package co.uk.marketanalyser.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.uk.marketanalyser.core.database.entity.MarketNewsEntity

/**
 * Data Access Object for the market_news table.
 *
 * Provides methods for persisting and retrieving market news articles.
 */
@Dao
interface MarketNewsDao {
    /**
     * Inserts or updates a market news article record.
     *
     * @param marketNews The market news entity to save.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(marketNews: MarketNewsEntity)

    /**
     * Retrieves all cached market news articles.
     *
     * @return A list of [MarketNewsEntity] articles.
     */
    @Query("SELECT * FROM market_news where tickerSentiments != '[]'")
    suspend fun getMarketNews(): List<MarketNewsEntity>
}