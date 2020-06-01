package net.yan.kotlin.promoteradm.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirebaseHelper {
    var auth: FirebaseAuth? = null
    var database: FirebaseFirestore? = null
    var storage: StorageReference? = null

    init {
        getFireAuth()
        getFireDataBase()
        getFireStorage()
    }

    fun getFireAuth(){
        if (auth == null){
            auth =  FirebaseAuth.getInstance()
        }
    }

    fun getFireDataBase(){
        if (database == null){
            database = FirebaseFirestore.getInstance()
        }
    }

    fun getFireStorage(){
        if (storage == null){
            storage = FirebaseStorage.getInstance().reference
        }
    }
}