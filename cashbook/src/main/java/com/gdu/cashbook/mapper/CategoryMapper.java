package com.gdu.cashbook.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Category;

@Mapper
public interface CategoryMapper {
	// 카테고리 이름 리스트로 받아오기
	public List<Category> selectCategoryName();
}
