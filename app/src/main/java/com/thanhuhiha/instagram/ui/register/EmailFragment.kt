package com.thanhuhiha.instagram.ui.register

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thanhuhiha.instagram.R
import com.thanhuhiha.instagram.ui.Fragment
import com.thanhuhiha.instagram.ui.common.coordinateBtnAndInputs
import com.thanhuhiha.instagram.ui.coordinateBtnAndInput
import kotlinx.android.synthetic.main.fragment_register_email.*

//1.Email, next button
class EmailFragment : Fragment() {
    private lateinit var mListener: Listener

    interface Listener {
        fun onNext(email: String)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        coordinateBtnAndInputs(next_btn, email_input)

        next_btn.setOnClickListener {
            val email = email_input.text.toString()
            mListener.onNext(email)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as Listener
    }
}