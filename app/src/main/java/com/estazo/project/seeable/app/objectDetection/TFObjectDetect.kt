package com.estazo.project.seeable.app.objectDetection

import android.content.Context
import android.graphics.*
import android.util.Log
import com.estazo.project.seeable.app.ml.ModelMlV2
import com.otaliastudios.cameraview.frame.Frame
import org.tensorflow.lite.support.image.TensorImage
import java.io.ByteArrayOutputStream


class TFLiteDetection(context: Context, private val onDetect: (String) -> Unit) {

    private val model = ModelMlV2.newInstance(context)
    private var detect_num = -1

    fun detect(frame: Frame) {

        val out = ByteArrayOutputStream()
        val yuv = YuvImage(
            frame.getData(), ImageFormat.NV21,
            frame.size.width, frame.size.height, null
        )
        yuv.compressToJpeg(Rect(0, 0, frame.size.width, frame.size.height), 90, out)
        val imageBytes: ByteArray = out.toByteArray()
        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        val resized = Bitmap.createScaledBitmap(image, 512, 512, true)
        // Creates inputs for reference.
        val normalizedInputImageTensor = TensorImage.fromBitmap(resized)

        // Runs model inference and gets result.
        val outputs = model.process(normalizedInputImageTensor)
        val classes = outputs.classesAsTensorBuffer
        val scores = outputs.scoresAsTensorBuffer
        val maxScore = scores.floatArray.max()
        val maxPosition = maxScore?.let { scores.floatArray.indexOf(it) }
        if (maxScore != null) {
            detect_num = maxScore.toInt()
            if (maxScore > 0.92) {
                maxPosition?.let { classes.floatArray[it].toString() }?.let { it ->
                    Log.d("Score", "it : $it")
                    detect_num = maxScore.toInt()
                    when (it) {
                        "0.0" -> {
                            Log.d("Score", "crosswalk detect")
                            onDetect.invoke(it)
//                            อันนี้อ่ะผิด เพราะว่ามันไปสร้าง instance ของ NavigateBlindFragment มาใหม่
//                            NavigateBlindFragment().let {
//                                Log.d("Score", "msg = $msg")
//                                it.receiveIMG(msg)
//                            }
                        }
                        "1.0" -> {
                            Log.d("Score", " bus sign detect")
                            onDetect.invoke(it)
//                            อันนี้อ่ะผิด เพราะว่ามันไปสร้าง instance ของ NavigateBlindFragment มาใหม่
//                            NavigateBlindFragment().let {
//                                Log.d("Score", "msg = $msg")
//                                it.receiveIMG(msg)
//                            }
                        }
                        else -> {
                            Log.d("Score", " not found")
                        }
                    }
                }

            }
        }

        model.close()

    }


}