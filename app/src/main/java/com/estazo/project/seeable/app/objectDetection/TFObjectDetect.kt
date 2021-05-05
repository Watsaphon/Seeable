package com.estazo.project.seeable.app.objectDetection

import android.content.Context
import android.graphics.*
import android.util.Log
import com.estazo.project.seeable.app.ml.ModelMlV2
import com.otaliastudios.cameraview.frame.Frame
import org.tensorflow.lite.support.image.TensorImage
import java.io.ByteArrayOutputStream


class TFLiteDetection(context: Context) {

    val model = ModelMlV2.newInstance(context)
    var detect_num = -1

     fun detect(frame: Frame) {

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