package net.yan.kotlin.promoteradm.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Cidade(
             var id: String="",
             val local: String="") {
}