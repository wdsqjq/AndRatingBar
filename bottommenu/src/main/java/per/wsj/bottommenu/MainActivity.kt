package per.wsj.bottommenu

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.shape.MaterialShapeDrawable

class MainActivity : AppCompatActivity() {

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val badge = navView.getOrCreateBadge(R.id.navigation_dashboard)
        badge.number = 18
        badge.backgroundColor = Color.RED
        badge.maxCharacterCount = 3
//        badge.badgeGravity = BadgeDrawable.TOP_START

        Log.e("badge", "height:" + badge.intrinsicHeight)

        val badgeDrawableClass = BadgeDrawable::class.java

        val badgeBoundsField = badgeDrawableClass.getDeclaredField("badgeBounds")
        badgeBoundsField.isAccessible = true
//        val badgeBounds = badgeBoundsField.get(badge) as Rect
//        // (-57, -24 - 18, 24)
//        badgeBounds.top += 4
//        badgeBounds.bottom += 4
        Log.e("badge badgeBounds2", badgeBoundsField.get(badge).toString())
        val rect = badgeBoundsField.get(badge) as Rect

        val badgeCenterXField = badgeDrawableClass.getDeclaredField("badgeCenterX")
        badgeCenterXField.isAccessible = true
        val badgeCenterX = badgeCenterXField.get(badge)
        Log.e("badge badgeCenterX", badgeCenterX.toString())

        val badgeCenterYField = badgeDrawableClass.getDeclaredField("badgeCenterY")
        badgeCenterYField.isAccessible = true

//        Log.e("badge badgeCenterY1", badgeCenterYField.get(badge).toString())
//        badgeCenterYField.set(badge, 4)
//        Log.e("badge badgeCenterY2", badgeCenterYField.get(badge).toString())

        val halfBadgeWidthField = badgeDrawableClass.getDeclaredField("halfBadgeWidth")
        halfBadgeWidthField.isAccessible = true
        val halfBadgeWidth = halfBadgeWidthField.get(badge)
        Log.e("badge halfBadgeWidth", halfBadgeWidth.toString())

        val halfBadgeHeightField = badgeDrawableClass.getDeclaredField("halfBadgeHeight")
        halfBadgeHeightField.isAccessible = true
        val halfBadgeHeight = halfBadgeHeightField.get(badge)
        Log.e("badge halfBadgeHeight", halfBadgeHeight.toString())

        BadgeUtils.updateBadgeBounds(
            rect, badgeCenterX as Float, 10f,
            halfBadgeWidth as Float,
            halfBadgeHeight as Float
        )

        val shapeDrawableField = badgeDrawableClass.getDeclaredField("shapeDrawable")
        shapeDrawableField.isAccessible = true
        (shapeDrawableField.get(badge) as MaterialShapeDrawable).bounds = badgeBoundsField.get(badge) as Rect

        badge.invalidateSelf()
        Log.e("badge badgeBounds3", badgeBoundsField.get(badge).toString())

        badge.setVisible(true,false)
    }
}
