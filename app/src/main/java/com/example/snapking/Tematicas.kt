package com.example.snapking

import com.example.snapking.BaseDatos.BaseDatos

class Tematicas{
var casa=arrayListOf<String>(
    "armario",
    "balcón",
    "baño",
    "comedor",
    "cuarto",
    "despacho",
    "dormitorio",
    "espejo",
    "fregadero",
    "horno",
    "hornomicroondas",
    "jardín",
    "vabo",
    "vadero",
    "vaptos",
    "pasillo",
    "patio",
    "salón",
    "sótano",
    "techo",
    "bañera",
    "cama",
    "cocina",
    "cocina",
    "escalera",
    "vadora",
    "mesa",
    "nevera",
    "puerta",
    "sil",
    "terraza",
    "ventana")

    var familia= arrayListOf<String>(
        "abuelo","abue","abuelos",
        "bisabuelo","bisabue","bisabuelos",
        "bisnieto","bisnieta","bisnietos",
        "cuñado","cuñada",
        "esposo","esposa","esposos",
        "hermanastro","hermanastra",
        "hermano","hermana",
        "hijastro","hijastra",
        "hijo","hija","hijos",
        "nieto","nieta","nietos",
        "padrastro","madrastra",
        "padre","madre","padres",
        "primo","prima",
        "sobrino","sobrina",
        "suegro","suegra",
        "tatarabuelo","tatarabue",
        "tío","tía",
        "yerno","nuera"
        )
    
    var animales = arrayListOf<String>(

        "mono",
        "fauna",
        "rata",
        "águila",
        "ave",
        "caballo",
        "camello",
        "canario",
        "canguro",
        "carnero",
        "cerdo",
        "chimpancé",
        "chivo",
        "cocodrilo",
        "conejo",
        "delfín",
        "elefante",
        "gallo",
        "gato",
        "halcón",
        "hipopótamo",
        "insecto",
        "león",
        "lobo",
        "loro",
        "oso",
        "panda",
        "pato",
        "pelícano",
        "perro",
        "pez",
        "pingüino",
        "ratón",
        "tiburón",
        "tigre",
        "toro",
        "zorro",
        "abeja",
        "araña",
        "ardilla",
        "cobra",
        "foca",
        "hormiga",
        "jirafa",
        "mosca",
        "rana",
        "serpiente",
        "tortuga",
        "trucha"
    )

    var cuerpoHumano = arrayListOf<String>(
                "bigote",
                 "brazo",
                 "cabello",
                 "corazón",
                 "dedo",
                 "diente",
                 "hombro",
                 "labio",
                 "ojo",
                 "pecho",
                 "pie",
                 "tobillo",
                "barba",
                "boca",
                "cabeza",
                "cara",
                "ceja",
                "espalda",
                "frente",
                "garganta",
                "lengua",
                "mano",
                "mejilla",

                "muñeca",
                "nalga",
                "nariz",
                "oreja",
                "pestaña",
                "piel",
                "pierna",
                "rodilla",
    )


    var ropa = arrayListOf<String>(
         "anillo",
                 "pendiente",
                 "cinturón",
                 "gorro",
                 "guante",
                 "pantalón",
                 "sombrero",
                 "sostén",
                 "suéter",
                 "traje",
                 "vestido",
                 "billetera",
                 "blusa",
                 "boina",
                 "camisa",
                 "camiseta",
                 "chaqueta",
                 "corbata",
                 "falda",
                 "gorra",
                 "piyama",
                "botas",
                "gafas",
                "pantuflas",
                "calcetines",
                "calzoncillos",
                "zapatos"
    )

    var objetosComunes = arrayListOf<String>(
        "agenda",
        "cuaderno",
        "periódico",
        "diccionario",
        "bolígrafo",
        "lápiz",
        "mesa",
        "silla",
        "lámpara",
        "celular",
         "ordenador",
        "llave",
        "bolso",
        "hoja de papel",
        "borrador",
        "carta",
        "postal",
        "mapa",
        "sobre",
        "sello"
    )

    public fun cargarTematicas() {
        var db = BaseDatos.getInstance()!!.database

        Tematicas().casa.forEach{
            db.reference.child("tematicas").child("casa").push().setValue(it)
        }

        Tematicas().familia.forEach{
            db.reference.child("tematicas").child("familia").push().setValue(it)
        }

        Tematicas().animales.forEach{
            db.reference.child("tematicas").child("animales").push().setValue(it)
        }

        Tematicas().cuerpoHumano.forEach{
            db.reference.child("tematicas").child("cuerpo_humano").push().setValue(it)
        }
        Tematicas().ropa.forEach{
            db.reference.child("tematicas").child("ropa").push().setValue(it)
        }

        Tematicas().objetosComunes.forEach{
            db.reference.child("tematicas").child("objetos_comunes").push().setValue(it)
        }


    }





}