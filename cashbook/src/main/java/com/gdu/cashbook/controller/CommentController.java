package com.gdu.cashbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.gdu.cashbook.service.CommentService;

@Controller
public class CommentController {
	@Autowired private CommentService commentService;

}
