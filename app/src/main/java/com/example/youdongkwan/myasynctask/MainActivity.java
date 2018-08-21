package com.example.youdongkwan.myasynctask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ProgressBar progress;
    BackgroundTask task;
    int value;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = (ProgressBar)findViewById(R.id.progress);
        textView = (TextView)findViewById(R.id.textView);

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = new BackgroundTask();
                task.execute(100);
            }
        });
        Button button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.cancel(true);
            }
        });
        Button button1 = (Button)findViewById(R.id.button4);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressTask task = new ProgressTask();
                task.execute("시작");
            }
        });
    }

    class ProgressTask extends AsyncTask<String,Integer,Integer>{
        int value = 0;

        //필수 쓰레드 넣기. 어떤게 동작하게 하겠다.
        @Override
        protected Integer doInBackground(String... strings) {
            while (isCancelled() == false){
                value++;
                if(value>100){
                    break;
                }else{
                    publishProgress(value);
                }

                try{
                    Thread.sleep(100);
                }catch (InterruptedException ex){}
            }
            return value;
        }

        //2번쨰 파라미터
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            progress.setProgress(values[0].intValue());
            textView.setText("Current Value : "+ values[0].toString());
        }

        //3번째 파라메타
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            Toast.makeText(getApplicationContext(),"완료됨",Toast.LENGTH_LONG).show();
        }

    }
    class BackgroundTask extends AsyncTask<Integer,Integer,Integer>{
        @Override
        protected void onPreExecute() {
            value = 0;
            progress.setProgress(value);
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            while (isCancelled() == false){
                value++;
                if(value>=100){
                    break;
                }else{
                    publishProgress(value);
                }

                try{
                    Thread.sleep(100);
                }catch (InterruptedException ex){}
            }
            return value;
        }

        @Override
        protected void onCancelled() {
            progress.setProgress(0);
            textView.setText("Cancelled");
        }

        @Override
        protected void onPostExecute(Integer integer) {
            progress.setProgress(0);
            textView.setText("Finished.");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progress.setProgress(values[0].intValue());
            textView.setText("Current Value : "+ values[0].toString());
        }
    }
}
