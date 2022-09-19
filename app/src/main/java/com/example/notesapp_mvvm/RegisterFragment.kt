package com.example.notesapp_mvvm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.notesapp_mvvm.databinding.FragmentRegisterBinding
import com.example.notesapp_mvvm.models.UserRequest
import com.example.notesapp_mvvm.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class registerFragment : Fragment() {


    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.btnLogin.setOnClickListener {
//            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            authViewModel.loginUser(UserRequest("labbaasdasddubba@gmail.com", "abc", "labbadubba"))
        }

        binding.btnSignUp.setOnClickListener {
//
            authViewModel.registerUser(UserRequest("labbadubba@gmail.com", "abc", "labbadubba"))
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // For performance reason
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when(it){
                is NetworkResult.Success -> {
                    // Token
                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                }
                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        })
    }
}