package com.example.volumen.ui.portico

import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.format.Formatter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.volumen.R
import com.example.volumen.api.portico.Data
import com.example.volumen.api.portico.PorticoStatus
import com.example.volumen.api.portico.ListPortico
import com.example.volumen.databinding.FragmentPorticoBinding
import com.example.volumen.utils.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class PorticoFragment : Fragment(R.layout.fragment_portico), PorticoAdapter.OnItemClickListener {

    private var _binding: FragmentPorticoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PorticoViewModel by viewModels()

    private val adapter: PorticoAdapter = PorticoAdapter(this)

    private lateinit var customProgressDialog: Dialog
    private var ip: String = ""
    private var namePortico: String = ""
    private var listPortico: List<Data> = ArrayList()
    private var flag = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPorticoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        setUpView()
        initObservers()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun init() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val rut = sharedPref.getString("RUT", "")
        val wifiName = sharedPref.getString("WIFI_NAME", "")
        val gson = Gson()
        val json = sharedPref.getString("LIST_PORTICO", "")
        if (json?.isNotEmpty() == true) {
            val itemType = object : TypeToken<List<Data>>() {}.type
            listPortico = gson.fromJson<List<Data>>(json, itemType)
        }
        binding.apply {
            tvRutPortico.text = rut
            tvWifiName.text = wifiName

            rvListPortico.adapter = adapter
            rvListPortico.layoutManager = GridLayoutManager(requireContext(), 3)
            sfRefreshPortico.setOnRefreshListener {
                chargeAdapter()
                sfRefreshPortico.isRefreshing = false
            }
        }
        chargeAdapter()
        customProgressDialog = Dialog(requireContext())
    }

    private fun setUpView() {
        binding.apply {
            lUser.setOnClickListener {
                findNavController().navigate(R.id.action_portico_to_login)
            }
        }
    }

    private fun chargeAdapter() {
        if (isOnline(requireContext())) {
            val url = URL_WEB + URL_WEB_PORTICO
            viewModel.getListPortico(url)
        } else {
            if (listPortico.isNotEmpty()) {
                adapter.submitList(listPortico)
            } else {
                view?.makeSnackbar(MSG_ERROR_INTERNET, false)
            }
        }
    }

    private fun initObservers() {
        viewModel.porticoLiveData.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Success -> {
                    flag = true
                    showSuccessListPorticoView(result.data)
                }
                is Resource.Loading -> showLoadingView()
                is Resource.Error -> showErrorView(result.error)
            }
        })
        viewModel.listPorticoLiveData.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Success -> showSuccessListPorticoView(result.data)
                is Resource.Loading -> showLoadingView()
                is Resource.Error -> showErrorView(result.error)
            }
        })
    }

    private fun showSuccessListPorticoView(list: ListPortico?) {
        customProgressDialog.dismiss()
        val data = list?.data
        if (data != null) {
            listPortico = data
        }
        data.let { adapter.submitList(it) }
    }

    private fun showSuccessListPorticoView(data: PorticoStatus?) {
        customProgressDialog.dismiss()
        val status = data?.info_portico?.status
        if (status != null) {
            saveData()
            findNavController().lifeCycleNavigate(lifecycleScope, R.id.volumen)
        } else {
            view?.makeSnackbar(MSG_ERROR, false)
        }
    }

    private fun showLoadingView() {
        customProgressDialog.setContentView(R.layout.custom_dialog)
        customProgressDialog.setCancelable(false)
        customProgressDialog.show()
    }

    private fun showErrorView(error: Throwable?) {
        customProgressDialog.dismiss()
        if (!flag) {
            view?.makeSnackbar(error?.message.toString(), false)
        }
    }

    private fun saveData() {
        val gson = Gson()
        val json = gson.toJson(listPortico)
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("IP", ip)
            putString("NAME_PORTICO", namePortico)
            putString("LIST_PORTICO", json)
            apply()
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onItemClick(data: Data) {
        ip = "http://${data.ip}:${data.port}"
        //ip = "http://192.168.1.141:5000"
        val ipPortico = ip + URL_PORTICO
        namePortico = data.name.uppercase(Locale.getDefault())
        viewModel.getPorticoStatus(ipPortico)
    }

}