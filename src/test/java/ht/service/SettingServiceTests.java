package ht.service;

import lombok.extern.slf4j.Slf4j;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
@ActiveProfiles({"local"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SettingServiceTests {

	@Resource
	private SettingService settingService;

	@Test
	public void selectSiteSetting() {

		Map<String, Object> siteSettings = settingService.selectSiteSetting();
		log.info("siteSettings: {}", siteSettings);
		assertThat(siteSettings.get("cnsr_max_cnt")).isNotNull();
	}
}
