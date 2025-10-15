package com.datamelt.dataprotection.utility;

public class FileUtility
{
    private static final String PATH_SEPERATOR = "/";

    public static String getFilePathAndName(String filePath, String fileName)
    {
        if(filePath.endsWith(PATH_SEPERATOR))
        {
            return filePath + fileName;
        }
        else
        {
            return filePath+ PATH_SEPERATOR + fileName;
        }
    }
}
