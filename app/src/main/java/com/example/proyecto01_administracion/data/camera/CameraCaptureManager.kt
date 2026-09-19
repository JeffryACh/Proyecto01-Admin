package com.example.proyecto01_administracion.data.camera

import android.content.Context
import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class CameraCaptureManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun capturePhoto(
        imageCapture: ImageCapture
    ): Result<String> {

        return suspendCancellableCoroutine { continuation ->

            val photoFile = createEvidenceFile()

            val outputOptions =
                ImageCapture.OutputFileOptions
                    .Builder(photoFile)
                    .build()

            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageSavedCallback {

                    override fun onImageSaved(
                        outputFileResults: ImageCapture.OutputFileResults
                    ) {
                        val uri = Uri.fromFile(photoFile)

                        if (continuation.isActive) {
                            continuation.resume(
                                Result.success(uri.toString())
                            )
                        }
                    }

                    override fun onError(
                        exception: ImageCaptureException
                    ) {
                        if (photoFile.exists()) {
                            photoFile.delete()
                        }

                        if (continuation.isActive) {
                            continuation.resume(
                                Result.failure(exception)
                            )
                        }
                    }
                }
            )
        }
    }

    private fun createEvidenceFile(): File {

        val evidenceDirectory = File(
            context.filesDir,
            EVIDENCE_DIRECTORY
        )

        if (!evidenceDirectory.exists()) {
            evidenceDirectory.mkdirs()
        }

        return File(
            evidenceDirectory,
            "evidence_${System.currentTimeMillis()}.jpg"
        )
    }

    companion object {
        private const val EVIDENCE_DIRECTORY =
            "maintenance_evidence"
    }
}