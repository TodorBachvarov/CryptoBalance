package tbd.com.myballance.logic;

import android.content.Context;

import java.util.ArrayList;

import tbd.com.myballance.SettingsManager;
import tbd.com.myballance.data.CoinMarketStatistics;
import tbd.com.myballance.data.FreshCryptoState;
import tbd.com.myballance.data.OwnedCryptoItem;

/**
 * Created by todorbachvarov on 17.12.17.
 */

//This will represent our wallet (owned values)
public class WalletManager {
    static WalletManager mInstence;
    Balance mCryptoBalance;
    Context context;

    WalletManager(Context context, WalletManagerListener listener){
        this.context = context;
        mInstence = this;
        loadBalance(listener);
    }

    public static void initWalletManager(Context context, WalletManagerListener listener){
        if(mInstence==null)
            new WalletManager(context, listener);
    }

    public static WalletManager getInstance(){
        return mInstence;
    }

    private void loadBalance(WalletManagerListener listener){
        mCryptoBalance = SettingsManager.getBalance();
        listener.onBalanceLoaded(mCryptoBalance);
    }

    //TODO remove when loadingin is done
    public Balance getCryptoBalance(){
        return SettingsManager.getBalance();
    }

    public Balance getBalance(){
        return mCryptoBalance;
    }

    public static abstract class WalletManagerListener {
        public abstract void onBalanceLoaded(Balance valance);
    }

    public static class Balance extends ArrayList<OwnedCryptoItem>{

        public double getTotalEUR(CoinMarketStatistics statistics){
            return getStatistics(statistics, new Listener() {
                @Override
                public double getTotal(double value, FreshCryptoState stat) {
                    if(stat!=null && value>0)
                        return stat.getPriceEur()*value;
                    else
                        return 0;
                }
            });
        }

        public double getTotalUSD(CoinMarketStatistics statistics){
            return getStatistics(statistics, new Listener() {
                @Override
                public double getTotal(double value, FreshCryptoState stat) {
                    if(stat!=null && value>0)
                        return stat.getPriceUsd()*value;
                    else
                        return 0;
                }
            });
        }

        public double getTotalBtc(CoinMarketStatistics statistics) {
            return getStatistics(statistics, new Listener() {
                @Override
                public double getTotal(double value, FreshCryptoState stat) {
                    if(stat!=null && value>0)
                        return stat.getPriceBtc()*value;
                    else
                        return 0;
                }
            });
        }

        public double getTotalBGN(CoinMarketStatistics statistics){
            double euroToBGN = 1.95652967;
            return getTotalEUR(statistics)* euroToBGN;
        }

        private double getStatistics(CoinMarketStatistics statistics,Listener listener){
            double total = 0.00;

            for(OwnedCryptoItem each : this){
                String currency = each.getCurrency();
                double value = each.getValue();
                FreshCryptoState criptoStat = statistics.get(SettingsManager.getInstance().getCryptoIdByName(currency));
                total+=listener.getTotal(value,criptoStat);
            }

            return total;
        }

        private abstract static class Listener{
            public abstract double getTotal(double value,FreshCryptoState stat);
        }
    }



}
