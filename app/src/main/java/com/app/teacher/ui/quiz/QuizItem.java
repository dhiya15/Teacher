package com.app.teacher.ui.quiz;

public class QuizItem {

    public int image;
    public String question;
    public boolean response;

    public QuizItem(int img, String question, boolean response) {
        this.image = img;
        this.question = question;
        this.response = response;
    }

}
