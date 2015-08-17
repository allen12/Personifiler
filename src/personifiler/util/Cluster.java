package personifiler.util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * A cluster is a wrapper tool to use with the rand index calculation and clustering.
 * 
 * @author Allen Cheng
 */
public class Cluster<T>
{
	private List<Pair<T>> samePairs;
	private List<Pair<T>> differentPairs;

	public Cluster(List<Pair<T>> a, List<Pair<T>> b)
	{
		samePairs = a; differentPairs = b;
	}
	
	public List<Pair<T>> getSamePairs()
	{
		return samePairs;
	}
	
	public List<Pair<T>> getDifferentPairs()
	{
		return differentPairs;
	}
	
	/**
	 * Helper method to get a cluster corresponding to an input map
	 * 
	 * Keys of input map are people's names, values the people's groups
	 * Expected result is a Cluster object
	 * samePairs consists of Pairs with people in the same group
	 * differentPairs consists of Pairs with people in different groups
	 * 
	 * @param map
	 * @return
	 */
	public static <T0, T1> Cluster<T0> transformIntoCluster(Map<T0, T1> map)
	{
		List<Pair<T0>> samePairs = new ArrayList<>();
		List<Pair<T0>> differentPairs = new ArrayList<>();

		List<T0> names = new ArrayList<>(map.keySet());
		List<T1> groups = new ArrayList<>(map.values());

		try
		{
			for (int i = 0; i < names.size(); i++)
			{
				for (int j = i+1; j < names.size(); j++)
				{
					if (groups.get(i).equals(groups.get(j)))
						samePairs.add(new Pair<T0>(names.get(i), names.get(j)));
					else
						differentPairs.add(new Pair<T0>(names.get(i), names.get(j)));
				}
			}
		} catch (Exception e) 
		{
			throw new PersonifilerException(e);
		}

		return new Cluster<T0>(samePairs, differentPairs);	
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
}