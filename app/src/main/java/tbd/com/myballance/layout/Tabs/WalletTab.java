package tbd.com.myballance.layout.Tabs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tbd.com.myballance.R;
import tbd.com.myballance.SettingsManager;
import tbd.com.myballance.data.CoinMarketStatistics;
import tbd.com.myballance.data.FreshCryptoState;
import tbd.com.myballance.data.OwnedCryptoItem;
import tbd.com.myballance.logic.LogicManager;
import tbd.com.myballance.logic.WalletManager;

/**
 * Created by todorbachvarov on 17.12.17.
 */

public class WalletTab extends MainTab {
    TextView       mTotaViewlUsd;
    TextView       mTotaViewlBtc;
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
        mTotaViewlBtc = rootView.findViewById(R.id.wallet_total_btc);
        mAdapter = new WalletAdapter(getActivity(),R.layout.wallet_list_item ,new ArrayList<OwnedCryptoItem>());
        mListView.setAdapter(mAdapter);
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
                double usdPrice = balance.getTotalUSD(statistics);
                mTotaViewlUsd.setText(String.format("$\n%.2f", usdPrice));
            }
            if (mTotaViewlUsd != null) {
                double btcPrice =balance.getTotalBtc(statistics);
                mTotaViewlBtc.setText(String.format("BTC %.6f", btcPrice));
            }
        }
    }

    private class WalletAdapter extends ArrayAdapter<OwnedCryptoItem> {

        public WalletAdapter(@NonNull Context context, int resource, List<OwnedCryptoItem> items) {
            super(context,R.layout.wallet_list_item,items);
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

                if (valueField != null) {
                    valueField.setText(String.valueOf(item.getValue()));
                }

                if (currencyField != null) {
                    currencyField.setText(item.getCurrency());
                }
                CoinMarketStatistics statistics =  LogicManager.getInstance().getCoinMarketStatistics();
                if (statistics != null) {
                    FreshCryptoState state = statistics.get(SettingsManager.getInstance().getCryptoIdByName(item.getCurrency()));

                    if(state!=null) {
                        if (dollarValue != null) {
                            double usdPrice = item.getValue() * state.getPriceUsd();
                            dollarValue.setText(String.format("$ %.2f", usdPrice));
                        }

                        if (dollarRate != null) {
                            double rate = state.getPriceUsd();
                            if (rate > 0)
                                dollarRate.setText(String.format(/*item.getCurrency()+*/"$ %.2f", rate));
                        }
                    }
                }
            }

            return v;
        }

    }
}
