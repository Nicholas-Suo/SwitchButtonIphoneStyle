package tech.xiaosuo.com.switchbutton;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class SwitchButtonView extends View {
    private static final String TAG = "SwitchButtonView";
    private static final int MAX_WIDTH = 100;
    private static final int MAX_HEIGHT = MAX_WIDTH/2;
    private static final int MIN_WIDTH = 40;
    private static final int MIN_HEIGHT = MIN_WIDTH/2;
    boolean isOn = false;
    int buttonWidth;
    int buttonHeight;
    float firstDownX;
    float firstDownY;
    float moveDistance = 0;
    public SwitchButtonView(Context context) {
        super(context);
    }

    public SwitchButtonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SwitchButtonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //make sure the width is height*2
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        buttonWidth = getWidth();
        buttonHeight = getHeight();
         if(buttonWidth > MAX_WIDTH){
             buttonWidth = MAX_WIDTH;
         }else if(buttonWidth < MIN_WIDTH){
             buttonWidth = MIN_WIDTH;
         }
        buttonHeight = buttonWidth/2;

        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSwitchButtonMove(canvas);
      //  drawSwitchButtonView(canvas);
/*        if(isOn == false){
            drawOff(canvas);
        }else {
            drawOn(canvas);
        }*/
    }

    private void drawOff(Canvas canvas){
        if(canvas == null){
            return;
        }
        int width = getWidth();
        int height = getHeight();
        Log.d(TAG," the switchbutton width: " + width + " height: " + height);

            width = buttonWidth;
            height = buttonHeight;
        Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //draw white bg
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(Color.GRAY);
        int left = 0;
        int top = 0;
        int right = width - left;
        int bottom = height - top;
        RectF rectF = new RectF(left,top,right,bottom);
        float rx = height/2;
        float ry = rx;
        canvas.drawRoundRect(rectF,rx,ry,bgPaint);
        //draw shadow
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeWidth(2);
        bgPaint.setColor(Color.WHITE);
        canvas.drawRoundRect(rectF,rx,ry,bgPaint);

        Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.WHITE);
        float cx = rx;
        float cy = ry;
        float circleRadius = rx-2;
        canvas.drawCircle(cx,cy,circleRadius,circlePaint);
    }

    private void drawOn(Canvas canvas){
        int width = getWidth();
        int height = getHeight();

        width = buttonWidth;
        height = buttonHeight;
        Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //draw white bg
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(Color.GREEN);


        int left = 0;
        int top = 0;
        int right = width - left;
        int bottom = height - top;
        RectF rectF = new RectF(left,top,right,bottom);
        float rx = height/2;
        float ry = rx;
        canvas.drawRoundRect(rectF,rx,ry,bgPaint);
        //draw shadow
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeWidth(2);
        bgPaint.setColor(Color.WHITE);
        canvas.drawRoundRect(rectF,rx,ry,bgPaint);

        Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.WHITE);
        float cx = right - left - rx;
        float cy = ry;
        float circleRadius = rx - 2;
        canvas.drawCircle(cx,cy,circleRadius,circlePaint);
    }

    private void drawSwitchButtonMove(Canvas canvas){
        int width = buttonWidth;
        int height = buttonHeight;
        Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //draw white bg
        bgPaint.setStyle(Paint.Style.FILL);
        if(isOn) {// has open
            bgPaint.setColor(Color.GREEN);
        }else {
            bgPaint.setColor(Color.GRAY);
        }

        int left = 0;
        int top = 0;
        int right = width - left;
        int bottom = height - top;
        RectF rectF = new RectF(left,top,right,bottom);
        float rx = height/2;
        float ry = rx;
        canvas.drawRoundRect(rectF,rx,ry,bgPaint);
        //draw shadow
/*        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeWidth(2);
        bgPaint.setColor(Color.WHITE);
        canvas.drawRoundRect(rectF,rx,ry,bgPaint);*/

        Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.WHITE);
        float cx = 0;
        if(isOn) {// has open
            cx = right - left - rx;  // the circle center x when open
        }else {
            cx = rx;  // the circle center x when close
        }



        float cy = ry;
        float circleRadius = rx - 2;

