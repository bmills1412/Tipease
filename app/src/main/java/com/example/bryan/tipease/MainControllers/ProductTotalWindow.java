package com.example.bryan.tipease.MainControllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.IBinder;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.bryan.tipease.CustomViews.ProductTotalEditText;
import com.example.bryan.tipease.R;


public class ProductTotalWindow implements PopupWindow.OnDismissListener,
                                           ProductTotalEditText.BackKeyListner,
                                           TextView.OnEditorActionListener {



    //#Windows#\\
    private PopupWindow scrimWindow;
    private PopupWindow window;




    //#content views of the windows#\\
    private View contentView;




    //#User views in the contentView#\\
    private ProductTotalEditText productEditText;




    //#Utility classes#\\
    private InputMethodManager inputManager;

    private Context context;

    private IBinder windowToken;



    //#listener for the product total#\\
    private OnDataSetListener dataListener;





    public ProductTotalWindow(Context context){
        this.context = context;
        create();
    }


    public void create(){

        if(window==null & scrimWindow == null){
            final LayoutInflater inflater = LayoutInflater.from(context);

            final int wrapContent = ViewGroup.LayoutParams.WRAP_CONTENT;
            final int matchParent = ViewGroup.LayoutParams.MATCH_PARENT;


              launchScrim(matchParent, matchParent, inflater);


              contentView = inflater.inflate(R.layout.product_total_layout, null);
              windowToken = contentView.getWindowToken();

              initTextBox();

              initWindow(matchParent, wrapContent);
        }
    }


    private void launchScrim(int width, int height, LayoutInflater inflater){
        View scrim = inflater.inflate(R.layout.scrim_popup, null);
        this.scrimWindow = new PopupWindow(scrim, width, height, false);
        scrimWindow.setBackgroundDrawable(new ColorDrawable());

    }


    private void initTextBox(){
        productEditText = (ProductTotalEditText) contentView.findViewById(R.id.productEditText);
        productEditText.setBackKeyListener(this);
        productEditText.setOnEditorActionListener(this);
    }


    private void initWindow(int width, int height){
        this.window = new PopupWindow(contentView, width, height, true);
             window.setBackgroundDrawable(new ColorDrawable());
             window.setOnDismissListener(this);
             window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
             window.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
    }





    public void show(View windowToken){
        if(window!=null) {
            this.scrimWindow.showAtLocation(windowToken, Gravity.NO_GRAVITY, 0, 0);
            window.showAtLocation(windowToken, Gravity.BOTTOM, 0, 0);
            launchIME();
        }
    }


    private void launchIME(){
        this.inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
             inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }





    public boolean isActive(){
        return (scrimWindow.isShowing() & window.isShowing());
    }




    //#called internally to dismiss the scrim, and IME#\\
    @Override
    public void onDismiss(){
        inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        scrimWindow.dismiss();
    }


    //#For client or internal use#\\
    public void dismiss(){
        window.dismiss();
    }


    //#Recieves back-key-presses before the whole framework does#\\
    @Override
    public void backKeyPressed() {
        window.dismiss();
    }





/**
 * @return false if we want to pass control to the framework (default)
 *         true if we want to maintain control of the IME for future use (in case of errors)
 */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        boolean handleOrPassControl = false;

        if(actionId == EditorInfo.IME_ACTION_DONE){

            String enteredTotal = v.getText().toString();

            boolean isUsable = isUsable(enteredTotal);

            if(isUsable) {
                updateListener(enteredTotal);
                this.dismiss();
            }
            else if(!isUsable){
                launchErrorDialog();
                handleOrPassControl = true;
            }


        }



    return handleOrPassControl;
    }


    private boolean isUsable(String productTotal){

        //The product total is NOT empty? True or False?
        //'local variable 'isEmpty' is redundant' igonore, i will update this method later
      boolean isEmpty = (!productTotal.equals(""));

        return isEmpty;
    }


    private void launchErrorDialog(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setMessage(R.string.errorDialogMessage);
            dialogBuilder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialogBuilder.show();
    }


    private void updateListener(String enteredTotal){
            dataListener.onDataSet(enteredTotal);
    }





    public void setOnDataSetListner(OnDataSetListener listner){
        this.dataListener = listner;
    }


    public interface OnDataSetListener{
        void onDataSet(String value);
    }


}
