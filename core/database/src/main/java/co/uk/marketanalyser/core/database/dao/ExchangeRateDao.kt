package co.uk.marketanalyser.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.uk.marketanalyser.core.database.entity.ExchangeRateEntity

@Dao
interface ExchangeRateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exchangeRate: ExchangeRateEntity)

    @Query("SELECT * FROM exchange_rates WHERE id = :id ")
    suspend fun getExchangeRate(id: String): ExchangeRateEntity?
}