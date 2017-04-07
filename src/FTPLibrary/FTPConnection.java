package FTPLibrary;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by aurim on 2017-03-27.
 */
public class FTPConnection
{
    private Socket socket = null;

    public FTPConnection()
    {
        socket = new Socket();
    }
    public void connect(InetSocketAddress socketAddress) throws IOException
    {
        connect(socketAddress.getHostName(), socketAddress.getPort());
    }
    public void connect(String hostname, int port) throws IOException
    {
        socket = new Socket(hostname, port);
    }
    public Socket getSocket()
    {
        return socket;
    }
    public void close() throws IOException
    {
        socket.close();
    }
}
