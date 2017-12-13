import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MonthData
{
	private SimpleStringProperty  code, name, total;
	private SimpleIntegerProperty no, quantity;
	MonthData(int no, String code, String name, int quantity, String total)
	{
		this.no = new SimpleIntegerProperty(no);
		this.code = new SimpleStringProperty(code);
		this.name = new SimpleStringProperty(name);
		this.quantity = new SimpleIntegerProperty(quantity);
		this.total = new SimpleStringProperty(total);
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
	
	public int getQuantity()
	{
		return quantity.get();
	}

	public void setMaxQuantity(int quantity)
	{
		this.quantity.set(quantity);
	}
	
	public String getTotal()
	{
		return total.get();
	}
	public void setTotal(String total)
	{
		this.total.set(total);
	}
}
