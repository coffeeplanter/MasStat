package ru.coffeeplanter.masstat;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import goldzweigapps.tabs.Builder.EasyTabsBuilder;
import goldzweigapps.tabs.Colors.EasyTabsColors;
import goldzweigapps.tabs.Items.TabItem;
import goldzweigapps.tabs.View.EasyTabs;
import ru.coffeeplanter.masstat.ui.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EasyTabs mTabs;
    ViewPager.OnPageChangeListener mOnPageChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTabs = (EasyTabs) findViewById(R.id.easytabs);
        createTabs();


        // This commented code will be used to add statistics view feature.
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    // This listener is used for correct removing Keywords tab.
//    private void initPageChangeListener() {
//        mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//            @Override
//            public void onPageSelected(final int position) {
//                if ((mTabs.getViewPager().getAdapter().getCount() == 3) && (position < 2)) {
//                    mTabs.getViewPager().removeOnPageChangeListener(this);
//                    createTabs(false);
//                    //noinspection ConstantConditions
//                    mTabs.getTabLayout().getTabAt(position).select();
//                    final TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
//                        @Override
//                        public void onTabSelected(TabLayout.Tab tab) {
//                            mTabs.getTabLayout().removeOnTabSelectedListener(this);
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mTabs.getViewPager().setCurrentItem(position, false);
//                                }
//                            }, 10);
//                        }
//                        @Override
//                        public void onTabUnselected(TabLayout.Tab tab) {
//                        }
//                        @Override
//                        public void onTabReselected(TabLayout.Tab tab) {
//                        }
//                    };
//                    mTabs.getTabLayout().addOnTabSelectedListener(tabSelectedListener);
//                    new Handler().postDelayed(new Runnable(){
//                        @Override
//                        public void run(){
//                            mTabs.getTabLayout().removeOnTabSelectedListener(tabSelectedListener);
//                        }
//                    }, 100);
//                }
//            }
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        };
//    }

    // Method for creating EasyTabs.
    private void createTabs() {
        List<TabItem> tabItems = new ArrayList<>();
        tabItems.add(new TabItem(new SiteListFragment(), "Sites"));
        tabItems.add(new TabItem(new PersonListFragment(), "Persons"));
        EasyTabsBuilder builder = EasyTabsBuilder
                .with(mTabs)
                .setTabsBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setTextColors(EasyTabsColors.White, EasyTabsColors.Silver);
        for (TabItem tabItem : tabItems) {
            builder.addTabs(tabItem);
        }
        builder.Build();
    }

    // NavigationView callback for adding statistics view feature in the future.
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
