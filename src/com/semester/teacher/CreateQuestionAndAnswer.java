package com.semester.teacher;

import com.semester.common.QuestionBank;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.io.*;

public class CreateQuestionAndAnswer extends JFrame{

    private JTextArea questionTextArea;
    private JTextArea answerTextArea;
    private JButton addQuestionButton;
    private JButton editQuestionButton;
    private JButton deleteQuestionButton;
    private JButton closeButton;
    private DefaultTableModel questionTableModel;
    private JTable questionTable;
    private String courseName;
    private String QUESTION_FILE_PATH ;


    public CreateQuestionAndAnswer(String courseName) {

        this.courseName = courseName;

        setTitle("Question And Answer of "+ courseName);
        setSize(900, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        QUESTION_FILE_PATH = "./"+courseName + "/questions.txt";


        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        questionTextArea = new JTextArea();
        questionPanel.add(new JScrollPane(questionTextArea), BorderLayout.CENTER);
        questionPanel.add(new JLabel("Question:"), BorderLayout.BEFORE_FIRST_LINE);

        JPanel answerPanel = new JPanel(new BorderLayout());
        answerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        answerTextArea = new JTextArea();
        answerPanel.add(new JScrollPane(answerTextArea), BorderLayout.CENTER);
        answerPanel.add(new JLabel("Answer:"), BorderLayout.BEFORE_FIRST_LINE);

        JPanel inputPanel = new JPanel(new GridLayout(2, 1));
        inputPanel.add(questionPanel);
        inputPanel.add(answerPanel);
        add(inputPanel, BorderLayout.CENTER);

        questionTableModel = new DefaultTableModel();
        questionTableModel.addColumn("Question");
        questionTableModel.addColumn("Answer");

        questionTable = new JTable(questionTableModel);
        JScrollPane scrollPane = new JScrollPane(questionTable);
        add(scrollPane, BorderLayout.WEST);


        JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
        addQuestionButton = new JButton("Add Question");
        editQuestionButton = new JButton("Edit Question");
        deleteQuestionButton = new JButton("Delete Question");
        closeButton = new JButton("Close");

        buttonPanel.add(addQuestionButton);
        buttonPanel.add(editQuestionButton);
        buttonPanel.add(deleteQuestionButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.EAST);

        File courseFile = new File(QUESTION_FILE_PATH);
        if(courseFile.exists())
        {
            loadQuestionsFromFile();
        }
        addQuestionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String question = questionTextArea.getText();
                String answer = answerTextArea.getText();
                if (!question.isEmpty() && !answer.isEmpty()) {
                    QuestionBank newQuestion = new QuestionBank(question, answer);
                    questionTableModel.addRow(new Object[]{newQuestion.getQuestion(), newQuestion.getAnswer()});
                    questionTextArea.setText("");
                    answerTextArea.setText("");
                    saveQuestionsToFile();
                }
            }
        });

        editQuestionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = questionTable.getSelectedRow();
                int selectedColumnIndex = questionTable.getSelectedColumn();
                if (selectedIndex != -1) {
                    String question = questionTableModel.getValueAt(selectedIndex, 0).toString();
                    String answer = questionTableModel.getValueAt(selectedIndex, 1).toString();
                    QuestionBank selectedQuestion = new QuestionBank(question, answer);
                    String editedQuestion = JOptionPane.showInputDialog(CreateQuestionAndAnswer.this,
                            "Enter edited question:", selectedQuestion.getQuestion());
                    if (editedQuestion != null && !editedQuestion.isEmpty()) {
                        selectedQuestion.setQuestion(editedQuestion);
                        String editedAnswer = JOptionPane.showInputDialog(CreateQuestionAndAnswer.this,
                                "Enter edited answer:", selectedQuestion.getAnswer());
                        if (editedAnswer != null && !editedAnswer.isEmpty()) {
                            selectedQuestion.setAnswer(editedAnswer);
                        }
                        questionTableModel.setValueAt(selectedQuestion.getQuestion(), selectedIndex, 0);
                        questionTableModel.setValueAt(selectedQuestion.getAnswer(), selectedIndex, 1);
                        saveQuestionsToFile();
                    }
                }
            }
        });

        deleteQuestionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = questionTable.getSelectedRow();
                if (selectedIndex != -1) {
                    questionTableModel.removeRow(selectedIndex);
                    saveQuestionsToFile();
                }
            }
        });

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               dispose();
            }
        });
    }

    private void loadQuestionsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(QUESTION_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    String question = parts[0];
                    String answer = parts[1];
                    QuestionBank loadedQuestion = new QuestionBank(question, answer);
                    questionTableModel.addRow(new Object[]{loadedQuestion.getQuestion(), loadedQuestion.getAnswer()});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveQuestionsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(QUESTION_FILE_PATH))) {
            for (int i = 0; i < questionTableModel.getRowCount(); i++) {
                String question = (String) questionTableModel.getValueAt(i,0).toString();
                String answer = (String) questionTableModel.getValueAt(i, 1).toString();
                String line = question + "|" + answer;
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
