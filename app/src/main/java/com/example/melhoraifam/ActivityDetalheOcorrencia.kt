package com.example.melhoraifam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ActivityDetalheOcorrencia : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var prioridade: String
    private var isAdmin: Boolean = false

    private lateinit var cardNivel1: CardView
    private lateinit var cardNivel2: CardView
    private lateinit var cardNivel3: CardView
    private lateinit var cardNivel4: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        setContentView(R.layout.activity_detalhe_ocorrencia)

        // Recuperando o ID da ocorrência:
        val idOcorrencia = intent.getStringExtra("OCORRENCIA_ID")

        // Elementos alteráveis pelo admin
        val spinner_status = findViewById<Spinner>(R.id.spinner_status)
        cardNivel1 = findViewById<CardView>(R.id.card_nivel_1)
        cardNivel2 = findViewById<CardView>(R.id.card_nivel_2)
        cardNivel3 = findViewById<CardView>(R.id.card_nivel_3)
        cardNivel4 = findViewById<CardView>(R.id.card_nivel_4)

        // Botões de ação
        val btn_gerar_relatorio = findViewById<Button>(R.id.btn_gerar_relatorio)
        val btn_salvar = findViewById<Button>(R.id.btn_salvar)
        Log.d("Detalhes", "Admin? $isAdmin")

        // Verificando se é admin:
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            checkAdminStatus(currentUser) { isAdminResult ->
                isAdmin = isAdminResult
                Log.d("Detalhes", "Admin? $isAdmin")
                if (isAdmin) {
                    grantAdminPrivileges()
                } else {
                    btn_salvar.visibility = View.GONE
                    btn_gerar_relatorio.visibility = View.GONE
                    spinner_status.visibility = View.GONE
                }
            }
        }

        // Definição dos elementos textuais a serem alterados
        val tv_numero_processo = findViewById<TextView>(R.id.tv_numero_processo)
        val tv_titulo = findViewById<TextView>(R.id.tv_titulo)
        val tv_data = findViewById<TextView>(R.id.tv_data)
        val tv_status = findViewById<TextView>(R.id.status)
        val tv_local = findViewById<TextView>(R.id.tv_local)
        val tv_descricao = findViewById<TextView>(R.id.tv_descricao)
        val tv_categoria = findViewById<TextView>(R.id.tv_categoria)
        val tv_autor = findViewById<TextView>(R.id.tv_autor)



        // Definindo os dados da ocorrência na activity:
        if (idOcorrencia != null) {
            val database = Firebase.database.reference.child("ocorrencias").child(idOcorrencia)
            database.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val ocorrencia = snapshot.getValue(OcorrenciaModel::class.java)
                    if (ocorrencia != null) {
                        val numeroProcessoText = "ID: ${idOcorrencia.toString()}"
                        tv_numero_processo.text = numeroProcessoText
                        tv_data.text = ocorrencia.data
                        tv_titulo.text = ocorrencia.titulo
                        tv_status.text = ocorrencia.status
                        val localText = "${ocorrencia.local} - ${ocorrencia.localEspecifico}"
                        tv_local.text = localText
                        tv_descricao.text = ocorrencia.descricao
                        tv_categoria.text = ocorrencia.categoria
                        tv_autor.text = ocorrencia.autor

                        prioridade = ocorrencia.prioridade
                        definirPrioridade(prioridade)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ActivityDetalheOcorrencia, "Erro ao carregar os dados da ocorrência", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Lógica do Toolbar
        val logo_ifam = findViewById<ImageView>(R.id.logo_ifam)
        logo_ifam.setOnClickListener {
            val intent = Intent(this, Homepage::class.java)
            startActivity(intent)
        }
    }


    // Lógica para definir os botões de prioridade
    private fun definirPrioridade(nivel: String) {
        val defaultColor = getColor(R.color.neutral_mid_500)
        cardNivel1.setCardBackgroundColor(defaultColor)
        cardNivel2.setCardBackgroundColor(defaultColor)
        cardNivel3.setCardBackgroundColor(defaultColor)
        cardNivel4.setCardBackgroundColor(defaultColor)

        val selectedColor = when (nivel) {
            "N1" -> getColor(R.color.error_light_500)
            "N2" -> getColor(R.color.warning_dark_900)
            "N3" -> getColor(R.color.warning_light_400)
            "N4" -> getColor(R.color.primary_light_green_300)
            else -> defaultColor
        }

        prioridade = nivel
        when (nivel) {
            "N1" -> cardNivel1.setCardBackgroundColor(selectedColor)
            "N2" -> cardNivel2.setCardBackgroundColor(selectedColor)
            "N3" -> cardNivel3.setCardBackgroundColor(selectedColor)
            "N4" -> cardNivel4.setCardBackgroundColor(selectedColor)
        }
    }

    private fun checkAdminStatus(currentUser: FirebaseUser, onResult: (Boolean) -> Unit) {
        val userID = currentUser.uid
        val database = Firebase.database
        val userRef = database.getReference("users").child(userID)

        // Lógica de acesso ao atributo admin no Realtime Database
        userRef.child("admin").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val adminStatus = snapshot.getValue(Boolean::class.java) ?: false
                    Log.d("Detalhes", "Atributo admin encontrado: $adminStatus")
                    onResult(adminStatus) // Retorna o valor via callback
                } else {
                    Log.d("Detalhes", "Atributo admin não encontrado")
                    onResult(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Detalhes", "Erro ao acessar o banco de dados: ${error.message}")
                onResult(false)
            }
        })
    }

    private fun grantAdminPrivileges() {
        // Trocar o text view pelo spinner
        val tv_status = findViewById<TextView>(R.id.status)
        tv_status.visibility = View.GONE

        // Deixar os botões de prioridade interagíveis
        val cardViews = mapOf(
            cardNivel1 to "N1",
            cardNivel2 to "N2",
            cardNivel3 to "N3",
            cardNivel4 to "N4"
        )

        cardViews.forEach { (cardView, priori) ->
            cardView.setOnClickListener {
                definirPrioridade(priori)
            }
        }
    }
}