package com.app.teacher.ui.marks;

import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.teacher.R;

import java.util.ArrayList;

public class MarksFragment extends Fragment {

    private static final String[] STUDENTS = new String[] {
            "Student 1", "Student 2",
            "Student 3", "Student 4",
            "Student 5", "Student 6"
    };

    private static final String[] PROMOS = new String[] {
            "1MIAD", "1MSIR", "1MSIA", "2MIAD", "2MSIR", "2MSIA"
    };

    ListView studentsListView;
    ArrayList studentsArrayList;
    AutoCompleteTextView studentsAutoComplete, promoAutoComplete;
    StudentsAdapter studentsAdapter;
    EditText tdMarkEditText, examMarkEditText;
    Button buttonAddStudent;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_marks, container, false);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, PROMOS);
        promoAutoComplete = root.findViewById(R.id.autoCompleteTextView2);
        promoAutoComplete.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, STUDENTS);
        studentsAutoComplete = root.findViewById(R.id.autoCompleteTextView3);
        studentsAutoComplete.setAdapter(adapter2);

        studentsListView = root.findViewById(R.id.studentsList);
        studentsArrayList = new ArrayList<MarksItem>();
        studentsAdapter = new StudentsAdapter(studentsArrayList);
        studentsListView.setAdapter(studentsAdapter);

        tdMarkEditText = root.findViewById(R.id.editTextNumber2);
        examMarkEditText = root.findViewById(R.id.editTextNumber3);
        buttonAddStudent = root.findViewById(R.id.button4);
        buttonAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentsArrayList.add(new MarksItem(R.drawable.student, studentsAutoComplete.getText().toString(), tdMarkEditText.getText().toString(), examMarkEditText.getText().toString()));
                studentsAdapter.notifyDataSetChanged();
            }
        });

        return root;
    }

    public void insert(View view){

    }

    public class StudentsAdapter extends BaseAdapter {

        ArrayList<MarksItem> list = new ArrayList<MarksItem>();

        public StudentsAdapter(ArrayList<MarksItem> list){
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
            View view = inflater.inflate(R.layout.mark_item, null);

            ImageView image = view.findViewById(R.id.image1);
            TextView studentFullName = view.findViewById(R.id.studentName);
            TextView tdMark = view.findViewById(R.id.tdMark);
            TextView examMark = view.findViewById(R.id.examMark);
            ImageView deleteImageView = view.findViewById(R.id.deleteImageView);

            image.setImageResource(list.get(position).image);
            studentFullName.setText("Full Name : " + list.get(position).name);
            tdMark.setText("TD : " + list.get(position).td);
            examMark.setText("Exam : " + list.get(position).exam);
            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    studentsArrayList.remove(position);
                    studentsAdapter.notifyDataSetChanged();
                }
            });

            return view;
        }

    }

}