package net.yan.kotlin.promoteradm.model

import com.google.firebase.database.Exclude
import java.security.Timestamp
import java.util.*


class PromPontos{

    var id: String = ""
        @Exclude
        get() = field
    var fk_id_promoter: String = ""
        get() = field

    var fk_id_pontos: String = ""
        get() = field
    var fk_id_cidade: String = ""
        get() = field
    var data: String? = null
        get() = field
    var foto: String? = null
        get() = field
}