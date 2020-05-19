package com.gdu.cashbook.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Cash;

@Mapper
public interface CashMapper {
	// 로그인 해당 회원의 오늘 날짜에대한 Cash 리스트 출력
	public List<Cash> selectCashListByDate(Cash cash);
}
