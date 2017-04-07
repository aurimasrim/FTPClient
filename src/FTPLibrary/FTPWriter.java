package FTPLibrary;

import java.io.*;

/**
 * Created by aurim on 2017-03-25.
 */
public class FTPWriter
{
    private BufferedWriter writer;

    public FTPWriter(OutputStream stream)
    {
        writer = new BufferedWriter(new OutputStreamWriter(stream));
    }
    public void writeLine(String line) throws IOException
    {
        writer.write(line);
        writer.write("\r\n");
        writer.flush();
    }
}
