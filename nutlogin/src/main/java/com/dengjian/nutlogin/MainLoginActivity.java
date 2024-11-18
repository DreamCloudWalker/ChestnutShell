package com.dengjian.nutlogin;

import android.os.Bundle;

import com.dengjian.annotations.BindPath;

import androidx.appcompat.app.AppCompatActivity;

@BindPath(path = "nutlogin/login")
public class MainLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
    }
}
