package BLL_package;

public class prediction {
	
	private Double current;
	private Double predicted;
	private String name;
	
	public prediction(Double c, Double p, String n)
	{
		current = c;
		predicted = p;
		name = n;
	}
	
	public Double getCurrent()
	{
		return current;
	}
	
	public Double getPredicted()
	{
		return predicted;
	}
	
	public String getName()
	{
		return name;
	}

}
