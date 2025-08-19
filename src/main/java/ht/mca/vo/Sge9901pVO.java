/*
* This class is automatically generated.
* Please do not modify the original source.
* 
* Supervisor      : 모승환
* Last modified date  : 2014.03.05
* VOGenerator     : v2.2.4
*
* Release info    :
* v2.2.3   	20140305  - toMap 메서드 수정
*/

package ht.mca.vo;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ss.common.channel.mca.common.net.StringInputStream;
import com.ss.common.channel.mca.util.MCAUtil;
import com.ss.common.channel.mca.vo.VOSupport;
import com.ss.common.util.StringUtility;

import lombok.Data;

/**
 * Tran Code     : Sge9901p
 * Tran Name     : Entity ID 고객번호 변환
 * Tran Type     : 0
 * Domain ID     : 
 * Target Server : 6
 * Customization : FALSE
 */
public class Sge9901pVO extends VOSupport {
	
	/* Input Records */
	private InRec1 inRec1 = new InRec1();

	/* Output Records */
	private OutRec1 outRec1 = new OutRec1();
	
	/* Get Instances */
	public InRec1 getInRec1() { return inRec1; }
	public OutRec1 getOutRec1() { return outRec1; }

	/* Set Instances */
	public void setInRec1(InRec1 inRec1) { this.inRec1 = inRec1; }
	public void setOutRec1(OutRec1 outRec1) { this.outRec1 = outRec1; }

	/* InRec Classes */	
	@Data
	public static class InRec1 {

		// Constructor
		public InRec1() {
			pText = null;
		}

		/* Attributes */
		private String A_PRCE_SECT_CODE;			// 조회구분코드, CHAR(1)	
		private String RBNO;			// 별칭명, VARCHAR(150)	
		private String ENTY_ID;			// 엔티티ID, BIGINT(19)	
		private String ACNT_NO;
			

		private String pText;			

	}

	/* OutRec Classes */
	@Data
	public static class OutRec1 { 
	
		// Constructor
		public OutRec1() {
		}

		/* Attributes */
		private String CLNT_NAME;			// 고객명, CHAR(90)	
		private String RBNO;			// 주민번호, CHAR(13)	
		private String ENTY_ID;			// 고객번호, CHAR(19)	

		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("CLNT_NAME",CLNT_NAME);
			m.put("RBNO",RBNO);
			m.put("ENTY_ID",ENTY_ID);
			return m;
		}
	}


	/* constructor */
	public Sge9901pVO (String ipAddr, String userID, String mediaType) {
		setHeaderUserID(userID);
		setHeaderTrCode("sge9901p");
		setHeaderDestination((char)'6');
		setHeaderPacketDataType((char)0);
		setHeaderMediaType(mediaType);
		setHeaderIpAddr(ipAddr);
		setCharSet("1");
        setIsActive("0");		
	}

	/* constructor */
	public Sge9901pVO (String ipAddr, String userID, String mediaType, String brchCD, String deptCD, String acctCD) {
		setHeaderUserID(userID);
		setHeaderTrCode("sge9901p");
		setHeaderDestination((char)'6');
		setHeaderPacketDataType((char)0);
		setHeaderMediaType(mediaType);
		setHeaderIpAddr(ipAddr);
		setHeaderBrchCD(brchCD);
		setHeaderDeptCD(deptCD);
		setHeaderAcctCD(acctCD);
		setCharSet("1");
        setIsActive("0");		
	}

	/* getBytes */
	public byte[] getBytes() throws Exception {
	
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
	
		if (needCert()) {
   			String PubKeyLn = StringUtility.lPad(Integer.toString(getPubKey().length()), 4, '0');
   			MCAUtil.writeString(PubKeyLn + getPubKey(), 4 + getPubKey().length(), bos);
   			String RtnLn = StringUtility.lPad(Integer.toString(getRtn().length()), 4, '0');
   			MCAUtil.writeString(RtnLn + getRtn(), 4 + getRtn().length(), bos);			
			initHeader(bos);
			MCAUtil.writeString(getPtext(), getPtext().getBytes().length, bos);
		} else {
			initHeader(bos);
			if (inRec1.getPText() != null) {
				MCAUtil.writeString(inRec1.getPText(), inRec1.getPText().getBytes().length, bos);
			} else {
				MCAUtil.writeString(inRec1.A_PRCE_SECT_CODE, 1, bos);
				MCAUtil.writeString(inRec1.RBNO, 13, bos);
				MCAUtil.writeString(inRec1.ENTY_ID, 19, bos);
				MCAUtil.writeString(inRec1.ACNT_NO, 20, bos);
			}
		}
		
		return bos.toByteArray();
	}
		
	/* parse */
	public void parse(StringInputStream sis) throws Exception {
			outRec1.CLNT_NAME = sis.readString(90);
			outRec1.RBNO = sis.readString(13);
			outRec1.ENTY_ID = sis.readString(19);
	}
	
	/* validate */
	public boolean validate() throws Exception {
		//implement here
		return true;
	}


	/* toMap */
	public Map<String, Object> toMap() {
		Map<String, Object> m = new HashMap<String, Object>();

		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
/*
		for (int i=0;i<outRec1.size();i++) {
			list1.add(outRec1.get(i).toMap());
		}
*/
		m.put("outRec1", outRec1);
/*
		List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();

		for (int i=0;i<outRec2.size();i++) {
			list2.add(outRec2.get(i).toMap());
		}
*/		


		return m;
	}
}
