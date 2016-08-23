package au.org.spark.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Pattern;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.molgenis.genotype.Alleles;
import org.molgenis.genotype.Sample;
import org.molgenis.genotype.variant.GeneticVariant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.sftp.session.SftpSession;
import org.springframework.stereotype.Service;

import au.org.spark.factory.DefaultExecSessionFactory;
import au.org.spark.factory.ExecSession;
import au.org.spark.factory.ExecSessionWrapper;
import au.org.spark.parser.SparkSftpBedBimFamGenotypeData;
import au.org.spark.parser.SparkSftpPedMapGenotypeData;
import au.org.spark.util.Constants;
import au.org.spark.web.view.AnalysisVo;
import au.org.spark.web.view.ComputationVo;
import au.org.spark.web.view.DataCenterVo;
import au.org.spark.web.view.DataSourceVo;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.ChannelSftp.LsEntry;

@Service
public class SshServiceImpl implements SshService {

	@Value("${ssh.root.path}")
	private String rootPath;

	private static Pattern pattern = Pattern.compile("\\s");

	@Autowired
	SftpSession sftpSession;

	@Autowired
	CassandraService cassandraService;

	// @Autowired
	// ExecSession execSession;

	@Autowired
	DefaultExecSessionFactory execSessionFactory;

	@Override
	public List<String> listFiles(String directory) throws Exception {
		ChannelSftp channelSftp = sftpSession.getClientInstance();

		List<String> files = new ArrayList<String>();
		channelSftp.cd(directory);
		List filelist = channelSftp.ls(directory);
		for (int i = 0; i < filelist.size(); i++) {
			files.add(filelist.get(i).toString());
		}
		return files;
	}

	public List<DataSourceVo> listFilesAndDirectories(String directory, String fileName) throws Exception {
		ChannelSftp channelSftp = sftpSession.getClientInstance();
		List<DataSourceVo> files = new ArrayList<DataSourceVo>();

		Vector<String> filest = null;

		if (directory == null) {
			channelSftp.cd(rootPath);
			filest = channelSftp.ls(rootPath);
		} else {
			channelSftp.cd(rootPath + directory);
			filest = channelSftp.ls(rootPath + directory);
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

	public void readFile(String directory, String fileName) throws Exception {
		ChannelSftp channelSftp = sftpSession.getClientInstance();

		if (directory == null) {
			channelSftp.cd(rootPath);
		} else {
			channelSftp.cd(rootPath + directory);
		}

		InputStream stream = channelSftp.get(fileName);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} finally {
			stream.close();
		}

	}

	public void processMapFile(DataSourceVo dataSource) throws Exception {
		ChannelSftp channelSftp = sftpSession.getClientInstance();

		String directory = dataSource.getDirectoryName();

		String fileName = dataSource.getFileName();

		if (directory == null) {
			channelSftp.cd(rootPath);
		} else {
			channelSftp.cd(rootPath + directory);
		}

		InputStream stream = channelSftp.get(fileName);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			String line = null;
			String mapValues[] = null;
			int rowId = 0;
			while ((line = br.readLine()) != null) {
				mapValues = pattern.split(line);
				cassandraService.insertMap(Constants.dataSourceTableName(dataSource), ++rowId, Integer.parseInt(mapValues[0]), mapValues[1], Double.parseDouble(mapValues[2]), Integer.parseInt(mapValues[3]));
			}
		} finally {
			stream.close();
		}

	}

