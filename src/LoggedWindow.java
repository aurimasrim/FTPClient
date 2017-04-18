import FTPLibrary.FTPClient;
import FTPLibrary.FTPFile;
import jdk.nashorn.internal.scripts.JO;

import javax.naming.NoPermissionException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.SocketException;

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

        setSize(450, 400);
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
        deleteButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    FTPFile clickedFile = currentFiles[table.getSelectedRow()];

                    if (clickedFile.isDir())
                    {
                        deleteDirectory(clickedFile.getName());
                    }
                    else
                    {
                        deleteFile(clickedFile.getName());
                    }
                }
                catch (ArrayIndexOutOfBoundsException exc)
                {
                    JOptionPane.showMessageDialog(null, "File or directory not selected");
                }
            }
        });
        buttonNewFolder.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                createDirectory();
            }
        });
        buttonRename.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    FTPFile clickedFile = currentFiles[table.getSelectedRow()];
                    rename(clickedFile.getName());
                }
                catch (ArrayIndexOutOfBoundsException exc)
                {
                    JOptionPane.showMessageDialog(null, "File or directory not selected");
                }
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
        catch (SocketException exc)
        {
            JOptionPane.showMessageDialog(null, exc.getMessage());
            System.exit(1);
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
        catch (SocketException exc)
        {
            JOptionPane.showMessageDialog(null, exc.getMessage());
            System.exit(1);
        }
        catch (IOException exc)
        {
            JOptionPane.showMessageDialog(null, exc.getMessage());
        }
        listDirectories();
    }
    public void updateTable(FTPFile[] array)
    {
        // išvalo
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
    // create empty file and download from server
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
            catch (SocketException exc)
            {
                JOptionPane.showMessageDialog(null, exc.getMessage());
                System.exit(1);
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
            catch (SocketException exc)
            {
                JOptionPane.showMessageDialog(null, exc.getMessage());
                System.exit(1);
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
    public void deleteFile(String name)
    {
        try
        {
            client.deleteFile(name);
        }
        catch (SocketException exc)
        {
            JOptionPane.showMessageDialog(null, exc.getMessage());
            System.exit(1);
        }
        catch (IOException exc)
        {
            JOptionPane.showMessageDialog(this, exc.getMessage());
        }
        listDirectories();
    }
    public void deleteDirectory(String name)
    {
        try
        {
            client.deleteDirectory(name);
        }
        catch (SocketException exc)
        {
            JOptionPane.showMessageDialog(null, exc.getMessage());
            System.exit(1);
        }
        catch (IOException exc)
        {
            JOptionPane.showMessageDialog(this, exc.getMessage());
        }
        listDirectories();
    }
    public void createDirectory()
    {
        try
        {
            String name = JOptionPane.showInputDialog("Folder name");
            if (!name.isEmpty())
            {
                client.createDirectory(name);
            }
            listDirectories();
        }
        catch (SocketException exc)
        {
            JOptionPane.showMessageDialog(null, exc.getMessage());
            System.exit(1);
        }
        catch (IOException exc)
        {
            JOptionPane.showMessageDialog(this, exc.getMessage());
        }
        catch (NullPointerException exc) {}

    }
    public void rename(String name)
    {
        try
        {
            String newName = JOptionPane.showInputDialog("Rename to");
            if (!newName.isEmpty())
            {
                client.renameDirectory(name, newName);
                listDirectories();
            }
        }
        catch (NullPointerException exc) {}
        catch (SocketException exc)
        {
            JOptionPane.showMessageDialog(null, exc.getMessage());
            System.exit(1);
        }
        catch (IOException exc)
        {
            JOptionPane.showMessageDialog(this, exc.getMessage());
        }

    }
}
