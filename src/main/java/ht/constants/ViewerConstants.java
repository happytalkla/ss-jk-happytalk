package ht.constants;

import static ht.constants.CommonConstants.*;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author KB099
 * @formatter:off
 */
public class ViewerConstants {

	public static final Map<String, String> COUNSELOR_CUSTOMER_HISTORY = new HashMap<String, String>();
	static {
		COUNSELOR_CUSTOMER_HISTORY.put(CSTM_LINK_DIV_CD_A, "해피톡");
		COUNSELOR_CUSTOMER_HISTORY.put(CSTM_LINK_DIV_CD_B, "카카오톡");
		COUNSELOR_CUSTOMER_HISTORY.put(CSTM_LINK_DIV_CD_C, "O2 Talk");
		COUNSELOR_CUSTOMER_HISTORY.put(CSTM_LINK_DIV_CD_D, "mPOP Talk");
	}
}
