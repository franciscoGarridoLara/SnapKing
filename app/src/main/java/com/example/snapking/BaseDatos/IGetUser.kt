package com.example.snapking.BaseDatos

import com.example.snapking.modelo.Usuario

interface IGetUser {
    fun OnCallBack(user: Usuario)
}