package app.gestureproject.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import app.gestureproject.R;

public class TutorialActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private TextView header;
    private TextView text;

    private final int PAGE_COUNT = 3;

    private String[] headerList;
    private String[] textList;

    private int currentPage = 0;
    private GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        header = findViewById(R.id.headerView);
        text = findViewById(R.id.textView);

        headerList = new String[PAGE_COUNT];
        textList = new String[PAGE_COUNT];

        loadText();
        tutorialTextInit();
        detector = new GestureDetector(this, this);
    }

    private void loadText(){
        //Over here text for this tutorial could be loaded from file or db,
        // but I will leave it hardcode for now

        headerList[0] = "Tutorial";
        headerList[1] = "Gesture Guessing Game";
        headerList[2] = "Gesture Drawing Game";

        textList[0] = "This app contains two games, in the main menu pick one with a radio button and tap play";

        textList[1] = "Gesture Guessing Game gives you different shapes on the screen " +
                "and you have to guess a gesture this shape represents. For exapmle for V shape you" +
                "have to imitate V shaped gesture.";

        textList[2] = "Gesture Drawing Game gives you a symbol on the screen and you have to draw " +
                "accuratly with your finger over the symbol contours, after tap Done button and app " +
                "will give you percentage of how accurate you was.";
    }

    private void tutorialTextInit(){
        header.setText(headerList[currentPage]);
        text.setText((textList[currentPage]));
    }

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
        float direction = e1.getX() - e2.getX() < 0 ? -1: 1;
        currentPage += direction;

        if(currentPage >= 0 && currentPage < PAGE_COUNT )
            tutorialTextInit();
        else
            backToMenu();

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return detector.onTouchEvent(event);
    }

    private void backToMenu(){
        startActivity(new Intent(this, MainActivity.class));
    }
}
