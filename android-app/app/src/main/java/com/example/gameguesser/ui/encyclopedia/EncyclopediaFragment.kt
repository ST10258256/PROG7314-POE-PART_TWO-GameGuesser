package com.example.gameguesser.ui.encyclopedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.gameguesser.R

class EncyclopediaFragment : Fragment() {

    private val viewModel: EncyclopediaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_encyclopedia, container, false)
        val tv = view.findViewById<TextView>(R.id.encyclopediaTextView)

        viewModel.entries.observe(viewLifecycleOwner, Observer { entries ->
            tv.text = entries.joinToString("\n\n") { it }
        })

        return view
    }
}
