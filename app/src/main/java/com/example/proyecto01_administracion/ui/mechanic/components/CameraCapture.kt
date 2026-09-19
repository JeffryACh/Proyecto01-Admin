package com.example.proyecto01_administracion.ui.mechanic.components

import android.content.Context
import android.net.Uri
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.io.File
import androidx.compose.ui.unit.dp

@Composable
fun CameraCapture(
    context: Context,
    onPhotoCaptured: (Uri) -> Unit
) {

    val lifecycleOwner = LocalLifecycleOwner.current

    var imageCapture by remember {
        mutableStateOf<ImageCapture?>(null)
    }


    Column {

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),

            factory = { ctx ->

                val previewView = PreviewView(ctx)

                val cameraProviderFuture =
                    ProcessCameraProvider.getInstance(ctx)


                cameraProviderFuture.addListener({

                    val cameraProvider =
                        cameraProviderFuture.get()


                    val preview =
                        Preview.Builder()
                            .build()
                            .also {
                                it.setSurfaceProvider(
                                    previewView.surfaceProvider
                                )
                            }


                    val capture =
                        ImageCapture.Builder()
                            .build()


                    imageCapture = capture


                    cameraProvider.unbindAll()

                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        capture
                    )


                }, ContextCompat.getMainExecutor(ctx))


                previewView
            }
        )


        Button(
            onClick = {

                val capture =
                    imageCapture ?: return@Button


                val file =
                    File(
                        context.filesDir,
                        "maintenance_${System.currentTimeMillis()}.jpg"
                    )


                val options =
                    ImageCapture.OutputFileOptions
                        .Builder(file)
                        .build()


                capture.takePicture(
                    options,
                    ContextCompat.getMainExecutor(context),
                    object :
                        ImageCapture.OnImageSavedCallback {


                        override fun onImageSaved(
                            result: ImageCapture.OutputFileResults
                        ) {

                            onPhotoCaptured(
                                Uri.fromFile(file)
                            )

                        }


                        override fun onError(
                            exception: ImageCaptureException
                        ) {
                            exception.printStackTrace()
                        }
                    }
                )

            }
        ) {

            Text("Capturar foto")

        }
    }
}