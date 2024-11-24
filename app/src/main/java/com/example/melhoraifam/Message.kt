package com.example.melhoraifam

data class Mensagem(
    val conteudo: String,
    val remetente: Remetente,
    val destinatario: Destinatario,
    val timestamp: Long
)
