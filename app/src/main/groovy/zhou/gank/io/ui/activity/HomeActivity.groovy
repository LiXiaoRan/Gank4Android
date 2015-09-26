package zhou.gank.io.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import groovy.transform.CompileStatic
import zhou.gank.io.R
import zhou.gank.io.comment.Config
import zhou.gank.io.ui.dialog.InfoDialog
import zhou.gank.io.ui.fragment.DailyFragment
import zhou.gank.io.util.Notifier
import zhou.gank.io.util.TextKit

@CompileStatic
class HomeActivity extends AppCompatActivity implements Notifier {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CoordinatorLayout coordinatorLayout;

    private DailyFragment dailyFragment;
    private Fragment currFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();

        dailyFragment = DailyFragment.newInstance(2015, 9, 25)

        add(dailyFragment)
    }

    def initView() {
        drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        navigationView = findViewById(R.id.nav_view) as NavigationView
        coordinatorLayout = findViewById(R.id.container) as CoordinatorLayout

        navigationView.setNavigationItemSelectedListener({ item ->
            def i = item as MenuItem
            drawerLayout.closeDrawers()
            switch (i.getItemId()) {
                case R.id.nav_daily:
                    replace(dailyFragment)
                    return true
                case R.id.nav_type:
                    Intent intent = new Intent(this, TabActivity.class)
                    intent.putExtra(Config.Static.IS_RANDOM, false)
                    startActivity(intent)
                    return true
                case R.id.nav_random:
                    Intent intent = new Intent(this, TabActivity.class)
                    intent.putExtra(Config.Static.IS_RANDOM, true)
                    startActivity(intent)
                    return true
                case R.id.nav_info:
                    def info = InfoDialog.newInstance(getString(R.string.nav_info), TextKit.getInfo())
                    info.show(getSupportFragmentManager(), "info")
                    return true
                case R.id.nav_setting:
                    def intent1 = new Intent(this, SettingActivity.class)
                    startActivity(intent1)
                    return true
            }
            return false
        })

    }

    def add(Fragment f) {
        getSupportFragmentManager().beginTransaction().add(R.id.container, f).commit();
        this.currFragment = f;
    }

    def replace(Fragment f) {
        if (currFragment == f) {
            return
        }
        coordinatorLayout.removeAllViews()
        getSupportFragmentManager().beginTransaction().remove(currFragment).add(R.id.container, f).commit()
        this.currFragment = f
    }

    @Override
    void notice(int noticeId) {
        switch (noticeId) {
            case Config.Action.OPEN_DRAWER_LAYOUT:
                drawerLayout.openDrawer(GravityCompat.START)
                break
        }
    }
}