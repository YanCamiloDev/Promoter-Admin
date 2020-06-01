package net.yan.kotlin.promoteradm.model

import com.google.firebase.database.Exclude

class Cliente {

    var id: String = ""
        @Exclude
        get
    var endereco: String = ""


}