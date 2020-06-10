package app.gestureproject.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import app.gestureproject.R;
import app.gestureproject.render.GestureGuessView;

public class GestureModeActivity extends AppCompatActivity {
    private  GestureGuessView gestureView;
    private long time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_mode);
    }

    @Override
    protected void onStart(){
        super.onStart();
        gestureView = findViewById(R.id.gestureView);
        gestureView.setActivity(this);
        time = System.currentTimeMillis();
    }

    public void gameComplete(){
        time = System.currentTimeMillis() - time;
        time /= 1000;
        Intent intent = new Intent(this, CompleteActivity.class);
        intent.putExtra("game",  "Gesture Guess");
        intent.putExtra("resultText", "Your time: ");
        intent.putExtra("result", time + " seconds");
        startActivity(intent);
    }

}
