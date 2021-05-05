package com.example.streamline

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*


private const val TAG = "TargetListFragment"

class TargetListFragment : Fragment() {

    /**
     * Required interface for hosting activities
     */
    interface Callbacks {
        fun onTargetSelected(targetId: UUID)
    }

    private var callbacks: Callbacks? = null

    private lateinit var targetRecyclerView: RecyclerView
    private var adapter: TargetAdapter? = TargetAdapter(emptyList())


    private val targetListViewModel: TargetListViewModel by lazy {
        ViewModelProviders.of(this).get(TargetListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        Log.d(TAG, "Total Targets: ${targetListViewModel.targets.size}")
//    }

    companion object {
        fun newInstance(): TargetListFragment {
            return TargetListFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_target_list, container, false)

        targetRecyclerView =
            view.findViewById(R.id.target_recycler_view) as RecyclerView
        targetRecyclerView.layoutManager = LinearLayoutManager(context)
        targetRecyclerView.adapter = adapter


//        updateUI()
        return view
    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        targetListViewModel.targetListLiveData.observe(
//            viewLifecycleOwner,
//            Observer { targets ->
//                targets?.let {
//                    Log.i(TAG, "Got targets ${targets.size}")
//                    updateUI(targets)
//                }
//            })
//    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        targetListViewModel.targetListLiveData.observe(
            viewLifecycleOwner,
            Observer { targets ->
                targets?.let {
                    Log.i(TAG, "Got targets ${targets.size}")
                    updateUI(targets)
                }
            })
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        Log.i("Clicked add", "clicked add")
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_target_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_target -> {
                val target = Target()
                targetListViewModel.addTarget(target)
                callbacks?.onTargetSelected(target.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI(targets: List<Target>) {
//        val targets = targetListViewModel.targets
        adapter = TargetAdapter(targets)
        targetRecyclerView.adapter = adapter
    }

    private inner class TargetHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var target: Target

        private val titleTextView: TextView = itemView.findViewById(R.id.target_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.target_date)
        private val contactedImageView: ImageView = itemView.findViewById(R.id.target_contacted)


        init {
            itemView.setOnClickListener(this)
        }

        fun bind(target: Target){
            this.target = target
            titleTextView.text = this.target.title
            dateTextView.text = this.target.date.toString()
            contactedImageView.visibility = if (target.isSolved) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        override fun onClick(v: View) {
            callbacks?.onTargetSelected(target.id)

        }
    }

    private inner class TargetAdapter(var targets: List<Target>)
        : RecyclerView.Adapter<TargetHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : TargetHolder {
            val view = layoutInflater.inflate(R.layout.list_item_target, parent, false)
            return TargetHolder(view)
        }



        override fun onBindViewHolder(holder: TargetHolder, position: Int) {
            val target = targets[position]
            holder.bind(target)
        }
        override fun getItemCount() = targets.size
    }
}