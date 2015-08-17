package personifiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public abstract class FeatureMatrix 
{
	// Key: the path of the file
	// Value: the file's owner
	Map<String, String> filesAndOwners = new LinkedHashMap<String, String>();
	
	// Key: a file owner's name
	// Value: the feature vector
	// Therefore, featureMatrix.size() should equal each double[].length
	Map<String, double[]> featureMatrix = new TreeMap<String, double[]>();
	
	
	
	
	
	
	
	
	/**
	 * Should calculate the feature matrix and store it into the instance
	 * variable "featureMatrix." Child classes should each have a different
	 * way of implementing this method.
	 */
	public abstract void calculateFeatureMatrix();
	
	/**
	 * Reads a text file containing file paths and their owners.
	 * Stores it into the instance variable "filesAndOwners"
	 * @param txt
	 */
	public void readFile(File txt)
	{
		BufferedReader r = null;
		
		try {
			r = new BufferedReader(new FileReader(txt));
			
			r.readLine(); //Get past the useless stuff
			r.readLine();
			
			while (r.ready())
			{
				String line = r.readLine();
				String[] split = line.split("\t\t\t");
				
				try
				{
					filesAndOwners.put(split[0], split[1]);
				} catch (Throwable e) {}
			}
			
			r.close();
			
		} catch (FileNotFoundException e) 
		{
			System.out.println("File not found!");
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
