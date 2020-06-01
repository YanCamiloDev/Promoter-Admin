package net.yan.kotlin.promoteradm.principal

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import net.yan.kotlin.promoteradm.R
import net.yan.kotlin.promoteradm.data.FirebaseHelper
import net.yan.kotlin.promoteradm.databinding.FragmentHomeBinding
import net.yan.kotlin.promoteradm.model.PromPontos

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val firebase = FirebaseHelper()
        val viewModelFactory = HomeViewModelFactory(firebase, resources)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        viewModel.estaLogado.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
            }
        })
        binding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.progress.visibility = View.VISIBLE

        val adapter = AdapterHome(click = Clique { promPontos: PromPontos, i: Int ->
            like(i)
        })


        viewModel.listProm?.observe(viewLifecycleOwner, Observer {
            it.let {
                adapter.adicionarLista(it)
                binding.progress.visibility = View.GONE
            }
        })
        binding.recy.layoutManager = LinearLayoutManager(context)
        binding.recy.adapter = adapter

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)
        binding.recy.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Log.i("CLICOU", "PASSOU AQUI")
            }
        })



        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            })
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.demo_primary, menu)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun like(position: Int) {
        val im = binding.recy.get(position).findViewById<ImageView>(R.id.heart2)
        val dr = im
        val cc = dr.drawable
        dr.alpha = 0.7f
        val ccc = cc as AnimatedVectorDrawable

        ccc.start()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.exxx)
            viewModel.sair()
        return true
    }
}
