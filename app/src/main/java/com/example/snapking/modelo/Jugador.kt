package com.example.snapking.modelo


data class Jugador(var id: String, var ready:Boolean, var punto:Int,var etapa:Etapa) {
    constructor() : this("", true,0,Etapa.LOBBY) {
    }
}