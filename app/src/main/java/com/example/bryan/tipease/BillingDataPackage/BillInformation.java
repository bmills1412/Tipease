package com.example.bryan.tipease.BillingDataPackage;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.NumberFormat;


public class BillInformation implements Parcelable{

    private static NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    private static final double DEFAULT_TOTAL = 0.0d;

    private int currentTip = 0;
    private int currentTax = 0;
    private int currentSplit = 0;

    private double tipTotal = 0d;
    private double taxTotal = 0d;
    private double splitTotal = 0d;

    private double originalProductBill = 0.0d;
    private double productBillTotal = 0.0d;


    private boolean hasProductTotal = false;
    private boolean isMultipleParty = false; // by default, it is an individual party

    public BillInformation(){

    }

    /**
     *
     * dest.writeString(getOriginalProductBill());
     *
     * dest.writeString(getCurrentTip());
     dest.writeString(getCurrentTax());
     dest.writeString(getCurrentSplit());
     *
     */
    public BillInformation(Parcel in){
        setOriginalProductBill(Double.parseDouble(in.readString()));

        setCurrentTip(toInt(in.readString()));
        setCurrentTax(toInt(in.readString()));
        setCurrentSplit(toInt(in.readString()));
    }



    public String getCurrentTip() {
        return toPercentage(currentTip);
    }
    public String getTipTotal(){
        return toCurrency(tipTotal);
    }


    public String getCurrentTax() {
        return toPercentage(currentTax);
    }
    public String getTaxTotal(){
        return toCurrency(taxTotal);
    }


    public String getCurrentSplit() {
        return Integer.toString(currentSplit);
    }
    public String getSplitTotal(){
        return toCurrency(splitTotal);
    }



    public String getOriginalProductBill() {
        return toCurrency(originalProductBill);
    }
    public String getProductBillTotal() {
        return toCurrency(productBillTotal);
    }


    public String getTipTotalWithSplit(){
        double splitDivide = (isMultipleParty) ? currentSplit : 1;
        return toCurrency(tipTotal/splitDivide);
    }

    public String getTaxTotalWithSplit(){
        double splitDivide = (isMultipleParty) ? currentSplit : 1;
        return toCurrency(taxTotal/splitDivide);
    }



    public void setCurrentTip(int currentTip) {
       if(hasProductTotal){
           this.currentTip = currentTip;
           tipTotal = originalProductBill * (currentTip/100.0d);
           setProductTotal();
       }
    }


    public void setCurrentTax(int currentTax) {
       if(hasProductTotal){
           this.currentTax = currentTax;
           this.taxTotal = originalProductBill * (currentTax/100.0d);
           setProductTotal();
       }
    }


    public void setCurrentSplit(int currentSplit) {
       if(hasProductTotal){
           this.isMultipleParty = (currentSplit>1);
           this.currentSplit = currentSplit;
           setProductTotal();
       }
    }


    public void setOriginalProductBill(double originalProductBill) {

        this.originalProductBill = originalProductBill;

        if(hasProductTotal)
            resetValues();
        if (!hasProductTotal)
            hasProductTotal = true;

        setProductTotal();
    }


    private void setProductTotal(){
        this.productBillTotal =
                (originalProductBill + tipTotal + taxTotal);
        this.splitTotal = productBillTotal / ((isMultipleParty) ? currentSplit : 1);

    }


    public boolean isMultipleParty(){
        return isMultipleParty;
    }

    public boolean hasPendingTransaction(){
        return (hasProductTotal);
    }


    private void resetValues(){
        currentTip = currentTax = currentSplit = 0;
        isMultipleParty = false;
        tipTotal = taxTotal = splitTotal = 0.0d;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    //Read back IN ORDER they're written.
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getOriginalProductBill());
        dest.writeString(getCurrentTip());
        dest.writeString(getCurrentTax());
        dest.writeString(getCurrentSplit());
    }





    private static String toPercentage(int bluh){
        return String.valueOf(bluh)+"%";
    }

    private static String toCurrency(double bluh){
        return currencyFormat.format(bluh);
   }

   private static int toInt(String woogaly){
       return Integer.parseInt(woogaly);
   }






    public static final Parcelable.Creator<BillInformation> CREATOR = new Parcelable.Creator<BillInformation>() {
        public BillInformation createFromParcel(Parcel pc) {
            return new BillInformation(pc);
        }
        public BillInformation[] newArray(int size) {
            return new BillInformation[size];
        }
    };







}
