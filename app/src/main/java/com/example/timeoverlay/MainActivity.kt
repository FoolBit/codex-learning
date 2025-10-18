package com.example.timeoverlay

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup

class MainActivity : AppCompatActivity() {

    private val overlayPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            updatePermissionState()
            if (Settings.canDrawOverlays(this)) {
                startOverlayService()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permissionStatus = findViewById<TextView>(R.id.permissionStatus)
        val requestPermission = findViewById<Button>(R.id.requestPermission)
        val toggleGroup = findViewById<MaterialButtonToggleGroup>(R.id.overlayToggleGroup)
        val startButton = findViewById<MaterialButton>(R.id.startOverlay)
        val stopButton = findViewById<MaterialButton>(R.id.stopOverlay)
        toggleGroup.check(startButton.id)

        requestPermission.setOnClickListener {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                overlayPermissionLauncher.launch(intent)
            } else {
                startOverlayService()
            }
        }

        toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener
            when (checkedId) {
                startButton.id -> {
                    if (Settings.canDrawOverlays(this)) {
                        startOverlayService()
                    } else {
                        updatePermissionState()
                        requestPermission.performClick()
                    }
                }

                stopButton.id -> stopOverlayService()
            }
        }

        permissionStatus.text = permissionMessage(Settings.canDrawOverlays(this))
        updatePermissionState()
    }

    override fun onResume() {
        super.onResume()
        updatePermissionState()
    }

    private fun permissionMessage(granted: Boolean): String =
        if (granted) {
            getString(R.string.overlay_permission_title) + "：已授予"
        } else {
            getString(R.string.overlay_permission_title) + "：未授予"
        }

    private fun updatePermissionState() {
        val permissionStatus = findViewById<TextView>(R.id.permissionStatus)
        val requestPermission = findViewById<Button>(R.id.requestPermission)
        val toggleGroup = findViewById<MaterialButtonToggleGroup>(R.id.overlayToggleGroup)

        val granted = Settings.canDrawOverlays(this)
        permissionStatus.text = permissionMessage(granted)
        requestPermission.isEnabled = !granted
        toggleGroup.isEnabled = granted
        if (!granted) {
            toggleGroup.clearChecked()
        } else if (toggleGroup.checkedButtonId == View.NO_ID) {
            toggleGroup.check(R.id.startOverlay)
        }
    }

    private fun startOverlayService() {
        val intent = Intent(this, OverlayService::class.java)
        ContextCompat.startForegroundService(this, intent)
    }

    private fun stopOverlayService() {
        val intent = Intent(this, OverlayService::class.java)
        stopService(intent)
    }
}
