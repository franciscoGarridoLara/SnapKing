package com.example.snapking.Firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class User private constructor(var auth:FirebaseAuth) {
    var mAuth:FirebaseAuth = auth

    companion object {
        private var instance: User? = null
        var TAG:String = "USER LOGS"

        @Synchronized
        private fun createInstance(auth: FirebaseAuth) {
            if (instance == null) {
                instance = User(auth)
            }
        }

        fun getInstancia(): User?
        {
            if (instance == null) return null
            return instance
        }

        fun crearInstance(auth: FirebaseAuth): User? {
            if (instance == null) createInstance(auth)
            return instance
        }
    }

    init {
        println("Singleton invoked")
    }

    fun printName() {
        var nombre = "no hay usuario"

        if(instance!=null)
            nombre = instance!!.mAuth.currentUser?.displayName.toString()

        Log.d(TAG,nombre)
    }

    fun printToken()
    {
        var token = "SIN TOKEN"

        if(instance!= null)
            token = instance!!.mAuth!!.uid.toString()
        Log.d(TAG,token)
    }


}