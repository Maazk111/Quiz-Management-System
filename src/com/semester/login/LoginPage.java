package com.semester.login;
import com.semester.student.StudentDisplay;
import com.semester.teacher.TeacherDisplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {
    private JLabel userNameLabel;
    private JLabel passwordLabel;
    //private JLabel userTypeLabel;
    private JTextField userNameTextField;
    private JPasswordField passwordField;
    //private JComboBox<String> userTypeDropdown;
    private JButton loginButton;

    private static final User[] users = {
            new Teacher("Sumaira", "admin123"),
            new Student("Maaz", "stu123"),
            new Student("Rumaisa", "stu123"),
            new Student("Farrukh", "stu123"),
            new Student("Maria", "stu123"),
            new Student("Shayan", "stu123")
    };

    public LoginPage() {
        initComponents();
        initListeners();
    }
    private void initComponents() {
        userNameLabel = new JLabel("Username:");
        passwordLabel = new JLabel("Password:");

        userNameTextField = new JTextField();
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");

        setLayout(new GridLayout(4, 2));
        add(userNameLabel);
        add(userNameTextField);
        add(passwordLabel);
        add(passwordField);

        add(new JLabel());
        add(loginButton);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Login Page");
        setSize(400, 200);
        setLocationRelativeTo(null);
    }

    private void initListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userNameTextField.getText();
                String password = new String(passwordField.getPassword());

                boolean isLoggedIn = false;
                User loggedInUser = null;

                for (User user : users) {
                    if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                        isLoggedIn = true;
                        loggedInUser = user;
                        break;
                    }
                }
                if (isLoggedIn) {
                    if (loggedInUser instanceof Teacher) {
                        System.out.println("Welcome, teacher!");
                        JOptionPane.showMessageDialog(LoginPage.this, "Login successful!");
                        TeacherDisplay teacherDisplay = new TeacherDisplay();
                        teacherDisplay.setVisible(true);
                        dispose();
                    } else if (loggedInUser instanceof Student) {
                        System.out.println("Welcome, student!");
                        StudentDisplay studentDisplay = new StudentDisplay(username);
                        studentDisplay.setVisible(true);
                        dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password!");
                }

            }
        });
    }
}

