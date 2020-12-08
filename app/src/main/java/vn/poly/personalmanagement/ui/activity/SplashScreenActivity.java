package vn.poly.personalmanagement.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import vn.poly.personalmanagement.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

//        new Thread(){
//            @Override
//            public void run() {
//              try{
//                  Thread.sleep(1500);
//
//              }catch (Exception e){
//                  e.printStackTrace();
//              }
//            }
//        }.start();
        startActivity(new Intent(SplashScreenActivity.this, SigninActivity.class));
        finish();
    }
}