package com.bangtong.wind.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

import com.bangtong.wind.R
import com.bangtong.wind.api.NetworkControl
import com.bangtong.wind.view.DebugViewModel
import kotlinx.android.synthetic.main.fragment_s_c_lock.*

/**
 * A simple [Fragment] subclass.
 */
class SCLockFragment : Fragment() {

    private val viewModel by activityViewModels<DebugViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_s_c_lock, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lockText.text = "0"
        unlockText.text = "0"
        lockStatusText.text = "Unlocked"
        viewModel.lockCount.observe(requireActivity(), Observer {
            lockText.text = viewModel.lockCount.value.toString()
        })
        viewModel.unlockCount.observe(requireActivity(), Observer {
            unlockText.text = viewModel.unlockCount.value.toString()
        })
        viewModel.lockStatus.observe(requireActivity(), Observer {
            lockStatusText.text = viewModel.lockStatus.value
        })
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
        lockButton.setOnClickListener{
            if(boxId.text.isNotEmpty()){
                if (viewModel.lockClickable){
                    viewModel.lockClickable = false
                    viewModel.lock(boxId.text.toString().toLong(),true)
                }
            }
        }
        unlockButton.setOnClickListener{
            if(boxId.text.isNotEmpty()) {
                if (viewModel.unlockClickable){
                    viewModel.unlockClickable = false
                    viewModel.unlock(boxId.text.toString().toLong(), true)
                }
            }
        }
    }

}
