package app.gestureproject.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;

import app.gestureproject.R;

public class CompleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        TextView time = findViewById(R.id.timeView);
        Bundle bundle = getIntent().getExtras();
        String game = bundle.getCharSequence("game").toString();
        String resultText = bundle.getCharSequence("resultText").toString();
        String result = bundle.getCharSequence("result").toString();
        time.setText(resultText + " " + result);

        try {
            File file = new File(getPackageManager().
                    getPackageInfo(getPackageName(), 0).
                    applicationInfo.dataDir + "/results.txt");

            FileWriter fw = new FileWriter(file, true);

            fw.write(game + ": " + result + "\n");
            fw.flush();
            fw.close();

        }catch (Throwable e){e.printStackTrace();}
    }

    public void backToMenu(View v){
        startActivity(new Intent(this, MainActivity.class));
    }
}
