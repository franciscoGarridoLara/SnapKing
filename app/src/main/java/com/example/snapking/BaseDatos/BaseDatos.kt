package com.example.snapking.BaseDatos

import android.util.Log
import com.example.snapking.Firebase.User
import com.example.snapking.modelo.Usuario
import com.example.snapking.modelo.WrapperUsuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import java.sql.Wrapper

class BaseDatos(){

    var database: FirebaseDatabase = Firebase.database
    val reference = database.reference


    fun escribirUsuario(wrapper : WrapperUsuario){

        Log.d(BaseDatos.TAG,"Escribiendo usuario")
        reference.child("usuarios").child(wrapper.id).setValue(wrapper.usuario)
    }



    companion object {
        private var instance: BaseDatos? = null
        var TAG:String = "DATABASE LOGS"

        @Synchronized
        private fun createInstance() {
            if (instance == null) {
                instance = BaseDatos()
            }
        }


        fun getInstance(): BaseDatos? {
            if (instance == null) createInstance()
            return instance
        }
    }

    init {
        println("Singleton invoked")
    }


}