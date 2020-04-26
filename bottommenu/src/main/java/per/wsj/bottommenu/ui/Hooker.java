package per.wsj.bottommenu.ui;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Field;

public class Hooker {
    /**
     * 文字下移
     *
     * @param menu
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void hookBnv(BottomNavigationView menu) {
        try {
            Class<BottomNavigationView> bottomNavigationViewClass = BottomNavigationView.class;
            Field menuViewField = bottomNavigationViewClass.getDeclaredField("menuView");
            menuViewField.setAccessible(true);
            Object menuView = menuViewField.get(menu);

            Class<BottomNavigationMenuView> bottomNavigationMenuViewClass = BottomNavigationMenuView.class;
            Field buttonsField = bottomNavigationMenuViewClass.getDeclaredField("buttons");
            buttonsField.setAccessible(true);
            Object[] buttons = (Object[]) buttonsField.get(menuView);
            for (Object button : buttons) {
                Class<BottomNavigationItemView> bottomNavigationItemViewClass = BottomNavigationItemView.class;
                Field smallLabelField = bottomNavigationItemViewClass.getDeclaredField("smallLabel");
                smallLabelField.setAccessible(true);
                TextView smallLabel = (TextView) smallLabelField.get(button);

                // 方式一:
//                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) ((ViewGroup) smallLabel.getParent()).getLayoutParams();
//                layoutParams.bottomMargin = -15;
//                ((ViewGroup) smallLabel.getParent()).setLayoutParams(layoutParams);
                // 方式二:
                ((ViewGroup) smallLabel.getParent()).scrollBy(0, -20);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
