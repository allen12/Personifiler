package personifiler.cluster;

import personifiler.util.Cluster;
import personifiler.util.Pair;

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
	
	public static <T> double getRandIndex(Cluster<T> one, Cluster<T> two)
	{		
		int a = 0;
		int b = 0;
		int numPairs = one.getSamePairs().size() + one.getDifferentPairs().size();

		//Calculate a
		for (Pair<T> pair: one.getSamePairs())
		{
			if (two.getSamePairs().contains(pair))
				a++;
		}

		//Calculate b
		for (Pair<T> pair: one.getDifferentPairs())
		{
			if (two.getDifferentPairs().contains(pair))
				b++;

		}
		
		return (a+b)/(double)numPairs;
	}

}
