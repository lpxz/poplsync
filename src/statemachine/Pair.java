package statemachine;

public class Pair {

	public Object t1part; 
	public Object t2part;
	
	public Pair(Object t1para, Object t2para) // line number!
	{
		t1part = t1para;
		t2part = t2para; 
	}
	
	@Override
	public int hashCode()
	{
		return t1part.hashCode() + t2part.hashCode();
	}
	@Override
	public boolean equals(Object anotherpara)
	{
		if(anotherpara instanceof Pair)
		{
			Pair another = (Pair)anotherpara;
			return (t1part.equals(another.t1part)&& t2part.equals(another.t2part));
		}
		else {
			throw new RuntimeException("should compare with the node of same type");
		}
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
