package com.example.bryan.tipease.UiInteractionPackage;





public interface TipViewControllerClient {

    void tipChanged(int newTip);
    void taxChanged(int newTax);
    void splitChanged(int newSplit);
    void productBillChanged(String newTotal);

    void onToggleClicked(boolean checked);

}
