package com.example.rx

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.animation.DynamicAnimation
import android.support.animation.SpringAnimation
import android.support.animation.SpringForce
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var flag = false
    private lateinit var rocketAnimation: AnimationDrawable

    private companion object Params {
        val INITIAL_ROTATION = 0f
        val STIFFNESS = SpringForce.STIFFNESS_HIGH
        val DAMPING_RATIO = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
        lateinit var rotationAnimation: SpringAnimation
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // create a rotation SpringAnimation
        rotationAnimation = createSpringAnimation(
            button, SpringAnimation.ROTATION,
            INITIAL_ROTATION, STIFFNESS, DAMPING_RATIO
        )

        var previousRotation = 0f
        var currentRotation = 0f


        val rigth300 = ObjectAnimator.ofFloat(imageView2, "translationX", 300f).apply {
            duration = 500
        }

        val left300 = ObjectAnimator.ofFloat(imageView2, "translationY", 300f).apply {
            duration = 500
        }

        val rotate300 = ObjectAnimator.ofFloat(imageView2, "rotationX", 300f).apply {
            duration = 500
        }


        imageView2.setOnClickListener {
            AnimatorSet().apply {
                playTogether(rigth300, left300,rotate300)
                start()
            }
        }

        button7.setOnClickListener {
            //            val pvhX = PropertyValuesHolder.ofFloat("x", -20f)
//            val pvhY = PropertyValuesHolder.ofFloat("y", 20f)
//            ObjectAnimator.ofPropertyValuesHolder(imageView2, pvhX, pvhY).start()


            //move to this coordinate

            val location = IntArray(2)
            rotate.getLocationInWindow(location)
            val x = location[0]
            val y = location[1]

            imageView2.animate().x(x.toFloat()).y(y.toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                }
            })

            close.startAnimation(
                AnimationUtils.loadAnimation(
                    this@MainActivity,
                    R.anim.rotate
                )
            )
        }


        rotate.setOnClickListener {
            ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f).apply {
                duration = 300
                start()
            }

        }


        val springAnim1 = findViewById<View>(R.id.imageView2).let { img ->
            // Setting up a spring animation to animate the view’s translationY property with the final
            // spring position at 0.
            SpringAnimation(img, DynamicAnimation.TRANSLATION_Y, 100f)
        }

        val springAnim2 = findViewById<View>(R.id.imageView2).let { img ->
            // Setting up a spring animation to animate the view’s translationY property with the final
            // spring position at 0.
            SpringAnimation(img, DynamicAnimation.TRANSLATION_Y, -100f)
        }

        button2.setOnClickListener {
            springAnim1.start()
        }

        button3.setOnClickListener {
            springAnim2.start()
        }


        button4.setOnClickListener {
            //            imageView2.animation= AnimationUtils.loadAnimation(this, R.anim.left)
            ObjectAnimator.ofFloat(imageView2, "translationX", -300f).apply {
                duration = 500
                start()
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
//                        animation.removeListener(this)
//                        animation.duration = 0
//                        (animation as ValueAnimator).reverse()
                        ObjectAnimator.ofFloat(imageView2, "translationX", 0f).apply {
                            duration = 500
                            start()
                        }

                    }
                })
            }
        }

        button5.setOnClickListener {
            ObjectAnimator.ofFloat(imageView2, "translationX", 300f).apply {
                duration = 500
                start()
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        ObjectAnimator.ofFloat(imageView2, "translationX", 0f).apply {
                            duration = 500
                            start()
                        }
                    }
                })
            }
        }












        button.setOnTouchListener { view, event ->
            val centerX = view.width / 2.0
            val centerY = view.height / 2.0
            val x = event.x
            val y = event.y

            // angle calculation
            fun updateCurrentRotation() {
                currentRotation = view.rotation +
                        Math.toDegrees(Math.atan2(x - centerX, centerY - y)).toFloat()
            }

            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    // cancel so we can grab the view during previous animation
                    rotationAnimation.cancel()

                    updateCurrentRotation()
                }
                MotionEvent.ACTION_MOVE -> {
                    // save current rotation
                    previousRotation = currentRotation

                    updateCurrentRotation()

                    // rotate view by angle difference
                    val angle = currentRotation - previousRotation
                    view.rotation += angle
                }
                MotionEvent.ACTION_UP -> rotationAnimation.start()
            }
            true
        }


        //AnimatedVectorDrawable

        imageView.setOnClickListener {
            if (flag) {
                imageView.setImageResource(R.drawable.avd_anim)
                val d = imageView.drawable
                val anim = d as AnimatedVectorDrawable
                anim.start()
            } else {
                imageView.setImageResource(R.drawable.avd_anim2)
                val d = imageView.drawable
                val anim = d as AnimatedVectorDrawable
                anim.start()

            }
            flag = !flag
        }

    }


    fun createSpringAnimation(
        view: View,
        property: DynamicAnimation.ViewProperty,
        finalPosition: Float,
        stiffness: Float,
        dampingRatio: Float
    ): SpringAnimation {
        val animation = SpringAnimation(view, property)
        val spring = SpringForce(finalPosition)
        spring.stiffness = stiffness
        spring.dampingRatio = dampingRatio
        animation.spring = spring
        return animation
    }


