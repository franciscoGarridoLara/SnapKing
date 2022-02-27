package com.example.snapking.BaseDatos

import com.example.snapking.Wrapper.WrapperUsuarioLobby

interface IGetUsersFromSala {
    fun OnCallBack(lista:List<WrapperUsuarioLobby>)
}