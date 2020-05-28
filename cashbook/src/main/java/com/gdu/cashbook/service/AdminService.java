package com.gdu.cashbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook.mapper.AdminMapper;

@Service
@Transactional
public class AdminService {
	@Autowired private AdminMapper adminMapper;
}
