package com.example.appretosophos.documents

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.getBitmap
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.createBitmap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.appretosophos.R
import com.example.appretosophos.data.Documents
import com.example.appretosophos.databinding.FragmentSendDocumentBinding
import com.example.appretosophos.model.DocumentsViewModel
import com.google.android.play.core.internal.zzac
import java.io.ByteArrayOutputStream

private const val REQUEST_CODE_C = 42
private const val REQUEST_CODE_F = 62

class SendDocumentFragment : Fragment() {
    //ViewModel for this fragment
    private val sharedViewModel: DocumentsViewModel by activityViewModels()

    // Binding object instance with access to the main menu screen
    private lateinit var binding: FragmentSendDocumentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_send_document, container,
            false
        )
        //Debug
        Log.d("SendDocument", "SendDocument screen onCreateView")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Initialize layout variables
        binding.viewModel = sharedViewModel
        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // Initialize values for test
        // sharedViewModel.initializeValues()

        // Fill values
        fillValues()

        // Buttons listeners
        binding.buttonAddDocumentCamera.setOnClickListener { addDocumentCamera() }
        binding.buttonAddDocument.setOnClickListener { addDocumentFile() }
        binding.buttonSendDocument.setOnClickListener { sendData() }

        //Debug
        Log.d("SendDocument", "SendDocument screen onViewCreated")
    }

    // Send data to server
    private fun sendData() {
        Log.d("SendDocument", "sendData")
        // Get input values
        setValues()
        // Check that all values are filled
        if (!sharedViewModel.checkValues()) {
            Toast.makeText(context, context?.getString(R.string.toast_6), Toast.LENGTH_SHORT)
                .show()
        } else {
            // Generate post body
            val document = Documents(
                sharedViewModel.documentType.value, sharedViewModel.documentNumber.value,
                sharedViewModel.name.value, sharedViewModel.lastName.value,
                sharedViewModel.city.value, sharedViewModel.mail.value,
                sharedViewModel.fileType.value, sharedViewModel.image64.value
            )
            // Post Request
            context?.let { sharedViewModel.postDocument(document, it) }
        }

    }

    // Fill values
    private fun fillValues() {
        if (!sharedViewModel.mail.value.isNullOrEmpty()) {
            binding.TextInputEmailEdittext.setText(sharedViewModel.mail.value.toString())
        }
        if (!sharedViewModel.documentNumber.value.isNullOrEmpty()) {
            binding.TextInputDocumentNumberEdittext.setText(
                sharedViewModel.documentNumber.value
                    .toString()
            )
        }
        if (!sharedViewModel.name.value.isNullOrEmpty()) {
            binding.TextInputNameEdittext.setText(sharedViewModel.name.value.toString())
        }
        if (!sharedViewModel.lastName.value.isNullOrEmpty()) {
            binding.TextInputLastNameEdittext.setText(sharedViewModel.lastName.value.toString())
        }
    }

    // Get input values
    private fun setValues() {
        sharedViewModel.setDocumentNumber(binding.TextInputDocumentNumberEdittext.text.toString())
        sharedViewModel.setName(binding.TextInputNameEdittext.text.toString())
        sharedViewModel.setLastName(binding.TextInputLastNameEdittext.text.toString())
        sharedViewModel.setMail(binding.TextInputEmailEdittext.text.toString())
    }


    // Add document from file
    private fun addDocumentFile() {
        val selectPictureIntent = Intent()
        selectPictureIntent.type = "image/*"
        selectPictureIntent.action = Intent.ACTION_GET_CONTENT
        // Error handling
        try {
            startActivityForResult(selectPictureIntent, REQUEST_CODE_F)
        } catch (e: Exception) {
            Toast.makeText(context, context?.getString(R.string.toast_5), Toast.LENGTH_SHORT)
                .show()
            Log.d("SendDocument", "addDocumentFile Error: $e")
        }
    }

    // Get photo of document
    private fun addDocumentCamera() {
        Log.d("SendDocument", "add Document")
        // Create intent
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Error handling
        try {
            startActivityForResult(takePictureIntent, REQUEST_CODE_C)
        } catch (e: Exception) {
            Toast.makeText(context, context?.getString(R.string.toast_4), Toast.LENGTH_SHORT)
                .show()
            Log.d("SendDocument", "addDocumentCamera Error: $e")
        }
    }

    // Override function that handles the result of intent
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("SendDocument", "onActivityResult")
        // Check the request code and the activity result
        if (requestCode == REQUEST_CODE_C && resultCode == Activity.RESULT_OK) {
            // Get image bitmap
            val takenImage = data?.extras?.get("data") as Bitmap
            // Compress and transform bitmap to base 64
            transformImage(takenImage)
        } else if (requestCode == REQUEST_CODE_F && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                // Get image uri
                val uri = data.data
                // Transform uri to bitmap
                val takenImage = getBitmap(requireContext().contentResolver, uri)
                // Compress and transform bitmap to base 64
                transformImage(takenImage)
            }
        } else {
            // call default method
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    // Compress and transform bitmap to base 64
    private fun transformImage(bitmap: Bitmap) {
        Log.d("SendDocument", "transformImage")
        // Initialize output stream
        val stream = ByteArrayOutputStream()
        // Compress image
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
        // Compress image to bytearray
        val byteArray = stream.toByteArray()
        // transform the bytearray to base64
        val image64 = Base64.encodeToString(byteArray, Base64.DEFAULT)
        // Store the image value
        sharedViewModel.setImage64(image64)

        // Decode Image
        val byteArrayDecoded = Base64.decode(image64, Base64.DEFAULT)

        // byte array to bitmap
        val takenImage = BitmapFactory.decodeByteArray(byteArrayDecoded, 0, byteArrayDecoded.size)
        binding.imageViewTest.setImageBitmap(takenImage)
    }


}