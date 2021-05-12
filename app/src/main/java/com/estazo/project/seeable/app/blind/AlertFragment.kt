package com.estazo.project.seeable.app.blind

import android.app.AlertDialog
import android.content.Context
import android.os.*
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
        Log.i("AlertFragment", "onCreate call")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alert,container, false)
        Log.i("AlertFragment", "onCreateView call")
        arguments?.let {
            val detect = AlertFragmentArgs.fromBundle(it).detect
            check = detect
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("AlertFragment", "onViewCreated call")
        when(check){
            "crosswalk" -> {alertDialogCrosswalkDetection()}
            "bussign" -> {alertDialogBusSignDetection()}
        }
    }


    private fun alertDialogBusSignDetection() {
        //Inflate the dialog with custom view
        val mDialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.alert_dialog_bus_sign_detection, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(requireContext()).setView(mDialogView)

        //show dialog
        mAlertDialog = mBuilder.show()
        mAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.setCancelable(false)
        mAlertDialog.show()

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
        textToSpeech!!.setSpeechRate(0.9f)

        mDialogView.busSignDetection.setOnVeryLongClickListener {
            vibrate()
            findNavController().navigate(R.id.action_alertFragment_to_blindFragment2)
            mAlertDialog.dismiss()
        }

    }

    private fun alertDialogCrosswalkDetection() {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(requireActivity()).inflate(R.layout.alert_dialog_crosswalk_detection, null)
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
                        findNavController().navigate(R.id.action_alertFragment_to_blindFragment2)
                        mAlertDialog.dismiss()
                        }
                        /** Double tap*/
                        else if (numberOfTaps == 2) { handler.postDelayed(Runnable {
                        vibrate()
                        findNavController().navigate(R.id.action_alertFragment_to_blindFragment2)
                        mAlertDialog.dismiss()
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