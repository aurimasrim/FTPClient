import javax.swing.*;

/**
 * Created by aurim on 2017-04-02.
 */
public class Main
{
    public static void main(String[] args)
    {
        LoginWindow lWindow = new LoginWindow();
        lWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lWindow.pack();
        lWindow.setVisible(true);
    }
}
