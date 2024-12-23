package com.example.melhoraifam

data class UsuarioModel(var imagem: Int, var nome: String, var admin: Boolean){

    constructor() : this(0, "", false)
}
