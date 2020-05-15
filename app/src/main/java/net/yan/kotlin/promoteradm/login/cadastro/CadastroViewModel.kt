package net.yan.kotlin.promoteradm.login.cadastro


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import net.yan.kotlin.promoteradm.data.FirebaseHelper
import net.yan.kotlin.promoteradm.databinding.FragmentCadastroBinding
import net.yan.kotlin.promoteradm.model.Promoter

class CadastroViewModel(val dataSource: FirebaseHelper, val binding: FragmentCadastroBinding) :
    ViewModel() {


    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.IO + job)
    var promo = MutableLiveData<Promoter>()
    var isLogado = MutableLiveData<Boolean>()
    var error = MutableLiveData<Pair<Int, String>>()


    init {
        isLogado.value = false
    }

    fun cadastrar() {
        validaEmailESenha(binding.loginCad.text.toString(), binding.senhaCad.text.toString())
    }

    fun validaEmailESenha(email: String, senha: String){
        if (email != "" && email != null) {
            if (senha!= "" && senha != null) {
                if ("@" in email && "." in email) {
                    if (senha.length > 5) {
                        isLogado.value = true
                    }else{
                        error.value = Pair(2, "Digite uma senha mais forte")
                    }
                }else{
                    error.value = Pair(1, "Digite um email Válido")
                }
            }else{
                error.value = Pair(2, "Digite a Senha")
            }
        }else{
            error.value = Pair(1, "Digite o login")
        }
    }

    fun mudouTela() {
        isLogado.value = false
    }

    fun selecionarFoto() {

    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}