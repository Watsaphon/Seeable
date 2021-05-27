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



## Seeable Overview: 

You can see in demo in this [Link](https://www.youtube.com/watch?v=PHhGOCUjQVc).

<p align="center">
  <img width="200" alt="screen shot 2017-08-07 at 12 18 15 pm" src="https://user-images.githubusercontent.com/84655285/119373783-00af0400-bce3-11eb-8c8a-b4170fa37f19.jpg">
  <img width="200" alt="screen shot 2017-08-07 at 12 18 15 pm" src="https://user-images.githubusercontent.com/84655285/119373824-0d335c80-bce3-11eb-93ea-37eedf500bcb.jpg">
  <img width="200" alt="screen shot 2017-08-07 at 12 18 15 pm" src="https://user-images.githubusercontent.com/84655285/119373940-2f2cdf00-bce3-11eb-8e8b-a3af41a82318.jpg">
  <img width="200" alt="screen shot 2017-08-07 at 12 18 15 pm" src="https://user-images.githubusercontent.com/64304421/119865784-3b64a680-bf46-11eb-8d6c-4e16581e0466.gif">
 
  <img width="200" alt="screen shot 2017-08-07 at 12 18 15 pm" src="https://user-images.githubusercontent.com/84655285/119373858-18868800-bce3-11eb-97a3-1027090980fb.jpg">
  <img width="200" alt="screen shot 2017-08-07 at 12 18 15 pm" src="https://user-images.githubusercontent.com/64304421/119880513-871f4c00-bf56-11eb-85ca-219b6e8f3b50.gif">
  <img width="200" alt="screen shot 2017-08-07 at 12 18 15 pm" src="https://user-images.githubusercontent.com/64304421/119875210-b763ec00-bf50-11eb-9a16-9e5839386299.png">
  <img width="200" alt="screen shot 2017-08-07 at 12 18 15 pm" src="https://user-images.githubusercontent.com/64304421/119875203-b468fb80-bf50-11eb-82ca-230815a4000a.png">
 
  <img width="200" alt="screen shot 2017-08-07 at 12 18 15 pm" src="https://user-images.githubusercontent.com/84655285/119373966-36ec8380-bce3-11eb-8a57-26ff6947f217.jpg">
  <img width="200" alt="screen shot 2017-08-07 at 12 18 15 pm" src="https://user-images.githubusercontent.com/84655285/119373973-38b64700-bce3-11eb-9336-cadb73af9fc3.jpg">
  <img width="200" alt="screen shot 2017-08-07 at 12 18 15 pm" src="https://user-images.githubusercontent.com/84655285/119373977-3a800a80-bce3-11eb-8389-d4b5b4c49abb.jpg">
  <img width="200" alt="screen shot 2017-08-07 at 12 18 15 pm" src="https://user-images.githubusercontent.com/64304421/119888308-68718300-bf5f-11eb-95c7-3aa021e07a91.gif">

</p>

