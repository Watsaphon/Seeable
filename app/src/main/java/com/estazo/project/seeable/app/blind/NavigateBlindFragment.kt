package com.estazo.project.seeable.app.blind

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.estazo.project.seeable.app.NotificationService
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.databinding.FragmentNavigateBlindBinding
import kotlinx.android.synthetic.main.alert_dialog_critical_event.view.*


class NavigateBlindFragment : Fragment() {

    private lateinit var binding: FragmentNavigateBlindBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("NavigateBlindFragment", "onCreate call")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_navigate_blind, container, false)
        Log.i("NavigateBlindFragment", "onCreateView call")



        binding.camera.setLifecycleOwner(activity)

        binding.camera.addFrameProcessor {
            NotificationService().detect(it)
        }

        binding.exit.setOnClickListener{
            requireActivity().onBackPressed()
        }

        return binding.root
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

    private fun alertDialogBusSignDetection() {

        val dialogBuilder = AlertDialog.Builder(activity)
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.alert_dialog_bus_sign_detection, null)

        dialogBuilder.setView(mDialogView)


        val dialog: AlertDialog = dialogBuilder.create()
        val dialogWindow: Window = dialog.window!!
        val dialogWindowAttributes: WindowManager.LayoutParams = dialogWindow.attributes

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogWindowAttributes)
        lp.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 280f, resources.displayMetrics).toInt()
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialogWindow.attributes = lp

        dialogWindow.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
//        dialogWindowAttributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        mDialogView.callAmbulance.setOnClickListener{
            Log.i("dialog","click call Ambulance ja")
            dialog.dismiss()
        }

        mDialogView.getLocation.setOnClickListener{
            Log.i("dialog","click get Location ja")
            dialog.dismiss()
        }

    }

}


