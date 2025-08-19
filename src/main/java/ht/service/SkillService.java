package ht.service;

import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import ht.persistence.SkillDao;

@Service
public class SkillService {

	@Resource
	private SkillDao dao;


	/**
	 * O2app 의 화면 이름 가져오기
	 */
	public Map<String, Object> selectO2appName(Map<String, Object> param){
		Map<String, Object> map = dao.selectO2appName(param);
		return map;
	}

}
