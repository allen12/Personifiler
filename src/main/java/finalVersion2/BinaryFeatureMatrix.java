package finalVersion2;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class BinaryFeatureMatrix extends FeatureMatrix
{
	// Sorted list of owners
	private String[] sortedOwners;
	
	
	@Override
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
		
		// Get the folder tree from the filesAndOwners map
		FolderTree tree = getFolderTree();
		
		// Calculate the feature matrix from the folder tree
		this.featureMatrix = generateFeatureMatrix(tree.root, this.featureMatrix);
	}
	
	int gotIt = 0;
	private Map<String, double[]> generateFeatureMatrix(
			FolderTree.Folder folder, 
			Map<String, double[]> matrix)
	{
		// Add the current folder's owner contents into the matrix
		List<String> owners = folder.owners;
		
		for (String owner: owners)
		{
			double[] ownerVector = matrix.get(owner);
			
			for (String owner2: owners)
			{
				int index = find(sortedOwners, owner2);
				if (gotIt != 8)
				{
					gotIt++;
					System.out.println(owner2 + " is " + index);
				}
				ownerVector[index] = 1.0;
			}
			
			matrix.put(owner, ownerVector);
		}
		
		// Do the same thing for each child of this folder recursively
		for (FolderTree.Folder f: folder.children)
			matrix = combineMaps(matrix, generateFeatureMatrix(f, matrix));
		
		return matrix;
	}
	
	public static double[] combineArray(double[] one, double[] two)
	{
		double[] n = new double[one.length];
		for (int i = 0; i < one.length; i++)
		{
			n[i] = one[i] + two[i];
			if (n[i] > 1)
				n[i] = 1;
		}
		
		return n;
	}
	
	public static Map<String, double[]> combineMaps(Map<String, double[]> one, Map<String, double[]> two)
	{
		Map<String, double[]> n = new TreeMap<String, double[]>();
		
		Iterator<Map.Entry<String, double[]>> iterator = one.entrySet().iterator();
		while (iterator.hasNext())
		{
			Map.Entry<String, double[]> entry = iterator.next();
			double[] combinedArray = combineArray(entry.getValue(), two.get((String)entry.getKey()));
			n.put(entry.getKey(), combinedArray);
		}
		
		return n;
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
	
	/**
	 * Gets the folder tree given by the filesAndOwners map.
	 * Precondition: every key in the map is of the form
	 * 		"root\\aaa\\bbb\\ccc\\...\\ddd." root must be the
	 * 		same for each key.
	 * @return
	 */
	private FolderTree getFolderTree()
	{
		// Construct the root Folder object
		String rootName = getRoot();
		FolderTree.Folder root = new FolderTree.Folder(rootName, null);
		
		// Construct the folder tree
		FolderTree tree = new FolderTree(root);
		
		// Iterate through the filesAndOwners map and add them to the folder tree
		Iterator<Map.Entry<String, String>> iterator = this.filesAndOwners.entrySet().iterator();
		while (iterator.hasNext())
		{
			
			Map.Entry<String, String> entry = iterator.next();
			String path = entry.getKey(); String owner = entry.getValue();

			tree.addFile(path.substring(2), owner);
		}
		
		return tree;
	}
	
	/**
	 * 
	 * @return the String of the root folder given by the first entry of the owners map
	 */
	private String getRoot()
	{
		Set<String> files = this.filesAndOwners.keySet();
		
		Iterator<String> iterator = files.iterator();
		
		String rootPath = iterator.next();
		
		String[] split = rootPath.split("\\\\");
		
		for (String s: split)
			if (!s.equals(""))
				return s;
		
		return null;
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
		BinaryFeatureMatrix b = new BinaryFeatureMatrix();
		
		b.readFile(new File("C:\\temp\\file-lists\\combined2.txt"));
		b.calculateFeatureMatrix();
		
		System.out.println(b);
		
		ClusterPeople c = new ClusterPeople(b.featureMatrix);
		
		System.out.println(c.randIndex());
	}

}
