package com.second;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class TeacherForm extends JFrame {
    private JButton JAddQuestionButton;
    private JButton JQuizButton;
    private JButton JBack;
    private String[] courses;

    public TeacherForm(){
        setTitle("Teacher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        courses = new String[]{
                "DBT-2001",
                "DBL-2001",
                "OOPT-2002",
                "OOPL-2002",
                "OSL-2003",
                "OSL-2003",
                "SDA-2004"
        };
        JList<String> courseList = new JList<>(courses);
        courseList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Add the list component to a scroll pane
        JScrollPane scrollPane = new JScrollPane(courseList);
        add(scrollPane, BorderLayout.CENTER);

        // Create the buttons
        JAddQuestionButton = new JButton("Add Question");
        JQuizButton = new JButton("Make Quiz");
        JBack = new JButton("Back");

        // Add button listeners
        JAddQuestionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = courseList.getSelectedIndex();
                if (index != -1) {
                    AddQuestion addQuestion = new AddQuestion(courseList.getSelectedValue());
                    addQuestion.setVisible(true);
                }
                else
                {
                    JOptionPane.showMessageDialog(TeacherForm.this, "You should select course");
                }
            }
        });

        JQuizButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = courseList.getSelectedIndex();
                if (selectedIndex != -1) {
                    MakeQuiz makeQuiz = new MakeQuiz(courseList.getSelectedValue());
                    makeQuiz.setVisible(true);
                }
                else
                {
                    JOptionPane.showMessageDialog(TeacherForm.this, "Course is not selected");
                }
            }
        });
        JBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new LoginStartForm().setVisible(true);
                dispose();
            }
        });

        // Create a panel for the buttons
        //JPanel buttonPanel = new JPanel(new GridLayout(3, 1));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(JAddQuestionButton);
        buttonPanel.add(JQuizButton);
        buttonPanel.add(JBack);

        add(buttonPanel, BorderLayout.SOUTH);


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
