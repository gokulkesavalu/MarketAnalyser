package co.uk.marketanalyser.core.database.typeconverters

import androidx.room.TypeConverter
import co.uk.marketanalyser.core.database.entity.MarketNewsTickerSentimentEntity
import co.uk.marketanalyser.core.database.entity.MarketNewsTopicEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Room TypeConverters for the Market News feature.
 *
 * These converters handle the serialization and deserialization of complex types
 * (like Lists and nested objects) into JSON strings so they can be stored in
 * SQLite columns.
 */
class MarketNewsTypeConverters {
    private val gson = Gson()

    /**
     * Converts a list of strings to a JSON string.
     *
     * @param value The list of strings to convert.
     * @return A JSON string representation of the list.
     */
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }

    /**
     * Converts a JSON string to a list of strings.
     *
     * @param value The JSON string to convert.
     * @return A list of strings.
     */
    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    /**
     * Converts a list of [MarketNewsTopicEntity] to a JSON string.
     *
     * @param value The list of topics to convert.
     * @return A JSON string representation of the list.
     */
    @TypeConverter
    fun fromMarketNewsTopicEntityList(value: List<MarketNewsTopicEntity>): String {
        return gson.toJson(value)
    }

    /**
     * Converts a JSON string to a list of [MarketNewsTopicEntity].
     *
     * @param value The JSON string to convert.
     * @return A list of [MarketNewsTopicEntity].
     */
    @TypeConverter
    fun toMarketNewsTopicEntityList(value: String): List<MarketNewsTopicEntity> {
        val listType = object : TypeToken<List<MarketNewsTopicEntity>>() {}.type
        return gson.fromJson(value, listType)
    }

    /**
     * Converts a list of [MarketNewsTickerSentimentEntity] to a JSON string.
     *
     * @param value The list of ticker sentiments to convert.
     * @return A JSON string representation of the list.
     */
    @TypeConverter
    fun fromMarketNewsTickerSentimentEntityList(value: List<MarketNewsTickerSentimentEntity>): String {
        return gson.toJson(value)
    }

    /**
     * Converts a JSON string to a list of [MarketNewsTickerSentimentEntity].
     *
     * @param value The JSON string to convert.
     * @return A list of [MarketNewsTickerSentimentEntity].
     */
    @TypeConverter
    fun toMarketNewsTickerSentimentEntityList(value: String): List<MarketNewsTickerSentimentEntity> {
        val listType = object : TypeToken<List<MarketNewsTickerSentimentEntity>>() {}.type
        return gson.fromJson(value, listType)
    }
}
