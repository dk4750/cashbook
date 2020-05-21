package com.gdu.cashbook.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Cash;
import com.gdu.cashbook.vo.DayAndPrice;

@Mapper
public interface CashMapper {
	// Cash 내역 수정하기.
	public int modifyCash(Cash cash);
	
	// Cash 내역 인서트 하기
	public int addCash(Cash cash);
	
	// 로그인 해당 회원의 오늘 날짜에대한 Cash 리스트 출력
	public List<Cash> selectCashListByDate(Cash cash);
	
	// memberId를 받아 해당 memberId의 cash를 모두 삭제
	public int removeCashByMember(String memberId);
	
	// cashNo을 넘겨받아 해당 컬럼 삭제
	public int removeCash(int cashNo);
	
	// 가격들의 합을 구하는 메소드
	public int selectCashKindSum(Cash cash);
	
	// cashNo에 따라 그 cash내역 가져오는 메소드
	public Cash selectCashOne(int cashNo);
	
	// 날짜별 수입, 지출의 합을 리턴하는 메소드.
	public List<DayAndPrice> selectDayAndPriceList(Map<String, Object> map);
}
