package com.example.notesapp_mvvm

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.notesapp_mvvm.databinding.FragmentLoginBinding
import com.example.notesapp_mvvm.models.UserRequest
import com.example.notesapp_mvvm.utils.NetworkResult
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val userRequest = createUserRequest()
            val userValidation = authViewModel.validateCredentials(userRequest.username, userRequest.email, userRequest.password, true)

            if(userValidation.first){
                authViewModel.loginUser(createUserRequest())
            } else {
                binding.txtError.text = userValidation.second
            }
//            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)

        }

        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_registerFragment)
        }

        bindObserver()
    }

    private fun createUserRequest() : UserRequest {
        val email = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()

        return UserRequest(email, password, "")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindObserver() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            when(it) {
                is NetworkResult.Success -> {
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }
            }
        })
    }
}