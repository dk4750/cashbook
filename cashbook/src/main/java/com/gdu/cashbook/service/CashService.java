package com.gdu.cashbook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook.mapper.CashMapper;
import com.gdu.cashbook.vo.Cash;

@Service
@Transactional
public class CashService {
	@Autowired
	private CashMapper cashMapper;
	
	// cashNO를 넘겨받아 해당 cash내역을 불러오기
	public Cash getCashOne(int cashNo) {
		return cashMapper.selectCashOne(cashNo);
	}
	
	// cashNo를 넘겨받아 해당 컬럼 삭제하는 메소드
	public int removeCash(int cashNo) {
		return cashMapper.removeCash(cashNo);
	}
	
	// 로그인 해당 회원의 오늘 날짜에대한 Cash 리스트 출력과 수입지출의 합을 같이 리턴하기떄문에 Map으로 리턴
	public Map<String, Object> getCashListByDate(Cash cash) {
		List<Cash> cashList = cashMapper.selectCashListByDate(cash);
		int cashKindSum = cashMapper.selectCashKindSum(cash);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cashList", cashList);
		map.put("cashKindSum", cashKindSum);
		return map;
	}
}
