package com.example.addresssearch.View

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.addresssearch.Data.Model.Address
import com.example.addresssearch.R
import com.example.addresssearch.View.Adapter.AddressAdapter
import com.example.addresssearch.ViewModel.SearchAddressViewModel
import com.example.addresssearch.databinding.ActivityMainBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val addressViewModel by viewModels<SearchAddressViewModel>()
    private val addressAdapter by lazy { AddressAdapter() }
    private var searchJob: Job? = null
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.rvAddress.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = addressAdapter
        }

        addressViewModel.isLoading.observe(this) {
            if (it){
                binding.progressBar.visibility = View.VISIBLE
                binding.imgSearch.visibility = View.GONE
            }else{
                binding.progressBar.visibility = View.GONE
                binding.imgSearch.visibility = View.VISIBLE
            }
        }
        addressViewModel.address.observe(this) {
            addressAdapter.diff.submitList(it)
        }
        addressAdapter.onClick = {
            openGGMap(it)
        }

        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.isNullOrEmpty()){
                    binding.edtSearch.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0)
                }else{
                    binding.edtSearch.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_close,0)

                }
                val keyWord = s.toString().trim()
                addressAdapter.setKeyWord(keyWord)
                searchJob?.cancel()
                searchJob = MainScope().launch {
                        delay(1000)
                        addressViewModel.searchAddress(keyWord)
                    }
                }


        })
    }

    private fun openGGMap(address: Address) {
        try {
            val uri = Uri.parse("google.navigation:q=${address.lat},${address.lon}&mode=d")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.apps.maps")
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
                Log.e("checkGGMap","ko null")
            } else {
                Log.e("checkGGMap","null")

                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/maps/dir/?api=1&destination=${address.lat},${address.lon}&travelmode=driving")
                )
                startActivity(browserIntent)
            }
        } catch (e: Exception) {
            Toast.makeText(this@MainActivity, "Lỗi mở gg map: ${e.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }

}