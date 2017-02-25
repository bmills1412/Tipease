package com.example.bryan.tipease.BillingDataPackage;


import android.os.Bundle;
import android.support.v4.app.Fragment;


/**
 * The two pricesDataFragments i'll have will be fed the same data from the mainscreen activity, hence the abstraction.
 */

public abstract class PricesDataBase extends Fragment {


    protected BillInformation information;

    protected boolean dataReady = false;

    @Override
    public void onCreate(Bundle b){
        super.onCreate(b);
        dataReady = information.hasPendingTransaction();
    }

    public abstract void onBillingInfoChanged();

    public abstract String getFragmentTag();

}
