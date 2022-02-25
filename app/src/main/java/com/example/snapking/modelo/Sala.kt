package com.example.snapking.modelo

data class Sala(
    var nombre:String, var capacidad: Long,
    var anfitrion:String, var estado:Boolean, var ronda: Ronda?, var rondas_totales: Long, var jugadores:List<Jugador>){


}