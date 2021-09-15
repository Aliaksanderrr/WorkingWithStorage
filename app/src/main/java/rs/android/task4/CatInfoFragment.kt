package rs.android.task4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import rs.android.task4.data.Cat
import rs.android.task4.databinding.FragmentCatInfoBinding

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
            binding.catAge.setText(cat.birthday.toString(), TextView.BufferType.EDITABLE)
            binding.catBreed.setText(cat.breed)
            binding.actionButton.text = context?.getString(R.string.update)
            isAdd = false

        }
        activity?.title = "Cat info"
        binding.actionButton.setOnClickListener {
            if (binding.catName.text.isNotEmpty() && binding.catAge.text.isNotEmpty() && binding.catBreed.text.isNotEmpty()){
                val newCat = Cat(catInstance.id,
                                 binding.catName.text.toString(),
                                 Integer.parseInt(binding.catAge.text.toString()),
                                 binding.catBreed.text.toString())
                if (isAdd){
                    viewModel.addCat(newCat)
                    binding.actionButton.text = context?.getString(R.string.update)
                    Toast.makeText( context, "Added", Toast.LENGTH_SHORT).show()
                    isAdd = false
                } else {
                    viewModel.updateCat(newCat)
                    Toast.makeText( context, "Updated", Toast.LENGTH_SHORT).show()
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