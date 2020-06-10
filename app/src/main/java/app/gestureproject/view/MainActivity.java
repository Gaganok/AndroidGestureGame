package app.gestureproject.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import app.gestureproject.R;
import app.gestureproject.service.BackgroundService;

public class MainActivity extends AppCompatActivity {

    private List<String> results = new ArrayList<String>();
    private Spinner resultSpinner;
    private RadioButton gestGuss;
    private RadioButton gestDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadResults();

        gestGuss = findViewById(R.id.gestureRadButton);
        gestDraw = findViewById(R.id.manualRadButton);

        gestGuss.setChecked(true);

        if(!BackgroundService.running) {
            startService(new Intent(this, BackgroundService.class));
            BackgroundService.setRunning(true);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();

        resultSpinner = findViewById(R.id.resultSpinner);
        initSpinner();

        resultSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        if(pos > 0){
                            throwToast(results.remove(pos));
                            initSpinner();
                            updateFile();
                        }
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                        //Do Nothing
                    }
                });
    }

    private void throwToast(String s){
        Toast.makeText(this,s + " was deleted", Toast.LENGTH_SHORT).show();
    }

    private void initSpinner(){
        String[] array = new String[results.size()];
        for(int i = 0; i < results.size(); i++)
            array[i] = results.get(i);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        resultSpinner.setAdapter(adapter);
    }

    private void updateFile(){
        try {
            File file = new File(getPackageManager().
                    getPackageInfo(getPackageName(), 0).
                    applicationInfo.dataDir + "/results.txt");
            FileWriter fw = new FileWriter(file, false);
            for(String s: results)
                fw.write(s + "\n");

            fw.flush();
            fw.close();
        }catch (Throwable e){e.printStackTrace();};
    }

    private void loadResults() {
        try {
            File file = new File(getPackageManager().
                    getPackageInfo(getPackageName(), 0).
                    applicationInfo.dataDir + "/results.txt");

            if(!file.exists()) {
                file.createNewFile();
                FileWriter fw = new FileWriter(file, false);
                fw.write("Last Results: \n");
                fw.flush();
                fw.close();

                results.add("Last Results: ");
            } else {
                Scanner scan = new Scanner(file);
                while (scan.hasNext())
                    results.add(scan.nextLine());
            }
        }catch (Throwable e){e.printStackTrace();}
    }

    public void play(View v){
        if(gestDraw.isChecked())
            startActivity(new Intent(this, GestureModeActivity.class));
        else
            startActivity(new Intent(this, GestureDrawingActivity.class));
    }

    public void tutorial(View v){
        startActivity(new Intent(this, TutorialActivity.class));
    }

    public void settings(View v){
        startActivity(new Intent(this, TutorialActivity.class));
    }
}
