package tbd.com.myballance.layout;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.view.View;

import tbd.com.myballance.R;
import tbd.com.myballance.SettingsManager;
import tbd.com.myballance.layout.Tabs.WalletTab;

/**
 * Created by todorbachvarov on 17.12.17.
 */

public class TabManager {
    private static int DEFAULT_TAB_SELECTION = R.id.navigation_home;
    FragmentManager mFragmentManager;
    View mPlaceHolder ;

    public static TabManager init(FragmentManager context, BottomNavigationView navigationView, View placeholder){
        TabManager manager = new TabManager(context,placeholder);
        navigationView.setOnNavigationItemSelectedListener(manager.getNavigationLIstener());
        manager.makeTransition(DEFAULT_TAB_SELECTION);
        return manager;
    }

    public TabManager(FragmentManager fragmentManager, View placeholder){
        mFragmentManager = fragmentManager;
        mPlaceHolder = placeholder;
//        transit(new WalletTab());
    }

    protected void refresh(){
        transit(new WalletTab());
    }

    //Hook Fragments
    protected boolean makeTransition(int tabId){
        switch (tabId) {
                case R.id.navigation_home:
                    transit(new WalletTab());
                    return true;
//                case R.id.navigation_dashboard:
//                    transit(new DashboardTab());
//                    return true;
                case R.id.navigation_set_interval:

                    SettingsManager.getInstance().CRIPTO_INTERVAL = (SettingsManager.getInstance().CRIPTO_INTERVAL + 1)%3;
                    refresh();
                    return true;
//
//                    return true;
        }

        return false;
    }

    protected void transit(Fragment fragment){
        if(mFragmentManager==null)
            return;
        // Begin the transaction
        android.support.v4.app.FragmentTransaction ft = mFragmentManager.beginTransaction();
// Replace the contents of the container with the new fragment
        ft.replace(mPlaceHolder.getId(), fragment);
// or ft.add(R.id.your_placeholder, new FooFragment());
// Complete the changes added above
        ft.commit();
    }

    //Navigation Listener
    protected BottomNavigationView.OnNavigationItemSelectedListener getNavigationLIstener(){
        return mOnNavigationItemSelectedListener;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return makeTransition(item.getItemId());

        }
    };


}
