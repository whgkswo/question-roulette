package com.example.whgkswo.questionroulette.helper;

import com.example.whgkswo.questionroulette.dto.QnA;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TextHelper {

    private static final String RESOURCE_PATH = "src/main/resources";

    // resources 폴더의 모든 txt 파일 목록 가져오기
    public static List<String> getQuestionFiles() {
        List<String> files = new ArrayList<>();

        try {
            Path resourcePath = Paths.get(RESOURCE_PATH);

            if (!Files.exists(resourcePath)) {
                throw new RuntimeException(RESOURCE_PATH + " 폴더를 찾을 수 없습니다!");
            }

            // .txt 파일만 필터링
            files = Files.walk(resourcePath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".txt"))
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());

        } catch (IOException e) {
            System.err.println("파일 목록 읽기 오류: " + e.getMessage());
            e.printStackTrace();
        }

        return files;
    }

    // 특정 파일에서 질문 읽기 (물리적 경로)
    public static List<String> getQuestions(String fileName) {
        List<String> questions = new ArrayList<>();

        try {
            Path filePath = Paths.get(RESOURCE_PATH, fileName);

            if (!Files.exists(filePath)) {
                throw new RuntimeException(filePath + " 파일을 찾을 수 없습니다!");
            }

            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

            StringBuilder currentQuestion = new StringBuilder();

            for (String line : lines) {
                if (line.trim().startsWith("-")) {
                    if (currentQuestion.length() > 0) {
                        questions.add(currentQuestion.toString().trim());
                        currentQuestion = new StringBuilder();
                    }
                    currentQuestion.append(line.trim().substring(1).trim());
                } else if (!line.trim().isEmpty()) {
                    if (currentQuestion.length() > 0) {
                        currentQuestion.append(" ");
                    }
                    currentQuestion.append(line.trim());
                }
            }

            if (currentQuestion.length() > 0) {
                questions.add(currentQuestion.toString().trim());
            }

        } catch (IOException e) {
            System.err.println("파일 읽기 오류: " + e.getMessage());
            e.printStackTrace();
        }

        return questions;
    }

    public static void saveRecord(List<QnA> qnaList) {
        File folder = new File("records");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String fileName = "records/interview_" + now.format(formatter) + ".txt";

        try (FileWriter writer = new FileWriter(fileName, StandardCharsets.UTF_8)) {
            DateTimeFormatter titleFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
            writer.write("=".repeat(60) + "\n");
            writer.write("면접 기록 - " + now.format(titleFormatter) + "\n");
            writer.write("=".repeat(60) + "\n\n");

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