	public void processPlinkDataSource(DataCenterVo dataCenter) throws Exception {
		ChannelSftp channelSftp = sftpSession.getClientInstance();

		String directory = dataCenter.getDirectory();

		if (directory == null) {
			channelSftp.cd(rootPath);
		} else {
			channelSftp.cd(rootPath + directory);
		}

		String mapFile = null;

		String pedFile = null;

		String bedFile = null;

		String bimFile = null;

		String famFile = null;

		Vector<String> filest = null;

		filest = channelSftp.ls(".");

		for (int i = 0; i < filest.size(); i++) {
			Object obj = filest.elementAt(i);
			if (obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry) {
				LsEntry entry = (LsEntry) obj;

				if (true && !entry.getAttrs().isDir()) {
					String fileName = entry.getFilename();
					String[] extList = fileName.split("\\.");
					if (extList.length > 1) {
						String extension = extList[1];
						if ("map".equalsIgnoreCase(extension)) {
							mapFile = fileName;
						} else if ("ped".equalsIgnoreCase(extension)) {
							pedFile = fileName;
						} else if ("bed".equalsIgnoreCase(extension)) {
							bedFile = fileName;
						} else if ("bim".equalsIgnoreCase(extension)) {
							bimFile = fileName;
						} else if ("fam".equalsIgnoreCase(extension)) {
							famFile = fileName;
						}
					}
				}

			}
		}

		if (mapFile != null && pedFile != null) {

			SparkSftpPedMapGenotypeData genotypeData = null;

			try {
				genotypeData = new SparkSftpPedMapGenotypeData(pedFile, mapFile, channelSftp);
			} catch (IOException ex) {
				ex.printStackTrace();
			}

			int id = 0;
			for (GeneticVariant variant : genotypeData) {

				String snp = variant.getVariantId().getPrimairyId();
				String chromosome = variant.getSequenceName();

				List<Alleles> alleles = genotypeData.getSampleVariants(variant);

				List<String> alleleList = new ArrayList<String>();
				for (Alleles allele : alleles) {
					alleleList.addAll(allele.getAllelesAsString());
				}

				cassandraService.insertPlinkGenomicData(dataCenter, ++id, snp, chromosome, alleleList);
			}

			for (Sample sample : genotypeData.getSamples()) {
				cassandraService.insertPlinkSampleData(dataCenter, sample);
			}
		} else if (bedFile != null && bimFile != null && famFile != null) {
			SparkSftpBedBimFamGenotypeData genotypeData = null;
			genotypeData = new SparkSftpBedBimFamGenotypeData(channelSftp, bimFile, famFile, 100);
			int id = 0;
			for (GeneticVariant variant : genotypeData) {
				String snp = variant.getVariantId().getPrimairyId();
				String chromosome = variant.getSequenceName();

				List<Alleles> alleles = genotypeData.getSampleVariants(variant);
				List<String> alleleList = new ArrayList<String>();

				for (Alleles allele : alleles) {
					alleleList.addAll(allele.getAllelesAsString());
				}
				cassandraService.insertPlinkGenomicData(dataCenter, ++id, snp, chromosome, alleleList);
				System.out.println();
			}

			for (Sample sample : genotypeData.getSamples()) {
				cassandraService.insertPlinkSampleData(dataCenter, sample);
			}
		}
	}

