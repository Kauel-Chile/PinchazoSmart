package com.kauel.pinchazoSmart.ui.portico

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.kauel.pinchazoSmart.R
import com.kauel.pinchazoSmart.api.portico.Data
import com.kauel.pinchazoSmart.api.portico.PorticoStatus
import com.kauel.pinchazoSmart.api.portico.ListPortico
import com.kauel.pinchazoSmart.databinding.FragmentPorticoBinding
import com.kauel.pinchazoSmart.utils.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
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
        if (listPortico.isNotEmpty()) {
            adapter.submitList(listPortico)
            if (isOnline(requireContext())) {
                getListPortico()
            }
        } else if (isOnline(requireContext())) {
            getListPortico()
        } else {
            view?.makeSnackbar(MSG_ERROR_INTERNET, false)
        }
    }

    private fun getListPortico() {
        val url = URL_WEB + URL_WEB_PORTICO
        viewModel.getListPortico(url)
    }

    private fun initObservers() {
        viewModel.porticoLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Success -> {
                    flag = true
                    showSuccessPorticoView(result.data)
                }
                is Resource.Loading -> showLoadingView(1)
                is Resource.Error -> showErrorView(result.error, 1)
            }
        }
        viewModel.listPorticoLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Success -> showSuccessListPorticoView(result.data)
                is Resource.Loading -> showLoadingView(2)
                is Resource.Error -> showErrorView(result.error, 2)
            }
        }
    }

    private fun showSuccessListPorticoView(list: ListPortico?) {
        binding.pbUploadListing.gone()
        val data = list?.data
        if (data != null) {
            listPortico = data
        }
        data.let { adapter.submitList(it) }
    }

    private fun showSuccessPorticoView(data: PorticoStatus?) {
        customProgressDialog.dismiss()
        val status = data?.info_portico?.status
        if (status != null) {
            saveData()
            findNavController().lifeCycleNavigate(lifecycleScope, R.id.volumen)
        } else {
            view?.makeSnackbar(MSG_ERROR, false)
        }
    }

    private fun showLoadingView(typeLoader: Int) {
        when (typeLoader) {
            1 -> {
                customProgressDialog.setContentView(R.layout.custom_dialog)
                customProgressDialog.setCancelable(false)
                customProgressDialog.show()
            }
            2 -> {
                binding.pbUploadListing.visible()
            }
        }
    }

    private fun showErrorView(error: Throwable?, typeLoader: Int) {
        when (typeLoader) {
            1 -> {
                customProgressDialog.dismiss()
            }
            2 -> {
                binding.pbUploadListing.gone()
            }
        }
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
        namePortico = data.name
        viewModel.getPorticoStatus(ipPortico)
    }

}