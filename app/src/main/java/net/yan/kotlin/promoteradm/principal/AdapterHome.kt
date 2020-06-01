package net.yan.kotlin.promoteradm.principal


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.yan.kotlin.promoteradm.databinding.CardHomeRecBinding
import net.yan.kotlin.promoteradm.model.PromPontos

class AdapterHome(val click: Clique) :
    ListAdapter<Data, RecyclerView.ViewHolder>(ClienteCallBack()){

    val uiScope = CoroutineScope(Dispatchers.Default)
    private var lista: List<Data.DataItem>? = null


    fun adicionarLista(array: Array<PromPontos>?) {
        uiScope.launch {
            lista = array?.map { Data.DataItem(it) }
            withContext(Dispatchers.Main) {
                submitList(lista)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MyViewHolder -> {
                val nightItem = getItem(position) as Data.DataItem
                holder.bind(click, nightItem.promotor, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> MyViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Data.DataItem -> 1
            else -> null
        }!!
    }

    class MyViewHolder private constructor(val binding: CardHomeRecBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(
            click: Clique,
            pp: PromPontos,
            view: Int
        ) {
            binding.promPontos = pp
            binding.click = click
            binding.view = view
            binding.executePendingBindings()
        }


        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = CardHomeRecBinding.inflate(inflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }


}

class ClienteCallBack : DiffUtil.ItemCallback<Data>() {
    override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
        return oldItem.promotor.id == newItem.promotor.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
        return oldItem == newItem
    }

}

class Clique(val clickListener: (promPontos: PromPontos, view: Int) -> Unit) {
    fun onClick(nome: PromPontos, view: Int) = clickListener(nome, view)
}

sealed class Data {
    data class DataItem(val name: PromPontos) : Data() {
        override val promotor = name
    }

    abstract val promotor: PromPontos
}
