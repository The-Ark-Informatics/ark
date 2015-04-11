package au.org.spark.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.sftp.session.SftpSession;
import org.springframework.stereotype.Service;

import au.org.spark.web.view.DataSourceVo;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;

@Service
public class SshServiceImpl implements SshService {

	private static final String ROOT_PATH = "/home/smaddumarach/";

	@Autowired
	SftpSession session;

	@Override
	public List<String> listFiles(String directory) throws Exception {
		ChannelSftp channelSftp = session.getClientInstance();

		List<String> files = new ArrayList<String>();
		channelSftp.cd(directory);
		List filelist = channelSftp.ls(directory);
		for (int i = 0; i < filelist.size(); i++) {
			// System.out.println(filelist.get(i).toString());
			files.add(filelist.get(i).toString());
		}
		return files;
	}

	public List<DataSourceVo> listFilesAndDirectories(String directory, String fileName) throws Exception {
		ChannelSftp channelSftp = session.getClientInstance();
		List<DataSourceVo> files = new ArrayList<DataSourceVo>();

		Vector<String> filest = null;

		if (directory == null) {
			channelSftp.cd(ROOT_PATH);
			filest = channelSftp.ls(ROOT_PATH);
		} else {
			channelSftp.cd(ROOT_PATH + directory);
			filest = channelSftp.ls(ROOT_PATH + directory);
		}

		for (int i = 0; i < filest.size(); i++) {
			Object obj = filest.elementAt(i);
			if (obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry) {
				LsEntry entry = (LsEntry) obj;

				if (true && !entry.getAttrs().isDir()) {
					files.add(new DataSourceVo(entry.getFilename(), "No", entry.getLongname(), "Offline"));
				}
				if (true && entry.getAttrs().isDir()) {
					if (!entry.getFilename().equals(".") && !entry.getFilename().equals("..")) {
						files.add(new DataSourceVo(entry.getFilename(), "Yes", entry.getLongname(), "Offline"));
					}
				}
			}
		}

		if (fileName != null && fileName.trim().length() > 0) {

			for (int i = 0; i < files.size(); ++i) {
				DataSourceVo source = files.get(i);
				if (!source.getFileName().toLowerCase().contains(fileName.toLowerCase())) {
					files.remove(i);
					--i;
				}
			}
		}

		return files;
	}

}
