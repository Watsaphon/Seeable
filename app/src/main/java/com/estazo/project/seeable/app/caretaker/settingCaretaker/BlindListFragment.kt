package com.estazo.project.seeable.app.caretaker.settingCaretaker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.caretaker.CaretakerViewModel
import com.estazo.project.seeable.app.databinding.FragmentBlindListBinding


class BlindListFragment : androidx.fragment.app.Fragment() {

    private lateinit var binding : FragmentBlindListBinding


//    private lateinit var viewModel: BlindListViewModel

    private val caretakerViewModel: CaretakerViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("BlindListFragment", "onCreate call")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
//        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_blind_list, container, false)
        val fragmentBinding = FragmentBlindListBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        Log.i("BlindListFragment", "onCreateView call")

        binding.backButton.setOnClickListener{
            requireActivity().onBackPressed()
        }

        return fragmentBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("BlindListFragment", "onViewCreated call")
        binding?.apply {
            // Specify the fragment as the lifecycle owner
            lifecycleOwner = viewLifecycleOwner

            // Assign the view model to a property in the binding class
            viewModel = caretakerViewModel

            // Assign the fragment
            blindListFragment = this@BlindListFragment
        }

        caretakerViewModel.userDisplay.observe(viewLifecycleOwner, Observer<List<Any>>{user ->
            val size = user.size
            Log.i("sizeArray","size : $size")
            if(size >= 1){binding.nameUser1.text = user[0].toString()}
            if(size >= 2){binding.nameUser2.text = user[1].toString()}
            if(size >= 3){binding.nameUser3.text = user[2].toString()}
            if(size == 4){binding.nameUser4.text = user[3].toString()}

        })

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("BlindListFragment", "onActivityCreated call")

    }
    override fun onStart() {
        super.onStart()
        Log.i("BlindListFragment", "onStart call")
    }
    override fun onResume() {
        super.onResume()
        Log.i("BlindListFragment", "onResume call")
    }
    override fun onPause() {
        super.onPause()
        Log.i("BlindListFragment", "onPause call")
    }
    override fun onStop() {
        super.onStop()
        Log.i("BlindListFragment", "onStop call")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("BlindListFragment", "onDestroyView call")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i("BlindListFragment", "onDestroy call")
    }
    override fun onDetach() {
        super.onDetach()
        Log.i("BlindListFragment", "onDetach call")
    }


}