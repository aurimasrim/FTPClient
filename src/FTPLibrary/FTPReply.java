package FTPLibrary;

/**
 * Created by aurim on 2017-03-25.
 */
public class FTPReply
{
    private int code;
    private String[] messages;

    public FTPReply(int code, String[] messages)
    {
        this.code = code;
        this.messages = messages;
    }
    public boolean isSuccess()
    {
        return code >= 200 && code < 300;
    }

    public boolean isReady()
    {
        return (code == 150)||(code == 350);
    }
    public int getCode()
    {
        return code;
    }
    public String[] getMessages()
    {
        return messages;
    }
}
