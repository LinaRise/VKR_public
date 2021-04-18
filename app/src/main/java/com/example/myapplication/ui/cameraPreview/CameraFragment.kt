package com.example.myapplication.ui.cameraPreview

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.database.DBHelper
import com.example.myapplication.entity.Word
import com.example.myapplication.ui.cameraPreview.camera.CameraSource
import com.example.myapplication.ui.cameraPreview.camera.CameraSourcePreview
import com.example.myapplication.ui.cameraPreview.camera.GraphicOverlay
import com.example.myapplication.ui.cameraPreview.camera.ICameraFragmentView
import com.example.myapplication.ui.chathead.ChatActivityPresenter
import com.example.myapplication.ui.chathead.CopiedTextAddDialog
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.util.*


class CameraFragment : Fragment(), ICameraFragmentView {

    private lateinit var cameraViewModel: CameraViewModel

    companion object {
        private const val TAG = "CameraFragment"

        // Intent request code to handle updating play services if needed.
        private const val RC_HANDLE_GMS = 9001

        // Permission request codes need to be < 256
        private const val RC_HANDLE_CAMERA_PERM = 2

        // Constants used to pass extra data in the intent
        const val AutoFocus = "AutoFocus"
        const val UseFlash = "UseFlash"
        const val TextBlockObject = "String"
    }

    lateinit var dbhelper: DBHelper
    lateinit var presenter: CameraFragmentPresenter


    private var cameraSource: CameraSource? = null
    private var preview: CameraSourcePreview? = null
    private var graphicOverlay: GraphicOverlay<OcrGraphic>? = null

    // Helper objects for detecting taps and pinches.
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var gestureDetector: GestureDetector? = null

    // A TextToSpeech engine for speaking a String value.
    private var tts: TextToSpeech? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cameraViewModel =
            ViewModelProvider(this).get(CameraViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_camera, container, false)
//        val textView: TextView = root.findViewById(R.id.text_dashboard)
//        cameraViewModel.text.observe(viewLifecycleOwner, {
//            textView.text = it
//        })
        dbhelper = DBHelper(requireContext())
        presenter = CameraFragmentPresenter(this, dbhelper)

        gestureDetector = GestureDetector(requireContext(), CaptureGestureListener())
        scaleGestureDetector = ScaleGestureDetector(requireContext(), ScaleListener())
        root.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                /*  if (event.action == MotionEvent.ACTION_MOVE) {
                      val b = scaleGestureDetector!!.onTouchEvent(event)
                      val c = gestureDetector!!.onTouchEvent(event)
                      return b || c
                  }
                  return true*/
                val b = scaleGestureDetector!!.onTouchEvent(event)
                val c = gestureDetector!!.onTouchEvent(event)
                return b || c
            }
        })
//        root.setOnTouchListener { _, event ->
//
//        }

        preview = root.findViewById<View>(R.id.preview) as CameraSourcePreview
        graphicOverlay = root.findViewById<GraphicOverlay<OcrGraphic>>(R.id.graphicOverlay)

        // Set good defaults for capturing text.

        // Set good defaults for capturing text.
        val autoFocus = true
        val useFlash = false

        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.

        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        val rc = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource(autoFocus, useFlash)
        } else {
            requestCameraPermission()
        }



        Snackbar.make(
            requireActivity().findViewById(android.R.id.content)!!,
            "Tap to Speak. Pinch/Stretch to zoom",
            Snackbar.LENGTH_LONG
        ).show()

        // Set up the Text To Speech engine.

        // Set up the Text To Speech engine.
        val listener = OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                Log.d("OnInitListener", "Text to speech engine started successfully.")
                tts!!.language = Locale.US
            } else {
                Log.d("OnInitListener", "Error starting the text to speech engine.")
            }
        }
        tts = TextToSpeech(activity?.applicationContext, listener)
        return root
    }

    private fun requestCameraPermission() {
        Log.w(CameraFragment.TAG, "Camera permission is not granted. Requesting permission")
        val permissions = arrayOf(Manifest.permission.CAMERA)
        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.CAMERA
            )
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissions,
                CameraFragment.RC_HANDLE_CAMERA_PERM
            )
            return
        }
        val thisActivity: Activity = requireActivity()
        val listener = View.OnClickListener {
            ActivityCompat.requestPermissions(
                thisActivity, permissions,
                CameraFragment.RC_HANDLE_CAMERA_PERM
            )
        }
        Snackbar.make(
            graphicOverlay!!, R.string.permission_camera_rationale,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(R.string.ok, listener)
            .show()
    }


