package com.semester.student;

import com.semester.login.LoginPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentDisplay extends JFrame {

    private JButton showQuizButton;
    private JButton createQuizButton;
    private JButton backToLoginButton;
    private String userName;

    public StudentDisplay(String userName){
        setTitle("Student");

        this.userName = userName;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        String[] courses = {
                "Database Management System Theory (DBT-2001)",
                "Database Management System Lab (DBL-2001)",
                "Object Oriented Programming Theory (OOPT-2002)",
                "Object Oriented Programming Lab (OOPL-2002)",
                "Operating System Lab (OSL-2003)",
                "Operating System Theory(OSL-2003)",
                "Software Design and Architecture (SDA-2004)"
        };

        JList<String> courseList = new JList<>(courses);
        courseList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scrollPane = new JScrollPane(courseList);
        add(scrollPane, BorderLayout.CENTER);

        showQuizButton = new JButton("Show Quiz");
        backToLoginButton = new JButton("Back to login");

        showQuizButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = courseList.getSelectedIndex();
                if (selectedIndex != -1) {
                    ShowQuiz showQuiz = new ShowQuiz(userName,courseList.getSelectedValue());
                    showQuiz.setVisible(true);
                }
                else
                {
                    JOptionPane.showMessageDialog(StudentDisplay.this, "Course is not selected");
                }
            }
        });
        backToLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new LoginPage().setVisible(true);
                dispose();
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
        buttonPanel.add(showQuizButton);
        buttonPanel.add(backToLoginButton);

        add(buttonPanel, BorderLayout.EAST);


    }

}
