package FTPLibrary;

import javax.naming.NoPermissionException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;

/**
 * Created by aurim on 2017-03-25.
 */
public class FTPClient
{
    private String hostname;
    private boolean connected = false;
    private boolean authenticated = false;
    private Socket socket = null;
    private FTPCommunication communication;
    private FTPConnector connector = new FTPConnector();
    private FTPStringTool stringTool = new FTPStringTool();
    private FTPDataTranferTool dtTool = new FTPDataTranferTool();

    public void connect(String hostname) throws IOException
    {
        connect(hostname, 21);
    };
    public void connect(String hostname, int port) throws IOException
    {
        if (connected)
        {
            throw new IOException("Already connected");
        }
        socket = connector.connectCommunicationSocket(hostname, port);
        communication = new FTPCommunication(socket);
        connected = true;
        //replyThread = new FTPReplyGettingThread(this);
        //replyThread.start();
    }
    public void disconnect() throws IOException
    {
        if (!connected)
        {
            throw new IOException("Not connected");
        }
        socket.close();
        connected = false;
    }
    public void loginAnonymous() throws IOException
    {
        login("anonymous", "anonymous");
    }
    public void login (String login, String password) throws IOException
    {
        communication.getFTPReply();
        communication.sendFTPCommand("USER " + login);
        FTPReply tempReply = communication.getFTPReply();
        if (tempReply.getCode() != 331)
        {
            throw new IOException(tempReply.getMessages()[0]);
        }
        communication.sendFTPCommand("PASS " + password);
        tempReply = communication.getFTPReply();
        if(!tempReply.isSuccess())
        {
            throw new IOException(tempReply.getMessages()[0]);
        }
        authenticated = true;
    }
    public FTPFile[] getDirectoryListing() throws IOException, NoPermissionException
    {
        if (!connected)
        {
            throw new IllegalStateException("Not connected");
        }
        if (!authenticated)
        {
            throw new IllegalStateException("Not authenticated");
        }
        communication.sendFTPCommand("PASV");
        FTPReply tempReply = communication.getFTPReply();

        if (!tempReply.isSuccess())
        {
            throw new IOException(tempReply.getMessages()[0]);
        }

        InetSocketAddress socketAddress = stringTool.extractSocketAddress(tempReply);
        communication.sendFTPCommand("MLSD");

        Socket socket = connector.connectDataTransferSocket(socketAddress);
        tempReply = communication.getFTPReply();
        if (!tempReply.isReady())
        {
            throw new NoPermissionException(tempReply.getMessages()[0]);
        }

        FTPFile[] list = dtTool.getList(socket);
        tempReply = communication.getFTPReply();
        if (!tempReply.isSuccess())
        {
            throw new IOException(tempReply.getMessages()[0]);
        }
        return list;
    }
    public void changeCurrentDirectory(String directoryName) throws IOException
    {
        if (!connected)
        {
            throw new IllegalStateException("Not connected");
        }
        if (!authenticated)
        {
            throw new IllegalStateException("Not authenticated");
        }
        communication.sendFTPCommand("CWD " + directoryName);
        FTPReply reply = communication.getFTPReply();
        if(!reply.isSuccess())
        {
            throw new IOException(reply.getMessages()[0]);
        }
    }
    public void changeDirectoryUp() throws IOException
    {
        if (!connected)
        {
            throw new IllegalStateException("Not connected");
        }
        if (!authenticated)
        {
            throw new IllegalStateException("Not authenticated");
        }
        communication.sendFTPCommand("CDUP");
        FTPReply reply = communication.getFTPReply();
        if(!reply.isSuccess())
        {
            throw new IOException(reply.getMessages()[0]);
        }
    }
    public void deleteFile(String name) throws IOException
    {
        if (!connected)
        {
            throw new IllegalStateException("Not connected");
        }
        if (!authenticated)
        {
            throw new IllegalStateException("Not authenticated");
        }
        communication.sendFTPCommand("DELE " + name);
        FTPReply reply = communication.getFTPReply();
        if(!reply.isSuccess())
        {
            throw new IOException(reply.getMessages()[0]);
        }
    }
    public void deleteDirectory(String name) throws IOException
    {
        if (!connected)
        {
            throw new IllegalStateException("Not connected");
        }
        if (!authenticated)
        {
            throw new IllegalStateException("Not authenticated");
        }
        communication.sendFTPCommand("RMD " + name);
        FTPReply reply = communication.getFTPReply();
        if(!reply.isSuccess())
        {
            throw new IOException(reply.getMessages()[0]);
        }
    }
    public void createDirectory(String name) throws IOException
    {
        if (!connected)
        {
            throw new IllegalStateException("Not connected");
        }
        if (!authenticated)
        {
            throw new IllegalStateException("Not authenticated");
        }
        communication.sendFTPCommand("MKD " + name);
        FTPReply reply = communication.getFTPReply();
        if(!reply.isSuccess())
        {
            throw new IOException(reply.getMessages()[0]);
        }
    }
    public void renameDirectory(String from, String to) throws IOException
    {
        if (!connected)
        {
            throw new IllegalStateException("Not connected");
        }
        if (!authenticated)
        {
            throw new IllegalStateException("Not authenticated");
        }
        communication.sendFTPCommand("RNFR " + from);
        FTPReply reply = communication.getFTPReply();
        if(!reply.isReady())
        {
            throw new IOException(reply.getMessages()[0]);
        }
        communication.sendFTPCommand("RNTO " + to);
        reply = communication.getFTPReply();
        if(!reply.isSuccess())
        {
            throw new IOException(reply.getMessages()[0]);
        }
    }
    public void uploadFile(File file) throws IOException, NoPermissionException
    {
        if (!connected)
        {
            throw new IllegalStateException("Not connected");
        }
        if (!authenticated)
        {
            throw new IllegalStateException("Not authenticated");
        }
        communication.sendFTPCommand("PASV");
        FTPReply tempReply = communication.getFTPReply();
        if (!tempReply.isSuccess())
        {
            throw new IOException(tempReply.getMessages()[0]);
        }
        InetSocketAddress socketAddress = stringTool.extractSocketAddress(tempReply);
        Socket socket = connector.connectDataTransferSocket(socketAddress);
        communication.sendFTPCommand("STOR " + file.getName());

        tempReply = communication.getFTPReply();
        if(!tempReply.isReady())
        {
            throw new NoPermissionException(tempReply.getMessages()[0]);
        }
        dtTool.sendData(file, socket);
        tempReply = communication.getFTPReply();
        if (!tempReply.isSuccess())
        {
            throw new IOException(tempReply.getMessages()[0]);
        }
    }
    public void downloadFile(File file) throws IOException
    {
        if (!connected)
        {
            throw new IllegalStateException("Not connected");
        }
        if (!authenticated)
        {
            throw new IllegalStateException("Not authenticated");
        }
        communication.sendFTPCommand("PASV");
        FTPReply tempReply = communication.getFTPReply();
        if (!tempReply.isSuccess())
        {
            throw new IOException(tempReply.getMessages()[0]);
        }
        InetSocketAddress socketAddress = stringTool.extractSocketAddress(tempReply);
        Socket socket = connector.connectDataTransferSocket(socketAddress);
        communication.sendFTPCommand("RETR " + file.getName());
        tempReply = communication.getFTPReply();
        if(!tempReply.isReady())
        {
            throw new IOException(tempReply.getMessages()[0]);
        }
        dtTool.getData(file, socket);
        tempReply = communication.getFTPReply();
        if (!tempReply.isSuccess())
        {
            throw new IOException(tempReply.getMessages()[0]);
        }
    }

    /*
    public void runReplyGetThread()
    {

    }
    */
}
/*
class FTPReplyGettingThread extends Thread
{
    private FTPLibrary.FTPClient client;
    public FTPReplyGettingThread(FTPLibrary.FTPClient client)
    {
        this.client = client;
    }
    public void run()
    {
        for(;;)
        {
            try
            {
                System.out.println(client.getFTPReply().message);
                System.out.println("ciklas\n");
            }
            catch (IOException exc)
            {
                System.out.println("FTP getting exception");
            }
        }
    }
}
*/
