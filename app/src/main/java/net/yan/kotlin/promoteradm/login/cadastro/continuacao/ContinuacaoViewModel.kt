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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import net.yan.kotlin.promoteradm.data.FirebaseHelper
import net.yan.kotlin.promoteradm.databinding.FragmentContinuacaoBinding
import net.yan.kotlin.promoteradm.model.Promoter
import java.io.ByteArrayOutputStream
import java.util.*

class ContinuacaoViewModel(
    val dataSource: FirebaseHelper,
    val binding: FragmentContinuacaoBinding
) : ViewModel() {

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.IO + job)

    private val _isCadastrado = MutableLiveData<Boolean>()
    val isCadastrado: LiveData<Boolean>
        get() = _isCadastrado


    private val _error = MutableLiveData<Pair<Int, String>>()
    val error: LiveData<Pair<Int, String>>
        get() = _error

    fun cadastrar(email: String, senha: String, nome: String, foto: Bitmap) {

        try {
            val res = dataSource.auth?.createUserWithEmailAndPassword(email, senha)
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        gravarFoto(nome, foto, email)

                    } else {
                        try {
                            throw it.exception!!
                        } catch (er: FirebaseAuthWeakPasswordException) {
                            _error.value = Pair(3, "Senha Inválida")
                        } catch (er: FirebaseAuthInvalidCredentialsException) {
                            _error.value = Pair(3, "Email inválido")
                        } catch (er: FirebaseAuthUserCollisionException) {
                            _error.value = Pair(3, "Essa conta já foi cadastrada")
                        }
                    }
                }
        } catch (e: Exception) {
            Log.i("EXCEPTION", e.message.toString())
        }

    }


    fun gravarFoto(nome: String, imageBitmap: Bitmap, email: String) {
        try {
            val usuario: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
            Log.i("USUARIO", usuario.uid)
            val nomeImagem = UUID.randomUUID().toString()
            val storageRef =
                FirebaseStorage.getInstance().reference.child("imagens").child("perfil").child(
                    "${usuario.uid}.jpeg"
                )
            var metadata = StorageMetadata.Builder()
                .setContentType("image/jpg")
                .build()

            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
            val data = baos.toByteArray()

            var uploadTask = storageRef.putBytes(data, metadata)
            uploadTask.addOnFailureListener {
                _error.value = Pair(3, "ERRO AO SALVAR FOTO")
                Log.i("FALHOU", it.message)
            }.addOnSuccessListener {
                val promoter = Promoter()
                val user: FirebaseUser = dataSource.auth!!.currentUser!!
                promoter.id = user.uid
                promoter.email = email
                promoter.nome = nome
                val dow = storageRef.downloadUrl
                dow.addOnSuccessListener {
                    promoter.foto = it.toString()
                    salvarNoBanco(promoter)
                }
            }
        } catch (e: FirebaseException) {
            _error.value = Pair(3, "ERRO AO SALVAR FOTO")
            Log.i("Exception", "UPLOAD FOTO " + e.message)
        }
    }

    fun salvarNoBanco(promoter: Promoter){
        try {
            val ref = dataSource.database!!.child("Promoter").child(promoter.id).setValue(promoter)
            ref.addOnCompleteListener {
                if (it.isSuccessful){
                    _isCadastrado.value = true
                }
            }
        }catch (e: FirebaseException){
            _error.value = Pair(3, "ERRO AO SALVAR NO BANCO")
            Log.i("Erro", "SALVAR NO BANCO")
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}