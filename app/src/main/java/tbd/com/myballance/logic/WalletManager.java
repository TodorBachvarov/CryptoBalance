package tbd.com.myballance.logic;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import tbd.com.myballance.SettingsManager;
import tbd.com.myballance.data.CoinMarketStatistics;
import tbd.com.myballance.data.CoinMarketStateInfo;
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
        mCryptoBalance = getCryptoBalanceMOC();
        listener.onBalanceLoaded(mCryptoBalance);
    }

    //TODO remove when loadingin is done
    private Balance getCryptoBalanceMOC(){
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
                public double getTotal(double value, CoinMarketStateInfo stat) {
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
                public double getTotal(double value, CoinMarketStateInfo stat) {
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
                public double getTotal(double value, CoinMarketStateInfo stat) {
                    if(stat!=null && value>0)
                        return stat.getPriceBtc()*value;
                    else
                        return 0;
                }
            });
        }

        public OwnedCryptoItem getItemById(String id){
            for (OwnedCryptoItem each: this){
                if(each.getCurrency().equals(id))
                    return each;
            }
            return null;
        }

        public double getTotalInCripto(final CoinMarketStatistics statistics, final String criptoId){
            return getStatistics(statistics, new Listener() {
                @Override
                public double getTotal(double value, CoinMarketStateInfo stat) {
                    if(stat!=null && value>0) {
                        CoinMarketStateInfo basisCoin = statistics.get(criptoId);
                        double basisInBtc = 0.0;
                        if(basisCoin!=null){
                            basisInBtc = basisCoin.getPriceBtc();
                        } else {
                            Log.e("WalletManager","Wrong id: "+ criptoId );
                        }
                        return (stat.getPriceBtc()/basisInBtc) * value;
                    }else
                        return 0;
                }
            });
        }

        public double getTotalBGN(CoinMarketStatistics statistics){
            double euroToBGN = 1.95652967;
            return getTotalEUR(statistics)* euroToBGN;
        }

        public double getTotalVelocityUSD(CoinMarketStatistics statistics, final double total, final int interval){
            return getStatistics(statistics, new Listener() {
                @Override
                public double getTotal(double value, CoinMarketStateInfo stat) {
                    if(stat!=null && value>0)
                        return ((stat.getPriceUsd()*value)/total) * (stat.getChange(interval));
                    else
                        return 0;
                }
            });
        }

        public double getTotalVelocityCripto(final CoinMarketStatistics statistics, final double totalInBTC, final String criptoId, final OwnedCryptoItem ownedBasisCripto){
            return getStatistics(statistics, new Listener() {
                @Override
                public double getTotal(OwnedCryptoItem item, CoinMarketStateInfo stat) {
                    if(stat!=null && item!=null && item.getValue()>=0 ) {
                        CoinMarketStateInfo basisCoin = statistics.get(criptoId);
                        double basisWeight = getPercentageWeight(ownedBasisCripto, basisCoin, totalInBTC, false);
                        if(!stat.getId().equals(criptoId)) {
                            double value = item.getValue();

                            double basisInBtc = basisCoin.getPriceBtc();
                            double itemPriceInBTC = stat.getPriceBtc();
                            double itemPriceInBasis = itemPriceInBTC / basisInBtc;

                            double itemWeight = getPercentageWeight(item, stat, totalInBTC, false);

                            //How much we gain from this stat - the oportunity if this weight was in Basis cripto (- what would be my price if this lite coin was in ether (basis = ether))
                            return (itemWeight * stat.getChange(SettingsManager.getInstance().CRIPTO_INTERVAL) / 100) - (itemWeight * basisCoin.getChange(SettingsManager.getInstance().CRIPTO_INTERVAL)/100);
                                    //Other representation - the difference between tha changes (coin - basis) and how much value it reflects - in percentage
                                    //(stat.getChangeLasHour() - basisCoin.getChangeLasHour())* itemWeight /100 /*to percentage*/
                        } else {
                            //The ratio is not changed from the BasisItem.value - We calculate how the rest of the balance is reflected from the current change
                            // (the Eth is Already an eth so if we calculate in ETH basis it's value won't change - we still have 2 eth  no matter how expensive it becomes :))
                            double percentageOfCapitalWithoutBasis = 100 - basisWeight;

//                            return basisWeight>0? -((basisWeight/100) * basisCoin.getChangeLasHour()):  -basisCoin.getChangeLasHour();
                        }
                    }
                    return 0;
                }
            });
        }
        public double getPercentageWeight(OwnedCryptoItem item, CoinMarketStateInfo info, double total ){
            return getPercentageWeight(item,info,total,true);
        }

        public double getPercentageWeight(OwnedCryptoItem item, CoinMarketStateInfo info, double total,boolean fiat ){
            if(total <= 0 )
                return 0;

            double totalPrice = item.getValue() * (fiat ? info.getPriceUsd() : info.getPriceBtc());
            return totalPrice/total * 100;
        }

        private double getStatistics(CoinMarketStatistics statistics,Listener listener){
            double total = 0.00;

            for(OwnedCryptoItem each : this){
                String currency = each.getCurrency();
                double value = each.getValue();
                CoinMarketStateInfo criptoStat = statistics.get(SettingsManager.getInstance().getCryptoIdByName(currency));
                //Implement one of them
                total+=listener.getTotal(value,criptoStat);
                total+=listener.getTotal(each,criptoStat);


            }

            return total;
        }



        private abstract static class Listener{
            public  double getTotal(double value,CoinMarketStateInfo stat){
                return 0;
            }
            public  double getTotal(OwnedCryptoItem item,CoinMarketStateInfo stat){
                return 0;
            }
        }
    }



}
