package com.dicoding.mystory.view.main


import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.mystory.R
import com.dicoding.mystory.adapter.LoadingStateAdapter
import com.dicoding.mystory.adapter.StoryListAdapter
import com.dicoding.mystory.databinding.ActivityMainBinding
import com.dicoding.mystory.factory.ViewModelFactory
import com.dicoding.mystory.factory.ViewModelFactoryStory
import com.dicoding.mystory.view.addstory.AddStoryActivity
import com.dicoding.mystory.view.login.LoginActivity
import com.dicoding.mystory.view.map.MapsActivity
import com.dicoding.mystory.view.welcome.WelcomeActivity


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private val viewModel: MainViewModel2 by viewModels {
        ViewModelFactoryStory(this)
    }
    private lateinit var binding: ActivityMainBinding
    private val adapter = StoryListAdapter()
    private var token:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()

    }


    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        mainViewModel = ViewModelProvider(
            this,
          factory
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                binding.tvName.text = getString(R.string.greeting, user.name)
               token=user.token
                getData(token)
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }

        }
    }
    private fun getData(token:String){
        showLoading(true)
        binding.apply {
            rvListStory.setHasFixedSize(true)
            rvListStory.layoutManager =  GridLayoutManager(this@MainActivity,2)
            rvListStory.adapter = this@MainActivity.adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    this@MainActivity.adapter.retry()
                }
            )

        }

        viewModel.sending(token).observe(this) {
            adapter.submitData(lifecycle, it)


        }

        showLoading(false)
    }

    override fun onResume() {
        super.onResume()
        getData(token)
    }

    private fun setupAction() {
        with(binding) {
                ibLogout.setOnClickListener {
                Intent(this@MainActivity, LoginActivity::class.java)
                    .apply {
                        startActivity(this)
                        finishAffinity()
                        mainViewModel.logout()
                    }
            }

            ibMylocation.setOnClickListener {
                Intent(this@MainActivity, MapsActivity::class.java)
                    .apply {
                        startActivity(this)

                    }
            }

            fabAddNewStory.setOnClickListener {
                Intent(this@MainActivity, AddStoryActivity::class.java)
                    .apply {
                        startActivity(this)
                    }
            }


        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }


}