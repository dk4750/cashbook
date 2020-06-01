package com.gdu.cashbook.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Admin;

@Mapper
public interface AdminMapper {
	// 관리자 로그인
	public Admin selectAdmin(Admin admin);
}
