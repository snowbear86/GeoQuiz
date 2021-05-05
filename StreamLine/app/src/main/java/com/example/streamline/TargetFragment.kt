package com.example.streamline

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import java.util.*
import androidx.lifecycle.Observer

private const val ARG_TARGET_ID = "target_id"
//val fragment = TargetFragment.newInstance(targetId)
private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0




class TargetFragment : Fragment(), DatePickerFragment.Callbacks {

    private lateinit var target: Target
    private lateinit var titleField: EditText
    private lateinit var detailField: EditText
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox
    private val targetDetailViewModel: TargetDetailViewModel by lazy {
        ViewModelProviders.of(this).get(TargetDetailViewModel::class.java)
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        target = Target()
        val targetId: UUID = arguments?.getSerializable(ARG_TARGET_ID) as UUID
        targetDetailViewModel.loadTarget(targetId)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_target, container, false)

        titleField = view.findViewById(R.id.target_title) as EditText
        detailField = view.findViewById(R.id.target_details) as EditText

        dateButton = view.findViewById(R.id.target_date) as Button
        solvedCheckBox = view.findViewById(R.id.target_solved) as CheckBox

//        dateButton.apply {
//            text = target.date.toString()
//            isEnabled = false
//        }



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        targetDetailViewModel.targetLiveData.observe(
            viewLifecycleOwner,
            Observer { target ->
                target?.let {
                    this.target = target
                    updateUI()
                }
            })
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {

            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This space intentionally left blank
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                target.title = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }
        val detailsWatcher = object : TextWatcher {

            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This space intentionally left blank
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                target.details = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }

        titleField.addTextChangedListener(titleWatcher)
        detailField.addTextChangedListener(detailsWatcher)

        solvedCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->
                target.isSolved = isChecked
            }
        }
        dateButton.setOnClickListener {
            DatePickerFragment.newInstance(target.date).apply {
                setTargetFragment(this@TargetFragment, REQUEST_DATE)

                show(this@TargetFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        targetDetailViewModel.saveTarget(target)
    }

    override fun onDateSelected(date: Date) {
        target.date = date
        updateUI()
    }

    private fun updateUI() {
        titleField.setText(target.title)
        dateButton.text = target.date.toString()
        detailField.setText(target.details)
        solvedCheckBox.apply {
            isChecked = target.isSolved
            jumpDrawablesToCurrentState()
        }    }

    companion object {

        fun newInstance(targetId: UUID): TargetFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TARGET_ID, targetId)
            }
            return TargetFragment().apply {
                arguments = args
            }
        }
    }
}

// maybe might have to copy and paste this ^^ to get the edit text working for details.