package com.example.snapking.Firebase

import android.util.Log
import com.example.snapking.modelo.Amigos
import com.example.snapking.modelo.Usuario
import com.example.snapking.modelo.WrapperUsuario
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth

class User private constructor(var auth:FirebaseAuth, var client : GoogleSignInClient?) {
    var mAuth:FirebaseAuth = auth
    //FIXME: Inicalizar este usuario cuando nos interese, si no va a crearlo todo el rato cuando no tengamos una instancia de esta clase.
    var user : Usuario = Usuario(auth.currentUser?.displayName.toString(), "",1, ArrayList())
    var wrapper : WrapperUsuario = WrapperUsuario(auth.currentUser!!.uid, user)
    var googleClient : GoogleSignInClient? = client

    companion object {
        private var instance: User? = null
        var TAG:String = "USER LOGS"

        @Synchronized
        private fun createInstance(auth: FirebaseAuth, client:GoogleSignInClient) {
            if (instance == null) {
                instance = User(auth,client)
            }
        }

        fun getInstancia(): User?
        {
            if (instance == null) return null
            return instance
        }

        fun crearInstance(auth: FirebaseAuth, client:GoogleSignInClient): User? {
            if (instance == null) createInstance(auth,client)
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

    fun printToken(): String {
        var token = "SIN TOKEN"

        if(instance!= null)
            token = instance!!.mAuth!!.uid.toString()
        Log.d(TAG,token)
        return token
    }

    fun signOut()
    {
        mAuth.signOut()
        googleClient?.signOut()
    }


}