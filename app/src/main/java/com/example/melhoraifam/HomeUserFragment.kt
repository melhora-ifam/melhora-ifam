package com.example.melhoraifam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Fragmento da HomePage com a visão de USUÁRIO
 */
class HomeUserFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_home_user, container, false)

        // Lógica do RecyclerView
        /*
        val recyclerView = view.findViewById<RecyclerView>(R.id.Ocorrencias)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val ocorrenciasList = arrayListOf(
            OcorrenciaModel(R.drawable.slowpoke, "Ocorrência 1", "Descrição da ocorrência 1", "Status 1", "Pr.1", "Local 1", "Categoria 1"),
            OcorrenciaModel(R.drawable.slowpoke, "Ocorrência 2", "Descrição da ocorrência 2", "Status 2", "Pr.2", "Local 2", "Categoria 2"),
            OcorrenciaModel(R.drawable.slowpoke, "Ocorrência 3", "Descrição da ocorrência 3", "Status 3", "Pr.3", "Local 3", "Categoria 3"),
            OcorrenciaModel(R.drawable.slowpoke, "Ocorrência 4", "Descrição da ocorrência 4", "Status 4", "Pr.4", "Local 4", "Categoria 4")
        )
        recyclerView.adapter = OcorrenciaAdapter(ocorrenciasList)
        */

        // Lógica do FAB
        val fab = view.findViewById<FloatingActionButton>(R.id.fabAdicionarOcorrencia)
        fab.setOnClickListener() {
            Toast.makeText(context, "Adicionando ocorrência...", Toast.LENGTH_SHORT).show()
        }

        // Lógica da barra de pesquisa
        val search = view.findViewById<SearchView>(R.id.barraDePesquisaHomeAdmin)
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(context, "Procurando por: $query", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Toast.makeText(context, "Texto digitado: $newText", Toast.LENGTH_SHORT).show()
                return true
            }
        })

        // Lógica do botão de filtro
        val filter: ImageButton = view.findViewById<ImageButton>(R.id.filterButton)
        filter.setOnClickListener() { v->
            val popupMenu = PopupMenu(context, v)
            val textFiltragem = view.findViewById<TextView>(R.id.textFiltragem)
            popupMenu.menuInflater.inflate(R.menu.filter_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.filter_option_1 -> {
                        Toast.makeText(context, "Filtrando pelos mais recentes!", Toast.LENGTH_SHORT).show()
                        textFiltragem.text = context?.getString(R.string.mais_recentes_txt)
                        true
                    }
                    R.id.filter_option_2 -> {
                        Toast.makeText(context, "Filtrando por local!", Toast.LENGTH_SHORT).show()
                        textFiltragem.text = context?.getString(R.string.por_local_txt)
                        true
                    }
                    R.id.filter_option_3 -> {
                        Toast.makeText(context, "Filtrando por categoria!", Toast.LENGTH_SHORT).show()
                        textFiltragem.text = context?.getString(R.string.por_categoria_txt)
                        true
                    }
                    R.id.filter_option_4 -> {
                        Toast.makeText(context, "Filtrando por status!", Toast.LENGTH_SHORT).show()
                        textFiltragem.text = context?.getString(R.string.por_status_txt)
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }


        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeUserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}