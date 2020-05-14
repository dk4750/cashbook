package com.gdu.cashbook.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberidMapper {
	// memberid 테이블에 id 인서트하는 메소드
	public int insertMemberId(String memberId);
}
