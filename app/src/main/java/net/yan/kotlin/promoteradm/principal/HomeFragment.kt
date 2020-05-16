package net.yan.kotlin.promoteradm.principal

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import net.yan.kotlin.promoteradm.R
import net.yan.kotlin.promoteradm.data.FirebaseHelper
import net.yan.kotlin.promoteradm.databinding.FragmentHomeBinding

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

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
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )


        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            })

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = AdapterHome(Clique {
            it.let {

            }
        })

        viewModel.estaLogado.observe(viewLifecycleOwner, Observer {
            if (it == false) {
                viewModel.listProm!!.observe(viewLifecycleOwner, Observer {
                    it.let {
                        adapter.adicionarLista(it)
                        binding.recy.adapter = adapter
                    }
                })
            }
        })
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.demo_primary, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.exxx)
            viewModel.sair()
        return true
    }
}
