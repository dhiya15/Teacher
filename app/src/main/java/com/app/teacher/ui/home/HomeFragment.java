package com.app.teacher.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.teacher.R;
import com.app.teacher.ui.attendance.AttendanceActivity;

import java.util.ArrayList;

public class HomeFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ArrayList<NewsItem> newsList = new ArrayList<NewsItem>();

        for(int i = 0; i < 10; i++){
            newsList.add(new NewsItem(R.drawable.about, "This is title " + i, "Something " + i + " is written here", "23/04/2021", "10:00"));
        }

        NewsAdapter newsListAdapter = new NewsAdapter(newsList);

        ListView newsListView = root.findViewById(R.id.newsListView);

        newsListView.setAdapter(newsListAdapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), newsList.get(position).title, Toast.LENGTH_SHORT).show();
            }
        });



        return root;
    }

    public class NewsAdapter extends BaseAdapter {

        ArrayList<NewsItem> list = new ArrayList<NewsItem>();

        public NewsAdapter(ArrayList<NewsItem> list){
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
            View view = inflater.inflate(R.layout.news_item, null);

            ImageView newsImage = view.findViewById(R.id.newsImage);
            TextView newsTitle = view.findViewById(R.id.newsTitle);
            TextView newsSubTitle = view.findViewById(R.id.newsSubTitle);
            TextView newsDate = view.findViewById(R.id.newsDate);
            TextView newsTime = view.findViewById(R.id.newsTime);

            newsImage.setImageResource(list.get(position).image);
            newsTitle.setText(list.get(position).title);
            newsSubTitle.setText(list.get(position).subTitle);
            newsDate.setText(list.get(position).date);
            newsTime.setText(list.get(position).time);

            return view;
        }

    }

}