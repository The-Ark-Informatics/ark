package au.org.spark.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.molgenis.genotype.Alleles;
import org.molgenis.genotype.plink.PlinkFileParser;
import org.molgenis.genotype.plink.datatypes.PedEntry;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;


public class SparkSftpPedFileReader implements PlinkFileParser, Iterable<PedEntry> {

	private BufferedReader reader;
//	private final File file;
	private final String separators;
	private long nrElements;
	
	private final String fileName;
	
	private final ChannelSftp channelSftp;

	/**
	 * Construct a PedFileDriver on this file
	 * 
	 * @param bimFile
	 * @throws Exception
	 */
	public SparkSftpPedFileReader(String pedFile, ChannelSftp channelSftp)
	{
		this(pedFile, DEFAULT_READ_FIELD_SEPARATORS,channelSftp);
	}

	public SparkSftpPedFileReader(String pedFile, char separator, ChannelSftp channelSftp)
	{
		this(pedFile, String.valueOf(separator), channelSftp);
	}

	public SparkSftpPedFileReader(String pedFile, String separators, ChannelSftp channelSftp)
	{
		if (pedFile == null) throw new IllegalArgumentException("file is null");
		this.fileName = pedFile;
		this.channelSftp = channelSftp;
		this.separators = separators;
		this.nrElements = -1l;
	}

	/**
	 * Get all PED file entries
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<PedEntry> getAllEntries() throws IOException, SftpException
	{
		reset();

		List<PedEntry> entryList = new ArrayList<PedEntry>();
		String line;
		while ((line = reader.readLine()) != null)
			entryList.add(parseEntry(line));

		return entryList;
	}

	@Override
	public Iterator<PedEntry> iterator()
	{
		try
		{
			reset();
			return new PedFileIterator();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		catch(SftpException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get a specific set of PED file entries
	 * 
	 * @param from
	 *            = inclusive
	 * @param to
	 *            = exclusive
	 * @return
	 * @throws Exception
	 */
	public List<PedEntry> getEntries(final long from, final long to) throws IOException, SftpException
	{
		reset();

		List<PedEntry> entryList = new ArrayList<PedEntry>();
		String line;
		for (int i = 0; (line = reader.readLine()) != null && i < to; ++i)
			if (i >= from) entryList.add(parseEntry(line));

		return entryList;
	}

	private PedEntry parseEntry(String line) throws IOException
	{
		final StringTokenizer strTokenizer = new StringTokenizer(line, separators);
		try
		{
			//Todo this to make sure not to save whole line in background after a split or tokanizer
			String family = new String(strTokenizer.nextToken());
			String individual = new String(strTokenizer.nextToken());
			String father = new String(strTokenizer.nextToken());
			String mother = new String(strTokenizer.nextToken());
			byte sex = Byte.parseByte(strTokenizer.nextToken());
			double phenotype = Double.parseDouble(strTokenizer.nextToken());

			return new PedEntry(family, individual, father, mother, sex, phenotype, new Iterator<Alleles>()
			{

				@Override
				public boolean hasNext()
				{
					return strTokenizer.hasMoreTokens();
				}

				@Override
				public Alleles next()
				{
					char allele1 = strTokenizer.nextToken().charAt(0);
					char allele2 = strTokenizer.nextToken().charAt(0);
					return Alleles.createBasedOnChars(allele1, allele2);
				}

				@Override
				public void remove()
				{
					throw new UnsupportedOperationException();
				}

			});
		}
		catch (NoSuchElementException e)
		{
			throw new IOException("error in line: " + line, e);
		}
		catch (IndexOutOfBoundsException e)
		{
			throw new IOException("error in line: " + line, e);
		}
		catch (NumberFormatException e)
		{
			throw new IOException("error in line: " + line, e);
		}
	}

	public long getNrOfElements() throws IOException, SftpException
	{
		if (nrElements == -1) nrElements = getNumberOfNonEmptyLines(channelSftp,fileName, FILE_ENCODING);
		return nrElements;
	}

	@Override
	public void close() throws IOException
	{
		if (this.reader != null) this.reader.close();
	}

	public void reset() throws IOException, SftpException
	{
		if (this.reader != null) close();
		this.reader = new BufferedReader(new InputStreamReader(channelSftp.get(fileName), FILE_ENCODING));
	}
	
	private static int getNumberOfNonEmptyLines(ChannelSftp channelSftp,String file, Charset charset) throws IOException, SftpException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(channelSftp.get(file), charset));
		try
		{
			int count = 0;
			String line;
			while ((line = reader.readLine()) != null)
				if (!line.isEmpty()) ++count;
			return count;
		}
		finally
		{
			reader.close();
		}
	}

	private class PedFileIterator implements Iterator<PedEntry>
	{
		private String line;

		public PedFileIterator()
		{
			try
			{
				line = reader.readLine();
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		}

		@Override
		public boolean hasNext()
		{
			return line != null;
		}

		@Override
		public PedEntry next()
		{
			PedEntry entry;
			try
			{
				entry = parseEntry(line);
				line = reader.readLine();
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}

			return entry;
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}

	}

}
