/*
* This class is automatically generated.
* Please do not modify the original source.
* 
* Supervisor      : 모승환, 이승필, 이승우
* Last modified date  : 2014. 01.16
* VOGenerator     : v2.2.0
*
* Release info    :
* v2.2.0   	20140116  - LangType -> MediaType으로 변경
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
 * Tran Code     : hfca_oa_105aq01
 * Tran Name     : HTS 약정/해지상태 조회
 * Tran Type     : 0
 * Domain ID     : HFCA
 * Target Server : H
 * Customization : FALSE
 */
public class Hfca_oa_105aq01VO extends VOSupport {
	
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
		private String A_RefrSect;			// 조회구분, CHAR(1)				
		private String A_AcctNo20;			// 계좌번호20, CHAR(20)				
		private String CustNo;			// 고객번호, CHAR(13)				
		private String UserID;			// 사용자ID, CHAR(16)				

		private String pText;
		
		/* Getters/Setters */
		public void setData(
			String A_RefrSect,
			String A_AcctNo20,
			String CustNo,
			String UserID
 		) {
			this.A_RefrSect = A_RefrSect;
			this.A_AcctNo20 = A_AcctNo20;
			this.CustNo = CustNo;
			this.UserID = UserID;
		}
			

		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			if (pText != null) {
				sb.append("[pText]"+pText);
			} else {
				sb.append("[A_RefrSect]"+A_RefrSect);
				sb.append(",");
				sb.append("[A_AcctNo20]"+A_AcctNo20);
				sb.append(",");
				sb.append("[CustNo]"+CustNo);
				sb.append(",");
				sb.append("[UserID]"+UserID);
			}
			sb.append("}");
			return sb.toString();
		}
	}

	/* OutRec Classes */
	@Data
	public static class OutRec1 { 
	
		// Constructor
		public OutRec1() {
		}

		/* Attributes */
		private String UserID;			// 사용자ID, CHAR(16)	
		private String HTSSectNm;			// HTS구분명, VARCHAR(20)	
		private String HTSDtailSectNm;			// HTS상세구분명, VARCHAR(40)	

		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("[UserID]"+UserID);
			sb.append(",");
			sb.append("[HTSSectNm]"+HTSSectNm);
			sb.append(",");
			sb.append("[HTSDtailSectNm]"+HTSDtailSectNm);
			sb.append("}");
			return sb.toString();
		}

		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("UserID",UserID);
			m.put("HTSSectNm",HTSSectNm);
			m.put("HTSDtailSectNm",HTSDtailSectNm);
			return m;
		}
	}

	/* constructor */
	public Hfca_oa_105aq01VO (String ipAddr, String userID, String mediaType) {
		setHeaderUserID(userID);
		setHeaderTrCode("hfca_oa_105aq01");
		setHeaderDestination((char) '6');
		setHeaderPacketDataType((char) 0);
		setHeaderMediaType(mediaType);
		setHeaderIpAddr(ipAddr);
		setCharSet("1");
		setIsActive("0");
	}

	/* constructor */
	public Hfca_oa_105aq01VO (String ipAddr, String userID, String mediaType, String brchCD, String deptCD, String acctCD) {
		setHeaderUserID(userID);
		setHeaderTrCode("hfca_oa_105aq01");
		setHeaderDestination((char) '6');
		setHeaderPacketDataType((char) 0);
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
				MCAUtil.writeString(inRec1.A_RefrSect, 1, bos);
				MCAUtil.writeString(inRec1.A_AcctNo20, 20, bos);
				MCAUtil.writeString(inRec1.CustNo, 13, bos);
				MCAUtil.writeString(inRec1.UserID, 16, bos);
			}
		}
		
		return bos.toByteArray();
	}
		
	/* parse */
	public void parse(StringInputStream sis) throws Exception {
		
		//int cntRecord=0;

		// OutRec1
		/*
		try {
			cntRecord=Integer.parseInt(sis.readString(4).trim());
		} catch (NumberFormatException e) { }
		*/
		
		//if (cntRecord < 0) cntRecord = 0;
		//System.out.println(">>>>>>>>>>>>>>>>BufLength : " + sis.getBufLength());
		try {
		//for (int i=0;i<cntRecord;i++) {
			//OutRec1 outRecord = new OutRec1();
			outRec1.UserID = sis.readString(16);
			outRec1.HTSSectNm = sis.readString(20);
			outRec1.HTSDtailSectNm = sis.readString(40);
		//	outRec1.add(outRecord);
		//}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/* validate */
	public boolean validate() throws Exception {
		//implement here
		return true;
	}


	/* toMap */
	public Map<String, Object> toMap() {
		return outRec1.toMap();
	}
}
