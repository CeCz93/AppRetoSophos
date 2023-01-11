package com.example.appretosophos.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.preference.PreferenceManager
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.appretosophos.R
import com.example.appretosophos.data.AddedDocument
import com.example.appretosophos.data.Documents
import com.example.appretosophos.data.Office
import com.example.appretosophos.databinding.FragmentDisplayDocumentBinding
import com.example.appretosophos.network.DocumentAPI
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch


enum class DocumentsApiStatus { LOADING, ERROR, DONE }

// View model for the documents app
class DocumentsViewModel : ViewModel() {

    // View model properties
    private val _mail = MutableLiveData<String>()
    private val _name = MutableLiveData<String>()
    private val _lastName = MutableLiveData<String>()
    private val _documentType = MutableLiveData<String>()
    private val _city = MutableLiveData<String>()
    private val _fileType = MutableLiveData<String>()
    private val _image64 = MutableLiveData<String>()
    private val _documentNumber = MutableLiveData<String>()
    private val _addedDocuments = MutableLiveData<List<AddedDocument>>()
    private val _status = MutableLiveData<DocumentsApiStatus>()
    private val _image64Display = MutableLiveData<String>()
    private val _offices = MutableLiveData<List<Office>>()

    // Properties getters
    val name: LiveData<String> = _name
    val lastName: LiveData<String> = _lastName
    val documentType: LiveData<String> = _documentType
    val city: LiveData<String> = _city
    val fileType: LiveData<String> = _fileType
    val image64: LiveData<String> = _image64
    val documentNumber: LiveData<String> = _documentNumber
    val mail: LiveData<String> = _mail
    val addedDocuments: LiveData<List<AddedDocument>> = _addedDocuments
    val status: LiveData<DocumentsApiStatus> = _status
    val offices: LiveData<List<Office>> = _offices

    // Properties setters
    fun setDocumentType(document: String) {
        _documentType.value = document
    }

    fun setCity(city: String) {
        _city.value = city
    }

    fun setFileType(fileType: String) {
        _fileType.value = fileType
    }

    fun setImage64(image64: String) {
        _image64.value = image64
    }

