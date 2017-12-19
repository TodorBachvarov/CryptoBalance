package tbd.com.myballance.data;

import org.json.JSONObject;

/**
 * Created by todorbachvarov on 17.12.17.
 */

//Loaded state from external Api (cryptomarketcap.com)
public class FreshCryptoState {
/*    {
        "id": "bitcoin",
            "name": "Bitcoin",
            "symbol": "BTC",
            "rank": "1",
            "price_usd": "573.137",
            "price_btc": "1.0",
            "24h_volume_usd": "72855700.0",
            "market_cap_usd": "9080883500.0",
            "available_supply": "15844176.0",
            "total_supply": "15844176.0",
            "percent_change_1h": "0.04",
            "percent_change_24h": "-0.3",
            "percent_change_7d": "-0.57",
            "last_updated": "1472762067"
    },*/
    private String id;
    private String mName;
    private String symbol;
    private String priceUsd;
    private String PriceBtc;
    private String priceEur;

    public static FreshCryptoState parce(JSONObject state) {
        FreshCryptoState stateResult = new FreshCryptoState();
        if (state != null) {
            stateResult.setId(state.optString("id", null));
            stateResult.setmName(state.optString("name", null));
            stateResult.setSymbol(state.optString("symbol", null));
            stateResult.setPriceUsd(state.optString("price_usd", null));
            stateResult.setPriceBtc(state.optString("price_btc", null));
            stateResult.setPriceEur(state.optString("price_eur", null));
        }

        return stateResult;
    }

    public FreshCryptoState(){

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setPriceUsd(String priceUsd) {
        this.priceUsd = priceUsd;
    }

    public void setPriceBtc(String priceBtc) {
        PriceBtc = priceBtc;
    }

    public String getId() {
        return id;
    }

    public String getmName() {
        return mName;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPriceUsd() {
        return priceUsd!=null && !priceUsd.isEmpty() ? Double.valueOf(priceUsd) : -1;
    }

//    public String getPriceBtc() {
//        return PriceBtc;
//    }

    public double getPriceBtc() {
        return PriceBtc!=null && !PriceBtc.isEmpty() ? Double.valueOf(PriceBtc) : -1;
    }

    public double getPriceEur(){
        return priceEur!=null && !priceEur.isEmpty() ? Double.valueOf(priceEur) : -1;
    }


    public void setPriceEur(String priceEur) {
        this.priceEur = priceEur;
    }
}
