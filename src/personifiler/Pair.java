package personifiler;

public class Pair<T>
{
	private T first;
	private T second;
	
	public Pair(T f, T s)
	{
		first = f; second = s;
	}
	
	/**
	 * Two pairs are equal if both objects of one pair matches both objects of the second pair.
	 * Order does not matter.
	 */
	public boolean equals(Object other)
	{
		if (other instanceof Pair<?> == false)
			return false;
		
		@SuppressWarnings("unchecked") //safe cast
		Pair<T> p = (Pair<T>) other;
		
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
