package com.example.palestratracker

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    // Lista per memorizzare gli allenamenti
    private val allenamenti = mutableListOf<Allenamento>()

    // Componenti UI
    private lateinit var editTextEsercizio: EditText
    private lateinit var editTextSerie: EditText
    private lateinit var editTextRipetizioni: EditText
    private lateinit var editTextPeso: EditText
    private lateinit var buttonAggiungi: Button
    private lateinit var listViewAllenamenti: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val listaStringhe = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Collego i componenti
        editTextEsercizio = findViewById(R.id.editTextEsercizio)
        editTextSerie = findViewById(R.id.editTextSerie)
        editTextRipetizioni = findViewById(R.id.editTextRipetizioni)
        editTextPeso = findViewById(R.id.editTextPeso)
        buttonAggiungi = findViewById(R.id.buttonAggiungi)
        listViewAllenamenti = findViewById(R.id.listViewAllenamenti)

        // Imposto l'adapter per la lista
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaStringhe)
        listViewAllenamenti.adapter = adapter

        // Listener per il bottone
        buttonAggiungi.setOnClickListener {
            aggiungiAllenamento()
        }

        // Listener per eliminare allenamento con click lungo
        listViewAllenamenti.setOnItemLongClickListener { _, _, position, _ ->
            eliminaAllenamento(position)
            true
        }
    }

    private fun aggiungiAllenamento() {
        val esercizio = editTextEsercizio.text.toString()
        val serieText = editTextSerie.text.toString()
        val ripetizioniText = editTextRipetizioni.text.toString()
        val pesoText = editTextPeso.text.toString()

        // Controllo che i campi non siano vuoti
        if (esercizio.isEmpty()) {
            Toast.makeText(this, "Inserisci il nome dell'esercizio", Toast.LENGTH_SHORT).show()
            return
        }

        if (serieText.isEmpty()) {
            Toast.makeText(this, "Inserisci il numero di serie", Toast.LENGTH_SHORT).show()
            return
        }

        if (ripetizioniText.isEmpty()) {
            Toast.makeText(this, "Inserisci il numero di ripetizioni", Toast.LENGTH_SHORT).show()
            return
        }

        if (pesoText.isEmpty()) {
            Toast.makeText(this, "Inserisci il peso", Toast.LENGTH_SHORT).show()
            return
        }

        // Converto i numeri
        val serie = serieText.toIntOrNull()
        val ripetizioni = ripetizioniText.toIntOrNull()
        val peso = pesoText.toDoubleOrNull()

        if (serie == null || serie <= 0) {
            Toast.makeText(this, "Inserisci un numero valido per le serie", Toast.LENGTH_SHORT).show()
            return
        }

        if (ripetizioni == null || ripetizioni <= 0) {
            Toast.makeText(this, "Inserisci un numero valido per le ripetizioni", Toast.LENGTH_SHORT).show()
            return
        }

        if (peso == null || peso < 0) {
            Toast.makeText(this, "Inserisci un peso valido", Toast.LENGTH_SHORT).show()
            return
        }

        // Creo l'allenamento
        val dataOggi = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val nuovoAllenamento = Allenamento(esercizio, serie, ripetizioni, peso, dataOggi)

        // Aggiungo alla lista
        allenamenti.add(nuovoAllenamento)

        // Aggiorno la lista visualizzata
        val stringaAllenamento = "$esercizio - ${serie}x$ripetizioni - ${peso}kg - $dataOggi"
        listaStringhe.add(stringaAllenamento)
        adapter.notifyDataSetChanged()

        // Pulisco i campi
        editTextEsercizio.text.clear()
        editTextSerie.text.clear()
        editTextRipetizioni.text.clear()
        editTextPeso.text.clear()

        Toast.makeText(this, "Allenamento aggiunto!", Toast.LENGTH_SHORT).show()
    }

    private fun eliminaAllenamento(position: Int) {
        if (position >= 0 && position < allenamenti.size) {
            allenamenti.removeAt(position)
            listaStringhe.removeAt(position)
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Allenamento eliminato", Toast.LENGTH_SHORT).show()
        }
    }
}

// Classe per rappresentare un allenamento
data class Allenamento(
    val esercizio: String,
    val serie: Int,
    val ripetizioni: Int,
    val peso: Double,
    val data: String
)