package com.example.kotlinpagination

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        val adapter = UserAdapter()
        val itemViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        itemViewModel.getUsersList(RequestData(10, 1, 1)).observe(this, Observer {
            if (it != null) {
                adapter.submitList(it)
            }
        })
        itemViewModel.getNetworkState()
            ?.observe(this, Observer { networkState -> adapter.setNetworkState(networkState) })
        recyclerView.adapter = adapter
    }
}