package net.yan.kotlin.promoteradm.principal

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.yan.kotlin.promoteradm.data.FirebaseHelper


class HomeViewModelFactory(
    private val dataSource: FirebaseHelper,
    private val resources: Resources
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(dataSource, resources) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
