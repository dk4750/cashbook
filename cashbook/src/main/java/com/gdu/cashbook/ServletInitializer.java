package com.gdu.cashbook;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

 /*
 * servlet API
 * 1. servlet : 요청처리
 * 2. filter : 요청전후에 처리되는코드를 만든다..필터링
 * 3. listener : 이벤트 반응 처리
 */

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CashbookApplication.class);
	}
}