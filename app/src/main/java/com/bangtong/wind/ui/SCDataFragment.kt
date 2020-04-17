package com.bangtong.wind.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

import com.bangtong.wind.R
import com.bangtong.wind.api.NetworkControl
import com.bangtong.wind.model.BoxIfo
import com.bangtong.wind.util.TimeControl
import com.bangtong.wind.view.DebugViewModel
import kotlinx.android.synthetic.main.fragment_s_c_data.*

/**
 * A simple [Fragment] subclass.
 */
class SCDataFragment : Fragment() {

    private val viewModel by activityViewModels<DebugViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_s_c_data, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        submit.setOnClickListener{
            if (boxId.text.isNotEmpty() && x.text.isNotEmpty() && y.text.isNotEmpty() && temperature.text.isNotEmpty()){
                viewModel.insertBoxIfoCloud(
                    BoxIfo(1,boxId.text.toString().toLong(), TimeControl.getTimeToLong(),
                        x.text.toString().toDouble(),y.text.toString().toDouble(),temperature.text.toString().toInt())
                )
            }
        }
        progressBar.visibility = View.GONE
        NetworkControl.callBackComplete.observe(requireActivity(), Observer {
            when(it){
                "Show progress bar" -> {
                    progressBar.visibility = View.VISIBLE
                }
                else -> {
                    progressBar.visibility = View.GONE
                }
            }
        })
    }
}
