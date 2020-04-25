package per.wsj.bottommenu.ui.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_LABELED
import kotlinx.android.synthetic.main.activity_main2.*
import per.wsj.bottommenu.R
import per.wsj.bottommenu.ui.Hooker

class Main2Activity : AppCompatActivity() {

    private var show = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        nav_view.setOnNavigationItemReselectedListener(BottomNavigationView.OnNavigationItemReselectedListener {
            Log.e("bottomMenuView:", it.itemId.toString())
        })

        nav_view.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    Log.e("bottomMenuView:", "home")
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    Log.e("bottomMenuView:", "dashboard")
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    Log.e("bottomMenuView:", "notification")
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })

//        nav_view.labelVisibilityMode = LABEL_VISIBILITY_LABELED
//        nav_view.menu.findItem(R.id.navigation_test).isVisible = false
//
//        btnHideItem.setOnClickListener {
//            nav_view.menu.findItem(R.id.navigation_test).isVisible = !show
//            show = !show
//            btnHideItem.text = if (show) "Hide" else "Show"
//        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Hooker.hookBnv(nav_view)
//        }
    }
}
