package com.example.mytravellerapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mytravellerapp.R;
import com.example.mytravellerapp.domain.UserService;
import com.example.mytravellerapp.domain.UserServiceImpl;
import com.example.mytravellerapp.model.entities.request.LoginRequest;
import com.example.mytravellerapp.model.entities.response.LoginResponse;
import com.example.mytravellerapp.model.rest.BMSService;
import android.view.View;





public class MainActivity extends AppCompatActivity {

    private BMSService bmsService = new BMSService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("MainActivity","*********************");
    }

}
