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

/**
 * Tran Code     : bsu0200p
 * Tran Name     : MDM 조회(폐쇄이관고객)
 * Tran Type     : 0
 * Domain ID     : BSU
 * Target Server : 6
 * Customization : FALSE
 */
public class Bsu0200pVO extends VOSupport {
	
	/* Input Records */
	private InRec1 inRec1 = new InRec1();

	/* Output Records */
	private OutRec1 outRec1 = new OutRec1();
	private OutRec2 outRec2 = new OutRec2();
	
	/* Get Instances */
	public InRec1 getInRec1() { return inRec1; }
	public OutRec1 getOutRec1() { return outRec1; }
	public OutRec2 getOutRec2(){ return outRec2; }

	/* Set Instances */
	public void setInRec1(InRec1 inRec1) { this.inRec1 = inRec1; }
	public void setOutRec1(OutRec1 outRec1) { this.outRec1 = outRec1; }
	public void setOutRec2(OutRec2 outRec2) { this.outRec2 = outRec2; }

	/* InRec Classes */	
	public static class InRec1 {

		// Constructor
		public InRec1() {
			pText = null;
		}

		/* Attributes */
		private String A_RFRN_SECT_CODE;			// 조회구분코드, CHAR(1)				
		private String ENTY_ID;			// 엔티티ID, BIGINT(19)				
		private String ALIS_NAME;			// 별칭명, VARCHAR(150)				

		private String pText;
		
