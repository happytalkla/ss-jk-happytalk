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

public class Sgd1616pVO extends VOSupport {

	/* Input Records */
	private InRec1 inRec1 = new InRec1();

	/* Output Records */
	private OutRec1 outRec1 = new OutRec1();
	private ArrayList<OutRec2> outRec2List = new ArrayList<OutRec2>();
	
	/* Get Instances */
	public InRec1 getInRec1() { return inRec1; }
	public OutRec1 getOutRec1() { return outRec1; }
	public ArrayList<OutRec2> getOutRecList() { return outRec2List; }

	/* Set Instances */
	public void setInRec1(InRec1 inRec1) { this.inRec1 = inRec1; }
	public void setOutRec1(OutRec1 outRec1) { this.outRec1 = outRec1; }
	public void setOutRec2List(ArrayList<OutRec2> outRec2List) { this.outRec2List = outRec2List; }


	/* InRec Classes */	
	public static class InRec1 {
		
		// Constructor
		public InRec1() {
			pText = null;
		}

		/* Attributes */
		private String CRM_CLNT_ID;				// CRM고객ID, C[19]				
		private String ACNT_STAS_TYPE_CODE;		// 계좌상태유형코드, C[2]				
		private String A_LEN_10_CNTI_RFRN_KYVL;	// 길이10연속조회키값, C[10]				

		private String pText;
		
