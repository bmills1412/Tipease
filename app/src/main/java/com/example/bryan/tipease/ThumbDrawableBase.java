package com.example.bryan.tipease;

import android.graphics.drawable.Drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;


/**
 * BaseClass for the drawables, that contains all of the drawables events
 */

public class ThumbDrawableBase extends Drawable  {



    private boolean hasFocus = false;


    //#THUMB PAINTS#\\
    protected Paint thumbPaint;
    protected TextPaint textPaint;
    protected Paint strokePaint;



    //#THUMB ATTRIBUTES#\\
    protected int attrTouchColor;
    protected int attrThumbColor;
    protected CharSequence thumbText;



    //#THUMB TEXTUAL LAYOUT#\\
    private Layout mTextLayout;



    //#UTILITY VALUES FOR VIEW#\\
    protected float floorRadians;
    protected float cielRadians;




    public ThumbDrawableBase(int attrThumbColor, int attrTouchColor,
                             int textColor, CharSequence text, float textSize, float thumbStrokeSize,
                             int thumbStrokeColor){

        this.attrTouchColor = attrTouchColor;

        this.attrThumbColor = attrThumbColor;

        this.thumbText = text;


        thumbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        thumbPaint.setColor(attrThumbColor);

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setColor(thumbStrokeColor);
        strokePaint.setStrokeWidth(thumbStrokeSize);

        this.textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
        textPaint.setStrokeWidth(1.0f);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);



        this.mTextLayout = new StaticLayout(text, textPaint, (int)Layout.getDesiredWidth(text, textPaint),
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0f, true);

    }




    @Override
    public void draw(Canvas canvas) {

        float radius = (getBounds().right - getBounds().left) *0.5f;


        if(hasFocus){
            canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), radius+strokePaint.getStrokeWidth(), strokePaint);
        }

        canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), radius, thumbPaint);


        canvas.save();
        canvas.translate(getBounds().centerX() - (mTextLayout.getWidth()/2), getBounds().centerY() - mTextLayout.getHeight()/2);
        mTextLayout.draw(canvas);
        canvas.restore();



    }





    public void changeColor(boolean isDown){
        if(isDown) this.thumbPaint.setColor(attrTouchColor);
        else this.thumbPaint.setColor(attrThumbColor);
    }




    public void setFloorAndCiel(float floor, float ciel){
        this.floorRadians = floor;
        this.cielRadians = ciel;
    }

    public float getFloorRadians(){
        return floorRadians;
    }

    public float getCielRadians(){
        return cielRadians;
    }



    public void setFlagHasFocus(boolean hasFocus){
        this.hasFocus = hasFocus;
    }


    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }


}
