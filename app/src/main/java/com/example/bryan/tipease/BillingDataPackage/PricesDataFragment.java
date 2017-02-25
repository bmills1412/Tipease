package com.example.bryan.tipease.BillingDataPackage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bryan.tipease.R;



public class PricesDataFragment extends PricesDataBase {

 private static final String TAG = "PRICES_DATA_FRAGMENT";


    private TextView tipTextView;
    private TextView taxTextView;
    private TextView splitTextView;

    private TextView productTextView;
    private TextView totalTextView;



    public PricesDataFragment() {
    }


    public static PricesDataBase newInstance(BillInformation info) {
        PricesDataFragment fragment = new PricesDataFragment();
        fragment.information = info;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.prices_data_fragment, container, false);

        tipTextView = (TextView) layoutView.findViewById(R.id.tipTotal);

        taxTextView = (TextView) layoutView.findViewById(R.id.taxTotal);

        splitTextView = (TextView) layoutView.findViewById(R.id.splitTotal);

        productTextView = (TextView) layoutView.findViewById(R.id.productTotal);

        totalTextView = (TextView) layoutView.findViewById(R.id.youPayTotal);


        if(dataReady) constructLayouts();

    return layoutView;
    }


    private void constructLayouts(){
        tipTextView.setText(information.getTipTotal());
        taxTextView.setText(information.getTaxTotal());
        splitTextView.setText(information.getSplitTotal());
        productTextView.setText(information.getOriginalProductBill());
        totalTextView.setText(information.getProductBillTotal());
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
