package com.draco.ssh.views

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.ScrollView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.draco.ssh.BuildConfig
import com.draco.ssh.R
import com.draco.ssh.viewmodels.ShellActivityViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.jcraft.jsch.*
import kotlinx.coroutines.*

class ShellActivity : AppCompatActivity() {
    val viewModel: ShellActivityViewModel by viewModels()

    private lateinit var progress: ProgressBar
    lateinit var command_btn: TextInputEditText
    private lateinit var output: MaterialTextView
    private lateinit var outputScrollView: ScrollView

    private lateinit var errorDialog: AlertDialog

    private lateinit var encryptedSharedPrefs: SharedPreferences
    private lateinit var appBarConfiguration: AppBarConfiguration

    var commands: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shell)
        progress = findViewById(R.id.progress)
        command_btn = findViewById(R.id.command)
        output = findViewById(R.id.output)
        outputScrollView = findViewById(R.id.output_scrollview)

        //commands = "cd /home/pi/ROBOT/pythonProject1;sudo python main.py"

        val masterKeyAlias = MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        encryptedSharedPrefs = EncryptedSharedPreferences.create(
            this,
            MasterKey.DEFAULT_MASTER_KEY_ALIAS,
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        with(encryptedSharedPrefs) {
            commands = getString("commands", "help")
        }

        //Фрагмент с управлением
//        to_control_activity = findViewById(R.id.to_control_activity)
        val controlFragment = findNavController(R.id.fragmentContainerView)
        appBarConfiguration = AppBarConfiguration(controlFragment.graph)
        setupActionBarWithNavController(controlFragment, appBarConfiguration)

        errorDialog = MaterialAlertDialogBuilder(this)
            .setTitle(R.string.error_title)
            .setPositiveButton(R.string.error_disconnect) { _, _ -> finish() }
            .setCancelable(false)
            .create()

        command_btn.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                val text = command_btn.text.toString()
                command_btn.text = null
                viewModel.shell.send(text)

                return@setOnKeyListener true
            }

            return@setOnKeyListener false
        }
        setupShell()
    }

    override fun onPause() {
        super.onPause()
        with(encryptedSharedPrefs.edit()) {
            putString("commands", commands.toString())
            apply()
        }
    }

    private fun setupShell() {
        val address = intent.getStringExtra("address")!!
        val port = try {
            Integer.parseInt(intent.getStringExtra("port")!!)
        } catch (_: Exception) { 22 }
        val username = intent.getStringExtra("username")!!
        val password = intent.getStringExtra("password")!!

        viewModel.connectClientAndStartOutputThread(username, address, port, password)
        viewModel.shell.getReady().observe(this) {
            if (it == true) {
                progress.visibility = View.INVISIBLE
                command_btn.isEnabled = true
            }
        }

        viewModel.shell.error.observe(this) { error(it) }
        viewModel.getOutputText().observe(this) { output.text = it }
    }

    private fun error(exceptionMessage: String) {
        errorDialog.run {
            setMessage(exceptionMessage)
            show()
        }
    }

    override fun onBackPressed() {
        viewModel.shell.deinitialize()
        super.onBackPressed()
    }

    override fun onDestroy() {
        errorDialog.dismiss()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.shell, menu)
        return super.onCreateOptionsMenu(menu)
    }
}