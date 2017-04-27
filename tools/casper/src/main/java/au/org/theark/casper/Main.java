package au.org.theark.casper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;

import org.apache.commons.codec.digest.DigestUtils;



public class Main {
		
	private  static final String ARK_SUBJECT_ATTACHEMENT_DIR="attachments";
	
	private static final String ARK_SUBJECT_CORRESPONDENCE_DIR="correspondence";
	
	private static final String DB_SCHEMA="study";
	
	private static final String SUBJECT_FILE_TABLE="subject_file";

	private static final String CORRESPONDENCE_TABLE="correspondences";
	
	private static final String FILE_ID_COLUMN="FILE_ID";

	private static final String FILE_PAYLOAD_COLUMN="PAYLOAD";

	private static final String ATTACHEMENT_FILE_ID_COLUMN="ATTACHMENT_FILE_ID";
	
	private static final String ATTACHEMENT_CHECKSUM_COLUMN="ATTACHMENT_CHECKSUM";
	
	private static final String ADD_FILE_ID="ALTER TABLE `study`.`subject_file` \n" + 
			"ADD COLUMN `FILE_ID` VARCHAR(1000) NULL";
	
	private static final String CHANGE_PAYLOAD_NULLABLE="ALTER TABLE `study`.`subject_file` \n" + 
			"CHANGE COLUMN `PAYLOAD` `PAYLOAD` LONGBLOB NULL";
	
	private static final String ADD_ATTACHEMENT_FILE_ID="ALTER TABLE `study`.`correspondences` \n" + 
			"ADD COLUMN `ATTACHMENT_FILE_ID` VARCHAR(1000) NULL";

	private static final String ADD_ATTACHEMENT_CHECKSUM="ALTER TABLE `study`.`correspondences` \n" + 
			"ADD COLUMN `ATTACHMENT_CHECKSUM` VARCHAR(50) NULL";
		
	private static final String SELECT_FILE_ATTACHMENT ="select sf.ID, sf.FILENAME, lss.STUDY_ID, lss.SUBJECT_UID,sf.PAYLOAD \n" + 
			"from study.subject_file sf \n" + 
			"	inner join study.link_subject_study lss on lss.ID = sf.LINK_SUBJECT_STUDY_ID \n" + 
			"where sf.id > ? \n" + 
			"	and sf.PAYLOAD is not null \n" + 
			"order by sf.ID limit 1"; 
	
	private static final String UPDATE_FILE_ATTACHMENT = "update study.subject_file sf \n" + 
			"set sf.file_id = ? \n" + 
			"where sf.id = ? ";
	
	private static final String SELECT_CORRESPONDANCE_ATTACHMENT ="select c.ID, c.ATTACHMENT_FILENAME, lss.STUDY_ID, lss.SUBJECT_UID,c.ATTACHMENT_PAYLOAD \n" + 
			"from study.correspondences c \n" + 
			"	inner join study.link_subject_study lss on lss.ID = c.LINK_SUBJECT_STUDY_ID \n" + 
			"where c.id > ? \n" + 
			"	and c.ATTACHMENT_PAYLOAD is not null \n" + 
			"order by c.ID limit 1";
	
	private static final String UPDATE_CORRESPONDANCE_ATTACHMENT="update study.correspondences c \n" + 
			"set c.ATTACHMENT_FILE_ID= ?, \n" + 
			"c.ATTACHMENT_CHECKSUM= ? \n" + 
			"where c.ID = ?";
	
	private static final String CASPER_PROPERTIES_FILE="casper.properties";
	
	private static Logger log = Logger.getLogger(Main.class.getName()) ;
	
	private static String dbUrl = "";
	private static String dbClass = "";
	private static String username = "";
	private static String password = "";
	
	private static String fileAttachmentDir="/tmp";

	public static void main(String[] args) throws Exception {
		Main m = new Main();	
		Connection con =getConnection();
		m.setup(con);
		m.migrateSubjectFiles(con);
		m.migrateCorrespondenceFiles(con);
		closeConnection(con);
	}
	
