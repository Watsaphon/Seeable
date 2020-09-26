package com.estazo.project.seeable.app

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle



//https://developers.google.com/maps/documentation/urls/android-intents?authuser=2

class GoogleMapScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_map_screen)

        /**Intent requests
            1.Create a Uri from an intent string. Use the result to create an Intent.
                 val gmmIntentUri = Uri.parse("geo:37.7749,-122.4194")
             if need to secure -> URI encoded query strings
                 val gmmIntentUri = Uri.parse("geo:37.7749,-122.4192?q=" + Uri.encode("1st & Pike, Seattle"))
           2.Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                 val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
           3. Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps")
            4.Attempt to start an activity that can handle the Intent
                startActivity(mapIntent)
         */

        /**Display a map
        Use the geo: intent to display a map at a specified location and zoom level.
        geo:latitude,longitude?z=zoom
         */

        /**Search for a location
            geo:latitude,longitude?q = query
            geo:0,0?q = my+street+address
            geo:0,0?q = latitude,longitude(label)
         for example : Search for restaurants in San Francisco
             val gmmIntentUri =Uri.parse("geo:37.7749,-122.4194?q=restaurants")
         */

        /**Location search
            Searching for a specific address will display a pin at that location.
               val gmmIntentUri =Uri.parse("geo:0,0?q=1600 Amphitheatre Parkway, Mountain+View, California")
         */

        /**Launch turn-by-turn navigation
        Use this intent to launch Google Maps navigation with turn-by-turn directions to the address
        or coordinate specified.
        Directions are always given from the user's current location.There are 2 commands
                google.navigation:q=a+street+address
                google.navigation:q=latitude,longitude
        Mode sets the method of transportation. Mode is optional, and can be set to one of:
        d for driving (default)
        b for bicycling
        l for two-wheeler
        w for walking
        Avoid sets features the route should try to avoid. Avoid is optional and can be set to one or more of:
        t for tolls
        h for highways
        f for ferries

        for example :
        val gmmIntentUri = Uri.parse("google.navigation:q=Taronga+Zoo,+Sydney+Australia&mode=b&avoid=tf")
         */


        /**Display a Street View panorama
            Use the google.streetview intent to launch Google Street View.
            Google Street View provides panoramic views from designated locations throughout its coverage area.
            User contributed Photospheres,and Street View special collections are also available.There are 2 commands
                 google.streetview:cbll=latitude,longitude&cbp=0,bearing,0,zoom,tilt
                 google.streetview:panoid=id&cbp=0,bearing,0,zoom,tilt

         for example :
        // Displays an image of the Swiss Alps
        val gmmIntentUri =Uri.parse("google.streetview:cbll=46.414382,10.013988")
         */

        val gmmIntentUri = Uri.parse("geo:13.574641,100.835714")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(packageManager)?.let {
            startActivity(mapIntent)
        }

    }
}