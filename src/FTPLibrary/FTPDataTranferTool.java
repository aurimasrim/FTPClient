package FTPLibrary;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by aurim on 2017-03-27.
 */
public class FTPDataTranferTool
{
    FTPStringTool st = new FTPStringTool();
    public FTPFile[] getList(Socket socket) throws IOException
    {
        FTPReader dataReader = new FTPReader(socket.getInputStream());
        ArrayList<FTPFile> files = new ArrayList<FTPFile>();
        String line;
        while (true)
        {
            if((line = dataReader.readLine()) == null)
            {
                break;
            }
            files.add(st.extractFTPFile(line));
        }
        socket.close();
        return files.toArray(new FTPFile[0]);
    }
    public void sendData(File file, Socket socket) throws IOException
    {
        BufferedOutputStream outStream = new BufferedOutputStream(socket.getOutputStream());
        BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));

        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        while((bytesRead = inStream.read(buffer)) != -1)
        {
            outStream.write(buffer, 0, bytesRead);
        }
        outStream.flush();
        outStream.close();
        inStream.close();
        socket.close();
    }
    public void getData(File file, Socket socket) throws IOException
    {
        BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(file));
        BufferedInputStream inStream = new BufferedInputStream(socket.getInputStream());

        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        while((bytesRead = inStream.read(buffer)) != -1)
        {
            outStream.write(buffer, 0, bytesRead);
        }
        outStream.flush();
        outStream.close();
        inStream.close();
        socket.close();
    }
}
