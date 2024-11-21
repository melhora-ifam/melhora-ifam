package com.example.melhoraifam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_user, container, false)


        val filter: ImageButton = view.findViewById<ImageButton>(R.id.filterButton)
        filter.setOnClickListener() {
            val popupMenu = PopupMenu(context, filter)
            popupMenu.menuInflater.inflate(R.menu.filter_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.filter_option_1 -> {
                        Toast.makeText(context, "Filtrando pelos mais recentes!", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.filter_option_2 -> {
                        Toast.makeText(context, "Filtrando por local!", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.filter_option_3 -> {
                        Toast.makeText(context, "Filtrando por categoria!", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.filter_option_4 -> {
                        Toast.makeText(context, "Filtrando por status!", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
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