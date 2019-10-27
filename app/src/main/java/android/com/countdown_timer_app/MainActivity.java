package android.com.countdown_timer_app;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SeekBar sk;
    TextView timer;
    Boolean counterActive = false;
    Button stop;
    MediaPlayer mp;
    CountDownTimer countdowntimer;
    public void resetTimer()
    {
        timer.setText("0:30");
        sk.setProgress(30);
        countdowntimer.cancel();//stop the countdown
        stop.setText("Go!");
        sk.setEnabled(true);
        counterActive = false; //Back to normal state

    }
    public void updateTimer(int secLeft)
    {
        int min = (int) secLeft/60;
        int sec = secLeft-min*60;
        String secString = Integer.toString(sec);
        String minString = Integer.toString(min);

        if(sec <= 9)
        {
            secString = "0" + secString; //This code is used bcz when we set timer to 10mins we can see only one "0" 10:0 at sec place
            //So to show 10:00 we are using this code.
        }
        else if(minString.equals("0"))
        {
            minString = "00"; //This code is used bcz when we set timer to 10mins we can see only one "0" 10:0 at sec place
            //So to show 10:00 we are using this code.
        }

        timer.setText(minString+":"+secString);
    }

    public void GO(View view) {
        if (counterActive.equals(false)) { //counterActive is a kind of "key" here once locked cannot to opened until finish
            counterActive = true;// Once Go method is executed counter changes to true and if loop cannot be entered again
            sk.setEnabled(false); //Disappear the SeekBar after Go btn is pressed
            stop.setText("Stop"); //Once Go is clicked Button text Changes to Stop
            countdowntimer = new CountDownTimer(sk.getProgress() * 1000 + 100, 1000) {// + 100 is to set delay for finish method exe.
                @Override
                public void onTick(long milliSecUntilDone) {
                    updateTimer((int) milliSecUntilDone / 1000);

                }

                @Override
                public void onFinish() {
                    resetTimer();
                    mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm);
                    //Here we cannot use this in mp bcz it refers to CountDownTimer so we need to use in ApplicationContext().
                    mp.start();
                    new Handler().postDelayed(new Runnable() { //This code Stops Alarm after 5sec of ring
                        @Override
                        public void run() {
                            mp.stop();
                        }
                    }, 5000);


                }
            }.start();
        } else //Else gets execute bcz one Go is executed the CounterActive is changed to true so on 2nd click it cannot be
        // entered to if Block so else gets executed
        {
            resetTimer();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sk = (SeekBar) findViewById(R.id.seek);
        timer = (TextView) findViewById(R.id.timer);
        stop = findViewById(R.id.go);
        sk.setMax(600); //10min*60s = 600s
        sk.setProgress(30);
        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //Here i is the progress on SeekBar s=and its the Total Seconds.
                updateTimer(i);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }}
