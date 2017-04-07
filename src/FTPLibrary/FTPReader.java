package FTPLibrary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by aurim on 2017-03-25.
 */
public class FTPReader
{
    private BufferedReader reader;
    private InputStream stream;

    public FTPReader(InputStream stream)
    {
        this.stream = stream;
        reader = new BufferedReader(new InputStreamReader(stream));
    }

    public String readLine() throws IOException
    {
        return reader.readLine();
    }

    public void close() throws IOException
    {
        reader.close();
    }
}
