package net.yan.kotlin.promoteradm.data

import android.util.Base64

object Base64Custom {
    fun codificarBase64(texto: String?):String {
        return Base64.encodeToString(texto!!.toByteArray(), Base64.DEFAULT).replace("(\\n|\\r)", "")
    }
    fun decodificarBase64(textoCodificado:String):String {
        return String(Base64.decode(textoCodificado, Base64.DEFAULT))
    }
}