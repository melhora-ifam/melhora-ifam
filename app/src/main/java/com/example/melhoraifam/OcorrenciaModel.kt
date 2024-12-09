package com.example.melhoraifam

data class OcorrenciaModel(var categoria: String, var descricao: String, var imagem: String,
                           var local: String, var localEspecifico: String, var prioridade: String,
                           var status: String, var titulo: String) {
    constructor() : this("", "", "","",
        "", "", "", "")
}
