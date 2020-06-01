package net.yan.kotlin.promoteradm.principal


import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import net.yan.kotlin.promoteradm.data.FirebaseHelper
import net.yan.kotlin.promoteradm.model.PromPontos

class HomeViewModel(
    val dataSource: FirebaseHelper,
    val resources: Resources
) : ViewModel() {

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.IO + job)
    val estaLogado = MutableLiveData<Boolean>()
    val newPhoto = MutableLiveData<Boolean>()
    var listProm: MutableLiveData<Array<PromPontos>>? = null
    val listaOfPass = mutableListOf<PromPontos>()

    init {
        val user = dataSource.auth?.currentUser
        estaLogado.value = user == null
        if (estaLogado.value != true) {
            uiScope.launch {
                consultar()
            }
        }
    }

    suspend fun consultar() {
        try {
            listProm = MutableLiveData<Array<PromPontos>>()
            val ref = dataSource.database!!.collection("Promoter_Ponto")
                .orderBy("data", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(50).get().await()
            withContext(Dispatchers.Main) {
                ref.documents.forEach {
                    val promPontos = it.toObject(PromPontos::class.java)
                    promPontos!!.id = it.id
                    listaOfPass.add(promPontos)
                    listProm!!.value = listaOfPass.toTypedArray()
                    Log.i("DADOS", "PASSOU")
                }
            }
        } catch (e: Exception) {
            Log.i("ERRO", e.message.toString())
        }
    }

    fun sair() {
        FirebaseAuth.getInstance().signOut()
        estaLogado.value = true
    }

    fun like() {
        newPhoto.value = true
    }

    fun likeExit() {
        newPhoto.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}