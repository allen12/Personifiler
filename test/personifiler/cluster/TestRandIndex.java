package personifiler.cluster;

import static org.junit.Assert.*;

import org.junit.Test;

import personifiler.util.TestUtils;

/**
 * Tests for {@link RandIndex}
 * 
 * @author Allen Cheng
 */
public class TestRandIndex
{

	/**
	 * Tests that the rand index of a cluster is between 0.0 and 1.0
	 */
	@Test
	public void testRandIndex()
	{
		ClusterPeople cluster = new ClusterPeople(TestUtils.getSampleFeatureMatrix());
		double randIndex = cluster.randIndex(TestUtils.getSampleGroundTruth());
		
		assertTrue(randIndex >= 0.0 && randIndex <= 1.0);
	}

}
