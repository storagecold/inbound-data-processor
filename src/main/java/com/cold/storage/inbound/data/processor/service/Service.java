import java.io.File;

public interface Service {
    boolean copyFile(File file, String dest);

    File getTrigFile(File srcFile);

    File getDataFile(File trigFile);

    boolean moveFile(File file, String dest);

    String getFileType(File file);

    String getLob(File file) throws Exception;

    void moveIncorrectFileToErrorDirectory(File file) ;

    String getSubmitterIdFromFile(File file) throws Exception;

    String getSecurityPath(String eligSysCd, String mktSegCd, String locationCd, String policyNumber);

    boolean moveFileToArchiveDirectory(File file);
}
