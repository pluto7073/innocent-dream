package io.innocent.dream.auth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import io.innocent.dream.InnocentDream;

public class AccountLogin extends JFrame {
    private JPanel panel1;
    private JTextField username;
    private JPasswordField token;
    private JButton loginBtn;

    public AccountLogin() {
        super("Log In");
        super.setSize(new Dimension(800, 600));
        super.setVisible(true);
        super.add(panel1);
        loginBtn.addActionListener(this::login);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        super.setLocation(dim.width / 2 - getWidth() / 2, dim.height / 2 - getHeight() / 2);
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void login(ActionEvent e) {
        String usernameStr = username.getText().trim();
        String tokenStr = new String(token.getPassword());
        boolean success = InnocentDream.apiManager.logIn(usernameStr, tokenStr);
        if (!success) System.out.println("Incorrect Username or Password");
        super.dispose();
    }

}
