package com.example.melhoraifam

import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActivityDetalheOcorrencia : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalhe_ocorrencia)

        // Definição dos elementos textuais a serem alterados
        val tv_titulo = findViewById<TextView>(R.id.tv_titulo)
        val tv_data = findViewById<TextView>(R.id.tv_data)
        val tv_local = findViewById<TextView>(R.id.tv_local)
        val tv_descricao = findViewById<TextView>(R.id.tv_descricao)

        // Elementos alteráveis pelo admin
        val spinner_status = findViewById<Spinner>(R.id.spinner_status)
        val card_nivel_1 = findViewById<CardView>(R.id.card_nivel_1)
        val card_nivel_2 = findViewById<CardView>(R.id.card_nivel_2)
        val card_nivel_3 = findViewById<CardView>(R.id.card_nivel_3)
        val card_nivel_4 = findViewById<CardView>(R.id.card_nivel_4)

        // Botões de ação
        val btn_gerar_relatorio = findViewById<Button>(R.id.btn_gerar_relatorio)
        val btn_salvar = findViewById<Button>(R.id.btn_salvar)
    }
}