package com.example.snapking.BaseDatos

import android.util.Log
import com.example.snapking.modelo.*
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
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
        reference.child("salas").child(sala.id).setValue(sala)

    }
    fun leerSala() {
        var datasnapshot = reference.child("salas").get().addOnSuccessListener { datasnapshot->

            for(child in datasnapshot.children){

                var ronda=Ronda(
                    child.child("ronda").child("numero").value as Int,
                    child.child("ronda").child("id_tematica") as String

                )

                var jugadores=ArrayList<Jugador>()

                for (idJugador in child.child("jugadores").children){



                }






                var sala=Sala(
                    child.child("nombre").value as String,
                    child.child("capacidad").value as Int,
                    child.child("anfitrion").value as String,
                    child.child("estado").value as Boolean,
                    ronda,
                    child.child("rondas_totales").value as Int,
                    jugadores


                )

            }

        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)

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