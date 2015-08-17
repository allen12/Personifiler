package personifiler.cluster;

import java.util.ArrayList;

/**
 * Helper class for clustering.
 * 
 * @author Allen Cheng
 *
 */
public class Group
{
	private ArrayList<PersonMatrix> members;
	private double[] mean;
	
	public Group(ArrayList<PersonMatrix> mems, double[] m)
	{
		members = mems;
		mean = m;
	}
	
	public ArrayList<PersonMatrix> getMembers()
	{
		return members;
	}
	
	public double[] getMean()
	{
		return mean;
	}
}
