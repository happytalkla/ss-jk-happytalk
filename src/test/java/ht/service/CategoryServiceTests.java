package ht.service;

import ht.constants.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import javax.annotation.Resource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@ActiveProfiles("local")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CategoryServiceTests {

	@Resource
	private SettingService settingService;
	@Resource
	private CategoryService categoryService;

	@Test
	public void selectDefaultCategory() throws Exception {

		Map<String, Object> category = categoryService.selectDefaultCategoryByChannel("A");
		log.info("category: {}", category);
		assertThat(category).isNotNull();
		assertThat(category.get("dft_ctg_yn")).isEqualTo("Y");
		assertThat(category.get("depart_cd")).isEqualTo("CS");
		assertThat(category.get("ctg_num")).isNotNull();

		category = categoryService.selectDefaultCategory(CommonConstants.DEPART_CD_CS);
		log.info("category: {}", category);
		assertThat(category).isNotNull();
		assertThat(category.get("dft_ctg_yn")).isEqualTo("Y");
		assertThat(category.get("depart_cd")).isEqualTo("CS");
		assertThat(category.get("ctg_num")).isNotNull();

		category = categoryService.selectDefaultCategory(CommonConstants.DEPART_CD_TM);
		log.info("category: {}", category);
		assertThat(category).isNotNull();
		assertThat(category.get("dft_ctg_yn")).isEqualTo("Y");
		assertThat(category.get("depart_cd")).isEqualTo("TM");
		assertThat(category.get("ctg_num")).isNotNull();
	}
/*
	@Test
	public void selectDefaultCtgNum() throws Exception {
		String ctgNum = categoryService.selectDefaultCtgNum();
		log.info("ctgNum: {}", ctgNum);
		assertThat(ctgNum).isNotNull();
		assertThat(ctgNum).isEqualTo("9999999999");

		ctgNum = categoryService.selectDefaultCtgNum(CommonConstants.DEPART_CD_CS);
		log.info("ctgNum: {}", ctgNum);
		assertThat(ctgNum).isNotNull();
		assertThat(ctgNum).isEqualTo("9999999999");

		ctgNum = categoryService.selectDefaultCtgNum(CommonConstants.DEPART_CD_TM);
		log.info("ctgNum: {}", ctgNum);
		assertThat(ctgNum).isNotNull();
		assertThat(ctgNum).isEqualTo("9999999998");
	}
	*/
}
