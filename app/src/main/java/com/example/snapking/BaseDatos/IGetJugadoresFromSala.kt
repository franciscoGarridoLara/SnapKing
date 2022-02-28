package com.example.snapking.BaseDatos

import com.example.snapking.modelo.Jugador

interface IGetJugadoresFromSala {
    fun OnCallback(lista:ArrayList<Jugador>)
}