	public void uploadProgram(String destinationDir, String name) {

		try {
			String path = destinationDir + File.separator + name;
			// List<String> fileNames = new ArrayList<String>();

			ChannelSftp channelSftp = sftpSession.getClientInstance();
			System.out.println("Print PWD :" + channelSftp.pwd());
			channelSftp.cd(rootPath + "spark");
			channelSftp.mkdir(name);
			channelSftp.cd(name);

			this.createFilesAndDirectories(new File(path).toPath(), channelSftp);

			// Clear the temporary directory
			// channelSftp.rm(rootPath+"spark/"+name);

			// for(String s:fileNames){
			// System.out.println(s);
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createFilesAndDirectories(Path dir, ChannelSftp channel) {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
			for (Path path : stream) {
				if (path.toFile().isDirectory()) {
					String dirName = path.getFileName().toString();
					channel.mkdir(dirName);
					channel.cd(dirName);
					createFilesAndDirectories(path, channel);
				} else {
					channel.put(new FileInputStream(path.toFile()), path.getFileName().toString());
				}
			}
			channel.cd("..");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String compileProgram(ComputationVo computationVo) {

		Map<String, String> map = new HashMap<String, String>();
		ExecSession execSession = null;
		ChannelExec channelExec = null;
		InputStream in = null;
		BufferedReader reader = null;
		String command = null;

		StringBuffer results = new StringBuffer();

		try {
			execSession = execSessionFactory.getSession();
			channelExec = execSession.getClientInstance();
			in = channelExec.getInputStream();

			String path = "spark/" + computationVo.getProgramId() + "/" + computationVo.getProgram();

			command = "cat " + path + "/spark.info";

			System.out.println("Command: " + command);
			channelExec.setCommand(command);
			channelExec.connect();

			reader = new BufferedReader(new InputStreamReader(in));
			String line;
			int index = 0;

			while ((line = reader.readLine()) != null) {
				System.out.println(++index + " : " + line);
				String array[] = line.split("[:]");
				if (array.length > 1) {
					map.put(array[0], array[1]);
				}
			}

			int exitStatus = channelExec.getExitStatus();

			if (exitStatus < 0) {
				System.out.println("Done, but exit status not set!");
			} else if (exitStatus > 0) {
				System.out.println("Done, but with error!");
			} else {
				System.out.println("Done!");
			}

			if (!Constants.SPARK_INFO_AUTOMATIC_MODE.equalsIgnoreCase(map.get(Constants.SPARK_INFO_MODE))) {

				execSession = execSessionFactory.getSession();
				channelExec = execSession.getClientInstance();
				in = channelExec.getInputStream();

				String output = map.get(Constants.SPARK_INFO_MAKEFILE).split("[.]")[0];

				command = "mkdir " + path + "/program/out; " + appendCheckStatement(map.get(Constants.SPARK_INFO_MAKETYPE) + " " + path + "/program/" + map.get(Constants.SPARK_INFO_MAKEFILE) + " -o " + path + "/program/out/" + output + ";");

				System.out.println("Command: " + command);

				channelExec.setCommand(command);
				channelExec.connect();

				reader = new BufferedReader(new InputStreamReader(in));

				while ((line = reader.readLine()) != null) {
					System.out.println(line);
					results.append(line);
				}

				exitStatus = channelExec.getExitStatus();

				if (exitStatus < 0) {
					System.out.println("Done, but exit status not set!");
				} else if (exitStatus > 0) {
					System.out.println("Done, but with error!");
				} else {
					System.out.println("Done!");
				}

			}

		} catch (Exception e) {
			System.err.println("Error: " + e);
			return "System Error";
		}
		return results.toString();
	}

	private String appendCheckStatement(String command) {
		return "if " + command + " then echo 'Compiled'; else echo 'Compile Failure'; fi;";
	}

	public void executeAnalysis(AnalysisVo analysisVo) {
		Map<String, String> map = new HashMap<String, String>();
		ExecSession execSession = null;
		ChannelExec channelExec = null;
		InputStream in = null;
		BufferedReader reader = null;
		String command = null;

		StringBuffer results = new StringBuffer();

		try {

			// read spark info and store the program instructions
			execSession = execSessionFactory.getSession();
			channelExec = execSession.getClientInstance();
			in = channelExec.getInputStream();

			String path = "spark/" + analysisVo.getProgramId() + "/" + analysisVo.getProgramName();

			command = "cat " + path + "/spark.info";

			System.out.println("Command: " + command);
			channelExec.setCommand(command);
			channelExec.connect();

			reader = new BufferedReader(new InputStreamReader(in));
			String line;
			int index = 0;

			while ((line = reader.readLine()) != null) {
				System.out.println(++index + " : " + line);
				String array[] = line.split("[:]");
				if (array.length > 1) {
					map.put(array[0], array[1]);
				}
			}

			int exitStatus = channelExec.getExitStatus();

			if (exitStatus < 0) {
				System.out.println("Done, but exit status not set!");
			} else if (exitStatus > 0) {
				System.out.println("Done, but with error!");
			} else {
				System.out.println("Done!");
			}

			String resultDir = null;
			// Move the data set to the output directory.
			if (Constants.DATA_CENTERS.SSH_TEST.toString().equals(analysisVo.getSourceDataCenter())) {
				ChannelSftp channelSftp = sftpSession.getClientInstance();
				channelSftp.cd(rootPath + "spark");
				channelSftp.cd(analysisVo.getProgramId() + "/" + analysisVo.getProgramName() + "/program");
				resultDir = "result" + analysisVo.getAnalysisId();
				channelSftp.mkdir(resultDir);
				channelSftp.cd(resultDir);

				execSession = execSessionFactory.getSession();
				channelExec = execSession.getClientInstance();
				String source = rootPath + analysisVo.getSourcePath();

				String destination = path + "/program" + "/" + resultDir;

				if (BooleanUtils.isTrue(analysisVo.isSourceDir())) {
					command = "cp -dr " + source + " " + destination;
					System.out.println("Command: " + command);
				} else {
					command = "cp " + source + " " + destination;
					System.out.println("Command: " + command);
				}
				channelExec.setCommand(command);
				channelExec.connect();
			}

			// execute the program for given data set
			execSession = execSessionFactory.getSession();
			channelExec = execSession.getClientInstance();
			in = channelExec.getInputStream();

			String program = map.get("makefile").split("[.]")[0];
			String output = map.get("output");
			String result = map.get("result");
			String mode = map.get("mode");
			String run = map.get("run");

			if ("automatic".equalsIgnoreCase(mode)) {
				String options = analysisVo.getParameters() != null ? analysisVo.getParameters() + " " : " ";
				command = "cd " + path + "/program/; " + "chmod 777 " + run + "; cd " + resultDir + "; ./../" + run + " " + options + "> " + result + ";";
			} else {
				command = "./" + path + "/program/" + output + "/" + program + ">" + path + "/program/" + resultDir + "/" + result + ";";
			}
			System.out.println("Command: " + command);

			channelExec.setCommand(command);
			channelExec.connect();

			reader = new BufferedReader(new InputStreamReader(in));

			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				results.append(line);
			}

			exitStatus = channelExec.getExitStatus();

			if (exitStatus < 0) {
				System.out.println("Done, but exit status not set!");
			} else if (exitStatus > 0) {
				System.out.println("Done, but with error!");
			} else {
				System.out.println("Done!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error: " + e);
		}
	}

	public String getAnalysisResults(AnalysisVo analysisVo) {
		Map<String, String> map = new HashMap<String, String>();
		ExecSession execSession = null;
		ChannelExec channelExec = null;
		InputStream in = null;
		BufferedReader reader = null;
		String command = null;

		StringBuffer results = new StringBuffer();

		try {
			execSession = execSessionFactory.getSession();
			channelExec = execSession.getClientInstance();
			in = channelExec.getInputStream();

			String path = "spark/" + analysisVo.getProgramId() + "/" + analysisVo.getProgramName();

			command = "cat " + path + "/spark.info";

			System.out.println("Command: " + command);
			channelExec.setCommand(command);
			channelExec.connect();

			reader = new BufferedReader(new InputStreamReader(in));
			String line;
			int index = 0;

			while ((line = reader.readLine()) != null) {
				System.out.println(++index + " : " + line);
				String array[] = line.split("[:]");
				if (array.length > 1) {
					map.put(array[0], array[1]);
				}
			}

			int exitStatus = channelExec.getExitStatus();

			if (exitStatus < 0) {
				System.out.println("Done, but exit status not set!");
			} else if (exitStatus > 0) {
				System.out.println("Done, but with error!");
			} else {
				System.out.println("Done!");
			}

			execSession = execSessionFactory.getSession();
			channelExec = execSession.getClientInstance();
			in = channelExec.getInputStream();

			String result = map.get("result");

			String resultDir = "result" + analysisVo.getAnalysisId();

			if (analysisVo.getResult() != null) {
				command = "cat " + path + "/program/" + resultDir + "/" + analysisVo.getResult() + ";";
			} else {
				command = "cat " + path + "/program/" + resultDir + "/" + result + ";";
			}

			System.out.println("Command: " + command);

			channelExec.setCommand(command);
			channelExec.connect();

			reader = new BufferedReader(new InputStreamReader(in));

			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				if (line.contains("\n")) {
					results.append(line);
				} else {
					results.append(line + "\n");
				}
			}

			exitStatus = channelExec.getExitStatus();

			if (exitStatus < 0) {
				System.out.println("Done, but exit status not set!");
			} else if (exitStatus > 0) {
				System.out.println("Done, but with error!");
			} else {
				System.out.println("Done!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error: " + e);
		}
		return results.toString();
	}

	private String getPathToCompileDir(String dirName) {
		String path = "";
		try {
			ChannelSftp channelSftp = sftpSession.getClientInstance();
			channelSftp.cd(rootPath);
			channelSftp.cd("spark");
			channelSftp.cd(dirName);

			Vector<String> filest = null;

			filest = channelSftp.ls(".");
			for (int i = 0; i < filest.size(); i++) {
				Object obj = filest.elementAt(i);
				if (obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry) {
					LsEntry entry = (LsEntry) obj;

					if (true && entry.getAttrs().isDir()) {
						path = entry.getFilename();
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}

	// TODO Need to remove in the code clean phase
	// public static void main(String[] args) {
	// String datasetPath =
	// "~/plink-1.07-x86_64/data/expr_data/100SNP/100/100SAMPLE";
	//
	// SparkSftpPedMapGenotypeData genotypeData = null;
	// try {

	// genotypeData = new SparkSftpPedMapGenotypeData(datasetPath);
	// } catch (IOException ex) {
	// ex.printStackTrace();
	// }
	//
	// for (GeneticVariant variant : genotypeData) {
	// System.out.print(variant.getVariantId() + " ");
	// System.out.println(variant.getSequenceName());
	//
	// List<Alleles> alleles = genotypeData.getSampleVariants(variant);
	//
	// for (Alleles allele : alleles) {
	// List<String> alleleList = allele.getAllelesAsString();
	// for (String s : alleleList) {
	// System.out.print(s);
	// }
	// }
	// System.out.println();
	// }
	//
	// for (Sample sample : genotypeData.getSamples()) {
	// System.out.println("Sample ID: " + sample.getId() + " sex: " +
	// sample.getSex() + " Pheno " +
	// sample.getAnnotationValues().get(GenotypeData.DOUBLE_PHENOTYPE_SAMPLE_ANNOTATION_NAME));
	// }
	// }
	//
	// public static void main1(String[] args) {
	// String datasetPath =
	// "~/plink-1.07-x86_64/data/expr_data/100SNP/100/100SAMPLE";
	//
	// // Create a binary plink genotype data object
	// SparkSftpBedBimFamGenotypeData genotypeData = null;
	// try {
	// genotypeData = new SparkSftpBedBimFamGenotypeData(datasetPath);
	// } catch (IOException ex) {
	// ex.printStackTrace();
	// }
	//
	// for (GeneticVariant variant : genotypeData) {
	// System.out.print(variant.getVariantId() + " ");
	// System.out.println(variant.getSequenceName());
	//
	// List<Alleles> alleles = genotypeData.getSampleVariants(variant);
	//
	// for (Alleles allele : alleles) {
	// List<String> alleleList = allele.getAllelesAsString();
	// for (String s : alleleList) {
	// System.out.print(s);
	// }
	// }
	// System.out.println();
	// }
	//
	// for (Sample sample : genotypeData.getSamples()) {
	// System.out.println("Sample ID: " + sample.getId() + " sex: " +
	// sample.getSex() + " Pheno " +
	// sample.getAnnotationValues().get(GenotypeData.DOUBLE_PHENOTYPE_SAMPLE_ANNOTATION_NAME));
	// }
	// }

}
