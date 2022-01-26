package com.example.volumen.ui.main

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.volumen.R
import com.example.volumen.api.volumen.*
import com.example.volumen.databinding.FragmentVolumenBinding
import com.example.volumen.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


@AndroidEntryPoint
class VolumenFragment : Fragment(R.layout.fragment_volumen) {

    private var _binding: FragmentVolumenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VolumenViewModel by viewModels()

    private var ip: String? = ""
    private lateinit var customProgressDialog: Dialog
    private var idPhone = ""
    private var nameImage = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.portico)
        }
        _binding = FragmentVolumenBinding.inflate(inflater, container, false)
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
        val namePortico = sharedPref.getString("NAME_PORTICO", "")
        val codeScandit = sharedPref.getString("CODE_SCANDIT", "")
        ip = sharedPref.getString("IP", "")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            idPhone =
                Settings.Secure.getString(activity?.contentResolver, Settings.Secure.ANDROID_ID)
        }
        binding.apply {
            tvRutPortico.text = rut?.trim()
            tvPortico.text = namePortico
            //edtBarcode.setText("123456")
            if (codeScandit?.isNotEmpty() == true) {
                if (validateScanditCode(codeScandit)) {
                    edtBarcode.setText(codeScandit)
                } else {
                    view?.makeSnackbar(MSG_ERROR_BARCODE, false)
                }
            }
        }
        customProgressDialog = Dialog(requireContext())
    }

    private fun validateScanditCode(code: String): Boolean {

        if (code.length != 26) return false

        val numeric = code.matches("-?\\d+(\\.\\d+)?".toRegex())

        if (numeric) return true

        return false
    }

    private fun setUpView() {
        binding.apply {
            lUser.setOnClickListener {
                findNavController().navigate(R.id.action_volumen_to_login)
            }
            lPortico.setOnClickListener {
                findNavController().navigate(R.id.action_volumen_to_portico)
            }
            lRequestSizes.setOnClickListener {
                if (edtBarcode.text.isNotEmpty()) {
                    val code = CodeRequest(
                        edtBarcode.text.toString(),
                        tvRutPortico.text.toString(),
                        tvPortico.text.toString(),
                        idPhone
                    )
                    val url = ip + URL_VOLUMEN
                    viewModel.getVolumen(url, code)
                } else {
                    view?.makeSnackbar(MSG_ERROR_CODE, false)
                }
            }
            scanner.setOnClickListener {
                findNavController().navigate(R.id.action_volumen_to_scandit)
            }
            btnSend.setOnClickListener {
                lifecycleScope.launch {
                    try {
                        if (validateDataSize()) {
                            if (isOnline(requireContext())) {
                                sendDataVolumen(true)
                            } else {
                                view?.makeSnackbar("NO INTERNET", false)
                            }
                        } else {
                            view?.makeSnackbar(DATA_NOT_SENT, false)
                        }
                    } catch (ex: Exception) {
                        view?.makeSnackbar(MSG_ERROR + ex.message, false)
                    }
                }
            }
            btnSendNull.setOnClickListener {
                try {
                    if (binding.edtBarcode.text.trim().isNotEmpty()) {
                        if (isOnline(requireContext())) {
                            sendDataVolumen(false)
                        } else {
                            view?.makeSnackbar("NO INTERNET", false)
                        }
                    } else {
                        view?.makeSnackbar(DATA_NOT_SENT, false)
                    }
                } catch (ex: Exception) {
                    view?.makeSnackbar(MSG_ERROR + ex.message, false)
                }
            }
        }
    }

    private fun sendDataVolumen(fullData: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val date = Date.from(current.atZone(ZoneId.systemDefault()).toInstant())
            val url = URL_WEB + URL_WEB_DATA_PORTICO
            binding.apply {
                val data1 = SendDataVolumen(
                    idPhone = idPhone,
                    highh = stringToDouble(edtHigh.text.toString()),
                    longg = stringToDouble(edtLong.text.toString()),
                    widthh = stringToDouble(edtWidth.text.toString()),
                    volumen = stringToDouble(edtVolume.text.toString()),
                    rut = tvRutPortico.text.toString(),
                    date = date,
                    portico = tvPortico.text.toString(),
                    bardCode = edtBarcode.text.toString(),
                    status = STATUS_SEND_OK,
                    img_name = nameImage)

                val data2 = SendDataVolumen(
                    idPhone = idPhone,
                    rut = tvRutPortico.text.toString(),
                    date = date,
                    portico = tvPortico.text.toString(),
                    bardCode = edtBarcode.text.toString(),
                    status = STATUS_SEND_NULL,
                    img_name = nameImage)

                if (fullData) {
                    viewModel.sendData(url, data1)
                } else {
                    viewModel.sendData(url, data2)
                }
            }

        }
    }

    private fun validateDataSize(): Boolean {

        binding.apply {
            if (edtBarcode.text.trim().isEmpty()
                || edtVolume.text.trim().isEmpty()
                || edtWidth.text.trim().isEmpty()
                || edtLong.text.trim().isEmpty()
                || edtHigh.text.trim().isEmpty()
            ) {
                return false
            }
        }

        return true
    }

    private fun clear() {
        binding.apply {
            edtBarcode.setText("")
            edtWidth.setText("")
            edtHigh.setText("")
            edtLong.setText("")
            edtVolume.setText("")
            imgResult.setImageResource(android.R.color.transparent)
        }
    }

    private fun initObservers() {
        viewModel.volumenLiveData.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Error -> showErrorView(result.error)
                is Resource.Loading -> showLoadingView()
                is Resource.Success -> showSuccessView(result.data)
            }
        })
        viewModel.sendDataLiveData.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Error -> showErrorView(result.error)
                is Resource.Loading -> showLoadingView()
                is Resource.Success -> showSuccessViewSendData(result.data)
            }
        })
    }

    private fun showSuccessViewSendData(result: Boolean?) {

        if (result != null) {

            if (result) {
                view?.makeSnackbar(DATA_SENT, true)
            } else {
                view?.makeSnackbar(DATA_SAVE, true)
            }

        }
        clear()
        customProgressDialog.dismiss()
    }

    private fun showSuccessView(data: Volumen?) {
        binding.apply {
            data?.info_volumen?.apply {
                edtHigh.setText(alto.toString())
                edtLong.setText(largo.toString())
                edtWidth.setText(ancho.toString())
                edtVolume.setText(volumen.toString())
            }
        }
        nameImage = data?.imagen.toString()
        val pathImage = ip + URL_IMAGEN + nameImage
        Glide.with(requireContext()).load(pathImage).into(binding.imgResult)
        customProgressDialog.dismiss()
    }

    private fun showLoadingView() {
        customProgressDialog.setContentView(R.layout.custom_dialog)
        customProgressDialog.setCancelable(false)
        customProgressDialog.show()
    }

    private fun showErrorView(error: Throwable?) {
        binding.apply {
            customProgressDialog.dismiss()
            view?.makeSnackbar(error?.message.toString(), false)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}