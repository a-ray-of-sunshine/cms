package org.winterframework.core.entity;


public class Dog implements IAnminal
{
	private String dogName;
	
	private int age;
	
	private float weight;

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public String getDogName()
	{
		return dogName;
	}

	public void setDogName(String dogName)
	{
		this.dogName = dogName;
	}

	public int getAge()
	{
		return age;
	}

	public void setAge(int age)
	{
		this.age = age;
	}
	
	@Override
	public void saySomething(String message)
	{
		System.out.println("i'am " + this.getDogName() + ". say: " + message); 
	}
}
