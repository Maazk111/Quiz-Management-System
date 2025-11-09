package com.second;

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

public class StartQuiz extends JFrame {

    private String quizName;
    private String quizDate;
    private String quizTime;
    private String courseName;
    private String userName;

    private JPanel panel;
    private JButton jSubmit;
    private String quizQuestionPath;
    private List<MakeQuizBank> quizList;
    private List<JTextField> textFields;

    public StartQuiz(String userName, String courseName, String quizName, String quizDate, String quizTime)
    {
        this.quizName = quizName;
        this.quizDate = quizDate;
        this.quizTime = quizTime;
        this.courseName = courseName;
        this.userName = userName;

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        quizQuestionPath = courseName +"_quiz_"+quizName+"_"+quizDate+"_"+quizTime+".txt";

        quizList = new ArrayList<>();
        textFields = new ArrayList<>();
        loadQuizQuestions();

        for (MakeQuizBank quizBank : quizList) {

            JPanel questionPanel = new JPanel(new BorderLayout());
            JLabel questionLabel = new JLabel("Question: " + quizBank.getQuestion());
            JTextField textField = new JTextField(20);
            JLabel pointsLabel = new JLabel("Points: "+quizBank.getPoints());

            textFields.add(textField);

            questionPanel.add(questionLabel, BorderLayout.CENTER);
            questionPanel.add(textField, BorderLayout.EAST);
            questionPanel.add(pointsLabel, BorderLayout.SOUTH);

            panel.add(questionPanel);
        }

        jSubmit = new JButton("Submit");
        jSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int total = 0;
                for (JTextField textField : textFields) {
                    String answer = textField.getText();

                    for(MakeQuizBank quizBank : quizList)
                    {
                        if(Objects.equals(quizBank.getAnswer(), answer))
                        {
                            total = total + quizBank.getPoints();
                        }
                    }

                }
                StudentForm studentForm = new StudentForm(userName,courseName,quizName,total,quizDate,quizTime);
                studentForm.setVisible(true);
                dispose();
            }

        });

        panel.add(Box.createVerticalStrut(10));
        panel.add(jSubmit);

        add(panel);

        setTitle("Start Quiz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 300);
        setLocationRelativeTo(null);
    }
    private void loadQuizQuestions() {
        try (BufferedReader reader = new BufferedReader(new FileReader(quizQuestionPath))) {
            String firstLine = reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    String question = parts[0];
                    String answer = parts[1];
                    int points = Integer.parseInt(parts[2]);
                    MakeQuizBank loadedQuizQuestion = new MakeQuizBank(question, answer,points);
                    System.out.println(loadedQuizQuestion.getQuestion());
                    quizList.add(loadedQuizQuestion);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
