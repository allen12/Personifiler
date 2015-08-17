package personifiler;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * <p> The rand index is an evaluation metric between two {@link Cluster}s that
 * measures the similarity between them.
 * 
 * <p> Each cluster has a list of same pairs, and a list of different pairs.
 * The rand index is equal to (a+b)/numPairs, where a is the number of same pairs
 * pairs that the two clusters agree upon, and b is the number of different pairs
 * that the two clusters agree upon.
 * 
 * @author Allen Cheng
 */
public class RandIndex
{
	
	public static double getRandIndex(Cluster one, Cluster two)
	{
		System.out.println("Calculating...");
		System.out.println("first cluster Same pairs size: " + one.getSamePairs().size());
		System.out.println("first cluster Different pairs size: " + one.getDifferentPairs().size());
		System.out.println("second cluster Same pairs size: " + two.getSamePairs().size());
		System.out.println("second cluster Different pairs size: " + two.getDifferentPairs().size());
		
		int a = 0;
		int b = 0;
		int numPairs = one.getSamePairs().size() + one.getDifferentPairs().size();

		//Calculate a
		for (Pair pair: one.getSamePairs())
		{
			if (two.getSamePairs().contains(pair))
				a++;
		}

		//Calculate b
		for (Pair pair: one.getDifferentPairs())
		{
			if (two.getDifferentPairs().contains(pair))
				b++;

		}

		System.out.println("a=" + a);
		System.out.println("b=" + b);
		return (a+b)/(double)numPairs;
	}

	public static Cluster getCluster(Map<String, String> map)
	{
		List<Pair> samePairs = new ArrayList<>();
		List<Pair> differentPairs = new ArrayList<>();

		List<String> names = new ArrayList<>(map.keySet());
		List<String> groups = new ArrayList<>(map.values());

		try
		{
			for (int i = 0; i < names.size(); i++)
			{
				for (int j = i+1; j < names.size(); j++)
				{
					if (groups.get(i).equals(groups.get(j)))
						samePairs.add(new Pair(names.get(i), names.get(j)));
					else
						samePairs.add(new Pair(names.get(i), names.get(j)));
				}
			}
		} catch (Exception e) 
		{
			throw new PersonifilerException(e);
		}

		return new Cluster(samePairs, differentPairs);	
	}

	/**
	 * Retrieves a mapping of name-->group from the ground truth list
	 * 
	 * @param people A collection of the people's names that need to be retrieved
	 * @return
	 */
	public static Map<String, String> getGroundTruthCluster(Collection<String> people)
	{
		Map<String, String> map = new HashMap<>();
		
		for (Person p: GroundTruth.getList())
		{
			if (people.contains(p.getName()))
				map.put(p.getName(), p.getGroup());
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
				if (p.getID().equals(ID))
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
