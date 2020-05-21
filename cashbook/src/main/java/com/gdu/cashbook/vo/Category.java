package com.gdu.cashbook.vo;

public class Category {
	private String categoryName;
	private String categoryDbsc;
	
	// getter setter
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryDbsc() {
		return categoryDbsc;
	}
	public void setCategoryDbsc(String categoryDbsc) {
		this.categoryDbsc = categoryDbsc;
	}
	
	// toString()
	@Override
	public String toString() {
		return "Category [categoryName=" + categoryName + ", categoryDbsc=" + categoryDbsc + "]";
	}
}
