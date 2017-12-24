package com.example.adeelturk.swipelefttoright;


import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class OnSwipeTouchListener implements OnTouchListener {


    float dX, dY;

    private View viewToAnimate;
    private View pointToBeNoted;

    private OnExitListener onExitListener;
    private boolean isExit;

    public OnSwipeTouchListener(Context ctx, View viewToAnimate, View pointToBeNoted, OnExitListener onExitListener) {

        this.viewToAnimate = viewToAnimate;
        this.pointToBeNoted = pointToBeNoted;
        this.onExitListener = onExitListener;
        isExit = false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                isExit = false;
                dX = viewToAnimate.getX() - event.getRawX();
                dY = viewToAnimate.getY(); /*- event.getRawY();*/
                break;

            case MotionEvent.ACTION_MOVE:

                // restricting the textview so that it could no move out of its container

                if ((event.getRawX() + dX) + viewToAnimate.getWidth() <= view.getWidth()) {

                    viewToAnimate.animate()
                            .x(event.getRawX() + dX)
                            .y(dY)
                            .setDuration(0)
                            .start();

                    // TODO you enhance the logic below

                    if (!isExit) {
                        float currentX2OfMovingTextView = event.getRawX() + dX + viewToAnimate.getWidth();
                        float centerXPonitOfPointToBeNotedTextView = pointToBeNoted.getX() + (pointToBeNoted.getWidth() / 2);
                        if (currentX2OfMovingTextView >= centerXPonitOfPointToBeNotedTextView) {

                            Log.v("adeel", "ok hogia");
                            isExit = true;
                            onExitListener.onExit();
                        }

                    }

                }
                // Log.v("adeel","moved "+dX);
                break;

            case MotionEvent.ACTION_UP:

                if (!isExit) {
                    float currentX2OfMovingTextView = event.getRawX() + dX + viewToAnimate.getWidth();
                    float centerXPonitOfPointToBeNotedTextView = pointToBeNoted.getX() + (pointToBeNoted.getWidth() / 2);
                    if (currentX2OfMovingTextView < centerXPonitOfPointToBeNotedTextView) {

                        Log.v("adeel", "ok hogia");
                        onExitListener.onCancel();
                    }

                }


                viewToAnimate.animate()
                        .x(0)
                        .y(dY)
                        .setDuration(1000) // 1000 is delay in milisec you can change it accordinly
                        .start();
                break;
            default:
                return false;


        }
        return true;
        //   return gestureDetector.onTouchEvent(event);
    }


}
