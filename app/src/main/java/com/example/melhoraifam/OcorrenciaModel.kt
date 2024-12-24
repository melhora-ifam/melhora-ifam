package com.example.melhoraifam

data class OcorrenciaModel(
    var autor: String,
    val autorID: String,
    var categoria: String,
    var data: String,
    var descricao: String,
    var imagem: String,
    var local: String,
    var localEspecifico: String,
    var prioridade: String,
    var status: String,
    var titulo: String) {

    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "")
}
