package net.yan.kotlin.promoteradm.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirebaseHelper() {
    var auth: FirebaseAuth? = null
    var database: DatabaseReference? = null
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
            database = FirebaseDatabase.getInstance().reference
        }
    }

    fun getFireStorage(){
        if (storage == null){
            storage = FirebaseStorage.getInstance().reference
        }
    }
}