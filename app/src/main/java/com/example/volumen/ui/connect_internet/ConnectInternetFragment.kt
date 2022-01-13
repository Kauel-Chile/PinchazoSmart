package com.example.volumen.ui.connect_internet

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
import androidx.navigation.fragment.findNavController
import com.example.volumen.R
import com.example.volumen.databinding.FragmentConectionWifiBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ConnectInternetFragment : Fragment(R.layout.fragment_conection_wifi) {

    private var _binding: FragmentConectionWifiBinding? = null
    private val binding get() = _binding!!

    private var wifiName: String = ""
    private var flag = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConectionWifiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        setUpView()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        if (isConnectedWifi(requireContext()) && !flag) {
            findNavController().navigate(R.id.action_connectInternet_to_portico)
        }
        super.onResume()
    }

    private fun init() {
        if (isConnectedWifi(requireContext())) {
            findNavController().navigate(R.id.action_connectInternet_to_portico)
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
        wifiName = wifiInfo.ssid
        saveWifiName()
        return networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI
    }

    private fun saveWifiName() {
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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}