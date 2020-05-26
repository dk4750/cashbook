package com.gdu.cashbook.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Category;

@Mapper
public interface CategoryMapper {
	// 카테고리 이름 리스트로 받아오기
	public List<Category> selectCategoryName(String memberId);
	
	// 카테고리 추가하기
	public int addCategory(Map map);
	
	// 카테고리 삭제하기
	public int removeCategory(Map map);
	
	// 해당 멤버의 카테고리 전체삭제
	public int removeCategoryAll(String memberId);
}
