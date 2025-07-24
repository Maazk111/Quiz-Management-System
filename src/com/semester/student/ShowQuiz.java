package com.semester.student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ShowQuiz extends JFrame {

    private DefaultTableModel showQuizTableModel;
    private JTable showQuizTable;
    private String courseName;
    private String QUIZ_DIRECTORY_PATH ;
    private String QUIZ_RESULT__DIRECTORY_PATH ;
    private JButton attemptQuizButton;
    private JButton closeButton;
    private int serialNum;
    private int total;
    private String userName;
    private String quizName;


    public ShowQuiz(String userName,String courseName,String quizName,int total,String quizDate, String quizTime){
        this.total = total;
        this.courseName = courseName;
        this.quizName = quizName;
        QUIZ_RESULT__DIRECTORY_PATH = "./"+courseName +"/Quiz_Result";

        if(compareDate(quizDate,quizTime)) {
            saveTotalToFile(userName, total);
        }
        else {
            JOptionPane.showMessageDialog(ShowQuiz.this, "Unfortunately your quiz time is up! Your quiz is not save ");
        }

        this.dispose();

    }
    public ShowQuiz(String userName,String courseName) {

        this.userName = userName;
        this.courseName = courseName;
        QUIZ_DIRECTORY_PATH = "./"+courseName +"/quizzes";


        setTitle("Quiz List for "+ courseName);
        setSize(900, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        serialNum= 0;

        showQuizTableModel = new DefaultTableModel();
        showQuizTableModel.addColumn("S.No");
        showQuizTableModel.addColumn("Quiz Name");
        showQuizTableModel.addColumn("Date");
        showQuizTableModel.addColumn("Time");

        showQuizTable = new JTable(showQuizTableModel);
        JScrollPane scrollPane = new JScrollPane(showQuizTable);
        add(scrollPane, BorderLayout.WEST);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
        attemptQuizButton = new JButton("Attempt Quiz");
        closeButton = new JButton("Close");

        buttonPanel.add(attemptQuizButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.EAST);

        loadFileListFromDirectory();

        attemptQuizButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int selectedIndex = showQuizTable.getSelectedRow();
                if (selectedIndex != -1) {
                    quizName = showQuizTableModel.getValueAt(selectedIndex, 1).toString();
                    String quizDate = showQuizTableModel.getValueAt(selectedIndex, 2).toString();
                    String quizTime = showQuizTableModel.getValueAt(selectedIndex, 3).toString();
                    AttemptQuiz attemptQuiz = new AttemptQuiz(userName,courseName,quizName,quizDate,quizTime);
                    attemptQuiz.setVisible(true);
                    dispose();
                }
            }
        });

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void loadFileListFromDirectory(){
        File directory = new File(QUIZ_DIRECTORY_PATH);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        String fileName = file.getName().replaceFirst("[.][^.]+$", "");
                        System.out.println(fileName);
                        String[] parts = fileName.split("_");
                        if (compareDate(parts[1], parts[2])) {
                            serialNum++;
                            showQuizTableModel.addRow(new Object[]{serialNum, parts[0], parts[1], parts[2]});
                        }

                    }

                }
            }
        } else {
            System.out.println("Directory does not exist.");
        }
    }

    private boolean compareDate(String date, String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = sdf.parse(date);
            LocalDate today = LocalDate.now();
            LocalDate parsedLocalDate = parsedDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            int comparisonResult = parsedLocalDate.compareTo(today);

            if (comparisonResult < 0) {
                System.out.println("The parsed date is before.");
            } else if (comparisonResult == 0) {
                System.out.println("The parsed date is today.");
                return compareTime(time);
            } else {
                return true;
            }

        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        return false;
    }
    private boolean compareTime(String time)
    {
        time = time.substring(0,2)+":"+time.substring(2);

        String timeFormat = "HH:mm";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormat);
        LocalTime parsedTime = LocalTime.parse(time, formatter);

        LocalTime currentTime = LocalTime.now();

        int comparisonResult = currentTime.compareTo(parsedTime);

        if (comparisonResult < 0) {
            System.out.println("The parsed time is before the current time.");
            return true;
        }
        return false;
    }
    private void saveTotalToFile(String userName, int total) {
        File directory = new File(QUIZ_RESULT__DIRECTORY_PATH);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("Directory created successfully.");
            } else {
                System.out.println("Failed to create the directory.");
            }
        }
        if (directory.exists() && directory.isDirectory()) {
           String QUIZ_RESULT_PATH = "./"+courseName +"/quiz_Result/"+quizName+".txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(QUIZ_RESULT_PATH))) {
                    String line = userName + "|" + total;
                    writer.write(line);
                    writer.newLine();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else
        {
            System.out.println("Directory does not exist.");
        }

    }
}
