import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesUtil {

    @Value("${bucketName}")
    @Getter
    public String bucketName;

    @Value("${objectFolder}")
    @Getter
    public String objectFolder;

    @Value("${EOEInbound}")
    @Getter
    public String eoeInbound;

    @Getter
    @Value("${EOEArchive}")
    public String eoeArchive;

    @Getter
    @Value("${EOEError}")
    public String eoeError;

    @Getter
    @Value("${eoeSTOP_FILE}")
    private String eoeStopfile;

    public String getEoeStopfile() {
        return eoeStopfile;
    }

    public void setEoeStopfile(String eoeStopfile) {
        this.eoeStopfile = eoeStopfile;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public void setObjectFolder(String objectFolder) {
        this.objectFolder = objectFolder;
    }

    public String getObjectFolder() {
        return objectFolder;
    }
    
    public String getEoeInbound() {
        return eoeInbound;
    }

    public void setEoeInbound(String eoeInbound) {
        this.eoeInbound = eoeInbound;
    }

    public String getEoeArchive() {
        return eoeArchive;
    }

    public void setEoeArchive(String eoeArchive) {
        this.eoeArchive = eoeArchive;
    }

    public String getEoeError() {
        return eoeError;
    }

    public void setEoeError(String eoeError) {
        this.eoeError = eoeError;
    }
}
