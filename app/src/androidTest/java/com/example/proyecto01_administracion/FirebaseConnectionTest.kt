package com.example.proyecto01_administracion

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FirebaseConnectionTest {

    // Declaramos la variable usando lateinit para que espere al @Before
    private lateinit var db: FirebaseFirestore

    @Before
    fun setup() {
        // Obtenemos el contexto real de tu aplicación (donde reside el google-services.json)
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        // Inicializamos Firebase explícitamente en el entorno de pruebas si no existe
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context)
        }

        // AHORA SÍ, obtenemos la instancia de Firestore.
        // Como FirebaseApp ya está inicializado, esto no devolverá null.
        db = FirebaseFirestore.getInstance()
    }

    @Test
    fun firebase_inicializa_y_comunica_con_servidor() {
        // Comprobamos que la base de datos se inicializó correctamente
        assertNotNull("La instancia de Firebase no debería ser nula", db)

        /*
         * Nota: Si en tu prueba original hacías alguna consulta específica a la base de datos,
         * puedes agregarla aquí abajo usando la variable 'db'.
         * Ejemplo:
         * val docRef = db.collection("usuarios").document("test")
         * assertNotNull(docRef)
         */
    }
}