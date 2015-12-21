package au.org.theark.study.web.component.manageuser.form;

import java.io.Serializable;

/**
 * This is used to implement the pop up yes no panel
 */
public class ConfirmationAnswer implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean answer;

	public ConfirmationAnswer(boolean answer) {
		this.answer = answer;
	}

	public boolean isAnswer() {
		return answer;
	}

	public void setAnswer(boolean answer) {
		this.answer = answer;
	}
}