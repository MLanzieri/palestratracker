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
    private lateinit var textViewFraseMotivazionale: TextView
    private lateinit var buttonNuovaFrase: Button
    private lateinit var editTextEsercizio: EditText
    private lateinit var editTextSerie: EditText
    private lateinit var editTextRipetizioni: EditText
    private lateinit var editTextPeso: EditText
    private lateinit var buttonAggiungi: Button
    private lateinit var listViewAllenamenti: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val listaStringhe = mutableListOf<String>()

    // Array di frasi motivazionali
    private val frasiMotivazionali = arrayOf(
        "Il dolore che senti oggi è la forza che sentirai domani",
        "Non smettere quando sei stanco, smetti quando hai finito",
        "Il tuo corpo può farcela. È la tua mente che devi convincere",
        "Ogni esperto è stato una volta un principiante",
        "Non aspettare di essere motivato, sii disciplinato",
        "La costanza batte il talento quando il talento non è costante",
        "Fai oggi quello che gli altri non faranno",
        "Non si tratta di essere perfetti, si tratta di essere migliori di ieri",
        "La palestra è dove le scuse muoiono e i risultati nascono",
        "Il sudore è grasso che piange",
        "Non puoi comprare la forma fisica, devi guadagnartela",
        "Ogni allenamento è un passo verso il tuo obiettivo",
        "La forza viene dalla volontà, non dalla capacità fisica",
        "Chi si allena trova soluzioni, non scuse"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Collego i componenti
        textViewFraseMotivazionale = findViewById(R.id.textViewFraseMotivazionale)
        buttonNuovaFrase = findViewById(R.id.buttonNuovaFrase)
        editTextEsercizio = findViewById(R.id.editTextEsercizio)
        editTextSerie = findViewById(R.id.editTextSerie)
        editTextRipetizioni = findViewById(R.id.editTextRipetizioni)
        editTextPeso = findViewById(R.id.editTextPeso)
        buttonAggiungi = findViewById(R.id.buttonAggiungi)
        listViewAllenamenti = findViewById(R.id.listViewAllenamenti)

        // Imposto l'adapter per la lista
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaStringhe)
        listViewAllenamenti.adapter = adapter

        // Mostro una frase motivazionale all'avvio
        mostraFraseMotivazionale()

        // Listener per il bottone nuova frase
        buttonNuovaFrase.setOnClickListener {
            mostraFraseMotivazionale()
        }

        // Listener per il bottone aggiungi allenamento
        buttonAggiungi.setOnClickListener {
            aggiungiAllenamento()
        }

        // Listener per eliminare allenamento con click lungo
        listViewAllenamenti.setOnItemLongClickListener { _, _, position, _ ->
            eliminaAllenamento(position)
            true
        }
    }

    private fun mostraFraseMotivazionale() {
        val random = Random()
        val indiceRandomico = random.nextInt(frasiMotivazionali.size)
        val fraseScelta = frasiMotivazionali[indiceRandomico]
        textViewFraseMotivazionale.text = fraseScelta
        Toast.makeText(this, "Nuova motivazione! 💪", Toast.LENGTH_SHORT).show()
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

        Toast.makeText(this, "Allenamento aggiunto! Forza! 💪", Toast.LENGTH_SHORT).show()
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