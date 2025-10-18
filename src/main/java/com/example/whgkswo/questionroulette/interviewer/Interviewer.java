package com.example.whgkswo.questionroulette.interviewer;

import com.example.whgkswo.questionroulette.dto.QnA;
import com.example.whgkswo.questionroulette.helper.TextHelper;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Interviewer {

    private final Scanner scanner = new Scanner(System.in);

    private final List<QnA> qnaList = new ArrayList<>();

    private Queue<String> setQuestions() {
        List<String> questions = TextHelper.getQuestions();
        System.out.println(String.format("랜덤 질문 룰렛을 시작합니다.\n질문 갯수를 선택해주세요.(최대 %d개)", questions.size()));

        int questionSize = Math.min(questions.size(), scanner.nextInt());
        scanner.nextLine(); // 버퍼 비우기

        // 질문 섞기
        Collections.shuffle(questions);

        // 큐에 담고 리턴
        return new LinkedList<>(questions.subList(0, questionSize));
    }

    public void startInterview() {
        Queue<String> questions = setQuestions();
        int totalQuestions = questions.size();
        int currentNumber = 1;

        System.out.println("\n" + "=".repeat(60));
        System.out.println("🎤 면접을 시작합니다!");
        System.out.println("=".repeat(60));

        while (!questions.isEmpty()) {
            String question = questions.poll();

            System.out.println("\n" + "=".repeat(60));
            System.out.println(String.format("📝 질문 %d/%d", currentNumber, totalQuestions));
            System.out.println("=".repeat(60));
            System.out.println("Q. " + question);
            System.out.println("=".repeat(60));
            System.out.print("\n💬 답변: ");

            String answer = scanner.nextLine();

            qnaList.add(new QnA(question, answer));
            currentNumber++;
        }

        System.out.println("\n🎉 면접이 종료되었습니다!");

        // 기록 저장
        TextHelper.saveRecord(qnaList);
    }
}