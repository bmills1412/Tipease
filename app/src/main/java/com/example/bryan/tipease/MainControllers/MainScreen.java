package com.example.bryan.tipease.MainControllers;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.bryan.tipease.BillingDataPackage.BillInformation;
import com.example.bryan.tipease.BillingDataPackage.PricesDataBase;
import com.example.bryan.tipease.BillingDataPackage.PricesDataFragment;
import com.example.bryan.tipease.BillingDataPackage.SecondaryPricesDataFragment;
import com.example.bryan.tipease.R;
import com.example.bryan.tipease.UiInteractionPackage.TipViewController;
import com.example.bryan.tipease.UiInteractionPackage.TipViewControllerClient;



public class MainScreen extends AppCompatActivity implements TipViewControllerClient {

  public static final String BILLING_INFO_SAVE = "woogalyBoogaly";

    private TipViewController tipViewController;


    private PricesDataBase currentPricesDataBase;

    private PricesDataBase mainPricesData;
    private PricesDataBase secondaryPricesData;

    private BillInformation information;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        if(savedInstanceState != null) information =  savedInstanceState.getParcelable(BILLING_INFO_SAVE);
        if(information==null) information = new BillInformation();

       FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();



        tipViewController = TipViewController.newInstance();
        tipViewController.registerClient(this);

        fragTrans.add(R.id.tipViewControllerContainer, tipViewController);


        currentPricesDataBase = mainPricesData = PricesDataFragment.newInstance(information);
        secondaryPricesData = SecondaryPricesDataFragment.newInstance(information);

        fragTrans.add(R.id.pricesDataContainer, currentPricesDataBase, currentPricesDataBase.getFragmentTag()).commit();

    }


    @Override
    public void onSaveInstanceState(Bundle s){
        if(information!=null)
        s.putParcelable(BILLING_INFO_SAVE, information);
    }


    @Override
    public void tipChanged(int newTip) {
        information.setCurrentTip(newTip);
        currentPricesDataBase.onBillingInfoChanged();
    }

    @Override
    public void taxChanged(int newTax) {
        information.setCurrentTax(newTax);
        currentPricesDataBase.onBillingInfoChanged();

    }

    @Override
    public void splitChanged(int newSplit) {
        information.setCurrentSplit(newSplit);
        currentPricesDataBase.onBillingInfoChanged();

    }

    @Override
    public void productBillChanged(String newTotal) {
        information.setOriginalProductBill(newTotal);
        currentPricesDataBase.onBillingInfoChanged();
    }





    @Override
    public void onToggleClicked(boolean checked) {
        if(checked){
            currentPricesDataBase = secondaryPricesData;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.pricesDataContainer, secondaryPricesData, currentPricesDataBase.getFragmentTag())
                    .commit();
        }else{
            currentPricesDataBase = mainPricesData;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.pricesDataContainer, mainPricesData, currentPricesDataBase.getFragmentTag())
                    .commit();
        }
    }



}


