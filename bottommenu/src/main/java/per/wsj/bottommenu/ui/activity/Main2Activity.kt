package per.wsj.bottommenu.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main2.*
import per.wsj.bottommenu.R

class Main2Activity : AppCompatActivity() {

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

//        when (it.itemId) {
//            R.id.navigation_square -> {
//                viewPager.currentItem = 0
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.navigation_star -> {
//                viewPager.currentItem = 1
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.navigation_me -> {
//                viewPager.currentItem = 2
//                return@OnNavigationItemSelectedListener true
//            }
//        }
    }
}
