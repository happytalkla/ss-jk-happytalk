package ht.domain;

import java.io.Serializable;

import org.apache.commons.collections4.map.CaseInsensitiveMap;

/**
 * MyBatis 에서 맵으로 받는 경우, 컬럼명의 대소문자를 무시하기 위해 사용됨
 *
 * @param <K>
 * @param <V>
 */
public class CMap<K, V> extends CaseInsensitiveMap<K, V> implements Serializable {

	private static final long serialVersionUID = 1L;
}
