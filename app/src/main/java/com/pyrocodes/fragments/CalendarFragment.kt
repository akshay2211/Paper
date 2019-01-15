package com.pyrocodes.fragments


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import com.pyrocodes.paper.R
import com.pyrocodes.utils.Utils
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CalendarFragment : Fragment() {
    // TODO: Rename and change types of parameters
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    private val TAG: String? = CalendarFragment::class.java.canonicalName + " xak"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        compactcalendar_view.setFirstDayOfWeek(Calendar.MONDAY);
        val ev1 = Event(Color.GREEN, 1433701251000L, "Some extra data that I want to store.")
        compactcalendar_view.addEvent(ev1)

        // Added event 2 GMT: Sun, 07 Jun 2015 19:10:51 GMT
        val ev2 = Event(Color.GREEN, 1433704251000L)
        compactcalendar_view.addEvent(ev2)

        // Query for events on Sun, 07 Jun 2015 GMT.
        // Time is not relevant when querying for events, since events are returned by day.
        // So you can pass in any arbitary DateTime and you will receive all events for that day.
        val events = compactcalendar_view.getEvents(1433701251000L) // can also take a Date object

        // events has size 2 with the 2 events inserted previously
        Log.d(TAG, "Events: $events")

        // define a listener to receive callbacks when certain events happen.
        compactcalendar_view.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date?) {
                val events = compactcalendar_view.getEvents(dateClicked)
                Log.d(TAG, "Day was clicked: $dateClicked with events $events")
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                var c = Calendar.getInstance()
                c.timeInMillis = firstDayOfNewMonth!!.time
                Log.d(TAG, "Month was scrolled to: ${Utils.getTimeFormated("MMM", c.timeInMillis)}")
            }

        })
    }

    /* fun onDayClick(dateClicked: Date) {
                val events = compactcalendar_view.getEvents(dateClicked)
                Log.d(TAG, "Day was clicked: $dateClicked with events $events")
            }

            fun onMonthScroll(firstDayOfNewMonth: Date) {
                Log.d(TAG, "Month was scrolled to: $firstDayOfNewMonth")
            }*/


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CalendarFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalendarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
