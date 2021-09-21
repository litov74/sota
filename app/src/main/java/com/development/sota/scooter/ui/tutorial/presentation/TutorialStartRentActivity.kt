package com.development.sota.scooter.ui.tutorial.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.development.sota.scooter.MainActivity
import com.development.sota.scooter.databinding.ActivityStartRentTutorialBinding
import com.development.sota.scooter.ui.drivings.presentation.DrivingsActivity
import com.development.sota.scooter.ui.drivings.presentation.DrivingsStartTarget
import com.development.sota.scooter.ui.help.presentation.HelpActivity
import com.development.sota.scooter.ui.map.data.Scooter
import com.development.sota.scooter.ui.map.presentation.MapActivity
import kotlinx.android.synthetic.main.activity_tutorial.*
import kotlinx.android.synthetic.main.fragment_tutorial.*
import moxy.MvpAppCompatActivity
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.alias.AddToEnd
import java.io.Serializable


interface TutorialStartRentView : MvpView {
    @AddToEnd
    fun nextPage(index: Int)

    @AddToEnd
    fun finishActivity()
}

class TutorialStartRentActivity : MvpAppCompatActivity(), TutorialStartRentView {
    private val presenter by moxyPresenter { TutorialStartRentPresenter(this) }
    private var _binding: ActivityStartRentTutorialBinding? = null
    private val binding get() = _binding!!
    private val QR_CODE_REQUEST = 555

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStartRentTutorialBinding.inflate(layoutInflater)

        setContentView(binding.root)

        window.decorView.setOnTouchListener(object: OnSwipeTouchListener(this@TutorialStartRentActivity){
            override fun onSwipeLeft() {
                presenter.onNextButtonClicked(binding.viewPager2Tutorial.currentItem)
            }

            override fun onSwipeRight() {
                //onBackPressed()
                val tmp: Int = 1
                presenter.onNextButtonClicked(binding.viewPager2Tutorial.currentItem - 2*tmp)
            }
        })

        binding.viewPager2Tutorial.adapter = TutorialStartRentAdapter()
        binding.viewPager2Tutorial.isUserInputEnabled = false


        binding.buttonTutorialSkip.setOnClickListener { presenter.onSkipButtonClicked() }
        binding.done.setOnClickListener { presenter.onSkipButtonClicked() }
        binding.needSupport.setOnClickListener {
            val intent = Intent (getApplicationContext(), HelpActivity::class.java)
            startActivity(intent)
        }

    }

    override fun nextPage(index: Int) {
        runOnUiThread {
            binding.viewPager2Tutorial.setCurrentItem(index, true)
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun finishActivity() {
        runOnUiThread {
            val intent = Intent(this, DrivingsActivity::class.java)

            intent.putExtra("aim", DrivingsStartTarget.DrivingList)
            startActivityForResult(intent, QR_CODE_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == QR_CODE_REQUEST && resultCode == RESULT_OK) {
            val sendData = Intent()
            sendData.putExtra("scooter", data?.getSerializableExtra("scooter") as Scooter as Serializable);
            setResult(Activity.RESULT_OK, sendData);
            finish()
        } else {
            finish()
        }

    }
}


open class OnSwipeStartRentTouchListener(ctx: Context): View.OnTouchListener {
    private val gestureDetector: GestureDetector
    companion object {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100
    }
    init {
        gestureDetector = GestureDetector(ctx, GestureListener())
    }
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }
    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            var result = false
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight()
                        } else {
                            onSwipeLeft()
                        }
                        result = true
                    }
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom()
                    } else {
                        onSwipeTop()
                    }
                    result = true
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }

            return result
        }
    }



    open fun onSwipeRight() {}

    open fun onSwipeLeft() {}

    open fun onSwipeTop() {}

    open fun onSwipeBottom() {}

}