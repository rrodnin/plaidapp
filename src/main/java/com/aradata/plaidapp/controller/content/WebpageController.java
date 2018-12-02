package com.aradata.plaidapp.controller.content;

import com.aradata.plaidapp.service.content.WebpageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/webpages")
public class WebpageController {
//
//	@Autowired
//	WebpageService service;
//
//	@PostMapping
//	public ResponseEntity<?> createContent(@RequestBody  String url) throws IOException {
//
//		return ResponseEntity.ok(service.createContentFromUrl(url));
//	}

}
