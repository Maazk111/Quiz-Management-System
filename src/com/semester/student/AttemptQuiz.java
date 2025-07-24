package com.semester.student;

import com.semester.common.QuizBank;
import com.semester.student.ShowQuiz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AttemptQuiz extends JFrame {

    private String quizName;
    private String quizDate;
    private String quizTime;
    private String courseName;
    private String userName;

    private JPanel panel;
    private JButton submitButton;
    private String QUIZ_QUESTION_FILE_PATH;
    private List<QuizBank> quizQuestionList;
    private List<JTextField> textFields;

    public AttemptQuiz( String userName,String courseName,String quizName, String quizDate, String quizTime)
    {
        this.quizName = quizName;
        this.quizDate = quizDate;
        this.quizTime = quizTime;
        this.courseName = courseName;
        this.userName = userName;

        setTitle("Question Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 300);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        QUIZ_QUESTION_FILE_PATH = "./"+courseName +"/quizzes/"+quizName+"_"+quizDate+"_"+quizTime+".txt";

        quizQuestionList = new ArrayList<>();
        textFields = new ArrayList<>();
        loadQuizQuestionsFromFile();

        for (QuizBank quizBank : quizQuestionList) {

            JPanel questionPanel = new JPanel(new BorderLayout());

            JLabel questionLabel = new JLabel("Question: " + quizBank.getQuestion());
            JTextField textField = new JTextField(20);
            JLabel pointsLabel = new JLabel("Points: "+quizBank.getPoints());

            textFields.add(textField);

            //questionPanel.add(serialLabel, BorderLayout.WEST);
            questionPanel.add(questionLabel, BorderLayout.CENTER);
            questionPanel.add(textField, BorderLayout.EAST);
            questionPanel.add(pointsLabel, BorderLayout.SOUTH);

            panel.add(questionPanel);
        }

        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int total = 0;
                for (JTextField textField : textFields) {
                    String answer = textField.getText();

                    for(QuizBank quizBank : quizQuestionList)
                    {
                        if(Objects.equals(quizBank.getAnswer(), answer))
                        {
                            total = total + quizBank.getPoints();
                        }
                    }

                }
                ShowQuiz showQuiz = new ShowQuiz(userName,courseName,quizName,total,quizDate,quizTime);
                showQuiz.setVisible(true);
                dispose();
            }

        });

        panel.add(Box.createVerticalStrut(10));
        panel.add(submitButton);

        add(panel);
    }
    private void loadQuizQuestionsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(QUIZ_QUESTION_FILE_PATH))) {

            String firstLine = reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    String question = parts[0];
                    String answer = parts[1];
                    int points = Integer.parseInt(parts[2]);
                    QuizBank loadedQuizQuestion = new QuizBank(question, answer,points);
                    System.out.println(loadedQuizQuestion.getQuestion());
                    quizQuestionList.add(loadedQuizQuestion);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
