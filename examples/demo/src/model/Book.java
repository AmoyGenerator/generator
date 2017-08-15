package model;

import java.lang.Integer;
import java.lang.String;
import java.util.Date;

public class Book{
    private Integer id;
    private String name;
    private Integer type;
    private Date registTime;

    public Book() {
		super();
	}

    public Book(Integer id, String name, Integer type, Date registTime) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.registTime = registTime;
	}
	
    public Integer getId() {
		return id;
	}
	
	public void setId(Integer id){
		this.id = id;
	}
	
    public String getName() {
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
    public Integer getType() {
		return type;
	}
	
	public void setType(Integer type){
		this.type = type;
	}
	
    public Date getRegistTime() {
		return registTime;
	}
	
	public void setRegistTime(Date registTime){
		this.registTime = registTime;
	}
	
}