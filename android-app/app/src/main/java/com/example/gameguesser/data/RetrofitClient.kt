package com.example.gameguesser.data

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

object RetrofitClient {

    private const val BASE_URL = "https://gameguesser-api.onrender.com/"

    val gson = GsonBuilder()
        .registerTypeAdapter(object : TypeToken<List<Game>>() {}.type, JsonDeserializer { json, _, _ ->
            val arr = json.asJsonArray
            arr.map { element ->
                val jsonObj = element.asJsonObject
                val idObject = jsonObj["_id"]?.asJsonObject
                val mongoId = idObject?.get("\$oid")?.asString ?: ""
                Game(
                    _id = if (mongoId.isNotEmpty()) IdObject(mongoId) else null,
                    id = mongoId,
                    name = jsonObj["name"]?.asString ?: "",
                    genre = jsonObj["genre"]?.asString ?: "",
                    platforms = jsonObj["platforms"]?.asJsonArray?.map { it.asString } ?: emptyList(),
                    releaseYear = jsonObj["releaseYear"]?.asInt ?: 0,
                    developer = jsonObj["developer"]?.asString ?: "",
                    publisher = jsonObj["publisher"]?.asString ?: "",
                    description = jsonObj["description"]?.asString ?: "",
                    coverImageUrl = jsonObj["coverImageUrl"]?.asString ?: "",
                    budget = jsonObj["budget"]?.asDouble ?: 0.0,
                    saga = jsonObj["saga"]?.asString ?: "",
                    pov = jsonObj["pov"]?.asString ?: "",
                    clues = jsonObj["clues"]?.asJsonArray?.map { it.asString } ?: emptyList(),
                    keywords = jsonObj["keywords"]?.asJsonArray?.map { it.asString } ?: emptyList()
                )
            }
        })
        .create()


    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }


    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
