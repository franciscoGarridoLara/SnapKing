package com.example.snapking.modelo

import kotlinx.serialization.Serializable

@Serializable
data class Sala(
    var nombre:String, var capacidad: Long,
    var anfitrion:String, var estado:Boolean, var ronda: Ronda?, var rondas_totales: Long, var jugadores:List<Jugador>){
    constructor() : this("", 0,"",true,null,0,ArrayList()) {
    }


}