		/* Getters/Setters */
		public void setData(
			String A_RFRN_SECT_CODE,
			String ENTY_ID,
			String ALIS_NAME
 		) {
			this.A_RFRN_SECT_CODE = A_RFRN_SECT_CODE;
			this.ENTY_ID = ENTY_ID;
			this.ALIS_NAME = ALIS_NAME;
		}
		public String getPText() {
			return this.pText;
		}
		public void setPText(String pText) {
			this.pText = pText;
		}
		public String getA_RFRN_SECT_CODE() { return A_RFRN_SECT_CODE; }
		public void setA_RFRN_SECT_CODE(String A_RFRN_SECT_CODE) { this.A_RFRN_SECT_CODE = A_RFRN_SECT_CODE; }				
		public String getENTY_ID() { return ENTY_ID; }
		public void setENTY_ID(String ENTY_ID) { this.ENTY_ID = ENTY_ID; }				
		public String getALIS_NAME() { return ALIS_NAME; }
		public void setALIS_NAME(String ALIS_NAME) { this.ALIS_NAME = ALIS_NAME; }				

		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			if (pText != null) {
				sb.append("[pText]"+pText);
			} else {
				sb.append("[A_RFRN_SECT_CODE]"+A_RFRN_SECT_CODE);
				sb.append(",");
				sb.append("[ENTY_ID]"+ENTY_ID);
				sb.append(",");
				sb.append("[ALIS_NAME]"+ALIS_NAME);
			}
			sb.append("}");
			return sb.toString();
		}
	}

	/* OutRec Classes */
	public static class OutRec1 { 
	
		// Constructor
		public OutRec1() {
		}

		/* Attributes */
		private String ACNT_ID;			// 계좌ID, BIGINT(19)	
		private String ACNT_NO;			// 계좌번호, CHAR(20)	
		private String ACNT_ALIS;			// 계좌별칭, CHAR(20)	
		private String ACNT_OWNR_ID;			// 계좌주ID, BIGINT(19)	
		private String ACNT_NAME;			// 계좌명, VARCHAR(90)	
		private String ACNT_TYPE_CODE;			// 계좌유형코드, CHAR(4)	
		private String A_ACNT_TYPE_CODE_NAME;			// 계좌유형코드명, CHAR(30)	
		private String ACNT_TYPE_NAME;			// 계좌유형명, VARCHAR(150)	
		private String ACNT_ALIS_TYPE_CODE;			// 계좌별칭유형코드, CHAR(4)	
		private String CMN_CDVL_NAME;			// 공통코드값명, VARCHAR(300)	

		/* Getters/Setters */
		public void setData(
			String ACNT_ID,
			String ACNT_NO,
			String ACNT_ALIS,
			String ACNT_OWNR_ID,
			String ACNT_NAME,
			String ACNT_TYPE_CODE,
			String A_ACNT_TYPE_CODE_NAME,
			String ACNT_TYPE_NAME,
			String ACNT_ALIS_TYPE_CODE,
			String CMN_CDVL_NAME
		 ) {
			this.ACNT_ID = ACNT_ID;
			this.ACNT_NO = ACNT_NO;
			this.ACNT_ALIS = ACNT_ALIS;
			this.ACNT_OWNR_ID = ACNT_OWNR_ID;
			this.ACNT_NAME = ACNT_NAME;
			this.ACNT_TYPE_CODE = ACNT_TYPE_CODE;
			this.A_ACNT_TYPE_CODE_NAME = A_ACNT_TYPE_CODE_NAME;
			this.ACNT_TYPE_NAME = ACNT_TYPE_NAME;
			this.ACNT_ALIS_TYPE_CODE = ACNT_ALIS_TYPE_CODE;
			this.CMN_CDVL_NAME = CMN_CDVL_NAME;
		}
		public String getACNT_ID() { return ACNT_ID; }
		public void setACNT_ID(String ACNT_ID) { this.ACNT_ID = ACNT_ID; }				
		public String getACNT_NO() { return ACNT_NO; }
		public void setACNT_NO(String ACNT_NO) { this.ACNT_NO = ACNT_NO; }				
		public String getACNT_ALIS() { return ACNT_ALIS; }
		public void setACNT_ALIS(String ACNT_ALIS) { this.ACNT_ALIS = ACNT_ALIS; }				
		public String getACNT_OWNR_ID() { return ACNT_OWNR_ID; }
		public void setACNT_OWNR_ID(String ACNT_OWNR_ID) { this.ACNT_OWNR_ID = ACNT_OWNR_ID; }				
		public String getACNT_NAME() { return ACNT_NAME; }
		public void setACNT_NAME(String ACNT_NAME) { this.ACNT_NAME = ACNT_NAME; }				
		public String getACNT_TYPE_CODE() { return ACNT_TYPE_CODE; }
		public void setACNT_TYPE_CODE(String ACNT_TYPE_CODE) { this.ACNT_TYPE_CODE = ACNT_TYPE_CODE; }				
		public String getA_ACNT_TYPE_CODE_NAME() { return A_ACNT_TYPE_CODE_NAME; }
		public void setA_ACNT_TYPE_CODE_NAME(String A_ACNT_TYPE_CODE_NAME) { this.A_ACNT_TYPE_CODE_NAME = A_ACNT_TYPE_CODE_NAME; }				
		public String getACNT_TYPE_NAME() { return ACNT_TYPE_NAME; }
		public void setACNT_TYPE_NAME(String ACNT_TYPE_NAME) { this.ACNT_TYPE_NAME = ACNT_TYPE_NAME; }				
		public String getACNT_ALIS_TYPE_CODE() { return ACNT_ALIS_TYPE_CODE; }
		public void setACNT_ALIS_TYPE_CODE(String ACNT_ALIS_TYPE_CODE) { this.ACNT_ALIS_TYPE_CODE = ACNT_ALIS_TYPE_CODE; }				
		public String getCMN_CDVL_NAME() { return CMN_CDVL_NAME; }
		public void setCMN_CDVL_NAME(String CMN_CDVL_NAME) { this.CMN_CDVL_NAME = CMN_CDVL_NAME; }				

		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("[ACNT_ID]"+ACNT_ID);
			sb.append(",");
			sb.append("[ACNT_NO]"+ACNT_NO);
			sb.append(",");
			sb.append("[ACNT_ALIS]"+ACNT_ALIS);
			sb.append(",");
			sb.append("[ACNT_OWNR_ID]"+ACNT_OWNR_ID);
			sb.append(",");
			sb.append("[ACNT_NAME]"+ACNT_NAME);
			sb.append(",");
			sb.append("[ACNT_TYPE_CODE]"+ACNT_TYPE_CODE);
			sb.append(",");
			sb.append("[A_ACNT_TYPE_CODE_NAME]"+A_ACNT_TYPE_CODE_NAME);
			sb.append(",");
			sb.append("[ACNT_TYPE_NAME]"+ACNT_TYPE_NAME);
			sb.append(",");
			sb.append("[ACNT_ALIS_TYPE_CODE]"+ACNT_ALIS_TYPE_CODE);
			sb.append(",");
			sb.append("[CMN_CDVL_NAME]"+CMN_CDVL_NAME);
			sb.append("}");
			return sb.toString();
		}

		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("ACNT_ID",ACNT_ID);
			m.put("ACNT_NO",ACNT_NO);
			m.put("ACNT_ALIS",ACNT_ALIS);
			m.put("ACNT_OWNR_ID",ACNT_OWNR_ID);
			m.put("ACNT_NAME",ACNT_NAME);
			m.put("ACNT_TYPE_CODE",ACNT_TYPE_CODE);
			m.put("A_ACNT_TYPE_CODE_NAME",A_ACNT_TYPE_CODE_NAME);
			m.put("ACNT_TYPE_NAME",ACNT_TYPE_NAME);
			m.put("ACNT_ALIS_TYPE_CODE",ACNT_ALIS_TYPE_CODE);
			m.put("CMN_CDVL_NAME",CMN_CDVL_NAME);
			return m;
		}
	}

	public static class OutRec2 { 
	
		// Constructor
		public OutRec2() {
		}

		/* Attributes */
		private String ENTY_ID;			// 엔티티ID, BIGINT(19)	
		private String ENTY_ALIS_TYPE_CODE;			// 엔티티별칭유형코드, CHAR(4)	
		private String CMN_CDVL_ENNM;			// 공통코드값영문명, VARCHAR(300)	
		private String ENTY_ALIS;			// 엔티티별칭, CHAR(20)	
		private String ENTY_NAME;			// 엔티티명, VARCHAR(300)	

		/* Getters/Setters */
		public void setData(
			String ENTY_ID,
			String ENTY_ALIS_TYPE_CODE,
			String CMN_CDVL_ENNM,
			String ENTY_ALIS,
			String ENTY_NAME
		 ) {
			this.ENTY_ID = ENTY_ID;
			this.ENTY_ALIS_TYPE_CODE = ENTY_ALIS_TYPE_CODE;
			this.CMN_CDVL_ENNM = CMN_CDVL_ENNM;
			this.ENTY_ALIS = ENTY_ALIS;
			this.ENTY_NAME = ENTY_NAME;
		}
		public String getENTY_ID() { return ENTY_ID; }
		public void setENTY_ID(String ENTY_ID) { this.ENTY_ID = ENTY_ID; }				
		public String getENTY_ALIS_TYPE_CODE() { return ENTY_ALIS_TYPE_CODE; }
		public void setENTY_ALIS_TYPE_CODE(String ENTY_ALIS_TYPE_CODE) { this.ENTY_ALIS_TYPE_CODE = ENTY_ALIS_TYPE_CODE; }				
		public String getCMN_CDVL_ENNM() { return CMN_CDVL_ENNM; }
		public void setCMN_CDVL_ENNM(String CMN_CDVL_ENNM) { this.CMN_CDVL_ENNM = CMN_CDVL_ENNM; }				
		public String getENTY_ALIS() { return ENTY_ALIS; }
		public void setENTY_ALIS(String ENTY_ALIS) { this.ENTY_ALIS = ENTY_ALIS; }				
		public String getENTY_NAME() { return ENTY_NAME; }
		public void setENTY_NAME(String ENTY_NAME) { this.ENTY_NAME = ENTY_NAME; }				

		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("[ENTY_ID]"+ENTY_ID);
			sb.append(",");
			sb.append("[ENTY_ALIS_TYPE_CODE]"+ENTY_ALIS_TYPE_CODE);
			sb.append(",");
			sb.append("[CMN_CDVL_ENNM]"+CMN_CDVL_ENNM);
			sb.append(",");
			sb.append("[ENTY_ALIS]"+ENTY_ALIS);
			sb.append(",");
			sb.append("[ENTY_NAME]"+ENTY_NAME);
			sb.append("}");
			return sb.toString();
		}

		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("ENTY_ID",ENTY_ID);
			m.put("ENTY_ALIS_TYPE_CODE",ENTY_ALIS_TYPE_CODE);
			m.put("CMN_CDVL_ENNM",CMN_CDVL_ENNM);
			m.put("ENTY_ALIS",ENTY_ALIS);
			m.put("ENTY_NAME",ENTY_NAME);
			return m;
		}
	}

	/* constructor */
	public Bsu0200pVO (String ipAddr, String userID, String mediaType) {
		setHeaderUserID(userID);
		setHeaderTrCode("bsu0200p");
		setHeaderDestination((char)'6');
		setHeaderPacketDataType((char)0);
		setHeaderMediaType(mediaType);
		setHeaderIpAddr(ipAddr);
		setCharSet("1");
        setIsActive("0");		
	}

	/* constructor */
	public Bsu0200pVO (String ipAddr, String userID, String mediaType, String brchCD, String deptCD, String acctCD) {
		setHeaderUserID(userID);
		setHeaderTrCode("bsu0200p");
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
				MCAUtil.writeString(inRec1.A_RFRN_SECT_CODE, 1, bos);
				MCAUtil.writeString(inRec1.ENTY_ID, 19, bos);
				MCAUtil.writeString(inRec1.ALIS_NAME, 150, bos);
			}
		}
		
		return bos.toByteArray();
	}
		
	/* parse */
	public void parse(StringInputStream sis) throws Exception {
		int cntRecord = 0;

			sis.readString(4).trim();

			//OutRec1 outRecord = new OutRec1();
			//OutRec2 outRecord2 = new OutRec2();
			outRec1.ACNT_ID = sis.readString(19);
			outRec1.ACNT_NO = sis.readString(20);
			outRec1.ACNT_ALIS = sis.readString(20);
			outRec1.ACNT_OWNR_ID = sis.readString(19);
			outRec1.ACNT_NAME = sis.readString(90);
			outRec1.ACNT_TYPE_CODE = sis.readString(4);
			outRec1.A_ACNT_TYPE_CODE_NAME = sis.readString(30);
//			outRec1.ACNT_TYPE_NAME = sis.readString(150);
			outRec1.ACNT_ALIS_TYPE_CODE = sis.readString(4);
			//outRec1.CMN_CDVL_NAME = sis.readString(300);
			
			outRec2.ENTY_ID = sis.readString(19);
			outRec2.ENTY_ALIS_TYPE_CODE = sis.readString(4);
			outRec2.CMN_CDVL_ENNM = sis.readString(300);
			outRec2.ENTY_ALIS = sis.readString(20);
			outRec2.ENTY_NAME = sis.readString(300);
			
			//outRecord.CMN_CDVL_NAME = sis.readString(300);
			
			//outRec1.add(outRecord);
	
			//outRec2.add(outRecord2);

			System.out.println(">>>>>>>>>>>>>>readString outRec1: " + outRec1);
			System.out.println(">>>>>>>>>>>>>>readString outRec2: " + outRec2);


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
		m.put("outRec2",outRec2);

		return m;
	}
}
