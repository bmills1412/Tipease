package com.example.bryan.tipease;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Draw the drop shadow here. Thanks, boo :)
 */

 class Track extends Drawable {



    private float radius;

    private float trackSize, stroke;

    private Paint strokePaint, trackPaint;




     Track(float trackSize,float strokeSize, int circleColor, int strokeColor){
        this.trackSize = trackSize;
        this.radius = trackSize*0.5f;
        this.stroke = strokeSize;

        trackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        trackPaint.setColor(circleColor);

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(strokeColor);
        strokePaint.setStrokeWidth(strokeSize*2);
        //for some reason, there was an issue with how the stroke is being drawn, in that only 1/2 of it was drawn?
        //it might be something to do with how the stroke was drawn in here, but i'm not sure. To compensate i
        //just "duct taped" the bug by multiplying it by 2. technically its a bug, but fuck it it works...


    }





    @Override
    public void draw(Canvas canvas) {

        float centerX, centerY;
        centerX = this.getBounds().centerX();
        centerY = this.getBounds().centerY();

        canvas.drawCircle(centerX, centerY, radius, strokePaint);

        canvas.drawCircle(centerX, centerY, radius, trackPaint);

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
