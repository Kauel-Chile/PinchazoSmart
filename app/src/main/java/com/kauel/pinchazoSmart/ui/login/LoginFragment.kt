package com.kauel.pinchazoSmart.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kauel.pinchazoSmart.BuildConfig
import com.kauel.pinchazoSmart.R
import com.kauel.pinchazoSmart.api.login.Login
import com.kauel.pinchazoSmart.api.login.ResponseDataLogin
import com.kauel.pinchazoSmart.databinding.FragmentLoginBinding
import com.kauel.pinchazoSmart.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    private var rut = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        initObservers()
        setUpView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun init() {
        val versionName = BuildConfig.VERSION_NAME
        binding.apply {
            tvVersion.text = "$VERSION_NAME $versionName"
        }
    }

    private fun setUpView() {
        binding.apply {
            btnLogin.setOnClickListener(View.OnClickListener {

                rut = formatRut(edtRut.text.toString())

                if (rut.isEmpty()) {
                    edtRut.error = ERROR_RUT_1
                    edtRut.requestFocus()
                    return@OnClickListener
                } else {
                    if (validaRut(rut) == false) {
                        edtRut.error = ERROR_RUT_2
                        edtRut.requestFocus()
                        return@OnClickListener
                    } else {
                        if (isOnline(requireContext())) {
                            login()
                        } else {
                            saveData()
                            findNavController().navigate(R.id.action_login_to_connectInternetFragment)
                        }
                    }
                }
            })
        }
    }

    private fun saveData() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("RUT", rut)
            putString("CODE_SCANDIT", "")
            apply()
        }
    }

    private fun login() {
        val url = URL_WEB + URL_WEB_LOGIN
        val user = Login(USERNAME_LOGIN, PASSWORD_LOGIN)
        viewModel.login(url = url, user = user)
    }

    private fun initObservers() {
        viewModel.loginLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> showErrorView(result.error)
                is Resource.Loading -> showLoadingView()
                is Resource.Success -> showSuccessView(result.data)
            }
        }
    }

    private fun showSuccessView(data: ResponseDataLogin?) {
        binding.apply {
            progressBar.gone()
        }
        saveData()
        findNavController().navigate(R.id.action_login_to_connectInternetFragment)
    }

    private fun showLoadingView() {
        binding.apply {
            lyForm.gone()
            progressBar.visible()
        }
    }

    private fun showErrorView(error: Throwable?) {
        binding.apply {
            lyForm.visible()
            progressBar.gone()
            view?.makeSnackbar(error?.message.toString(), false)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}