	public Main() throws Exception{
		Properties prop =new Properties();
		InputStream input = getClass().getClassLoader().getResourceAsStream(Main.CASPER_PROPERTIES_FILE);
		prop.load(input);
		
		Main.dbUrl = prop.getProperty("db.url");
		Main.dbClass = prop.getProperty("db.class");
		Main.username = prop.getProperty("db.username");
		Main.password = prop.getProperty("db.password");
		Main.fileAttachmentDir = prop.getProperty("file.attachment.dir");
		
	}

	private static Connection getConnection() {
		Connection con = null;
		try {
			Class.forName(dbClass);
			con = DriverManager.getConnection(dbUrl, username, password);
			System.out.println("Got Connection.");
		} catch (ClassNotFoundException e) {
			log.severe(e.toString());
		} catch (SQLException e) {
			log.severe(e.toString());
		}
		return con;
	}
	
	private static void closeConnection(Connection con) {
		try{
			con.close();
		}catch(Exception e){
			log.severe(e.toString());
		}
	}

//	public void testConnect() {
//		
//		String query = "Select distinct(table_name) from INFORMATION_SCHEMA.TABLES";
//		
//		try {
//
//			Class.forName(dbClass);
//			Connection connection = DriverManager.getConnection(dbUrl, username, password);
//			Statement statement = connection.createStatement();
//			ResultSet resultSet = statement.executeQuery(query);
//			while (resultSet.next()) {
//				String tableName = resultSet.getString(1);
//				System.out.println("Table name : " + tableName);
//			}
//			connection.close();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	
	
	
//	private void testDBMeta(Connection con)throws Exception{
//		DatabaseMetaData metadata = con.getMetaData();
//		ResultSet resultSet;
//		resultSet = metadata.getTables(Main.DB_SCHEMA, null, Main.SUBJECT_FILE_TABLE, null);
//		while(resultSet.next())
//		    System.out.println(resultSet.getString("TABLE_NAME"));
//	}
	
	
	private void setup(Connection con) throws Exception{
		PreparedStatement ps = null; 
		if(!isColumnExists(con,Main.DB_SCHEMA,Main.SUBJECT_FILE_TABLE,Main.FILE_ID_COLUMN)){
			ps=con.prepareStatement(Main.ADD_FILE_ID);
			ps.execute();
		}
		
		if(!isColumnExistsAndNullable(con,Main.DB_SCHEMA,Main.SUBJECT_FILE_TABLE,Main.FILE_PAYLOAD_COLUMN)){
			ps=con.prepareStatement(Main.CHANGE_PAYLOAD_NULLABLE);
			ps.execute();
		}
		
		if(!isColumnExists(con,Main.DB_SCHEMA,Main.CORRESPONDENCE_TABLE,Main.ATTACHEMENT_FILE_ID_COLUMN)){
			ps=con.prepareStatement(Main.ADD_ATTACHEMENT_FILE_ID);
			ps.execute();
		}
		
		if(!isColumnExists(con,Main.DB_SCHEMA,Main.CORRESPONDENCE_TABLE,Main.ATTACHEMENT_CHECKSUM_COLUMN)){
			ps=con.prepareStatement(Main.ADD_ATTACHEMENT_CHECKSUM);
			ps.execute();
		}
	}
	
	private boolean isColumnExists(Connection con,String schema,String tableName,String columnName) throws Exception{
		boolean isExist = false;
		DatabaseMetaData md = con.getMetaData();
		ResultSet rs = md.getColumns(schema, null, tableName, columnName);
		if (rs.next()) {
		   isExist = true;
		}
		return isExist;
	}
	
