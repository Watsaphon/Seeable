package com.estazo.project.seeable.app.blind

import android.app.AlertDialog
import android.content.Context
import android.os.*
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.databinding.FragmentAlertBinding
import kotlinx.android.synthetic.main.alert_dialog_bus_sign_detection.view.*
import kotlinx.android.synthetic.main.alert_dialog_crosswalk_detection.view.*
import java.util.*


class AlertFragment : Fragment() {

    private lateinit var binding : FragmentAlertBinding
    private lateinit var mAlertDialog: AlertDialog

    var textToSpeech: TextToSpeech? = null

    private lateinit var check : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alert,container, false)

        arguments?.let {
            val detect = AlertFragmentArgs.fromBundle(it).detect
            check = detect
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when(check){
            "crosswalk" -> {alertDialogCrosswalkDetection()}
            "bussign" -> {alertDialogBusSignDetection()}
        }

    }


    private fun alertDialogBusSignDetection() {
        Toast.makeText(requireContext(),"asdfasdfasdf",Toast.LENGTH_SHORT).show()
        Log.i("checkdt", "in alert")
        //Inflate the dialog with custom view
        val mDialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.alert_dialog_bus_sign_detection, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(requireContext()).setView(mDialogView)
        //show dialog
        Log.i("checkdt", "set view already")

        mAlertDialog = mBuilder.show()
        Log.i("checkdt", "show ja")
        mAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.setCancelable(false)
        mAlertDialog.show()
        Log.i("checkdt", "set other")

        textToSpeech = TextToSpeech(context, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech!!.language = Locale.US
                textToSpeech!!.speak(
                    getString(R.string.busSignDetection_speak),
                    TextToSpeech.QUEUE_FLUSH,
                    null
                )
            }
        })
        Log.i("checkdt", "set tts")

        textToSpeech!!.setSpeechRate(0.9f)
        Log.i("checkdt", "use tts")

        mDialogView.busSignDetection.setOnVeryLongClickListener {
            vibrate()
//            val tts = getString(R.string.fallDetection_fine)
//            textToSpeech!!.speak(tts, TextToSpeech.QUEUE_FLUSH, null)
            mAlertDialog.dismiss()
            requireActivity().onBackPressed()
        }
        Log.i("checkdt", "end ja")

    }

    private fun alertDialogCrosswalkDetection() {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(requireActivity())
            .inflate(R.layout.alert_dialog_crosswalk_detection, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(requireContext()).setView(mDialogView)
        //show dialog
        val mAlertDialog = mBuilder.show()
        mAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.setCancelable(false)

        textToSpeech = TextToSpeech(requireContext(), TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech!!.language = Locale.US
                textToSpeech!!.speak(getString(R.string.crosswalkDetection_speak), TextToSpeech.QUEUE_FLUSH, null)
            } })
        textToSpeech!!.setSpeechRate(0.9f)

        // Implementing onTouchListener on our Text View
        mDialogView.crosswalkDetection.setOnTouchListener(object : View.OnTouchListener {
            // Handler to handle the number of clicks
            var handler: Handler = Handler()
            var numberOfTaps = 0
            var lastTapTimeMs: Long = 0
            var touchDownMs: Long = 0
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> touchDownMs = System.currentTimeMillis()
                    MotionEvent.ACTION_UP -> {
                        // Handle the numberOfTaps
                        handler.removeCallbacksAndMessages(null)
                        if (System.currentTimeMillis() - touchDownMs
                            > ViewConfiguration.getTapTimeout()) {
                            //it was not a tap
                            numberOfTaps = 0
                            lastTapTimeMs = 0
                        }
                        if (numberOfTaps > 0
                            && System.currentTimeMillis() - lastTapTimeMs
                            < ViewConfiguration.getDoubleTapTimeout()
                        ) {
                            // if the view was clicked once
                            numberOfTaps += 1
                        } else {
                            // if the view was never clicked
                            numberOfTaps = 1
                        }
                        lastTapTimeMs = System.currentTimeMillis()
                        /** Triple Tap*/
                        if (numberOfTaps == 3) {
                        vibrate()
                        val tts = getString(R.string.crosswalkDetection_help)
                        textToSpeech!!.speak(tts, TextToSpeech.QUEUE_FLUSH, null)
                        mAlertDialog.dismiss()
                        requireActivity().onBackPressed() }
                        /** Double tap*/
                        else if (numberOfTaps == 2) { handler.postDelayed(Runnable {
                        vibrate()
                        mAlertDialog.dismiss()
                        requireActivity().onBackPressed()
                        }, ViewConfiguration.getDoubleTapTimeout().toLong()) }
                    }
                }
                return true
            }
        })
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
    private fun vibrate() {
        // Vibrate for 300 milliseconds
        val v = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            //deprecated in API 26
            v.vibrate(300)
        }
    }

}

///** Double tab within a span of 0.5 sec */
//abstract class DoubleClickListener : View.OnClickListener {
//    var lastClickTime: Long = 0
//    override fun onClick(v: View?) {
//        val clickTime = System.currentTimeMillis()
//        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
//            onDoubleClick(v)
//        }
//        lastClickTime = clickTime
//    }
//
//    abstract fun onDoubleClick(v: View?)
//    companion object {
//        private const val DOUBLE_CLICK_TIME_DELTA: Long = 300 //milliseconds
//    }
//}