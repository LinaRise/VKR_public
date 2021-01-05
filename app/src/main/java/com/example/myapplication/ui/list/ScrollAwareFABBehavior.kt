/*
package com.example.myapplication.ui.list

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ScrollAwareFABBehavior(context: Context?, attrs: AttributeSet?) :
    FloatingActionButton.Behavior() {

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout, child: FloatingActionButton,
        directTargetChild: View, target: View, nestedScrollAxes: Int
    ): Boolean {
        // Ensure we react to vertical scrolling
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
//                || super.onStartNestedScroll(
//            coordinatorLayout,
//            child,
//            directTargetChild,
//            target,
//            nestedScrollAxes

    }



    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type,
            consumed
        )
        if (dyConsumed > 0) {
            val layoutParams = child.layoutParams as CoordinatorLayout.LayoutParams
            val fab_bottomMargin = layoutParams.bottomMargin
            child.animate().translationY((child.height + fab_bottomMargin).toFloat())
                .setInterpolator(LinearInterpolator()).start()
        } else if (dyConsumed < 0) {
            child.animate().translationY(0.0f).setInterpolator(LinearInterpolator()).start()
        }
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        type: Int
    ) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
        child.animate().translationY(0.0f).setInterpolator(LinearInterpolator()).start()
    }

    //    override fun onNestedScroll(
//        coordinatorLayout: CoordinatorLayout, child: FloatingActionButton,
//        target: View, dxConsumed: Int, dyConsumed: Int,
//        dxUnconsumed: Int, dyUnconsumed: Int
//    ) {
//        super.onNestedScroll(
//            coordinatorLayout,
//            child,
//            target,
//            dxConsumed,
//            dyConsumed,
//            dxUnconsumed,
//            dyUnconsumed
//        )
//        if (dyConsumed > 0 && child.visibility == View.VISIBLE) {
//            // User scrolled down and the FAB is currently visible -> hide the FAB
//            child.hide()
//        } else if (dyConsumed < 0 && child.visibility != View.VISIBLE) {
//            // User scrolled up and the FAB is currently not visible -> show the FAB
//            child.show()
//        }
//    }
}*/
