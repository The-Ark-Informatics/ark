package au.org.theark.core.vo;

import java.util.ArrayList;
import java.util.List;


public class ArkUserVO extends BaseVO{

	private static final long serialVersionUID = 1L;
	
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String confirmPassword;
	private String oldPassword;
	private String phoneNumber;
	private List<String> userRoleList;
	private List<String> userModuleList;
	private List<ModuleVO> modules;	//Represents the user associated modules and their roles
	private List<ModuleVO> availableModules;
	private boolean changePassword;
	private String availableRolesLMC;
	private String addAllBtn;
	private String selectedRolesLMC;
	private StudyVO studyVO;

	

	
	private List<ArkUserVO> userList;
	
	
	public StudyVO getStudyVO() {
		return studyVO;
	}

	public void setStudyVO(StudyVO studyVO) {
		this.studyVO = studyVO;
	}

	public String getAvailableRolesLMC() {
		return availableRolesLMC;
	}

	public void setAvailableRolesLMC(String availableRolesLMC) {
		this.availableRolesLMC = availableRolesLMC;
	}

	public String getSelectedRolesLMC() {
		return selectedRolesLMC;
	}

	public void setSelectedRolesLMC(String selectedRolesLMC) {
		this.selectedRolesLMC = selectedRolesLMC;
	}

	public String getAddAllBtn() {
		return addAllBtn;
	}

	public void setAddAllBtn(String addAllBtn) {
		this.addAllBtn = addAllBtn;
	}

	/* A field added to support a control inside the accordion */
	private String txtField;

	public String getTxtField() {
		return txtField;
	}

	public void setTxtField(String txtField) {
		this.txtField = txtField;
	}

	private int mode;
	
	

	public ArkUserVO(){
		super();
		this.studyVO = new StudyVO();
		this.modules = new ArrayList<ModuleVO>();
		this.userRoleList = new ArrayList<String>();
		
	}
	
	public ArkUserVO(String userName, String firstName, String lastName,
			String email, String password, List<ModuleVO> modules, String phoneNumber, String confirmPassword, int mode, String oldPassword, boolean changePassword) {
		super();
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.modules = modules;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.mode = mode;
		this.oldPassword = oldPassword;
		this.changePassword = changePassword;
	}

	
	public boolean isChangePassword() {
		return changePassword;
	}

	public void setChangePassword(boolean changePassword) {
		this.changePassword = changePassword;
	}
	
	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	
	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public List<String> getUserRoleList() {
		return userRoleList;
	}

	public void setUserRoleList(List<String> userRoleList) {
		this.userRoleList = userRoleList;
	}

	public List<String> getUserModuleList() {
		return userModuleList;
	}

	public void setUserModuleList(List<String> userModuleList) {
		this.userModuleList = userModuleList;
	}
	
	public List<ModuleVO> getModules() {
		return modules;
	}
	public void setModules(List<ModuleVO> modules) {
		this.modules = modules;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("\nuserName or cn: ");
		sb.append(userName);
		sb.append("\nfirstName: ");
		sb.append(firstName);
		sb.append("\nemail: ");
		sb.append("\nlastName: ");
		sb.append(lastName);
		sb.append(email);
		sb.append("\n Module");
		sb.append(getModules());
		sb.append("\n Roles");
		sb.append("Phone");
		sb.append(getPhoneNumber());
		sb.append("\n Mode:");
		sb.append(mode);
		
		return sb.toString();
	}

	public List<ArkUserVO> getUserList() {
		return userList;
	}

	public void setUserList(List<ArkUserVO> userList) {
		this.userList = userList;
	}

	public List<ModuleVO> getAvailableModules() {
		return availableModules;
	}

	public void setAvailableModules(List<ModuleVO> availableModules) {
		this.availableModules = availableModules;
	}

	
	
}
