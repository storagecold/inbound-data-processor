import org.springframework.stereotype.Component;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Utils {

    public static String genFileType(File file, String Path) throws NoSuchAlgorithmException {

        String currentDate = "";
        DateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
        currentDate = dateFormat.format(new Date());

        String fileName = file.getName();

        String Key = "";

        if (fileName.toUpperCase().startsWith(Constants.WF1))
            Key = Path + Constants.WF1 + Constants.FILLER + currentDate + Constants.FILLER + fileName;
        else if (fileName.toUpperCase().startsWith(Constants.WF2))
            Key = Path + Constants.WF2 + Constants.FILLER + currentDate + Constants.FILLER + fileName;
        else if (fileName.toUpperCase().startsWith(Constants.HSA))
            Key = Path + Constants.HSA + Constants.FILLER + currentDate + Constants.FILLER + fileName;
        else if (fileName.toUpperCase().endsWith(Constants.GSF))
            Key = Path + Constants.GSF + Constants.FILLER + currentDate + Constants.FILLER + fileName;
        else
            Key = Path + Constants.MnR + Constants.FILLER + currentDate + Constants.FILLER + fileName;

        return Key;
    }

    public static File renameFile(File file) {

        String fromFilePath = file.getAbsolutePath();
        String fileName = file.getName();
        String toFilePath = null;
        boolean fileRenamed = false;
        File oldFile = new File(fromFilePath);

        if (null != fromFilePath && fromFilePath.length() > 0) {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd.HHmmss");
            String currentDate = dateFormat.format(new Date());
            fileName = fileName.replace('.', ',');
            String[] holder = fileName.split(",");
           int index = fromFilePath.lastIndexOf(holder[1]);
           toFilePath = fromFilePath.substring(0,index) + currentDate + Constants.Dot_Ext + holder[1];

            File renamedFile = new File(toFilePath);

            if (renamedFile.exists()) {
                renamedFile.delete();
            }

            if (oldFile.renameTo(renamedFile)) {
                fileRenamed = true;
            }
        }

        if (fileRenamed) {
            File renameFile = new File(toFilePath);
            if (renameFile.exists()) {

                return renameFile;
            }
        }
        return null;
    }

}
