package com.example.melhoraifam

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.melhoraifam.databinding.ActivityMainBinding
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Criando intent para ir para a home
        /*val btnHome = findViewById<Button>(R.id.btnHome)
        btnHome.setOnClickListener() {
            val intent = Intent(this, Homepage::class.java)
            startActivity(intent)
        }*/

        // A PARTIR DAQUI, FUNÇÕES CRIADAS PELA JÚLIA
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        loadFragment1(toolbar())
        loadFragment(login())
    }


    /*private fun loadFragment1(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer1, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }*/

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayoutMain, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
