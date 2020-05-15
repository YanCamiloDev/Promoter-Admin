package net.yan.kotlin.promoteradm.login.cadastro.continuacao


import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import net.yan.kotlin.promoteradm.R
import net.yan.kotlin.promoteradm.data.FirebaseHelper
import net.yan.kotlin.promoteradm.databinding.FragmentContinuacaoBinding


class ContinuacaoFragment : Fragment() {

    private lateinit var binding: FragmentContinuacaoBinding
    private lateinit var firebase: FirebaseHelper
    private lateinit var viewModel: ContinuacaoViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_continuacao, container, false)
        firebase = FirebaseHelper()
        val viewModelFactory = ContinuacaoViewModelFactory(
            firebase, binding
        )

        val argumentos by navArgs<ContinuacaoFragmentArgs>()
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ContinuacaoViewModel::class.java)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        binding.email = argumentos.email
        binding.senha = argumentos.senha

        binding.circleImageViewFotoPerfil.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                    startActivityForResult(takePictureIntent, 100)
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner, Observer {
            if (it.first == 3){
                Snackbar.make(binding.root, it.second, Snackbar.LENGTH_LONG). show()
            }
        })

        viewModel.isCadastrado.observe(viewLifecycleOwner, Observer {
            if (it == true){
                findNavController().navigate(ContinuacaoFragmentDirections.actionContinuacaoFragmentToLoginFragment())
            }
        })

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(
            R.menu.menu
            , menu
        )

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_menu) {
            findNavController().popBackStack()
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == -1) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.circleImageViewFotoPerfil.setImageBitmap(imageBitmap)
            binding.bitMap = imageBitmap
            Log.i("BitMap", binding.bitMap.toString())
            binding.nome = binding.log.text.toString()
        }

    }
}