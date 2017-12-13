import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class SaleData
{
	private SimpleIntegerProperty no;
	private SimpleStringProperty code, name, quantity, price, total, day, month, year;
	public SaleData(int no,String code, String name, String quantity, String price, String total, String day, String month, String year)
	{
		this.no = new SimpleIntegerProperty(no);
		this.code = new SimpleStringProperty(code);
		this.name = new SimpleStringProperty(name);
		this.quantity = new SimpleStringProperty(quantity);
		this.price = new SimpleStringProperty(price);
		this.total = new SimpleStringProperty(total);
		this.day = new SimpleStringProperty(day);
		this.month = new SimpleStringProperty(month);
		this.year = new SimpleStringProperty(year);
	}
	
	public int getNo()
	{
		return no.get();
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
	
	public String getQuantity()
	{
		return quantity.get();
	}
	public void setQuantity(String quantity)
	{
		this.quantity.set(quantity);
	}
	
	public String getPrice()
	{
		return price.get();
	}
	public void setPrice(String price)
	{
		this.price.set(price);
	}
	
	public String getTotal()
	{
		return total.get();
	}
	public void setTotal(String total)
	{
		this.total.set(total);
	}
	
	public String getDay()
	{
		return day.get();
	}
	public void setDay(String day)
	{
		this.day.set(day);
	}
	
	public String getMonth()
	{
		return month.get();
	}
	public void setMonth(String month)
	{
		this.month.set(month);
	}
	
	public String getYear()
	{
		return year.get();
	}
	public void setYear(String year)
	{
		this.year.set(year);
	}
	
}
