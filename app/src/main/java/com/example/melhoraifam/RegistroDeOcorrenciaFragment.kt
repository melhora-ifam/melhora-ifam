package com.example.melhoraifam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner

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
        val spinner: Spinner = view.findViewById(R.id.categoria)

        // Criar o ArrayAdapter com as opções
        val adapter = ArrayAdapter.createFromResource(
            requireContext(), // use 'requireContext()' ao invés de 'this' para o contexto no Fragment
            R.array.categoria_options,
            android.R.layout.simple_spinner_item
        )

        // Especificar o layout para a lista suspensa
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Definir o adaptador para o Spinner
        spinner.adapter = adapter

        return view // Retornar a view inflada
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegistroDeOcorrenciaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}