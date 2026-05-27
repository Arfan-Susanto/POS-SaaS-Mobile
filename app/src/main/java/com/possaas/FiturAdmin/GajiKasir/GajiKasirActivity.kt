package com.possaas.FiturAdmin.GajiKasir

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.possaas.R

class GajiKasirActivity : AppCompatActivity() {

    private lateinit var recyclerKasir:
            RecyclerView

    private lateinit var adapter:
            GajiKasirAdapter

    private val listKasir =
        ArrayList<GajiKasirModel>()

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_gaji_kasir
        )

        recyclerKasir =
            findViewById(
                R.id.recyclerKasir
            )

        val btnBack =
            findViewById<LinearLayout>(
                R.id.btnBack
            )

        recyclerKasir.layoutManager =
            LinearLayoutManager(this)

        adapter =
            GajiKasirAdapter(listKasir)

        recyclerKasir.adapter =
            adapter

        btnBack.setOnClickListener {

            finish()
        }

        loadKasir()
    }

    private fun loadKasir() {

        FirebaseDatabase
            .getInstance()
            .getReference("users")
            .addValueEventListener(

                object : ValueEventListener {

                    override fun onDataChange(
                        snapshot: DataSnapshot
                    ) {

                        listKasir.clear()

                        for (data in snapshot.children) {

                            val role =
                                data.child("role")
                                    .value
                                    .toString()

                            // HANYA KASIR
                            if (role == "KASIR") {

                                val model =
                                    GajiKasirModel(

                                        uid =
                                            data.key.toString(),

                                        shortName =
                                            data.child("short_name")
                                                .value
                                                .toString(),

                                        role =
                                            role,

                                        profileImage =
                                            data.child("profile_image")
                                                .value
                                                .toString()
                                    )

                                listKasir.add(model)
                            }
                        }

                        adapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(
                        error: DatabaseError
                    ) {

                    }
                }
            )
    }
}