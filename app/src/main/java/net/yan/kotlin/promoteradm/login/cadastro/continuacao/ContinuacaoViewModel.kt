package net.yan.kotlin.promoteradm.login.cadastro.continuacao

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import net.yan.kotlin.promoteradm.data.FirebaseHelper
import net.yan.kotlin.promoteradm.databinding.FragmentContinuacaoBinding
import net.yan.kotlin.promoteradm.model.Promoter
import java.io.ByteArrayOutputStream


class ContinuacaoViewModel(
    val dataSource: FirebaseHelper,
    val binding: FragmentContinuacaoBinding
) : ViewModel() {

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.IO + job)

    private val _isCadastrado = MutableLiveData<Boolean>()
    val isCadastrado: LiveData<Boolean>
        get() = _isCadastrado

    private val _load = MutableLiveData<Boolean>()
    val load: LiveData<Boolean>
        get() = _load


    private val _error = MutableLiveData<Pair<Int, String>>()
    val error: LiveData<Pair<Int, String>>
        get() = _error

    init {
        _load.value = false
    }

    fun cadastrar(email: String, nome: String, senha: String, foto: Bitmap) {
        _load.value = true
        Log.i("PROGRESS", "TRUE")
        uiScope.launch {
            try {
                val valor = dataSource.auth!!.createUserWithEmailAndPassword(email, senha)
                    .await()
                gravarFoto(nome, foto, email)
            } catch (er: FirebaseAuthWeakPasswordException) {
                withContext(Dispatchers.Main) {
                    _error.value = Pair(3, "Senha Inválida")
                }
            } catch (er: FirebaseAuthInvalidCredentialsException) {
                withContext(Dispatchers.Main) {
                    _error.value = Pair(3, "Email inválido")
                }
            } catch (er: FirebaseAuthUserCollisionException) {
                withContext(Dispatchers.Main) {
                    _error.value = Pair(3, "Essa conta já foi cadastrada")
                }
            } catch (e: Exception) {
                Log.i("STATUS", e.message)
            }
        }

    }

    suspend fun gravarFoto(nome: String, imageBitmap: Bitmap, email: String) {
        try {
            val usuario: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
            val storageRef =
                FirebaseStorage.getInstance().reference.child("imagens").child("perfil").child(
                    "${usuario.uid}.jpeg"
                )
            val metadata = StorageMetadata.Builder()
                .setContentType("image/jpg")
                .build()
            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
            val data = baos.toByteArray()
            storageRef.putBytes(data, metadata).await()
            val promoter = Promoter()
            val user: FirebaseUser = dataSource.auth!!.currentUser!!
            promoter.id = user.uid
            promoter.email = email
            promoter.nome = nome
            val dow = storageRef.downloadUrl.await()
            promoter.foto = dow.toString()
            salvarNoBanco(promoter)
        } catch (e: FirebaseException) {

        }
    }

    suspend fun salvarNoBanco(promoter: Promoter) {
        try {
            dataSource.database!!.collection("Promoter").add(promoter).await()
            withContext(Dispatchers.Main) {
                _isCadastrado.value = true
            }
        } catch (e: FirebaseException) {
            _error.value = Pair(3, "ERRO AO SALVAR NO BANCO")
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
        _load.value == false

    }
}