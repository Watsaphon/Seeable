# Seeable - Application for helping blinds

This code is only for `Android Kotlin` application, excluding Heroku server and device (Arduino).

## Introduction
Seeable is an application that helps blind people live better and safer.

Feature of Seeable :

* The application has  `2 Sections`: the `Blind Section` and `Caretaker Section`.

  * `Blind Section`: Main Feature for blind
    * `Self-Navigate` > lead the blind to the destination that the blind has set via google map and     
      start object detection.

    * `Caretaker-Navigate` > lead the blind to the destination that the caretaker has set via google 
      map and start object detection.

    * `Call Emergency` > call the emergency number that is set up.

    * `Send Location` > shared your current location to the `Real-time Firebase`.
    
  * `Caretaker Section`: Main Feature for caretaker
    * `BPM` > receive value from the device and show it to monitor.
    
    * `Health Status` > receive value from the device and measure it to change the `Health Status`
      (4 level: Normal, Tired, Exhausted, Danger).
      
    * `Activity` > check the blind if they are using google maps or not.
    
    * `Blind Lis`t > manage some data of the blinds people in care, such as `Caretaker-Navigate`,
      `add new blinds`, etc.
 
* The application can be used in `2 Languages` (Thai, English).
* The application increases the security of pairing and registration with `OTP Verification`.
* The application will send some `Notification` to blind or caretaker.
* The application uses `AI (Artificial Intelligence)` to process for `Object Detection` and 
  `TTS (Text-To-Speech)` notify blind by speaking.


## Prerequisites

you need to know:

* [Android Kotlin Fundamentals](https://developer.android.com/courses/kotlin-android-fundamentals/overview#lesson_1_build_your_first_app)(train basic for android kotlin)
* [Activity Lifecycle](https://developer.android.com/guide/components/activities/activity-lifecycle)
* [Fragment Lifecycle](https://developer.android.com/guide/fragments/lifecycle) 
* [Fragment Navigation](https://developer.android.com/guide/navigation/navigation-getting-started) (findnavcontroller)
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
* [Databinding](https://developer.android.com/codelabs/android-databinding#0)
* [Livedata](https://developer.android.com/topic/libraries/architecture/livedata)
* [Shared Preferences](https://developer.android.com/reference/android/content/SharedPreferences)
* [Dialog](https://developer.android.com/guide/topics/ui/dialogs)
* [Firebase](https://firebase.google.com/) (OTP,Map,Etc...)
* [Google Cloud Platform (GCP)](https://cloud.google.com/gcp/?utm_source=google&utm_medium=cpc&utm_campaign=japac-TH-all-en-dr-bkws-all-super-trial-e-dr-1009882&utm_content=text-ad-none-none-DEV_c-CRE_498747413894-ADGP_Hybrid%20%7C%20BKWS%20-%20EXA%20%7C%20Txt%20~%20GCP%20~%20General_google%20cloud%20platform-KWID_43700023274638565-kwd-296644789888&userloc_9074385-network_g&utm_term=KW_google%20cloud%20platform&gclid=Cj0KCQjwhr2FBhDbARIsACjwLo376RKvnQeVXwzHU5Csh5H99q2RJ7GBK0W3xcjnsd88b45WRa41irAaAtfSEALw_wcB&gclsrc=aw.ds)
* [TensorFlowLite](https://www.tensorflow.org/lite/guide/android)
* [TTS(Text-To-Speech)](https://stackoverflow.com/questions/3058919/text-to-speechtts-android)
