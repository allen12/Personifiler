package personifiler;

import java.util.ArrayList;

/**
 * Helper class for clustering.
 * 
 * @author Allen Cheng
 *
 */
public class Group
{
	ArrayList<PersonMatrix> members;
	double[] mean;
	
	public Group(ArrayList<PersonMatrix> mems, double[] m)
	{
		members = mems;
		mean = m;
	}
}
