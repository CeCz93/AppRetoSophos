package com.example.appretosophos.network

import com.example.appretosophos.data.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


// Create a moshi a object - Moshi Builder
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private const val BASE_URL = "https://6w33tkx4f9.execute-api.us-east-1.amazonaws.com"


// Create retrofit object - Retrofit builder
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

// Interface that defines how retrofit talks to the web server
interface DocumentApiService {

    // Log in request
    @GET("RS_Usuarios")
    suspend fun logUser(
        @Query("idUsuario") mail: String,
        @Query("clave") password: String
    ): UserData

    // Post Document
    @POST("RS_Documentos")
    suspend fun postDocument(@Body document: Documents): PostDocumentResponse

    // Get documents mail
    @GET("RS_Documentos")
    suspend fun getDocumentsMail(
        @Query("correo") mail: String,
    ): GetDocumentsResponse

    // Get documents id
    @GET("RS_Documentos")
    suspend fun getDocumentsId(
        @Query("idRegistro") id: String,
    ): GetDocumentIdResponse

    // Get offices
    @GET("RS_Oficinas")
    suspend fun getOffices(): GetOfficesResponse
}

// Singleton API object
object DocumentAPI {
    val retrofitService: DocumentApiService by lazy {
        retrofit.create(DocumentApiService::class.java)
    }
}