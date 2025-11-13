package com.example.actividad2t1

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ClienteAdapter
    private lateinit var dbHelper: ClienteDBHelper
    private lateinit var tvContador: TextView
    private lateinit var etBuscar: EditText

    private var clientes: MutableList<Cliente> = mutableListOf()
    private var clientesFiltrados: MutableList<Cliente> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbHelper = ClienteDBHelper(this)

        tvContador = findViewById(R.id.tvContador)
        etBuscar = findViewById(R.id.etBuscar)
        recyclerView = findViewById(R.id.recyclerView)

        // Lista filtrada como base
        adapter = ClienteAdapter(clientesFiltrados,
            onEditar = { cliente ->
                val intent = Intent(this, ClienteFormActivity::class.java)
                intent.putExtra("cliente_id", cliente.id)
                startActivity(intent)
            },
            onBorrar = { cliente ->
                AlertDialog.Builder(this)
                    .setTitle("¿Eliminar cliente?")
                    .setMessage("¿Estás seguro de eliminar a ${cliente.nombre}?")
                    .setPositiveButton("Sí") { _, _ ->
                        dbHelper.borrarCliente(cliente.id)
                        cargarClientes()
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, ClienteFormActivity::class.java)
            startActivity(intent)
        }

        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                filtrarClientes(text.toString())
            }
        })

        cargarClientes()
    }

    override fun onResume() {
        super.onResume()
        cargarClientes()
    }

    private fun cargarClientes() {
        clientes.clear()
        clientes.addAll(dbHelper.obtenerTodosLosClientes())
        filtrarClientes(etBuscar.text.toString())
    }

    private fun filtrarClientes(query: String) {
        val texto = query.lowercase()
        clientesFiltrados.clear()
        if (texto.isEmpty()) {
            clientesFiltrados.addAll(clientes)
        } else {
            clientesFiltrados.addAll(clientes.filter { c ->
                c.nombre.lowercase().contains(texto)
            })
        }
        adapter.actualizarLista(clientesFiltrados)
        tvContador.text = "Clientes: ${clientesFiltrados.size}"
    }
}
