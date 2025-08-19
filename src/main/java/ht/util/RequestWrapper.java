package ht.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class RequestWrapper extends HttpServletRequestWrapper {
	private Map<String, String[]> parameterMap;

	public RequestWrapper(HttpServletRequest request) {
		super(request);
		parameterMap = new HashMap<String, String[]>(request.getParameterMap());
	}

	@Override
	public String getParameter(String name) {
		String[] strings = getParameterMap().get(name);
		if(strings != null && strings.length > 0) {
			return strings[0];
		}
		return super.getParameter(name);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		// parameter replace 후 request에 등록
		for(String key : parameterMap.keySet()) {
			String[] values = parameterMap.get(key);
			if(values != null && values.length > 0) {
				String[] newValues = StringUtil.replaceHtmlTag(values);
				parameterMap.put(key, newValues);
			}
		}

		return parameterMap;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(getParameterMap().keySet());
	}

	@Override
	public String[] getParameterValues(String name) {
		return getParameterMap().get(name);
	}

}
