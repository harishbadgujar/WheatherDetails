package com.example.weatherforecast.ui.city

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.database.entity.Bookmark
import com.example.weatherforecast.databinding.CityFragmentBinding
import com.example.weatherforecast.network.Status

/**
 * Created by Harish on 16-07-2021
 */
class CityFragment : Fragment() {
    lateinit var binding: CityFragmentBinding
    private lateinit var viewModel: CityViewModel
    private var bookMark: Bookmark? = null

    companion object {
        fun newInstance() = CityFragment()

        const val CITY = "city"
        const val CITY_FRAGMENT = "cityFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bookMark = arguments?.getParcelable(CITY)
        binding = CityFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CityViewModel::class.java)
        bookMark?.let { bookMark ->
            activity?.let {
                viewModel.getCityWeatherData(bookMark).observe(it) { resource ->

                    when (resource.status) {

                        Status.Loading -> {
                            binding.progressHorizontal.isVisible = true
                            binding.progressHorizontal.animate()
                        }

                        Status.Success -> {
                            resource.data?.let { weather ->

                                if (weather.isSuccessful) {

                                    binding.progressHorizontal.isVisible = false
                                    binding.locationCard.isVisible = true
                                    binding.weathercard.isVisible = true

                                    weather.body()?.let { weatherResponse ->

                                        with(binding) {

                                            tvLocation.text = weatherResponse.name

                                            tvMaxTemp.text =
                                                weatherResponse.main.temp_max.toString()

                                            tvMinTemp.text =
                                                weatherResponse.main.temp_min.toString()

                                            tvhHumidity.text =
                                                weatherResponse.main.humidity.toString()

                                            tvWind.text = weatherResponse.wind.speed.toString()

                                            tvPressure.text =
                                                weatherResponse.main.pressure.toString()

                                            tvSea.text = weatherResponse.main.sea_level.toString()
                                        }

                                    }

                                }

                            }
                        }

                        Status.Error -> {
                            Toast.makeText(activity, resource.message, Toast.LENGTH_LONG).show()
                        }

                    }

                }
            }
        }
    }

    override fun onResume() {
        bookMark?.let {
            activity?.title = it.city
        }
        super.onResume()
    }

}