package com.thanhuhiha.instagram.ui.register

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thanhuhiha.instagram.R
import com.thanhuhiha.instagram.ui.Fragment
import com.thanhuhiha.instagram.ui.common.coordinateBtnAndInputs
import kotlinx.android.synthetic.main.fragment_register_namepass.*

//2. fullname, password, register button
class NamePassFragment : Fragment() {
    private lateinit var mListener: Listener

    interface Listener {
        fun onRegister(fullName: String, password: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_namepass, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        coordinateBtnAndInputs(register_btn, full_name_input, password_input)
        register_btn.setOnClickListener {
            val fullName = full_name_input.text.toString()
            val password = password_input.text.toString()
            mListener.onRegister(fullName, password)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as Listener
    }
}