package com.example.bryan.tipease;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.NumberFormat;

public class MainScreen extends AppCompatActivity implements TipView.OnValueChangedListener {


    //#Custom view to update values#\\
    private TipView tipView;



    //#Views to show prices data#\\
    private FrameLayout pricesTotalContainer;

    private TextView tipTextView;
    private TextView taxTextView;
    private TextView splitTextView;
    private TextView productTextView;
    private TextView totalTextView;

    private TextView youPayEachPay;


    //#misc states related to updating the total#\\
    private ImageButton productEditButton;
    private ImageButton dataToggle;

    private ProductTotalWindow productTotalWindow;



    //#current data#\\
    private double productTotal = 0.0d;

    private double currentTip = 0.0d;
    private double currentTax = 0.0d;
    private int currentSplit = 1;


    private NumberFormat currencyFormat;


    private String pricesDefaults = "$0.00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        this.tipView = (TipView) findViewById(R.id.tipView);
             tipView.setOnValueChangedListener(this);


        initDataPricesViews();

        initProductEditView();

        currencyFormat = NumberFormat.getCurrencyInstance();

    }


    private void initDataPricesViews(){
        tipTextView = (TextView) findViewById(R.id.tipTotal);
        taxTextView = (TextView) findViewById(R.id.taxTotal);
        splitTextView = (TextView) findViewById(R.id.splitTotal);
        splitTextView.setText(String.valueOf(currentSplit));
        productTextView = (TextView) findViewById(R.id.productTotal);
        totalTextView = (TextView) findViewById(R.id.youPayTotal);
        youPayEachPay = (TextView) findViewById(R.id.youPayEachPay);
    }


    private void initProductEditView(){

        productEditButton = (ImageButton) findViewById(R.id.productDialogLauncher);
        dataToggle = (ImageButton) findViewById(R.id.dataToggle);

        productEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(productTotalWindow ==null) {
                    productTotalWindow = new ProductTotalWindow(MainScreen.this);
                    productTotalWindow.setOnDataSetListner(MainScreen.this.dataSetListener);
                }
                productTotalWindow.show(productEditButton);
            }
        });

    }




    @Override
    public void onPause(){
        super.onPause();
        if(productTotalWindow!=null)
            if(productTotalWindow.isActive())
            productTotalWindow.dismiss();
    }




    @Override
    public void tipValueChanged(int newTip) {
        if(productTotal != 0.0d){

            currentTip = productTotal * (newTip/100.0d);

            tipTextView.setText(currencyFormat.format(currentTip));

            onTotalChanged();
        }
    }

    @Override
    public void taxValueChanged(int newTax) {
        if(productTotal != 0.0d){

            currentTax = productTotal * (newTax/100.0d);

            taxTextView.setText(currencyFormat.format(currentTax));

            onTotalChanged();
        }
    }

    @Override
    public void splitValueChanged(int newSplit) {
        if(newSplit > 0){

            int newText = (newSplit > 1) ? R.string.eachPay : R.string.youPay;
            youPayEachPay.setText(newText);

            youPayEachPay.setText(R.string.eachPay);
            currentSplit = newSplit;
            splitTextView.setText(String.valueOf(newSplit));
            onTotalChanged();
        }else{
            youPayEachPay.setText(R.string.youPay);
        }
    }




    ProductTotalWindow.OnDataSetListener dataSetListener = new ProductTotalWindow.OnDataSetListener() {
        @Override
        public void onDataSet(String value) {
            MainScreen.this.productTotal = Double.parseDouble(value);

            productTextView.setText(currencyFormat.format(productTotal));

            tipView.reset();
            resetValues();

        onTotalChanged();
        }
    };


    private void resetValues(){
        currentSplit = 1;
        splitTextView.setText(String.valueOf(currentSplit));
        youPayEachPay.setText(R.string.youPay);
        currentTax = currentTip = 0.0d;
        tipTextView.setText(pricesDefaults);
        taxTextView.setText(pricesDefaults);
    }




    private void onTotalChanged(){
         double youPayTotal = (productTotal + currentTip + currentTax) / currentSplit;
        totalTextView.setText(currencyFormat.format(youPayTotal));
    }




}
