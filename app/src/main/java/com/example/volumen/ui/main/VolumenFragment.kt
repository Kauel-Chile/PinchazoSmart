package com.example.volumen.ui.main

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.volumen.R
import com.example.volumen.api.volumen.CodeRequest
import com.example.volumen.api.volumen.ResponseSendDataVolumen
import com.example.volumen.api.volumen.SendDataVolumen
import com.example.volumen.api.volumen.Volumen
import com.example.volumen.databinding.FragmentVolumenBinding
import com.example.volumen.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
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
        binding.apply {
            tvRutPortico.text = rut
            tvPortico.text = namePortico
            edtBarcode.setText(codeScandit)
        }
        customProgressDialog = Dialog(requireContext())
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
                    val code = CodeRequest(edtBarcode.text.toString())
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
                        if (validateSize()) {
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
            val idPhone =
                Settings.Secure.getString(activity?.contentResolver, Settings.Secure.ANDROID_ID)
            val current = LocalDateTime.now()
            val date = Date.from(current.atZone(ZoneId.systemDefault()).toInstant())
            val url = URL_WEB + URL_WEB_DATA_PORTICO
            binding.apply {
                val data1 = SendDataVolumen(
                    idPhone = idPhone,
                    high = stringToDouble(edtHigh.text.toString()),
                    long = stringToDouble(edtLong.text.toString()),
                    width = stringToDouble(edtWidth.text.toString()),
                    volumen = stringToDouble(edtVolume.text.toString()),
                    rut = tvRutPortico.text.toString(),
                    date = date,
                    portico = tvPortico.text.toString(),
                    bardCode = edtBarcode.text.toString(),
                    status = "OK")

                val data2 = SendDataVolumen(
                    idPhone = idPhone,
                    rut = tvRutPortico.text.toString(),
                    date = date,
                    portico = tvPortico.text.toString(),
                    bardCode = edtBarcode.text.toString(),
                    status = "SIN MEDIDAS")

                if (fullData) {
                    viewModel.sendDataVolumen(url, data1)
                    //viewModel.insertSendDataVolumen(data1)
                } else {
                    viewModel.sendDataVolumen(url, data2)
                    //viewModel.insertSendDataVolumen(data2)
                }
            }

        }
    }

    private fun validateSize(): Boolean {

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
        viewModel.sendDataVolumenLiveData.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Error -> showErrorView(result.error)
                is Resource.Loading -> showLoadingView()
                is Resource.Success -> showSuccessViewSendData(result.data)
            }
        })
//        viewModel.listData.observe(viewLifecycleOwner, { list ->
//            val listData = list
//        })
    }

    private fun showSuccessViewSendData(data: ResponseSendDataVolumen?) {
        val response = data?.result
        response?.let {
            if (it) {
                view?.makeSnackbar(DATA_SENT, true)
                clear()
            } else {
                view?.makeSnackbar(MSG_ERROR, false)
            }
        }
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
        val nameImage = data?.imagen
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

    var msg: String? = ""
    var lastMsg = ""

    @SuppressLint("Range")
    private fun downloadImage(url: String) {

        val directory = File(Environment.DIRECTORY_PICTURES)

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val downloadUri = Uri.parse(url)

        val request = DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(url.substring(url.lastIndexOf("/") + 1))
                .setDescription("")
                .setDestinationInExternalPublicDir(
                    directory.toString(),
                    url.substring(url.lastIndexOf("/") + 1)
                )
        }

        val downloadId = downloadManager.enqueue(request)
        val query = DownloadManager.Query().setFilterById(downloadId)
        Thread(Runnable {
            var downloading = true
            while (downloading) {
                val cursor: Cursor = downloadManager.query(query)
                cursor.moveToFirst()
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                msg = statusMessage(url, directory, status)
                if (msg != lastMsg) {
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                    lastMsg = msg ?: ""
                }
                cursor.close()
            }
        }).start()
    }

    private fun statusMessage(url: String, directory: File, status: Int): String? {
        var msg = ""
        msg = when (status) {
            DownloadManager.STATUS_FAILED -> "Download has been failed, please try again"
            DownloadManager.STATUS_PAUSED -> "Paused"
            DownloadManager.STATUS_PENDING -> "Pending"
            DownloadManager.STATUS_RUNNING -> "Downloading..."
            DownloadManager.STATUS_SUCCESSFUL -> "Image downloaded successfully in $directory" + File.separator + url.substring(
                url.lastIndexOf("/") + 1
            )
            else -> "There's nothing to download"
        }
        return msg
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}