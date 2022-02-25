package com.example.snapking.BaseDatos

import android.util.Log
import com.example.snapking.Firebase.User
import com.example.snapking.modelo.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.sql.Wrapper


class BaseDatos(){

    var database: FirebaseDatabase = Firebase.database
    val reference = database.reference
    var salas= ArrayList<WrapperSala>()


    fun escribirUsuario(wrapper : WrapperUsuario){

        Log.d(BaseDatos.TAG,"Escribiendo usuario")
        reference.child("usuarios").child(wrapper.id).setValue(wrapper.usuario)
    }
    fun escribrSala(sala:WrapperSala){
        Log.d(BaseDatos.TAG,"Escribiendo sala")
        reference.child("salas").push().setValue(sala)

    }
    fun leerSala(): ArrayList<WrapperSala> {
        var listasalas:ArrayList<WrapperSala>
        listasalas=ArrayList<WrapperSala>()

        var datasnapshot = reference.child("salas").get().addOnSuccessListener { datasnapshot->

            for(child in datasnapshot.children){

                var ronda:Ronda?
                try {
                    ronda=Ronda(
                        child.child("ronda").child("numero").value as Int,
                        child.child("ronda").child("id_tematica") as String

                    )
                } catch (e: NullPointerException) {
                    ronda=null
                }

                var jugadores=ArrayList<Jugador>()

                    for(jugadorsnap in child.child("jugadores").children){
                        var long= jugadorsnap.child("punto").value as Long

                      var j= jugadorsnap.key?.let {
                          Jugador(
                              it,
                              jugadorsnap.child("ready").value as Boolean,
                              long.toInt()
                             )
                      }
                        if (j != null) {
                            jugadores.add(j)
                        }


                    }


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


                    if(wrapperSala!= null){
                        listasalas.add(wrapperSala)
                        Log.d("------------------mmmmmmm","ajkdhsajkdhak")
                    }else{
                        Log.d("------------------mmmmmmm","nulllazooooooooo")
                    }



            }

        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)


        }
        return listasalas

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

        var sala=leerSalabyId(id)



        if (sala!=null) {

            reference.child("salas").child(id).setValue(sala)
        }
    }

    fun getUsersWithNickname(nickname:String) : List<WrapperUsuario>?{
        var usuarios : ArrayList<WrapperUsuario>?
        usuarios = ArrayList<WrapperUsuario>()
        var userFound : WrapperUsuario?
        reference.child("usuarios").get().addOnSuccessListener {

            for (usuario in it.children)
            {
                var userNickname = usuario.child("nickname").value as String
                if(userNickname.equals(nickname))
                {
                    userFound = WrapperUsuario(usuario.key.toString(),
                        Usuario(
                        usuario.child("nickname").value as String,
                        usuario.child("avatar").value as String,
                        usuario.child("nivel").value as Long,
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
        var wrapperUsuario : WrapperUsuario = User.getInstancia()!!.wrapper

        if(wrapperUsuario != null && wrapperUsuario.usuario != null && !wrapperUsuario.usuario.amigos.contains(idUsuario))
        {
            wrapperUsuario.usuario.amigos.add(idUsuario)
        }

        escribirUsuario(wrapperUsuario)

    }

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

    fun reconstruirUsuario(snapshot:DataSnapshot): Usuario{
        var usuario : Usuario
        var listaAmigos : ArrayList<String> = ArrayList()

        for(id in snapshot.child("amigos").children)
        {
            listaAmigos.add(id as String)
        }
        usuario =  Usuario(
            snapshot.child("nickname").value as String,
            snapshot.child("avatar").value as String,
            snapshot.child("nivel").value as Long,
            listaAmigos
        )


        return usuario
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