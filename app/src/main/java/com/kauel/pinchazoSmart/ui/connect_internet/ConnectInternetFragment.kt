package com.kauel.pinchazoSmart.ui.connect_internet

import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kauel.pinchazoSmart.R
import com.kauel.pinchazoSmart.api.login.Login
import com.kauel.pinchazoSmart.api.login.ResponseDataLogin
import com.kauel.pinchazoSmart.api.login.ResponseLogin
import com.kauel.pinchazoSmart.databinding.FragmentConectionWifiBinding
import com.kauel.pinchazoSmart.ui.login.LoginViewModel
import com.kauel.pinchazoSmart.utils.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ConnectInternetFragment : Fragment(R.layout.fragment_conection_wifi) {

    private var _binding: FragmentConectionWifiBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    private var flag = false
    private var wifiName: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentConectionWifiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        initObservers()
        setUpView()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        if (isConnectedWifi(requireContext()) && !flag) {
            //findNavController().navigate(R.id.action_connectInternet_to_portico)
            login()
        }
        super.onResume()
    }

    private fun init() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val token = sharedPref.getString("TOKEN", "")
        if (isConnectedWifi(requireContext())) {
            if (token?.isEmpty() == true) {
                login()
            } else {
                findNavController().navigate(R.id.action_connectInternet_to_portico)
            }
            flag = true
        }
    }

    @Suppress("DEPRECATION")
    private fun isConnectedWifi(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        val wifiManager = context.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager?
        val wifiInfo = wifiManager!!.connectionInfo
        val nameWifi = wifiInfo.ssid
        wifiName = nameWifi
        saveData()
        return networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI
    }

    private fun saveData() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("WIFI_NAME", wifiName)
            apply()
        }
    }

    private fun setUpView() {
        binding.apply {
            btnConnect.setOnClickListener(View.OnClickListener {
                startActivityForResult(Intent(android.provider.Settings.ACTION_SETTINGS), 0);
            })
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
        binding.progressBar.gone()
        findNavController().navigate(R.id.action_connectInternet_to_portico)
    }

    private fun showLoadingView() {
        binding.progressBar.visible()
    }

    private fun showErrorView(error: Throwable?) {
        binding.apply {
            progressBar.gone()
            view?.makeSnackbar(error?.message.toString(), false)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}