package personifiler.cluster;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for {@link ClusterPeople}
 * 
 * @author Allen Cheng
 */
public class ClusterPeopleTest
{
	/**
	 * Tests that clustering a feature matrix will produce results
	 * that have those same people originally contained in the feature matrix.
	 */
	@Test
	public void testClustering()
	{
		Map<String, double[]> featureMatrix = new HashMap<>();
		featureMatrix.put("Person A", new double[]{1.0, 3.0, 1.0});
		featureMatrix.put("Person B", new double[]{3.0, 1.0, 0.0});
		featureMatrix.put("Person C", new double[]{1.0, 0.0, 1.0});
		
		ClusterPeople test = new ClusterPeople(featureMatrix);
		Map<String, Integer> result = test.getClusteredMap();
		
		assertEquals( 3, result.size() );
		assertTrue( result.containsKey("Person A") );
		assertTrue( result.containsKey("Person B") );
		assertTrue( result.containsKey("Person C") );
	}

}
