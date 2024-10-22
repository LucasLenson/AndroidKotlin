package com.example.salobeleza.view

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.salobeleza.databinding.ActivityAgendamentoBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class Agendamento : AppCompatActivity() {

    private lateinit var binding: ActivityAgendamentoBinding
    private val calendar: Calendar = Calendar.getInstance()
    private var data: String = ""
    private var hora: String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgendamentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val nome = intent.extras?.getString("nome").toString()

        val datePicker = binding.datepicker
        datePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->

            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            var dia = dayOfMonth.toString()
            val mes: String

            // Ajuste para formatação do dia e mês
            if (dayOfMonth < 10) {
                dia = "0$dayOfMonth"
            }

            mes = if (monthOfYear < 10) {
                "0${monthOfYear + 1}"
            } else {
                (monthOfYear + 1).toString()
            }

            data = "$dia/$mes/$year"
        }

        binding.timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->

            val minuto: String = if (minute < 10) {
                "0$minute"
            } else {
                minute.toString()
            }

            hora = "$hourOfDay:$minuto"
        }

        binding.timePicker.setIs24HourView(true)

        binding.btAgendar.setOnClickListener {

            val barbeiro1 = binding.barbeiro1
            val barbeiro2 = binding.barbeiro2

            // Validações
            when {
                hora.isEmpty() -> {
                    mensagem(it, "Preencha o horário", "#FF0000")
                }
                // Convertendo a hora para uma verificação numérica correta
                !horaValida(hora) -> {
                    mensagem(it, "O salão está fechado! Horário de atendimento das 08:00 às 19:00!", "#FF0000")
                }
                data.isEmpty() -> {
                    mensagem(it, "Preencha a data!", "#FF0000")
                }
                barbeiro1.isChecked && data.isNotEmpty() && hora.isNotEmpty() -> {
                    salvarAgendamento(it, nome, "Lucas Alves Rodrigues", data, hora)
                }
                barbeiro2.isChecked && data.isNotEmpty() && hora.isNotEmpty() -> {
                    salvarAgendamento(it, nome, "Lucas Luis Inacio", data, hora)
                }
                else -> {
                    mensagem(it, "Escolha um atendente!", "#FF0000")
                }
            }
        }
    }

    // Método para validar se a hora está no intervalo correto
    private fun horaValida(hora: String): Boolean {
        val (horas, minutos) = hora.split(":").map { it.trim().toInt() }
        return horas in 8..19 && minutos in 0..59
    }

    private fun mensagem(view: View, mensagem: String, cor: String) {
        val snackbar = Snackbar.make(view, mensagem, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(Color.parseColor(cor))
        snackbar.setTextColor(Color.parseColor("#FFFFFF"))
        snackbar.show()
    }

    private fun salvarAgendamento(view: View, cliente: String, barbeiro: String, data: String, hora: String) {
        val db = FirebaseFirestore.getInstance()

        val dadosUsuario = hashMapOf(
            "cliente" to cliente,
            "barbeiro" to barbeiro,
            "data" to data,
            "hora" to hora
        )

        db.collection("agendamento").document(cliente)
            .set(dadosUsuario)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    mensagem(view, "Agendamento realizado com sucesso!", "#FF03DAC5")
                } else {
                    mensagem(view, "Erro ao realizar o agendamento!", "#FF0000")
                }
            }
            .addOnFailureListener {
                mensagem(view, "Erro no servidor!", "#FF0000")
            }
    }
}
