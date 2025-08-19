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

public class Mag0081pVO extends VOSupport {
	
	/* Input Records */
	private InRec1 inRec1 = new InRec1();

	/* Output Records */
	private OutRec1 outRec1 = new OutRec1();
	private ArrayList<OutRec2> outRec2 = new ArrayList<OutRec2>();
	
	/* Get Instances */
	public InRec1 getInRec1() { return inRec1; }
	public OutRec1 getOutRec1() { return outRec1; }
	public ArrayList<OutRec2> getOutRec2() { return outRec2; }

	/* Set Instances */
	public void setInRec1(InRec1 inRec1) { this.inRec1 = inRec1; }
	public void setOutRec1(OutRec1 outRec1) { this.outRec1 = outRec1; }
	public void setOutRec2(ArrayList<OutRec2> outRec2) { this.outRec2 = outRec2; }

	/* InRec Classes */	
	public static class InRec1 {

		// Constructor
		public InRec1() {
			pText = null;
		}

		/* Attributes */
		private String ACNT_NO;						//	계좌번호,			C[20]
		private String CLNT_RNNO;					//	고객실명번호, 		C[13]		
		private String A_PSWD_CRYP;					//  비밀번호암호문, 		C[44]
		private String A_ACNT_STAS_DTLS_CODE;		//  계좌상태상세코드, 	C[1]
		private String A_NXT_ACNT_ID;				//  다음계좌ID, 		C[19]

		private String pText;
		
