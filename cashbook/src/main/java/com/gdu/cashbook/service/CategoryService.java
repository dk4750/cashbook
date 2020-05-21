package com.gdu.cashbook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook.mapper.CategoryMapper;
import com.gdu.cashbook.vo.Category;

@Service
@Transactional
public class CategoryService {
	@Autowired
	private CategoryMapper categoryMapper;
	
	// 카테고리 이름 리스트로 출력하기
	public List<Category> getCategoryName() {
		return categoryMapper.selectCategoryName();
	}
}
