package com.example.proyecto01_administracion

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FirebaseConnectionTest {

    @Test
    fun firebase_inicializa_y_comunica_con_servidor() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        
        // 0. Inicialización robusta
        val app = if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context)
        } else {
            FirebaseApp.getInstance()
        }

        assertNotNull("FirebaseApp no se pudo inicializar.", app)
        
        // 1. Diagnóstico de configuración (Esto fallará con un mensaje claro si el JSON no se leyó)
        val options = app?.options
        val apiKey = options?.apiKey
        val appId = options?.applicationId
        
        assertNotNull("API Key no encontrada. Revisa el google-services.json y haz Clean/Rebuild.", apiKey)
        assertNotNull("App ID no encontrado. Revisa el google-services.json y haz Clean/Rebuild.", appId)

        val auth = FirebaseAuth.getInstance(app!!)
        assertNotNull("No se pudo obtener la instancia de FirebaseAuth.", auth)

        // 2. Hacemos una petición REAL al servidor de Firebase
        // Usamos Tasks.await para detener la prueba hasta que Firebase en la nube responda
        val exception = try {
            Tasks.await(auth.signInWithEmailAndPassword("qa_test_conexion@transandina.com", "clave_falsa_123"))
            null // La prueba fallará si llega aquí, porque este usuario no debería existir
        } catch (e: Exception) {
            e // Capturamos la respuesta del servidor
        }

        // 3. Verificamos la respuesta de Google
        // Si nos da uno de estos dos errores, significa que la app viajó a la nube,
        // consultó la base de datos real de TransAndina, y regresó. ¡Conexión perfecta!
        val isFirebaseError = exception?.cause is FirebaseAuthInvalidUserException ||
                exception?.cause is FirebaseAuthInvalidCredentialsException

        assertTrue("No se logró conexión con el servidor de Firebase", isFirebaseError)
    }
}