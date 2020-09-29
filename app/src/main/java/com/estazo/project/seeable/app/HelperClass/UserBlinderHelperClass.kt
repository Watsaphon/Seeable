package com.estazo.project.seeable.app.HelperClass

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

//class UserBlinderHelperClass (val ID : String , val Username: String, val Password: String, val FullName: String, val Phone: String, val NameHelper: String, val PhoneHelper: String,val latitude: Int, val longitude: Int)

@IgnoreExtraProperties
data class UserBlinderHelperClass(
    val ID: String,
    val Username: String,
    val Password: String,
    val FullName: String,
    val Phone: String,
    val NameHelper: String,
    val PhoneHelper: String,
    val latitude: Double,
    val longitude: Double
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to ID,
            "username" to Username,
            "password" to Password,
            "fullName" to FullName,
            "phone" to Phone,
            "nameHelper" to NameHelper,
            "phoneHelper" to PhoneHelper,
            "latitude" to latitude,
            "longitude" to longitude
        )
    }
}



//        /** PopupWindow set gravity center */
//        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        // Inflate a custom view using layout inflater
//        val view = inflater.inflate(R.layout.another_view,null)
//
//        // Initialize a new instance of popup window
//        val popupWindow = PopupWindow(
//            view, // Custom view to show in popup window
//            LinearLayout.LayoutParams.WRAP_CONTENT, // Width of popup window
//            LinearLayout.LayoutParams.WRAP_CONTENT // Window height
//        )
//
//        // If API level 23 or higher then execute the code
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            // Create a new slide animation for popup window enter transition
//            val slideIn = Slide()
//            slideIn.slideEdge = Gravity.TOP
//            popupWindow.enterTransition = slideIn
//
//            // Slide animation for popup window exit transition
//            val slideOut = Slide()
//            slideOut.slideEdge = Gravity.RIGHT
//            popupWindow.exitTransition = slideOut
//
//        }
//
//        // Get the widgets reference from custom view
//        val tv = view.findViewById<TextView>(R.id.text_view)
//        val buttonPopupX = view.findViewById<Button>(R.id.button_popup)
//        val buttonPopup1 = view.findViewById<Button>(R.id.button_popup1)
//        val buttonPopup2= view.findViewById<Button>(R.id.button_popup2)
//        val buttonPopup3 = view.findViewById<Button>(R.id.button_popup3)
//
//
//        // Set click listener for popup window's text view
//        tv.setOnClickListener{
//            // Change the text color of popup window's text view
//            tv.setTextColor(Color.RED)
//        }
//
//        // Set a click listener for popup's button widget
//        buttonPopupX.setOnClickListener{
//            // Dismiss the popup window
//            popupWindow.dismiss()
//        }
//        buttonPopup1.setOnClickListener{
//            Toast.makeText(applicationContext,"TEST1 Press!!",Toast.LENGTH_SHORT).show()
//        }
//        buttonPopup2.setOnClickListener{
//            Toast.makeText(applicationContext,"TEST2 Press!!",Toast.LENGTH_SHORT).show()
//        }
//        buttonPopup3.setOnClickListener{
//            Toast.makeText(applicationContext,"TEST3 Press!!",Toast.LENGTH_SHORT).show()
//        }
//        // Set a dismiss listener for popup window
//        popupWindow.setOnDismissListener {
//            Toast.makeText(applicationContext,"Popup closed",Toast.LENGTH_SHORT).show()
//        }
//
//        // Finally, show the popup window on app
//        TransitionManager.beginDelayedTransition(root_layout_activity_main_person)
//        popupWindow.showAtLocation(
//            root_layout_activity_main_person, // Location to display popup window
//            Gravity.CENTER, // Exact position of layout to display popup
//            0, // X offset
//            0 // Y offset
//        )
