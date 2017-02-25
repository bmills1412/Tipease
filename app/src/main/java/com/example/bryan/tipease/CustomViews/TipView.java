package com.example.bryan.tipease.CustomViews;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.example.bryan.tipease.CustomViews.ThumbDrawableBase;
import com.example.bryan.tipease.CustomViews.Track;
import com.example.bryan.tipease.R;



//TODO: Write logic for anything else (state saving/ accessibility events/ closing any resources onDetatchFromWindow/  etc)
//TODO: Tidy up the code, refractor, etc

public class TipView extends View implements Drawable.Callback {




    //#STATIC UTILITY FIELDS#\\
    private static final int STATE_MOVEMENT_BREAK_BOUNDARIES = -10;

    public final float tipTickMarkRad = (float)(Math.PI*3)/2;

    public final float taxTickMarkRad = tipTickMarkRad - ((float)(Math.PI*2)/3);

    public final float splitTickMarkRad = taxTickMarkRad - ((float)(Math.PI*2)/3);

    private static final float THUMB_GUTTER = 0.08726f; // thumb offset from the tick marks




    //#MISCELLANEOUS#\\
    private OnValueChangedListener onValueChangedListener;


    private final float[] tickRads = {tipTickMarkRad, splitTickMarkRad, taxTickMarkRad};


    private Rect[] resetBounds = new Rect[3];


    private float drawStartPosX, drawStartPosY,  drawLengthPos;



    //#THUMB POS#\\
    private float tipPos = -1.0f;
    private float taxPos = -1.0f;
    private float splitPos = -1.0f;


    //# TICK MARK VALUES #\\
    private Paint tickPaint;
    private float tickSize;
    private int tickcolor;




    //#Track Values - SIZE/TEXT/PAINT #\\
    private Paint trackTextPaint;
    private int trackTextSize;
    private int trackTextColor;
    private Rect trackTextRect;


    private float trackSize;
    private float trackStrokeSize;


    private Drawable trackDrawable;




    //#THUMB DRAWABLES#\\
    private ThumbDrawableBase drawableForUpdate;
    private ThumbDrawableBase tipDrawable, taxDrawable, splitDrawable;




    //#THUMB DRAWABLE AND VALUES#\\
    private float thumbSize; //used internally to tell the drawable its bounds


    private float tipFloor, taxFloor, splitFloor;
    private float thumbOffset;




    //#TOUCH INFORMATION#\\
    private ViewConfiguration configuration = ViewConfiguration.get(getContext());
    private float mTouchX, mTouchY;



    //#CURRENT VALUES#\\
    private int currentTip = 0;
    private int currentTax = 0;
    private int currentSplit = 0;




    public TipView(Context c){
        this(c, null);
    }

    public TipView(Context context, AttributeSet set){
        this(context, set, R.attr.TipViewStyle);

    }

