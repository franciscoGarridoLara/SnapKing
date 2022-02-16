package com.example.snapking.modelo


import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.snapking.R

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth


public class Modelo {

    private lateinit var alertDialog: android.app.AlertDialog
    private lateinit var progressDialog: ProgressDialog

    //COMPROBAR CONEXION INTERNET.
    fun compruebaConexion(context: Context): Boolean {

        var connected = false

        val connec = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Recupera todas las redes (tanto móviles como wifi)
        val redes = connec.allNetworkInfo

        for (i in redes.indices) {
            // Si alguna red tiene conexión, se devuelve true
            if (redes[i].state == NetworkInfo.State.CONNECTED) {
                connected = true
            }
        }
        return connected
    }

    // Hacer actividades transprentes.
    fun activityTransparent(activity: AppCompatActivity) {
        activity.window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    //Dialogo perso.
    fun showDialog(contexto: Context, mensage: String) {
        Toast.makeText(contexto,
            mensage, Toast.LENGTH_SHORT).show()
        //alertDialog = SpotsDialog(contexto, mensage)
        //alertDialog.show()
    }

    // Cerrar dialogo.
    fun hideDialog() {
        alertDialog.dismiss()
    }

    // DIALOGO NORMAL.
    fun showProgressDialog(title: String, message: String, contexto: Context) {
        if (progressDialog != null && progressDialog.isShowing)
            progressDialog.setMessage(message)
        else
            progressDialog = ProgressDialog.show(contexto, title, message, true, false)
    }

    // CERRAR DIALOGO.
    fun hideProgressDialog() {
        if (progressDialog != null)
            progressDialog.hide()
    }

    // Cerrar sesiones.
    fun signOut(auth: FirebaseAuth, client: GoogleSignInClient) {
        auth.signOut()
        client.signOut()
    }

}