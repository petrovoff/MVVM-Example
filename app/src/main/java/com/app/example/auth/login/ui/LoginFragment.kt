package com.app.example.auth.login.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.example.R
import com.app.example.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private val TAG = "LoginFragment12354"

    private val viewModel by viewModels<LoginViewModel>()

    private var binding: FragmentLoginBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        binding?.apply {
            btnLogin.setOnClickListener {
                viewModel.email.value = inputEmail.text.toString()
                viewModel.password.value = inputPassword.text.toString()
                viewModel.onLoginClicked()
            }
            btnRegister.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }
        }

        viewModel.userMutableLiveData.observe(viewLifecycleOwner) { user ->
            user?.isValid().let { validationCode ->
                when(validationCode) {
                    0 -> Toast.makeText(requireContext(), getString(R.string.message_enter_email), Toast.LENGTH_SHORT).show()
                    1 -> Toast.makeText(requireContext(), getString(R.string.message_enter_format_email), Toast.LENGTH_SHORT).show()
                    2 -> Toast.makeText(requireContext(), getString(R.string.message_enter_password), Toast.LENGTH_SHORT).show()
                    3 -> Toast.makeText(requireContext(), getString(R.string.message_weak_password), Toast.LENGTH_SHORT).show()
                    else -> viewModel.loginUser()
                }
            }
        }
        viewModel.loggedUser.observe(viewLifecycleOwner) {
            Log.e(TAG, "isUserExist: $it")
            if (it){
                startMain()
            } else {
                Toast.makeText(requireContext(), "Ovaj korisnik ne postoji", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun startMain() {
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}