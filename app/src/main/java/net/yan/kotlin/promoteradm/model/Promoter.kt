package net.yan.kotlin.promoteradm.model


import com.google.firebase.database.Exclude

class Promoter {

    var id: String = ""
        @Exclude
        get
    var email: String = ""

    var senha: String = ""
        @Exclude
        get

    var foto: String = ""

    var nome: String? = ""


}