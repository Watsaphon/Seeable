package com.estazo.project.seeable.app.blind

import android.app.AlertDialog
import android.content.Context
import android.os.*
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.estazo.project.seeable.app.MainActivity
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.databinding.FragmentNavigateBlindBinding
import com.estazo.project.seeable.app.objectDetection.TFLiteDetection
import kotlinx.android.synthetic.main.alert_dialog_bus_sign_detection.view.*
import kotlinx.android.synthetic.main.alert_dialog_crosswalk_detection.view.*
import java.util.*


class NavigateBlindFragment : Fragment() {

    private lateinit var binding: FragmentNavigateBlindBinding
    private lateinit var  mAlertDialog : AlertDialog
    private var textToSpeech: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("NavigateBlindFragment", "onCreate call")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_navigate_blind, container, false)
        Log.i("NavigateBlindFragment", "onCreateView call")


        binding.camera.setLifecycleOwner(activity)

        binding.camera.addFrameProcessor{ frame ->
            TFLiteDetection(requireActivity()).detect(frame)
//            NotificationService().detect(frame)
        }

//        binding.exit.setOnClickListener{
//            requireActivity().onBackPressed()
//        }

//        alertDialogBusSignDetection()
//        alertDialogCrosswalkDetection()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Log.i("NavigateBlindFragment", "onStart call")
        binding.camera.open()

    }

    override fun onResume() {
        super.onResume()
        Log.i("NavigateBlindFragment", "onResume call")

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
    }

    fun receiveIMG(msg : String){
            if(msg != null){
                binding.camera.close()
                binding.camera.destroy()
                Log.i("Score","receiveIMG msg = $msg")
//                if (this::mAlertDialog.isInitialized) {
//                    if (mAlertDialog.isShowing){
//                        Log.i("Score","if mAlertDialog.isShowing")
//                        /**nothing do*/
//                    }
//                    else {
//                        Log.i("Score","else ja")
//                        when (msg) {
//                            "0.0" -> {
//                                Log.i("Score","in when msg = $msg ")
//                                alertDialogCrosswalkDetection()
//                            }
//                            "1.0"->{
//                                Log.i("Score","in when msg = $msg ")
//                                alertDialogBusSignDetection()
//                            }
//                        }
//                    }
                when (msg) {
                            "0.0" -> {
                                Log.i("Score","1 in when msg = $msg ")
//                                MainActivity().let {
//                                    Log.i("Score","2 in when msg = $msg ")
//                                    it.checkDetection("crosswalk")
//                                }
                                checkDetection("crosswalk")
                                alertDialogCrosswalkDetection()
                            }
                            "1.0"->{
                                Log.i("Score","3 in when msg = $msg ")
                                MainActivity().let {
                                    Log.i("Score","4 in when msg = $msg ")
                                    it.checkDetection("bussign")
                                }
//                                Log.i("Score","in when msg = $msg ")
//                                checkDetection("bussign")
//                                alertDialogBusSignDetection()
                            }
                }
            }
    }

    private fun checkDetection( detect : String ){
        if(detect == "crosswalk" ){
            Log.i("Score","detect = crosswalk")
            alertDialogCrosswalkDetection()
        }
        else if(detect == "bussign"){
            Log.i("Score","detect = bussign")
            alertDialogBusSignDetection()
        }
//        if (!mAlertDialog.isShowing){
//            Log.i("checkdt"," no dialog alert")
//        }
//        else{
//            Log.i("checkdt","have dialog alert")
//
//        }

    }

    private fun alertDialogBusSignDetection() {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.alert_dialog_bus_sign_detection, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(activity).setView(mDialogView)
        //show dialog
        mAlertDialog  = mBuilder.show()
        mAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.setCancelable(false)

        textToSpeech = TextToSpeech(activity, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech!!.language = Locale.US
                textToSpeech!!.speak(getString(R.string.busSignDetection_speak), TextToSpeech.QUEUE_FLUSH, null)
            }
        })
        textToSpeech!!.setSpeechRate(0.9f)

        mDialogView.busSignDetection.setOnVeryLongClickListener{
            vibrate()
//            val tts = getString(R.string.fallDetection_fine)
//            textToSpeech!!.speak(tts, TextToSpeech.QUEUE_FLUSH, null)
            mAlertDialog.dismiss()
        }

    }

    private fun alertDialogCrosswalkDetection() {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(requireActivity()).inflate(R.layout.alert_dialog_crosswalk_detection, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(requireActivity()).setView(mDialogView)
        //show dialog
        mAlertDialog  = mBuilder.show()
        mAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.setCancelable(false)

        textToSpeech = TextToSpeech(requireActivity(), TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech!!.language = Locale.US
                textToSpeech!!.speak(getString(R.string.crosswalkDetection_speak), TextToSpeech.QUEUE_FLUSH, null)
            }
        })
        textToSpeech!!.setSpeechRate(0.9f)

        mDialogView.crosswalkDetection.setOnVeryLongClickListener{
            vibrate()
//            val tts = getString(R.string.fallDetection_fine)
//            textToSpeech!!.speak(tts, TextToSpeech.QUEUE_FLUSH, null)
            mAlertDialog.dismiss()
        }

    }

    /** function press and hold button for few seconds */
    private fun View.setOnVeryLongClickListener(listener: () -> Unit) {
        setOnTouchListener(object : View.OnTouchListener {
            private val longClickDuration = 2000L
            private val handler = Handler()
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    handler.postDelayed({ listener.invoke() }, longClickDuration)
                } else if (event?.action == MotionEvent.ACTION_UP) {
                    handler.removeCallbacksAndMessages(null)
                }
                return true
            }
        })
    }
    /** function vibrations */
    private fun vibrate(){
        // Vibrate for 300 milliseconds
        val v = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
        }
        else {
            //deprecated in API 26
            v.vibrate(300)
        }
    }



}

//interface receiveIMG(msg : String){
//
//}

