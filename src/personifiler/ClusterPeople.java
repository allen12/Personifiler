package personifiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class ClusterPeople 
{
	private PersonMatrix[] matrix;
	private Map<String, Integer> map; //Clustered map, call cluster()
	
	public ClusterPeople(Map<String, double[]> map) //size is the number of people to cluster
	{
		matrix = new PersonMatrix[map.size()];
		
		Iterator<Map.Entry<String, double[]>> iterator = map.entrySet().iterator();
		
		for (int i = 0; i < matrix.length; i++)
		{
			Map.Entry<String, double[]> entry = iterator.next();
			
			matrix[i] = new PersonMatrix(entry.getKey(), entry.getValue());
		}
		
		cluster();
	}
	
	/**
	 * Returns a clustered list of people.
	 * Key is the person's name, Value is an arbitrary integer denoting the group they belong in
	 * @return
	 */
	public Map<String, Integer> getClusteredMap()
	{
		if (map == null)
			cluster();
		
		return map;
	}

	/**
	 * Clusters using k-means.
	 * Distance measure is cosine distance.
	 * 
	 * Uses k = Math.ceil(sqrt(n/2))
	 * 
	 * @return
	 */
	public Map<String, Integer> cluster()
	{
		System.out.println("STARTING CLUSTERING");
		int n = matrix.length;
		int k = (int)Math.ceil(Math.sqrt(n / 2.0));
		System.out.println("n = " + n);
		System.out.println("k = " + k);
		
		//---------------------------------------Initialize random centroids---------------------------------------
		Group[] groups = new Group[k];
		int[] randomIndecies = new int[k];
		for (int i = 0; i < randomIndecies.length; i++)
			randomIndecies[i] = new Random().nextInt(n);
		while (true)
		{
			for (int i = 0; i < randomIndecies.length; i++)
				for (int j = i-1; j >= 0; j--)
					if (randomIndecies[i] == randomIndecies[j])
					{
						randomIndecies[i] = new Random().nextInt(n);
						continue;
					}
			break;
		}
		for (int i = 0; i < randomIndecies.length; i++)
		{
			groups[i] = new Group(new ArrayList<PersonMatrix>(
				    Arrays.asList(matrix[randomIndecies[i]])), matrix[randomIndecies[i]].matrix);
		}
		//---------------------------------------------------------------------------------------------------------

		//-------------------- Repeat clustering for 25 times ------------------------------
		
		for (int a = 0; a < 25; a++)
		{
			System.out.println("Iteration " + a);
			//Assign each person to a centroid based on the closest cosine distance
			Group[] temp = groups.clone();
			for (PersonMatrix person: matrix)
			{
				//Find closest centroid
				double largestDistance = getCosineDistance(person.matrix, temp[0].mean);
				Group maxGroup = temp[0];
				int groupNum = 0;
				for (int i = 0; i < temp.length; i++)
				{
					double distance = getCosineDistance(person.matrix, temp[i].mean);
					System.out.println("Person " + person.name + " has a distance of " + distance + " to group " + i);
					if (distance > largestDistance)
					{
						largestDistance = distance;
						maxGroup = temp[i];
						groupNum = i;
					}
				}
				
				System.out.println("Person " + person.name + " is closest to group " + groupNum);
				maxGroup.members.add(person);
			}
			
			//Recalculate cluster means
			for (Group group: temp)
			{
				for (int i = 0; i < group.mean.length; i++)
				{
					double average = 0.0;
					for (PersonMatrix person: group.members)
					{
						average += person.matrix[i];
					}
					
					average /= group.members.size();
					
					group.mean[i] = average;
				}
			}
			
			groups = temp.clone();
		}
		
		
		//-------------------------------------------------------------------------------
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		for (int i = 0; i < groups.length; i++)
		{
			for (PersonMatrix person: groups[i].members)
				map.put(person.name, i);
		}
		
		this.map = map;
		return map;
		
	}
	
	public double randIndex()
	{
		System.out.println("Determining the cluster. Size = " + map.size());
		Cluster<String> one = getCluster(map);
		
		System.out.println("Determining the ground truth cluster");
		Cluster<String> two = transformIntoCluster(Cluster.getGroundTruthCluster(map.keySet()));
		
		return RandIndex.getRandIndex(one, two);
	}
	
	public static Cluster<String> getCluster(Map<String, Integer> map)
	{
		List<Pair<String>> samePairs = new ArrayList<>();
		List<Pair<String>> differentPairs = new ArrayList<>();
		
		Iterator<Map.Entry<String, Integer>> outer = map.entrySet().iterator();
		
		while (outer.hasNext())
		{
			Iterator<Map.Entry<String, Integer>> inner = map.entrySet().iterator();
			Map.Entry<String, Integer> outerEntry = outer.next();
			
			// Advance the inner iterator to  the outer iterator
			while (inner.hasNext())
			{
				Map.Entry<String, Integer> innerEntry = inner.next();
				if (innerEntry.getKey().equals(outerEntry.getKey()))
				{
					break;
				}
			}
			
			while (inner.hasNext())
			{
				Map.Entry<String, Integer> innerEntry = (Map.Entry<String, Integer>)inner.next();
				
				if (innerEntry.getValue().equals(outerEntry.getValue()))
				{
					samePairs.add(new Pair<String>(outerEntry.getKey(), innerEntry.getKey()));
				}
				else
				{
					differentPairs.add(new Pair<String>(outerEntry.getKey(), innerEntry.getKey()));
				}
			}
		}
		
		return new Cluster<String>(samePairs, differentPairs);	
	}
	
	// Keys of input map are people's names, values the people's groups
	// Expected result is a Cluster object
	// samePairs consists of Pairs with people in the same group
	// differentPairs consists of Pairs with people in different groups
	private static Cluster<String> transformIntoCluster(Map<String, String> map)
	{
		List<Pair<String>> samePairs = new ArrayList<>();
		List<Pair<String>> differentPairs = new ArrayList<>();

		List<String> names = new ArrayList<>(map.keySet());
		List<String> groups = new ArrayList<>(map.values());

		try
		{
			for (int i = 0; i < names.size(); i++)
			{
				for (int j = i+1; j < names.size(); j++)
				{
					if (groups.get(i).equals(groups.get(j)))
						samePairs.add(new Pair<String>(names.get(i), names.get(j)));
					else
						samePairs.add(new Pair<String>(names.get(i), names.get(j)));
				}
			}
		} catch (Exception e) 
		{
			throw new PersonifilerException(e);
		}

		return new Cluster<String>(samePairs, differentPairs);	
	}
	
	public double getCosineDistance(double[] vector1, double[] vector2)
	{
		double dotProduct = 0.0;
		for (int i = 0; i < vector1.length; i++)
			dotProduct += vector1[i]*vector2[i];
		
		double magnitude = Math.sqrt(magnitudeOfBinaryArray(vector1)) * Math.sqrt(magnitudeOfBinaryArray(vector2));
		if (magnitude == 0.0)
			magnitude = 0.88;
		
		return dotProduct/magnitude;
		
	}
	
	private double magnitudeOfBinaryArray(double[] array)
	{
		double sum = 0.0;
		for (double d: array)
			sum += d;
		
		return sum;
	}


}
