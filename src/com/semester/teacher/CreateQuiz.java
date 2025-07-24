package com.semester.teacher;

import com.semester.common.QuestionBank;
import com.semester.common.QuizBank;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class CreateQuiz extends JFrame {

    private JPanel topPanel,middlePanel, bottomPanel;
    private JTextField quizNameTextField;
    private JSpinner quizDateSpinner, timeSpinner;
    private JButton addButton, deleteButton, createQuizButton,closeButton;
    private String courseName;

    private DefaultTableModel questionTableModel;
    private JTable questionTable;

    private DefaultTableModel quizQuestionTableModel;
    private JTable quizQuestionTable;

    private String QUESTION_FILE_PATH ;
    private String QUIZ_QUESTION_FILE_PATH;

public CreateQuiz(String courseName) {
    initializeComponents(courseName);
    setupLayout();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(900, 400);
    setLocationRelativeTo(null);
    setVisible(true);
}
    private void initializeComponents(String courseName) {
        this.courseName = courseName;
        setTitle("Quiz for "+ courseName);
        QUESTION_FILE_PATH = "./"+courseName + "/questions.txt";

        topPanel = new JPanel();
        middlePanel = new JPanel(new GridLayout(1, 2));
        bottomPanel = new JPanel();
        quizNameTextField = new JTextField(20);
        SpinnerDateModel dateModel = new SpinnerDateModel();
        quizDateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(quizDateSpinner, "dd/MM/yyyy");
        quizDateSpinner.setEditor(dateEditor);

        SpinnerListModel timeModel = new SpinnerListModel(createTimeList());
        timeSpinner = new JSpinner(timeModel);

        questionTableModel = new DefaultTableModel();
        quizQuestionTableModel = new DefaultTableModel();


        addButton = new JButton("Add");
        deleteButton = new JButton("Delete");
        createQuizButton = new JButton("Create Quiz");
        closeButton = new JButton("Close");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = questionTable.getSelectedRow();
                if (selectedIndex != -1) {
                    String question = questionTableModel.getValueAt(selectedIndex, 0).toString();
                    String answer = questionTableModel.getValueAt(selectedIndex, 1).toString();
                    int points = Integer.parseInt(JOptionPane.showInputDialog(CreateQuiz.this,
                            "Enter points:", "0"));
                    QuizBank quizBank = new QuizBank(question,answer,points);
                    quizQuestionTableModel.addRow(new Object[]{quizBank.getQuestion(), quizBank.getAnswer(),quizBank.getPoints()});
                    questionTableModel.removeRow(selectedIndex);
                }
                else
                {
                    JOptionPane.showMessageDialog(CreateQuiz.this, "Question is not selected");
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = quizQuestionTable.getSelectedRow();
                if (selectedIndex != -1) {
                    String question = quizQuestionTableModel.getValueAt(selectedIndex, 0).toString();
                    String answer = quizQuestionTableModel.getValueAt(selectedIndex, 1).toString();
                    QuestionBank selectedQuestion = new QuestionBank(question, answer);
                    quizQuestionTableModel.removeRow(selectedIndex);
                    questionTableModel.addRow(new Object[]{selectedQuestion.getQuestion(), selectedQuestion.getAnswer()});
                }
            }
        });
        createQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(quizNameTextField != null && !Objects.equals(quizNameTextField.getText(), "")) {
                    String quiName = quizNameTextField.getText();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String date = dateFormat.format(quizDateSpinner.getValue());
                    String time = timeSpinner.getValue().toString();

                    QUIZ_QUESTION_FILE_PATH = "./"+courseName +"/quizzes/"+quizNameTextField.getText()+"_"+date+"_"+time.replace(":","")+".txt";

                    if (quizQuestionTableModel.getRowCount() != 0) {
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(QUIZ_QUESTION_FILE_PATH))) {


                            writer.write(quiName+"|"+ date + "|" + time);
                            writer.newLine();

                            for (int i = 0; i < quizQuestionTableModel.getRowCount(); i++) {
                                String question = (String) quizQuestionTableModel.getValueAt(i, 0).toString();
                                String answer = (String) quizQuestionTableModel.getValueAt(i, 1).toString();
                                int points = (int) quizQuestionTableModel.getValueAt(i, 2);
                                String line = question + "|" + answer +"|"+ points;
                                writer.write(line);
                                writer.newLine();
                            }

                            JOptionPane.showMessageDialog(CreateQuiz.this, "Quiz is created");

                            for(int i = 0; i < quizQuestionTableModel.getRowCount(); i++){
                                String question = (String) quizQuestionTableModel.getValueAt(i, 0).toString();
                                String answer = (String) quizQuestionTableModel.getValueAt(i, 1).toString();
                                QuestionBank selectedQuizQuestion = new QuestionBank(question, answer);
                                questionTableModel.addRow(new Object[]{selectedQuizQuestion.getQuestion(), selectedQuizQuestion.getAnswer()});
                            }

                            quizQuestionTableModel.setRowCount(0);
                            quizNameTextField.setText("");
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(CreateQuiz.this, "Quiz question List is empty");
                    }
                }else
                {
                    JOptionPane.showMessageDialog(CreateQuiz.this, "Quiz name is empty");
                }
            }
        });
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        topPanel.setLayout(new FlowLayout());
        topPanel.add(new JLabel("Quiz Name:"));
        topPanel.add(quizNameTextField);
        topPanel.add(new JLabel("Quiz Date:"));
        topPanel.add(quizDateSpinner);
        topPanel.add(new JLabel("Quiz Time:"));
        topPanel.add(timeSpinner);

        questionTableModel.addColumn("Question");
        questionTableModel.addColumn("Answer");


        questionTable = new JTable(questionTableModel);
        JScrollPane scrollPane = new JScrollPane(questionTable);
        middlePanel.add(scrollPane);

        quizQuestionTableModel.addColumn("Question");
        quizQuestionTableModel.addColumn("Answer");
        quizQuestionTableModel.addColumn("Points");

        quizQuestionTable = new JTable(quizQuestionTableModel);
        JScrollPane quizScrollPane = new JScrollPane(quizQuestionTable);
        middlePanel.add(quizScrollPane);

        File courseFile = new File(QUESTION_FILE_PATH);
        if(courseFile.exists())
        {
            loadQuestionsFromFile();
        }

        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(addButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(createQuizButton);
        bottomPanel.add(closeButton);

        add(topPanel, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);


    }
    private String[] createTimeList() {
        String[] times = new String[24 * 60];
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        for (int i = 0; i < 24 * 60; i++) {
            times[i] = timeFormat.format(calendar.getTime());
            calendar.add(Calendar.MINUTE, 1);
        }

        return times;
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
}
