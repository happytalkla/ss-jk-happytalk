package ht.service;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import ht.domain.MenuVO;
import ht.persistence.MenuDao;

@Service
public class MenuService {

	@Resource
	private MenuDao menuDao;

	/**
	 * 분류 목록 조회
	 */
	public List<MenuVO> selectMenuList(MenuVO vo) {
		return menuDao.selectMenuList(vo);
	}
}
