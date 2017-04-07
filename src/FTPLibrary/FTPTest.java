package FTPLibrary; /**
 * Created by aurim on 2017-03-24.
 */
import FTPLibrary.FTPClient;

import java.io.*;
import java.nio.file.Files;

public class FTPTest
{
    public static void main(String[] args) throws Exception
    {
        //Socket TestSocket = new Socket();
        FTPClient client = new FTPClient();
        try
        {
            client.connect("speedtest.tele2.net");
        }
        catch (IOException exc)
        {
            System.out.println("There is no such server");
        }
        client.loginAnonymous();
        client.getDirectoryListing();
        System.out.println(("size=53".substring(1, 3)));
        //client.createDirectory("newfolder");
        //String[] list = client.getDirectoryListing();
        //for(String line : list)
        //{
        //    System.out.println(line);
        //}
        //client.changeCurrentDirectory("upload");
        //list = client.getDirectoryListing();
        //for(String line : list)
        //{
        //    System.out.println(line);
        //}
        //File file = new File("C:\\affiliate-avatar.jpg");
        //client.uploadFile(file);
        //File file = new File("D:\\torr downloads\\50MB.zip");
        //file.createNewFile();
        //client.downloadFile(file, file.getName());
        //client.("def", "defs");

    }
}
