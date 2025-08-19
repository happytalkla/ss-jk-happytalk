package ht.mca.vo;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.ss.common.channel.mca.common.net.StringInputStream;
import com.ss.common.channel.mca.util.MCAUtil;
import com.ss.common.channel.mca.vo.VOSupport;
import com.ss.common.util.StringUtility;

public class Sdb1624pVO extends VOSupport {
	
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
		private String CLNT_ENTY_ID;			//고객엔티티ID

		private String pText;
		
		/* Getters/Setters */
		public void setData(
			String CLNT_ENTY_ID
 		) {
			this.CLNT_ENTY_ID = CLNT_ENTY_ID;
		}
		public String getPText() {
			return this.pText;
		}
		public void setPText(String pText) {
			this.pText = pText;
		}
		
		public String getCLNT_ENTY_ID() { return CLNT_ENTY_ID; }
		public void setCLNT_ENTY_ID(String CLNT_ENTY_ID) { this.CLNT_ENTY_ID = CLNT_ENTY_ID; }				

		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			if (pText != null) {
				sb.append("[pText]"+pText);
			} else {
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
		private String A_CLNT_CLSS_CODE1;			// 고객등급코드, C[1]	
		private String PRIM_SVC_XPRT_YYMM;			// 우대서비스만료년월, C[6]	
		private String CLNT_CLSN_NAME;				// PHC Membership 분류명, C[30]	
		private String FEE_SUCL_YN;					// 수수료우수고객여부, C[1]	
		private String A_CLNT_CLSS_NAME;			// 맞춤서비스등급, C[90]	
		private String FMLY_UNFC_RTNG_TAGT_YN;		// 가족통합등급대상여부, C[1]	
		

		/* Getters/Setters */
		public void setData(
			String A_CLNT_CLSS_CODE1,
			String PRIM_SVC_XPRT_YYMM,
			String CLNT_CLSN_NAME,
			String FEE_SUCL_YN,
			String A_CLNT_CLSS_NAME,
			String FMLY_UNFC_RTNG_TAGT_YN
		) {
			this.A_CLNT_CLSS_CODE1 = A_CLNT_CLSS_CODE1;
			this.PRIM_SVC_XPRT_YYMM = PRIM_SVC_XPRT_YYMM;
			this.CLNT_CLSN_NAME = CLNT_CLSN_NAME;
			this.FEE_SUCL_YN = FEE_SUCL_YN;
			this.A_CLNT_CLSS_NAME = A_CLNT_CLSS_NAME;
			this.FMLY_UNFC_RTNG_TAGT_YN = FMLY_UNFC_RTNG_TAGT_YN;
		}


		public String getA_CLNT_CLSS_CODE1() {
			return A_CLNT_CLSS_CODE1;
		}
		public void setA_CLNT_CLSS_CODE1(String a_CLNT_CLSS_CODE1) {
			A_CLNT_CLSS_CODE1 = a_CLNT_CLSS_CODE1;
		}
		public String getPRIM_SVC_XPRT_YYMM() {
			return PRIM_SVC_XPRT_YYMM;
		}
		public void setPRIM_SVC_XPRT_YYMM(String pRIM_SVC_XPRT_YYMM) {
			PRIM_SVC_XPRT_YYMM = pRIM_SVC_XPRT_YYMM;
		}
		public String getCLNT_CLSN_NAME() {
			return CLNT_CLSN_NAME;
		}
		public void setCLNT_CLSN_NAME(String cLNT_CLSN_NAME) {
			CLNT_CLSN_NAME = cLNT_CLSN_NAME;
		}
		public String getFEE_SUCL_YN() {
			return FEE_SUCL_YN;
		}
		public void setFEE_SUCL_YN(String fEE_SUCL_YN) {
			FEE_SUCL_YN = fEE_SUCL_YN;
		}
		public String getA_CLNT_CLSS_NAME() {
			return A_CLNT_CLSS_NAME;
		}
		public void setA_CLNT_CLSS_NAME(String a_CLNT_CLSS_NAME) {
			A_CLNT_CLSS_NAME = a_CLNT_CLSS_NAME;
		}
		public String getFMLY_UNFC_RTNG_TAGT_YN() {
			return FMLY_UNFC_RTNG_TAGT_YN;
		}
		public void setFMLY_UNFC_RTNG_TAGT_YN(String fMLY_UNFC_RTNG_TAGT_YN) {
			FMLY_UNFC_RTNG_TAGT_YN = fMLY_UNFC_RTNG_TAGT_YN;
		}
		
		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("[A_CLNT_CLSS_CODE1]"+A_CLNT_CLSS_CODE1);
			sb.append(",");
			sb.append("[PRIM_SVC_XPRT_YYMM]"+PRIM_SVC_XPRT_YYMM);
			sb.append(",");
			sb.append("[CLNT_CLSN_NAME]"+CLNT_CLSN_NAME);
			sb.append(",");
			sb.append("[FEE_SUCL_YN]"+FEE_SUCL_YN);
			sb.append(",");
			sb.append("[A_CLNT_CLSS_NAME]"+A_CLNT_CLSS_NAME);
			sb.append(",");
			sb.append("[FMLY_UNFC_RTNG_TAGT_YN]"+FMLY_UNFC_RTNG_TAGT_YN);
			sb.append("}");
			return sb.toString();
		}
		

		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("A_CLNT_CLSS_CODE1",A_CLNT_CLSS_CODE1);
			m.put("PRIM_SVC_XPRT_YYMM",PRIM_SVC_XPRT_YYMM);
			m.put("CLNT_CLSN_NAME",CLNT_CLSN_NAME);
			m.put("FEE_SUCL_YN",FEE_SUCL_YN);
			m.put("A_CLNT_CLSS_NAME",A_CLNT_CLSS_NAME);
			m.put("FMLY_UNFC_RTNG_TAGT_YN",FMLY_UNFC_RTNG_TAGT_YN);
			return m;
		}
	}

	/* constructor */
	public Sdb1624pVO (String ipAddr, String userID, String mediaType) {
		setHeaderUserID(userID);
		setHeaderTrCode("sdb1624p");
		setHeaderDestination((char)'6');
		setHeaderPacketDataType((char)0);
		setHeaderMediaType(mediaType);
		setHeaderIpAddr(ipAddr);
		setCharSet("1");
        setIsActive("0");		
	}

	/* constructor */
	public Sdb1624pVO (String ipAddr, String userID, String mediaType, String brchCD, String deptCD, String acctCD) {
		setHeaderUserID(userID);
		setHeaderTrCode("sdb1624p");
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
				MCAUtil.writeString(inRec1.CLNT_ENTY_ID, 19, bos);
			}
		}
		
		return bos.toByteArray();
	}
		
	/* parse */
	public void parse(StringInputStream sis) throws Exception {
		
		int cntRecord=0;

		// OutRec1
		outRec1.A_CLNT_CLSS_CODE1 = sis.readString(1);
		outRec1.PRIM_SVC_XPRT_YYMM = sis.readString(6);
		outRec1.CLNT_CLSN_NAME = sis.readString(30);
		outRec1.FEE_SUCL_YN = sis.readString(1);
		outRec1.A_CLNT_CLSS_NAME = sis.readString(90);
		outRec1.FMLY_UNFC_RTNG_TAGT_YN = sis.readString(1);

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
