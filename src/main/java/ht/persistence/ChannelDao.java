package ht.persistence;

import ht.domain.channel.ChannelRoom;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

@Repository
public class ChannelDao {

	@Resource
	private SqlSession sqlSession;

	/**
	 * 채널방 조회
	 */
	public ChannelRoom selectChannelRoom(Map<String, Object> sqlParams) {

		return sqlSession.selectOne("channel.selectChannelRoom", sqlParams);
	}

	/**
	 * 채널방 등록
	 */
	public void insertChannelRoom(ChannelRoom channelRoom) {

		sqlSession.insert("channel.insertChannelRoom", channelRoom);
	}

	/**
	 * 채널방 수정
	 */
	public void updateChannelRoom(ChannelRoom channelRoom) {

		sqlSession.update("channel.updateChannelRoom", channelRoom);
	}

	/**
	 * 채널방 삭제
	 */
	public void deleteChannelRoom(ChannelRoom channelRoom) {

		sqlSession.delete("channel.deleteChannelRoom", channelRoom);
	}
}
