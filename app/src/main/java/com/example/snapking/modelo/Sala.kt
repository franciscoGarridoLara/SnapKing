package com.example.snapking.modelo

data class Sala(var nombre:String, var capacidad:Int,
                var anfitrion:String, var estado:Boolean, var ronda: Ronda?, var rondas_totales:Int, var jugadores:List<Jugador>){


}