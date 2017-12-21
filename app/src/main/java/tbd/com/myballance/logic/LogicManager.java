package tbd.com.myballance.logic;

import org.json.JSONArray;

import java.util.ArrayList;

import tbd.com.myballance.data.CoinMarketStatistics;

/**
 * Created by todorbachvarov on 17.12.17.
 */

public class LogicManager {
    public static LogicManager sInstance;

    private ArrayList<LogicListener> mListeners;
    private CoinMarketStatistics mCoinMarketStatistics;

    public static LogicManager initInstace(){
        sInstance = new LogicManager();
        return sInstance;
    }

    public static LogicManager getInstance(){
        return sInstance  ;
    }

    public CoinMarketStatistics getCoinMarketStatistics(){
        return mCoinMarketStatistics;
    }

    public LogicManager(){
        mListeners = new ArrayList<>();
    }

    public void addListener(LogicListener listener){
        mListeners.add(listener);
    }

    public void refreshMarker(){
        RequestExecutor.getInstance().loadMarket(50, new RequestExecutor.RequestListener() {
            @Override
            public void onSuccess(Object result) {
                if(result instanceof JSONArray){
                    mCoinMarketStatistics = CoinMarketStatistics.load((JSONArray) result);
                    for(LogicListener each: mListeners){
                        each.onCapitalUpdater();
                    }
                }
            }
        });

    }

    public static abstract class LogicListener{
        public abstract void onCapitalUpdater();
    }

}
