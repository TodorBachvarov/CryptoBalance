package tbd.com.myballance.data;

import android.util.Log;

import org.json.JSONObject;

import tbd.com.myballance.SettingsManager;

/**
 * Created by todorbachvarov on 17.12.17.
 */

//Loaded state from external Api (cryptomarketcap.com)
public class CoinMarketStateInfo {
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
    private double changeLasHour;
    private double changeLasDay;
    private double changeLasWeek;

    public static CoinMarketStateInfo parce(JSONObject state) {
        CoinMarketStateInfo stateResult = new CoinMarketStateInfo();
        if (state != null) {
            stateResult.setId(state.optString("id", null));
            stateResult.setmName(state.optString("name", null));
            stateResult.setSymbol(state.optString("symbol", null));
            stateResult.setPriceUsd(state.optString("price_usd", null));
            stateResult.setPriceBtc(state.optString("price_btc", null));
            stateResult.setPriceEur(state.optString("price_eur", null));
            String percentageChangeHour = state.optString("percent_change_1h", null);
            String percentageChangeDay = state.optString("percent_change_24h", null);
            String percentageChangeWeek = state.optString("percent_change_7d", null);
            stateResult.setChangeLastHour(percentageChangeHour!=null ? Double.valueOf(percentageChangeHour):0.0);
            stateResult.setChangeLastDay(percentageChangeDay!=null ? Double.valueOf(percentageChangeDay):0.0);
            stateResult.setChangeLastWeek(percentageChangeWeek!=null ? Double.valueOf(percentageChangeWeek):0.0);
        }

        return stateResult;
    }

    public CoinMarketStateInfo(){

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

    public void setChangeLastHour(double percentage) {
        changeLasHour = percentage;
    }
    public void setChangeLastDay(double percentage) {
        changeLasDay = percentage;
    }
    public void setChangeLastWeek(double percentage) {
        changeLasWeek = percentage;
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

    //Change
    public double getChange(int interval){
        switch (interval){
            case (SettingsManager.TimeInterval.DAY_INTERVAL):return getChangeLasDay();
            case (SettingsManager.TimeInterval.WEEK_INTERVAL):return getChangeLasWeek();

//            (SettingsManager.TimeInterval.HOUR_INTERVAl)
            default: return getChangeLasHour();
        }
    }

    private double getChangeLasHour() {
        return changeLasHour;
    }
    private double getChangeLasDay() {
        return changeLasDay;
    }
    private double getChangeLasWeek() {
        return changeLasWeek;
    }

    public double getPriceInCrypto(String cryptoBasis, CoinMarketStatistics statistics) {
        double result = 0;
        CoinMarketStateInfo basisInfo = statistics.get(SettingsManager.getInstance().getCryptoIdByName(cryptoBasis));
        if(basisInfo==null){
            Log.e("getPriceInCrypto","Missing Info for :"+cryptoBasis);
            return result;
        }
        double itemPriceInBTC = getPriceUsd();
        double basisPriceInBTC = basisInfo.getPriceUsd();
        result = itemPriceInBTC!=0 && basisPriceInBTC!=0 ? itemPriceInBTC/basisPriceInBTC : result;

        return result ;
    }
}
