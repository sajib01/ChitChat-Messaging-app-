package com.sajib.chitchat;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by sajib on 11/10/16.
 */

public class Timerview extends View {
    private float OuterCircleRadius;
    private float barRadius;
    private float CX;
    private float CY;
    private int originX;
    private int originY;
    private int degree=270;
    private float X;
    private float Y;
    float prevoius=270;
    int value=0;
    private float animationDuration=2000;

    public Timerview(Context context) {
        super(context);
    }

    public Timerview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Timerview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Timerview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {

            Paint outerpaint = new Paint();
            outerpaint.setColor(Color.WHITE);
            outerpaint.setStyle(Paint.Style.STROKE);
            outerpaint.setStrokeWidth(30);
            outerpaint.setAntiAlias(true);

            originX = getMeasuredWidth() / 2;
            originY = getMeasuredHeight() / 2;

            if (getMeasuredHeight() > getMeasuredWidth()) {
                OuterCircleRadius = (float) (getMeasuredWidth() / 2.5);
            }

            if (getMeasuredHeight() < getMeasuredWidth()) {
                OuterCircleRadius = (float) (getMeasuredHeight() / 2.5);
            }

            Paint blurback = new Paint();
            blurback.setAntiAlias(true);
            BlurMaskFilter blurMaskFilter = new BlurMaskFilter(40.F, BlurMaskFilter.Blur.OUTER);
            blurback.setMaskFilter(blurMaskFilter);
            setLayerType(View.LAYER_TYPE_SOFTWARE, blurback);

            canvas.drawCircle(originX, originY, OuterCircleRadius, blurback);

            canvas.drawCircle(originX, originY, OuterCircleRadius, outerpaint);

            Paint innerpaint = new Paint();
            innerpaint.setColor(Color.GRAY);
            innerpaint.setStyle(Paint.Style.FILL);
            innerpaint.setAntiAlias(true);

            canvas.drawCircle(originX, originY, OuterCircleRadius - 16, innerpaint);

        Paint barpaint=new Paint();
        barpaint.setStyle(Paint.Style.FILL_AND_STROKE);
        barpaint.setColor(0xFFFFA500);
        barpaint.setAntiAlias(true);
        barRadius=50;

        float arcRectStartingX = (originX - OuterCircleRadius);
        float arcRectStartingY = (originY - OuterCircleRadius);
        float arcRectEndingX = (originX + OuterCircleRadius);
        float arcRectEndingY = (originY + OuterCircleRadius);

        Paint arc=new Paint();
        arc.setStyle(Paint.Style.STROKE);
        arc.setStrokeWidth(30);
        arc.setColor(0xFFFFA500);
        arc.setAntiAlias(true);

        canvas.drawArc(arcRectStartingX,arcRectStartingY,arcRectEndingX,arcRectEndingY,270,degree-270,false,arc);

        CX=getpositionInX(originX,degree);
        CY=getpositionInY(originY,degree);

        canvas.drawCircle(CX,CY,barRadius,barpaint);

        Paint Textpaint=new Paint();
        Textpaint.setStyle(Paint.Style.FILL_AND_STROKE);
        Textpaint.setColor(0xFFFFA500);
        Textpaint.setAntiAlias(true);
        Textpaint.setTextSize(106);
        Textpaint.setTextAlign(Paint.Align.CENTER);

        float positionX = (float) (getMeasuredWidth() / 2);
        float positionY = (float) ((getMeasuredHeight() / 2) - ((Textpaint.descent() + Textpaint.ascent()) / 2));
        canvas.drawText(String.valueOf(value), positionX, positionY, Textpaint);





    }


    private void startanimation(int newDegree)
    {
        float prev = 0;
        prev=prevoius;
        final ValueAnimator animator = ValueAnimator.ofFloat(prev,newDegree);
        int changeInValue = (int) Math.abs(newDegree - prevoius);
        long durationToUse = (long) (animationDuration * ((float) changeInValue / (float) 360));
        animator.setDuration(durationToUse);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = ((Float) (animation.getAnimatedValue())).floatValue();
                degree= (int) val;
                Timerview.this.value = (int) (val-270)/2;
                invalidate();

            }


        });


        animator.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        getParent().requestDisallowInterceptTouchEvent(true);
        if(event.getAction()==MotionEvent.ACTION_DOWN)
        {
            X=event.getX();
            Y=event.getY();
            int o= (int) Math.sqrt(Math.pow(X-originX,2)+Math.pow(Y-originY,2));

            if(o>=(int)(OuterCircleRadius-40)&&o<=(int)(OuterCircleRadius+40)) {

                startanimation((int) (FindDegree(X, Y)+270));
                prevoius=FindDegree(X, Y)+270;

            }
        }

        if(event.getAction()==MotionEvent.ACTION_MOVE)
        {
            X=event.getX();
            Y=event.getY();

            int o= (int) Math.sqrt(Math.pow(X-originX,2)+Math.pow(Y-originY,2));

            if(o>=(int)(OuterCircleRadius-40)&&o<=(int)(OuterCircleRadius+40)) {

                degree= (int) (FindDegree(X, Y)+270);
                value=(int)(degree-270)/2;
                prevoius=degree;
                invalidate();

            }
            return true;
        }
        return true;

    }

    private float FindDegree(float x, float y)
    {
        float angle = (float) Math.toDegrees(Math.atan2(x-originX, originY - y));

        if(angle < 0){
            angle += 360;
        }
        return angle;
    }




    public float getpositionInX(float originX, float degree) {

        float x = (float)(OuterCircleRadius * Math.cos(degree*Math.PI/180)) + originX;
        return x;
    }

    public float getpositionInY(float originY, float degree) {
        float y = (float)(OuterCircleRadius * Math.sin(degree*Math.PI/180)) + originY;
        return y;
    }

    public int getValue()
    {
        return value;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SaveState ss = new SaveState(superState);
        ss.value = value;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SaveState ss = (SaveState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        value = ss.value;
    }

    static class SaveState extends BaseSavedState{

        int value; //this will store the current value from ValueBar

        SaveState(Parcelable superState) {
            super(superState);
        }

        private SaveState(Parcel in) {
            super(in);
            value = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(value);
        }

        public static final Creator<SaveState> CREATOR
                = new Creator<SaveState>() {
            public SaveState createFromParcel(Parcel in) {
                return new SaveState(in);
            }

            public SaveState[] newArray(int size) {
                return new SaveState[size];
            }
        };
    }
}
