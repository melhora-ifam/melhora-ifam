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
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Inclui a lógica de incluir uma nova
 * ocorrência no banco de dados
 */
class RegistroDeOcorrenciaFragment : Fragment() {

    // Definir parâmetros, se necessário
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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
        // val localInput = view.findViewById<Spinner>(R.id.local)
        val localEspecificoInput = view.findViewById<EditText>(R.id.local_especifico_input)
        // val categoriaInput = view.findViewById<Spinner>(R.id.categoria)
        val descricaoInput = view.findViewById<EditText>(R.id.descricao)

        // Enviar os dados para o firebase
        val buttonEnviar = view.findViewById<Button>(R.id.buttonEnviar)
        buttonEnviar.setOnClickListener {
            val titulo = tituloInput.text.toString().trim()
            val local = localInput.selectedItem.toString()
            val localEspecifico = localEspecificoInput.text.toString().trim()
            val categoria = categoriaInput.selectedItem.toString()
            val descricao = descricaoInput.text.toString().trim()
            val prioridade = "N1"
            val status = "Em aberto"
            val imagem = "https://www.pokemon.com/static-assets/content-assets/cms2/img/pokedex/full/079.png"

            if (titulo.isEmpty() || localEspecifico.isEmpty() || descricao.isEmpty()){
                Toast.makeText(context, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            } else {
                val ocorrenciaMap = mapOf(
                    "titulo" to titulo,
                    "local" to local,
                    "localEspecifico" to localEspecifico,
                    "categoria" to categoria,
                    "descricao" to descricao,
                    "prioridade" to prioridade,
                    "status" to status,
                    "imagem" to imagem
                )

                registrarOcorrencia(ocorrenciaMap)
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
}