package com.app.example.auth.registration.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.example.R
import com.app.example.databinding.FragmentRegisterBinding
import com.app.example.privacy.PrivacyActivity
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val TAG = "RegisterFragment12354"

    private val viewModel by viewModels<RegisterViewModel>()
    private var binding: FragmentRegisterBinding? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)

        binding?.apply {
            btnRegister.setOnClickListener {
                val email = inputEmail.text.toString()
                val password = inputPassword.text.toString()
                val confirmPassword = inputConfirmPassword.text.toString()
                viewModel.onRegisteredClicked(email, password, confirmPassword)
            }
            btnOpenLogin.setOnClickListener {
                findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
            }
            btnOpenPrivacy.setOnClickListener {
                startPrivacyPolicy()
            }
        }

        viewModel.userRegisterMutableLiveData.observe(viewLifecycleOwner) { user ->
            Log.d(TAG, "onViewCreated: ${Gson().toJson(user)}")
            user?.isValid().let { validationCode ->
                when(validationCode) {
                    0 -> Toast.makeText(requireContext(), getString(R.string.message_enter_email), Toast.LENGTH_SHORT).show()
                    1 -> Toast.makeText(requireContext(), getString(R.string.message_enter_format_email), Toast.LENGTH_SHORT).show()
                    2 -> Toast.makeText(requireContext(), getString(R.string.message_enter_password), Toast.LENGTH_SHORT).show()
                    3 -> Toast.makeText(requireContext(), getString(R.string.message_weak_password), Toast.LENGTH_SHORT).show()
                    4 -> Toast.makeText(requireContext(), getString(R.string.message_confirm_password), Toast.LENGTH_SHORT).show()
                    5 -> Toast.makeText(requireContext(), getString(R.string.message_password_confirm_password), Toast.LENGTH_SHORT).show()
                    else -> {
                        if (binding!!.checkboxPrivacy.isChecked) {
                            viewModel.registerUser()
                        } else {
                            Toast.makeText(requireContext(), getString(R.string.message_privacy_policy), Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }
        }
        viewModel.registeredUser.observe(viewLifecycleOwner) {
            if (it){
                startMain()
            } else {
                Toast.makeText(requireContext(), "Nije uspesno kreiran", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun startMain() {
//        findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToMainActivity())
        requireActivity().finish()
    }

    private fun startPrivacyPolicy() {
        PrivacyActivity.newInit(requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}