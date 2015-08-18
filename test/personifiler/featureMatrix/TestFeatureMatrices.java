package personifiler.featureMatrix;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;


/**
 * Tests for various feature matrix implementations
 * 
 * @author Allen Cheng
 */
@Ignore // for now
public class TestFeatureMatrices
{
	private static File INPUT 	   = new File("testData/sampleFilesAndOwners.txt");
	private static String DELIMITER = "\t";
	
	@Test
	public void testBinaryFeatureMatrix()
	{
		FeatureMatrix matrix = new BinaryFeatureMatrix();
		matrix.readFile(INPUT, DELIMITER);
		matrix.calculateFeatureMatrix();
		
		Map<String, double[]> featureMatrix = matrix.getFeatureMatrix();
		
		assertEquals( new double[]{1.0, 1.0, 1.0}, featureMatrix.get("Person A"));
		assertEquals( new double[]{1.0, 1.0, 0.0}, featureMatrix.get("Person B"));
		assertEquals( new double[]{1.0, 0.0, 1.0}, featureMatrix.get("Person C"));
	}
}
