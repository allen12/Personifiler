package personifiler;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * A cluster is a wrapper tool to use with the rand index calculation.
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