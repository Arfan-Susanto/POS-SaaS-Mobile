package com.possaas.FiturAdmin.GajiKasir

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.possaas.R
import de.hdodenhof.circleimageview.CircleImageView

class GajiKasirAdapter(

    private val list:
    ArrayList<GajiKasirModel>

) : RecyclerView.Adapter<
        GajiKasirAdapter.ViewHolder>() {

    class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val imgKasir:
                CircleImageView =
            itemView.findViewById(
                R.id.imgKasir
            )

        val txtNama:
                TextView =
            itemView.findViewById(
                R.id.txtNama
            )

        val txtRole:
                TextView =
            itemView.findViewById(
                R.id.txtRole
            )

        val btnGaji:
                LinearLayout =
            itemView.findViewById(
                R.id.btnGaji
            )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view =
            LayoutInflater.from(
                parent.context
            ).inflate(
                R.layout.item_gaji_kasir,
                parent,
                false
            )

        return ViewHolder(view)
    }

    override fun getItemCount():
            Int = list.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val item =
            list[position]

        holder.txtNama.text =
            item.shortName

        holder.txtRole.text =
            item.role

        Glide.with(holder.itemView.context)
            .load(item.profileImage)
            .placeholder(R.drawable.logo_bfc)
            .into(holder.imgKasir)

        holder.btnGaji.setOnClickListener {

        }
    }
}