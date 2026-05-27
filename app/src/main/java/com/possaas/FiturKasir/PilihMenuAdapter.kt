package com.possaas.FiturKasir

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.possaas.R
import java.text.NumberFormat
import java.util.Locale

class PilihMenuAdapter(

    private val listMenu: ArrayList<PilihMenuModel>

) : RecyclerView.Adapter<PilihMenuAdapter.ViewHolder>() {

    inner class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val imgMenu: ImageView =
            itemView.findViewById(R.id.imgMenu)

        val txtNamaMenu: TextView =
            itemView.findViewById(R.id.txtNamaMenu)

        val txtHarga: TextView =
            itemView.findViewById(R.id.txtHarga)

        val layoutKategori: LinearLayout =
            itemView.findViewById(R.id.layoutKategori)

        val txtJumlah: TextView =
            itemView.findViewById(R.id.txtJumlah)

        val btnPlus: LinearLayout =
            itemView.findViewById(R.id.btnPlus)

        val btnMinus: LinearLayout =
            itemView.findViewById(R.id.btnMinus)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_pilih_menu,
                    parent,
                    false
                )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listMenu.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val menu = listMenu[position]

        holder.txtNamaMenu.text =
            menu.nama

        val rupiah =
            NumberFormat.getInstance(
                Locale("id", "ID")
            )

        holder.txtHarga.text =
            "Rp ${rupiah.format(menu.harga)}"

        holder.txtJumlah.text =
            menu.jumlah.toString()

        Glide.with(holder.itemView.context)
            .load(menu.foto)
            .placeholder(R.drawable.logo_bfc)
            .into(holder.imgMenu)

        // CATEGORY
        holder.layoutKategori.removeAllViews()

        for (kategori in menu.kategori) {

            val txtKategori =
                TextView(holder.itemView.context)

            txtKategori.text = kategori

            txtKategori.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.accent
                )
            )

            txtKategori.textSize = 11f

            txtKategori.setPadding(
                22,
                10,
                22,
                10
            )

            txtKategori.setTypeface(
                null,
                android.graphics.Typeface.BOLD
            )

            txtKategori.background =
                ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.bg_kategori_primary
                )

            val params =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

            params.marginEnd = 10

            txtKategori.layoutParams =
                params

            holder.layoutKategori.addView(
                txtKategori
            )
        }

        // PLUS
        holder.btnPlus.setOnClickListener {

            if (menu.jumlah < menu.stok) {

                menu.jumlah++

                notifyItemChanged(position)
            }
        }

        // MINUS
        holder.btnMinus.setOnClickListener {

            if (menu.jumlah > 0) {

                menu.jumlah--

                notifyItemChanged(position)
            }
        }
    }
}