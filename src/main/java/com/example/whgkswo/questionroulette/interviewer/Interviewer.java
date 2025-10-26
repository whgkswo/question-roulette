package com.example.whgkswo.questionroulette.interviewer;

import com.example.whgkswo.questionroulette.dto.QnA;
import com.example.whgkswo.questionroulette.helper.TextHelper;

import java.util.*;

public class Interviewer {

    private final Scanner scanner = new Scanner(System.in);
    private final List<QnA> qnaList = new ArrayList<>();

    // 파일 선택
    private String selectQuestionFile() {
        List<String> files = TextHelper.getQuestionFiles();

        if (files.isEmpty()) {
            throw new RuntimeException("질문 파일이 없습니다!");
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("📂 사용 가능한 질문 파일 목록");
        System.out.println("=".repeat(60));

        for (int i = 0; i < files.size(); i++) {
            System.out.println(String.format("[%d] %s", i + 1, files.get(i)));
        }

        System.out.println("=".repeat(60));
        System.out.print("사용할 파일 번호를 선택하세요: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // 버퍼 비우기

        if (choice < 1 || choice > files.size()) {
            System.out.println("❌ 잘못된 번호입니다. 첫 번째 파일을 사용합니다.");
            return files.get(0);
        }

        String selectedFile = files.get(choice - 1);
        System.out.println("✅ 선택된 파일: " + selectedFile);

        return selectedFile;
    }

    private Queue<String> setQuestions() {
        // 파일 선택
        String fileName = selectQuestionFile();

        // 선택된 파일에서 질문 로드
        List<String> questions = TextHelper.getQuestions(fileName);

        System.out.println(String.format("\n질문 개수를 선택해주세요. (최대 %d개)", questions.size()));
        System.out.print("개수: ");

        int questionSize = Math.min(questions.size(), scanner.nextInt());
        scanner.nextLine(); // 버퍼 비우기

        Collections.shuffle(questions);

        Queue<String> questionQueue = new LinkedList<>();
        questionQueue.add("간단하게 자기소개 부탁드립니다.");
        questionQueue.addAll(questions.subList(0, questionSize));

        return questionQueue;
    }

    public void startInterview() {
        System.out.println("\n🎤 랜덤 질문 룰렛을 시작합니다!");

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

        TextHelper.saveRecord(qnaList);
    }
}