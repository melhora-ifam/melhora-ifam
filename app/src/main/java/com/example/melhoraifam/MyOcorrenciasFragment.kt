package com.example.melhoraifam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyOcorrenciasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyOcorrenciasFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var database: DatabaseReference
    private lateinit var ocorrenciasList: MutableList<OcorrenciaModel>
    private lateinit var idsList: MutableList<String>
    private lateinit var adapter: OcorrenciaAdapter
    private lateinit var ocorrenciasRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_ocorrencias, container, false)

        // Lógica do RecyclerView
        database = Firebase.database.reference
        ocorrenciasList = mutableListOf()
        idsList = mutableListOf()
        adapter = OcorrenciaAdapter(ocorrenciasList, idsList)
        ocorrenciasRecyclerView = view.findViewById(R.id.minhas_ocorrencias)
        ocorrenciasRecyclerView.layoutManager = LinearLayoutManager(context)
        recuperarMinhasOcorrencias()

        // Lógica do navBar
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navbar)
        navBar.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home_navbar -> {
                    val home: Fragment = HomeUserFragment()
                    val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
                    transaction.replace(R.id.frameLayoutHome, home)
                    transaction.commit()
                    true
                }
                R.id.ocorrencias_navbar -> {
                    Toast.makeText(context, "Minhas ocorrecias", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.perfil_navbar -> {
                    val perfilFragment: Fragment = PerfilFragment()
                    val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
                    transaction.replace(R.id.frameLayoutHome, perfilFragment)
                    transaction.commit()
                    true
                }
                else -> false
            }
        }

        return view
    }

    private fun recuperarMinhasOcorrencias() {
        val currentUser = Firebase.auth.currentUser
        val userID = currentUser?.uid

        if (userID == null) {
            Toast.makeText(context, "Erro: Usuário não autenticado!", Toast.LENGTH_SHORT).show()
            return
        }

        database.child("ocorrencias").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    ocorrenciasList.clear()
                    idsList.clear()
                    for (ocorrenciaSnapshot in snapshot.children) {
                        val ocorrencia = ocorrenciaSnapshot.getValue(OcorrenciaModel::class.java)
                        val id = ocorrenciaSnapshot.key
                        if (ocorrencia != null && id != null && ocorrencia.autorID == userID) {
                            ocorrenciasList.add(ocorrencia)
                            idsList.add(id)
                        }
                    }
                    adapter = OcorrenciaAdapter(ocorrenciasList, idsList) // Adiciona as ocorrências no adapter
                    ocorrenciasRecyclerView.adapter = adapter

                    // Adiciona interatividade nos cards
                    adapter.setOnItemClickListener(object: OcorrenciaAdapter.OnItemClickListener{
                        override fun onItemClick(id: String) {
                            Toast.makeText(context, "Card com o id $id selecionado", Toast.LENGTH_SHORT).show()
                            val intent = Intent(requireContext(), ActivityDetalheOcorrencia::class.java)
                            intent.putExtra("OCORRENCIA_ID", id)
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