package com.example.snapking.BaseDatos

import android.util.Log
import com.example.snapking.modelo.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


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