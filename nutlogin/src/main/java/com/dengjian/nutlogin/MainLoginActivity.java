package com.dengjian.nutlogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dengjian.annotations.BindPath;

@BindPath("nutlogin/login")
public class MainLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
    }
}
