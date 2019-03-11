package com.example.sucursaladvisetv;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LoginMain2Activity extends AppCompatActivity {
    private TextView urlTv;
    private TextView codeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main2);

        urlTv = findViewById(R.id.urlTv);
        codeTv = findViewById(R.id.codeTv);


    }
}