		/* Getters/Setters */
		public void setData(
			String CRM_CLNT_ID,
			String ACNT_STAS_TYPE_CODE,
			String A_LEN_10_CNTI_RFRN_KYVL
 		) {
			this.CRM_CLNT_ID = CRM_CLNT_ID;
			this.ACNT_STAS_TYPE_CODE = ACNT_STAS_TYPE_CODE;
			this.A_LEN_10_CNTI_RFRN_KYVL = A_LEN_10_CNTI_RFRN_KYVL;
		}
		public String getPText() {
			return this.pText;
		}
		public void setPText(String pText) {
			this.pText = pText;
		}
		public String getCRM_CLNT_ID() {
			return CRM_CLNT_ID;
		}
		public void setCRM_CLNT_ID(String cRM_CLNT_ID) {
			CRM_CLNT_ID = cRM_CLNT_ID;
		}
		public String getACNT_STAS_TYPE_CODE() {
			return ACNT_STAS_TYPE_CODE;
		}
		public void setACNT_STAS_TYPE_CODE(String aCNT_STAS_TYPE_CODE) {
			ACNT_STAS_TYPE_CODE = aCNT_STAS_TYPE_CODE;
		}
		public String getA_LEN_10_CNTI_RFRN_KYVL() {
			return A_LEN_10_CNTI_RFRN_KYVL;
		}
		public void setA_LEN_10_CNTI_RFRN_KYVL(String a_LEN_10_CNTI_RFRN_KYVL) {
			A_LEN_10_CNTI_RFRN_KYVL = a_LEN_10_CNTI_RFRN_KYVL;
		}
		
		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			if (pText != null) {
				sb.append("[pText]"+pText);
			} else {
				sb.append("[CRM_CLNT_ID]"+CRM_CLNT_ID);
				sb.append(",");
				sb.append("[ACNT_STAS_TYPE_CODE]"+ACNT_STAS_TYPE_CODE);
				sb.append(",");
				sb.append("[A_LEN_10_CNTI_RFRN_KYVL]"+A_LEN_10_CNTI_RFRN_KYVL);
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
		private String A_LEN_10_CNTI_RFRN_KYVL;			// 길이10연속조회키값, C[10]
		
		/* Getters/Setters */
		public void setData(
			String A_LEN_10_CNTI_RFRN_KYVL
		) {
			this.A_LEN_10_CNTI_RFRN_KYVL = A_LEN_10_CNTI_RFRN_KYVL;
		}
		public String getA_LEN_10_CNTI_RFRN_KYVL() { return A_LEN_10_CNTI_RFRN_KYVL; }
		public void setA_LEN_10_CNTI_RFRN_KYVL(String A_LEN_10_CNTI_RFRN_KYVL) { this.A_LEN_10_CNTI_RFRN_KYVL = A_LEN_10_CNTI_RFRN_KYVL; }
		

		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("[A_LEN_10_CNTI_RFRN_KYVL]"+A_LEN_10_CNTI_RFRN_KYVL);
			sb.append("}");
			return sb.toString();
		}

		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("A_LEN_10_CNTI_RFRN_KYVL",A_LEN_10_CNTI_RFRN_KYVL);
			return m;
		}
	}
	
	@Data
	public static class OutRec2 { 
	
		// Constructor
		public OutRec2() {
		}
		
		/* Attributes */
		private String ACNT_ID;						// 계좌ID,		C[19] 	
		private String ACNT_OWNR_ID;				// 계좌주ID,		C[19] 	
		private String ACNT_NO;						// 계좌번호,		C[20] 	
		private String ACNT_NAME;					// 계좌명,		C[90] 	
		private String A_ACNT_STAS_NAME;			// 계좌상태명,		C[30] 	
		private String PRFT_CNTR_ENTY_NAME;			// 실적점명,		C[60] 	
		private String ACNT_MNGG_BRNH_ENTY_NAME;	// 계좌관리점명,	C[60] 	
		private String A_MNGR_EMPY_NAME;			// 관리자사원명,	C[60] 	
		/*
		 * IPCC_MCH MCA I/F 응답 컬럼 추가
		 */
		private String TEMP1;              			// 임시1,	C[60]
		private String TEMP2;			            // 임시2,	C[60]

		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("[ACNT_ID]"+ACNT_ID);
			sb.append(",");
			sb.append("[ACNT_OWNR_ID]"+ACNT_OWNR_ID);
			sb.append(",");
			sb.append("[ACNT_NO]"+ACNT_NO);
			sb.append(",");
			sb.append("[ACNT_NAME]"+ACNT_NAME);
			sb.append(",");
			sb.append("[A_ACNT_STAS_NAME]"+A_ACNT_STAS_NAME);
			sb.append(",");
			sb.append("[PRFT_CNTR_ENTY_NAME]"+PRFT_CNTR_ENTY_NAME);
			sb.append(",");
			sb.append("[ACNT_MNGG_BRNH_ENTY_NAME]"+ACNT_MNGG_BRNH_ENTY_NAME);
			sb.append(",");
			sb.append("[A_MNGR_EMPY_NAME]"+A_MNGR_EMPY_NAME);
			sb.append(",");
			/*
			 * IPCC_MCH MCA I/F 응답 컬럼 추가
			 */
			sb.append("[TEMP1]"+TEMP1);
			sb.append(",");
			sb.append("[TEMP2]"+TEMP2);
			sb.append("}");
			return sb.toString();
		}
		
		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("ACNT_ID", ACNT_ID);
			m.put("ACNT_OWNR_ID",ACNT_OWNR_ID);
			m.put("ACNT_NO",ACNT_NO);
			m.put("ACNT_NAME",ACNT_NAME);
			m.put("A_ACNT_STAS_NAME",A_ACNT_STAS_NAME);
			m.put("PRFT_CNTR_ENTY_NAME",PRFT_CNTR_ENTY_NAME);
			m.put("ACNT_MNGG_BRNH_ENTY_NAME",ACNT_MNGG_BRNH_ENTY_NAME);
			m.put("A_MNGR_EMPY_NAME",A_MNGR_EMPY_NAME);
			/*
			 * IPCC_MCH MCA I/F 응답 컬럼 추가
			 */
			m.put("TEMP1",TEMP1);
			m.put("TEMP2",TEMP2);
			return m;
		}
	}

	/* constructor */
	public Sgd1616pVO (String ipAddr, String userID, String mediaType) {
		setHeaderUserID("");
		setHeaderTrCode("sgd1616p");
		setHeaderDestination((char)'6');
		setHeaderPacketDataType((char)0);
		setHeaderMediaType(mediaType);
		setHeaderIpAddr(ipAddr);
		setCharSet("1");
        setIsActive("0");		
	}

	/* constructor */
	public Sgd1616pVO (String ipAddr, String userID, String mediaType, String brchCD, String deptCD, String acctCD) {
		setHeaderUserID("");
		setHeaderTrCode("sgd1616p");
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
				MCAUtil.writeString(inRec1.CRM_CLNT_ID,19, bos);
				MCAUtil.writeString(inRec1.ACNT_STAS_TYPE_CODE,2, bos);
				MCAUtil.writeString(inRec1.A_LEN_10_CNTI_RFRN_KYVL,10, bos);
			}
		}
		
		return bos.toByteArray();
	}
		
	/* parse */
	public void parse(StringInputStream sis) {

		int cntRecord = 18;

		try {
			// OutRec1
			outRec1.A_LEN_10_CNTI_RFRN_KYVL = sis.readString(10);
			// OutRec2
			try {
				cntRecord = Integer.parseInt(sis.readString(4).trim());
			} catch (NumberFormatException e) {
				throw new Exception("OutRec2 NumberFormatException");
			}

			// if (cntRecord < 0) cntRecord = 0;

			for (int i = 0; i < cntRecord; i++) {
				OutRec2 outRecord = new OutRec2();
				outRecord.ACNT_ID = sis.readString(19);
				outRecord.ACNT_OWNR_ID = sis.readString(19);
				outRecord.ACNT_NO = sis.readString(20);
				outRecord.ACNT_NAME = sis.readString(90);
				outRecord.A_ACNT_STAS_NAME = sis.readString(30);
				outRecord.PRFT_CNTR_ENTY_NAME = sis.readString(60);
				outRecord.ACNT_MNGG_BRNH_ENTY_NAME = sis.readString(60);
				/*
				 * IPCC_MCH MCA I/F 응답 컬럼 추가
				 */
				outRecord.TEMP1 = sis.readString(60);
				outRecord.TEMP2 = sis.readString(60);
				outRec2List.add(outRecord);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

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
		
		sb.append("[outRec2]\n");
		sb.append("\t"+outRec2List.toString()+"\n");

		return sb.toString();
	}

	/* toMap */
	public Map<String, Object> toMap() {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("outRec1", outRec1.toMap());
		
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();

		for (int i=0;i<outRec2List.size();i++) {
			list1.add(outRec2List.get(i).toMap());
		}
		m.put("outRec2",list1);
		
		return m;
	}
	
}
