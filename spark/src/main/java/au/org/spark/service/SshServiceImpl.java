package au.org.spark.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import org.molgenis.genotype.Alleles;
import org.molgenis.genotype.Sample;
import org.molgenis.genotype.variant.GeneticVariant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.sftp.session.SftpSession;
import org.springframework.stereotype.Service;

import au.org.spark.parser.SparkSftpBedBimFamGenotypeData;
import au.org.spark.parser.SparkSftpPedMapGenotypeData;
import au.org.spark.util.Constants;
import au.org.spark.web.view.DataCenterVo;
import au.org.spark.web.view.DataSourceVo;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;

@Service
public class SshServiceImpl implements SshService {
	
	@Value("${ssh.root.path}")
	private String rootPath;


	private static Pattern pattern = Pattern.compile("\\s");

	@Autowired
	SftpSession session;

	@Autowired
	CassandraService cassandraService;

	@Override
	public List<String> listFiles(String directory) throws Exception {
		ChannelSftp channelSftp = session.getClientInstance();

		List<String> files = new ArrayList<String>();
		channelSftp.cd(directory);
		List filelist = channelSftp.ls(directory);
		for (int i = 0; i < filelist.size(); i++) {
			files.add(filelist.get(i).toString());
		}
		return files;
	}

	public List<DataSourceVo> listFilesAndDirectories(String directory, String fileName) throws Exception {
		ChannelSftp channelSftp = session.getClientInstance();
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
		ChannelSftp channelSftp = session.getClientInstance();

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
		ChannelSftp channelSftp = session.getClientInstance();

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
		ChannelSftp channelSftp = session.getClientInstance();

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

	//TODO Need to remove in the code clean phase
//	public static void main(String[] args) {
//		String datasetPath = "~/plink-1.07-x86_64/data/expr_data/100SNP/100/100SAMPLE";
//
//		SparkSftpPedMapGenotypeData genotypeData = null;
//		try {
//			genotypeData = new SparkSftpPedMapGenotypeData(datasetPath);
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
//
//		for (GeneticVariant variant : genotypeData) {
//			System.out.print(variant.getVariantId() + " ");
//			System.out.println(variant.getSequenceName());
//
//			List<Alleles> alleles = genotypeData.getSampleVariants(variant);
//
//			for (Alleles allele : alleles) {
//				List<String> alleleList = allele.getAllelesAsString();
//				for (String s : alleleList) {
//					System.out.print(s);
//				}
//			}
//			System.out.println();
//		}
//
//		for (Sample sample : genotypeData.getSamples()) {
//			System.out.println("Sample ID: " + sample.getId() + " sex: " + sample.getSex() + " Pheno " + sample.getAnnotationValues().get(GenotypeData.DOUBLE_PHENOTYPE_SAMPLE_ANNOTATION_NAME));
//		}
//	}
//
//	public static void main1(String[] args) {
//		String datasetPath = "~/plink-1.07-x86_64/data/expr_data/100SNP/100/100SAMPLE";
//
//		// Create a binary plink genotype data object
//		SparkSftpBedBimFamGenotypeData genotypeData = null;
//		try {
//			genotypeData = new SparkSftpBedBimFamGenotypeData(datasetPath);
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
//
//		for (GeneticVariant variant : genotypeData) {
//			System.out.print(variant.getVariantId() + " ");
//			System.out.println(variant.getSequenceName());
//
//			List<Alleles> alleles = genotypeData.getSampleVariants(variant);
//
//			for (Alleles allele : alleles) {
//				List<String> alleleList = allele.getAllelesAsString();
//				for (String s : alleleList) {
//					System.out.print(s);
//				}
//			}
//			System.out.println();
//		}
//
//		for (Sample sample : genotypeData.getSamples()) {
//			System.out.println("Sample ID: " + sample.getId() + " sex: " + sample.getSex() + " Pheno " + sample.getAnnotationValues().get(GenotypeData.DOUBLE_PHENOTYPE_SAMPLE_ANNOTATION_NAME));
//		}
//	}

}
