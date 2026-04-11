package co.uk.marketanalyser.feature.exchangerate.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ExchangeRateEntity::class], version = 1)
abstract class ExchangeRateDatabase : RoomDatabase() {
    abstract fun exchangeRateDao(): ExchangeRateDao
}