package personifiler.featureMatrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import personifiler.util.PersonifilerException;

/**
 * <p> Feature matrix object representation. 
 * 
 * <p> A feature matrix is similar to an adjacency matrix of a graph. If there are
 *	 <i> n </i> file owners, then the feature matrix A will have dimensions n x n.
 *
 * <p> The element A_ij represents a connection, if any, between persons i and j.
 * It is up to implementing subclasses to implement the <code>calculateFeatureMatrix</code>
 * method in order to detemrine how to represent this connection.
 * 
 * <p> The feature matrix implementation in this class is a Map of Strings-->double[]
 * Each map entry is a String representing the owner's name, and the corresponding double[]
 * is their feature vector, or a row in the overall feature matrix. Therefore, each entry's
 * double[].length == featureMatrix.size() should be true because the feature matrix is a square matrix.
 * 
 * @author Allen Cheng
 */
public abstract class FeatureMatrix 
{
	// Key: the path of the file
	// Value: the file's owner
	protected Map<String, String> filesAndOwners = new LinkedHashMap<>();
	
	// Key: a file owner's name
	// Value: the feature vector corresponding to that file owner
	// Therefore, featureMatrix.size() should equal each double[].length
	protected Map<String, double[]> featureMatrix;
	
	/**
	 * Calculates the feature matrix and stores it into the instance
	 * variable "featureMatrix." Child classes should each have a different
	 * way of implementing this method.
	 */
	public abstract void calculateFeatureMatrix();
	
	/**
	 * Ingest a text file and stores into the instance variable "filesAndOwners"
	 * 
	 * @param txt
	 * @param delimiter
	 */
	public void readFile(File txt, String delimiter)
	{
		readFile(txt, delimiter, 0);
	}
	
	/**
	 * Reads a text file containing file paths and their owners.
	 * Stores it into the instance variable "filesAndOwners"
	 * 
	 * @param txt File to ingest data
	 * @param delimiter A regular expression of the delimiter between the name and group
	 * @param skipLines number of header lines to skip
	 */
	public void readFile(File txt, String delimiter, int skipLines)
	{
		if (skipLines < 0)
			throw new PersonifilerException("skipLines cannot be negative");
		
		try (BufferedReader r = new BufferedReader(new FileReader(txt)) )
		{
			for (int i = 0; i < skipLines && r.ready(); i++)
				r.readLine();

			while (r.ready())
			{
				String line = r.readLine();
				
				if (line == null || line.equals(""))
					continue;
				
				String[] split = line.split(delimiter);

				filesAndOwners.put(split[0], split[1]);
			}

		} catch (FileNotFoundException e) 
		{
			System.out.println("File not found!");
			
		} catch (IOException e) 
		{
			throw new PersonifilerException(e);
		}
	}
	
	public Map<String, double[]> getFeatureMatrix()
	{
		if (featureMatrix == null)
			calculateFeatureMatrix();
		
		return featureMatrix;
	}
	
	public Map<String, String> getFilesAndOwners()
	{
		return filesAndOwners;
	}
}
