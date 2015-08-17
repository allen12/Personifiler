package finalVersion2;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class RandIndexTEST 
{

	public static double getRandIndex(Cluster one, Cluster two)
	{
		System.out.println("Calculating...");
		System.out.println("first cluster Same pairs size: " + one.samePairs.size());
		System.out.println("first cluster Different pairs size: " + one.differentPairs.size());
		System.out.println("second cluster Same pairs size: " + two.samePairs.size());
		System.out.println("second cluster Different pairs size: " + two.differentPairs.size());
		int a = 0;
		int b = 0;
		int numPairs = one.samePairs.size() + one.differentPairs.size();


		//Calculate a
		for (Pair pair: one.samePairs)
		{
//			for (Pair pair2: two.samePairs)
//			{
//				if (pair.equals(pair2))
//				{
//					a++;
//					break;
//				}
//			}
			if (two.samePairs.contains(pair))
				a++;
		}

		//Calculate b
		for (Pair pair: one.differentPairs)
		{
//			for (Pair pair2: two.differentPairs)
//			{
//				if (pair.equals(pair2))
//				{
//					b++;
//					break;
//				}
//			}
			if (two.differentPairs.contains(pair))
			{
				b++;
				
			}

		}

		System.out.println("a=" + a);
		System.out.println("b=" + b);
		return (a+b)/(double)numPairs;

	}

	public static Cluster getCluster(Map<String, String> map)
	{

		List<Pair> samePairs = new ArrayList<Pair>();
		List<Pair> differentPairs = new ArrayList<Pair>();

		List<String> names = new ArrayList<String>(map.keySet());
		List<String> groups = new ArrayList<String>(map.values());

		try
		{
			for (int i = 0; i < names.size(); i++)
			{
				for (int j = i+1; j < names.size(); j++)
				{
					if (groups.get(i).equals(groups.get(j)))
						samePairs.add(new Pair(names.get(i), names.get(j)));
					else
						differentPairs.add(new Pair(names.get(i), names.get(j)));
				}
			}
		} catch (Throwable e) {}
		//		
		//		Iterator<Map.Entry<String, String>> outer = map.entrySet().iterator();
		//		
		//		while (outer.hasNext())
		//		{
		//			Iterator<Map.Entry<String, String>> inner = map.entrySet().iterator();
		//			Map.Entry<String, String> outerEntry = outer.next();
		//			
		//			// Advance the inner iterator to  the outer iterator
		//			while (inner.hasNext())
		//			{
		//				Map.Entry<String, String> innerEntry = inner.next();
		//				if (innerEntry.getKey().equals(outerEntry.getKey()))
		//					break;
		//			}
		//			
		//			while (inner.hasNext())
		//			{
		//				Map.Entry<String, String> innerEntry = inner.next();
		//				
		//				if (innerEntry.getValue().equals(outerEntry.getValue()))
		//				{
		////					System.out.println("Same pair");
		//					samePairs.add(new Pair(outerEntry.getKey(), innerEntry.getKey()));
		//				}
		//				else
		//				{
		////					System.out.println("Different pair");
		//					differentPairs.add(new Pair(outerEntry.getKey(), innerEntry.getKey()));
		//				}
		//			}
		//		}

		return new Cluster(samePairs, differentPairs);	
	}


	//The 4 people in the specified directly, we will say that they are all in the same group
	public static Map<String, String> getCluster1()
	{
		Map<String, String> map = new HashMap<String, String>();

		File[] files = new File("\\\\Dom1\\aos\\Public\\EN.605.401 Summer 2012\\").listFiles();
		List<File> allFiles = getFiles(files);
		for (File f: files)
			allFiles.add(f);

		List<Person> listOwners = getListOfOwners(allFiles);

		for (Person p: listOwners)
			map.put(p.name, "0");

		return map;
	}

	//ground truth cluster
	public static Map<String, String> getCluster2(Set<String> people)
	{
		Map<String, String> map = new HashMap<String, String>();

		List<Person> fullList = GroundTruth.getList();

		for (Person p: fullList)
		{
			if (people.contains(p.name))
				map.put(p.name, p.group);
		}

		return map;
	}

	//Get a list of all the owners of a file list
	public static List<Person> getListOfOwners(List<File> files)
	{
		List<String> listIDs = new ArrayList<String>();
		List<Person> listPeople = new ArrayList<Person>();
		List<Person> fullList = GroundTruth.getList();

		for (File f: files)
		{
			String ID = getFileOwnerID(f);
			if (!listIDs.contains(ID))
				listIDs.add(ID);
		}


		for (String ID: listIDs)
		{
			for (Person p: fullList)
			{
				if (p.ID.equals(ID))
				{
					listPeople.add(p);
					break;
				}
			}

		}

		return listPeople;

	}

	public static List<File> getFiles(File[] files)
	{
		List<File> allFiles = new ArrayList<File>();

		for (File f: files)
		{
			if (f.isDirectory() )
				allFiles.addAll(getFiles(f.listFiles()));
			else
				allFiles.add(f);
		}

		return allFiles;
	}

	public static String getFileOwnerID(File file)
	{
		String ownerID = "";

		try
		{
			String directory = file.getAbsolutePath();
			Path path = Paths.get(directory);
			FileOwnerAttributeView ownerAttributeView = Files.getFileAttributeView(path, FileOwnerAttributeView.class);
			UserPrincipal owner = ownerAttributeView.getOwner();
			ownerID = owner.getName();

			char[] array = ownerID.toCharArray();
			int index = 0;
			for (int i = index; i < array.length; i++)
				if (array[i] == '\\')
				{
					index = i; break;
				}
			//TOUCHJE1
			ownerID = ownerID.substring(index+1);
		} catch(Exception e)
		{
			ownerID = "Unable to get the owner";
		}

		return ownerID;
	}
}
