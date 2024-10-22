package com.example.salobeleza.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.salobeleza.R
import com.example.salobeleza.adapter.ServicosAdapter
import com.example.salobeleza.databinding.ActivityHomeBinding
import com.example.salobeleza.model.Servicos

class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var servicosAdapter: ServicosAdapter
    private val listaServicos: MutableList<Servicos> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val nome = intent.extras?.getString("nome") ?: "Usuário"

        binding.txtNomeUsuario.text = "Bem-vindo, $nome"
        val recyclerViewServicos = binding.recyclerViewServicos
        recyclerViewServicos.layoutManager = GridLayoutManager(this, 2)
        servicosAdapter = ServicosAdapter(this, listaServicos)
        recyclerViewServicos.setHasFixedSize(true)
        recyclerViewServicos.adapter = servicosAdapter
        getServicos()

        binding.btAgendar.setOnClickListener {
            val intent = Intent(this, Agendamento::class.java)
            intent.putExtra("nome",nome)
            startActivity(intent)
        }

    }

    private fun getServicos() {
        listaServicos.clear() // Limpa a lista antes de adicionar novamente
        val servico1 = Servicos(R.drawable.corte, "Corte de cabelo")
        listaServicos.add(servico1)

        val servico2 = Servicos(R.drawable.manicure, "Manicure")
        listaServicos.add(servico2)

        val servico3 = Servicos(R.drawable.tintura, "Tintura")
        listaServicos.add(servico3)

        val servico4 = Servicos(R.drawable.tratamento, "Tratamento capilar")
        listaServicos.add(servico4)

        servicosAdapter.notifyDataSetChanged() // Notifica o adaptador sobre as alterações
    }

}
