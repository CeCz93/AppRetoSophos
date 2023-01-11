package com.example.appretosophos.viewdocuments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.appretosophos.R
import com.example.appretosophos.databinding.FragmentDisplayDocumentBinding
import com.example.appretosophos.model.DocumentsViewModel


class DisplayDocumentFragment : Fragment() {

    //Companion object used to define constants
    companion object {
        const val ID = "documentID"
    }

    //ViewModel for this fragment
    private val sharedViewModel: DocumentsViewModel by activityViewModels()

    // Binding object instance with access to the main menu screen
    private lateinit var binding: FragmentDisplayDocumentBinding

    // Document id
    private lateinit var documentId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // get document id
        arguments?.let {
            documentId = it.getString(ID).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_display_document, container,
            false
        )
        //Debug
        Log.d("DisplayDocument", "DisplayDocument")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Initialize layout variables
        binding.viewModel = sharedViewModel
        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // Get the document image
        sharedViewModel.getDocumentsID(documentId, binding)

        //Debug
        Log.d("DisplayDocument", "DisplayDocument screen onViewCreated")
    }

}