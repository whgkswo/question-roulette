package com.example.whgkswo.questionroulette.helper;

import com.example.whgkswo.questionroulette.dto.QnA;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TextHelper {

    public static List<String> getQuestions() {
        List<String> questions = new ArrayList<>();

        try {
            // resources 폴더의 파일 읽기
            InputStream inputStream = TextHelper.class
                    .getClassLoader()
                    .getResourceAsStream("questions.txt");

            if (inputStream == null) {
                throw new RuntimeException("questions.txt 파일을 찾을 수 없습니다!");
            }

            // UTF-8로 읽기 (한글 깨짐 방지)
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8)
            );

            String line;
            StringBuilder currentQuestion = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                // 하이픈으로 시작하면 새 질문
                if (line.trim().startsWith("-")) {
                    // 이전 질문 저장
                    if (currentQuestion.length() > 0) {
                        questions.add(currentQuestion.toString().trim());
                        currentQuestion = new StringBuilder();
                    }
                    // 하이픈 제거하고 질문 시작
                    currentQuestion.append(line.trim().substring(1).trim());
                } else if (!line.trim().isEmpty()) {
                    // 질문이 여러 줄인 경우
                    if (currentQuestion.length() > 0) {
                        currentQuestion.append(" ");
                    }
                    currentQuestion.append(line.trim());
                }
            }

            // 마지막 질문 추가
            if (currentQuestion.length() > 0) {
                questions.add(currentQuestion.toString().trim());
            }

            reader.close();

        } catch (Exception e) {
            System.err.println("파일 읽기 오류: " + e.getMessage());
            e.printStackTrace();
        }

        return questions;
    }

    // 면접 기록 저장
    public static void saveRecord(List<QnA> qnaList) {
        // records 폴더 생성
        File folder = new File("records");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // 현재 시각을 파일명으로
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String fileName = "records/interview_" + now.format(formatter) + ".txt";

        try (FileWriter writer = new FileWriter(fileName, StandardCharsets.UTF_8)) {
            // 제목 (현재 시각)
            DateTimeFormatter titleFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
            writer.write("=".repeat(60) + "\n");
            writer.write("면접 기록 - " + now.format(titleFormatter) + "\n");
            writer.write("=".repeat(60) + "\n\n");

            // Q&A 내용
            for (int i = 0; i < qnaList.size(); i++) {
                QnA qna = qnaList.get(i);
                writer.write(String.format("[질문 %d]\n", i + 1));
                writer.write("Q. " + qna.question() + "\n\n");
                writer.write("A. " + qna.answer() + "\n");
                writer.write("\n" + "-".repeat(60) + "\n\n");
            }

            System.out.println("\n✅ 면접 기록이 저장되었습니다: " + fileName);

        } catch (IOException e) {
            System.err.println("❌ 파일 저장 실패: " + e.getMessage());
        }
    }
}
