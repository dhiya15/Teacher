package com.app.teacher.ui.quiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.teacher.R;
import com.app.teacher.ui.attendance.AttendanceActivity;
import com.app.teacher.ui.profile.ProfileFragment;
import com.studioidan.httpagent.HttpAgent;
import com.studioidan.httpagent.JsonCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import api.URLs;
import db.SharedPref;
import models.Module;
import models.Promo;

public class QuizFragment extends Fragment {

    ListView quizListView;

    ArrayList<QuizItem> quizArrayList;
    ArrayList promosArrayList;
    ArrayList modulesArrayList;

    AutoCompleteTextView promoAutoComplete;
    AutoCompleteTextView moduleAutoComplete;

    QuizAdapter quizAdapter;
    ArrayAdapter<Promo> promosAdapter;
    ArrayAdapter<Module> modulesAdapter;

    EditText questionEditText, responseEditText;

    Button buttonAddQuiz, buttonSendQuiz, buttonAttendance;

    String teacherCode = "";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_quiz, container, false);

        promoAutoComplete = root.findViewById(R.id.promoAutoComplet);
        moduleAutoComplete = root.findViewById(R.id.moduleAutoComplete);
        quizListView = root.findViewById(R.id.quizsList);
        questionEditText = root.findViewById(R.id.questionEditText);
        responseEditText = root.findViewById(R.id.answerEditText);
        buttonAddQuiz = root.findViewById(R.id.button4);
        buttonSendQuiz = root.findViewById(R.id.button5);

        quizArrayList = new ArrayList<QuizItem>();
        promosArrayList = new ArrayList<Promo>();
        modulesArrayList = new ArrayList<Module>();

        quizAdapter = new QuizAdapter(quizArrayList);
        quizListView.setAdapter(quizAdapter);

        getAllPromos();
        getAllModules();
        getTeacherCode();

        buttonAddQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ans = Boolean.parseBoolean(responseEditText.getText().toString());
                quizArrayList.add(new QuizItem(R.drawable.questions, questionEditText.getText().toString(), ans));
                quizAdapter.notifyDataSetChanged();
            }
        });

        buttonSendQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = new SharedPref(getContext()).loadToken();
                if(! token.equals("")){
                    HttpAgent.post(URLs.STORE_EXAM)
                            .headers("Authorization", "Bearer " + token, "Content-Type", "application/json")
                            .withBody(prepareJSONToSend().toString())
                            .goJson(new JsonCallback() {
                                @Override
                                protected void onDone(boolean success, JSONObject jsonObject) {
                                    if(success && (jsonObject != null)){
                                        try {
                                            boolean status = jsonObject.getBoolean("status");
                                            if(status){
                                                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                }
            }
        });

        buttonAttendance = root.findViewById(R.id.buttoAtt);
        buttonAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AttendanceActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    public void getAllPromos(){
        String token = new SharedPref(getContext()).loadToken();
        if(! token.equals("")){
            HttpAgent.get(URLs.ALL_PROMOS)
                    .headers("Authorization", "Bearer " + token, "Content-Type", "application/json")
                    .goJson(new JsonCallback() {
                        @Override
                        protected void onDone(boolean success, JSONObject jsonObject) {
                            if(success && (jsonObject != null)){
                                try {
                                    boolean status = jsonObject.getBoolean("status");
                                    if(status){
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for(int i=0; i<jsonArray.length(); i++){
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            promosArrayList.add(new Promo(object.getInt("id"), object.getString("name")));
                                        }
                                        promosAdapter = new ArrayAdapter<>(getContext(),
                                                android.R.layout.simple_dropdown_item_1line, promosArrayList);
                                        promoAutoComplete.setAdapter(promosAdapter);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }
    }

    public void getAllModules(){
        String token = new SharedPref(getContext()).loadToken();
        if(! token.equals("")){
            HttpAgent.post(URLs.ALL_MODULES)
                    .headers("Authorization", "Bearer " + token, "Content-Type", "application/json")
                    .withBody("{\"level\": 1}")
                    .goJson(new JsonCallback() {
                        @Override
                        protected void onDone(boolean success, JSONObject jsonObject) {
                            if(success && (jsonObject != null)){
                                try {
                                    boolean status = jsonObject.getBoolean("status");
                                    if(status){
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for(int i=0; i<jsonArray.length(); i++){
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            modulesArrayList.add(new Module(object.getInt("id"), object.getString("name"), object.getString("code")));
                                        }
                                        modulesAdapter= new ArrayAdapter<>(getContext(),
                                                android.R.layout.simple_dropdown_item_1line, modulesArrayList);
                                        moduleAutoComplete.setAdapter(modulesAdapter);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }
    }

    public void getTeacherCode(){
        String token = new SharedPref(getContext()).loadToken();

        if(! token.equals("")){
            HttpAgent.get(URLs.AUTH_INFO)
                    .headers("Authorization", "Bearer " + token, "Content-Type", "application/json")
                    .goJson(new JsonCallback() {
                        @Override
                        protected void onDone(boolean success, JSONObject jsonObject) {
                            if(success && (jsonObject != null)){
                                try {
                                    teacherCode = jsonObject.getString("code");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }
    }

    public JSONObject prepareJSONToSend(){
        JSONObject infos = new JSONObject();
        JSONObject question;
        try {
            infos.put("teacher_id", teacherCode);
            infos.put("module_id", Module.findIdIn(modulesArrayList, moduleAutoComplete.getText().toString()));
            infos.put("promo_id", Promo.findIdIn(promosArrayList, promoAutoComplete.getText().toString()));

            JSONArray questions = new JSONArray();
            for(int i=0; i<quizArrayList.size(); i++){
                question = new JSONObject();
                question.put("name", quizArrayList.get(i).question);
                question.put("ans", quizArrayList.get(i).response);
                questions.put(question);
            }
            infos.put("qts", questions);
            Log.d("Result", infos.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return infos;
    }

    public class QuizAdapter extends BaseAdapter {

        ArrayList<QuizItem> list = new ArrayList<QuizItem>();

        public QuizAdapter(ArrayList<QuizItem> list){
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.quiz_item, null);

            ImageView image = view.findViewById(R.id.quizImage);
            TextView question = view.findViewById(R.id.question);
            TextView responses = view.findViewById(R.id.answer);
            ImageView deleteImageView = view.findViewById(R.id.deleteImageView2);

            image.setImageResource(list.get(position).image);
            question.setText("Question : " + list.get(position).question);
            responses.setText("Answer : " + list.get(position).response);
            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    quizArrayList.remove(position);
                    quizAdapter.notifyDataSetChanged();
                }
            });

            return view;
        }

    }

}