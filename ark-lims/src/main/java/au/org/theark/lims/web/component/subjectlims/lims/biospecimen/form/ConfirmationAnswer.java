package au.org.theark.lims.web.component.subjectlims.lims.biospecimen.form;

import java.io.Serializable;

public class ConfirmationAnswer implements Serializable {
	 
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