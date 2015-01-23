package au.org.spark.service;

import java.util.List;

public interface SshService {
	
	public List<String> listFiles(String directory) throws Exception;
}