    public TipView(Context c, AttributeSet set, int styleDef){
        this(c, set, styleDef, 0);

        TypedArray resArray = c.obtainStyledAttributes(set, R.styleable.TipView, styleDef, R.style.WidgetTipViewStyle);
        initView(resArray);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TipView(Context c, AttributeSet set, int styleDefault, int themeDefault){
        super(c, set, styleDefault, themeDefault);
    }

    private void initView(TypedArray resArray){

        initResources(resArray);

        this.tickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
             tickPaint.setStyle(Paint.Style.STROKE);
             tickPaint.setStrokeWidth(tickSize);
             tickPaint.setColor(tickcolor);


        this.trackTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
             trackTextPaint.setColor(trackTextColor);
             trackTextPaint.setTextSize(trackTextSize);
             trackTextRect = new Rect();


        initThumbPos();
        initBounds();
    }

    private void initResources(TypedArray resArray){

        int tipColor = 0;
        int taxColor = 0;
        int splitColor = 0;
        int thumbTouchColor = 0;
        int thumbTextColor = 0;
        int thumbStrokeSize = 0;
        int thumbStrokeColor = 0;

        int trackColor = 0;
        int trackStrokeColor = 0;



        CharSequence tipText = null;
        CharSequence taxText = null;
        CharSequence splitText = null;


        float textSize = 0f;



        final int n = resArray.getIndexCount();
        for(int i = 0; i < n; i++){
            int attr = resArray.getIndex(i);
            switch(attr){

                case R.styleable.TipView_thumbSize:
                    this.thumbSize = resArray.getDimensionPixelSize(attr, 68);
                    break;

                case R.styleable.TipView_thumbStrokeSize:
                    thumbStrokeSize = resArray.getDimensionPixelSize(attr, 5);
                    break;

                case R.styleable.TipView_thumbStrokeColor:
                    thumbStrokeColor = resArray.getColor(attr, 0);
                    break;

                case R.styleable.TipView_trackSize:
                    this.trackSize = resArray.getDimensionPixelSize(attr, 225);
                    break;

                case R.styleable.TipView_trackTextSize:
                    this.trackTextSize = resArray.getDimensionPixelSize(attr, 32);
                    break;

                case R.styleable.TipView_trackStrokeSize:
                    this.trackStrokeSize = resArray.getDimensionPixelSize(attr, 54);
                    break;

                case R.styleable.TipView_tickmarkSize:
                    this.tickSize = resArray.getDimensionPixelSize(attr, 3);
                    break;

                case R.styleable.TipView_tickmarkColor:
                    this.tickcolor = resArray.getColor(attr, Color.WHITE);
                    break;

                case R.styleable.TipView_thumbTouchColor:
                    thumbTouchColor = resArray.getColor(attr, 0);
                    break;

                case R.styleable.TipView_tipColor:
                    tipColor = resArray.getColor(attr, 0);
                    break;

                case R.styleable.TipView_taxColor:
                    taxColor = resArray.getColor(attr, 0);
                    break;

                case R.styleable.TipView_splitColor:
                    splitColor = resArray.getColor(attr, 0);
                    break;

                case R.styleable.TipView_tipText:
                    tipText = resArray.getText(attr);
                    break;

                case R.styleable.TipView_taxText:
                    taxText = resArray.getText(attr);
                    break;

                case R.styleable.TipView_splitText:
                    splitText = resArray.getText(attr);
                    break;

                case R.styleable.TipView_thumbTextSize:
                    textSize = resArray.getDimensionPixelSize(attr, 5);
                default:
                    break;

                case R.styleable.TipView_trackColor:
                    trackColor = resArray.getColor(attr, 0);
                    break;

                case R.styleable.TipView_trackStrokeColor:
                    trackStrokeColor = resArray.getColor(attr, 0);
                    break;
                case R.styleable.TipView_thumbTextColor:
                    thumbTextColor = resArray.getColor(attr, 0);
                    break;
                case R.styleable.TipView_trackTextColor:
                    this.trackTextColor = resArray.getColor(attr, 0);
                    break;
            }
        }
        resArray.recycle();


        tipDrawable = new ThumbDrawableBase(tipColor, thumbTouchColor,
                thumbTextColor, tipText, textSize,
                thumbStrokeSize, thumbStrokeColor);

        taxDrawable = new ThumbDrawableBase(taxColor, thumbTouchColor,
                thumbTextColor, taxText, textSize,
                thumbStrokeSize, thumbStrokeColor);

        splitDrawable = new ThumbDrawableBase(splitColor, thumbTouchColor,
                thumbTextColor, splitText, textSize,
                thumbStrokeSize, thumbStrokeColor);



        this.trackDrawable = new Track(trackSize, trackStrokeSize, trackColor, trackStrokeColor);

    }

    private void initThumbPos(){

        this.thumbOffset = (float)Math.atan2( thumbSize*0.5f, trackSize*0.5f + trackStrokeSize*0.5f);


        this.splitFloor = splitTickMarkRad + thumbOffset + THUMB_GUTTER;
        final float splitCiel = taxTickMarkRad - thumbOffset - THUMB_GUTTER;
        splitDrawable.setFloorAndCiel(splitFloor, splitCiel);


        this.tipFloor = tipTickMarkRad + thumbOffset + THUMB_GUTTER;
        final float tipCiel = splitTickMarkRad - thumbOffset - THUMB_GUTTER  ;
        tipDrawable.setFloorAndCiel(tipFloor, tipCiel);


        this.taxFloor = taxTickMarkRad + thumbOffset + THUMB_GUTTER ;
        final float taxCiel = tipTickMarkRad - thumbOffset - THUMB_GUTTER;
        taxDrawable.setFloorAndCiel(taxFloor, taxCiel);


    }


    private void initBounds(){
        int trackBounds = (int)(trackSize+(trackStrokeSize*2));

        this.trackDrawable.setBounds(getPaddingLeft()+(int)(thumbSize/2), getPaddingTop()+(int)thumbSize/2, trackBounds, trackBounds);


        this.drawStartPosX = trackDrawable.getBounds().centerX();
        this.drawStartPosY = trackDrawable.getBounds().centerY();
        this.drawLengthPos = trackSize*0.5f + trackStrokeSize*0.5f;


        final int tipLeft = (int)((polarX(drawStartPosX , drawLengthPos, tipFloor)) - thumbSize*0.5f);
        final int tipTop =  (int)((polarY(drawStartPosY, drawLengthPos, tipFloor)) - thumbSize*0.5f);

        tipDrawable.setBounds(tipLeft, tipTop, tipLeft+(int)thumbSize, tipTop+(int)thumbSize);
        this.resetBounds[0] = tipDrawable.copyBounds();


        final int taxLeft = (int)((polarX(drawStartPosX , drawLengthPos, taxFloor)) - thumbSize*0.5f);
        final int taxTop =  (int)((polarY(drawStartPosY, drawLengthPos, taxFloor)) - thumbSize*0.5f);

        taxDrawable.setBounds(taxLeft, taxTop, taxLeft+(int)thumbSize, taxTop+(int)thumbSize);
        this.resetBounds[1] = taxDrawable.copyBounds();


        final int splitLeft = (int)((polarX(drawStartPosX , drawLengthPos, splitFloor)) - thumbSize*0.5f);
        final int splitTop =  (int)((polarY(drawStartPosY, drawLengthPos, splitFloor)) - thumbSize*0.5f);

        splitDrawable.setBounds(splitLeft, splitTop, splitLeft+(int)thumbSize, splitTop+(int)thumbSize);
        this.resetBounds[2] = splitDrawable.copyBounds();
    }




    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        trackDrawable.draw(canvas);

        if(drawableForUpdate != null){
            drawCurrentValue(canvas);
        }

        drawTicks(canvas);
        tipDrawable.draw(canvas);
        taxDrawable.draw(canvas);
        splitDrawable.draw(canvas);
    }

