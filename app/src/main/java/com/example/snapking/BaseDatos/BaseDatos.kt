package com.example.snapking.BaseDatos

import android.text.BoringLayout
import android.util.Log
import com.example.snapking.Firebase.User
import com.example.snapking.modelo.*
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

import java.sql.Wrapper


class BaseDatos(){

    var database: FirebaseDatabase = Firebase.database
    val reference = database.reference
    lateinit var listaSalasglobal:ArrayList<WrapperSala>




    fun getUser(id:String) : Usuario?
    {
        var user : Usuario? = null

        var usuarios = reference.child("usuarios").get().addOnSuccessListener {
            for (usuario in it.children)
            {
                if(usuario.key.equals(id))
                {
                    Log.d(TAG,"Existe el usuario por lo que llamamos a la funcion de reconstruir.")
                    //user = reconstruirUsuario(usuario)
                }

            }
        }

        return user
    }

    fun escribrSala(sala:WrapperSala){
        Log.d(BaseDatos.TAG,"Escribiendo sala")
        reference.child("salas").push().setValue(sala)

    }

       fun leerSala(interfazSala:ISalasLectura): ArrayList<WrapperSala> {
        var listasalas:ArrayList<WrapperSala>
        Log.d("-----------------fff","entradon")
        listaSalasglobal=ArrayList()
        listasalas=ArrayList()
           var listanueva: ArrayList<WrapperSala>? =null

        var datasnapshot = reference.child("salas").get().addOnSuccessListener {
            Log.d("-----------------fff","entradon2")
            for(child in it.children){

                var ronda:Ronda?
                try {
                    ronda=Ronda(
                        child.child("ronda").child("numero").value as Int,
                        child.child("ronda").child("id_tematica") as String

                    )
                } catch (e: NullPointerException) {
                    ronda=null
                }
                Log.d("-----------------fff","entradon3")
                var jugadores=ArrayList<Jugador>()

                    for(jugadorsnap in child.child("jugadores").children){
                        var long= jugadorsnap.child("punto").value as Long

                      var j:String?
                          j=jugadorsnap.key

                        Log.d("-----------------fff","entradon4")

                        if (j != null) {

                            var jugadorclass=Jugador(j,false, 0)
                            jugadores.add(jugadorclass)
                        }


                    }

                Log.d("-----------------fff","entradon5")
                    var sala=Sala(
                        child.child("nombre").value as String,
                        child.child("capacidad").value as Long,
                        child.child("anfitrion").value as String,
                        child.child("estado").value as Boolean,
                        ronda,
                        child.child("rondas_totales").value as Long,
                        jugadores
                    )
                    var clave=child.key
                var wrapperSala:WrapperSala?
                  wrapperSala=null
                if(clave!=null){
                     wrapperSala= WrapperSala(clave,sala)
                }

                Log.d("-----------------fff","entradon6")
                    if(wrapperSala!= null){
                        listasalas.add(wrapperSala)
                        Log.d("------------------mmmmmmm","base de datos size "+listasalas.size.toString())
                    }else{
                        Log.d("------------------mmmmmmm","nulllazooooooooo")

                    }
                    listaSalasglobal=listasalas
                Log.d("-----------------fff","entradon7")
                    interfazSala.OncallBack(listasalas)
                Log.d("-----------------fff","entradon8")



            }

        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)


        }

           Log.d("firebase", "--------------------------------"+listasalas.size.toString())




        return listaSalasglobal

    }

    fun leerSalabyId(id:String ): Sala? {
        var sala:Sala?
        sala=null
        reference.child("salas").child(id).get().addOnSuccessListener {
            sala=it.getValue<Sala>()
        }
        return sala

    }

    fun escribirSala(sala: Sala): String? {
        var clave=reference.child("salas").push().key
        if (clave != null) {
            reference.child("salas").child(clave).setValue(sala)
        }
        return clave

    }
    fun meterJugadorSala(id:String,jugador:Jugador){
        reference.child("salas").child(id).child("jugadores").push().setValue(jugador)
    }

    fun getUsersWithNickname(nickname:String) : List<WrapperUsuario>?{
        var usuarios : ArrayList<WrapperUsuario>?
        usuarios = ArrayList<WrapperUsuario>()
        var userFound : WrapperUsuario?
        reference.child("usuarios").get().addOnSuccessListener {

            for (usuario in it.children)
            {
                var userNickname = usuario.child("nickname").value as String
                var lvl = usuario.child("nivel").value as Long
                var avatar = usuario.child("avatar").value as String

                if(userNickname.contains(nickname))
                {
                    userFound = WrapperUsuario(usuario.key.toString(),
                            Usuario(
                                userNickname,
                                avatar,
                                lvl,
                                ArrayList()
                            )
                        )

                    for(amigo in usuario.child("amigos").children)
                    {
                        var idAmigo = amigo.value as String
                        if(idAmigo != null)
                        {
                            userFound!!.usuario.amigos.add(idAmigo!!)
                        }
                    }


                    if(userFound != null)
                    {
                        usuarios.add(userFound!!)
                    }
                }
            }


        }
        return usuarios
    }


    fun agregarUsuario(idUsuario: String)
    {
        var wrapperUsuario : WrapperUsuario = User.wrapper!!

        if(wrapperUsuario != null && wrapperUsuario.usuario != null && !wrapperUsuario.usuario.amigos.contains(idUsuario))
        {
            Log.d(TAG, "Agregando amigo " + idUsuario + " a el usuario " + wrapperUsuario.usuario.nickname)
            wrapperUsuario.usuario.amigos.add(idUsuario)
            actualizarUsuario(wrapperUsuario)
        }



    }

    fun actualizarUsuario(usuario : WrapperUsuario){
        reference.child("usuarios").child(usuario.id).setValue(usuario.usuario)
    }

    /*
    fun getListWrapperUsuariosFromListIds(ids : List<String>) : ArrayList<WrapperUsuario>
    {
        var listaWrapperUsuarios : ArrayList<WrapperUsuario> = ArrayList()
        Log.d(TAG,"SE LLAMA A LA FUNCION DE RECONSTRUIR -------------------")

        reference.child("usuarios").get().addOnSuccessListener {
            for(usuario in it.children)
            {
                var id:String = usuario.key.toString()
                Log.d(TAG,"Id de la base de datos: " + id.toString())
                for(idLista in ids)
                {
                    Log.d(TAG,"Id de la lista de amigos del usuario: " + idLista)
                    if(id.toString().equals(idLista))
                    {
                        listaWrapperUsuarios.add(WrapperUsuario(id,reconstruirUsuario(usuario)))
                        break
                    }
                }

            }


        }

        return listaWrapperUsuarios
    }


*/



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