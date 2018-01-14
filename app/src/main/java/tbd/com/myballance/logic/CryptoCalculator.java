package tbd.com.myballance.logic;

import tbd.com.myballance.data.OwnedCryptoItem;

/**
 * Created by todorbachvarov on 3.01.18.
 */

public class CryptoCalculator {
    public static CryptoCalculator sInstance;

    private WalletManager.Balance mSelection;

    public static CryptoCalculator getInstance(){
        if(sInstance == null)
            sInstance = new CryptoCalculator();

        return sInstance;
    }

    public CryptoCalculator(){
        mSelection = new WalletManager.Balance();
    }

   public void addSelection(OwnedCryptoItem item){
        mSelection.add(item);
   }

   public void removeSelection(OwnedCryptoItem item){
       if(mSelection.contains(item))
           mSelection.remove(item);
   }

   public boolean contains(OwnedCryptoItem item){
       return mSelection.contains(item);
   }

   public boolean hasSelection(){
       return !mSelection.isEmpty();
   }

   public void clear(){
       mSelection.clear();
   }

   public WalletManager.Balance getBalance(){
       return mSelection;
   }

}
