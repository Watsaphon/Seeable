package com.estazo.project.seeable.app

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.estazo.project.seeable.app.ml.ModelMlV2
import com.otaliastudios.cameraview.frame.Frame
import kotlinx.android.synthetic.main.alert_dialog_critical_event.view.*
import org.tensorflow.lite.support.image.TensorImage
import java.io.ByteArrayOutputStream


class NotificationService : NotificationListenerService() {
    private lateinit var context: Context

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        Log.i("notificationService"," call create class NotificationService")

    }


    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val pack = sbn.packageName
        val extras = sbn.notification.extras
        val title = extras.getString("android.title").toString()
        val text = extras.getCharSequence("android.text").toString()

        val regex = "ETA".toRegex()
        val matchResult = regex.find(text)?.value.toString()


        Log.i("notificationService"," pack : $pack , title : $title" +
                " , text : $text , regEX -> matchResult : $matchResult ")

//        if (pack == "com.google.android.apps.maps" && title != "null" && text != "null" && matchResult == "ETA" ){
        if ( title != "null" && text != "null"){
            Log.i("Package", pack)
            Log.i("Title", title)
            Log.i("Text", text)
            val msgrcv = Intent("Msg")
            msgrcv.putExtra("package", pack)
            msgrcv.putExtra("title", title)
            msgrcv.putExtra("text", text)
            LocalBroadcastManager.getInstance(context!!).sendBroadcast(msgrcv)
            if(pack == "com.estazo.project.seeable.app" && title == "critical_Condition"){
                dialog()
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.i("Msg", "Notification Removed")
        val launchIntent = packageManager.getLaunchIntentForPackage("com.estazo.project.seeable.app")
        startActivity(launchIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("notificationService"," call destroy")
    }

    private fun dialog() {

        val dialogBuilder = AlertDialog.Builder(this)
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_critical_event, null)

        dialogBuilder.setView(mDialogView)

        Log.i("dialog","dialog call")
        val dialog: AlertDialog = dialogBuilder.create()
        val dialogWindow: Window = dialog.window!!
        val dialogWindowAttributes: WindowManager.LayoutParams = dialogWindow.attributes

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogWindowAttributes)
        lp.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 280f, resources.displayMetrics).toInt()
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialogWindow.attributes = lp

        dialogWindow.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        mDialogView.callAmbulance.setOnClickListener{
            Log.i("dialog","click call Ambulance ja")
            dialog.dismiss()
        }

        mDialogView.getLocation.setOnClickListener{
            Log.i("dialog","click get Location ja")
            dialog.dismiss()
        }

    }

    fun detect(frame: Frame) {

        Log.d("Score","function detect call")

        val model = ModelMlV2.newInstance(context)
        var detect_num = -1

        val out = ByteArrayOutputStream()
        val yuv = YuvImage(frame.getData(), ImageFormat.NV21,
            frame.size.width, frame.size.height, null)

        yuv.compressToJpeg(Rect(0, 0, frame.size.width, frame.size.height), 90, out)
        val imageBytes: ByteArray = out.toByteArray()
        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        val resized = Bitmap.createScaledBitmap(image, 512, 512, true)
        // Creates inputs for reference.
        val normalizedInputImageTensor = TensorImage.fromBitmap(resized)

        // Runs model inference and gets result.
        val outputs = model.process(normalizedInputImageTensor)
        val locations = outputs.locationsAsTensorBuffer
        val classes = outputs.classesAsTensorBuffer
        val scores = outputs.scoresAsTensorBuffer
        val numberOfDetections = outputs.numberOfDetectionsAsTensorBuffer
        val maxScore = scores.floatArray.max()
        val maxPosition = maxScore?.let { scores.floatArray.indexOf(it) }
        Log.d("Score","eiei")

        if (maxScore != null) {
            detect_num = maxScore.toInt()
            val test = maxScore
            if (maxScore > 0.9) {
                maxPosition?.let { classes.floatArray[it].toString() }?.let {
                    Log.d("Score", "it : $it")
                    detect_num = maxScore.toInt()
                }
/*
                ส่งค่าไปที่ใช้ในการเด้ง Dialog
                0 = ทางม้าลาย, 1 = ป้ายรถเมล์
                detect_num.toInt()

                ในหน้าที่ทำ navigate เพิ่มอันนี้ดวย
                lateinit var camera : CameraView

                * ใน onCreate() *
                camera = findViewById(R.id.camera)
                camera.setLifecycleOwner(this)
                cameraA.addFrameProcessor {
                    TFLiteDetection(this@*ActivityName*).detect(it)
                }

                แล้วเพิ่ม cameraView ใน layout ตั้งขนาดมันให้เป็น 1*1 dp พอ
*/
                Log.d("Score","detect_num(float) : $test  , detect_num(int) :$detect_num")

//                BlindFragment().let {
//                    it.onTextUpdate(detect_num)
//                }

            }
        }
        model.close()
    }


}