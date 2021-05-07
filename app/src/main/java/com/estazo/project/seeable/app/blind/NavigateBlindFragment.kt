package com.estazo.project.seeable.app.blind

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.databinding.FragmentNavigateBlindBinding
import com.estazo.project.seeable.app.objectDetection.TFLiteDetection

class NavigateBlindFragment : Fragment() {


    private lateinit var binding: FragmentNavigateBlindBinding
    private lateinit var sharedPrefNavigate: SharedPreferences

    private lateinit var viewModel: NavigateBlindViewModel
    private var isAlreadyFound = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("NavigateBlindFragment", "onCreate call")
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // in here you can do logic when backPress is clicked
                sharedPrefNavigate = requireActivity().getSharedPreferences("value", 0)
                val editor = sharedPrefNavigate.edit()
                editor.putString("stringKeyNavigate", "not found!")
                editor.apply()
                val launchIntent =
                    requireActivity().packageManager.getLaunchIntentForPackage("com.estazo.project.seeable.app")
                startActivity(launchIntent)
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_navigate_blind, container, false)
        Log.i("NavigateBlindFragment", "onCreateView call")

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("NavigateBlindFragment", "onActivityCreated call")

    }

    override fun onStart() {
        super.onStart()
        Log.i("NavigateBlindFragment", "onStart call")


    }

    override fun onResume() {
        super.onResume()
        Log.i("NavigateBlindFragment", "onResume call")

        binding.camera.open()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //        viewModel = ViewModelProviders.of(this).get(NavigateBlindViewModel::class.java)
        viewModel = ViewModelProvider(this).get(NavigateBlindViewModel::class.java)
        var a = viewModel.detect.hasActiveObservers()
        var b = viewModel.detect.hasObservers()
        viewModel.detectDialog().observe(viewLifecycleOwner, Observer<String> { detect ->
            Log.i("NavigateBlindFragment", "viewModel detect = $detect")
//            binding.test.text = " detect si wa"
//            if (detect == "0.0") {
//                binding.test.text = detect
////                alertDialogCrosswalkDetection()
//            } else if (detect == "1.0") {
//                binding.test.text = detect
////                alertDialogBusSignDetection()
//            }
        })
        binding.camera.setLifecycleOwner(null)
        binding.camera.addFrameProcessor { frame ->
            TFLiteDetection(requireContext(), onDetect = { it ->
                receiveIMG(it)
            }).detect(frame)
//            tfLiteDetection.detect(frame)
        }
//        //        binding.camera.setLifecycleOwner(viewLifecycleOwner)
//        val tfLiteDetection = TFLiteDetection(requireContext()) {
//            receiveIMG(it)
//        }
        binding.camera.open()
        a = viewModel.detect.hasActiveObservers()
        b = viewModel.detect.hasObservers()

    }

    fun onDetect(s: String) {
        receiveIMG(s)
    }

    override fun onPause() {
        super.onPause()
        Log.i("NavigateBlindFragment", "onPause call")
        binding.camera.close()
    }

    override fun onStop() {
        super.onStop()
        Log.i("NavigateBlindFragment", "OnStop call")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("NavigateBlindFragment", "onDestroyView call")
        binding.camera.destroy()
        sharedPrefNavigate = requireActivity().getSharedPreferences("value", 0)
        val editor = sharedPrefNavigate.edit()
        editor.putString("stringKeyNavigate", "not found!")
        editor.apply()
    }

    fun receiveIMG(msg: String) {
        val a = viewModel.detect.hasActiveObservers()
        val b = viewModel.detect.hasObservers()
        if (msg != null) {
            Log.i("Score", "receiveIMG msg = $msg")
            checkDetection("$msg")
        }
    }

    private fun checkDetection(detect: String) {
//        viewModel.detect.value = "$detect"
        val a = viewModel.detect.hasActiveObservers()
        val b = viewModel.detect.hasObservers()
        Log.i("Score", "checkDetection call")

        if (detect == "0.0") {
            Log.i("Score", "detect = crosswalk")
            binding.camera.close()
            val a = viewModel.detect.hasActiveObservers()
            val b = viewModel.detect.hasObservers()
            if (viewModel != null) {
                viewModel.detect.postValue(detect)
            }
            if (isAlreadyFound) {
            } else {
                isAlreadyFound = true
                val action =
                    NavigateBlindFragmentDirections.actionNavigateBlindFragmentToAlertFragment("crosswalk")
                findNavController().navigate(action)
            }
        } else if (detect == "1.0") {
            binding.camera.close()
            val a = viewModel.detect.hasActiveObservers()
            val b = viewModel.detect.hasObservers()
            Log.i("Score", "detect = bussign")
            if (viewModel != null) {
                var a = viewModel.detect.hasActiveObservers()
                var b = viewModel.detect.hasObservers()
                viewModel.detect.postValue(detect)
                a = viewModel.detect.hasActiveObservers()
                b = viewModel.detect.hasObservers()

            } else {
                viewModel.detect.value = "$detect"
                viewModel.detect.postValue(detect)
            }
            if (isAlreadyFound) {
            } else {
                isAlreadyFound = true
                val action =
                    NavigateBlindFragmentDirections.actionNavigateBlindFragmentToAlertFragment("bussign")
                findNavController().navigate(action)
            }
        }

    }
}

