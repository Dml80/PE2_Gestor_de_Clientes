package com.example.actividad2t1

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ClienteDBHelper(context: Context) : SQLiteOpenHelper(context, "clientes.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE clientes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre TEXT, " +
                    "email TEXT, " +
                    "telefono TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS clientes")
        onCreate(db)
    }

    // Obtener todos los clientes
    fun obtenerTodosLosClientes(): List<Cliente> {
        val lista = mutableListOf<Cliente>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id, nombre, email, telefono FROM clientes",
            null
        )
        while (cursor.moveToNext()) {
            lista.add(
                Cliente(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono"))
                )
            )
        }
        cursor.close()
        db.close()
        return lista
    }

    // Insertar un cliente
    fun insertarCliente(nombre: String, email: String, telefono: String): Long {
        val db = writableDatabase
        val stmt = db.compileStatement("INSERT INTO clientes (nombre, email, telefono) VALUES (?, ?, ?)")
        stmt.bindString(1, nombre)
        stmt.bindString(2, email)
        stmt.bindString(3, telefono)
        val id = stmt.executeInsert()
        db.close()
        return id
    }

    // Actualizar un cliente
    fun actualizarCliente(id: Int, nombre: String, email: String, telefono: String): Int {
        val db = writableDatabase
        val stmt = db.compileStatement("UPDATE clientes SET nombre = ?, email = ?, telefono = ? WHERE id = ?")
        stmt.bindString(1, nombre)
        stmt.bindString(2, email)
        stmt.bindString(3, telefono)
        stmt.bindLong(4, id.toLong())
        val rows = stmt.executeUpdateDelete()
        db.close()
        return rows
    }

    // Borrar un cliente
    fun borrarCliente(id: Int): Int {
        val db = writableDatabase
        val stmt = db.compileStatement("DELETE FROM clientes WHERE id = ?")
        stmt.bindLong(1, id.toLong())
        val rows = stmt.executeUpdateDelete()
        db.close()
        return rows
    }
}
