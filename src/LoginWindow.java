import FTPLibrary.FTPClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by aurim on 2017-03-31.
 */
public class LoginWindow extends JFrame
{
    private JPanel mainPanel;
    private JPanel buttonsPanel;
    private JPanel labelsPanel;
    private JPanel fieldsPanel;
    private JButton exitButton;
    private JButton loginButton;
    private JTextField textServer;
    private JCheckBox checkBoxLogin;
    private JTextField textPort;
    private JTextField textUser;
    private JPasswordField textPassword;
    private JLabel labelServer;
    private JLabel labelPort;
    private JLabel labelUser;
    private JLabel labelPassword;
    private JLabel labelCheckBox;
    private FTPClient client;
    private LoggedWindow loggedWindow;

    public LoginWindow()
    {
        setContentPane(mainPanel);
        client = new FTPClient();
        setLoginInfoVisible(false);
        exitButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });
        loginButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                onLoginButton();
            }
        });
    }



    public void setLoginInfoVisible(boolean value)
    {
        labelUser.setVisible(value);
        labelPassword.setVisible(value);
        textUser.setVisible(value);;
        textPassword.setVisible(value);;
    }
    public void setServerInfoVisible(boolean value)
    {
        labelServer.setVisible(value);
        labelPort.setVisible(value);
        labelCheckBox.setVisible(value);
        textServer.setVisible(value);
        textPort.setVisible(value);
        checkBoxLogin.setVisible(value);
    }
    public void onLoginButton()
    {
        if (textServer.isVisible())
        {
            connect();
        }
        else
        {
            login();
        }
    }
    private void connect()
    {
        if (textServer.getText().isEmpty()||textPort.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Fill empty fields");
        }
        else
        {
            try
            {
                client.connect(textServer.getText(), Integer.parseInt(textPort.getText()));
                if (checkBoxLogin.isSelected())
                {
                    client.loginAnonymous();
                    this.setVisible(false);
                    loggedWindow = new LoggedWindow(client);
                    loggedWindow.setVisible(true);
                }
                else
                {
                    setServerInfoVisible(false);
                    setLoginInfoVisible(true);
                }
            }
            catch (IOException exc)
            {
                try
                {
                    client.disconnect();
                }
                catch (IOException exc1)
                {
                    JOptionPane.showMessageDialog(null,"Error: " +  exc1.getMessage());
                }
                setLoginInfoVisible(false);
                setServerInfoVisible(true);
                JOptionPane.showMessageDialog(null,"Error: " +  exc.getMessage());
            }
        }
    }
    private void login()
    {
        if (textUser.getText().isEmpty()||textPassword.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Fill empty fields");
        }
        else
        {
            try
            {
                client.login(textUser.getText(), textPassword.getText());
                LoginWindow.this.setVisible(false);
                loggedWindow = new LoggedWindow(client);
                loggedWindow.setVisible(true);
            }
            catch (IOException exc)
            {
                try
                {
                    client.disconnect();
                }
                catch (IOException exc1)
                {
                    JOptionPane.showMessageDialog(null,"Error: " +  exc1.getMessage());
                }
                setLoginInfoVisible(false);
                setServerInfoVisible(true);
                JOptionPane.showMessageDialog(null, "Error: " + exc.getMessage());
            }
        }
    }
}
