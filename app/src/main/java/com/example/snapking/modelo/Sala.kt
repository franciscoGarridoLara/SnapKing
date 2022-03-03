package com.example.snapking.modelo


data class Sala(
    var nombre:String, var capacidad: Long,
    var anfitrion:String, var estado:Boolean, var ronda: Ronda?, var rondas_totales: Long, var jugadores:List<Jugador>,var start:Boolean?,var etapa:Etapa){
    constructor() : this("", 0,"",true,null,0,ArrayList(),null,Etapa.LOBBY) {
    }


}