package personifiler.cluster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import personifiler.util.Cluster;
import personifiler.util.GroundTruth;


public class ClusterPeople 
{
	private PersonMatrix[] matrix;
	private Map<String, Integer> map; //Clustered map, call cluster()
	
	public ClusterPeople(Map<String, double[]> featureMatrix) 
	{
		matrix = new PersonMatrix[featureMatrix.size()];
		
		Iterator<Map.Entry<String, double[]>> iterator = featureMatrix.entrySet().iterator();
		
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
	private void cluster()
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
				    Arrays.asList(matrix[randomIndecies[i]])), matrix[randomIndecies[i]].getMatrix());
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
				double largestDistance = getCosineDistance(person.getMatrix(), temp[0].getMean());
				Group maxGroup = temp[0];
				int groupNum = 0;
				for (int i = 0; i < temp.length; i++)
				{
					double distance = getCosineDistance(person.getMatrix(), temp[i].getMean());
					System.out.println("Person " + person.getName() + " has a distance of " + distance + " to group " + i);
					if (distance > largestDistance)
					{
						largestDistance = distance;
						maxGroup = temp[i];
						groupNum = i;
					}
				}
				
				System.out.println("Person " + person.getName() + " is closest to group " + groupNum);
				maxGroup.getMembers().add(person);
			}
			
			//Recalculate cluster means
			for (Group group: temp)
			{
				for (int i = 0; i < group.getMean().length; i++)
				{
					double average = 0.0;
					for (PersonMatrix person: group.getMembers())
					{
						average += person.getMatrix()[i];
					}
					
					average /= group.getMembers().size();
					
					group.getMean()[i] = average;
				}
			}
			
			groups = temp.clone();
		}
		
		
		//-------------------------------------------------------------------------------
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		for (int i = 0; i < groups.length; i++)
		{
			for (PersonMatrix person: groups[i].getMembers())
				map.put(person.getName(), i);
		}
		
		this.map = map;
	}
	
	/**
	 * Determines the rand index of the clustered group of people versus
	 * the ground truth group of people.
	 * 
	 * @return
	 */
	public double randIndex()
	{
		if (map == null)
			cluster();
		
		System.out.println("Determining the cluster. Size = " + map.size());
		Cluster<String> one = Cluster.transformIntoCluster(map);
		
		System.out.println("Determining the ground truth cluster");
		Cluster<String> two = Cluster.transformIntoCluster(GroundTruth.getGroundTruthCluster(map.keySet()));
		
		return RandIndex.getRandIndex(one, two);
	}
	
	
	private double getCosineDistance(double[] vector1, double[] vector2)
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
