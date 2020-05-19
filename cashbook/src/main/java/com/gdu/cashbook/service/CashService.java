package com.gdu.cashbook.service;

import java.util.List;

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
	
	// 로그인 해당 회원의 오늘 날짜에대한 Cash 리스트 출력
	public List<Cash> getCashListByDate(Cash cash) {
		return cashMapper.selectCashListByDate(cash);
	}
}
