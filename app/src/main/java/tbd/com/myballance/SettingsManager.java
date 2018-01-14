package tbd.com.myballance;

import java.util.ArrayList;
import java.util.HashMap;

import tbd.com.myballance.data.OwnedCryptoItem;
import tbd.com.myballance.logic.WalletManager;

/**
 * Created by todorbachvarov on 17.12.17.
 */

public class SettingsManager {
    public static String CRIPTO_BASIS = "BTC";
    public static int CRIPTO_INTERVAL = TimeInterval.HOUR_INTERVAl;
    public static ArrayList<String> BASIS_ORDER ;
    private static SettingsManager sInstance;

    public static HashMap<String,String> sCryptoCurrencyIDs = new HashMap<>();

    public static SettingsManager getInstance(){
        return sInstance;
    }

    public static void initInstance(){
        sInstance = new SettingsManager();
        initCryptoIds();
        initCriptoBasisOrder();
    }

    public String getCryptoIdByName(String name){
        return sCryptoCurrencyIDs.get(name);
    }

    private static void initCryptoIds() {
        sCryptoCurrencyIDs.put("BTC","bitcoin");
        sCryptoCurrencyIDs.put("BCH","bitcoin-cash");
        sCryptoCurrencyIDs.put("BTG","bitcoin-gold");
        sCryptoCurrencyIDs.put("ETH","ethereum");
        sCryptoCurrencyIDs.put("ETC","ethereum-classic");
        sCryptoCurrencyIDs.put("LTC","litecoin");
        sCryptoCurrencyIDs.put("DASH","dash");
        sCryptoCurrencyIDs.put("IOTA","iota");
        sCryptoCurrencyIDs.put("XRP","ripple");
        sCryptoCurrencyIDs.put("ADA","cardano");
        sCryptoCurrencyIDs.put("ZEC","zcash");
        sCryptoCurrencyIDs.put("EOS","eos");
        sCryptoCurrencyIDs.put("XMR","monero");
        sCryptoCurrencyIDs.put("SNT","status");
        sCryptoCurrencyIDs.put("XLM","stellar");
        sCryptoCurrencyIDs.put("TRX","tron");
        sCryptoCurrencyIDs.put("NEO","neo");
    }

    private static void initCriptoBasisOrder(){
        BASIS_ORDER = new ArrayList<>();
        BASIS_ORDER.add("BTC");
        BASIS_ORDER.add("BCH");
        BASIS_ORDER.add("BTG");
        BASIS_ORDER.add("ETH");
        BASIS_ORDER.add("ETC");
        BASIS_ORDER.add("LTC");
        BASIS_ORDER.add("DASH");
        BASIS_ORDER.add("IOTA");
        BASIS_ORDER.add("XRP");
        BASIS_ORDER.add("ADA");
        BASIS_ORDER.add("ZEC");
    }

    public static String switchCriptoBasis(){
        int currentIndex = BASIS_ORDER.indexOf(CRIPTO_BASIS);
        CRIPTO_BASIS = currentIndex<BASIS_ORDER.size()-1? BASIS_ORDER.get(currentIndex+1):BASIS_ORDER.get(0);
        return  CRIPTO_BASIS;
    }

    public static WalletManager.Balance getBalance(){
        //TODO Load from DB / SharedPrefs/ Back end
        WalletManager.Balance mocBalance = new WalletManager.Balance();
        mocBalance.add(new OwnedCryptoItem("BTC",   0.073));
        mocBalance.add(new OwnedCryptoItem("BTC",   0.0499));
        mocBalance.add(new OwnedCryptoItem("ETH",   2.01));
//        mocBalance.add(new OwnedCryptoItem("DASH",  0.28406443));
        mocBalance.add(new OwnedCryptoItem("DASH",  0.00106));
        mocBalance.add(new OwnedCryptoItem("LTC",   0.51142));
//        mocBalance.add(new OwnedCryptoItem("LTC",   1.189));
//        mocBalance.add(new OwnedCryptoItem("LTC",   0.42443137 ));
//        mocBalance.add(new OwnedCryptoItem("LTC",   0.00492598 ));
        mocBalance.add(new OwnedCryptoItem("IOTA",  72.0));
        mocBalance.add(new OwnedCryptoItem("ADA",   152.8470));
//        mocBalance.add(new OwnedCryptoItem("ETC",   3.1));
        mocBalance.add(new OwnedCryptoItem("ETC",   0.361));
        mocBalance.add(new OwnedCryptoItem("XRP",   0.07000000));
//        mocBalance.add(new OwnedCryptoItem("BTC",   0.00233239));
//        mocBalance.add(new OwnedCryptoItem("ETH",   0.04302029));
//        mocBalance.add(new OwnedCryptoItem("IOTA",   17.01));
//        mocBalance.add(new OwnedCryptoItem("NEO",   0.4389825));
        mocBalance.add(new OwnedCryptoItem("SNT",   104.0));
//        mocBalance.add(new OwnedCryptoItem("XLM",   54.0));
        mocBalance.add(new OwnedCryptoItem("XLM",   0.94));
        mocBalance.add(new OwnedCryptoItem("TRX",   793.423));
//        mocBalance.add(new OwnedCryptoItem("TRX",   217.00));
        mocBalance.add(new OwnedCryptoItem("XMR",   0.10989869));
//        mocBalance.add(new OwnedCryptoItem("EOS",   4.0));
        mocBalance.add(new OwnedCryptoItem("EOS",   7.76598905));

        mocBalance.add(new OwnedCryptoItem("BCH",   0.0000001));
        mocBalance.add(new OwnedCryptoItem("BTG",   0.0000001));
        mocBalance.add(new OwnedCryptoItem("ZEC",   0.0000001));



        return  mocBalance;
    }

    public static class TimeInterval{
        public final static int HOUR_INTERVAl=0;
        public final static int DAY_INTERVAL=1;
        public final static int WEEK_INTERVAL=2;

        public static String getLabel(int interval){
            switch (interval){
                case DAY_INTERVAL: return "DAY";
                case WEEK_INTERVAL: return "WEEK";

                default : return "HOUR";
            }
        }
    }
}
