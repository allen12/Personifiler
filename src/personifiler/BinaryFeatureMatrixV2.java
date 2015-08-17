package personifiler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Uses a Map<String, Set<String>> to organize files and owners
 * instead of a folder tree as in V1.
 * 
 * @author chenga1
 *
 */
public class BinaryFeatureMatrixV2 extends FeatureMatrix
{
	
	// Sorted list of owners
	private String[] sortedOwners;
	
	public void calculateFeatureMatrix()
	{
		// Gets a list of all of the owners in the map
		Set<String> owners = new TreeSet<String>(this.filesAndOwners.values());
		sortedOwners = new String[owners.size()];
		int i = 0;
		for (String owner: owners)
		{
			sortedOwners[i] = owner; i++;
		}
		
		// Initializes the featureMatrix map
		for (String o: owners)
			this.featureMatrix.put(o, new double[owners.size()]);
		
		// Get the file-owner mappings
		Map<String, Set<String>> mappings = getMappings();
		
		// Generate the feature matrix
		generateFeatureMatrix(mappings);
		
	}
	
	public void generateFeatureMatrix(Map<String, Set<String>> mappings)
	{
		List<Set<String>> owners = new ArrayList<Set<String>>(mappings.values());
		
		for (Set<String> set: owners)
		{
			for (String owner: set)
			{
				double[] ownerVector = this.featureMatrix.get(owner);
				
				for (String owner2: set)
				{
					int index = find(sortedOwners, owner2);
					
					ownerVector[index] = 1.0;
					
				}
				
				this.featureMatrix.put(owner, ownerVector);
			}
		}
	}
	
	
	/**
	 * Finds the index of an object o in array
	 * @param array
	 * @param o
	 * @return
	 */
	private int find(Object[] array, Object o)
	{
		for (int i = 0; i < array.length; i++)
			if (o.equals(array[i]))
				return i;
		
		return -1;
	}
	
	public Map<String, Set<String>> getMappings()
	{
		Map<String, Set<String>> mappings = new HashMap<String, Set<String>>();	
		
		// Iterator for the files and owners map
		Iterator<Map.Entry<String, String>> iterator = this.filesAndOwners.entrySet().iterator();
		
		// Iterate through
		while (iterator.hasNext())
		{
			Map.Entry<String, String> entry = iterator.next();
			String filePath = entry.getKey();
			String ownerName = entry.getValue();

			
			// if the "file" is actually a directory, then create a new mapping
			if (new File(filePath).isDirectory())
			{
				Set<String> set = new HashSet<String>();
				set.add(ownerName);
				
				mappings.put(filePath, set);
			}
			
			// if not, then find its parent and add its owner
			else
			{
				String parent = new File(filePath).getParent();
				
				Set<String> set = mappings.get(parent);
				
				// if for some reason there is no mapping then create one first
				if (set == null)
				{
					Set<String> newSet = new HashSet<String>();
					newSet.add(ownerName);
					
					mappings.put(parent, newSet);
					
					set = mappings.get(parent);
				}
				
				set.add(ownerName);
				
				mappings.remove(parent);
				mappings.put(parent, set);
			}
		}
		
		return mappings;
	}
	
	public String toString()
	{
		String s = "";
		
		Iterator<Map.Entry<String, double[]>> iterator = this.featureMatrix.entrySet().iterator();
		while (iterator.hasNext())
		{
			Map.Entry<String, double[]> entry = iterator.next();
			
			s += "\n" + entry.getKey() + ": [";
			
			double[] vector = entry.getValue();
			
			for (double d: vector)
				s += d + " ";
			
			s += "]";
		}
		
		return s;
	}
	
	public static void main(String[] args)
	{
		BinaryFeatureMatrixV2 b = new BinaryFeatureMatrixV2();
		
		b.readFile(new File("C:\\temp\\file-lists\\2014-12-19-14-54-31_VLF_Analysis.txt"));
		b.calculateFeatureMatrix();
		
		System.out.println(b);
		
		ClusterPeople c = new ClusterPeople(b.featureMatrix);
		
		System.out.println(c.randIndex());
	}
}