    fun setDocumentNumber(documentNumber: String) {
        _documentNumber.value = documentNumber
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun setLastName(lastName: String) {
        _lastName.value = lastName
    }

    fun setMail(mail: String) {
        _mail.value = mail
    }

    fun checkValues(): Boolean {
        return !(_name.value.isNullOrEmpty() || _lastName.value.isNullOrEmpty() ||
                _city.value.isNullOrEmpty() || _documentNumber.value.isNullOrEmpty() ||
                _documentType.value.isNullOrEmpty() || _fileType.value.isNullOrEmpty() ||
                _mail.value.isNullOrEmpty() || _image64.value.isNullOrEmpty())
    }

    // Initialize values
    fun initializeValues() {
        _name.value = "Carlos"
        _lastName.value = "Cadavid"
        _city.value = "Medellín"
        _mail.value = "cesteban.czapata@gmail.com"
        _documentNumber.value = "1035429663"
        _documentType.value = "CC"
        _fileType.value = "Cédula"
    }

    // Log in function
    fun logInRequest(
        mail: String,
        password: String,
        context: Context,
        navController: NavController
    ) {
        // Launch the view model coroutine
        viewModelScope.launch {
            // Error handling
            try {
                // HTTP Log in request
                val userData = DocumentAPI.retrofitService.logUser(mail, password)
                // Access control
                if (userData.access) {
                    // Store user data
                    _mail.value = mail
                    _name.value = userData.name!!
                    _lastName.value = userData.lastName!!
                    //Debug
                    Log.d(
                        "LogIn", "LogInRequest ,Access ${userData.access} mail: ${_mail.value}, " +
                                "name ${_name.value}, lastName ${_lastName.value}"
                    )
                    // Save data
                    saveUserData(mail, password, context)
                    // Navigate to main menu
                    navController.navigate(R.id.action_logInFragment_to_mainMenuFragment)
                } else {
                    // Error message
                    Toast.makeText(
                        context, context.getString(R.string.toast_2),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                // Debug
                Log.d("LogIn", "LogInRequest Error, ${e.message}")
                // Error message
                Toast.makeText(context, context.getString(R.string.toast_3), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun saveUserData(mail: String, password: String, context: Context){
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor =sharedPreferences.edit()
        editor.putString("mail", mail)
        editor.putString("password", password)
        editor.apply()
    }

    fun loadUserData(context: Context, navController: NavController){
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        val mail = sharedPreferences.getString("mail", null)
        val password = sharedPreferences.getString("password", null)

        if (mail.isNullOrEmpty() || password.isNullOrEmpty()){
            Toast.makeText(context, context.getString(R.string.toast_9), Toast.LENGTH_SHORT)
                .show()

        }else {
            logInRequest(mail, password, context, navController)
        }
    }

    fun postDocument(document: Documents, context: Context) {
        // Launch the view model coroutine
        viewModelScope.launch {
            try {
                // Post document
                val postDocumentResponse = DocumentAPI.retrofitService.postDocument(document)
                if (!postDocumentResponse.put) {
                    // Debug
                    Log.d("SendDocument", "postDocument response Error")
                    // Error message
                    Toast.makeText(
                        context, context.getString(R.string.toast_7),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.d("SendDocument", "postDocument success")
                    Toast.makeText(
                        context, context.getString(R.string.toast_8),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                // Debug
                Log.d("SendDocument", "postDocument Error, ${e.message}")
                // Error message
                Toast.makeText(context, context.getString(R.string.toast_3), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun getDocuments() {
        // Launch the vie model coroutine
        viewModelScope.launch {
            _status.value = DocumentsApiStatus.LOADING
            //Exception handling: Missing internet connection
            try {
                // Get the documents
                val getDocumentsResponse = _mail.value?.let {
                    DocumentAPI.retrofitService.getDocumentsMail(
                        it
                    )
                }
                if (getDocumentsResponse != null) {
                    _addedDocuments.value = getDocumentsResponse.documentsList
                }
                // Save the result to the status variable
                Log.d("ViewDocuments", "getDocuments success")
                _status.value = DocumentsApiStatus.DONE
            } catch (e: Exception) {
                _status.value = DocumentsApiStatus.ERROR
                // Clear the recycler view
                _addedDocuments.value = listOf()
                Log.d("ViewDocuments", "getDocuments Error, ${e.message}")
            }

        }
    }

    fun getDocumentsID(document: String, binding: FragmentDisplayDocumentBinding) {
        // Launch the vie model coroutine
        viewModelScope.launch {
            _status.value = DocumentsApiStatus.LOADING
            //Exception handling: Missing internet connection
            try {
                // Get the documents
                val documentResponse = DocumentAPI.retrofitService.getDocumentsId(document)
                val receivedDocument = documentResponse.documentsList[0]
                _image64Display.value = receivedDocument.file!!
                // Transform Image
                val takenImage = transformImage()
                // Set image
                binding.documentImage.setImageBitmap(takenImage)
                // Update status
                _status.value = DocumentsApiStatus.DONE
                Log.d("DisplayDocument", "getDocumentID success")
            } catch (e: Exception) {
                _status.value = DocumentsApiStatus.ERROR
                // Clear the recycler view
                _addedDocuments.value = listOf()
                Log.d("DisplayDocument", "getDocumentID Error, ${e.message}")
            }

        }
    }

    // Compress and transform bitmap to base 64
    private fun transformImage(): Bitmap {
        Log.d("DisplayDocument", "transformImage")
        // Decode Image
        val byteArrayDecoded = Base64.decode(_image64Display.value, Base64.DEFAULT)
        // byte array to bitmap
        return BitmapFactory.decodeByteArray(byteArrayDecoded, 0, byteArrayDecoded.size)
    }

    fun getOffices(map: GoogleMap) {
        // Launch the vie model coroutine
        viewModelScope.launch {
            try {
                // Get the offices
                val officesResponse = DocumentAPI.retrofitService.getOffices()
                _offices.value = officesResponse.officesList

                for (item in _offices.value as MutableList<Office>) {
                    val lat = item.lat.toDouble()
                    val lon = item.long.toDouble()
                    val name = item.name
                    val position = LatLng(lat, lon)
                    Log.d("Maps", "Location $position name $name")
                    map.addMarker(MarkerOptions().position(position).title(name))
                }

                Log.d("Maps", "getOffices success")
            } catch (e: Exception) {

                Log.d("Maps", "getOffices Error, ${e.message}")
            }

        }
    }
}