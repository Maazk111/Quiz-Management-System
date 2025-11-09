package com.second;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Pattern;

public class StudentForm extends JFrame {

    private DefaultTableModel quizModel;
    private JTable quizTable;
    private String courseName;
    private String resultPath;
    private JButton startQuizButton;
    private JButton back;
    private int total;
    private String userName;
    private String quizName;

    String[] courses = {
            "DBT-2001",
            "DBL-2001",
            "OOPT-2002",
            "OOPL-2002",
            "OSL-2003",
            "OSL-2003",
            "SDA-2004"
    };


    public StudentForm(String userName, String courseName, String quizName, int total, String quizDate, String quizTime){
        this.total = total;
        this.courseName = courseName;
        this.quizName = quizName;
        //resultPath = courseName +"_Quiz_Result";

        if(compareDate(quizDate,quizTime)) {
            saveTotalToFile(userName, total);
        }
        else {
            JOptionPane.showMessageDialog(StudentForm.this, "Unfortunately your quiz time is up! Your quiz is not save ");
        }

    }
    public StudentForm(String userName) {

        this.userName = userName;
        this.courseName = courseName;



        setTitle("Quiz List");
        setSize(900, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        quizModel = new DefaultTableModel();
        quizModel.addColumn("Course Name");
        quizModel.addColumn("Quiz Name");
        quizModel.addColumn("Date");
        quizModel.addColumn("Time");

        quizTable = new JTable(quizModel);
        JScrollPane scrollPane = new JScrollPane(quizTable);
        add(scrollPane, BorderLayout.WEST);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
        startQuizButton = new JButton("Start Quiz");
        back = new JButton("Back");

        buttonPanel.add(startQuizButton);
        buttonPanel.add(back);
        add(buttonPanel, BorderLayout.SOUTH);

        loadQuizFiles(courses);

        startQuizButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int selectedIndex = quizTable.getSelectedRow();
                if (selectedIndex != -1) {
                    quizName = quizModel.getValueAt(selectedIndex, 1).toString();
                    String quizDate = quizModel.getValueAt(selectedIndex, 2).toString();
                    String quizTime = quizModel.getValueAt(selectedIndex, 3).toString();
                    String courseName = quizModel.getValueAt(selectedIndex, 0).toString();
                     StartQuiz startQuiz = new StartQuiz(userName,courseName,quizName,quizDate,quizTime);
                    startQuiz.setVisible(true);
                    dispose();
                }
            }
        });

        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new LoginStartForm().setVisible(true);
                dispose();
            }
        });
    }

    private void loadQuizFiles(String[] courses){
        File currentDirectory = new File(".");
        File[] files = currentDirectory.listFiles();
        for(String course : courses)
        {
            String partialName = course+"_quiz";
            String regex = Pattern.quote(partialName) + ".*\\.txt";
            Pattern pattern = Pattern.compile(regex);

            for (File file : files) {
                if (file.isFile() && pattern.matcher(file.getName()).matches()) {
                    String fileName = file.getName().replaceFirst("[.][^.]+$", "");
                    String[] parts = fileName.split("_");
                    if (compareDate(parts[3], parts[4])) {
                        System.out.println("parts[3]"+parts[3]);
                        quizModel.addRow(new Object[]{course,parts[2], parts[3], parts[4]});
                    }
                }
            }
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
           String QUIZ_RESULT_PATH = courseName +"_quiz_Result_"+quizName+".txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(QUIZ_RESULT_PATH))) {
                    String line = userName + "|" + total;
                    writer.write(line);
                    writer.newLine();

            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