//  override  fun onTouchEvent(e: MotionEvent?): Boolean {
//        val b = scaleGestureDetector!!.onTouchEvent(e)
//        val c = gestureDetector!!.onTouchEvent(e)
//        return b || c || super.onTouchEvent(e)
//    }

    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the ocr detector to detect small text samples
     * at long distances.
     *
     * Suppressing InlinedApi since there is a check that the minimum version is met before using
     * the constant.
     */
    @SuppressLint("InlinedApi")
    private fun createCameraSource(autoFocus: Boolean, useFlash: Boolean) {
        val context: Context = requireActivity().applicationContext

        // A text recognizer is created to find text.  An associated multi-processor instance
        // is set to receive the text recognition results, track the text, and maintain
        // graphics for each text block on screen.  The factory is used by the multi-processor to
        // create a separate tracker instance for each text block.
        val textRecognizer = TextRecognizer.Builder(context).build()
        textRecognizer.setProcessor(OcrDetectorProcessor(graphicOverlay))
        if (!textRecognizer.isOperational) {
            // Note: The first time that an app using a Vision API is installed on a
            // device, GMS will download a native libraries to the device in order to do detection.
            // Usually this completes before the app is run for the first time.  But if that
            // download has not yet completed, then the above call will not detect any text,
            // barcodes, or faces.
            //
            // isOperational() can be used to check if the required native libraries are currently
            // available.  The detectors will automatically become operational once the library
            // downloads complete on device.
            Log.w(CameraFragment.TAG, "Detector dependencies are not yet available.")

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            val lowstorageFilter = IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW)
            val hasLowStorage = requireActivity().registerReceiver(null, lowstorageFilter) != null
            if (hasLowStorage) {
                Toast.makeText(requireContext(), R.string.low_storage_error, Toast.LENGTH_LONG)
                    .show()
                Log.w(CameraFragment.TAG, getString(R.string.low_storage_error))
            }
        }

        // Creates and starts the camera.  Note that this uses a higher resolution in comparison
        // to other detection examples to enable the text recognizer to detect small pieces of text.
        cameraSource = CameraSource.Builder(requireActivity().applicationContext, textRecognizer)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .setRequestedPreviewSize(1280, 1024)
            .setRequestedFps(2.0f)
            .setFlashMode(if (useFlash) Camera.Parameters.FLASH_MODE_TORCH else null)
            .setFocusMode(if (autoFocus) Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO else null)
            .build()
    }

    /**
     * Restarts the camera.
     */
    override fun onResume() {
        super.onResume()
        startCameraSource()
    }

    /**
     * Stops the camera.
     */
    override fun onPause() {
        super.onPause()
        if (preview != null) {
            preview!!.stop()
        }
    }

    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */
    /* override fun onDestroy() {
        super.onDestroy()
        if (preview != null) {
            preview!!.release()
        }
    }*/


    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on [.requestPermissions].
     *
     *
     * **Note:** It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     *
     *
     * @param requestCode  The request code passed in [.requestPermissions].
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     * which is either [PackageManager.PERMISSION_GRANTED]
     * or [PackageManager.PERMISSION_DENIED]. Never null.
     * @see .requestPermissions
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode != CameraFragment.RC_HANDLE_CAMERA_PERM) {
            Log.d(CameraFragment.TAG, "Got unexpected permission result: $requestCode")
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(
                CameraFragment.TAG,
                "Camera permission granted - initialize the camera source"
            )
            // we have permission, so create the camerasource
            val autoFocus: Boolean = requireActivity().intent.getBooleanExtra(
                CameraFragment.AutoFocus,
                true
            )
            val useFlash: Boolean = requireActivity().intent.getBooleanExtra(
                CameraFragment.UseFlash,
                false
            )
            createCameraSource(autoFocus, useFlash)
            return
        }
        Log.e(
            CameraFragment.TAG, "Permission not granted: results len = " + grantResults.size +
                    " Result code = " + if (grantResults.isNotEmpty()) grantResults[0] else "(empty)"
        )
        val listener =
            DialogInterface.OnClickListener { dialog, id -> requireActivity().finish() }
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Multitracker sample")
            .setMessage(R.string.no_camera_permission)
            .setPositiveButton(R.string.ok, listener)
            .show()
    }

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    @Throws(SecurityException::class)
    private fun startCameraSource() {
        // check that the device has play services available.
        val code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
            requireContext().applicationContext
        )
        if (code != ConnectionResult.SUCCESS) {
            val dlg = GoogleApiAvailability.getInstance()
                .getErrorDialog(this, code, CameraFragment.RC_HANDLE_GMS)
            dlg.show()
        }
        if (cameraSource != null) {
            try {
                preview!!.start(cameraSource, graphicOverlay)
            } catch (e: IOException) {
                Log.e(CameraFragment.TAG, "Unable to start camera source.", e)
                cameraSource!!.release()
                cameraSource = null
            }
        }
    }

    /**
     * onTap is called to speak the tapped TextBlock, if any, out loud.
     *
     * @param rawX - the raw position of the tap
     * @param rawY - the raw position of the tap.
     * @return true if the tap was on a TextBlock
     */
    private fun onTap(rawX: Float, rawY: Float): Boolean {
        val graphic = graphicOverlay!!.getGraphicAtLocation(rawX, rawY)
        var text: TextBlock? = null
        if (graphic != null) {
            text = graphic.textBlock
            if (text != null) {
                Log.d(CameraFragment.TAG, "text data is being spoken! " + text.value)
                // Speak the string.
                tts!!.speak(text.value, TextToSpeech.QUEUE_ADD, null, "DEFAULT")
                val sets = presenter.getAllSetsTitles()
                val copiedTextAddDialog =
                    CopiedTextAddDialog(
                        sets,
                        Word(originalWord = text.value, translatedWord = ""),
                        null,
                        dbhelper
                    )
                copiedTextAddDialog.show(childFragmentManager, "Add copied word")
            } else {
                Log.d(CameraFragment.TAG, "text data is null")
            }
        } else {
            Log.d(CameraFragment.TAG, "no text detected")
        }
        return text != null
    }

    inner class CaptureGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            return onTap(e.rawX, e.rawY) || super.onSingleTapConfirmed(e)
        }

    }

    inner class ScaleListener : ScaleGestureDetector.OnScaleGestureListener {
        /**
         * Responds to scaling events for a gesture in progress.
         * Reported by pointer motion.
         *
         * @param detector The detector reporting the event - use this to
         * retrieve extended info about event state.
         * @return Whether or not the detector should consider this event
         * as handled. If an event was not handled, the detector
         * will continue to accumulate movement until an event is
         * handled. This can be useful if an application, for example,
         * only wants to update scaling factors if the change is
         * greater than 0.01.
         */
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            return false
        }

        /**
         * Responds to the beginning of a scaling gesture. Reported by
         * new pointers going down.
         *
         * @param detector The detector reporting the event - use this to
         * retrieve extended info about event state.
         * @return Whether or not the detector should continue recognizing
         * this gesture. For example, if a gesture is beginning
         * with a focal point outside of a region where it makes
         * sense, onScaleBegin() may return false to ignore the
         * rest of the gesture.
         */
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            return true
        }

        /**
         * Responds to the end of a scale gesture. Reported by existing
         * pointers going up.
         *
         *
         * Once a scale has ended, [ScaleGestureDetector.getFocusX]
         * and [ScaleGestureDetector.getFocusY] will return focal point
         * of the pointers remaining on the screen.
         *
         * @param detector The detector reporting the event - use this to
         * retrieve extended info about event state.
         */
        override fun onScaleEnd(detector: ScaleGestureDetector) {
            if (cameraSource != null) {
                cameraSource!!.doZoom(detector.scaleFactor)
            }
        }
    }

}