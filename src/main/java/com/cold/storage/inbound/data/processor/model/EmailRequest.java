import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EmailRequest implements Serializable {

	private String fileName;

	private String subject;

	private String message;
	
	private String lob;
	
	private List<String> toEmails;
	
	private List<String> ccEmails;
	
}
