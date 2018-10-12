package starlabs.noticeboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

   import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends Activity {

        SharedData sharedData;
        private FirebaseAuth auth;

        // Splash screen timer
        private static int SPLASH_TIME_OUT = 3000;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_welcome);
            sharedData=new SharedData(getApplicationContext());

            auth=FirebaseAuth.getInstance();





            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity

                    if(auth.getCurrentUser()==null)
                    {
                        startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                        sharedData.firstTime();
                    }

                    if(auth.getCurrentUser()!=null) {
                        Intent i = new Intent(WelcomeActivity.this, NoticeActivity.class);
                        startActivity(i);
                        finish();
                    }

                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);

        }

    }

