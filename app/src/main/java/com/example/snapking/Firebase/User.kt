package com.example.snapking.Firebase

import android.util.Log
import com.example.snapking.BaseDatos.BaseDatos
import com.example.snapking.modelo.Amigos
import com.example.snapking.modelo.Usuario
import com.example.snapking.modelo.WrapperUsuario
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking

class User private constructor(var auth:FirebaseAuth, var client : GoogleSignInClient?) {
    private var database: FirebaseDatabase = Firebase.database
    val reference = database.reference
    var mAuth:FirebaseAuth = auth
    var googleClient : GoogleSignInClient? = client
    //FIXME: Inicalizar este usuario cuando nos interese, si no va a crearlo todo el rato cuando no tengamos una instancia de esta clase.




    companion object {
        private var instance: User? = null
        var TAG:String = "USER LOGS"
        var user : Usuario? = null
        var wrapper : WrapperUsuario? = null

        @Synchronized
        private fun createInstance(auth: FirebaseAuth, client:GoogleSignInClient) {
            if (instance == null) {
                    //FIXME:FUCKING COROUTINES
                    instance = User(auth,client)
                    Log.d(TAG,"Creando instancia, ID currentUser: " + auth.currentUser!!.uid + " GoogleCliente: " + client.toString())

                    //Se crea el usuario
                    instance!!.createUser()

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

    fun createUser(){

        var usuarios = instance!!.reference.child("usuarios").get().addOnSuccessListener {
            var existe = false
            var userFound : DataSnapshot? = null

            for (usuario in it.children)
            {
                if(usuario.key.equals(mAuth!!.currentUser!!.uid))
                {
                    Log.d(BaseDatos.TAG,"El usuario ya existe en Firebase.")
                    existe = true
                    userFound = usuario
                    break
                }
            }
            if(existe && userFound != null)
                user = reconstruirUsuario(userFound!!)
            else{
                Log.d(TAG,"Creando usuario por defecto y insertando en la base de datos.")
                user = Usuario(auth.currentUser!!.displayName.toString(),"https://firebasestorage.googleapis.com/v0/b/snap-king.appspot.com/o/user.png?alt=media&token=042c6e93-c9ec-4ee1-8d8e-0113d4bcf14f",1, ArrayList())
            }

            wrapper = WrapperUsuario(instance!!.mAuth.currentUser!!.uid,user!!)
            instance!!.reference.child("usuarios").child(wrapper!!.id).setValue(wrapper!!.usuario)

            Log.d(TAG,"Usuario inicializado -> " + user.toString())

        }
    }

    fun reconstruirUsuario(snapshot:DataSnapshot): Usuario{
        var usuario : Usuario
        var listaAmigos : ArrayList<String> = ArrayList()

        for(id in snapshot.child("amigos").children)
        {
            Log.d(TAG,"Reconstruyendo amigos.")
            listaAmigos.add(id.value.toString())
        }
        usuario =  Usuario(
            snapshot.child("nickname").value as String,
            snapshot.child("avatar").value as String,
            snapshot.child("nivel").value as Long,
            listaAmigos
        )

        Log.d(BaseDatos.TAG, "Usuario reconstruido -> " + usuario.toString())

        return usuario
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

    fun getAvatar(): String{
        if(user!= null)
            return user!!.avatar
        else
            return "https://firebasestorage.googleapis.com/v0/b/snap-king.appspot.com/o/user.png?alt=media&token=042c6e93-c9ec-4ee1-8d8e-0113d4bcf14f"
    }
    fun signOut()
    {
        mAuth.signOut()
        googleClient?.signOut()
        instance = null
    }


}