package net.yan.kotlin.promoteradm.principal

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import net.yan.kotlin.promoteradm.R
import net.yan.kotlin.promoteradm.data.FirebaseHelper
import net.yan.kotlin.promoteradm.databinding.FragmentHomeBinding

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )
        val firebase = FirebaseHelper()
        val viewModelFactory = HomeViewModelFactory(firebase, binding, resources)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)

        viewModel.estaLogado.observe(viewLifecycleOwner, Observer {
            if (it == false) {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
            }
        })

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    } else {
                        activity?.finish()
                    }
                }
            })

        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        val adapter = AdapterHome(Clique {
            it.let {

            }
        })


        viewModel.listProm!!.observe(viewLifecycleOwner, Observer {
            it.let {
                adapter.adicionarLista(it)
                Log.i("Dados", "TAMANHO DA LISTA: "+it.size.toString())
            }

        })
        binding.recy.adapter = adapter


        val linear = binding.bottomDrawer
        bottomSheetBehavior = BottomSheetBehavior.from(linear)
        binding.bar.setNavigationOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
        binding.bar.setNavigationIcon(R.drawable.ic_menu_black_24dp)



        return binding.root
    }


}
