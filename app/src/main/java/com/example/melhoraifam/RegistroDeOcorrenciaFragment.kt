package com.example.melhoraifam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Inclui a lógica de incluir uma nova
 * ocorrência no banco de dados
 */
class RegistroDeOcorrenciaFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflar o layout para este fragmento
        val view = inflater.inflate(R.layout.fragment_registro_de_ocorrencia, container, false)

        // Acessar o Spinner usando 'view'
        val categoriaInput: Spinner = view.findViewById(R.id.categoria)
        val localInput = view.findViewById<Spinner>(R.id.local)

        // Criar o ArrayAdapter com as opções
        val categoriaAdapter = ArrayAdapter.createFromResource(
            requireContext(), // use 'requireContext()' ao invés de 'this' para o contexto no Fragment
            R.array.categoria_options,
            android.R.layout.simple_spinner_item
        )
        val localAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.local_options,
            android.R.layout.simple_spinner_item
        )

        // Especificar o layout para a lista suspensa
        categoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        localAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Definir o adaptador para o Spinner
        categoriaInput.adapter = categoriaAdapter
        localInput.adapter = localAdapter

        val tituloInput = view.findViewById<EditText>(R.id.tituloOcorrencia)
        val localEspecificoInput = view.findViewById<EditText>(R.id.local_especifico_input)
        val descricaoInput = view.findViewById<EditText>(R.id.descricao)

        // Enviar os dados para o firebase
        val buttonEnviar = view.findViewById<Button>(R.id.buttonEnviar)
        buttonEnviar.setOnClickListener {
            val titulo = tituloInput.text.toString().trim()
            val local = localInput.selectedItem.toString()
            val localEspecifico = localEspecificoInput.text.toString().trim()
            val categoria = categoriaInput.selectedItem.toString()
            val descricao = descricaoInput.text.toString().trim()
            val prioridade = "N4" // Padrão, menor prioridade
            val status = "Em aberto" // Padrão, status inicial
            val imagem = "https://www.pokemon.com/static-assets/content-assets/cms2/img/pokedex/full/079.png"
            val data = recuperarDataAtual()

            if (titulo.isEmpty() || localEspecifico.isEmpty() || descricao.isEmpty()){
                Toast.makeText(context, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            } else {
                // Recuperar o nome do usuário que enviou a ocorrência
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                if (uid != null) {
                    val userRef = Firebase.database.getReference("users").child(uid)
                    userRef.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val autor = snapshot.child("nome").value?.toString() ?: "Usuário desconhecido"

                            val ocorrenciaMap = mapOf(
                                "titulo" to titulo,
                                "local" to local,
                                "localEspecifico" to localEspecifico,
                                "categoria" to categoria,
                                "descricao" to descricao,
                                "prioridade" to prioridade,
                                "status" to status,
                                "imagem" to imagem,
                                "data" to data,
                                "autor" to autor
                            )

                            registrarOcorrencia(ocorrenciaMap)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(context, "Erro ao recuperar o autor da ocorrência", Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    Toast.makeText(context, "Usuário não autenticado!", Toast.LENGTH_SHORT).show()
                }
            }
        }


        return view // Retornar a view inflada
    }

    private fun registrarOcorrencia(ocorrenciaMap: Map<String, String>) {
        val database = Firebase.database
        val ocorrenciaID = database.getReference("ocorrencias").push().key
        if (ocorrenciaID != null) {
            database.getReference("ocorrencias").child(ocorrenciaID).setValue(ocorrenciaMap)
                .addOnSuccessListener {
                    Toast.makeText(context, "Ocorrência registrada com sucesso!", Toast.LENGTH_SHORT).show()
                    val home: Fragment = HomeUserFragment()
                    val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
                    transaction.replace(R.id.frameLayoutHome, home)
                    transaction.commit()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Erro ao eegistrar ocorrência: $e", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "Erro ao gerar ID para ocorrência", Toast.LENGTH_SHORT).show()
        }
    }

    private fun recuperarDataAtual(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }
}