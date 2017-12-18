package tbd.com.myballance;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import tbd.com.myballance.layout.TabManager;
import tbd.com.myballance.logic.LogicManager;
import tbd.com.myballance.logic.RequestExecutor;

public class MainActivity extends AppCompatActivity {

    TabManager mTabManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View placeholder = findViewById(R.id.fragment_placeholder);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        RequestExecutor.initInstance(getBaseContext());

        LogicManager.initInstace();
        SettingsManager.initInstance();

        mTabManager = TabManager.init(getSupportFragmentManager(),navigation,placeholder);
    }



}
