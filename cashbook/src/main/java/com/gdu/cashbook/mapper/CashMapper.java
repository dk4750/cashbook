package com.gdu.cashbook.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Cash;

@Mapper
public interface CashMapper {
	// 로그인 해당 회원의 오늘 날짜에대한 Cash 리스트 출력
	public List<Cash> selectCashListByDate(Cash cash);
	
	// cashNo을 넘겨받아 해당 컬럼 삭제
	public int removeCash(int cashNo);
	
	// 가격들의 합을 구하는 메소드
	public int selectCashKindSum(Cash cash);
	
	// cashNo에 따라 그 cash내역 가져오는 메소드
	public Cash selectCashOne(int cashNo);
}
