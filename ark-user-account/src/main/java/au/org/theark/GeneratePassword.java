package au.org.theark;

import javax.naming.InvalidNameException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import au.org.theark.dao.ArkUserDao;
import au.org.theark.vo.ArkUserVO;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * A Stand alone java application that can be used for the purpose of creating an Ark User in LDAP . It does not create the user in the
 * database(Ark_User and Ark_User_Role), after running this the administrator should create the entries manually. It can be further enhanced to
 * connect to the back end and automate however keeping the scope very limited.
 * 
 * @author nivedann
 * 
 */
public class GeneratePassword {

	private static String SQL_FILENAME = "initialArkUser.sql";
	
	
	public static void main(String[] args) {

		ArkUserVO arkUserVO = new ArkUserVO();

		GeneratePassword generatePassword = new GeneratePassword();

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		ArkUserDao arkUserDAO = (ArkUserDao) context.getBeanFactory().getBean("arkUserDao");

		generatePassword.init(args, arkUserVO, context, arkUserDAO);

		// If all went well create the user in LDAP
		if (context != null && arkUserDAO != null) {
			try {
				arkUserDAO.createArkUser(arkUserVO);
				System.out.println("\n -- The ark user was successfully created in LDAP.");
				generatePassword.writeSqlScript(arkUserVO);
				System.out.println("\n -- Matching SQL script written to " + SQL_FILENAME + ".");
			}
			catch (InvalidNameException e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Could not create account. Context  or the DAO was not loaded");
		}
	}

	/**
	 * 
	 * @param args
	 * @param arkUserVO
	 * @param context
	 * @param arkUserDAO
	 */
	public void init(String args[], ArkUserVO arkUserVO, ClassPathXmlApplicationContext context, ArkUserDao arkUserDAO) {

		if (args.length == 4) {
			arkUserVO.setArkUserId(args[0]);
			arkUserVO.setPassword(args[1]);
			arkUserVO.setFirstName(args[2]);
			arkUserVO.setLastName(args[3]);
		}
		else {
			System.out.println("Please Enter the details again. Incorrect format.");
			System.out.println("\n Example: java -jar passgen.jar somename@ark.org.au mYPass90word Jhonson Tang");
		}

	}
	
	// Function to write an SQL script that creates the initial Ark user in the database backend.
	// The username of this user matches the username entered into LDAP.
	private void writeSqlScript(ArkUserVO arkUserVO) {
	
		try
		{
			// open the script file, overwriting any previous file
			PrintStream outputStream = new PrintStream(new FileOutputStream(SQL_FILENAME));
			// output the script's SQL
			outputStream.println("-- This script will create in the database a super user account for " + 
									arkUserVO.getArkUserId() + 
									" (" + arkUserVO.getFirstName() + " " + arkUserVO.getLastName() + ")");
			outputStream.println("USE study;");
			outputStream.println("-- NOTE: You must first setup all the database schema with appropriate patches before importing this.");
			outputStream.println("-- Insert first Super User as a valid account");
			outputStream.println("INSERT INTO `study`.`ark_user` (`ID`, `LDAP_USER_NAME`) VALUES (1, '" + arkUserVO.getArkUserId() + "');");
			outputStream.println("-- Set up the permissions for the first Super User (ark_role_id = 1)");
			outputStream.println("INSERT INTO `study`.`ark_user_role` (ID,ARK_USER_ID,ARK_ROLE_ID,ARK_MODULE_ID,STUDY_ID) VALUES (1,1,1,1,NULL);");
			outputStream.println("INSERT INTO `study`.`ark_user_role` (ID,ARK_USER_ID,ARK_ROLE_ID,ARK_MODULE_ID,STUDY_ID) VALUES (2,1,1,2,NULL);");
			outputStream.println("INSERT INTO `study`.`ark_user_role` (ID,ARK_USER_ID,ARK_ROLE_ID,ARK_MODULE_ID,STUDY_ID) VALUES (3,1,1,3,NULL);");
			outputStream.println("INSERT INTO `study`.`ark_user_role` (ID,ARK_USER_ID,ARK_ROLE_ID,ARK_MODULE_ID,STUDY_ID) VALUES (4,1,1,4,NULL);");
			outputStream.println("INSERT INTO `study`.`ark_user_role` (ID,ARK_USER_ID,ARK_ROLE_ID,ARK_MODULE_ID,STUDY_ID) VALUES (5,1,1,5,NULL);");				
			outputStream.println("-- NB: ark_module_id = 6 (Reporting) omitted, because reporting relies on permissions defined in other modules.");
			outputStream.println("INSERT INTO `study`.`ark_user_role` (ID,ARK_USER_ID,ARK_ROLE_ID,ARK_MODULE_ID,STUDY_ID) VALUES (7,1,1,7,NULL);");
			// script complete, close the file
			outputStream.close();
		}
		catch(Exception ex)
		{
			System.out.println("Error writing matching SQL script " + SQL_FILENAME + " for user " + arkUserVO.getArkUserId() + ".");
		}
	}

}
