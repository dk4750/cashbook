package com.gdu.cashbook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook.mapper.CategoryMapper;
import com.gdu.cashbook.vo.Category;

@Service
@Transactional
public class CategoryService {
	@Autowired	private CategoryMapper categoryMapper;
	
	// 카테고리 이름 리스트로 출력하기
	public List<Category> getCategoryName(String memberId) {
		return categoryMapper.selectCategoryName(memberId);
	}
	
	// 카테고리 추가
	public int addCategory(String categoryName, String memberId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("categoryName", categoryName);
		map.put("memberId", memberId);
		
		return categoryMapper.addCategory(map);
	}
	
	// 카테고리 삭제
	public int removeCategory(String categoryName, String memberId) {
		Map<String, Object> map = new HashMap<>();
		map.put("categoryName", categoryName);
		map.put("memberId", memberId);
		
		return categoryMapper.removeCategory(map);
	}
}