		/* Getters/Setters */
		public void setData(
				 String ACNT_NO,				
				 String CLNT_RNNO,				
				 String A_PSWD_CRYP,	
				 String A_ACNT_STAS_DTLS_CODE, 
				 String A_NXT_ACNT_ID

 		) {
			this.ACNT_NO = ACNT_NO;				
			this.CLNT_RNNO = CLNT_RNNO;				
			this.A_PSWD_CRYP = A_PSWD_CRYP;	
			this.A_ACNT_STAS_DTLS_CODE = A_ACNT_STAS_DTLS_CODE; 
			this.A_NXT_ACNT_ID = A_NXT_ACNT_ID; 
		}
		public String getPText() {
			return this.pText;
		}
		public void setPText(String pText) {
			this.pText = pText;
		}
		 public String getACNT_NO() { return ACNT_NO;}				
		 public String getCLNT_RNNO() { return CLNT_RNNO;}				
		 public String getA_PSWD_CRYP() { return A_PSWD_CRYP;}	
		 public String getA_ACNT_STAS_DTLS_CODE() { return A_ACNT_STAS_DTLS_CODE;} 
		 public String getA_NXT_ACNT_ID() { return A_NXT_ACNT_ID;} 
		 public void setACNT_NO(String ACNT_NO) {this.ACNT_NO = ACNT_NO;}				
		 public void setCLNT_RNNO(String CLNT_RNNO) {this.CLNT_RNNO = CLNT_RNNO;}				
		 public void setA_PSWD_CRYP(String A_PSWD_CRYP) {this.A_PSWD_CRYP = A_PSWD_CRYP;}	
		 public void setA_ACNT_STAS_DTLS_CODE(String A_ACNT_STAS_DTLS_CODE) {this.A_ACNT_STAS_DTLS_CODE = A_ACNT_STAS_DTLS_CODE;} 
		 public void setA_NXT_ACNT_ID(String A_NXT_ACNT_ID) {this.A_NXT_ACNT_ID = A_NXT_ACNT_ID;}		

		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			if (pText != null) {
				sb.append("[pText]"+pText);
			} else {
				sb.append("[ACNT_NO]"+ACNT_NO);
				sb.append(",");
				sb.append("[CLNT_RNNO]"+CLNT_RNNO);
				sb.append(",");
				sb.append("[A_PSWD_CRYP]"+A_PSWD_CRYP);
				sb.append(",");
				sb.append("[A_ACNT_STAS_DTLS_CODE]"+A_ACNT_STAS_DTLS_CODE);
				sb.append(",");
				sb.append("[A_NXT_ACNT_ID]"+A_NXT_ACNT_ID);
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
		private String CLNT_RNNO;
		private String CLNT_NAME;
		private String A_BANK_TNFR_LMAM;
		private String A_TMS_BNTR_LMAM;
		private String CTRT_AMNT;
		private String SCRY_RTNG_CODE;
		private String A_SCRY_CRD_USE_YN;
		private String A_OTP_USE_YN;
		private String A_DPSD_MRTG_LN_CTRT_YN;
		private String A_SBSN_MRTG_LN_CTRT_YN;
		private String A_SLB_CTRT_YN;
		private String A_NXT_ACNT_ID;

		/* Getters/Setters */
		public void setData(
				String CLNT_RNNO,
				String CLNT_NAME,
				String A_BANK_TNFR_LMAM,
				String A_TMS_BNTR_LMAM,
				String CTRT_AMNT,
				String SCRY_RTNG_CODE,
				String A_SCRY_CRD_USE_YN,
				String A_OTP_USE_YN,
				String A_DPSD_MRTG_LN_CTRT_YN,
				String A_SBSN_MRTG_LN_CTRT_YN,
				String A_SLB_CTRT_YN,
				String A_NXT_ACNT_ID
		 ) {
			this.CLNT_RNNO = CLNT_RNNO;
			this.CLNT_NAME = CLNT_NAME;
			this.A_BANK_TNFR_LMAM = A_BANK_TNFR_LMAM;
			this.A_TMS_BNTR_LMAM = A_TMS_BNTR_LMAM;
			this.CTRT_AMNT = CTRT_AMNT;
			this.SCRY_RTNG_CODE = SCRY_RTNG_CODE;
			this.A_SCRY_CRD_USE_YN = A_SCRY_CRD_USE_YN;
			this.A_OTP_USE_YN = A_OTP_USE_YN;
			this.A_DPSD_MRTG_LN_CTRT_YN = A_DPSD_MRTG_LN_CTRT_YN;
			this.A_SBSN_MRTG_LN_CTRT_YN = A_SBSN_MRTG_LN_CTRT_YN;
			this.A_SLB_CTRT_YN = A_SLB_CTRT_YN;
			this.A_NXT_ACNT_ID = A_NXT_ACNT_ID;

		}
		public String getCLNT_RNNO() { return CLNT_RNNO;}
		public String getCLNT_NAME() { return CLNT_NAME;}
		public String getA_BANK_TNFR_LMAM() { return A_BANK_TNFR_LMAM;}
		public String getA_TMS_BNTR_LMAM() { return A_TMS_BNTR_LMAM;}
		public String getCTRT_AMNT() { return CTRT_AMNT;}
		public String getSCRY_RTNG_CODE() { return SCRY_RTNG_CODE;}
		public String getA_SCRY_CRD_USE_YN() { return A_SCRY_CRD_USE_YN;}
		public String getA_OTP_USE_YN() { return A_OTP_USE_YN;}
		public String getA_DPSD_MRTG_LN_CTRT_YN() { return A_DPSD_MRTG_LN_CTRT_YN;}
		public String getA_SBSN_MRTG_LN_CTRT_YN() { return A_SBSN_MRTG_LN_CTRT_YN;}
		public String getA_SLB_CTRT_YN() { return A_SLB_CTRT_YN;}
		public String getA_NXT_ACNT_ID() { return A_NXT_ACNT_ID;}

		public void setCLNT_RNNO(String CLNT_RNNO) { this.CLNT_RNNO = CLNT_RNNO;}
		public void setCLNT_NAME(String CLNT_NAME) { this.CLNT_NAME = CLNT_NAME;}
		public void setA_BANK_TNFR_LMAM(String A_BANK_TNFR_LMAM) { this.A_BANK_TNFR_LMAM = A_BANK_TNFR_LMAM;}
		public void setA_TMS_BNTR_LMAM(String A_TMS_BNTR_LMAM) { this.A_TMS_BNTR_LMAM = A_TMS_BNTR_LMAM;}
		public void setCTRT_AMNT(String CTRT_AMNT) { this.CTRT_AMNT = CTRT_AMNT;}
		public void setSCRY_RTNG_CODE(String SCRY_RTNG_CODE) { this.SCRY_RTNG_CODE = SCRY_RTNG_CODE;}
		public void setA_SCRY_CRD_USE_YN(String A_SCRY_CRD_USE_YN) { this.A_SCRY_CRD_USE_YN = A_SCRY_CRD_USE_YN;}
		public void setA_OTP_USE_YN(String A_OTP_USE_YN) { this.A_OTP_USE_YN = A_OTP_USE_YN;}
		public void setA_DPSD_MRTG_LN_CTRT_YN(String A_DPSD_MRTG_LN_CTRT_YN) { this.A_DPSD_MRTG_LN_CTRT_YN = A_DPSD_MRTG_LN_CTRT_YN;}
		public void setA_SBSN_MRTG_LN_CTRT_YN(String A_SBSN_MRTG_LN_CTRT_YN) { this.A_SBSN_MRTG_LN_CTRT_YN = A_SBSN_MRTG_LN_CTRT_YN;}
		public void setA_SLB_CTRT_YN(String A_SLB_CTRT_YN) { this.A_SLB_CTRT_YN = A_SLB_CTRT_YN;}
		public void setA_NXT_ACNT_ID(String A_NXT_ACNT_ID) { this.A_NXT_ACNT_ID = A_NXT_ACNT_ID;}
			

		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("[CLNT_RNNO]"+CLNT_RNNO);
			sb.append(",");
			sb.append("[CLNT_NAME]"+CLNT_NAME);
			sb.append(",");
			sb.append("[A_BANK_TNFR_LMAM]"+A_BANK_TNFR_LMAM);
			sb.append(",");
			sb.append("[A_TMS_BNTR_LMAM]"+A_TMS_BNTR_LMAM);
			sb.append(",");
			sb.append("[CTRT_AMNT]"+CTRT_AMNT);
			sb.append(",");
			sb.append("[SCRY_RTNG_CODE]"+SCRY_RTNG_CODE);
			sb.append(",");
			sb.append("[A_SCRY_CRD_USE_YN]"+A_SCRY_CRD_USE_YN);
			sb.append(",");
			sb.append("[A_OTP_USE_YN]"+A_OTP_USE_YN);
			sb.append(",");
			sb.append("[A_DPSD_MRTG_LN_CTRT_YN]"+A_DPSD_MRTG_LN_CTRT_YN);
			sb.append(",");
			sb.append("[A_SBSN_MRTG_LN_CTRT_YN]"+A_SBSN_MRTG_LN_CTRT_YN);
			sb.append(",");
			sb.append("[A_SLB_CTRT_YN]"+A_SLB_CTRT_YN);
			sb.append(",");
			sb.append("[A_NXT_ACNT_ID]"+A_NXT_ACNT_ID);

			sb.append("}");
			return sb.toString();
		}

		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("CLNT_RNNO",CLNT_RNNO);
			m.put("CLNT_NAME",CLNT_NAME);
			m.put("A_BANK_TNFR_LMAM",A_BANK_TNFR_LMAM);
			m.put("A_TMS_BNTR_LMAM",A_TMS_BNTR_LMAM);
			m.put("CTRT_AMNT",CTRT_AMNT);
			m.put("SCRY_RTNG_CODE",SCRY_RTNG_CODE);
			m.put("A_SCRY_CRD_USE_YN",A_SCRY_CRD_USE_YN);
			m.put("A_OTP_USE_YN",A_OTP_USE_YN);
			m.put("A_DPSD_MRTG_LN_CTRT_YN",A_DPSD_MRTG_LN_CTRT_YN);
			m.put("A_SBSN_MRTG_LN_CTRT_YN",A_SBSN_MRTG_LN_CTRT_YN);
			m.put("A_SLB_CTRT_YN",A_SLB_CTRT_YN);
			m.put("A_NXT_ACNT_ID",A_NXT_ACNT_ID);

