package ht.domain;

import lombok.Data;

@Data
/**
 * 접근 가능한 IP를 관리한다.
 * @author TD5242
 *
 */
public class IpManage {
	
	private String ipList = "10.45.48.26"
			+ ",10.45.48.27"
			+ ",10.45.48.28"
			+ ",10.45.48.29";
}
