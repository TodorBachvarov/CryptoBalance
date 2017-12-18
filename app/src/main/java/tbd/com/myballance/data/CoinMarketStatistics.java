package tbd.com.myballance.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by todorbachvarov on 17.12.17.
 */

public class CoinMarketStatistics extends HashMap<String,FreshCryptoState> {

    public static CoinMarketStatistics load(JSONArray responce){
        CoinMarketStatistics result = new CoinMarketStatistics();
        if(responce!=null && responce.length()>0){
            for(int i=0 ; i <responce.length() ; i++){
                JSONObject criptoData = responce.optJSONObject(i);
                if(criptoData != null){
                    result.put(criptoData.optString("id"),FreshCryptoState.parce(criptoData));
                }
            }
        }

        return result;
    }

}
