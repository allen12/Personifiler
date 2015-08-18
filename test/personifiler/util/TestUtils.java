package personifiler.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * General testing utility methods
 * 
 * @author Allen Cheng
 */
public class TestUtils
{

	public static Map<String, double[]> getSampleFeatureMatrix()
	{
		Map<String, double[]> featureMatrix = new HashMap<>();
		featureMatrix.put("Person A", new double[]{1.0, 3.0, 1.0});
		featureMatrix.put("Person B", new double[]{3.0, 1.0, 0.0});
		featureMatrix.put("Person C", new double[]{1.0, 0.0, 1.0});
		
		return featureMatrix;
	}
	
	public static GroundTruth getSampleGroundTruth()
	{
		return new GroundTruth()
		{
			final String GROUP = "They are all in the same group.";
			
			@Override public Collection<Person> getGroundTruth() 
			{
				return Arrays.asList(new Person("Person A", GROUP), 
						new Person("Person B", GROUP), new Person("Person C", GROUP));
			}
			
			@Override public Map<String, String> getGroundTruthCluster(Collection<String> people)
			{
				Map<String, String> ret = new HashMap<>();
				for (String person : people)
					ret.put(person, GROUP);
				
				return ret;
			}
		};
	}
}
