package app.gestureproject.render;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
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

import app.gestureproject.manager.GameManager;
import app.gestureproject.view.GestureModeActivity;

public class GestureGuessView extends View implements GestureDetector.OnGestureListener{

    private final Stack<Integer> stack = new Stack<Integer>();
    private final Map<Integer, String> directionMap = new HashMap<Integer, String>();
    private final ArrayList<List<Integer>> solutions = new ArrayList<List<Integer>>();
    private final Map<Integer, Runnable> drawFunctionMap = new HashMap<Integer, Runnable>();

    private int gestureCheckOption;
    private GestureModeActivity activity;

    private float lastX = -1;
    private float lastY = -1;
    private final float ERROR_PIXELS = 20;

    private Canvas canvas;
    private int currentShape = 0;

    int offsetX = 375;
    int offsetY = 425;

    int sizeX = 100;
    int sizeY = 200;

    private  final Paint paint = new Paint();

    public GestureGuessView(Context context) {
        super(context);
        init(context);
    }

    public GestureGuessView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GestureGuessView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        //SoMETHING Here!
        directionMap.put(1, "UP_LEFT");
        directionMap.put(2, "UP_RIGHT");
        directionMap.put(4, "DOWN_LEFT");
        directionMap.put(5, "DOWN_RIGHT");

        initFunctions();

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(GameManager.fontSize);
        paint.setColor(Color.BLACK);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        this.canvas = canvas;
        clearCanvas();

        drawFunctionMap.get(currentShape).run();
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

                lastX = event.getX();
                lastY = event.getY();

                stack.clear();
                stack.push(-1); // stack at 0 == invalid

                return true;
            case (MotionEvent.ACTION_UP):
                Log.d("Event: ", "UP");
                stack.remove(0);

                for(Integer i : stack)
                    Log.e("ErrorEpt", i + "");

                if(gestureCheckOption == 0) {
                    if (stack.equals(solutions.get(0)) || stack.equals(solutions.get(1)))
                        nextShape(++currentShape);


                } else if(gestureCheckOption == 1)
                    if(stack.size() >= solutions.get(0).size())
                        if(solutionCheck(solutions.get(0)) || solutionCheck(solutions.get(1)))
                            nextShape(++currentShape);




                return true;

            case (MotionEvent.ACTION_MOVE):
                float dx = event.getX() - lastX;
                float dy = event.getY() - lastY;

