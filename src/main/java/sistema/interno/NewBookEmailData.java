package sistema.interno;

import java.util.List;

public class NewBookEmailData {

	private String authorName;
	private String title;
	private List<String> userEmails;
	
	public NewBookEmailData(String authorName, String title, List<String> userEmails) {
		this.authorName = authorName;
		this.title = title;
		this.userEmails = userEmails;
	}

	public String getAuthorName() {
		return authorName;
	}

	public String getTitle() {
		return title;
	}

	public List<String> getUserEmails() {
		return userEmails;
	}
	
	
	
}
