package personifiler;
import java.util.List;

/**
 * 
 * A cluster is a wrapper tool to use with the rand index calculation.
 * 
 * @author Allen Cheng
 */
public class Cluster
{
	private List<Pair> samePairs;
	private List<Pair> differentPairs;

	public Cluster(List<Pair> a, List<Pair> b)
	{
		samePairs = a; differentPairs = b;
	}
	
	public List<Pair> getSamePairs()
	{
		return samePairs;
	}
	
	public List<Pair> getDifferentPairs()
	{
		return differentPairs;
	}
}