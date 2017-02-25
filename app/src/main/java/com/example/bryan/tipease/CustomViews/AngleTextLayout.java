package com.example.bryan.tipease.CustomViews;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.transition.ChangeBounds;
import android.util.AttributeSet;
import android.view.View;

import com.example.bryan.tipease.R;

/**
 * Created by bryan on 2/21/2017.
 */

public class AngleTextLayout extends View {






    private TextPaint titleTextPaint;
    private TextPaint descriptionTextPaint;
    private TextPaint primaryValueTextPaint;
    private TextPaint secondaryValueTextPaint;




    private int titleTextSize;
    private int descriptionTextSize;
    private int primaryValueTextSize;
    private int secondaryValueTextSize;



    private CharSequence titleText;
    private CharSequence descriptionText;
    private CharSequence primaryValueText;
    private CharSequence secondaryValueText;


    private int standardTextColor;
    private int specialTextColor;



    private int titleTextStartX;
    private int titleTextStartY;



    private int textPadding;


    private Rect titleTextRect;
    private Rect descriptionTextRect;
    private Rect primaryValueTextRect;
    private Rect secondaryValueTextRect;



   public AngleTextLayout(Context context){
       this(context, null);
   }

   public AngleTextLayout(Context context, AttributeSet set){
       this(context, set, 0); //cahnge to default styling
   }

   public AngleTextLayout(Context context, AttributeSet set, int defaultStyle){
       super(context, set, defaultStyle);
       initResources(context.obtainStyledAttributes(set, R.styleable.AngleTextLayout, defaultStyle, R.style.WidgetAngleTextLayoutStyle));
   }

   @TargetApi(21)
    public AngleTextLayout(Context context, AttributeSet set, int defStyle, int defTheme){
        super(context, set, defStyle, defTheme);
   }


  private void initResources(TypedArray array){


      final int n = array.getIndexCount();
      for(int i = 0; i < n; i++){

          int attr = array.getIndex(i);

          switch (attr){

              case R.styleable.AngleTextLayout_standardTextColor:
                  this.standardTextColor = array.getColor(attr, 0);
                  break;

              case R.styleable.AngleTextLayout_specialTextColor:
                  this.specialTextColor = array.getColor(attr, 0);
                  break;

              case R.styleable.AngleTextLayout_titleTextSize:
                  this.titleTextSize = array.getDimensionPixelSize(attr, 22);
                  break;

              case R.styleable.AngleTextLayout_descriptionTextSize:
                  this.descriptionTextSize = array.getDimensionPixelSize(attr, 18);
                  break;

              case R.styleable.AngleTextLayout_primaryValueTextSize:
                  this.primaryValueTextSize = array.getDimensionPixelSize(attr, 24);
                  break;

              case R.styleable.AngleTextLayout_secondaryValueTextSize:
                  this.secondaryValueTextSize = array.getDimensionPixelSize(attr, 22);
                  break;

              case R.styleable.AngleTextLayout_titleText:
                  this.titleText = array.getString(attr);
                  break;

              case R.styleable.AngleTextLayout_descriptionText:
                  this.descriptionText = array.getString(attr);
                  break;

              case R.styleable.AngleTextLayout_primaryValueText:
                  this.primaryValueText = array.getString(attr);
                  break;

              case R.styleable.AngleTextLayout_secondaryValueText:
                  this.secondaryValueText = array.getString(attr);
                  break;

              case R.styleable.AngleTextLayout_textPadding:
                  this.textPadding = array.getDimensionPixelSize(attr, 12);

                  break;

              default: break;
          }

      }

      init();
  }

  private void init(){
      this.titleTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
           titleTextPaint.setColor(standardTextColor);
           titleTextPaint.setTextSize(titleTextSize);

      this.descriptionTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
           descriptionTextPaint.setColor(standardTextColor);
           descriptionTextPaint.setTextSize(descriptionTextSize);

      this.primaryValueTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
           primaryValueTextPaint.setColor(specialTextColor);
           primaryValueTextPaint.setTextSize(primaryValueTextSize);


      this.secondaryValueTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
           secondaryValueTextPaint.setColor(specialTextColor);
           secondaryValueTextPaint.setTextSize(secondaryValueTextSize);


      titleTextRect = new Rect();
      titleTextPaint.getTextBounds((String)titleText, 0, titleText.length(), titleTextRect);

      descriptionTextRect = new Rect();
      descriptionTextPaint.getTextBounds((String)descriptionText, 0, descriptionText.length(), descriptionTextRect);


      primaryValueTextRect = new Rect();
      primaryValueTextPaint.getTextBounds((String)primaryValueText, 0, primaryValueText.length(), primaryValueTextRect);


      secondaryValueTextRect = new Rect();
      secondaryValueTextPaint.getTextBounds((String)secondaryValueText, 0, secondaryValueText.length(), secondaryValueTextRect);


      this.titleTextStartX = getPaddingLeft();
      this.titleTextStartY = getPaddingTop();

  }


    @Override
    public void onMeasure(int w, int h){

     int width = MeasureSpec.getSize(w);

     int textHeight = titleTextRect.height() +
                      descriptionTextRect.height() +
                      primaryValueTextRect.height();


     int paddingVertical = getPaddingTop() + getPaddingBottom();


     int height = textHeight+textPadding+paddingVertical;

      setMeasuredDimension(width, height);

    }


    //X and Y in drawText are the BOTTOM-LEFT coordinates
    @Override
    public void onDraw(Canvas canvas){

        int titleDescent = (int)titleTextPaint.getFontMetrics().descent;
        int drawTitleY = getPaddingTop() + (titleTextRect.height() - titleDescent);
        canvas.drawText((String)titleText, titleTextStartX, drawTitleY, titleTextPaint);


        int drawSecondaryValueX = getWidth() - getPaddingRight() - secondaryValueTextRect.width();
        int drawSecondaryValueY = getPaddingTop() + (int)(secondaryValueTextRect.height() - secondaryValueTextPaint.getFontMetrics().descent);
        canvas.drawText((String)secondaryValueText, drawSecondaryValueX, drawSecondaryValueY, secondaryValueTextPaint);


        int drawDescY = drawTitleY + textPadding + (int)(descriptionTextRect.height() - descriptionTextPaint.getFontMetrics().descent);
        canvas.drawText((String)descriptionText, titleTextStartX, drawDescY, descriptionTextPaint);


        int drawPrimaryTextX = titleTextStartX + titleTextRect.width();
        int drawPrimaryTextY = drawDescY + textPadding + (int)(primaryValueTextRect.height() - primaryValueTextPaint.getFontMetrics().descent);
        canvas.drawText((String)primaryValueText, drawPrimaryTextX, drawPrimaryTextY, primaryValueTextPaint);


    }




    public void setPrimaryValueText(CharSequence primaryValueText){
        this.primaryValueText = primaryValueText;
        primaryValueTextPaint.getTextBounds((String)primaryValueText, 0, primaryValueText.length(), primaryValueTextRect);
        invalidate(primaryValueTextRect);
    }




    public void setSecondaryValueText(CharSequence secondaryValueText){
        this.secondaryValueText = secondaryValueText;
        secondaryValueTextPaint.getTextBounds((String)secondaryValueText, 0, secondaryValueText.length(), secondaryValueTextRect);
        invalidate(secondaryValueTextRect);
    }



}
