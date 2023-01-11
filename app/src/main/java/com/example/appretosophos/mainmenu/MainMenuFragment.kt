package com.example.appretosophos.mainmenu

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.appretosophos.R
import com.example.appretosophos.databinding.FragmentMainMenuBinding
import com.example.appretosophos.model.DocumentsViewModel


class MainMenuFragment : Fragment() {
    //ViewModel for this fragment
    private val sharedViewModel: DocumentsViewModel by activityViewModels()

    // Binding object instance with access to the main menu screen
    private lateinit var binding: FragmentMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // Add menu to fragment
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate menu
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.menuMainSendDocument -> {
                sendDocument()
                true
            }
            R.id.menuMainViewDocuments -> {
                viewDocuments()
                true
            }
            R.id.menuMainOffices -> {
                viewOffices()
                true
            }
            R.id.menuMainLogOut -> {
                logOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_menu, container,
            false)
        //Debug
        Log.d("MainMenu","MainMenu screen onCreateView")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize layout variables
        binding.viewModel = sharedViewModel
        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner =viewLifecycleOwner

        // Button listener
        binding.buttonSendDocument.setOnClickListener{ sendDocument() }
        binding.buttonViewDocument.setOnClickListener { viewDocuments() }
        binding.buttonOffices.setOnClickListener { viewOffices() }

        binding.textViewWelcome.text="Hola ${sharedViewModel.name.value} !!"

        //Debug
        Log.d("MainMenu","MainMenu screen onViewCreated")
    }

    // Navigate to send document fragment
    private fun sendDocument(){
        findNavController().navigate(R.id.action_mainMenuFragment_to_sendDocumentFragment)
    }

    // Navigate to send document fragment
    private fun viewDocuments(){
        findNavController().navigate(R.id.action_mainMenuFragment_to_viewDocumentsFragment)
    }

    private fun viewOffices(){
        findNavController().navigate(R.id.action_mainMenuFragment_to_mapsFragment)
    }

    private fun logOut(){
        findNavController().navigate(R.id.action_mainMenuFragment_to_logInFragment)
    }

}