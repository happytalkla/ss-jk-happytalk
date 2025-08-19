package ht.persistence;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;

import ht.domain.MenuVO;

@Repository
public class MenuDao {

	@Resource
	private SqlSession sqlSession;

	/**
	 * 메뉴 목록 조회
	 */
	public List<MenuVO> selectMenuList(MenuVO vo) {
		return sqlSession.selectList("menu.selectMenuList", vo);
	}


}
