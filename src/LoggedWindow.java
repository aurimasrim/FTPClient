import FTPLibrary.FTPClient;
import FTPLibrary.FTPFile;

import javax.naming.NoPermissionException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

/**
 * Created by aurim on 2017-04-02.
 */
public class LoggedWindow extends JFrame
{
    private JPanel mainPanel;
    private JPanel buttonsPanel;
    private JList list;
    private JButton buttonUp;
    private JButton deleteButton;
    private JButton buttonUpload;
    private FTPFile[] currentFiles;

    private FTPClient client;
    //private DefaultTableModel model;
    private DefaultTableModel model = new DefaultTableModel()
    {
        public boolean isCellEditable(int row, int column)
        {
            return false;//This causes all cells to be not editable
        }
    };
    private JTable table = new JTable(model);
    private JButton buttonRename;
    private JButton buttonNewFolder;

    public LoggedWindow(FTPClient client)
    {
        this.client = client;
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(400, 400);
        this.add(new JScrollPane(table));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //model = (DefaultTableModel) table.getModel();

        model.addColumn("name");
        model.addColumn("size");
        model.addColumn("modify date");
        model.addColumn("permissions");
        //model.addRow(new Object[]{"asdf", "2", "333"});
        listDirectories();
        table.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2)
                {
                    FTPFile clickedFile = currentFiles[table.getSelectedRow()];

                    if (clickedFile.isDir())
                    {
                        changeDirectory(clickedFile.getName());
                    }
                    else
                    {
                        getFile(clickedFile.getName());
                    }
                }
            }
        });
        buttonUp.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                changeDirectoryUp();
            }
        });
        buttonUpload.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                uploadFile();
            }
        });
    }
    public void listDirectories ()
    {
        try
        {
            //String[] array =
            //client.getDirectoryListing();
            currentFiles = client.getDirectoryListing();
            updateTable(currentFiles);

        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        catch(NoPermissionException ex)
        {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    public void changeDirectory(String directory)
    {
        try
        {
            client.changeCurrentDirectory(directory);
        }
        catch (IOException exc)
        {
            JOptionPane.showMessageDialog(null, exc.getMessage());
        }
        listDirectories();
    }
    public void changeDirectoryUp()
    {
        try
        {
            client.changeDirectoryUp();
        }
        catch (IOException exc)
        {
            JOptionPane.showMessageDialog(null, exc.getMessage());
        }
        listDirectories();
    }
    public void updateTable(FTPFile[] array)
    {
        model.setRowCount(0);
        for(FTPFile file : array)
        {
            model.addRow(new Object[]{file.getName(), file.getSize(), file.getModifyDate().toString(), file.getPermissions()});
            //model.addRow(new Object[]{"asd", "2", "3", "4"});
            //System.out.println(file.getName());
            //System.out.println(file.getSize());
            //System.out.println(file.getModifyDate().toString());
            //System.out.println(file.getPermissions());
        }
    }
    public void getFile(String name)
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(LoggedWindow.this) == JFileChooser.APPROVE_OPTION)
        {
            String path = chooser.getSelectedFile() + "\\" + name;
            path = path.replace("\\", "\\\\");
            File file = new File(path);
            if(file.exists())
            {
                JOptionPane.showMessageDialog(null, "File already exists");
                return;
            }
            try
            {
                file.createNewFile();
                client.downloadFile(file);
            }
            catch(IOException exc)
            {
                JOptionPane.showMessageDialog(null, exc.getMessage());
            }
        }


    }
    public void uploadFile()
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                client.uploadFile(chooser.getSelectedFile());
            }
            catch(IOException exc)
            {
                JOptionPane.showMessageDialog(this, exc.getMessage());
            }
            catch(NoPermissionException exc)
            {
                JOptionPane.showMessageDialog(this, exc.getMessage());
            }
            listDirectories();
        }
    }
}
