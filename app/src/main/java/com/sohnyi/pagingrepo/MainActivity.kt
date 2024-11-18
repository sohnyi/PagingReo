package com.sohnyi.pagingrepo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.sohnyi.pagingrepo.data.RepoRepository
import com.sohnyi.pagingrepo.databinding.ActivityMainBinding
import com.sohnyi.pagingrepo.model.Repo
import com.sohnyi.pagingrepo.ui.adapter.LoadStateFooterAdapter
import com.sohnyi.pagingrepo.ui.adapter.RepoAdapter
import com.sohnyi.pagingrepo.viewmodel.MainViewModel
import com.sohnyi.pagingrepo.viewmodel.MainViewModelFactory
import com.sohnyi.pagingrepo.viewmodel.UiAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private val repoAdapter by lazy {
        RepoAdapter(::onReoClick)
    }


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

        val footerAdapter = LoadStateFooterAdapter { repoAdapter.retry() }
        val concatAdapter = repoAdapter.withLoadStateFooter(footerAdapter)
        binding.initUI(concatAdapter)

        binding.bindSearch(viewModel.action)
        binding.bindList(repoAdapter, viewModel.reposFlow)

    }


    private fun ActivityMainBinding.initUI(adapter: ConcatAdapter) {
        rvRepos.adapter = adapter
        rvRepos.layoutManager = LinearLayoutManager(this@MainActivity)
    }

    private fun ActivityMainBinding.bindSearch(
        onUsernameChanged: (UiAction.Search) -> Unit,
    ) {
        etUsername.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput(onUsernameChanged)
                Log.i(TAG, "bindSearch: before return true")
                true
            } else {
                false
            }
        }

        etUsername.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput(onUsernameChanged)
                Log.i(TAG, "bindSearch: before return true")
                true
            } else {
                false
            }
        }
    }

    private fun ActivityMainBinding.updateRepoListFromInput(
        onUsernameChanged: (UiAction.Search) -> Unit,
    ) {
        etUsername.text.trim().let {
            if (it.isNotEmpty()) {
                rvRepos.scrollToPosition(0)
                Log.i(TAG, "updateRepoListFromInput: $it")
                onUsernameChanged(UiAction.Search(userName = it.toString()))
            }
        }
    }

    private fun ActivityMainBinding.bindList(
        adapter: RepoAdapter,
        repos: Flow<PagingData<Repo>>,
    ) {
        lifecycleScope.launch {
            repos.collectLatest {
                adapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collect { state ->
                progressCircular.isVisible = state.source.refresh is LoadState.Loading
                val isEmpty = state.source.refresh is LoadState.NotLoading && adapter.itemCount == 0
                tvEmpty.isVisible = isEmpty
                rvRepos.isVisible = !isEmpty


                layoutError.isVisible = state.source.refresh is LoadState.Error
            }
        }

        btnRetry.setOnClickListener { adapter.retry() }
    }

    private fun onReoClick(position: Int) {
        try {
            val repo = repoAdapter.peek(position) ?: return
            val url = repo.htmlUrl ?: return
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        } catch (e: Exception) {
            Log.e(TAG, "onReoClick: ERROR!", e)
        }
    }
}