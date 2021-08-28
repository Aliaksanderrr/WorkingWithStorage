package rs.android.task4

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import rs.android.task4.data.Cat
import rs.android.task4.databinding.FragmentCatBinding
import rs.android.task4.databinding.FragmentListCatItemBinding


class CatFragment : Fragment() {

    private var _binding: FragmentCatBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CatFragmentViewModel

    private var adapter: CatAdapter = CatAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatBinding.inflate(inflater, container,false)
        viewModel = ViewModelProvider(this).get(CatFragmentViewModel::class.java)

        binding.catsRecycler.layoutManager = LinearLayoutManager(context)
        binding.catsRecycler.adapter = adapter

        return binding.root
    }

    var num = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener {
            //TODO create new fragment
            viewModel.addCat(Cat(name = "Tom${num++}", breed = "Sara${num++}"))
            Toast.makeText( context, "add cat", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.catsListLiveData.observe(viewLifecycleOwner, Observer { cats ->
            cats?.let {
                Log.d(TAG, "Got catLiveData ${cats.size}")
//                updateUI(cats)
                adapter.submitList(cats)
        }})
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

//    private fun updateUI(cats: List<Cat>){
//        adapter.let {
//            it.cats = cats
//        }
//        binding.catsRecycler.adapter = adapter
//    }

//    private inner class CatAdapter(var cats: List<Cat>) : RecyclerView.Adapter<CatHolder>(){
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatHolder {
//            val itemBinding = FragmentListCatItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
//            Log.d(TAG, "CatAdapter fun onCreateViewHolder(): ${cats.size}")
//            return CatHolder(itemBinding)
//        }
//
//        override fun onBindViewHolder(holder: CatHolder, position: Int) {
//            holder.bind(cats[position])
//        }
//
//        override fun getItemCount(): Int = cats.size
//
//    }

    private class CatAdapter : ListAdapter<Cat, CatHolder>(CatDiffUtilCallback()){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatHolder {
            return CatHolder.from(parent)
        }

        override fun onBindViewHolder(holder: CatHolder, position: Int) {
            val catItem = getItem(position)
            holder.bind(catItem)
        }


    }

    private class CatHolder(private val itemBinding: FragmentListCatItemBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(cat: Cat){
            itemBinding.catName.text = cat.name
            itemBinding.catAge.text = cat.birthday.toString()
            itemBinding.catBreed.text = cat.breed
        }

        companion object {
            fun from(parent: ViewGroup): CatHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FragmentListCatItemBinding.inflate(layoutInflater, parent, false)
                return CatHolder(binding)
            }
        }
    }

    private class CatDiffUtilCallback:  DiffUtil.ItemCallback<Cat>(){
        override fun areItemsTheSame(oldItem: Cat, newItem: Cat): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cat, newItem: Cat): Boolean {
            return oldItem == newItem
        }
    }


    companion object {
        fun newInstance() = CatFragment()
    }

}