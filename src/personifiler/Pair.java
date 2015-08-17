package personifiler;

public class Pair 
{
	Object first;
	Object second;
	
	public Pair(Object f, Object s)
	{
		first = f; second = s;
	}
	
	//Two pairs are equal if both objects of one pair matches both objects of the second pair
	public boolean equals(Object other)
	{
		Pair p = (Pair)other;
		
		if ((first.equals(p.first) && second.equals(p.second)) || (first.equals(p.second) && second.equals(p.first)))
			return true;
		
		return false;
	}
	
	public String toString()
	{
		String string = "";
		string += first + " and " + second;
		return string;
	}
}
