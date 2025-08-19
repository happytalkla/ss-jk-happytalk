package ht.mca.vo;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.ss.common.channel.mca.common.net.StringInputStream;
import com.ss.common.channel.mca.util.MCAUtil;
import com.ss.common.channel.mca.vo.VOSupport;
import com.ss.common.util.StringUtility;

public class Mah0368pVO extends VOSupport {

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
	public static class InRec1 {
		// Constructor
		public InRec1() {
			pText = null;
		}

		/* Attributes */
		private String ACNT_NO;			//계좌번호 C[20]
		private String CLNT_RRNO;		//고객주민등록번호 C[13]
		private String CLNT_ENTY_ID;	//고객엔티티ID C[19]
		
		private String pText;
		
		/* Getters/Setters */
		public void setData(
			String ACNT_NO,
			String CLNT_RRNO,
			String CLNT_ENTY_ID
 		) {
			this.ACNT_NO = ACNT_NO;
			this.CLNT_RRNO = CLNT_RRNO;
			this.CLNT_ENTY_ID = CLNT_ENTY_ID;
		}
		public String getPText() {
			return this.pText;
		}
		public void setPText(String pText) {
			this.pText = pText;
		}
		public String getACNT_NO() {
			return ACNT_NO;
		}
		public void setACNT_NO(String aCNT_NO) {
			ACNT_NO = aCNT_NO;
		}
		public String getCLNT_RRNO() {
			return CLNT_RRNO;
		}
		public void setCLNT_RRNO(String cLNT_RRNO) {
			CLNT_RRNO = cLNT_RRNO;
		}
		public String getCLNT_ENTY_ID() {
			return CLNT_ENTY_ID;
		}
		public void setCLNT_ENTY_ID(String cLNT_ENTY_ID) {
			CLNT_ENTY_ID = cLNT_ENTY_ID;
		}
		
		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			if (pText != null) {
				sb.append("[pText]"+pText);
			} else {
				sb.append("[ACNT_NO]"+ACNT_NO);
				sb.append(",");
				sb.append("[CLNT_RRNO]"+CLNT_RRNO);
				sb.append(",");
				sb.append("[CLNT_ENTY_ID]"+CLNT_ENTY_ID);
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
		private String CLNT_POPN_CTNT;		// 고객성향내용, C[300]	
		private String A_PUP_YN;			// 팝업여부, C[1]	

		/* Getters/Setters */
		public void setData(
			String CLNT_POPN_CTNT,
			String A_PUP_YN
		) {
			this.CLNT_POPN_CTNT = CLNT_POPN_CTNT;
			this.A_PUP_YN = A_PUP_YN;
		}
		public String getCLNT_POPN_CTNT() {
			return CLNT_POPN_CTNT;
		}
		public void setCLNT_POPN_CTNT(String cLNT_POPN_CTNT) {
			CLNT_POPN_CTNT = cLNT_POPN_CTNT;
		}
		public String getA_PUP_YN() {
			return A_PUP_YN;
		}
		public void setA_PUP_YN(String a_PUP_YN) {
			A_PUP_YN = a_PUP_YN;
		}
		
		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("[CLNT_POPN_CTNT]"+CLNT_POPN_CTNT);
			sb.append(",");
			sb.append("[A_PUP_YN]"+A_PUP_YN);
			sb.append("}");
			return sb.toString();
		}
		
		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("CLNT_POPN_CTNT",CLNT_POPN_CTNT);
			m.put("A_PUP_YN",A_PUP_YN);
			return m;
		}
	}

	/* constructor */
	public Mah0368pVO (String ipAddr, String userID, String mediaType) {
		setHeaderUserID(userID);
		setHeaderTrCode("mah0368p");
		setHeaderDestination((char)'6');
		setHeaderPacketDataType((char)0);
		setHeaderMediaType(mediaType);
		setHeaderIpAddr(ipAddr);
		setCharSet("1");
        setIsActive("0");		
	}

	/* constructor */
	public Mah0368pVO (String ipAddr, String userID, String mediaType, String brchCD, String deptCD, String acctCD) {
		setHeaderUserID(userID);
		setHeaderTrCode("mah0368p");
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
				MCAUtil.writeString(inRec1.ACNT_NO, 20, bos);
				MCAUtil.writeString(inRec1.CLNT_RRNO, 13, bos);
				MCAUtil.writeString(inRec1.CLNT_ENTY_ID, 19, bos);
			}
		}
		
		return bos.toByteArray();
	}
		
	/* parse */
	public void parse(StringInputStream sis) throws Exception {
		
		int cntRecord=0;

		// OutRec1
		outRec1.CLNT_POPN_CTNT = sis.readString(300);
		outRec1.A_PUP_YN = sis.readString(1);

	}
	/* validate */
	public boolean validate() throws Exception {
		//implement here
		return true;
	}

	/* toString */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[inRec1]\n");
		sb.append("\t"+inRec1.toString()+"\n");
		sb.append("\n");
		
		sb.append("[outRec1]\n");
		sb.append("\t"+outRec1.toString()+"\n");

		return sb.toString();
	}

	/* toMap */
	public Map<String, Object> toMap() {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("outRec1",outRec1.toMap());
		
		return m;
	}
	
}
