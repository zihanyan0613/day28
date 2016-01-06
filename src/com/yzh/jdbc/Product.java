package com.yzh.jdbc;

import java.util.Date;

public class Product {

	private Integer id;
	private String name;

	private String desc;
	private Date createDate;
	
	public Product() {
		// TODO Auto-generated constructor stub
	}

	public Product(Integer id, String name, String desc, Date createDate) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.createDate = createDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", desc=" + desc
				+ ", createDate=" + createDate + "]";
	}
}