                if(Math.abs(dx) > ERROR_PIXELS || Math.abs(dy) > ERROR_PIXELS) {
                    int directionX = dx < 0 ? 1 : 2;
                    int directionY = dy < 0 ? 0 : 3;

                    int direction = directionX + directionY;
                    Log.d("look here please", direction + "");

                    if (!stack.peek().equals(direction))
                        stack.push(directionX + directionY);

                    lastX = event.getX();
                    lastY = event.getY();
                }
        }

        return false;
    }
    //Gestures END


    private boolean solutionCheck(List<Integer> solution){
        int counter = 0;

        for (int i = 0; i < solution.size(); i++)
            if (stack.get(0) == solution.get(i)) {
                for (int j = 0; j < stack.size(); j++)
                    if (stack.get(j % stack.size()) == solution.get(i++ % solution.size()))
                        counter++;
            }
        float accuracy = ((float) counter) / ((float)stack.size());
        return accuracy >= 0.8;
    }

    private void vShapeGesture(){
        gestureCheckOption = 0;
        solutions.add(Arrays.asList(5, 2));
        solutions.add(Arrays.asList(4, 1));

        canvas.drawLine(offsetX, offsetY,
                offsetX + sizeX, offsetY + sizeY, paint);

        canvas.drawLine(offsetX + sizeX, offsetY + sizeY,
                offsetX + sizeX * 2, offsetY, paint);
    }

    private void circleShapeGesture(){
        gestureCheckOption = 1;
        solutions.add(Arrays.asList(5, 4, 1, 2));
        solutions.add(Arrays.asList(4, 5, 2, 1));

        int radius = sizeX + sizeX;
        int innerRadius = radius - 50;

        canvas.drawCircle(offsetX + sizeX, offsetY + sizeY, radius, paint);
        paint.setColor(Color.WHITE);

        canvas.drawCircle(offsetX + sizeX, offsetY + sizeY, innerRadius, paint);
        paint.setColor(Color.BLACK);
    }

    private void aShapeGesture(){
        gestureCheckOption = 0;
        solutions.add(Arrays.asList(2, 5));
        solutions.add(Arrays.asList(1, 4));

        canvas.drawLine(offsetX, offsetY + sizeY,
                offsetX + sizeX, offsetY , paint);

        canvas.drawLine(offsetX + sizeX, offsetY,
                offsetX + sizeX * 2, offsetY + sizeY, paint);
    }

    private void vLeftShapeGesture(){
        gestureCheckOption = 0;
        solutions.add(Arrays.asList(4, 5));
        solutions.add(Arrays.asList(1, 2));

        canvas.drawLine(offsetX + sizeX * 2, offsetY,
                offsetX, offsetY + sizeY , paint);

        canvas.drawLine(offsetX, offsetY + sizeY,
                offsetX + sizeX * 2, offsetY + sizeY * 2, paint);
    }

    private void vRightShapeGesture(){
        gestureCheckOption = 0;
        solutions.add(Arrays.asList(5, 4));
        solutions.add(Arrays.asList(2, 1));

        canvas.drawLine(offsetX, offsetY,
                offsetX + sizeX * 2, offsetY + sizeY , paint);

        canvas.drawLine(offsetX + sizeX * 2, offsetY + sizeY,
                offsetX, offsetY + sizeY * 2, paint);
    }

    private void diamondShapeGesture(){
        gestureCheckOption = 1;
        solutions.add(Arrays.asList(5, 4, 1, 2));
        solutions.add(Arrays.asList(4, 5, 2, 1));

        canvas.drawLine(offsetX + sizeX, offsetY,
                offsetX - sizeX, offsetY + sizeY, paint);

        canvas.drawLine(offsetX - sizeX, offsetY + sizeY,
                offsetX + sizeX, offsetY + sizeY * 2, paint);

        canvas.drawLine(offsetX + sizeX, offsetY + sizeY * 2,
                offsetX + sizeX * 3, offsetY + sizeY, paint);

        canvas.drawLine(offsetX + sizeX * 3, offsetY + sizeY,
                offsetX + sizeX, offsetY, paint);
    }

    private void clearCanvas() {
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
        paint.setColor(Color.BLACK);
    }

    private void nextShape(int i){
        if(currentShape == drawFunctionMap.size())
            activity.gameComplete();
        else {
            Toast.makeText(this.getContext(), "Well Done!", Toast.LENGTH_SHORT).show();
            solutions.clear();
            this.invalidate();
        }
    }

    public void setActivity(GestureModeActivity activity){
        this.activity = activity;
    }

    private void initFunctions() {
        drawFunctionMap.put(0, new Runnable() {
            @Override
            public void run() {
                vShapeGesture();
            }
        });

        drawFunctionMap.put(1, new Runnable() {
            @Override
            public void run() {
                circleShapeGesture();
            }
        });

        drawFunctionMap.put(2, new Runnable() {
            @Override
            public void run() {
                aShapeGesture();
            }
        });

        drawFunctionMap.put(3, new Runnable() {
            @Override
            public void run() {
                vLeftShapeGesture();
            }
        });

        drawFunctionMap.put(4, new Runnable() {
            @Override
            public void run() {
                vRightShapeGesture();
            }
        });

        drawFunctionMap.put(5, new Runnable() {
            @Override
            public void run() {
                diamondShapeGesture();
            }
        });
    }
}
