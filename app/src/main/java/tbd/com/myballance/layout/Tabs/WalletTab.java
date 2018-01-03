package tbd.com.myballance.layout.Tabs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tbd.com.myballance.R;
import tbd.com.myballance.SettingsManager;
import tbd.com.myballance.data.CoinMarketStatistics;
import tbd.com.myballance.data.CoinMarketStateInfo;
import tbd.com.myballance.data.OwnedCryptoItem;
import tbd.com.myballance.logic.LogicManager;
import tbd.com.myballance.logic.WalletManager;

/**
 * Created by todorbachvarov on 17.12.17.
 */

public class WalletTab extends MainTab {
    TextView       mTotaViewlUsd;
    TextView       mTotalVelocitylUsd;
    TextView       mTotaViewlBtc;
    TextView       mTotalVelocitylBtc;
    ListView      mListView;
    WalletAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.wallet_layout;
    }

    @Override
    protected void init(View rootView) {
        mListView = (ListView)rootView.findViewById(R.id.wallet_list);
        mTotaViewlUsd = rootView.findViewById(R.id.wallet_total_usd);
        mTotaViewlUsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchTotalFiatCurrency();
            }
        });
        mTotalVelocitylUsd = rootView.findViewById(R.id.wallet_total_velocity_usd);
        mTotalVelocitylBtc = rootView.findViewById(R.id.wallet_total_velocity_btc);
        mTotaViewlBtc = rootView.findViewById(R.id.wallet_total_btc);
        mTotaViewlBtc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchTotalCriptoCurrency();
            }
        });
        mAdapter = new WalletAdapter(getActivity(),R.layout.wallet_list_item ,new ArrayList<OwnedCryptoItem>());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mItemClickListener);
        LogicManager.getInstance().addListener(new LogicManager.LogicListener() {

            @Override
            public void onCapitalUpdater() {
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
                WalletManager.Balance balance = WalletManager.getInstance().getBalance();
                mAdapter.addAll(balance);
                mAdapter.notifyDataSetChanged();
                mListView.invalidate();
//                mListView.requestLayout();
//                mListView.setVisibility(View.VISIBLE);
                updateTotals();
            }
        });
        loadWallet();
    }

    private void loadWallet(){
        LogicManager.getInstance().refreshMarker();
        WalletManager.initWalletManager(getContext(), new WalletManager.WalletManagerListener() {
            @Override
            public void onBalanceLoaded(WalletManager.Balance valance) {
                onWalletItemsLoaded(valance);
            }
        });
    }

    private void onWalletItemsLoaded(ArrayList<OwnedCryptoItem> items){
        mAdapter.addAll(items);
        mAdapter.notifyDataSetChanged();
        updateTotals();
    }

    protected void updateTotals(){
        WalletManager.Balance balance = WalletManager.getInstance().getBalance();
        CoinMarketStatistics statistics = LogicManager.getInstance().getCoinMarketStatistics();
        if(balance!=null && statistics!=null) {
            if (mTotaViewlUsd != null) {
                //Total in usd
                double usdPrice = balance.getTotalUSD(statistics);
                mTotaViewlUsd.setText(usdPrice>1000?String.format("$ \n %,.0f", usdPrice):String.format("$\n %,.2f", usdPrice));

                //Click on the view to see the different currency value
//                //Total Eur
//                double eurPrice = balance.getTotalEUR(statistics);
//                mTotaViewlUsd.setText(String.format("€ \n%.2f", eurPrice));
//
//                //Total BGN
//                double bgnPrice = balance.getTotalBGN(statistics);
//                mTotaViewlUsd.setText(String.format("BGN \n%.2f", bgnPrice));

//                updatePercentChangeColor(mTotaViewlUsd,balance.getTotalVelocityUSD(statistics,balance.));

                if(mTotalVelocitylUsd!=null){
                    double velocityUsd = balance.getTotalVelocityUSD(statistics,usdPrice,SettingsManager.getInstance().CRIPTO_INTERVAL);
                    populateTotalVelocityText(mTotalVelocitylUsd,velocityUsd);
                    updatePercentChangeColor(mTotalVelocitylUsd,velocityUsd);
                }
            }

            String basisId = SettingsManager.sCryptoCurrencyIDs.get(SettingsManager.CRIPTO_BASIS);
            double totalBTC = balance.getTotalBtc(statistics);
            double totalBasis = balance.getTotalInCripto(statistics,basisId);
            double velocity = balance.getTotalVelocityCripto(statistics,totalBTC,basisId,balance.getItemById(SettingsManager.CRIPTO_BASIS));
            populateTotalBTCViews(totalBasis,velocity,SettingsManager.CRIPTO_BASIS);

        }
    }

    private void populateTotalBTCViews(double total,double velocity , String currency){
        if (mTotaViewlBtc != null) {
            mTotaViewlBtc.setText(currency+String.format("                  %.6f", total));
        }

        if(mTotalVelocitylBtc!=null){

           populateTotalVelocityText(mTotalVelocitylBtc,velocity);
            updatePercentChangeColor(mTotalVelocitylBtc,velocity);
        }
    }

    protected void switchTotalCriptoCurrency(){
        WalletManager.Balance balance = WalletManager.getInstance().getBalance();
        CoinMarketStatistics statistics = LogicManager.getInstance().getCoinMarketStatistics();
        if(mTotaViewlBtc != null && balance!=null && statistics!=null){
            String basis = SettingsManager.switchCriptoBasis();
            String basisId = SettingsManager.sCryptoCurrencyIDs.get(basis);
            double totalInBTC = balance.getTotalBtc(statistics);
            double totalInBasis = balance.getTotalInCripto(statistics,basisId);
            double velocity = balance.getTotalVelocityCripto(statistics,totalInBTC,basisId ,balance.getItemById(basis));

            populateTotalBTCViews(totalInBasis,velocity,basis);
            mAdapter.notifyDataSetChanged();
        }
    }

    protected void switchTotalFiatCurrency(){
        WalletManager.Balance balance = WalletManager.getInstance().getBalance();
        CoinMarketStatistics statistics = LogicManager.getInstance().getCoinMarketStatistics();
        if(mTotaViewlUsd != null && balance!=null && statistics!=null){
            String currentTotal = mTotaViewlUsd.getText()!=null ? mTotaViewlUsd.getText().toString():null;
            String currencySymbol = "$";
            double balanceValue = balance.getTotalUSD(statistics);
            if(currentTotal.contains("$")){
                //Switch to euro
                currencySymbol = "€";
                balanceValue = balance.getTotalEUR(statistics);
            } else if (currentTotal.contains("€")){
                currencySymbol = "BGN";
                balanceValue = balance.getTotalBGN(statistics);
            }
            //else its BGN so show usd
            mTotaViewlUsd.setText(currencySymbol+"\n"+(balanceValue>1000?String.format("%,.0f", balanceValue):
                                                                    String.format("%,.2f", balanceValue)));

        }
    }

    //Utils text
    private void populateTotalVelocityText(TextView view, double velocity){
        String velosity = velocity>0?"Gain":"Loss";
        String interval =" last "+ SettingsManager.TimeInterval.getLabel(SettingsManager.getInstance().CRIPTO_INTERVAL);
        String velosityInterval = velosity + interval;

        view.setText(String.format("%s:\n %.4f", velosityInterval, velocity)+" %");
    }

    //Utils ui colours
    protected void setItemBackgroundColor(View item,double price){
        if(price<1){
            item.setBackgroundResource(R.color.colorWatchlist_higlightet);
        } else {
            item.setBackgroundResource(R.color.colorWatchList);
        }
    }

    protected void updatePercentChangeColor(TextView view, double change){
        if(view == null)
            return ;

        int color = change>=0 ? R.color.colorProfit_green : R.color.colorProfit_red;
        if(isAdded())
            view.setTextColor(getResources().getColor(color));
    }

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //TODO listener
            OwnedCryptoItem item = mAdapter.getItem(i);
            SettingsManager.CRIPTO_BASIS = item.getCurrency();
            mAdapter.notifyDataSetChanged();
            updateTotals();
        }
    };

    private class WalletAdapter extends ArrayAdapter<OwnedCryptoItem> {

        public WalletAdapter(@NonNull Context context, int resource, List<OwnedCryptoItem> items) {
            super(context, R.layout.wallet_list_item, items);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.wallet_list_item, null);
            }

            OwnedCryptoItem item = getItem(position);

            //Filling TODO
            if (item != null) {
                TextView valueField = v.findViewById(R.id.value);
                TextView currencyField = v.findViewById(R.id.currency);
                final TextView dollarValue = v.findViewById(R.id.wallet_item_dollar_value);
                final TextView dollarRate = v.findViewById(R.id.wallet_item_dollar_rate);
                final TextView percentCurrency = v.findViewById(R.id.currency_percent);
                final TextView percentVolume = v.findViewById(R.id.value_percent);
                final TextView percentRate = v.findViewById(R.id.wallet_item_dollar_rate_percent);
                final TextView percentProfit = v.findViewById(R.id.wallet_item_dollar_value_percent);

                if (valueField != null) {
                    valueField.setText(String.valueOf(item.getValue()));
                }

                CoinMarketStatistics statistics = LogicManager.getInstance().getCoinMarketStatistics();
                if (statistics != null) {
                    CoinMarketStateInfo state = statistics.get(SettingsManager.getInstance().getCryptoIdByName(item.getCurrency()));

                    if (currencyField != null) {
                        currencyField.setText(item.getCurrency());
                    }

                    if (state != null) {
                        if (dollarValue != null) {
                            double usdPrice = item.getValue() * state.getPriceUsd();
                            dollarValue.setText(usdPrice>1000?String.format("$ %,.0f", usdPrice):String.format("$ %,.2f", usdPrice));
                            setItemBackgroundColor(v, usdPrice);
                            updatePercentChangeColor(dollarValue, state.getChange(SettingsManager.getInstance().CRIPTO_INTERVAL));
                        }

                        if (dollarRate != null) {
                            double rate = state.getPriceUsd();
                            dollarRate.setText(rate>1000?String.format("$ %,.0f", rate):String.format("$ %,.2f", rate));
                        }
                        //Percent Views
                        if (percentCurrency != null) {
                            double totalUsd =  WalletManager.getInstance().getBalance().getTotalUSD(statistics);
                            double weight = WalletManager.getInstance().getBalance().getPercentageWeight(item,state,totalUsd);
                            percentCurrency.setText(String.format(" %.2f", weight) + "%");
                        }
                        if (percentVolume != null) {
                            double rate = state.getPriceInCrypto(SettingsManager.getInstance().CRIPTO_BASIS,statistics);
                            String text = "";
                            int colourID = R.color.primary_text_color;
                            if(item.getCurrency().equals(SettingsManager.CRIPTO_BASIS)) {
                                 text = "BASIS";
                                 colourID = R.color.total_yellow;
                            } else {
                                text = String.format(" %.6f ", rate) + SettingsManager.CRIPTO_BASIS;
                                colourID = R.color.primary_text_color;
                            }
                            percentVolume.setText(text);
                            percentVolume.setTextColor(getContext().getResources().getColor(colourID));
//                            percentVolume.setText(String.format(" %.2f", rate) + "%");
//                            updatePercentChangeColor(percentVolume, state.getChange(SettingsManager.getInstance().CRIPTO_INTERVAL));
                        }
                        if (percentRate != null) {
                            double rate = state.getChange(SettingsManager.getInstance().CRIPTO_INTERVAL);
                            percentRate.setText(String.format(" %.2f", rate) + "%");
                            updatePercentChangeColor(percentRate, rate);
                        }
                        if (percentProfit != null) {
//                            WalletManager.Balance balance = WalletManager.getInstance().getBalance();
//                            String basisId = SettingsManager.sCryptoCurrencyIDs.get(item.getCurrency());
//                            double totalBTC = balance.getTotalBtc(statistics);
//                            double totalBasis = balance.getTotalInCripto(statistics,basisId);
//                            double velocity = balance.getTotalVelocityCripto(statistics,totalBTC,basisId,balance.getItemById(item.getCurrency()));


                            double totalUsd =  WalletManager.getInstance().getBalance().getTotalUSD(statistics);
                            double weight = WalletManager.getInstance().getBalance().getPercentageWeight(item,state,totalUsd);
                            double change = state.getChange(SettingsManager.getInstance().CRIPTO_INTERVAL);


                            double rate = weight*change/100;
                            percentProfit.setText(String.format(" %.2f", rate) + "%");

                            updatePercentChangeColor(percentProfit, rate);
                        }
                    }
                }
            }

            return v;
        }
    }
}
