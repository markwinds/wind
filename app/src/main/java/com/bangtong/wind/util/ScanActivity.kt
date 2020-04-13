package com.bangtong.wind.util

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView


class ScanActivity : MyActivity(), ZBarScannerView.ResultHandler {

    private lateinit var mScannerView: ZBarScannerView
    private val TAG = "ScanActivity_ZBT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mScannerView = ZBarScannerView(this);    // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    override fun onResume() {
        super.onResume()
        mScannerView.setResultHandler(this) // Register ourselves as a handler for scan results.
        mScannerView.startCamera() // Start camera on resume
    }

    override fun onPause() {
        super.onPause()
        mScannerView.stopCamera() // Stop camera on pause
    }

    override fun handleResult(rawResult: Result?) {
        // Do something with the result here
        LogUtil.d(TAG, rawResult!!.contents); // Prints scan results
        //Log.v(TAG, rawResult.getBarcodeFormat().getName()); // Prints the scan format (qrcode, pdf417 etc.)

        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
        val replyIntent = Intent().putExtra("code",rawResult?.contents)
        setResult(Activity.RESULT_OK, replyIntent)
        finish()
    }
}
