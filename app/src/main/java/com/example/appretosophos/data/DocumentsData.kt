package com.example.appretosophos.data

import com.squareup.moshi.Json

// Json log in response
data class UserData(
    val id: String? = "",
    @Json(name = "nombre") val name: String? = "",
    @Json(name = "apellido") val lastName: String? = "",
    @Json(name = "acceso") val access: Boolean,
    @Json(name = "admin") val admin: Boolean? = false
)

// Json Post Document Body
data class Documents(
    @Json(name = "TipoId") val documentType: String? = "",
    @Json(name = "Identificacion") val documentNumber: String? = "",
    @Json(name = "Nombre") val name: String? = "",
    @Json(name = "Apellido") val lastName: String? = "",
    @Json(name = "Ciudad") val city: String? = "",
    @Json(name = "Correo") val mail: String? = "",
    @Json(name = "TipoAdjunto") val fileType: String? = "",
    @Json(name = "Adjunto") val file: String? = "",
)

// Post Document response
data class PostDocumentResponse(
    @Json(name = "put") val put: Boolean = false
)

// Get documents response
data class GetDocumentsResponse(
    @Json(name = "Items") val documentsList: MutableList<AddedDocument> = mutableListOf(),
    @Json(name = "Count") val documentsNumber: Int = 0,
    @Json(name = "ScannedCount") val documentsImages: Int = 0,
)

// Get documents response
data class GetDocumentIdResponse(
    @Json(name = "Items") val documentsList: MutableList<AddedDocumentFull> = mutableListOf(),
    @Json(name = "Count") val documentsNumber: Int = 0,
    @Json(name = "ScannedCount") val documentsImages: Int = 0,
)

// Added documents
data class AddedDocument(
    @Json(name = "IdRegistro") val documentId: String = "",
    @Json(name = "Fecha") val date: String = "",
    @Json(name = "TipoAdjunto") val documentType: String = "",
    @Json(name = "Nombre") val name: String = "",
    @Json(name = "Apellido") val lastName: String = ""
)

// Added documents
data class AddedDocumentFull(
    @Json(name = "Ciudad") val city: String? = "",
    @Json(name = "Fecha") val date: String = "",
    @Json(name = "TipoAdjunto") val fileType: String = "",
    @Json(name = "Nombre") val name: String? = "",
    @Json(name = "Apellido") val lastName: String = "",
    @Json(name = "Identificacion") val documentNumber: String? = "",
    @Json(name = "IdRegistro") val documentId: String = "",
    @Json(name = "TipoId") val documentType: String? = "",
    @Json(name = "Correo") val mail: String? = "",
    @Json(name = "Adjunto") val file: String? = ""
)

// Get Offices Response
data class GetOfficesResponse(
    @Json(name = "Items") val officesList: MutableList<Office> = mutableListOf(),
    @Json(name = "Count") val officesNumber: Int = 0,
    @Json(name = "ScannedCount") val officesImages: Int = 0,
)

// Office
data class Office(
    @Json(name = "Ciudad") val city: String = "",
    @Json(name = "Longitud") val long: String = "",
    @Json(name = "IdOficina") val officeID: Int = 0,
    @Json(name = "Latitud") val lat: String = "",
    @Json(name = "Nombre") val name: String = ""
)