package com.app.teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.studioidan.httpagent.HttpAgent;
import com.studioidan.httpagent.JsonCallback;

import org.json.JSONException;
import org.json.JSONObject;

import api.URLs;
import db.SharedPref;

public class MainActivity extends AppCompatActivity {

    EditText userCode, userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String token = new SharedPref(this).loadToken();

        if(! token.equals("")){
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        } else {
            userCode = findViewById(R.id.userCode);
            userPassword = findViewById(R.id.userPassword);
        }
    }

    public void send(View view) {
        String code = userCode.getText().toString();
        String password = userPassword.getText().toString();

        HttpAgent.post(URLs.AUTH_LOGIN)
                .queryParams("code", code, "password", password)
                .goJson(new JsonCallback() {
                    @Override
                    protected void onDone(boolean success, JSONObject jsonObject) {
                        if(success && jsonObject != null){
                            try {
                                boolean status = jsonObject.getBoolean("status");
                                if(status) {
                                    String token = jsonObject.getString("token");
                                    new SharedPref(getBaseContext()).saveToken(token);

                                    Intent intent = new Intent(getBaseContext(), MenuActivity.class);
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

}