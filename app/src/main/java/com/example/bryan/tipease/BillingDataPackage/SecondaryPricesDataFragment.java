package com.example.bryan.tipease.BillingDataPackage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bryan.tipease.CustomViews.AngleTextLayout;
import com.example.bryan.tipease.R;


public class SecondaryPricesDataFragment extends PricesDataBase {

    private static final String TAG = "SECONDARY_PRICES_DATA_FRAGMENT";



    private AngleTextLayout tipLayout;
    private AngleTextLayout taxLayout;
    private AngleTextLayout splitLayout;



    public SecondaryPricesDataFragment() {
    }


    public static SecondaryPricesDataFragment newInstance(BillInformation information) {
        SecondaryPricesDataFragment fragment = new SecondaryPricesDataFragment();
        fragment.information = information;
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.fragment_secondary_prices_data, container, false);


        this.tipLayout = (AngleTextLayout) layoutView.findViewById(R.id.angleLayoutTip);
        this.taxLayout = (AngleTextLayout) layoutView.findViewById(R.id.angleLayoutTax);
        this.splitLayout = (AngleTextLayout) layoutView.findViewById(R.id.angleLayoutSplit);

        if(dataReady) constructLayouts();


        return layoutView;
    }



    private void constructLayouts(){
        //The values need to be adjusted according to the split, but this is for examples sake.

        tipLayout.setPrimaryValueText(information.getTipTotalWithSplit());
        tipLayout.setSecondaryValueText(information.getCurrentTip());

        taxLayout.setPrimaryValueText(information.getTaxTotalWithSplit());
        taxLayout.setSecondaryValueText(information.getCurrentTax());

        splitLayout.setPrimaryValueText(information.getSplitTotal());
        splitLayout.setSecondaryValueText(information.getCurrentSplit());
    }


    @Override
    public void onBillingInfoChanged() {
        constructLayouts();
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

}
