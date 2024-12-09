package com.example.melhoraifam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Fragmento da HomePage com a visão de ADMINISTRADOR
 */
class HomeAdminFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var database: DatabaseReference
    private lateinit var ocorrenciasList: MutableList<OcorrenciaModel>
    private lateinit var adapter: OcorrenciaAdapter
    private lateinit var ocorrenciasRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home_admin, container, false)
        Log.d("HomeAdminFragment", "onCreateView: Layout carregado");

        // Lógica do RecyclerView
        database = Firebase.database.reference
        ocorrenciasList = mutableListOf()
        adapter = OcorrenciaAdapter(ocorrenciasList)
        ocorrenciasRecyclerView = view.findViewById<RecyclerView>(R.id.Ocorrencias)
        ocorrenciasRecyclerView.layoutManager = LinearLayoutManager(context)
        recuperarOcorrencias()

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
            popupMenu.menuInflater.inflate(R.menu.admin_filter_menu, popupMenu.menu)

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
                    R.id.filter_option_5 -> {
                        Toast.makeText(context, "Filtrando por prioridade!", Toast.LENGTH_SHORT).show()
                        textFiltragem.text = context?.getString(R.string.por_prioridade_txt)
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }


        // Lógica do navBar
        val bottomNavBar = requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navbar)
        bottomNavBar.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home_navbar -> {
                    Toast.makeText(context, "Homepage", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.usuarios_navbar -> {
                    val ocorrencias: Fragment = ManageUsuariosFragment()
                    val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
                    transaction.replace(R.id.frameLayoutHome, ocorrencias)
                    transaction.commit()
                    true
                }
                R.id.perfil_navbar -> {
                    Toast.makeText(context, "Perfil do usuário", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        return view
    }

    // Lógica de recuperar ocorrências no Firebase
    private fun recuperarOcorrencias() {
        database.child("ocorrencias").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    ocorrenciasList.clear()
                    for (ocorrenciaSnapshot in snapshot.children) {
                        val ocorrencia = ocorrenciaSnapshot.getValue(OcorrenciaModel::class.java)
                        if (ocorrencia != null) {
                            ocorrenciasList.add(ocorrencia)
                        }
                    }
                    adapter = OcorrenciaAdapter(ocorrenciasList) // Adiciona as ocorrências no adapter
                    ocorrenciasRecyclerView.adapter = adapter

                    // Adiciona interatividade nos cards
                    adapter.setOnItemClickListener(object: OcorrenciaAdapter.OnItemClickListener{
                        override fun onItemClick(position: Int) {
                            Toast.makeText(context, "Card nº $position selecionado", Toast.LENGTH_SHORT).show()
                            val intent = Intent(requireContext(), ActivityDetalheOcorrencia::class.java)
                            startActivity(intent)
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HomeAdminFragment", "Erro ao recuperar dados: ${error.message}")            }
        })
    }
}