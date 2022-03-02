package com.example.snapking.BaseDatos

import com.example.snapking.modelo.Foto

interface IGetFotos {
    fun OnCallBack(fotos : ArrayList<Foto>)
}