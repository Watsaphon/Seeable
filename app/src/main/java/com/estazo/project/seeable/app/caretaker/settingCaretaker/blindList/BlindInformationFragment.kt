package com.estazo.project.seeable.app.caretaker.settingCaretaker.blindList

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.estazo.project.seeable.app.MainActivity
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.databinding.FragmentBlindInformationBinding
import com.estazo.project.seeable.app.helperClass.Blind
import com.estazo.project.seeable.app.register.LittleMoreFragmentArgs
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class BlindInformationFragment : Fragment() {

    private lateinit var binding : FragmentBlindInformationBinding

    private val blindInfoViewModel : BlindInformationViewModel by activityViewModels()

    private lateinit var sharedPrefLanguage: SharedPreferences
    private lateinit var sharedPrefPhone: SharedPreferences
    private lateinit var phoneCaretaker  : String
    private lateinit var language : String

    private lateinit var selectPhoneBlind: String
    private lateinit var selectUsernameBlind: String
    private lateinit var  positionBlindUser : String

    private lateinit var phoneBlind : String

    private lateinit var  mAlertDialog : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("BlindInformation", "onCreate call")
        sharedPrefLanguage = requireActivity().getSharedPreferences("value", 0)
        sharedPrefPhone= requireActivity().getSharedPreferences("value", 0)

        language = sharedPrefLanguage.getString("stringKey", "not found!").toString()
        phoneCaretaker  = sharedPrefPhone.getString("stringKeyPhone", "not found!").toString()

        arguments?.let {
            val mobile = BlindInformationFragmentArgs.fromBundle(it).phoneBlind
            phoneBlind = mobile
        }
        Log.i("BlindInformation", "phoneBlind : $phoneBlind")
//        val query = FirebaseDatabase.getInstance().getReference("users_blind").child("$phoneBlind")
//        query.addListenerForSingleValueEvent(valueEventListener)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_blind_information, container, false)
        Log.i("BlindInformation", "onCreateView call")

        val query = FirebaseDatabase.getInstance().getReference("users_blind").child("$phoneBlind")
        query.addListenerForSingleValueEvent(valueEventListener)

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("BlindInformation", "onViewCreated call")

        binding.editNameButton.setOnClickListener {
            updateDisplayName()
        }

        binding.saveBtn.setOnClickListener {
            (activity as MainActivity).closeKeyboard()
            addDisplayName()
        }

        blindInfoViewModel.user.observe(viewLifecycleOwner, Observer<String> { user ->
            selectUsernameBlind = user.toString()
            binding.displayName.text = selectUsernameBlind
        })
        blindInfoViewModel.phone.observe(viewLifecycleOwner, Observer<String> { phone ->
            selectPhoneBlind = phone.toString()
            if(language == "en"){
                binding.phone.text = "Phone : $selectPhoneBlind"
            }
            else if(language =="th"){
                binding.phone.text = "เบอร์โทรศัพท์ : $selectPhoneBlind"
            }

        })
        blindInfoViewModel.position.observe(viewLifecycleOwner, Observer<Int> { position ->
            positionBlindUser = position.toString()
        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("BlindInformation", "onActivityCreated call")
        val ref = FirebaseDatabase.getInstance().reference

        binding.editBlind.setOnClickListener{
            Toast.makeText(activity,"you can't edit this blind destination ",Toast.LENGTH_SHORT).show()
        }
        binding.deleteBlind.setOnClickListener{
            Toast.makeText(activity,"you can't delete this blind destination ",Toast.LENGTH_SHORT).show()
        }
        binding.editCare.setOnClickListener{
            alertDialogLoading()
            val action = BlindInformationFragmentDirections.actionBlindInformationFragmentToSearchLocationCaretakerFragment(selectPhoneBlind)
            findNavController().navigate(action)
        }
        binding.deleteCare.setOnClickListener{
            val childUpdates = hashMapOf<String, Any>("users_blind/$selectPhoneBlind/Navigation/navigate_careUser" to "-")
            ref.updateChildren(childUpdates)
            val childUpdates2 = hashMapOf<String, Any>("users_blind/$selectPhoneBlind/Navigation/title_Navigate_careUser" to "-")
            ref.updateChildren(childUpdates2)
            binding.careNavigate.visibility = View.GONE
            binding.addNavigate.visibility = View.VISIBLE

        }
        binding.addNavigate.setOnClickListener{
            alertDialogLoading()
            val action = BlindInformationFragmentDirections.actionBlindInformationFragmentToSearchLocationCaretakerFragment(selectPhoneBlind)
            findNavController().navigate(action)
        }
    }

    private fun updateDisplayName() {
        binding.editName.visibility = View.VISIBLE
        binding.saveBtn.visibility = View.VISIBLE

        binding.displayName.visibility = View.GONE
        binding.editNameButton.visibility = View.GONE

        // Set the focus to the edit text.
        binding.editName.requestFocus()

    }

    private fun addDisplayName() {
        val oldName : String = selectUsernameBlind
        var updateName : String  = ""
        updateName =  binding.editName.text.toString()

        when {
            updateName.isEmpty() -> {
                Toast.makeText(activity,R.string.update_empty_BlindInformationFragment, Toast.LENGTH_LONG).show()
            }
            updateName == selectUsernameBlind -> {
                Toast.makeText(activity,R.string.update_same_BlindInformationFragment, Toast.LENGTH_LONG).show()
            }
            else -> {
                selectUsernameBlind = updateName
                binding.displayName.text = updateName

                val ref = FirebaseDatabase.getInstance().reference
                val childUpdates = hashMapOf<String,Any>("users_blind/$selectPhoneBlind/displayName" to updateName)
                ref.updateChildren(childUpdates)
                val childUpdates2 = hashMapOf<String,Any>("users_caretaker/$phoneCaretaker/Blind/user$positionBlindUser" to "$selectPhoneBlind/$updateName")
                ref.updateChildren(childUpdates2)

                binding.editName.visibility = View.GONE
                binding.saveBtn.visibility = View.GONE

                binding.displayName.visibility = View.VISIBLE
                binding.editNameButton.visibility = View.VISIBLE

                binding.editName.text.clear()

                Toast.makeText(activity,R.string.update_success_BlindInformationFragment, Toast.LENGTH_LONG).show()

            }
        }
    }

    /**remove user from realtime database (users_caretaker) */
    private var valueEventListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {

                val titleBlind =
                    dataSnapshot.child("Navigation/title_Navigate_blindUser").value.toString()
                val locationBlind =
                    dataSnapshot.child("Navigation/navigate_blindUser").value.toString()
                val titleCaretaker =
                    dataSnapshot.child("Navigation/title_Navigate_careUser").value.toString()
                val locationCaretaker =
                    dataSnapshot.child("Navigation/navigate_careUser").value.toString()
                Log.i("BlindInformation", "titleBlind : $titleBlind , locationBlind : $locationBlind ," +
                        " titleCaretaker : $titleCaretaker , locationCaretaker : $locationCaretaker ")
                var numUser : Int = 0
                if(titleBlind != "-" && locationBlind != "-" ){
                    binding.blindNavigate.visibility = View.VISIBLE
                    binding.titleLocationBlind.text = "$titleBlind (only Blind)"
//                    ++numUser
                }
                if(titleCaretaker != "-" && locationCaretaker != "-" ){
                    binding.careNavigate.visibility = View.VISIBLE
                    binding.titleLocationCare.text = "$titleCaretaker"
                    ++numUser
                }
                if(numUser < 1){
                    binding.addNavigate.visibility = View.VISIBLE
                }
            }

        }
        override fun onCancelled(databaseError: DatabaseError) {}
    }

    /** AlertDialog to loading  */
    private fun alertDialogLoading() {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.loading_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(activity).setView(mDialogView)
        //show dialog
        mAlertDialog  = mBuilder.show()
        mAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.setCancelable(false)
    }

    override fun onPause() {
        super.onPause()
        Log.i("BlindInformation", "onPause call")
    }

    override fun onStop() {
        super.onStop()
        Log.i("BlindInformation", "onStop call")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("BlindInformation", "onDestroyView call")
        if (this::mAlertDialog.isInitialized) {
            if (mAlertDialog.isShowing) {
                Log.i("pppp", "alert is showing in if")
                mAlertDialog.dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("BlindInformation", "onDestroy call")
    }

}