package com.example.appretosophos.viewdocuments

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.appretosophos.R
import com.example.appretosophos.databinding.FragmentViewDocumentsBinding
import com.example.appretosophos.model.DocumentsViewModel


class ViewDocumentsFragment : Fragment() {

    //ViewModel for this fragment
    private val sharedViewModel: DocumentsViewModel by activityViewModels()

    // Binding object instance with access to the main menu screen
    private lateinit var binding: FragmentViewDocumentsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_view_documents, container,
            false
        )
        //Debug
        Log.d("ViewDocuments", "ViewDocuments screen onCreateView")

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
        //sharedViewModel.initializeValues()

        // Get Documents
        sharedViewModel.getDocuments()

        binding.recyclerViewDocuments.adapter = ViewDocumentsAdapter(findNavController())

        //Debug
        Log.d("ViewDocuments", "ViewDocuments screen onViewCreated")
    }

    // View the selected file
    fun viewFile(fileId: String){

    }

}