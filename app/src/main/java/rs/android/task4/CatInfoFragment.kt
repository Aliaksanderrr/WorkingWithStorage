package rs.android.task4

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import rs.android.task4.data.Cat
import rs.android.task4.databinding.FragmentCatInfoBinding
import java.util.*

class CatInfoFragment: Fragment() {

    private lateinit var catInstance: Cat
    private var isAdd = true

    private var _binding: FragmentCatInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CatInfoFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatInfoBinding.inflate(inflater, container,false)
        viewModel = ViewModelProvider(this).get(CatInfoFragmentViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cat = arguments?.getSerializable(CAT_KEY) as Cat
        catInstance = cat
        if (cat.name != "none"){
            binding.catName.setText(cat.name)
            binding.catAge.setText(cat.birthday.toString())
            binding.catBreed.setText(cat.breed)
            binding.actionButton.text = context?.getString(R.string.update)
            isAdd = false

        }
        activity?.title = "Cat info"
//        deactivateButton()
        binding.actionButton.setOnClickListener {
            if (binding.catName.text.isNotEmpty() && binding.catAge.text.isNotEmpty() && binding.catBreed.text.isNotEmpty()){
                val newCat = Cat(catInstance.id, binding.catName.text.toString(),catInstance.birthday, binding.catBreed.text.toString())
                if (isAdd){
                    viewModel.addCat(newCat)
                    binding.actionButton.text = context?.getString(R.string.update)
                    isAdd = false
                } else {
                    viewModel.updateCat(newCat)
                }
            } else {
                Toast.makeText(context, "Fields are not filled",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

//    private fun activateButton(){
//        binding.actionButton.isEnabled = true
//    }
//
//    private fun deactivateButton(){
//        binding.actionButton.isActivated = false
//        binding.actionButton.isEnabled = false
//    }

    companion object{
        fun newInstance(cat: Cat): CatInfoFragment{
            val fragment = CatInfoFragment()

            fragment.arguments = Bundle().apply {
                putSerializable(CAT_KEY, cat)
            }
            return fragment
        }
        private const val CAT_KEY = "cat key"
    }
}