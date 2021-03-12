package io.innocent.dream.auth;

import javax.swing.*;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import java.awt.*;
import java.awt.event.ActionEvent;

import io.innocent.dream.InnocentDream;

public class AccountLogin extends JFrame {
	
	private static final long serialVersionUID = -4513199173983045501L;
	private JPanel panel1;
    private JTextField username;
    private JPasswordField token;
    private JButton loginBtn;

    public AccountLogin() {
        super("Log In");
        this.$$$setupUI$$$();
        super.setSize(new Dimension(800, 600));
        super.setVisible(true);
        super.add(this.panel1);
        this.loginBtn.addActionListener(this::login);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        super.setLocation(dim.width / 2 - this.getWidth() / 2, dim.height / 2 - this.getHeight() / 2);
        super.setDefaultCloseOperation(3);
    }

    public void login(ActionEvent e) {
    	String usernameStr = username.getText().trim();
        String tokenStr = new String(token.getPassword());
        boolean success = InnocentDream.apiManager.logIn(usernameStr, tokenStr);
        if (!success) System.out.println("Incorrect Username or Token");
        super.dispose();
    }

    private void $$$setupUI$$$() {
        this.panel1 = new JPanel();
        this.panel1.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        this.panel1.add(panel2, new GridConstraints(0, 1, 1, 1, 0, 3, 3, 3, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
        this.username = new JTextField();
        panel2.add(this.username, new GridConstraints(1, 1, 1, 1, 8, 1, 4, 0, (Dimension)null, new Dimension(150, -1), (Dimension)null, 0, false));
        Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(0, 1, 1, 1, 0, 2, 1, 4, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
        JLabel label1 = new JLabel();
        label1.setText("Username: ");
        panel2.add(label1, new GridConstraints(1, 0, 1, 1, 8, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
        JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        this.panel1.add(panel3, new GridConstraints(1, 1, 1, 1, 0, 3, 3, 3, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
        this.token = new JPasswordField();
        panel3.add(this.token, new GridConstraints(0, 1, 1, 1, 8, 1, 4, 0, (Dimension)null, new Dimension(150, -1), (Dimension)null, 0, false));
        Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(2, 1, 1, 1, 0, 2, 1, 4, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
        JLabel label2 = new JLabel();
        label2.setText("Game Token (Not Password): ");
        panel3.add(label2, new GridConstraints(0, 0, 1, 1, 8, 0, 0, 0, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
        this.loginBtn = new JButton();
        this.loginBtn.setText("Log In");
        panel3.add(this.loginBtn, new GridConstraints(1, 1, 1, 1, 0, 1, 3, 0, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
        Spacer spacer3 = new Spacer();
        this.panel1.add(spacer3, new GridConstraints(0, 0, 1, 1, 0, 1, 4, 1, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
        Spacer spacer4 = new Spacer();
        this.panel1.add(spacer4, new GridConstraints(0, 2, 1, 1, 0, 1, 4, 1, (Dimension)null, (Dimension)null, (Dimension)null, 0, false));
    }

    public JComponent $$$getRootComponent$$$() {
        return this.panel1;
    }

}
