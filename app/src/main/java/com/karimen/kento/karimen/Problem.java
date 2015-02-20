package com.karimen.kento.karimen;

import android.widget.LinearLayout;

/**
 * Created by Kento on 15/02/09.
 */
public class Problem {
    int id;
    String question_image_url;
    String question_text;

    public boolean isUser_answer() {
        return user_answer;
    }

    public void setUser_answer(boolean user_answer) {
        this.user_answer = user_answer;
    }

    String explanation;
    boolean correct_answer;
    boolean user_answer;

    public int getId() {
        return id;
    }

    public String getQuestion_image_url() {
        return question_image_url;
    }

    public void setQuestion_image_url(String question_image_url) {
        this.question_image_url = question_image_url;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public boolean isCorrect_answer() {
        return correct_answer;
    }

    public void setCorrect_answer(boolean correct_answer) {
        this.correct_answer = correct_answer;
    }

    public void setId(int id) {
        this.id = id;
    }
}
