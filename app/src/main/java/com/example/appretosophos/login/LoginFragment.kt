package com.example.appretosophos.login

import android.app.KeyguardManager
import androidx.core.hardware.fingerprint.FingerprintManagerCompat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.appretosophos.R
import com.example.appretosophos.databinding.FragmentLogInBinding
import com.example.appretosophos.model.DocumentsViewModel
import java.util.concurrent.Executor


class LoginFragment : Fragment() {

    //ViewModel for this fragment
    private val sharedViewModel: DocumentsViewModel by activityViewModels()

    // Binding object instance with access to the log in screen
    private lateinit var binding: FragmentLogInBinding

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_log_in, container,
            false
        )
        //Debug
        Log.d("LogIn", "LogIn screen onCreateView")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Initialize layout variables
        binding.viewModel = sharedViewModel
        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        //Debug
        Log.d("LogIn", "LogIn screen onViewCreated")

        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        requireContext(), "Error autenticación: $errString",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)

                    Toast.makeText(
                        requireContext(), "Autenticación exitosa!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    sharedViewModel.loadUserData(requireContext(), findNavController())
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        requireContext(), "Autenticación incorrecta!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticación biométrica")
            .setSubtitle("Log in usando huella")
            .setNegativeButtonText("Usar usuario y contraseña")
            .build()

        // Button listener
        binding.buttonEnter.setOnClickListener { logIn() }
        binding.buttonFingerPrint.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }


    }

    // Log in function
    private fun logIn() {
        // Get the values from the text fields
        val mail = binding.textInputMailEditText.text.toString()
        val password = binding.TextInputPasswordEdittext.text.toString()
        //Debug
        Log.d("LogIn", "LogIn Function mail: $mail password: $password")
        // Check is the fields are empty
        if (mail.isEmpty() || password.isEmpty()) {
            // Message to user
            Toast.makeText(
                context, getString(R.string.toast_1),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            // LogIn request
            val navController = findNavController()
            context?.let { sharedViewModel.logInRequest(mail, password, it, navController) }
        }
    }


}