package com.second;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class AddQuestion extends JFrame{

    private JTextArea JQuestionText;
    private JTextArea JAnswerText;
    private JButton addButton;
    private JButton deleteButton;
    private JButton closeButton;
    private DefaultTableModel questionModel;
    private JTable jTable;
    private String courseName;
    private String questionFilePath;


    public AddQuestion(String courseName) {

        this.courseName = courseName;


        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        questionFilePath = courseName + "_questions.txt";

        // Question Text Area
        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JQuestionText = new JTextArea();
        questionPanel.add(new JScrollPane(JQuestionText), BorderLayout.CENTER);
        questionPanel.add(new JLabel("Question:"), BorderLayout.BEFORE_FIRST_LINE);
        // Answer Text Area
        JPanel answerPanel = new JPanel(new BorderLayout());
        answerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JAnswerText = new JTextArea();
        answerPanel.add(new JScrollPane(JAnswerText), BorderLayout.CENTER);
        answerPanel.add(new JLabel("Answer:"), BorderLayout.BEFORE_FIRST_LINE);

        // Combine question and answer panels
        JPanel inputPanel = new JPanel(new GridLayout(2, 1));
        inputPanel.add(questionPanel);
        inputPanel.add(answerPanel);
        add(inputPanel, BorderLayout.CENTER);

        questionModel = new DefaultTableModel();
        questionModel.addColumn("Question");
        questionModel.addColumn("Answer");

        jTable = new JTable(questionModel);
        JScrollPane scrollPane = new JScrollPane(jTable);
        add(scrollPane, BorderLayout.WEST);


        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
        addButton = new JButton("Add Question");
        deleteButton = new JButton("Delete Question");
        closeButton = new JButton("Close");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setTitle("Question And Answer");
        setSize(900, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        File courseFile = new File(questionFilePath);
        if(courseFile.exists())
        {
            loadQuestionList();
        }
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String question = JQuestionText.getText();
                String answer = JAnswerText.getText();
                if (!question.isEmpty() && !answer.isEmpty()) {
                    QuestionAnswerBank questionAnswerBank = new QuestionAnswerBank(question, answer);
                    questionModel.addRow(new Object[]{questionAnswerBank.getQuestion(), questionAnswerBank.getAnswer()});
                    JQuestionText.setText("");
                    JAnswerText.setText("");
                    saveQuestionList();
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = jTable.getSelectedRow();
                if (selectedIndex != -1) {
                    questionModel.removeRow(selectedIndex);
                    saveQuestionList();
                }
            }
        });

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               dispose();
            }
        });
    }

    private void loadQuestionList() {
        try (BufferedReader reader = new BufferedReader(new FileReader(questionFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    String question = parts[0];
                    String answer = parts[1];
                    QuestionAnswerBank questionAnswerBank = new QuestionAnswerBank(question, answer);
                    questionModel.addRow(new Object[]{questionAnswerBank.getQuestion(), questionAnswerBank.getAnswer()});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveQuestionList() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(questionFilePath))) {
            for (int i = 0; i < questionModel.getRowCount(); i++) {
                String question = (String) questionModel.getValueAt(i,0).toString();
                String answer = (String) questionModel.getValueAt(i, 1).toString();
                String line = question + "|" + answer;
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
