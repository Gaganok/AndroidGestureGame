package app.gestureproject.render;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import app.gestureproject.R;
import app.gestureproject.manager.GameManager;
import app.gestureproject.view.GestureDrawingActivity;
import app.gestureproject.view.GestureModeActivity;

public class GestureDrawingView extends View implements GestureDetector.OnGestureListener{

    private ArrayList<Cord> current = new ArrayList<Cord>();

    private int offsetX = 200;
    private int offsetY = 200;

    private Canvas canvasBit;
    private Bitmap bitmapCanvas;
    private Bitmap bitmapShape;

    private int[] goal;
    private  final Paint paint = new Paint();

    public GestureDrawingView(Context context) {
        super(context);
        init(context);
    }

    public GestureDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GestureDrawingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(GameManager.fontSize);
        paint.setColor(Color.BLACK);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setAntiAlias(true);

        this.post(new Runnable() {
            @Override
            public void run() {
                bitmapCanvas = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                canvasBit = new Canvas(bitmapCanvas);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                bitmapShape = BitmapFactory.decodeResource(getResources(), R.drawable.david_star, options);

                canvasBit.drawBitmap(bitmapShape, offsetX, offsetY, null);

                goal = new int[bitmapCanvas.getHeight() * bitmapCanvas.getWidth()];
                bitmapCanvas.getPixels(goal, 0, bitmapCanvas.getWidth(), 0, 0,
                        bitmapCanvas.getWidth(), bitmapCanvas.getHeight());

                canvasBit.drawColor(Color.argb(100, 255, 255, 255));
            }
        });


    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        canvas.drawBitmap(bitmapCanvas, 0, 0, null);
    }

    //Gestures START
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case (MotionEvent.ACTION_DOWN):
                Log.d("Event: ", "Down");
                return true;
            case (MotionEvent.ACTION_UP):
                Log.d("Event: ", "UP");
                return true;

            case (MotionEvent.ACTION_MOVE):
                Log.d("Event: ", "Move");
                current.add(new Cord(event.getX(), event.getY()));
                drawCanvas();
                invalidate();
        }

        return false;
    }
    //Gestures END

    public float processAccuracy(){
        int[] pixels = new int[bitmapCanvas.getWidth() * bitmapCanvas.getHeight()];
        bitmapCanvas.getPixels(pixels, 0, bitmapCanvas.getWidth(), 0, 0,
                bitmapCanvas.getWidth(), bitmapCanvas.getHeight());

        double accuracyRate = 1.55;
        double counter = 0;
        int goalCounter = 0;

        for(int i = 0; i < pixels.length; i++){
            if(goal[i] == Color.BLACK)
                goalCounter++;

            if(pixels[i] == Color.BLACK)
                if(pixels[i] == goal[i])
                    counter += accuracyRate;
                else
                    counter--;
        }

        if (counter < 0)
            counter = 0;
        else if (counter > goalCounter)
            counter = goalCounter;
        
        float accuracy = ((float) counter) / ((float) goalCounter);
        return accuracy;
    }

    private void drawCanvas(){
        if(current.size() > 1)
            for(int i = 0; i < current.size() - 1; i++) {
                Cord c1 = current.get(i);
                Cord c2 = current.get(i + 1);
                canvasBit.drawLine(c1.x, c1.y, c2.x, c2.y, paint);
            }
    }

    private class Cord{
        float x;
        float y;

        public Cord(float x, float y){
            this.x = x;
            this.y = y;
        }
    }
}
