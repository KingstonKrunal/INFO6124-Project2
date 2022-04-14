package com.example.krunalshah.info6124_project2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import kotlin.math.log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SmsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SmsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var currentAddress: String? = null
    private var param2: String? = null

    private lateinit var numberText: EditText
    private lateinit var sendSmsButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentAddress = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_sms, container, false)

        numberText = view.findViewById(R.id.editTextNumber)
        sendSmsButton = view.findViewById(R.id.sendSmsButton)

        sendSmsButton.setOnClickListener {
            currentAddress = MapsFragment().currentAddress
            Log.i("address", "$currentAddress")

            val intent = Intent(Intent.ACTION_SEND)
            intent.data = Uri.parse("sms:$numberText")
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_EMAIL, numberText.text.toString())
            intent.putExtra(Intent.EXTRA_SUBJECT, "My Location")
            intent.putExtra(Intent.EXTRA_TEXT, currentAddress)

            startActivity(Intent.createChooser(intent, "Select Messages"))
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SmsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SmsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}