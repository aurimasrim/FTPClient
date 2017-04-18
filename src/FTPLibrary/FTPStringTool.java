package FTPLibrary;

import com.sun.xml.internal.ws.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by aurim on 2017-03-27.
 */
public class FTPStringTool
{
    public InetSocketAddress extractSocketAddress(FTPReply reply)
    {
        String line = reply.getMessages()[0];
        String[] numbers = line.substring(line.indexOf("(") + 1, line.indexOf(")")).split(",");
        StringBuffer ipAddress = new StringBuffer();
        ipAddress.append(numbers[0]).append(".").append(numbers[1]).append(".").append(numbers[2]).append(".").append(numbers[3]);
        int port = Integer.parseInt(numbers[4]) * 256 + Integer.parseInt(numbers[5]);
        return new InetSocketAddress(ipAddress.toString(), port);
    }
    public FTPFile extractFTPFile(String line)
    {
        String[] tempArray = line.split(";");
        String name = null;
        long size = 0;
        boolean isDir = false;
        LocalDateTime modifyDate = null;
        String permissions = null;
        for(String i : tempArray)
        {
            if (i.contains("size"))
            {
                size = Integer.parseInt(i.substring(5));
            }
            else if (i.contains("type"))
            {
                if (i.contains("dir")) isDir = true;
                else  isDir = false;
            }
            else if (i.contains("modify"))
            {
                String sDate = i.substring(7);
                int year = Integer.parseInt(sDate.substring(0, 4));
                int month = Integer.parseInt(sDate.substring(4, 6));
                int day = Integer.parseInt(sDate.substring(6, 8));
                int hour = Integer.parseInt(sDate.substring(8, 10));
                int minutes = Integer.parseInt(sDate.substring(10, 12));
                int seconds = Integer.parseInt(sDate.substring(12));
                modifyDate = LocalDateTime.of(year, month, day, hour, minutes, seconds);
            }
            else if (i.contains("perm"))
            {
                permissions = i.substring(5);
            }
            else
            {
                name = i.substring(1);
            }
        }
        FTPFile file = new FTPFile(name, size, modifyDate, isDir, permissions);
        //System.out.println(file.getName());
        return file;
    }
}
