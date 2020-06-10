package app.gestureproject.view;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;

import app.gestureproject.R;
import app.gestureproject.render.GestureDrawingView;

public class GestureDrawingActivity extends AppCompatActivity {

    public static int width;
    public static int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_drawing);
    }

    public void process(View v){
       GestureDrawingView view = findViewById(R.id.gestureDrawingView);
       float accuracy = view.processAccuracy();
       accuracy *= 100;
       
       Intent intent = new Intent(this, CompleteActivity.class);
       intent.putExtra("game", "GestureDrawing");
       intent.putExtra("resultText", "Your accuracy: ");
       intent.putExtra("result", ((int) accuracy) + "%");
       startActivity(intent);
       Log.e("!!!***ACCURACY***!!!",accuracy + "");
    }
}
