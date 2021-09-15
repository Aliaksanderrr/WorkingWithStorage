package rs.android.task4

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import rs.android.task4.data.Cat
import rs.android.task4.databinding.FragmentListCatItemBinding
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import rs.android.task4.databinding.FragmentListCatsBinding


class CatsListFragment : Fragment() {

    interface CatInfoListener{
        fun chosenCat(cat: Cat)
        fun callFilter()
    }

    private lateinit var listener: CatInfoListener
    private var _binding: FragmentListCatsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CatsListFragmentViewModel

    private var adapter: CatAdapter = CatAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (context is CatInfoListener){
            listener = context as CatInfoListener
        }

        _binding = FragmentListCatsBinding.inflate(inflater, container,false)
        viewModel = ViewModelProvider(this).get(CatsListFragmentViewModel::class.java)

        binding.catsRecycler.layoutManager = LinearLayoutManager(context)
        binding.catsRecycler.adapter = adapter

        val callback: ItemTouchHelper.Callback = SimpleItemTouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.catsRecycler)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Cats"
        binding.floatingActionButton.setOnClickListener {
            listener.chosenCat(Cat())
       }

    }

    override fun onStart() {
        super.onStart()
        viewModel.catsListLiveData.observe(viewLifecycleOwner, Observer { cats ->
            cats?.let {
                Log.d(TAG, "Got catLiveData ${cats.size}")
                adapter.submitList(cats)
        }})
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateCatsList()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_list_cats, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.filter -> {
                listener.callFilter()
                Toast.makeText( context, "push", Toast.LENGTH_SHORT).show()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private inner class CatAdapter : ListAdapter<Cat, CatHolder>(CatDiffUtilCallback()), ItemTouchHelperAdapter{

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatHolder {
            return CatHolder.from(parent, listener)
        }

        override fun onBindViewHolder(holder: CatHolder, position: Int) {
            val catItem = getItem(position)
            holder.bind(catItem)
        }

        override fun onItemDismiss(position: Int) {
            val catItem = getItem(position)
            Toast.makeText( context, "delete cat name:${catItem.name}", Toast.LENGTH_SHORT).show()
            viewModel.deleteCat(catItem)
        }

    }

    private class CatHolder(private val itemBinding: FragmentListCatItemBinding, private val listener: CatInfoListener) : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{

        private lateinit var cat: Cat

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(cat: Cat){
            this.cat = cat
            itemBinding.catName.text = cat.name
            itemBinding.catAge.text = cat.birthday.toString()
            itemBinding.catBreed.text = cat.breed
        }

        companion object {
            fun from(parent: ViewGroup, listener: CatInfoListener): CatHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FragmentListCatItemBinding.inflate(layoutInflater, parent, false)
                return CatHolder(binding, listener)
            }
        }

        override fun onClick(v: View?) {
            listener.chosenCat(cat)
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

    interface ItemTouchHelperAdapter {
        fun onItemDismiss(position: Int)
    }

    private class SimpleItemTouchHelperCallback(val mAdapter: ItemTouchHelperAdapter): ItemTouchHelper.Callback(){

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            return makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            TODO("Not yet implemented")
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            mAdapter.onItemDismiss(viewHolder.adapterPosition);
        }

        override fun isItemViewSwipeEnabled(): Boolean{ return true}
    }

    companion object {
        fun newInstance() = CatsListFragment()
    }

}