package finalVersion2;
import java.util.List;


	public  class Cluster
	{
		List<Pair> samePairs;
		List<Pair> differentPairs;
		
		public Cluster(List<Pair> a, List<Pair> b)
		{
			samePairs = a; differentPairs = b;
		}
	}