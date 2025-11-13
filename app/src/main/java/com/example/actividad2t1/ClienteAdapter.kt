package com.example.actividad2t1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClienteAdapter(
    private var clientes: List<Cliente>,
    private val onEditar: (Cliente) -> Unit,
    private val onBorrar: (Cliente) -> Unit
) : RecyclerView.Adapter<ClienteAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvEmail: TextView = view.findViewById(R.id.tvEmail)
        val tvTelefono: TextView = view.findViewById(R.id.tvTelefono)
        val btnEditar: ImageButton = view.findViewById(R.id.btnEditar)
        val btnBorrar: ImageButton = view.findViewById(R.id.btnBorrar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cliente = clientes[position]
        holder.tvNombre.text = cliente.nombre
        holder.tvEmail.text = cliente.email
        holder.tvTelefono.text = cliente.telefono

        holder.btnEditar.setOnClickListener { onEditar(cliente) }
        holder.btnBorrar.setOnClickListener { onBorrar(cliente) }
    }

    override fun getItemCount(): Int = clientes.size

    fun actualizarLista(nuevaLista: List<Cliente>) {
        clientes = nuevaLista
        notifyDataSetChanged()
    }
}