	private boolean isColumnExistsAndNullable(Connection con,String schema,String tableName,String columnName) throws Exception{
		boolean isAllowed = false;
		DatabaseMetaData md = con.getMetaData();
		ResultSet rs = md.getColumns(schema, null, tableName, columnName);
		if (rs.next()) {
			isAllowed = rs.getInt(11)==ResultSetMetaData.columnNoNulls?false:true;
		}
		return isAllowed;
	}
	
	
	public void migrateSubjectFiles(Connection con)throws Exception{
		int id = 0;
		int previousId=0;
		PreparedStatement selectPS = con.prepareStatement(Main.SELECT_FILE_ATTACHMENT);
		PreparedStatement updatePS = con.prepareStatement(Main.UPDATE_FILE_ATTACHMENT);
		while(true){
			previousId =id;
			id = migrate(id, selectPS, updatePS, Main.ARK_SUBJECT_ATTACHEMENT_DIR);
			if(previousId != id){
				continue;
			}else{
				break;
			}
		}
	}
	
	public void migrateCorrespondenceFiles(Connection con)throws Exception{
		int id = 0;
		int previousId=0;
		PreparedStatement selectPS = con.prepareStatement(Main.SELECT_CORRESPONDANCE_ATTACHMENT);
		PreparedStatement updatePS = con.prepareStatement(Main.UPDATE_CORRESPONDANCE_ATTACHMENT);
		while(true){
			previousId =id;
			id = migrate(id, selectPS, updatePS, Main.ARK_SUBJECT_CORRESPONDENCE_DIR);
			if(previousId != id){
				continue;
			}else{
				break;
			}
		}
	}

	private int migrate(int id, PreparedStatement selectPS, PreparedStatement updatePS, String baseDir) throws Exception {
		
//		int previousId = id;
		String fileName = null;
		long studyId = 0;
		String subjectUID=null;
		byte[] payload = null;

		String fileId=null;
		
		selectPS.setInt(1, id);
		ResultSet rs = selectPS.executeQuery();

		if (rs.next()) {
			id = rs.getInt(1);
			fileName = rs.getString(2);
			studyId = rs.getInt(3);
			subjectUID = rs.getString(4);
			payload = rs.getBytes(5);
			
			fileId = generateArkFileId(fileName);
			
			saveArkFileAttachment(studyId, subjectUID, baseDir, fileName, payload, fileId);
			
			if(Main.ARK_SUBJECT_ATTACHEMENT_DIR.equals(baseDir)){
				updatePS.setString(1, fileId);
				updatePS.setInt(2, id);
				updatePS.executeUpdate();
			}else{
				String checksum= DigestUtils.md5Hex(new ByteArrayInputStream(payload)).toUpperCase();				
				updatePS.setString(1, fileId);
				updatePS.setString(2, checksum);
				updatePS.setInt(3, id);
				updatePS.executeUpdate();
			}
			
			//Clear payload 
			payload = null;
			
		}
//		if(previousId != id){
//			migrate(id, selectPS, updatePS,baseDir);
//		}
		return id;
	}
	
	private void saveArkFileAttachment(final long studyId, final String subjectUID, final String directoryType, final String fileName, final byte[] payload, final String fileId) {

		String directoryName = getArkFileDirName(studyId, subjectUID, directoryType);
		log.info("about to output to " + directoryName); 
		File fileDir = new File(directoryName);

		if (!fileDir.exists()) {
			boolean result = false;
			try {
				fileDir.mkdirs();
				result = true;
			}
			catch (SecurityException se) {
				log.severe("Do not have the sufficient permission to access the file directory");
			}finally{
				fileDir.exists();
			}
			if (result) {
				//log.info("DIR created successfully " + directoryName);
			}
			//
		}
		createFile(directoryName, fileId, payload);
	}
	
	private void createFile(final String directory, final String fileId, final byte[] payload) {
		try {
			File file = new File(directory + File.separator + fileId);
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(payload);
			fos.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	private String getArkFileDirName(final Long studyId, final String subjectUID, final String directoryType) {
		String directoryName = this.fileAttachmentDir + File.separator + studyId + java.io.File.separator + directoryType +  File.separator + subjectUID ;
		return directoryName;
	}
	
	private String generateArkFileId(String fileName) {
		return System.currentTimeMillis() + "_" + UUID.randomUUID() + "_" + (fileName != null ? fileName.replaceAll("\\s", "_"):null);
	}
	
}
