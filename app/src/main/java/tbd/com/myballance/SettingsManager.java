package tbd.com.myballance;

import java.util.HashMap;

import tbd.com.myballance.data.OwnedCryptoItem;
import tbd.com.myballance.logic.WalletManager;

/**
 * Created by todorbachvarov on 17.12.17.
 */

public class SettingsManager {
    private static SettingsManager sInstance;

    public static HashMap<String,String> sCryptoCurrencyIDs = new HashMap<>();

    public static SettingsManager getInstance(){
        return sInstance;
    }

    public static void initInstance(){
        sInstance = new SettingsManager();
        initCryptoIds();
    }

    public String getCryptoIdByName(String name){
        return sCryptoCurrencyIDs.get(name);
    }

    private static void initCryptoIds() {
        sCryptoCurrencyIDs.put("btc","bitcoin");
        sCryptoCurrencyIDs.put("eth","ethereum");
        sCryptoCurrencyIDs.put("etc","ethereum-classic");
        sCryptoCurrencyIDs.put("ltc","litecoin");
        sCryptoCurrencyIDs.put("dash","dash");
        sCryptoCurrencyIDs.put("iota","iota");
    }

    public static WalletManager.Balance getBalance(){
        //TODO Load from DB / SharedPrefs/ Back end
        WalletManager.Balance mocBalance = new WalletManager.Balance();
        mocBalance.add(new OwnedCryptoItem("btc",0.0637));
        mocBalance.add(new OwnedCryptoItem("eth", 2.01));
        mocBalance.add(new OwnedCryptoItem("etc", 3.2));
        mocBalance.add(new OwnedCryptoItem("dash", 0.3690));
        mocBalance.add( new OwnedCryptoItem("ltc", 1.009));
        mocBalance.add( new OwnedCryptoItem("iota", 104.0));

        return  mocBalance;
    }
}
