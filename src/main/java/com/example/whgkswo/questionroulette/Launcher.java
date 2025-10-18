package com.example.whgkswo.questionroulette;


import com.example.whgkswo.questionroulette.interviewer.Interviewer;

public class Launcher {
    public static void main(String[] args) {
        Interviewer interviewer = new Interviewer();
        interviewer.startInterview();
    }
}
