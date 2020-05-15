package net.yan.kotlin.promoteradm.login


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.yan.kotlin.promoteradm.data.FirebaseHelper
import net.yan.kotlin.promoteradm.databinding.FragmentLoginBinding
import java.lang.Exception

class LoginViewModel(
    val dataSource: FirebaseHelper,
    val binding: FragmentLoginBinding
) : ViewModel() {

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Default + job)
    var error = MutableLiveData<String>()
    var isLogado = MutableLiveData<Boolean>()
    var cadastrar = MutableLiveData<Boolean>()

    init {
        cadastrar.value = false
    }

    fun logar(){
        uiScope.launch {
            try {
                val res = dataSource.auth?.signInWithEmailAndPassword(binding.email.text.toString(),  binding.senhs.text.toString())
                    ?.addOnCompleteListener {
                        if (it.isSuccessful){
                            isLogado.value = true
                        }else{
                            try {
                                throw it.exception!!
                            }catch (er: FirebaseAuthWeakPasswordException){
                                error.value = "Senha fraca"
                            }catch (er: FirebaseAuthInvalidCredentialsException){
                                error.value = "Email inválido"
                            }catch (er: FirebaseAuthUserCollisionException){
                                error.value = "Essa conta já foi cadastrada"
                            }
                        }
                    }
            }catch (e: Exception){
                Log.i("EXCEPTION", e.message.toString())
            }
        }
    }

    fun cadastrar(){
        cadastrar.value = true
    }
    fun mudouTela(){
        cadastrar.value = false
    }
    fun mudouTelaHome(){
        isLogado.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}