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
    private boolean mChecked = false;

    private int buttonWidth;
    private int buttonHeight;
    private float firstDownX;
    private float firstDownY;
    private float moveDistance = 0;

    private static final int MOVE_TO_RIGHT = 1;
    private static final int MOVE_TO_LEFT = -1;
    private static final int MOVE_NONE = 0;
    private int moveDirection = MOVE_NONE;
    SwitchButtonClickListener mListener;
    public void setChecked(boolean checked) {
        if(mChecked != checked){
            mChecked = checked;
        }
        invalidate();
    }

    public boolean isChecked(){
        return mChecked;
    }
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

    //only draw not checked view
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

    /*//only draw checked view
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
    }*/

    /**
     * show the current status,and deal with the touch move envent.
     * @param canvas
     */
    private void drawSwitchButtonMove(Canvas canvas){
        int width = buttonWidth;
        int height = buttonHeight;
        Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //draw white bg
        bgPaint.setStyle(Paint.Style.FILL);
        if(isChecked()) {// has open
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
        canvas.drawRoundRect(rectF,rx,ry,bgPaint);//draw the origin bg
        //draw shadow
/*        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeWidth(2);
        bgPaint.setColor(Color.WHITE);
        canvas.drawRoundRect(rectF,rx,ry,bgPaint);*/
        //draw cicle
        Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.WHITE);
        float cx = 0;
        if(isChecked()) {// has open
            cx = right - left - rx;  // the circle center x when open
        }else {
            cx = rx;  // the circle center x when close
        }

        float cy = ry;
        float circleRadius = rx - 2;

        Log.d(TAG, " moveDistance is: " + moveDistance);
       //draw bg when move.
        Paint movePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        movePaint.setStyle(Paint.Style.FILL);
        if((moveDirection == MOVE_TO_RIGHT) && (moveDistance>0)){//draw the move bg  画滑动背景图  from left to right when is off //moveDistance > 0 &&
            movePaint.setColor(Color.GREEN);
            float moveRight = left + moveDistance + 2*circleRadius;//why +2*circleRadius,because bg need be under the circle .
            if(moveRight > right){//防止超过图标宽度
                moveRight = right;
            }


            RectF rectFMove = new RectF(left,top, moveRight ,bottom);
            canvas.drawRoundRect(rectFMove,rx,ry,movePaint);
            //draw circle begin
            float originCx = cx;
            cx = cx + moveDistance;

            float rightCenter = right - left - rx;
            if(cx > rightCenter){
                cx = rightCenter;
            }
            canvas.drawCircle(cx,cy,circleRadius,circlePaint);
            //draw circle end

        }else if((moveDirection == MOVE_TO_LEFT) && (moveDistance < 0)){ // from right to left when is on // moveDistance < 0
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

    }
//only show button
    /*private void drawSwitchButtonView(Canvas canvas){
        int width = buttonWidth;
        int height = buttonHeight;
        Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //draw white bg
        bgPaint.setStyle(Paint.Style.FILL);
        if(isChecked()) {// has open
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
        if(isChecked()) {// has open
           cx = right - left - rx;
        }else {
            cx = rx;
        }

        float cy = ry;
        float circleRadius = rx - 2;
        canvas.drawCircle(cx,cy,circleRadius,circlePaint);
    }*/


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
                    if(moveDirection == MOVE_NONE){
                      if(moveDistance > 0){
                          moveDirection = MOVE_TO_RIGHT;
                      }else if(moveDistance < 0){
                          moveDirection = MOVE_TO_LEFT;
                      }
                    }
                    invalidate();
                    break;
            case MotionEvent.ACTION_UP:
                boolean status = isChecked();
                //when the click or move instance > (buttonWidth/2) - (buttonHeight/2) ,the swithc button need change status.
                if( ((moveDistance == 0) && (moveDirection == MOVE_NONE))
                        || ((moveDirection == MOVE_TO_RIGHT) && (moveDistance > 0)  && (Math.abs(moveDistance) >  (buttonWidth/2) - (buttonHeight/2) ))
                        ||  ((moveDirection == MOVE_TO_LEFT) && (moveDistance < 0)  && (Math.abs(moveDistance) >  (buttonWidth/2) - (buttonHeight/2) ))
                   )
                {
                    setChecked(!status);
                    if(mListener != null){//add the callback function when the checke status changed
                        mListener.OnSwitchButtonClick();
                    }
                }else{
                    setChecked(status);
                }
              //  invalidate();
                moveDistance = 0;//if moveDistance is eaull 0, the user has stopped the move.
                moveDirection = MOVE_NONE;
                break;
                default:
                    break;
        }
        return true;
    }

   void setOnSwitchButtonListener(SwitchButtonClickListener listener){
       mListener = listener;
    }

    public interface SwitchButtonClickListener{
        void OnSwitchButtonClick();
    }
}
