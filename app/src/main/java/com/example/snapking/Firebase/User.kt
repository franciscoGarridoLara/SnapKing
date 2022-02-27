package com.example.snapking.Firebase

import android.util.Log
import com.example.snapking.BaseDatos.BaseDatos
import com.example.snapking.modelo.Amigos
import com.example.snapking.modelo.Usuario
import com.example.snapking.modelo.WrapperUsuario
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking

class User private constructor(var auth:FirebaseAuth, var client : GoogleSignInClient?) {
    var mAuth:FirebaseAuth = auth
    var googleClient : GoogleSignInClient? = client
    //FIXME: Inicalizar este usuario cuando nos interese, si no va a crearlo todo el rato cuando no tengamos una instancia de esta clase.
    lateinit var user : Usuario
    lateinit var wrapper : WrapperUsuario


    companion object {
        private var instance: User? = null
        var TAG:String = "USER LOGS"

        @Synchronized
        private fun createInstance(auth: FirebaseAuth, client:GoogleSignInClient) {
            if (instance == null) {

                    instance = User(auth,client)
                    Log.d(TAG,"Creando instancia, ID currentUser: " + auth.currentUser!!.uid)


                    var userFound : Usuario? = BaseDatos.getInstance()!!.getUser(auth.currentUser!!.uid)
                        if(userFound != null)
                        {
                            Log.d(TAG,"Usuario reconstruido: " + userFound.toString())
                            instance!!.user = userFound
                        }else
                        {
                            instance!!.user = Usuario(auth.currentUser?.displayName.toString(), "https://firebasestorage.googleapis.com/v0/b/snap-king.appspot.com/o/user.png?alt=media&token=042c6e93-c9ec-4ee1-8d8e-0113d4bcf14f",1, ArrayList())
                        }

                    instance!!.wrapper = WrapperUsuario(auth.currentUser!!.uid, instance!!.user)

                    BaseDatos.getInstance()!!.existeUsuario(instance!!.wrapper,0)





                    //instance!!.wrapper  = WrapperUsuario(auth.currentUser!!.uid, instance!!.user)



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