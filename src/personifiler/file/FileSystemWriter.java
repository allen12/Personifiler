package personifiler.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;

import personifiler.util.PersonifilerException;

/**
 * Sample class on traversing through a file directory, fully recursively, and writing
 * to a text file all the folders/files and their owners.
 * 
 * @author Allen Cheng
 */
public class FileSystemWriter
{
	private BufferedWriter writer;

	/**
	 * Constructs a FileSystemWriter object
	 * 
	 * @param TXT_OUT The directory of the output text file
	 */
	public FileSystemWriter(final String TXT_OUT)
	{
		try
		{
			writer = new BufferedWriter(new FileWriter(new File(TXT_OUT)));
		} catch (IOException e)
		{
			throw new PersonifilerException(e);
		}
	}

	/**
	 * Traverses through the filesystem fully recursively and write to the text file in a buffer
	 * @param ROOT The starting directory
	 */
	public void writeFileOwners(final String ROOT)
	{
		try
		{
			File thisFile = new File(ROOT);
			writer.write(thisFile.getAbsolutePath() + "\t" + getOwnerName(thisFile));
			writer.newLine();

			File[] filesInDirectory = new File(ROOT).listFiles();

			for (File file : filesInDirectory)
			{
				if (file.isDirectory() == true)
				{
					writeFileOwners(file.getAbsolutePath());
				}
				else
				{
					writer.write(file.getAbsolutePath() + "\t" + getOwnerName(file));
					writer.newLine();
				}
			}
		} catch (IOException e)
		{
			throw new PersonifilerException(e);
		}
	}

	private String getOwnerName(File file)
	{
		String directory = file.getAbsolutePath();
		Path path = Paths.get(directory);
		FileOwnerAttributeView ownerAttributeView = Files.getFileAttributeView(path, FileOwnerAttributeView.class);
		UserPrincipal owner = null;

		try
		{
			owner = ownerAttributeView.getOwner();
		} catch (IOException e)
		{
			throw new PersonifilerException(e);
		}

		return owner.getName();
	}

	/**
	 * Call after finished examining directories to close the write buffer.
	 */
	public void finish()
	{
		try
		{
			writer.flush();
			writer.close();
		} catch (IOException e)
		{
			throw new PersonifilerException(e);
		}
	}
}
