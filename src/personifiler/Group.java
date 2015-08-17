package personifiler;

import java.util.ArrayList;
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
