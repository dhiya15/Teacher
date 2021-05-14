package com.app.teacher.ui.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.teacher.MenuActivity;
import com.app.teacher.R;
import com.studioidan.httpagent.HttpAgent;
import com.studioidan.httpagent.JsonCallback;
import com.studioidan.httpagent.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import api.DownLoadImageTask;
import api.URLs;
import db.SharedPref;

public class ProfileFragment extends Fragment {

    ImageView imgProfile;
    EditText userName, userEmail, userPhone, userBirth;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        imgProfile = root.findViewById(R.id.userAvatar);
        userName = root.findViewById(R.id.userName);
        userEmail = root.findViewById(R.id.userEmail);
        userPhone = root.findViewById(R.id.userPhone);
        userBirth = root.findViewById(R.id.userBirth);

        String token = new SharedPref(getContext()).loadToken();

        if(! token.equals("")){
            HttpAgent.get(URLs.AUTH_INFO)
                    .headers("Authorization", "Bearer " + token, "Content-Type", "application/json")
                    .goJson(new JsonCallback() {
                        @Override
                        protected void onDone(boolean success, JSONObject jsonObject) {
                            if(success && (jsonObject != null)){
                                try {
                                    userName.setText(jsonObject.getString("name"));
                                    userEmail.setText(jsonObject.getString("email"));
                                    userPhone.setText(jsonObject.getString("phone"));
                                    userBirth.setText(jsonObject.getString("birth"));

                                    String imgURL = jsonObject.getString("avatar");
                                    new DownLoadImageTask(imgProfile).execute(imgURL);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }

        return root;
    }


}
