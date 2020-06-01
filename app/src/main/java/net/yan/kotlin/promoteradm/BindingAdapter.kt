package net.yan.kotlin.promoteradm

import android.annotation.SuppressLint
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.card_home_rec.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import net.yan.kotlin.promoteradm.data.FirebaseHelper
import net.yan.kotlin.promoteradm.model.Cidade
import net.yan.kotlin.promoteradm.model.Cliente
import net.yan.kotlin.promoteradm.model.PromPontos
import net.yan.kotlin.promoteradm.model.Promoter
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("cliente")
fun TextView.setNomes(promPontos: PromPontos) {
    val fire = FirebaseHelper()
    val coroutineScope = CoroutineScope(Dispatchers.IO)
    coroutineScope.launch {
        try {
            val ref =
                fire.database!!.collection("Clientes").document(promPontos.fk_id_pontos).get()
                    .await()
            val cliente = ref.toObject(Cliente::class.java)
            withContext(Dispatchers.Main) {
                text = cliente?.endereco
            }
        } catch (e: Exception) {
            Log.i("ERRO", "MÉTODO: SETNOMES " + e.message)
        }
    }
}

@SuppressLint("SetTextI18n", "SimpleDateFormat")
@BindingAdapter("data")
fun TextView.setData(promPontos: PromPontos) {
    val date = Date(promPontos.data!!)
    val format = SimpleDateFormat("dd/MM/yyyy")
    text = "Data de Publicação: " + format.format(date)
}


@BindingAdapter("cidade")
fun TextView.addNomeCidade(promPontos: PromPontos) {
    val fir = FirebaseHelper()
    val coroutine = CoroutineScope(Dispatchers.IO)
    coroutine.launch {
        try {
            val ref =
                fir.database!!.collection("Cidade").document(promPontos.fk_id_cidade).get().await()
            val user = ref.toObject(Cidade::class.java)
            withContext(Dispatchers.Main) {
                text = user?.local
            }
        } catch (e: Exception) {
            Log.i("ERRO", "MÉTODO: addNomeCidade " + e.message)
        }
    }
}

@BindingAdapter("promoter")
fun TextView.addNomeCliente(promPontos: PromPontos) {
    val fir = FirebaseHelper()
    val coroutine = CoroutineScope(Dispatchers.IO)
    coroutine.launch {
        try {
            val ref =
                fir.database!!.collection("Promoter").document(promPontos.fk_id_promoter).get()
                    .await()
            val user = ref.toObject(Promoter::class.java)
            Log.i("ERRO", user?.foto)
            withContext(Dispatchers.Main) {
                text = user?.nome
            }
        } catch (e: Exception) {
            Log.i("ERRO", "MÉTODO: addNomeCliente " + e.message)
        }
    }
}

@BindingAdapter("fotoLocal")
fun ImageView.addFoto(item: String?) {
    if (item != null) {
        CoroutineScope(Dispatchers.IO).launch {
            val glide = Glide.with(context)
                .load(item)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
            withContext(Dispatchers.Main) {
                glide.into(fotoLocal)
            }
        }
    }
}

/*
   if ()
   val dr = heart2
   val cc = dr.drawable
   dr.alpha = 0.7f
   val ccc = cc as AnimatedVectorDrawable
   ccc.start()
 */


@BindingAdapter("fotoPerfil")
fun ImageView.addFotoPerfil(promPontos: PromPontos) {
    val fir = FirebaseHelper()
    val coroutine = CoroutineScope(Dispatchers.IO)
    coroutine.launch {
        try {
            val ref =
                fir.database!!.collection("Promoter").document(promPontos.fk_id_promoter).get()
                    .await()
            val promoter = ref.toObject(Promoter::class.java)
            Log.i("ERRO", promoter!!.foto)
            val glide = Glide.with(context)
                .load(promoter.foto)
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
            withContext(Dispatchers.Main) {
                glide.into(round_perfil)
            }
        } catch (e: Exception) {
            Log.i("ERRO", "MÉTODO: addFotoPerfil " + e.message)
        }
    }
}

