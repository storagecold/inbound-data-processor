import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.optum.eligibility.file.reader.model.Submitters;
import com.optum.eligibility.file.reader.mongo.repository.SubmittersRepository;
import com.optum.eligibility.file.reader.utils.PropertiesUtil;
import com.optum.eligibility.file.reader.utils.SubmittersUtil;


@Component
@Slf4j
public class ProcessFileStep {
	
	private static final String ALL = "ALL";
	private static final String NONE = "NONE";
	private static final String PRIORITY = "PRIORITY";
	private static final String NORMAL = "NORMAL";

	@Autowired
	SubmittersUtil submittersUtil;

	@Autowired
	PropertiesUtil propertiesUtil;
	
	File file;
	String fileSubmitterId;
	String fileName = "";
	
	public void setFile(File f) {
		file  = f;
		
		if (null != file) {
			// The part of the file name before the first occurrence of dot character is the
			// Submitter ID, for example, if the file name is: STTEXF.U.20190731.gsf.trig,
			// then STTEXF is the Submitter ID
			fileSubmitterId = file.getName().substring(0, file.getName().indexOf('.'));
			fileName = file.getName().replace(propertiesUtil.getTrigFileExtension(), "");
		}
	}
	
	public ProcessFileStep() {
	}
	
	public boolean canProcessFile() {
		
		if (isProcessFileAll()) {
			return true;
		}
		if (isProcessFilePriority()) {
			return true;
		}
		if (isProcessFileNormal()) {
			return true;
		}
		
		// Return FALSE because the last option is NONE which
		// means that this application shouldn't process any files
		return false;
		
	}
	
	private boolean isProcessFileNormal() {

		if (propertiesUtil.getProcessFiles().equalsIgnoreCase(NORMAL)) {
			if (!isMarketDataFile() && isNormalFile()) {
				return true;
			}
		}
		return false;
	}

	public boolean isNormalFile() {
		return !(submittersUtil.isPrioritySubmitter(fileSubmitterId));
	}

	public boolean isMarketDataFile() {
		return propertiesUtil.getMarketDataFileNames().contains(fileName);
	}

	public boolean isDHPFile() {
		return fileName.substring(0, fileName.length()-4).matches(propertiesUtil.getVbdNamePattern());
	}

	public boolean isOOPMaxFile() {
		return fileName.substring(0, fileName.length()-4).matches(propertiesUtil.getOopmaxNamePattern());
	}

	private boolean isProcessFilePriority() {
		if (propertiesUtil.getProcessFiles().equalsIgnoreCase(PRIORITY)) {
			if (isMarketDataFile() || isPriorityFile()) {
				return true;
			}
		}
		return false;
	}

	public boolean isPriorityFile() {
		return submittersUtil.isPrioritySubmitter(fileSubmitterId);
	}

	public boolean isProcessFileAll() {
		if (propertiesUtil.getProcessFiles().equalsIgnoreCase(ALL)) {
			return true;
		}
		return false;
	}
	
	
	public boolean isProcessFileNone() {
		if (propertiesUtil.getProcessFiles().equalsIgnoreCase(NONE)) {
			return true;
		}
		return false;
	}
}
