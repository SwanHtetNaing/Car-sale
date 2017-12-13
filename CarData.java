
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CarData
{
	private SimpleStringProperty code, name, price;
	private SimpleIntegerProperty no, maxQuantity;
	
	public CarData(int no, String code, String name, int maxQuantity, String price)
	{
		this.no = new SimpleIntegerProperty(no);
		this.code = new SimpleStringProperty(code);
		this.name =new SimpleStringProperty(name);
		this.maxQuantity = new SimpleIntegerProperty(maxQuantity);
		this.price = new SimpleStringProperty(price);
	}

	public int getNo()
	{
		return this.no.get();
	}
	
	public void setNo(int no)
	{
		this.no.set(no);
	}
	
	
	public String getCode()
	{
		return code.get();
	}

	public void setCode(String code)
	{
		this.code.set(code);
	}

	public String getName()
	{
		return name.get();
	}

	public void setName(String name)
	{
		this.name.set(name);
	}

	// price
	public String getPrice()
	{
		return price.get();
	}

	public void setPrice(String price)
	{
		this.price.set(price);
	}
	
	// max quantity
	public int getMaxQuantity()
	{
		return maxQuantity.get();
	}

	public void setMaxQuantity(int maxQuantity)
	{
		this.maxQuantity.set(maxQuantity);
	}
}
