package com.semester.teacher;

import com.semester.login.LoginPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class TeacherDisplay extends JFrame {
    private JButton createQAButton;
    private JButton createQuizButton;
    private JButton backToLoginButton;
    private final String[] semesterCourses = {
            "Database Management System Theory (DBT-2001)",
            "Database Management System Lab (DBL-2001)",
            "Object Oriented Programming Theory (OOPT-2002)",
            "Object Oriented Programming Lab (OOPL-2002)",
            "Operating System Lab (OSL-2003)",
            "Operating System Theory(OSL-2003)",
            "Software Design and Architecture (SDA-2004)"
    };

    public TeacherDisplay(){
        setTitle("Teacher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        createDirectoryForEachCourse(semesterCourses);
        JList<String> courseList = new JList<>(semesterCourses);
        courseList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Add the list component to a scroll pane
        JScrollPane scrollPane = new JScrollPane(courseList);
        add(scrollPane, BorderLayout.CENTER);

        // Create the buttons
        createQAButton = new JButton("Create Question and Answer");
        createQuizButton = new JButton("Create Quiz");
        backToLoginButton = new JButton("Back to login");

        // Add button listeners
        createQAButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = courseList.getSelectedIndex();
                if (selectedIndex != -1) {
                    CreateQuestionAndAnswer createQuestionAndAnswer = new CreateQuestionAndAnswer(courseList.getSelectedValue());
                    createQuestionAndAnswer.setVisible(true);
                }
                else
                {
                    JOptionPane.showMessageDialog(TeacherDisplay.this, "Course is not selected");
                }
            }
        });

        createQuizButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = courseList.getSelectedIndex();
                if (selectedIndex != -1) {
                    CreateQuiz createQuiz = new CreateQuiz(courseList.getSelectedValue());
                    createQuiz.setVisible(true);
                }
                else
                {
                    JOptionPane.showMessageDialog(TeacherDisplay.this, "Course is not selected");
                }
            }
        });
        backToLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new LoginPage().setVisible(true);
                dispose();
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1));
        buttonPanel.add(createQAButton);
        buttonPanel.add(createQuizButton);
        buttonPanel.add(backToLoginButton);

        add(buttonPanel, BorderLayout.EAST);

    }

    private void createDirectoryForEachCourse(String[] courses)
    {
        for (String course : courses  ) {
            File parentDirectory = new File("./"+course);
            if (!parentDirectory.exists()) {
                boolean created = parentDirectory.mkdirs();
                if (created) {
                    System.out.println("Directory created successfully.");
                } else {
                    System.out.println("Failed to create the directory.");
                }
            } else {
                System.out.println("Directory already exists.");
            }
            File subDirectory = new File("./"+course+"/quizzes");
            if (!subDirectory.exists()) {
                boolean created = subDirectory.mkdirs();
                if (created) {
                    System.out.println("Subdirectory created successfully.");
                } else {
                    System.out.println("Failed to create the subdirectory.");
                }
            } else {
                System.out.println("Subdirectory already exists.");
            }

        }

    }

}
