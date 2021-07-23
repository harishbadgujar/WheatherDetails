package com.example.weatherforecast.ui.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.HelpFragmentBinding

/**
 * Created by Harish on 16-07-2021
 */
class HelpFragment : Fragment() {
    lateinit var binding: HelpFragmentBinding

    private val helpString = "<html>\n" +
            "\n" +
            "<body>\n" +
            "\n" +
            "<b>1. Bookmark Location</b> <br>\n" +
            "In order to bookmark location tap on + button. From the given map, select location of your interest to bookmark. <br><br>\n" +
            "\n" +
            "<b>2. Delete Location </b><br>\n" +
            "In order to delete the location fro the location list, tap on the delete icon. <br><br>"+
            "\n" +
            "<b>3. Check Weather Details </b><br>\n" +
            "To see the weather details of bookmarked location, tap on that location, it will open new screen where you can see the weather details like temperature, wind, pressure. <br>\n" +
            "\n" +
            "<body>\n" +
            "\n" +
            "</html>"

    companion object {
        fun newInstance() = HelpFragment()

        const val HELP_FRAGMENT = "helpFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HelpFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.webView.loadData(helpString,"text/html",null)
    }

    override fun onResume() {
        activity?.title = getString(R.string.how_to_use_app)
        super.onResume()
    }


}