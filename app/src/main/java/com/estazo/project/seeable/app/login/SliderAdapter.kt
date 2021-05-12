package com.estazo.project.seeable.app.login

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.estazo.project.seeable.app.R


/**import library for PagerAdapter*/
class SliderAdapter(var context: Context) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null

    private val subHeader = intArrayOf(
        R.string.subheading_slide_page_1,
        R.string.subheading_slide_page_2,
        R.string.subheading_slide_page_3,
        R.string.subheading_slide_page_4,
        R.string.subheading_slide_page_5
    )
    private val description = intArrayOf(
        R.string.content_page_1,
        R.string.content_page_2,
        R.string.content_page_3,
        R.string.content_page_4,
        R.string.content_page_5
    )
    private val imgRes = intArrayOf(
        R.mipmap.img_intro1_foreground,
        R.mipmap.img_intro2_foreground,
        R.mipmap.img_intro3_foreground,
        R.mipmap.img_intro4_foreground,
        R.mipmap.img_intro5_foreground
    )
    private lateinit var tvSubHeader: TextView
    private lateinit var tvDesc: TextView
    private lateinit var img: ImageView
    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o
    }

    override fun getCount(): Int {
        return subHeader.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater!!.inflate(R.layout.slide_content, container, false)
        tvSubHeader = view.findViewById(R.id.sub_heading)
        tvDesc = view.findViewById(R.id.content)
        img = view.findViewById(R.id.imageView)
        tvSubHeader.setText(subHeader[position])
        tvDesc.setText(description[position])
        img.setImageResource(imgRes[position])
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

}