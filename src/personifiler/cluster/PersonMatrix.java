package personifiler.cluster;

public class PersonMatrix 
{
	private String name;
	private double[] matrix;
	
	public PersonMatrix(String name, double[] m)
	{
		this.name = name;
		this.matrix = m;
	}
	
	public String getName()
	{
		return name;
	}

	public double[] getMatrix()
	{
		return matrix;
	}

}
