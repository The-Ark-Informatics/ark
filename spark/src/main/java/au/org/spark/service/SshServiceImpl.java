package au.org.spark.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.sftp.session.SftpSession;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.ChannelSftp;

@Service
public class SshServiceImpl implements SshService {

	@Autowired
	SftpSession session;
	
	@Override
	public List<String> listFiles(String directory) throws Exception {
		ChannelSftp channelSftp= session.getClientInstance();
		
		List<String> files = new ArrayList<String>(); 
        channelSftp.cd(directory);
        List filelist = channelSftp.ls(directory);
        for(int i=0; i<filelist.size();i++){
//            System.out.println(filelist.get(i).toString());
        	files.add(filelist.get(i).toString());
        }
		return files;
	}

}
