package com.estazo.project.seeable.app.login

import android.content.Intent
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
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.estazo.project.seeable.app.R
import com.estazo.project.seeable.app.SplashScreen
import com.estazo.project.seeable.app.databinding.FragmentIntroduceAppBinding
import java.util.*


class IntroduceAppFragment : Fragment() {

    private lateinit var binding : FragmentIntroduceAppBinding

    private var currentPage = 0
//    private var dotsLayout: LinearLayout? = null
    private lateinit var dot: Array<TextView?>
//    private lateinit var slideViewPager: ViewPager
//    lateinit var btn_next: Button
//    private lateinit var skip :TextView
//    private lateinit var changeLang :TextView
    private lateinit var sharedPrefLanguage: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_introduce_app)
//        hideSystemUI()
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
//
//        sharedPrefLanguage = getSharedPreferences("value", 0)
//
//        dotsLayout = findViewById(R.id.dotsLayout)
//        slideViewPager = findViewById(R.id.slideViewPager)
//        btn_next = findViewById(R.id.next_button)
//        skip = findViewById(R.id.skip_button)
//        changeLang = findViewById(R.id.en_th)
//
//
//        val text = "EN|TH"
//        val ssb = SpannableStringBuilder(text)
//        val fcsWhite = ForegroundColorSpan(Color.WHITE)
//        val fcsGreen = ForegroundColorSpan(Color.GREEN)
//
//        val language = sharedPrefLanguage.getString("stringKey", "not found!")
//        if(language == "en"){
//            ssb.setSpan(fcsWhite, 3, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            ssb.setSpan(fcsGreen, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            changeLang.text = ssb
//        }
//        else if(language =="th"){
//            ssb.setSpan(fcsWhite, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            ssb.setSpan(fcsGreen, 3, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            changeLang.text = ssb
//        }
//
//        val adapter = SliderAdapter(this)
//        slideViewPager.adapter = adapter
//        //Add Indicator
//        addIndicator(0)
//        slideViewPager.addOnPageChangeListener(listener)
//        btn_next.setOnClickListener(View.OnClickListener {
//            gotoLogin()
//            slideViewPager.currentItem = currentPage + 1
//        })
//        skip.setOnClickListener(View.OnClickListener { skipLogin() })
//        changeLang.setOnClickListener(View.OnClickListener { changeLanguage() })
//        Log.i("Slide Activity", "Slide called")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_introduce_app,container, false)

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
        binding.nextButton.setOnClickListener(View.OnClickListener {
            gotoLogin()
            binding.slideViewPager.currentItem = currentPage + 1
        })
        binding.skipButton.setOnClickListener(View.OnClickListener { skipLogin() })
        binding.enTh.setOnClickListener(View.OnClickListener { changeLanguage() })
        Log.i("Slide Activity", "Slide called")


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun addIndicator(position: Int) {
        dot = arrayOfNulls(5)
        binding.dotsLayout.removeAllViews()
        for (i in dot.indices) {
            dot[i] = TextView(activity)
            dot[i]!!.text = Html.fromHtml("&#8226;")
            dot[i]!!.textSize = 35.0f
//            dot[i]!!.setTextColor(resources.getColor(R.color.colorTransWhite))
            binding.dotsLayout.addView(dot[i])
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
//            val intent = Intent(this, LoginScreen::class.java)
//            startActivity(intent)
        }
    }

    private fun skipLogin() {
        findNavController().navigate(R.id.action_introduceAppFragment_to_loginScreen)
//        val intent = Intent(this, LoginScreen::class.java)
//        startActivity(intent)
//        finish()
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