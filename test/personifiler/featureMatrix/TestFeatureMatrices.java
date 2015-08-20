package personifiler.featureMatrix;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import org.junit.Test;


/**
 * Tests for various feature matrix implementations
 * 
 * @author Allen Cheng
 */
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

		assertTrue( Arrays.equals(new double[]{1.0, 1.0, 1.0}, featureMatrix.get("Person A") ));
		assertTrue( Arrays.equals(new double[]{1.0, 1.0, 0.0}, featureMatrix.get("Person B") ));
		assertTrue( Arrays.equals(new double[]{1.0, 0.0, 1.0}, featureMatrix.get("Person C") ));
	}
	
	@Test
	public void testUnbinaryFeatureMatrix()
	{
		FeatureMatrix matrix = new UnbinaryFeatureMatrix();
		matrix.readFile(INPUT, DELIMITER);
		matrix.calculateFeatureMatrix();
		
		Map<String, double[]> featureMatrix = matrix.getFeatureMatrix();

		assertTrue( Arrays.equals(new double[]{2.0, 1.0, 1.0}, featureMatrix.get("Person A") ));
		assertTrue( Arrays.equals(new double[]{1.0, 1.0, 0.0}, featureMatrix.get("Person B") ));
		assertTrue( Arrays.equals(new double[]{1.0, 0.0, 1.0}, featureMatrix.get("Person C") ));
	}
}
