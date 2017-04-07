package FTPLibrary;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

/**
 * Created by aurim on 2017-03-27.
 */
public class FTPConnector
{
    public Socket connectCommunicationSocket(String hostname, int port) throws IOException
    {
        return connectCommunicationSocket(new InetSocketAddress(hostname, port));
    }
    public Socket connectCommunicationSocket(InetSocketAddress socketAddress) throws IOException
    {
        Socket socket = new Socket();
        socket.setKeepAlive(true);
        socket.connect(socketAddress, 10000);
        return socket;
    }
    public Socket connectDataTransferSocket(String hostname, int port) throws IOException
    {
        return connectDataTransferSocket(new InetSocketAddress(hostname, port));
    }
    public Socket connectDataTransferSocket(InetSocketAddress socketAddress) throws IOException
    {
        Socket socket = new Socket();
        socket.connect(socketAddress, 10000);
        return socket;
    }
}
