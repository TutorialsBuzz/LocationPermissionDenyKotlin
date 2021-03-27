package com.tutorialsbuzz.handlelocationpermissiondeny

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val PERMISSION_ID = 42

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestBtn.setOnClickListener({
            if (!checkPermissionsGranted())
                requestPermissions();
        })

        visitAppSettingsBtn.setOnClickListener({
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        })

    }

    override fun onResume() {
        super.onResume()
        if (checkPermissionsGranted()) {
            visitAppSettingsBtn.visibility = View.GONE
            requestBtn.visibility = View.GONE
            label.setText("Permission Granted")
        } else {
            label.setText("Permission Denied")
            checkUserRequestedDontAskAgain()
        }

    }

    fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    fun checkPermissionsGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                //when granted
                label.setText("Permission Granted:")
                showToast("Permission Granted:")
            } else {
                //Permission Denied
                label.setText("Permission Denied:")
                showToast("Permission Denied:")

                checkUserRequestedDontAskAgain()
            }
        }
    }

    private fun checkUserRequestedDontAskAgain() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val rationalFalgCOARSE =
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
            val rationalFalgFINE =
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
            if (!rationalFalgCOARSE && !rationalFalgFINE) {
                label.setText("permission_denied_forcefully")
                visitAppSettingsBtn.visibility = View.VISIBLE
                requestBtn.visibility = View.GONE
                showToast("permission_denied_forcefully")
            }
        }
    }

    fun showToast(msg: String) {
        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_LONG).show()
    }

}