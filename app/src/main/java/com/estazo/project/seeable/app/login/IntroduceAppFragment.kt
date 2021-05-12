package com.estazo.project.seeable.app.login

import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.databinding.FragmentIntroduceAppBinding
import java.util.*


class IntroduceAppFragment : Fragment() {

    private lateinit var binding : FragmentIntroduceAppBinding

    private var currentPage = 0

    private lateinit var dot: Array<TextView?>

    private lateinit var sharedPrefLanguage: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("IntroduceAppFragment", "call onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_introduce_app,container, false)
        Log.i("IntroduceAppFragment", "call onCreateView")

        sharedPrefLanguage = requireActivity().getSharedPreferences("value", 0)

        val text = "EN|TH"
        val ssb = SpannableStringBuilder(text)
        val fcsWhite = ForegroundColorSpan(Color.WHITE)
        val fcsGreen = ForegroundColorSpan(Color.GREEN)

        val language = sharedPrefLanguage.getString("stringKey", "not found!")
        if(language == "en"){
            ssb.setSpan(fcsWhite, 3, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            ssb.setSpan(fcsGreen, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.enTh.text = ssb
        }
        else if(language =="th"){
            ssb.setSpan(fcsWhite, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            ssb.setSpan(fcsGreen, 3, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.enTh.text = ssb
        }

        val adapter = SliderAdapter(requireActivity())
        binding.slideViewPager.adapter = adapter
        //Add Indicator
        addIndicator(0)
        binding.slideViewPager.addOnPageChangeListener(listener)
        binding.nextButton.setOnClickListener {
            gotoLogin()
            binding.slideViewPager.currentItem = currentPage + 1
        }
        binding.skipButton.setOnClickListener { skipLogin() }
        binding.enTh.setOnClickListener { changeLanguage() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("IntroduceAppFragment", "call onViewCreated")

    }

    private fun addIndicator(position: Int) {
        dot = arrayOfNulls(5)
        binding.dotsLayout.removeAllViews()
        for (i in dot.indices) {
            dot[i] = TextView(activity)
            dot[i]!!.text = Html.fromHtml("&#8226;")
        }
        if (dot.isNotEmpty()) dot[position]!!.setTextColor(resources.getColor(R.color.colorWhite))
    }

    private var listener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(p0: Int) {}
        override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}
        override fun onPageSelected(p0: Int) {
            currentPage = p0
            addIndicator(p0)
        }
    }

    private fun gotoLogin() {
        if (currentPage == dot.size - 1) {
            findNavController().navigate(R.id.action_introduceAppFragment_to_loginScreen)
        }
    }

    private fun skipLogin() {
        findNavController().navigate(R.id.action_introduceAppFragment_to_loginScreen)
    }

    /** change Language TH and EN  */
    private fun changeLanguage(){
        val language = sharedPrefLanguage.getString("stringKey", "not found!")
        Log.i("CheckLanguage", "Now Language is :$language ")
        var locale: Locale? = null
        val editor = sharedPrefLanguage.edit()
        if (language=="en") {
            locale = Locale("th")
            editor.putString("stringKey", "th")
            editor.apply()
        } else if (language =="th") {
            locale = Locale("en")
            editor.putString("stringKey", "en")
            editor.apply()
        }
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        requireActivity().baseContext.resources.updateConfiguration(config, null)
        findNavController().navigate(R.id.introduceAppFragment)
    }

}