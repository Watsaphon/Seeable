package com.estazo.project.seeable.app.Caretaker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.estazo.project.seeable.app.Device.BPMRunnable
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.databinding.FragmentCaretakerBinding



class CaretakerFragment : Fragment() {

    private lateinit var binding : FragmentCaretakerBinding

    private lateinit var viewModel: CaretakerViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Spinner Drop down elements
        val user: MutableList<String> = ArrayList()
        user.add("Item 1")
        user.add("Item 2")
        user.add("Item 3")
        user.add("Item 4")

        val types = arrayOf("John Jenkin Jr.", "Pikulkeaw","Puttaku","Rataros")

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_caretaker, container, false)
        Log.i("CaretakerFragment", "Called ViewModelProvider.get")
        Log.i("CaretakerFragment", "onCreateView call")
        viewModel = ViewModelProvider(this).get(CaretakerViewModel::class.java)


        val spinner = binding.spinner
//        spinner?.adapter = activity?.applicationContext?.let { ArrayAdapter<String>(it, R.layout.support_simple_spinner_dropdown_item, user) }
        spinner?.adapter = ArrayAdapter(activity?.applicationContext!!, R.layout.list_name_blind, types)

        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("Hello world")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val type = parent?.getItemAtPosition(position).toString()
                Toast.makeText(activity,type, Toast.LENGTH_LONG).show()
                println(type)
            }

        }

        val bpm_thread = Thread(BPMRunnable(binding.bpmNumber))
        bpm_thread.start()

        binding.setting.setOnClickListener{view : View  ->
            view.findNavController().navigate(R.id.action_caretakerFragment_to_settingCaretakerFragment)
        }


        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i("CaretakerFragment", "onAttach call")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("CaretakerFragment", "onCreate call")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("CaretakerFragment", "onActivityCreated call")
    }
    override fun onStart() {
        super.onStart()
        Log.i("CaretakerFragment", "onStart call")
    }
    override fun onResume() {
        super.onResume()
        Log.i("CaretakerFragment", "onResume call")
    }
    override fun onPause() {
        super.onPause()
        Log.i("CaretakerFragment", "onPause call")
    }
    override fun onStop() {
        super.onStop()
        Log.i("CaretakerFragment", "onStop call")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("CaretakerFragment", "onDestroyView call")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i("CaretakerFragment", "onDestroy call")
    }
    override fun onDetach() {
        super.onDetach()
        Log.i("CaretakerFragment", "onDetach call")
    }

}