package com.example.actividad2t1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ClienteFormActivity : AppCompatActivity() {
    private var clienteId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_form)

        val nombreEditText = findViewById<EditText>(R.id.etNombre)
        val emailEditText = findViewById<EditText>(R.id.etEmail)
        val telefonoEditText = findViewById<EditText>(R.id.etTelefono)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        // Comprobar si venimos para editar (recibimos cliente_id por intent)
        clienteId = intent.getIntExtra("cliente_id", -1)
        if (clienteId != -1) {
            val dbHelper = ClienteDBHelper(this)
            val db = dbHelper.readableDatabase
            val cursor = db.rawQuery(
                "SELECT nombre, email, telefono FROM clientes WHERE id = ?",
                arrayOf(clienteId.toString()))
            if (cursor.moveToFirst()) {
                nombreEditText.setText(cursor.getString(0))
                emailEditText.setText(cursor.getString(1))
                telefonoEditText.setText(cursor.getString(2))
            }
            cursor.close()
            db.close()
        } else {
            clienteId = null // No editar, sino crear
        }

        btnGuardar.setOnClickListener {
            val nombre = nombreEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val telefono = telefonoEditText.text.toString().trim()

            // Validaciones básicas
            if (nombre.isEmpty()) {
                nombreEditText.error = "El nombre es obligatorio"
                return@setOnClickListener
            }
            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditText.error = "Introduce un email válido"
                return@setOnClickListener
            }
            if (telefono.length < 9) {
                telefonoEditText.error = "El teléfono debe tener al menos 9 números"
                return@setOnClickListener
            }

            val dbHelper = ClienteDBHelper(this)
            if (clienteId != null && clienteId != -1) {
                // Editar existente
                dbHelper.actualizarCliente(clienteId!!, nombre, email, telefono)
                Toast.makeText(this, "Cliente actualizado", Toast.LENGTH_SHORT).show()
            } else {
                // Crear nuevo
                dbHelper.insertarCliente(nombre, email, telefono)
                Toast.makeText(this, "Cliente guardado", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }
}
