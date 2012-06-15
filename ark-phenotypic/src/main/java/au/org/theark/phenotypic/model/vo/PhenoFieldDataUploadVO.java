package au.org.theark.phenotypic.model.vo;

import java.io.File;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.vo.UploadVO;

public class PhenoFieldDataUploadVO extends UploadVO {

	private static final long	serialVersionUID	= -3761087990900803427L;

	private File tempFile = null;
	private CustomFieldGroup questionnaire;	// store the Questionnaire selected from drop-down
	private ArkFunction arkFunction;
	private boolean overrideDataValidationChkBox;
	private String fileName;
	private String	uploadType;

	public File getTempFile() {
		return tempFile;
	}

	public void setTempFile(File tempFile) {
		this.tempFile = tempFile;
	}

	public CustomFieldGroup getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(CustomFieldGroup questionnaire) {
		this.questionnaire = questionnaire;
	}

	public ArkFunction getArkFunction() {
		return arkFunction;
	}
	
	public void setArkFunction(ArkFunction arkFunction) {
		this.arkFunction = arkFunction;
	}

	public boolean isOverrideDataValidationChkBox() {
		return overrideDataValidationChkBox;
	}

	public void setOverrideDataValidationChkBox(boolean overrideDataValidationChkBox) {
		this.overrideDataValidationChkBox = overrideDataValidationChkBox;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUploadType() {
		return uploadType;
	}

	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}

}
