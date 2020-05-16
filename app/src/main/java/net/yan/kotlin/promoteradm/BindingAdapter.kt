package net.yan.kotlin.promoteradm

import android.annotation.SuppressLint
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.card_home_rec.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.yan.kotlin.promoteradm.data.FirebaseHelper
import net.yan.kotlin.promoteradm.model.Cidade
import net.yan.kotlin.promoteradm.model.Cliente
import net.yan.kotlin.promoteradm.model.PromPontos
import net.yan.kotlin.promoteradm.model.Promoter


@BindingAdapter("cliente")
fun TextView.setNomes(promPontos: PromPontos) {
    val fir = FirebaseHelper()
    val coroutine = CoroutineScope(Dispatchers.IO)
    coroutine.launch {
        val ref = fir.database?.child("Clientes")?.child(promPontos.fk_id_pontos)
        ref?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(Cliente::class.java)
                text = user?.endereco
            }
        })
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("data")
fun TextView.setData(promPontos: PromPontos) {
    text = "Data de Publicação: " + promPontos.data.toString()
}


@BindingAdapter("cidade")
fun TextView.addNomeCidade(promPontos: PromPontos) {
    val fir = FirebaseHelper()
    val coroutine = CoroutineScope(Dispatchers.IO)
    coroutine.launch {
        val ref = fir.database?.child("Cidade")?.child(promPontos.fk_id_cidade)
        ref?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(Cidade::class.java)
                text = user?.local
            }
        })

    }
}

@BindingAdapter("promoter")
fun TextView.addNomeCliente(promPontos: PromPontos) {
    val fir = FirebaseHelper()
    val coroutine = CoroutineScope(Dispatchers.IO)
    coroutine.launch {
        val ref = fir.database?.child("Promoter")?.child(promPontos.fk_id_promoter)
        ref?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(Promoter::class.java)
                text = user?.nome
            }
        })

    }
}

@BindingAdapter("fotoLocal")
fun ImageView.addFoto(item: String?) {
    if (item != null) {
        Glide.with(context)
            .load(item)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    }
}

@BindingAdapter("fotoPerfil")
fun ImageView.addFotoPerfil(promPontos: PromPontos) {
    val fir = FirebaseHelper()
    var vari: Promoter
    Log.i("Dados 2", promPontos.fk_id_promoter)
    val coroutine = CoroutineScope(Dispatchers.IO)
    Log.i("Dados", "ID PROMOTOR: " + promPontos.fk_id_promoter)
    coroutine.launch {
        val ref = fir.database?.child("Promoter")?.child(promPontos.fk_id_promoter)
        ref?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (i in p0.children) {
                    val user = p0.getValue(Promoter::class.java)
                    vari = user!!
                    Glide.with(context)
                        .load(vari.foto)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(round_perfil)
                }

            }
        })
    }
}

