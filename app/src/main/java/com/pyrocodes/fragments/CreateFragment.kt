package com.pyrocodes.fragments


import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pyrocodes.paper.R
import com.pyrocodes.utils.Utils
import kotlinx.android.synthetic.main.fragment_create.*
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class CreateFragment : androidx.fragment.app.Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var c = Calendar.getInstance()
        Log.e("on Created", "View")
        c.timeInMillis = Date().time
        date.text = c.get(Calendar.DAY_OF_MONTH).toString()
        month.text = Utils.getTimeFormated("MMMM", c.timeInMillis)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("on destroy", "View")
    }

    override fun onStop() {
        super.onStop()
        Log.e("on Stop", "View")
    }

    override fun onPause() {
        super.onPause()



        Log.e("on pause", "View")

    }

    private fun saveData(data: String, sqLiteDatabase: SQLiteDatabase) {

    }

    override fun onResume() {
        super.onResume()
        Log.e("on resume", "View")
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
