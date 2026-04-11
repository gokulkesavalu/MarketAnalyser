package co.uk.marketanalyser.feature.exchangerate.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ExchangeRateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exchangeRate: ExchangeRateEntity)

    @Query("SELECT * FROM exchange_rates WHERE id = :id ")
    suspend fun getExchangeRate(id: String): ExchangeRateEntity?
}