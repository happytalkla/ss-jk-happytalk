package ht.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;

import ht.config.CustomProperty;
import ht.service.CategoryService;
import ht.service.EndCtgService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class RestCategoryController {

	@Resource
	private CustomProperty customProperty;
	@Resource
	private CategoryService categoryService;
	@Resource
	private EndCtgService endCtgService;

	/**
	 * 종료 분류 검색
	 */
	@CrossOrigin
	@GetMapping(path = "/api/end-category")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getEndCategoryList(
			@RequestParam(value = "keyword", required = false) String keyword)
					throws Exception
	{
		log.info("GET END CATEGORY, KEYWORD: {}", keyword);

		if (!Strings.isNullOrEmpty(keyword)) {
			List<Map<String, Object>> endCategoryList = endCtgService.selectEndCategoryListByCtgNm(keyword);
			return new ResponseEntity<>(endCategoryList, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
		}
	}

	/**
	 * 종료 분류
	 */
	@CrossOrigin
	@GetMapping(path = "/api/end-category/{joinedCategogyCode}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getEndCategory(
			@PathVariable("joinedCategogyCode") String joinedCategogyCode)
					throws Exception
	{
		log.info("GET END CATEGORY, JOINED CATEGORY ID: {}", joinedCategogyCode);

		Map<String, Object> endCategory = endCtgService.selectCategory(joinedCategogyCode);
		if (endCategory != null) {
			return new ResponseEntity<>(endCategory, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(Collections.emptyMap(), HttpStatus.OK);
		}
	}
}