			return m;
		}
	}

	public static class OutRec2 { 
	
		// Constructor
		public OutRec2() {
		}

		/* Attributes */
		private String ACNT_ID;
		private String ACNT_NO;
		private String ACNT_TYPE_NAME;
		private String A_HTS_CTRT_YN;
		private String A_ARS_CTRT_YN;
		private String ACNT_SBTT_CTRT_YN;
		private String A_BNTR_CTRT_YN;
		private String A_CMA_UNAS_NAME;
		private String A_RGLY_AT_CTRT_YN1;
		private String A_RGLY_AT_CTRT_YN2;
		private String A_RGLY_AT_CTRT_YN3;
		private String A_PRFT_AMNT_SBTT_CTRT_YN;
		private String FRGN_STCK_CTRT_YN;
		private String A_CRDT_CTRT_YN;
		private String A_UNWN_YN;
		private String A_FTSB_EXSN_YN;
		private String OTPR_ACNT_RGSN_YN;
		private String A_STCK_ETF_ACMV_CTRT_YN;
		private String ENTR_BNKN_CTRT_YN;
		private String A_INSN_FUNS_TNFR_WTDW_YN;
		private String A_AFW_APPN_CTRT_YN;
		private String A_ANYT_LN_CTRT_YN;
		private String A_INTM_LN_CTRT_YN;
		private String A_SLES_CTRT_YN;
		private String A_INTS_SPCR_TRDG_CTRT_YN;
		private String A_BOND_RGLY_RESR_CTRT_YN;
		private String A_BOND_CPN_RESR_CTRT_YN;
		private String A_FCF_TNFR_WTDW_CTRT_YN;
		private String A_TAGT_YN1;
		private String A_TAGT_YN2;
		private String A_TAGT_YN3;
		private String A_TAGT_YN4;
		private String A_TAGT_YN5;
		private String A_TAGT_YN6;
		private String ACNT_FNCN_TYPE_CODE;
		private String A_TAGT_YN7;

		/* Getters/Setters */
		public void setData(
				String ACNT_ID,
				String ACNT_NO,
				String ACNT_TYPE_NAME,
				String A_HTS_CTRT_YN,
				String A_ARS_CTRT_YN,
				String ACNT_SBTT_CTRT_YN,
				String A_BNTR_CTRT_YN,
				String A_CMA_UNAS_NAME,
				String A_RGLY_AT_CTRT_YN1,
				String A_RGLY_AT_CTRT_YN2,
				String A_RGLY_AT_CTRT_YN3,
				String A_PRFT_AMNT_SBTT_CTRT_YN,
				String FRGN_STCK_CTRT_YN,
				String A_CRDT_CTRT_YN,
				String A_UNWN_YN,
				String A_FTSB_EXSN_YN,
				String OTPR_ACNT_RGSN_YN,
				String A_STCK_ETF_ACMV_CTRT_YN,
				String ENTR_BNKN_CTRT_YN,
				String A_INSN_FUNS_TNFR_WTDW_YN,
				String A_AFW_APPN_CTRT_YN,
				String A_ANYT_LN_CTRT_YN,
				String A_INTM_LN_CTRT_YN,
				String A_SLES_CTRT_YN,
				String A_INTS_SPCR_TRDG_CTRT_YN,
				String A_BOND_RGLY_RESR_CTRT_YN,
				String A_BOND_CPN_RESR_CTRT_YN,
				String A_FCF_TNFR_WTDW_CTRT_YN,
				String A_TAGT_YN1,
				String A_TAGT_YN2,
				String A_TAGT_YN3,
				String A_TAGT_YN4,
				String A_TAGT_YN5,
				String A_TAGT_YN6,
				String ACNT_FNCN_TYPE_CODE,
				String A_TAGT_YN7
		 ) {
			this.ACNT_ID =ACNT_ID;
			this.ACNT_NO =ACNT_NO;
			this.ACNT_TYPE_NAME =ACNT_TYPE_NAME;
			this.A_HTS_CTRT_YN =A_HTS_CTRT_YN;
			this.A_ARS_CTRT_YN =A_ARS_CTRT_YN;
			this.ACNT_SBTT_CTRT_YN =ACNT_SBTT_CTRT_YN;
			this.A_BNTR_CTRT_YN =A_BNTR_CTRT_YN;
			this.A_CMA_UNAS_NAME =A_CMA_UNAS_NAME;
			this.A_RGLY_AT_CTRT_YN1 =A_RGLY_AT_CTRT_YN1;
			this.A_RGLY_AT_CTRT_YN2 =A_RGLY_AT_CTRT_YN2;
			this.A_RGLY_AT_CTRT_YN3 =A_RGLY_AT_CTRT_YN3;
			this.A_PRFT_AMNT_SBTT_CTRT_YN =A_PRFT_AMNT_SBTT_CTRT_YN;
			this.FRGN_STCK_CTRT_YN =FRGN_STCK_CTRT_YN;
			this.A_CRDT_CTRT_YN =A_CRDT_CTRT_YN;
			this.A_UNWN_YN =A_UNWN_YN;
			this.A_FTSB_EXSN_YN =A_FTSB_EXSN_YN;
			this.OTPR_ACNT_RGSN_YN =OTPR_ACNT_RGSN_YN;
			this.A_STCK_ETF_ACMV_CTRT_YN =A_STCK_ETF_ACMV_CTRT_YN;
			this.ENTR_BNKN_CTRT_YN =ENTR_BNKN_CTRT_YN;
			this.A_INSN_FUNS_TNFR_WTDW_YN =A_INSN_FUNS_TNFR_WTDW_YN;
			this.A_AFW_APPN_CTRT_YN =A_AFW_APPN_CTRT_YN;
			this.A_ANYT_LN_CTRT_YN =A_ANYT_LN_CTRT_YN;
			this.A_INTM_LN_CTRT_YN =A_INTM_LN_CTRT_YN;
			this.A_SLES_CTRT_YN =A_SLES_CTRT_YN;
			this.A_INTS_SPCR_TRDG_CTRT_YN =A_INTS_SPCR_TRDG_CTRT_YN;
			this.A_BOND_RGLY_RESR_CTRT_YN =A_BOND_RGLY_RESR_CTRT_YN;
			this.A_BOND_CPN_RESR_CTRT_YN =A_BOND_CPN_RESR_CTRT_YN;
			this.A_FCF_TNFR_WTDW_CTRT_YN =A_FCF_TNFR_WTDW_CTRT_YN;
			this.A_TAGT_YN1 =A_TAGT_YN1;
			this.A_TAGT_YN2 =A_TAGT_YN2;
			this.A_TAGT_YN3 =A_TAGT_YN3;
			this.A_TAGT_YN4 =A_TAGT_YN4;
			this.A_TAGT_YN5 =A_TAGT_YN5;
			this.A_TAGT_YN6 =A_TAGT_YN6;
			this.ACNT_FNCN_TYPE_CODE =ACNT_FNCN_TYPE_CODE;
			this.A_TAGT_YN7 =A_TAGT_YN7;


		}
		public String getACNT_ID() { return ACNT_ID;}
		public String getACNT_NO() { return ACNT_NO;}
		public String getACNT_TYPE_NAME() { return ACNT_TYPE_NAME;}
		public String getA_HTS_CTRT_YN() { return A_HTS_CTRT_YN;}
		public String getA_ARS_CTRT_YN() { return A_ARS_CTRT_YN;}
		public String getACNT_SBTT_CTRT_YN() { return ACNT_SBTT_CTRT_YN;}
		public String getA_BNTR_CTRT_YN() { return A_BNTR_CTRT_YN;}
		public String getA_CMA_UNAS_NAME() { return A_CMA_UNAS_NAME;}
		public String getA_RGLY_AT_CTRT_YN1() { return A_RGLY_AT_CTRT_YN1;}
		public String getA_RGLY_AT_CTRT_YN2() { return A_RGLY_AT_CTRT_YN2;}
		public String getA_RGLY_AT_CTRT_YN3() { return A_RGLY_AT_CTRT_YN3;}
		public String getA_PRFT_AMNT_SBTT_CTRT_YN() { return A_PRFT_AMNT_SBTT_CTRT_YN;}
		public String getFRGN_STCK_CTRT_YN() { return FRGN_STCK_CTRT_YN;}
		public String getA_CRDT_CTRT_YN() { return A_CRDT_CTRT_YN;}
		public String getA_UNWN_YN() { return A_UNWN_YN;}
		public String getA_FTSB_EXSN_YN() { return A_FTSB_EXSN_YN;}
		public String getOTPR_ACNT_RGSN_YN() { return OTPR_ACNT_RGSN_YN;}
		public String getA_STCK_ETF_ACMV_CTRT_YN() { return A_STCK_ETF_ACMV_CTRT_YN;}
		public String getENTR_BNKN_CTRT_YN() { return ENTR_BNKN_CTRT_YN;}
		public String getA_INSN_FUNS_TNFR_WTDW_YN() { return A_INSN_FUNS_TNFR_WTDW_YN;}
		public String getA_AFW_APPN_CTRT_YN() { return A_AFW_APPN_CTRT_YN;}
		public String getA_ANYT_LN_CTRT_YN() { return A_ANYT_LN_CTRT_YN;}
		public String getA_INTM_LN_CTRT_YN() { return A_INTM_LN_CTRT_YN;}
		public String getA_SLES_CTRT_YN() { return A_SLES_CTRT_YN;}
		public String getA_INTS_SPCR_TRDG_CTRT_YN() { return A_INTS_SPCR_TRDG_CTRT_YN;}
		public String getA_BOND_RGLY_RESR_CTRT_YN() { return A_BOND_RGLY_RESR_CTRT_YN;}
		public String getA_BOND_CPN_RESR_CTRT_YN() { return A_BOND_CPN_RESR_CTRT_YN;}
		public String getA_FCF_TNFR_WTDW_CTRT_YN() { return A_FCF_TNFR_WTDW_CTRT_YN;}
		public String getA_TAGT_YN1() { return A_TAGT_YN1;}
		public String getA_TAGT_YN2() { return A_TAGT_YN2;}
		public String getA_TAGT_YN3() { return A_TAGT_YN3;}
		public String getA_TAGT_YN4() { return A_TAGT_YN4;}
		public String getA_TAGT_YN5() { return A_TAGT_YN5;}
		public String getA_TAGT_YN6() { return A_TAGT_YN6;}
		public String getACNT_FNCN_TYPE_CODE() { return ACNT_FNCN_TYPE_CODE;}
		public String getA_TAGT_YN7() { return A_TAGT_YN7;}

		public void setACNT_ID(String ACNT_ID) { this.ACNT_ID = ACNT_ID;}
		public void setACNT_NO(String ACNT_NO) { this.ACNT_NO = ACNT_NO;}
		public void setACNT_TYPE_NAME(String ACNT_TYPE_NAME) { this.ACNT_TYPE_NAME = ACNT_TYPE_NAME;}
		public void setA_HTS_CTRT_YN(String A_HTS_CTRT_YN) { this.A_HTS_CTRT_YN = A_HTS_CTRT_YN;}
		public void setA_ARS_CTRT_YN(String A_ARS_CTRT_YN) { this.A_ARS_CTRT_YN = A_ARS_CTRT_YN;}
		public void setACNT_SBTT_CTRT_YN(String ACNT_SBTT_CTRT_YN) { this.ACNT_SBTT_CTRT_YN = ACNT_SBTT_CTRT_YN;}
		public void setA_BNTR_CTRT_YN(String A_BNTR_CTRT_YN) { this.A_BNTR_CTRT_YN = A_BNTR_CTRT_YN;}
		public void setA_CMA_UNAS_NAME(String A_CMA_UNAS_NAME) { this.A_CMA_UNAS_NAME = A_CMA_UNAS_NAME;}
		public void setA_RGLY_AT_CTRT_YN1(String A_RGLY_AT_CTRT_YN1) { this.A_RGLY_AT_CTRT_YN1 = A_RGLY_AT_CTRT_YN1;}
		public void setA_RGLY_AT_CTRT_YN2(String A_RGLY_AT_CTRT_YN2) { this.A_RGLY_AT_CTRT_YN2 = A_RGLY_AT_CTRT_YN2;}
		public void setA_RGLY_AT_CTRT_YN3(String A_RGLY_AT_CTRT_YN3) { this.A_RGLY_AT_CTRT_YN3 = A_RGLY_AT_CTRT_YN3;}
		public void setA_PRFT_AMNT_SBTT_CTRT_YN(String A_PRFT_AMNT_SBTT_CTRT_YN) { this.A_PRFT_AMNT_SBTT_CTRT_YN = A_PRFT_AMNT_SBTT_CTRT_YN;}
		public void setFRGN_STCK_CTRT_YN(String FRGN_STCK_CTRT_YN) { this.FRGN_STCK_CTRT_YN = FRGN_STCK_CTRT_YN;}
		public void setA_CRDT_CTRT_YN(String A_CRDT_CTRT_YN) { this.A_CRDT_CTRT_YN = A_CRDT_CTRT_YN;}
		public void setA_UNWN_YN(String A_UNWN_YN) { this.A_UNWN_YN = A_UNWN_YN;}
		public void setA_FTSB_EXSN_YN(String A_FTSB_EXSN_YN) { this.A_FTSB_EXSN_YN = A_FTSB_EXSN_YN;}
		public void setOTPR_ACNT_RGSN_YN(String OTPR_ACNT_RGSN_YN) { this.OTPR_ACNT_RGSN_YN = OTPR_ACNT_RGSN_YN;}
		public void setA_STCK_ETF_ACMV_CTRT_YN(String A_STCK_ETF_ACMV_CTRT_YN) { this.A_STCK_ETF_ACMV_CTRT_YN = A_STCK_ETF_ACMV_CTRT_YN;}
		public void setENTR_BNKN_CTRT_YN(String ENTR_BNKN_CTRT_YN) { this.ENTR_BNKN_CTRT_YN = ENTR_BNKN_CTRT_YN;}
		public void setA_INSN_FUNS_TNFR_WTDW_YN(String A_INSN_FUNS_TNFR_WTDW_YN) { this.A_INSN_FUNS_TNFR_WTDW_YN = A_INSN_FUNS_TNFR_WTDW_YN;}
		public void setA_AFW_APPN_CTRT_YN(String A_AFW_APPN_CTRT_YN) { this.A_AFW_APPN_CTRT_YN = A_AFW_APPN_CTRT_YN;}
		public void setA_ANYT_LN_CTRT_YN(String A_ANYT_LN_CTRT_YN) { this.A_ANYT_LN_CTRT_YN = A_ANYT_LN_CTRT_YN;}
		public void setA_INTM_LN_CTRT_YN(String A_INTM_LN_CTRT_YN) { this.A_INTM_LN_CTRT_YN = A_INTM_LN_CTRT_YN;}
		public void setA_SLES_CTRT_YN(String A_SLES_CTRT_YN) { this.A_SLES_CTRT_YN = A_SLES_CTRT_YN;}
		public void setA_INTS_SPCR_TRDG_CTRT_YN(String A_INTS_SPCR_TRDG_CTRT_YN) { this.A_INTS_SPCR_TRDG_CTRT_YN = A_INTS_SPCR_TRDG_CTRT_YN;}
		public void setA_BOND_RGLY_RESR_CTRT_YN(String A_BOND_RGLY_RESR_CTRT_YN) { this.A_BOND_RGLY_RESR_CTRT_YN = A_BOND_RGLY_RESR_CTRT_YN;}
		public void setA_BOND_CPN_RESR_CTRT_YN(String A_BOND_CPN_RESR_CTRT_YN) { this.A_BOND_CPN_RESR_CTRT_YN = A_BOND_CPN_RESR_CTRT_YN;}
		public void setA_FCF_TNFR_WTDW_CTRT_YN(String A_FCF_TNFR_WTDW_CTRT_YN) { this.A_FCF_TNFR_WTDW_CTRT_YN = A_FCF_TNFR_WTDW_CTRT_YN;}
		public void setA_TAGT_YN1(String A_TAGT_YN1) { this.A_TAGT_YN1 = A_TAGT_YN1;}
		public void setA_TAGT_YN2(String A_TAGT_YN2) { this.A_TAGT_YN2 = A_TAGT_YN2;}
		public void setA_TAGT_YN3(String A_TAGT_YN3) { this.A_TAGT_YN3 = A_TAGT_YN3;}
		public void setA_TAGT_YN4(String A_TAGT_YN4) { this.A_TAGT_YN4 = A_TAGT_YN4;}
		public void setA_TAGT_YN5(String A_TAGT_YN5) { this.A_TAGT_YN5 = A_TAGT_YN5;}
		public void setA_TAGT_YN6(String A_TAGT_YN6) { this.A_TAGT_YN6 = A_TAGT_YN6;}
		public void setACNT_FNCN_TYPE_CODE(String ACNT_FNCN_TYPE_CODE) { this.ACNT_FNCN_TYPE_CODE = ACNT_FNCN_TYPE_CODE;}
		public void setA_TAGT_YN7(String A_TAGT_YN7) { this.A_TAGT_YN7 = A_TAGT_YN7;}
			

		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("[ACNT_ID]"+ACNT_ID);
			sb.append(",");
			sb.append("[ACNT_NO]"+ACNT_NO);
			sb.append(",");
			sb.append("[ACNT_TYPE_NAME]"+ACNT_TYPE_NAME);
			sb.append(",");
			sb.append("[A_HTS_CTRT_YN]"+A_HTS_CTRT_YN);
			sb.append(",");
			sb.append("[A_ARS_CTRT_YN]"+A_ARS_CTRT_YN);
			sb.append(",");
			sb.append("[ACNT_SBTT_CTRT_YN]"+ACNT_SBTT_CTRT_YN);
			sb.append(",");
			sb.append("[A_BNTR_CTRT_YN]"+A_BNTR_CTRT_YN);
			sb.append(",");
			sb.append("[A_CMA_UNAS_NAME]"+A_CMA_UNAS_NAME);
			sb.append(",");
			sb.append("[A_RGLY_AT_CTRT_YN1]"+A_RGLY_AT_CTRT_YN1);
			sb.append(",");
			sb.append("[A_RGLY_AT_CTRT_YN2]"+A_RGLY_AT_CTRT_YN2);
			sb.append(",");
			sb.append("[A_RGLY_AT_CTRT_YN3]"+A_RGLY_AT_CTRT_YN3);
			sb.append(",");
			sb.append("[A_PRFT_AMNT_SBTT_CTRT_YN]"+A_PRFT_AMNT_SBTT_CTRT_YN);
			sb.append(",");
			sb.append("[FRGN_STCK_CTRT_YN]"+FRGN_STCK_CTRT_YN);
			sb.append(",");
			sb.append("[A_CRDT_CTRT_YN]"+A_CRDT_CTRT_YN);
			sb.append(",");
			sb.append("[A_UNWN_YN]"+A_UNWN_YN);
			sb.append(",");
			sb.append("[A_FTSB_EXSN_YN]"+A_FTSB_EXSN_YN);
			sb.append(",");
			sb.append("[OTPR_ACNT_RGSN_YN]"+OTPR_ACNT_RGSN_YN);
			sb.append(",");
			sb.append("[A_STCK_ETF_ACMV_CTRT_YN]"+A_STCK_ETF_ACMV_CTRT_YN);
			sb.append(",");
			sb.append("[ENTR_BNKN_CTRT_YN]"+ENTR_BNKN_CTRT_YN);
			sb.append(",");
			sb.append("[A_INSN_FUNS_TNFR_WTDW_YN]"+A_INSN_FUNS_TNFR_WTDW_YN);
			sb.append(",");
			sb.append("[A_AFW_APPN_CTRT_YN]"+A_AFW_APPN_CTRT_YN);
			sb.append(",");
			sb.append("[A_ANYT_LN_CTRT_YN]"+A_ANYT_LN_CTRT_YN);
			sb.append(",");
			sb.append("[A_INTM_LN_CTRT_YN]"+A_INTM_LN_CTRT_YN);
			sb.append(",");
			sb.append("[A_SLES_CTRT_YN]"+A_SLES_CTRT_YN);
			sb.append(",");
			sb.append("[A_INTS_SPCR_TRDG_CTRT_YN]"+A_INTS_SPCR_TRDG_CTRT_YN);
			sb.append(",");
			sb.append("[A_BOND_RGLY_RESR_CTRT_YN]"+A_BOND_RGLY_RESR_CTRT_YN);
			sb.append(",");
			sb.append("[A_BOND_CPN_RESR_CTRT_YN]"+A_BOND_CPN_RESR_CTRT_YN);
			sb.append(",");
			sb.append("[A_FCF_TNFR_WTDW_CTRT_YN]"+A_FCF_TNFR_WTDW_CTRT_YN);
			sb.append(",");
			sb.append("[A_TAGT_YN1]"+A_TAGT_YN1);
			sb.append(",");
			sb.append("[A_TAGT_YN2]"+A_TAGT_YN2);
			sb.append(",");
			sb.append("[A_TAGT_YN3]"+A_TAGT_YN3);
			sb.append(",");
			sb.append("[A_TAGT_YN4]"+A_TAGT_YN4);
			sb.append(",");
			sb.append("[A_TAGT_YN5]"+A_TAGT_YN5);
			sb.append(",");
			sb.append("[A_TAGT_YN6]"+A_TAGT_YN6);
			sb.append(",");
			sb.append("[ACNT_FNCN_TYPE_CODE]"+ACNT_FNCN_TYPE_CODE);
			sb.append(",");
			sb.append("[A_TAGT_YN7]"+A_TAGT_YN7);
			sb.append("}");
			return sb.toString();
		}

		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("ACNT_ID", ACNT_ID);
			m.put("ACNT_NO", ACNT_NO);
			m.put("ACNT_TYPE_NAME", ACNT_TYPE_NAME);
			m.put("A_HTS_CTRT_YN", A_HTS_CTRT_YN);
			m.put("A_ARS_CTRT_YN", A_ARS_CTRT_YN);
			m.put("ACNT_SBTT_CTRT_YN", ACNT_SBTT_CTRT_YN);
			m.put("A_BNTR_CTRT_YN", A_BNTR_CTRT_YN);
			m.put("A_CMA_UNAS_NAME", A_CMA_UNAS_NAME);
			m.put("A_RGLY_AT_CTRT_YN1", A_RGLY_AT_CTRT_YN1);
			m.put("A_RGLY_AT_CTRT_YN2", A_RGLY_AT_CTRT_YN2);
			m.put("A_RGLY_AT_CTRT_YN3", A_RGLY_AT_CTRT_YN3);
			m.put("A_PRFT_AMNT_SBTT_CTRT_YN", A_PRFT_AMNT_SBTT_CTRT_YN);
			m.put("FRGN_STCK_CTRT_YN", FRGN_STCK_CTRT_YN);
			m.put("A_CRDT_CTRT_YN", A_CRDT_CTRT_YN);
			m.put("A_UNWN_YN", A_UNWN_YN);
			m.put("A_FTSB_EXSN_YN", A_FTSB_EXSN_YN);
			m.put("OTPR_ACNT_RGSN_YN", OTPR_ACNT_RGSN_YN);
			m.put("A_STCK_ETF_ACMV_CTRT_YN", A_STCK_ETF_ACMV_CTRT_YN);
			m.put("ENTR_BNKN_CTRT_YN", ENTR_BNKN_CTRT_YN);
			m.put("A_INSN_FUNS_TNFR_WTDW_YN", A_INSN_FUNS_TNFR_WTDW_YN);
			m.put("A_AFW_APPN_CTRT_YN", A_AFW_APPN_CTRT_YN);
			m.put("A_ANYT_LN_CTRT_YN", A_ANYT_LN_CTRT_YN);
			m.put("A_INTM_LN_CTRT_YN", A_INTM_LN_CTRT_YN);
			m.put("A_SLES_CTRT_YN", A_SLES_CTRT_YN);
			m.put("A_INTS_SPCR_TRDG_CTRT_YN", A_INTS_SPCR_TRDG_CTRT_YN);
			m.put("A_BOND_RGLY_RESR_CTRT_YN", A_BOND_RGLY_RESR_CTRT_YN);
			m.put("A_BOND_CPN_RESR_CTRT_YN", A_BOND_CPN_RESR_CTRT_YN);
			m.put("A_FCF_TNFR_WTDW_CTRT_YN", A_FCF_TNFR_WTDW_CTRT_YN);
			m.put("A_TAGT_YN1", A_TAGT_YN1);
			m.put("A_TAGT_YN2", A_TAGT_YN2);
			m.put("A_TAGT_YN3", A_TAGT_YN3);
			m.put("A_TAGT_YN4", A_TAGT_YN4);
			m.put("A_TAGT_YN5", A_TAGT_YN5);
			m.put("A_TAGT_YN6", A_TAGT_YN6);
			m.put("ACNT_FNCN_TYPE_CODE", ACNT_FNCN_TYPE_CODE);
			m.put("A_TAGT_YN7", A_TAGT_YN7);

			return m;
		}
	}

	/* constructor */
	public Mag0081pVO (String ipAddr, String userID, String mediaType) {
		setHeaderUserID(userID);
		setHeaderTrCode("mag0081p");
		setHeaderDestination((char)'6');
		setHeaderPacketDataType((char)0);
		setHeaderMediaType(mediaType);
		setHeaderIpAddr(ipAddr);
		setCharSet("1");
        setIsActive("0");		
	}

	/* constructor */
	public Mag0081pVO (String ipAddr, String userID, String mediaType, String brchCD, String deptCD, String acctCD) {
		setHeaderUserID(userID);
		setHeaderTrCode("mag0081p");
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
				MCAUtil.writeString(inRec1.ACNT_NO,20, bos);
				MCAUtil.writeString(inRec1.CLNT_RNNO,13, bos);
				MCAUtil.writeString(inRec1.A_PSWD_CRYP,44, bos);
				MCAUtil.writeString(inRec1.A_ACNT_STAS_DTLS_CODE,1, bos);
				MCAUtil.writeString(inRec1.A_NXT_ACNT_ID,19, bos);
			}
		}
		
		return bos.toByteArray();
	}
		
	/* parse */
	public void parse(StringInputStream sis) throws Exception {
		
		int cntRecord=18;

		// OutRec1
		outRec1.CLNT_RNNO = sis.readString(13);
		outRec1.CLNT_NAME = sis.readString(90);
		outRec1.A_BANK_TNFR_LMAM = sis.readString(18);
		outRec1.A_TMS_BNTR_LMAM = sis.readString(18);
		outRec1.CTRT_AMNT = sis.readString(16);
		outRec1.SCRY_RTNG_CODE = sis.readString(1);
		outRec1.A_SCRY_CRD_USE_YN = sis.readString(1);
		outRec1.A_OTP_USE_YN = sis.readString(1);
		outRec1.A_DPSD_MRTG_LN_CTRT_YN = sis.readString(1);
		outRec1.A_SBSN_MRTG_LN_CTRT_YN = sis.readString(1);
		outRec1.A_SLB_CTRT_YN = sis.readString(1);
		outRec1.A_NXT_ACNT_ID = sis.readString(19);
		
		sis.readString(4);
		
		if (cntRecord < 0) cntRecord = 0;
		
		for (int i=0;i<cntRecord;i++) {
			OutRec2 outRecord = new OutRec2();
			outRecord.ACNT_ID = sis.readString(19);
			outRecord.ACNT_NO = sis.readString(20);
			outRecord.ACNT_TYPE_NAME = sis.readString(150);
			outRecord.A_HTS_CTRT_YN = sis.readString(1);
			outRecord.A_ARS_CTRT_YN = sis.readString(1);
			outRecord.ACNT_SBTT_CTRT_YN = sis.readString(1);
			outRecord.A_BNTR_CTRT_YN = sis.readString(1);
			outRecord.A_CMA_UNAS_NAME = sis.readString(60);
			outRecord.A_RGLY_AT_CTRT_YN1 = sis.readString(1);
			outRecord.A_RGLY_AT_CTRT_YN2 = sis.readString(1);
			outRecord.A_RGLY_AT_CTRT_YN3 = sis.readString(1);
			outRecord.A_PRFT_AMNT_SBTT_CTRT_YN = sis.readString(1);
			outRecord.FRGN_STCK_CTRT_YN = sis.readString(1);
			outRecord.A_CRDT_CTRT_YN = sis.readString(1);
			outRecord.A_UNWN_YN = sis.readString(1);
			outRecord.A_FTSB_EXSN_YN = sis.readString(1);
			outRecord.OTPR_ACNT_RGSN_YN = sis.readString(1);
			outRecord.A_STCK_ETF_ACMV_CTRT_YN = sis.readString(1);
			outRecord.ENTR_BNKN_CTRT_YN = sis.readString(1);
			outRecord.A_INSN_FUNS_TNFR_WTDW_YN = sis.readString(1);
			outRecord.A_AFW_APPN_CTRT_YN = sis.readString(1);
			outRecord.A_ANYT_LN_CTRT_YN = sis.readString(1);
			outRecord.A_INTM_LN_CTRT_YN = sis.readString(1);
			outRecord.A_SLES_CTRT_YN = sis.readString(1);
			outRecord.A_INTS_SPCR_TRDG_CTRT_YN = sis.readString(1);
			outRecord.A_BOND_RGLY_RESR_CTRT_YN = sis.readString(1);
			outRecord.A_BOND_CPN_RESR_CTRT_YN = sis.readString(1);
			outRecord.A_FCF_TNFR_WTDW_CTRT_YN = sis.readString(1);
			outRecord.A_TAGT_YN1 = sis.readString(1);
			outRecord.A_TAGT_YN2 = sis.readString(1);
			outRecord.A_TAGT_YN3 = sis.readString(1);
			outRecord.A_TAGT_YN4 = sis.readString(1);
			outRecord.A_TAGT_YN5 = sis.readString(1);
			outRecord.A_TAGT_YN6 = sis.readString(1);
			outRecord.ACNT_FNCN_TYPE_CODE = sis.readString(4);
			outRecord.A_TAGT_YN7 = sis.readString(1);
			outRec2.add(outRecord);
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
		for (int i=0;i<outRec2.size();i++) {
			sb.append("\t"+outRec2.get(i).toString()+"\n");
		}

		return sb.toString();
	}

	/* toMap */
	public Map<String, Object> toMap() {
		Map<String, Object> m = new HashMap<String, Object>();

		m.put("outRec1",outRec1.toMap());
		
		List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();

		for (int i=0;i<outRec2.size();i++) {
			list2.add(outRec2.get(i).toMap());
		}
		m.put("outRec2",list2);

		return m;
	}
}

