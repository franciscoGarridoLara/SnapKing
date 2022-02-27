package com.example.snapking.BaseDatos

import com.example.snapking.modelo.WrapperSala

interface ISalasLectura {
    fun OncallBack(lista:List<WrapperSala>)
}