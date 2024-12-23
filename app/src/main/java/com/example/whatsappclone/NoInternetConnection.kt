package com.example.whatsappclone

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.whatsappclone.Fragments.ChatsFragments
import com.example.whatsappclone.databinding.FragmentNoInternetConnectionBinding


class NoInternetConnection : Fragment() {
    private val imgBack: ImageView? = null
    private var helper = Helper()

    private var binding: FragmentNoInternetConnectionBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoInternetConnectionBinding.inflate(inflater, container, false)
        init()
        binding?.ivBack?.setOnClickListener {

            val i = Intent(getActivity(), ChatsFragments::class.java)
            startActivity(i)


        }
        return binding!!.root
    }

    private fun init() {
        helper = Helper()
        helper.setContext(requireContext())
        if (helper.isOnline()) {
            val i = Intent(requireContext(), ChatDetailActivity::class.java)
            startActivity(i)
        }
        else {

        }
    }

}