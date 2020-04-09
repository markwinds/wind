package com.bangtong.wind.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation

import com.bangtong.wind.R
import com.bangtong.wind.api.WindService
import com.bangtong.wind.model.User
import com.bangtong.wind.util.LogUtil
import com.bangtong.wind.util.MyApplication
import com.bangtong.wind.util.TinyDB
import kotlinx.android.synthetic.main.fragment_log_in.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class LogInFragment : Fragment() {

    //private val viewModel by activityViewModels<WindViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        val tinyDB = TinyDB(MyApplication.context)
//        val id = tinyDB.getString("userId")
//        if(id != ""){
//            Navigation.findNavController(userId).navigate(R.id.action_logInFragment_to_homeFragment)
//        }
//        buttonLogIn.setOnClickListener{
//            if(userId.text.toString() != "" && userPwd.text.toString() != "") {
//                if(viewModel.logIn(userId.text.toString(), userPwd.text.toString()) == "OK"){
//                    Navigation.findNavController(userId).navigate(R.id.action_logInFragment_to_homeFragment)
//                }
//            }
//        }
//        buttonSignUp.setOnClickListener{
//            if(userId.text.toString() != "" && userPwd.text.toString() != "")
//                viewModel.signUp(userId.text.toString(),userPwd.text.toString())
//        }
//
//    }

}