    private void drawTicks(Canvas c){
        final float tickLengthStart = trackSize*0.5f;
        final float tickLengthEnd = tickLengthStart+trackStrokeSize;

        for(int i =0; i < 3; i++){
            float sX = polarX(drawStartPosX, tickLengthStart, tickRads[i]);
            float sY = polarY(drawStartPosY, tickLengthStart, tickRads[i]);

            float eX = polarX(drawStartPosX, tickLengthEnd, tickRads[i]);
            float eY = polarY(drawStartPosY, tickLengthEnd, tickRads[i]);

            c.drawLine(sX, sY, eX, eY, tickPaint);
        }


    }

    //Switch from String concatenation to a NumberFormat for easier use
    private void drawCurrentValue(Canvas canvas){
        String value ="";
        float xPos, yPos;


        if(drawableForUpdate == tipDrawable)
            value = String.valueOf(currentTip);

        else if(drawableForUpdate == taxDrawable)
            value = String.valueOf(currentTax);

        else if(drawableForUpdate == splitDrawable)
            value = String.valueOf(currentSplit);


        if(drawableForUpdate != splitDrawable)
        value+= "%";

        trackTextPaint.getTextBounds(value, 0, value.length(), trackTextRect);


        //using canvas may cause some alignment issues with other form factors
        xPos = trackDrawable.getBounds().centerX() - trackTextPaint.measureText(value)*0.5f;
        yPos = trackDrawable.getBounds().centerY() + trackTextRect.height()/2;


        canvas.drawText(value, xPos, yPos, trackTextPaint);
    }




    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom){
        super.onLayout(changed, left, top, right, bottom);
    }





    @Override
    public void onMeasure(int wSpec, int hSpec){
        super.onMeasure(wSpec, hSpec);
        int width, height, widthMode, heightMode;

        int sizeW = 0;
        int sizeH;

        width = MeasureSpec.getSize(wSpec);
        height = MeasureSpec.getSize(hSpec);
        widthMode = MeasureSpec.getMode(wSpec);
        heightMode = MeasureSpec.getMode(hSpec);

        float paddingHor = getPaddingLeft()+getPaddingRight();
        float paddingVer = getPaddingTop()+getPaddingBottom();
        float thumbPad = thumbSize*0.5f;


        if(widthMode == MeasureSpec.EXACTLY){
            sizeW = width;
        }
        if(heightMode == MeasureSpec.EXACTLY){
            sizeH = height;
        } else {
            sizeW = (int) (trackSize + (trackStrokeSize*2) + paddingHor + thumbPad);

            sizeH = (int) (trackSize + (trackStrokeSize*2) + paddingVer + thumbPad);

        }

        setMeasuredDimension(sizeW, sizeH);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){

        switch(event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                mTouchX = event.getX();
                mTouchY = event.getY();

                if(drawableForUpdate!=null) drawableForUpdate.setFlagHasFocus(false);
                if((drawableForUpdate = isHit()) != null ){
                    setAndRefreshDrawableState(true);
                    return true;
                }

                break;

            case MotionEvent.ACTION_MOVE:
                this.mTouchX = event.getX();
                this.mTouchY = event.getY();

                float newTheta = this.getNewThumbPos();

                if(newTheta != STATE_MOVEMENT_BREAK_BOUNDARIES){
                    setNewThumbBoundariesAndInvalidate(newTheta);
                    updateCurrentValueAndListeners(newTheta);
                }else{
                    drawableForUpdate.changeColor(false);
                    invalidateDrawable(drawableForUpdate);
                    return false;
                }

                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                setAndRefreshDrawableState(false);
                break;
        }

        return false;
    }


    public ThumbDrawableBase isHit(){
        if(tipDrawable.getBounds().contains((int)mTouchX, (int)mTouchY))
            return tipDrawable;

        else if (splitDrawable.getBounds().contains((int)mTouchX, (int)mTouchY))
            return splitDrawable;

        else if(taxDrawable.getBounds().contains( (int)mTouchX, (int)mTouchY ))
            return taxDrawable;

        return  null;
    }

    private void setNewThumbBoundariesAndInvalidate(float newAngle){

        float newLeft = polarX(drawStartPosX, drawLengthPos, newAngle) - thumbSize*0.5f;
        float newTop = polarY(drawStartPosY, drawLengthPos, newAngle) - thumbSize*0.5f;

        drawableForUpdate.setBounds((int)newLeft, (int)newTop, (int)(newLeft+thumbSize), (int)(newTop+thumbSize));

        invalidateDrawable(drawableForUpdate);
    }

    private float getNewThumbPos(){
        mTouchX -= trackDrawable.getBounds().centerX();
        mTouchY -= trackDrawable.getBounds().centerY();

        float theta = (float)Math.atan2(mTouchY, mTouchX);
        if(theta <= 0) theta += (Math.PI*2);

        final int thumbDirection = (drawableForUpdate.getCielRadians() > drawableForUpdate.getFloorRadians()) ? 1 : 0;

        if(thumbDirection == 1) {
            if ((theta <= drawableForUpdate.getCielRadians() & theta >= drawableForUpdate.getFloorRadians()))
                return theta;

        } else if(thumbDirection==0) {
            if (theta >= drawableForUpdate.getFloorRadians() & theta <= (Math.PI * 2) | theta <= (Math.PI * 2) & theta <= drawableForUpdate.getCielRadians())
                return theta;
        }



        return STATE_MOVEMENT_BREAK_BOUNDARIES;
    }

    private void updateCurrentValueAndListeners(float theta){

        float radianValue = theta - drawableForUpdate.getFloorRadians();
        float totalValue = (drawableForUpdate.getCielRadians() - drawableForUpdate.getFloorRadians());



        if(drawableForUpdate == taxDrawable) {
            this.currentTax = (int)(((radianValue / totalValue) * .31f) * 100.0f);
            onValueChangedListener.taxValueChanged(currentTax);
        }

        else if(drawableForUpdate == tipDrawable){
            totalValue = (float)((Math.PI*2) - drawableForUpdate.getFloorRadians()) + (drawableForUpdate.getCielRadians());
            if(radianValue < 0) radianValue+= (float)Math.PI*2;
            this.currentTip = (int)(((radianValue / totalValue) * .31f) * 100.0f);
            onValueChangedListener.tipValueChanged(currentTip);
        }

        else if(drawableForUpdate == splitDrawable){
            this.currentSplit =  Math.round(((radianValue / totalValue) * 0.8f ) * 10.0f);
            onValueChangedListener.splitValueChanged(currentSplit);
        }

    }

    @Override
    public void invalidateDrawable(Drawable who){
        int l = who.getBounds().left;
        int t = who.getBounds().top;
        int r = who.getBounds().right;
        int b = who.getBounds().bottom;
        invalidate(l, t, r, b);
    }

    private void setAndRefreshDrawableState(boolean touched){
        drawableForUpdate.setFlagHasFocus(true);
        drawableForUpdate.changeColor(touched);
          invalidateDrawable(drawableForUpdate);
    }




    public void reset(){

        if(drawableForUpdate !=null){
            drawableForUpdate.setFlagHasFocus(false);
            drawableForUpdate = null;
        }

        tipDrawable.setBounds(resetBounds[0]);
          taxDrawable.setBounds(resetBounds[1]);
            splitDrawable.setBounds(resetBounds[2]);

        invalidateDrawable(tipDrawable);
          invalidateDrawable(taxDrawable);
            invalidateDrawable(splitDrawable);

        currentTax = 0;
          currentTip = 0;
            currentSplit = 0;
    }




    public void setOnValueChangedListener(OnValueChangedListener changedListener) {
        this.onValueChangedListener = changedListener;
    }




    /**
     *
     * @param start The scale bias of the value. I.E. where to start. 0 if we don't want to start anywhere but top/left
     * @param howFar The radius of the formula (R * Theta)
     * @param direction the theta value of the formula (r * THETA)
     * @return The polar coordinate of the cartesian value
     */
    public static float polarX(float start, float howFar, float direction){
        return (start + howFar * (float)Math.cos(direction));
    }
    public static float polarY(float start, float howFar, float direction) {
        return (start + howFar * (float) Math.sin(direction));
    }


    public interface OnValueChangedListener{
        void tipValueChanged(int newTip);
        void taxValueChanged(int newTax);
        void splitValueChanged(int newSplit);

    }


}

