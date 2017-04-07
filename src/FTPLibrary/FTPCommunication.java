package FTPLibrary;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

/**
 * Created by aurim on 2017-03-26.
 */
public class FTPCommunication
{
    private Socket comSocket;
    private FTPReader comReader = null;
    private FTPWriter comWriter = null;

    //public FTPReplyGettingThread replyThread;
    public FTPCommunication(Socket socket) throws IOException
    {
        this.comSocket = socket;
        comReader = new FTPReader(socket.getInputStream());
        comWriter = new FTPWriter(socket.getOutputStream());
    }
    public FTPReply getFTPReply() throws IOException
    {
        int code = 0;
        String line;
        ArrayList<String> messages = new ArrayList<String>();
        while(true)
        {
            line = comReader.readLine();
            code = Integer.parseInt(line.substring(0, 3));
            messages.add(line.substring(4));

            System.out.println(line);

            // Last message line
            if (line.charAt(3) == ' ')
            {
                break;
            }
        }
        return new FTPReply(code, messages.toArray(new String[0]));
    }
    public void sendFTPCommand(String command) throws IOException
    {
        comWriter.writeLine(command);
        System.out.println(command);
    }
    /*
    public Socket connect(String hostname, int port) throws IOException
    {
        socket = new Socket(hostname, port);
        comReader = new FTPLibrary.FTPStringTool.FTPReader(socket.getInputStream());
        writer = new FTPLibrary.FTPWriter(socket.getOutputStream());
        if (!getFTPReply().isSuccess())
        {
            throw new IOException("Can't connect");
        }
        return socket;
    }
    */


    /*
    public void setReader(FTPLibrary.FTPStringTool.FTPReader reader)
    {
        this.reader = reader;
    }
    public void setWriter(FTPLibrary.FTPWriter writer)
    {
        this.writer = writer;
    }
    */
}
