package net.yan.kotlin.promoteradm.principal


import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.*
import net.yan.kotlin.promoteradm.data.FirebaseHelper
import net.yan.kotlin.promoteradm.databinding.FragmentHomeBinding
import net.yan.kotlin.promoteradm.model.PromPontos

class HomeViewModel(
    val dataSource: FirebaseHelper,
    val binding: FragmentHomeBinding,
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

        val ref = dataSource.database?.child("Promoter_Ponto")

        uiScope.launch {
            if (listProm == null) {
                listProm = MutableLiveData<Array<PromPontos>>()
                ref!!.addChildEventListener(object : ChildEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onChildMoved(p0: DataSnapshot, p1: String?) {

                    }

                    override fun onChildChanged(p0: DataSnapshot, p1: String?) {

                    }

                    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                        val promPontos = p0.getValue(PromPontos::class.java)
                        promPontos?.id = p0.key.toString()
                        listaOfPass.add(0, promPontos!!)
                        listProm!!.value = listaOfPass.toTypedArray()
                    }

                    override fun onChildRemoved(p0: DataSnapshot) {

                    }

                })
            }


            withContext(Dispatchers.Main) {
                estaLogado.value = user != null
            }
        }
    }

    fun sair() {
        uiScope.launch {
            FirebaseAuth.getInstance().signOut()
            withContext(Dispatchers.Main) {
                estaLogado.value = false
            }
        }
    }

    fun addLocalAndVenda() {
        newPhoto.value = true
    }

    fun addLocalAndVendaClose() {
        newPhoto.value = false
    }

    fun novaTela() {
        estaLogado.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}