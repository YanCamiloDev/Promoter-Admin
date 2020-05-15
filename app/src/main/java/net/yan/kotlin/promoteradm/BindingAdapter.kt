package net.yan.kotlin.promoteradm

import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.chip.ChipGroup
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.yan.kotlin.promoteradm.data.FirebaseHelper
import net.yan.kotlin.promoteradm.model.PromPontos
import net.yan.kotlin.promoteradm.model.Promoter

@BindingAdapter("textFormater")
fun chip(chipGroup: ChipGroup, array: List<String>) {
    for (text in array) {

    }
}

@BindingAdapter("itens")
fun TextView.setNomes(item: String?) {
    item?.let {
        text = item
    }
}

@BindingAdapter("nomeUser")
fun TextView.addNomeUser(item: String?) {
    item?.let {
        text = item
    }
}

@BindingAdapter("cidade")
fun TextView.addNomeCidade(item: String?) {
    item?.let {
        text = item
    }
}

@BindingAdapter("cliente")
fun TextView.addNomeCliente(item: String?) {
    item?.let {
        text = item
    }
}

@BindingAdapter("fotoLocal")
fun ImageView.addFoto(item: String?) {
    if (item != null) {
        Glide.with(context)
            .load(item)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this);
    }
}
/*
@BindingAdapter("fotoPerfil")
fun ImageView.addFotoPerfil(promPontos: PromPontos) {
    val fir = FirebaseHelper()
    val coroutine = CoroutineScope(Dispatchers.IO)
    coroutine.launch {
        val ref = fir.database?.child("Promoter")?.child(promPontos.fk_id_promoter)
        ref?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(Promoter::class.java)
                Glide.with(context)
                    .load(user?.foto)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(view);
            }
        })
    }


}


 */