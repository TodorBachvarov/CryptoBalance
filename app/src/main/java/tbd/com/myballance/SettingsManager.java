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
        sCryptoCurrencyIDs.put("XPR","ripple");
        sCryptoCurrencyIDs.put("ADA","cardano");
        sCryptoCurrencyIDs.put("ZEC","zcash");
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
        BASIS_ORDER.add("XPR");
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
        mocBalance.add(new OwnedCryptoItem("BTC",0.11401228));
        mocBalance.add(new OwnedCryptoItem("ETH", 2.01));
        mocBalance.add( new OwnedCryptoItem("IOTA", 104.0));
        mocBalance.add( new OwnedCryptoItem("LTC", 1.61142376));
        mocBalance.add(new OwnedCryptoItem("DASH", 0.36906672));
        mocBalance.add(new OwnedCryptoItem("ETC", 3.2));

        mocBalance.add(new OwnedCryptoItem("BCH", 0.0000001));
        mocBalance.add(new OwnedCryptoItem("BTG", 0.0000001));
        mocBalance.add(new OwnedCryptoItem("ZEC", 0.0000001));
        mocBalance.add(new OwnedCryptoItem("ADA", 0.0000001));
        mocBalance.add(new OwnedCryptoItem("XPR", 0.0000001));

        return  mocBalance;
    }
}