//    val spring = SpringAnimation(button, DynamicAnimation.Y, -20f)
//        .setStartVelocity(5000f)
//    spring.start()


    //New Thread

//        var runnable = Runnable { someFunc() }
////        var xxx = Thread(runnable)
////        xxx.start()

    //with CAROUTINES

//        GlobalScope.launch(Dispatchers.Default) { someFunc() }


//        button.setOnClickListener {
//            Observable.just(1, 2, 3)
//                .subscribeOn(Schedulers.newThread())
//                .map { it * 2 }
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                    {
//                        textView.text = it.toString()
//                        Toast.makeText(this, "OK", Toast.LENGTH_LONG).show()
//                    },
//                    { Toast.makeText(this, it.message, Toast.LENGTH_LONG).show() })
//        }

//

//
//        val executionTime = measureTimeMillis {}
//        Log.i("TOTAL", "TOTAL TIME : $executionTime")
//
//        button.setOnClickListener {
//
//            someFunc()
//        }


//editText.addTextChangedListener(object :TextWatcher{
//
//    override fun afterTextChanged(s: Editable?) {
//
//    }
//
//    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//    }
//
//    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//        textView.text=editText.text
//    }
//})


    // rx binding


//        RxTextView.textChanges(editText)
//            .debounce(500, TimeUnit.MILLISECONDS)
//            .skip(3)
//            .subscribe {
//                Observable.just(it)
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe { textView.text = it }
//            }
//
//        var clicker = RxView.clicks(button).subscribe()


//zip


//        button.setOnClickListener {
//            val stringA = "abcd"
//            val stringB = "zyx"
//           // textView.text = stringA.zip(stringB) { a, b -> "$a$b" }.toString()
//
//            val names= Observable.just("1","2","3")
//            val ages= Observable.just(21,22,23,24)
//
//        }


}

//    private fun someFunc() {
//        var x = Observable.range(1, 4000)
//            .subscribeOn(Schedulers.newThread())
//            .map { it * 2 }
//            .subscribe {
//                result += it.toString()
//
//                Log.i("TAG", "$it {${Thread.currentThread().name}}")
//
//                result2 = runBlocking { async(Dispatchers.IO) { exampleCoroutines(it) }.await() }
//
//                Log.i("TAG", "Main get Answer $result2 in {${Thread.currentThread().name}}")
//
//            }
//    }
//
//    fun exampleCoroutines(number: Int): String {
//        Log.i("TAG", "Caroutine get $number in ${Thread.currentThread().name}")
//        Log.i("TAG", "Caroutine make change  $number+1 in  ${Thread.currentThread().name}")
//        return "${number + 1}"
//    }




