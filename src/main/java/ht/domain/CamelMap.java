package ht.domain;

import java.util.HashMap;

import org.springframework.jdbc.support.JdbcUtils;

@SuppressWarnings("rawtypes")
public class CamelMap extends HashMap {

	private static final long serialVersionUID = 363562512382644497L;

	@SuppressWarnings("unchecked")
	@Override
	public Object put(Object key, Object value) {
		String k = (String) key;
		if (k.indexOf("_") > -1 || k.toUpperCase().equals(k)) {
			return super.put(JdbcUtils.convertUnderscoreNameToPropertyName(k), value);
		} else {
			return super.put(k, value);
		}
	}

}
