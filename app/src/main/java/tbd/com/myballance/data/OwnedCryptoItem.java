package tbd.com.myballance.data;

/**
 * Created by todorbachvarov on 17.12.17.
 */

public class OwnedCryptoItem {
    double mValue;
    String mCurrency;

    public OwnedCryptoItem(String currency, Double value){
        mValue = value;
        mCurrency = currency;
    }

    public double getValue() {
        return mValue;
    }

    public String getCurrency() {
        return mCurrency;
    }

//    private FreshCryptoState optFreshState(final RefreshListener listener){
//        if(freshState == null /* TODO check for timestamp */){
//            refreshState(new RefreshListener(){
//                @Override
//                public void onRefreshDone(FreshCryptoState freshState) {
//                    //TODO setter
//                    OwnedCryptoItem.this.freshState = freshState;
//                    listener.onRefreshDone(freshState);
//                }
//            });
//            return null;
//        }
//
//        //its fresh enough
//        return freshState;
//    }

    //TMP
    FreshCryptoState getMocFreshState(){
        double btcUsd = 1920.00;
        double ethUsd = 760.00;
        FreshCryptoState mocState = new FreshCryptoState();
        mocState.setPriceUsd(String.valueOf(this.getCurrency().equalsIgnoreCase("btc")? btcUsd : ethUsd));

        return mocState;
    }

    //TODO pass Owned Item
    public static abstract class  RefreshListener{
        public abstract  void onRefreshDone(OwnedCryptoItem freshState);
    }

}
