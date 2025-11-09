package com.second;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class MakeQuiz extends JFrame {


    private JTextField JQuizNameField;
    private JSpinner date, time;

    private JButton addButton,makeQuiz,closeButton;
    private String courseName;

    private DefaultTableModel questionModel;
    private JTable questionTable;

    private String questionFilePath;
    private String quizFilePath;

    public MakeQuiz(String courseName) {
        this.courseName = courseName;
        setTitle("Quiz for " + courseName);
        questionFilePath = courseName + "_questions.txt";

        JPanel topPanel = new JPanel();
        JPanel middlePanel = new JPanel(new GridLayout(1, 2));
        JPanel bottomPanel = new JPanel();

        JQuizNameField = new JTextField(20);
        SpinnerDateModel dateModel = new SpinnerDateModel();
        date = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(date, "dd/MM/yyyy");
        date.setEditor(dateEditor);

        SpinnerListModel timeModel = new SpinnerListModel(createTimeList());
        time = new JSpinner(timeModel);

        questionModel = new DefaultTableModel();


        addButton = new JButton("Add Points");

        makeQuiz = new JButton("Create Quiz");
        closeButton = new JButton("Close");

        setLayout(new BorderLayout());

        topPanel.setLayout(new FlowLayout());
        topPanel.add(new JLabel("Quiz Name:"));
        topPanel.add(JQuizNameField);
        topPanel.add(new JLabel("Quiz Date:"));
        topPanel.add(date);
        topPanel.add(new JLabel("Quiz Time:"));
        topPanel.add(time);

        questionModel.addColumn("Question");
        questionModel.addColumn("Answer");
        questionModel.addColumn("Points");


        questionTable = new JTable(questionModel);
        JScrollPane scrollPane = new JScrollPane(questionTable);
        middlePanel.add(scrollPane);


        File courseFile = new File(questionFilePath);
        if (courseFile.exists()) {
            loadQuestionList();
        }

        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(addButton);
        bottomPanel.add(makeQuiz);
        bottomPanel.add(closeButton);

        add(topPanel, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = questionTable.getSelectedRow();
                if (selectedIndex != -1) {
                    String question = questionModel.getValueAt(selectedIndex, 0).toString();
                    String answer = questionModel.getValueAt(selectedIndex, 1).toString();
                    int points = Integer.parseInt(JOptionPane.showInputDialog(MakeQuiz.this,
                            "Enter points:", "0"));
                    MakeQuizBank makeQuizBank = new MakeQuizBank(question,answer,points);
                    questionModel.setValueAt(makeQuizBank.getPoints(),selectedIndex,2);
                 }
                else
                {
                    JOptionPane.showMessageDialog(MakeQuiz.this, "You should select a question");
                }
            }
        });
        makeQuiz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (JQuizNameField != null && !Objects.equals(JQuizNameField.getText(), "")) {
                    String quiName = JQuizNameField.getText();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String date = dateFormat.format(MakeQuiz.this.date.getValue());
                    String time = MakeQuiz.this.time.getValue().toString();

                    quizFilePath = courseName + "_quiz_" + JQuizNameField.getText() + "_" + date + "_" + time.replace(":","") + ".txt";

                    if (questionModel.getRowCount() != 0) {
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(quizFilePath))) {
                            writer.write(quiName + "|" + date + "|" + time);
                            writer.newLine();

                            for (int i = 0; i < questionModel.getRowCount(); i++) {
                                String question = (String) questionModel.getValueAt(i, 0).toString();
                                String answer = (String) questionModel.getValueAt(i, 1).toString();
                                int points = (int) questionModel.getValueAt(i, 2);
                                String line = question + "|" + answer + "|" + points;
                                writer.write(line);
                                writer.newLine();
                            }

                            JOptionPane.showMessageDialog(MakeQuiz.this, "Quiz is created successfully");
                           dispose();

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(MakeQuiz.this, "Question List is empty");
                    }
                } else {
                    JOptionPane.showMessageDialog(MakeQuiz.this, "Quiz name is empty");
                }
            }
        });
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private String[] createTimeList() {
        String[] times = new String[24 * 60];
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        for (int i = 0; i < 24 * 60; i++) {
            times[i] = format.format(calendar.getTime());
            calendar.add(Calendar.MINUTE, 1);
        }

        return times;
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
}
