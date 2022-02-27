package com.example.snapking.modelo

import kotlinx.serialization.Serializable

@Serializable
data class WrapperSala(var id:String="",var sala:Sala) {


    fun serialize(){
        var result=id

    }
}