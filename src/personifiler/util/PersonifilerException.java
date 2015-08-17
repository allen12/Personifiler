package personifiler.util;

@SuppressWarnings("serial")
public class PersonifilerException extends RuntimeException
{
	
	public PersonifilerException()
	{
		super();
	}
	
	public PersonifilerException(Exception e)
	{
		super(e);
	}
	
	public PersonifilerException(String message)
	{
		super(message);
	}
	
	public PersonifilerException(String message, Exception e)
	{
		super(message, e);
	}

}
