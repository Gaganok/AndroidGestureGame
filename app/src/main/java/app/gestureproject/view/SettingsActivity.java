package app.gestureproject.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import app.gestureproject.R;
import app.gestureproject.manager.GameManager;

public class SettingsActivity extends AppCompatActivity {
    int min = 10;
    private TextView exampleView;
    private TextView fontView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SeekBar seek = findViewById(R.id.seekBar);
        fontView = findViewById(R.id.fontSizeView);
        fontView.setText("Font Size: " + GameManager.fontSize);

        exampleView = findViewById(R.id.exapleView);
        exampleView.setTextSize(GameManager.fontSize + min);

        seek.setMax(20);
        seek.setProgress(GameManager.fontSize);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int cur = min + progress;
                GameManager.setFontSize(cur);
                fontView.setText("Font Size: " + cur);
                exampleView.setTextSize(cur);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