/*
        if( moveDistance >=  right){//避免超出图标的宽度
            moveDistance = right;
        }
*/
       //draw bg when move.
        Paint movePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        movePaint.setStyle(Paint.Style.FILL);
        if(moveDistance > 0){//draw the move bg  画滑动背景图  from left to right when is off
            movePaint.setColor(Color.GREEN);
            float moveRight = left + moveDistance + 2*circleRadius;//why +2*circleRadius,because bg need be under the circle .
            if(moveRight > right){//防止超过图标宽度
                moveRight = right;
            }
            RectF rectFMove = new RectF(left,top, moveRight ,bottom);
            canvas.drawRoundRect(rectFMove,rx,ry,movePaint);
            //draw circle begin
            cx = cx + moveDistance;
            float rightCenter = right - left - rx;
            if(cx > rightCenter){
                cx = rightCenter;
            }
            canvas.drawCircle(cx,cy,circleRadius,circlePaint);
            //draw circle end

        }else if(moveDistance < 0){ // from right to left when is on
            movePaint.setColor(Color.GRAY);
            float moveLeft = right + moveDistance - 2*circleRadius;//why +2*circleRadius,because bg need be under the circle .
            if(moveLeft < left){
                moveLeft = left;
            }
            RectF rectFMove = new RectF(moveLeft,top, right ,bottom);
            canvas.drawRoundRect(rectFMove,rx,ry,movePaint);
           //draw circle begin
            cx = right + moveDistance - circleRadius;
            if(cx < rx){
                cx = rx;
            }
            canvas.drawCircle(cx,cy,circleRadius,circlePaint);
            //draw circle end
        }else {// when not move ,moveDistance is 0
            canvas.drawCircle(cx,cy,circleRadius,circlePaint);
        }

/*        //draw circle when move
       if(moveDistance > 0){
            cx = cx + moveDistance;
            float rightCenter = right - left - rx;
            if(cx > rightCenter){
                cx = rightCenter;
            }

       }else if(moveDistance < 0){
           cx = right + moveDistance - circleRadius;
           if(cx < rx){
               cx = rx;
           }
       }
        canvas.drawCircle(cx,cy,circleRadius,circlePaint);*/
    }

    private void drawSwitchButtonView(Canvas canvas){
        int width = buttonWidth;
        int height = buttonHeight;
        Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //draw white bg
        bgPaint.setStyle(Paint.Style.FILL);
        if(isOn) {// has open
            bgPaint.setColor(Color.GREEN);
        }else {
            bgPaint.setColor(Color.GRAY);
        }

        int left = 0;
        int top = 0;
        int right = width - left;
        int bottom = height - top;
        RectF rectF = new RectF(left,top,right,bottom);
        float rx = height/2;
        float ry = rx;
        canvas.drawRoundRect(rectF,rx,ry,bgPaint);
        //draw shadow
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeWidth(2);
        bgPaint.setColor(Color.WHITE);
        canvas.drawRoundRect(rectF,rx,ry,bgPaint);

        Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.WHITE);
        float cx = 0;
        if(isOn) {// has open
           cx = right - left - rx;
        }else {
            cx = rx;
        }

        float cy = ry;
        float circleRadius = rx - 2;
        canvas.drawCircle(cx,cy,circleRadius,circlePaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        Log.d(TAG, " touch aciont is: " + action);
        switch(action){
            case MotionEvent.ACTION_DOWN:
                firstDownX = x;
                firstDownY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                    moveDistance = x - firstDownX;
                    invalidate();
                    break;
            case MotionEvent.ACTION_UP:
                if(isOn){
                    isOn =false;
                }else{
                    isOn = true;
                }
                moveDistance = 0;
                invalidate();
                break;
                default:
                    break;
        }
        return true;
    }
}
