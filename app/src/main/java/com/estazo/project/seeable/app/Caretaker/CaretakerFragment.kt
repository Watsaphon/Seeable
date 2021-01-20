package com.estazo.project.seeable.app.Caretaker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.estazo.project.seeable.app.R
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.work.*
import com.estazo.project.seeable.app.Register.BPMWorker
import com.estazo.project.seeable.app.databinding.FragmentCaretakerBinding
import java.util.concurrent.TimeUnit


class CaretakerFragment : Fragment() {

    private lateinit var binding : FragmentCaretakerBinding

    private lateinit var viewModel: CaretakerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_caretaker, container, false)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_caretaker,
            container,
            false
        )
        Log.i("CaretakerFragment", "Called ViewModelProvider.get")

        viewModel = ViewModelProvider(this).get(CaretakerViewModel::class.java)

        viewModel.bpm.observe(viewLifecycleOwner , Observer {newBPM ->
            binding.bpmNumber.text = newBPM.toString()
        })

        val bpmValue = workDataOf("bpm" to "no-value")
        val constraint = Constraints.Builder().apply { setRequiredNetworkType(NetworkType.CONNECTED) }.build()
        //one time
        val request = PeriodicWorkRequestBuilder<BPMWorker>(10, TimeUnit.SECONDS).apply {
            setInputData(bpmValue)
            setConstraints(constraint)
        }.build()

        WorkManager.getInstance().enqueue(request)

        return binding.root
    }



}