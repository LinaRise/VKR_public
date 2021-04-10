package com.example.myapplication.ui.chathead

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import android.view.View.OnTouchListener
import android.widget.ImageView
import android.widget.Toast
import com.example.myapplication.R


class ChatHeadService : Service() {
    private var mWindowManager: WindowManager? = null
    private var mChatHeadView: View? = null
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onCreate() {
        super.onCreate()
        //Inflate the chat head layout we created
        mChatHeadView = LayoutInflater.from(this).inflate(R.layout.layout_chat_head,null,false)

       /* Toast.makeText(application, "TOAST", Toast.LENGTH_SHORT)
            .show()*/
        //Add the view to the window.
        val LAYOUT_FLAG: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        //Specify the chat head position
//Initially view will be added to top-left corner
        params.gravity = Gravity.TOP or Gravity.LEFT
        params.x = 0
        params.y = 100

        //Add the view to the window
        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager?
        mWindowManager!!.addView(mChatHeadView, params)

//….
//….
        //Set the close button.

        //Set the close button.
        val closeButton: ImageView = mChatHeadView!!.findViewById<View>(R.id.close_btn) as ImageView
        closeButton.setOnClickListener { //close the service and remove the chat head from the window
            stopSelf()
        }

//Drag and move chat head using user's touch action.

//Drag and move chat head using user's touch action.
        val chatHeadImage: ImageView =
            mChatHeadView!!.findViewById<View>(R.id.chat_head_profile_iv) as ImageView



        chatHeadImage.setOnTouchListener(object : OnTouchListener {
            private var lastAction = 0
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {

                        //remember the initial position.
                        initialX = params.x
                        initialY = params.y

                        //get the touch location
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        lastAction = event.action
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        //As we implemented on touch listener with ACTION_MOVE,
                        //we have to check if the previous action was ACTION_DOWN
                        //to identify if the user clicked the view or not.

                        if ((Math.abs(initialTouchX - event.rawX) < 5) && (Math.abs(
                                initialTouchY - event.rawY
                            ) < 5)
                        ) {
                            val intent = Intent(this@ChatHeadService, ChatActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                            startActivity(intent)

                            //close the service and remove the chat heads
                            stopSelf()
                        }
                    else Toast.makeText(
                        applicationContext
                        , "It's not a click !",
                    Toast.LENGTH_LONG
                        )
                        .show()
                    lastAction = event.action
                        return true


                }
                MotionEvent.ACTION_MOVE -> {
                    //Calculate the X and Y coordinates of the view.
                    params.x = initialX + (event.rawX - initialTouchX).toInt()
                    params.y = initialY + (event.rawY - initialTouchY).toInt()

                    //Update the layout with new X & Y coordinate
                    mWindowManager!!.updateViewLayout(mChatHeadView, params)
                    lastAction = event.action
                    return true
                }
            }
            return false
        }
    })


}

override fun onDestroy() {
    super.onDestroy()
    if (mChatHeadView != null) mWindowManager!!.removeView(mChatHeadView)
}
}