package com.possaas.Kasir

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.possaas.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeKasirFragment : Fragment() {

    private lateinit var txtWelcome: TextView
    private lateinit var txtRole: TextView
    private lateinit var txtDate: TextView
    private lateinit var txtHour: TextView
    private lateinit var txtMinute: TextView
    private lateinit var txtSecond: TextView

    private val handler =
        Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view =
            inflater.inflate(
                R.layout.fragment_home_kasir,
                container,
                false
            )

        txtWelcome =
            view.findViewById(R.id.txtWelcome)

        txtRole =
            view.findViewById(R.id.txtRole)

        txtDate =
            view.findViewById(R.id.txtDate)

        txtHour =
            view.findViewById(R.id.txtHour)

        txtMinute =
            view.findViewById(R.id.txtMinute)

        txtSecond =
            view.findViewById(R.id.txtSecond)

        loadUserData()

        startRealtimeClock()

        return view
    }

    private fun loadUserData() {

        val uid =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        FirebaseDatabase
            .getInstance()
            .getReference("users")
            .child(uid)
            .get()
            .addOnSuccessListener {

                val shortName =
                    it.child("short_name")
                        .value
                        .toString()

                val role =
                    it.child("role")
                        .value
                        .toString()

                txtWelcome.text =
                    "Selamat datang, $shortName"

                txtRole.text = role
            }
    }

    private fun startRealtimeClock() {

        handler.post(object : Runnable {

            override fun run() {

                val date =
                    SimpleDateFormat(
                        "dd MMMM yyyy",
                        Locale("id", "ID")
                    ).format(Date())

                txtDate.text = date

                txtHour.text =
                    SimpleDateFormat(
                        "HH",
                        Locale.getDefault()
                    ).format(Date())

                txtMinute.text =
                    SimpleDateFormat(
                        "mm",
                        Locale.getDefault()
                    ).format(Date())

                txtSecond.text =
                    SimpleDateFormat(
                        "ss",
                        Locale.getDefault()
                    ).format(Date())

                handler.postDelayed(
                    this,
                    1000
                )
            }
        })
    }
}