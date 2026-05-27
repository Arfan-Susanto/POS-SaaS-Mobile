package com.possaas.FiturKasir

import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.possaas.R

class PilihMenuActivity : AppCompatActivity() {

    private lateinit var recyclerMenu:
            RecyclerView

    private lateinit var menuAdapter:
            PilihMenuAdapter

    private val menuList =
        ArrayList<PilihMenuModel>()

    private val allMenuList =
        ArrayList<PilihMenuModel>()

    private var selectedCategories =
        ArrayList<String>()

    private var currentSort =
        "default"

    private var currentSearch =
        ""

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_pilih_menu
        )

        recyclerMenu =
            findViewById(R.id.recyclerMenu)

        val edtSearch =
            findViewById<EditText>(
                R.id.edtSearch
            )

        val btnFilter =
            findViewById<LinearLayout>(
                R.id.btnFilter
            )

        val txtFilter =
            findViewById<TextView>(
                R.id.txtFilter
            )

        menuAdapter =
            PilihMenuAdapter(menuList)

        recyclerMenu.layoutManager =
            GridLayoutManager(this, 2)

        recyclerMenu.adapter =
            menuAdapter

        setupCategory(
            R.id.categoryMinuman,
            "Minuman"
        )

        setupCategory(
            R.id.categoryMakanan,
            "Makanan"
        )

        setupCategory(
            R.id.categoryAlacarte,
            "Ala carte"
        )

        setupCategory(
            R.id.categorySaus,
            "Saus"
        )

        setupCategory(
            R.id.categoryPaket,
            "Paket"
        )

        // SEARCH
        edtSearch.addTextChangedListener(

            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                    currentSearch =
                        s.toString()

                    applyFilter()
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {}
            }
        )

        // FILTER
        btnFilter.setOnClickListener {

            val popup =
                PopupMenu(
                    this,
                    btnFilter,
                    0,
                    0,
                    R.style.CustomPopupMenu
                )

            popup.menu.add(
                styleMenu("Default")
            )

            popup.menu.add(
                styleMenu(
                    "Harga Tertinggi"
                )
            )

            popup.menu.add(
                styleMenu(
                    "Harga Terendah"
                )
            )

            popup.setOnMenuItemClickListener {

                when (it.title.toString()) {

                    "Harga Tertinggi" -> {

                        currentSort =
                            "highest"

                        txtFilter.text =
                            "Harga Tertinggi"
                    }

                    "Harga Terendah" -> {

                        currentSort =
                            "lowest"

                        txtFilter.text =
                            "Harga Terendah"
                    }

                    else -> {

                        currentSort =
                            "default"

                        txtFilter.text =
                            "Default"
                    }
                }

                applyFilter()

                true
            }

            popup.show()
        }

        loadMenu()
    }

    // LOAD MENU
    private fun loadMenu() {

        FirebaseDatabase
            .getInstance()
            .getReference("menu")
            .addListenerForSingleValueEvent(

                object : ValueEventListener {

                    override fun onDataChange(
                        snapshot: DataSnapshot
                    ) {

                        allMenuList.clear()

                        for (data in snapshot.children) {

                            val id =
                                data.key.toString()

                            FirebaseDatabase
                                .getInstance()
                                .getReference("stok")
                                .child(id)
                                .addListenerForSingleValueEvent(

                                    object : ValueEventListener {

                                        override fun onDataChange(
                                            stokSnapshot: DataSnapshot
                                        ) {

                                            val stok =
                                                stokSnapshot
                                                    .child("jumlah")
                                                    .getValue(Int::class.java)
                                                    ?: 0

                                            val model =
                                                PilihMenuModel(

                                                    id = id,

                                                    nama =
                                                        data.child("nama")
                                                            .value
                                                            .toString(),

                                                    harga =
                                                        data.child("harga")
                                                            .getValue(Long::class.java)
                                                            ?: 0L,

                                                    foto =
                                                        data.child("foto")
                                                            .value
                                                            .toString(),

                                                    kategori =
                                                        data.child("kategori")
                                                            .children
                                                            .map {
                                                                it.value.toString()
                                                            }
                                                            .toMutableList(),

                                                    stok = stok
                                                )

                                            allMenuList.add(model)

                                            applyFilter()
                                        }

                                        override fun onCancelled(
                                            error: DatabaseError
                                        ) {}
                                    }
                                )
                        }
                    }

                    override fun onCancelled(
                        error: DatabaseError
                    ) {}
                }
            )
    }

    // FILTER SEARCH SORT
    private fun applyFilter() {

        menuList.clear()

        var filtered =

            if (
                selectedCategories.isEmpty()
            ) {

                allMenuList

            } else {

                allMenuList.filter { menu ->

                    selectedCategories.all {
                        menu.kategori.contains(it)
                    }
                }
            }

        // SEARCH
        if (currentSearch.isNotEmpty()) {

            filtered =
                filtered.filter {

                    it.nama.contains(
                        currentSearch,
                        true
                    )
                }
        }

        // SORT
        filtered = when (currentSort) {

            "highest" -> {

                filtered.sortedByDescending {
                    it.harga
                }
            }

            "lowest" -> {

                filtered.sortedBy {
                    it.harga
                }
            }

            else -> {

                filtered.reversed()
            }
        }

        menuList.addAll(filtered)

        menuAdapter.notifyDataSetChanged()
    }

    private fun setupCategory(
        id: Int,
        value: String
    ) {

        val textView =
            findViewById<TextView>(id)

        updateCategoryUI(
            textView,
            false
        )

        textView.setOnClickListener {

            if (
                selectedCategories.contains(value)
            ) {

                selectedCategories.remove(value)

                updateCategoryUI(
                    textView,
                    false
                )

            } else {

                selectedCategories.add(value)

                updateCategoryUI(
                    textView,
                    true
                )
            }

            applyFilter()
        }
    }

    private fun updateCategoryUI(
        textView: TextView,
        selected: Boolean
    ) {

        if (selected) {

            textView.setBackgroundResource(
                R.drawable.bg_category_selected_kasir
            )

            textView.setTextColor(
                getColor(R.color.accent)
            )

        } else {

            textView.setBackgroundResource(
                R.drawable.bg_category_unselected_kasir
            )

            textView.setTextColor(
                getColor(R.color.primary)
            )
        }
    }

    private fun styleMenu(
        title: String
    ): SpannableString {

        val span =
            SpannableString(title)

        span.setSpan(

            ForegroundColorSpan(
                getColor(R.color.primary)
            ),

            0,

            span.length,

            0
        )

        span.setSpan(

            StyleSpan(Typeface.NORMAL),

            0,

            span.length,

            0
        )

        return span
    }
}