package com.sohnyi.pagingrepo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.sohnyi.pagingrepo.data.RepoRepository
import com.sohnyi.pagingrepo.databinding.ActivityMainBinding
import com.sohnyi.pagingrepo.model.Repo
import com.sohnyi.pagingrepo.ui.adapter.RepoAdapter
import com.sohnyi.pagingrepo.viewmodel.MainViewModel
import com.sohnyi.pagingrepo.viewmodel.MainViewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var userName = "square"

    override fun onCreate(savedInstanceState: Bundle?) {

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(this, RepoRepository())
        )[MainViewModel::class.java]

        val adapter = RepoAdapter(::onReoClick)
        binding.initUI(adapter)

       val repos = viewModel.getRepos(userName)
        binding.bindList(adapter,repos)
    }


    private fun ActivityMainBinding.initUI(adapter: RepoAdapter) {
        rvRepos.adapter = adapter
        rvRepos.layoutManager = LinearLayoutManager(this@MainActivity)
    }

    private fun ActivityMainBinding.bindList(
        adapter: RepoAdapter,
        repos: Flow<PagingData<Repo>>
    ) {
        lifecycleScope.launch {
            repos.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun onReoClick(position: Int) {

    }
}