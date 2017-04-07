package FTPLibrary;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by aurim on 2017-04-02.
 */
public class FTPFile
{
    public String getName()
    {
        return name;
    }

    public long getSize()
    {
        return size;
    }

    public LocalDateTime getModifyDate()
    {
        return modifyDate;
    }

    public boolean isDir()
    {
        return isDir;
    }

    public String getPermissions()
    {
        return permissions;
    }

    private String name;
    private long size;
    private LocalDateTime modifyDate;
    private boolean isDir;
    String permissions;
    public FTPFile(String name, long size, LocalDateTime modifyDate, boolean isDir, String permissions)
    {
        this.name = name;
        this.size = size;
        this.modifyDate = modifyDate;
        this.isDir = isDir;
        this.permissions = permissions;
    }
}
