package au.org.theark;

import javax.naming.InvalidNameException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import au.org.theark.dao.ArkUserDao;
import au.org.theark.vo.ArkUserVO;

/**
 * A Stand alone java application that can be used for the purpose of creating an Ark User in LDAP . It does not create the user in the
 * database(Ark_User and Ark_User_Role), after running this the administrator should create the entries manually. It can be further enhanced to
 * connect to the back end and automate however keeping the scope very limited.
 * 
 * @author nivedann
 * 
 */
public class GeneratePassword {

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
				System.out.println("\n -- The ark user was successfully created in LDAP. ");
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
}
