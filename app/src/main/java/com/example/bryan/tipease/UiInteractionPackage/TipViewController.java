package com.example.bryan.tipease.UiInteractionPackage;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import com.example.bryan.tipease.CustomViews.TipView;
import com.example.bryan.tipease.MainControllers.ProductTotalWindow;
import com.example.bryan.tipease.R;

/**
 * Does nothing but deliver messages to the registered client
 */

public class TipViewController extends Fragment {


    private TipViewControllerClient client;




    private CardView cardView;

    private TipView tipView;
    private ImageButton productEditButton;
    private CheckBox dataToggle;

    private ProductTotalWindow productTotalWindow;




    public TipViewController() {
    }

    public static TipViewController newInstance() {
        return new TipViewController();
    }




    @Override
    public void onPause(){
        super.onPause();
        if(productTotalWindow!=null)
            if(productTotalWindow.isActive())
                productTotalWindow.dismiss();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.tipview_controller, container, false);

        cardView = (CardView) layoutView.findViewById(R.id.tipViewControllerContainer);


        this.tipView = (TipView) layoutView.findViewById(R.id.tipView);
        this.productEditButton = (ImageButton) layoutView.findViewById(R.id.productDialogLauncher);
        this.dataToggle = (CheckBox) layoutView.findViewById(R.id.dataToggle);

        tipView.setOnValueChangedListener(new TipView.OnValueChangedListener() {
            @Override
            public void tipValueChanged(int newTip) {
                client.tipChanged(newTip);
            }

            @Override
            public void taxValueChanged(int newTax) {
                client.taxChanged(newTax);
            }

            @Override
            public void splitValueChanged(int newSplit) {
                client.splitChanged(newSplit);
            }
        });


        productEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(productTotalWindow == null) {
                    productTotalWindow = new ProductTotalWindow(getContext());
                    productTotalWindow.setOnDataSetListner(productSetListener);
                }
                productTotalWindow.show(v);
            }
        });


        dataToggle.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) buttonView.setButtonDrawable(R.drawable.toggle_checked_compound);
                else buttonView.setButtonDrawable(R.drawable.toggle_unchecked);

                client.onToggleClicked(isChecked);
            }
        });



    return  layoutView;
    }



    ProductTotalWindow.OnDataSetListener productSetListener = new ProductTotalWindow.OnDataSetListener() {
        @Override
        public void onDataSet(String value) {
            tipView.reset();
            client.productBillChanged(value);
        }
    };




    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void resetShadow(boolean on){
        //get shadow elevation and remove/include (for fragment being added)
    }



    public void registerClient(TipViewControllerClient client){
        this.client = client;
    }


}
