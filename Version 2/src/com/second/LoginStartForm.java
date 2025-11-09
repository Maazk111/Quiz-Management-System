package com.second;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginStartForm extends JFrame {
    private JLabel userName;
    private JLabel password;
    private JLabel userTypeLabel;
    private JTextField jTextField;
    private JPasswordField jPasswordField;
    private JComboBox<String> userTypeDropdown;
    private JButton jLogin;

    public LoginStartForm() {

        userName = new JLabel("Username:");
        password = new JLabel("Password:");
        userTypeLabel = new JLabel("User Type:");
        jTextField = new JTextField();
        jPasswordField = new JPasswordField();
        userTypeDropdown = new JComboBox<>(new String[]{"Teacher", "Student"});
        jLogin = new JButton("Login");

        setLayout(new GridLayout(4, 2));
        add(userName);
        add(jTextField);
        add(password);
        add(jPasswordField);
        add(userTypeLabel);
        add(userTypeDropdown);
        add(new JLabel());
        add(jLogin);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Login Page");
        setSize(400, 200);
        setLocationRelativeTo(null);


        jLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = jTextField.getText();
                String password = new String(jPasswordField.getPassword());
                String usertype = (String) userTypeDropdown.getSelectedItem();
                if ((username.equals("teacher") && password.equals("password") && usertype.equals("Teacher")) || (username.equals("student") && password.equals("password") && usertype.equals("Student"))) {
                    JOptionPane.showMessageDialog(LoginStartForm.this, "Login successful!");
                    if (usertype.equals("Teacher")) {
                        TeacherForm teacherForm = new TeacherForm();
                        teacherForm.setVisible(true);
                        dispose();
                    } else {
                        StudentForm studentForm = new StudentForm(username);
                        studentForm.setVisible(true);
                        dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid credentials!");
                }
            }
        });
    }
}





