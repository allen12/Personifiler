package personifiler.util;

import java.util.Collection;
import java.util.Map;

/**
 * <p> The GroundTruth interface represents a ground truth view of the actual groups of people.
 * 
 * <p> Implementing classes need to provide their own implementation on how to ingest data
 * to obtain a Collection (List, Set, etc.) of Person objects.
 * 
 * @author Allen Cheng
 */
public interface GroundTruth
{
	/**
	 * Obtain a list of the ground truth of the actual groups of people.
	 * 
	 * @return
	 */
	Collection<Person> getGroundTruth();
	
	/**
	 * Obtain the groups of the requested collection of people's names.
	 * 
	 * @param people A collection of strings of people's names to request their groups
	 * @return A map - keys are people's names requested by the people argument, values are their corresponding groups
	 */
	Map<String, String> getGroundTruthCluster(Collection<String> people);
}
