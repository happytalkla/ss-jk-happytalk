package ht.mca.vo;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.ss.common.channel.mca.common.net.StringInputStream;
import com.ss.common.channel.mca.util.MCAUtil;
import com.ss.common.channel.mca.vo.VOSupport;
import com.ss.common.util.StringUtility;

import ht.util.StringUtil;

public class Sgd1611pVO extends VOSupport {

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
		private String CRM_CLNT_ID;			// CRM고객ID			
	
		private String pText;
		
		/* Getters/Setters */
		public void setData(
				String CRM_CLNT_ID
	 		) {
				this.CRM_CLNT_ID = CRM_CLNT_ID;
			}
		public String getPText() {
			return this.pText;
		}
		public void setPText(String pText) {
			this.pText = pText;
		}
		public String getCRM_CLNT_ID() { return CRM_CLNT_ID; }
		public void setCRM_CLNT_ID(String CRM_CLNT_ID) { this.CRM_CLNT_ID = CRM_CLNT_ID; }			
	
		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			if (pText != null) {
				sb.append("[pText]"+pText);
			} else {
				sb.append("[CRM_CLNT_ID]"+CRM_CLNT_ID);
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
		private String CLNT_NAME;                   //고객명           C[90]
		private String ENG_CLNT_NAME;               //영문고객명       C[60]
		private String BRDT;                        //생년월일         C[8]
		private String CLNT_CLSN_NAME;              //고객분류명       C[30]
		private String A_INVR_SECT_CODE_NAME;       //투자자구분코드명 C[60]
		private String A_NTNY_NAME;                 //소속국가명       C[60]
		private String A_RSDT_CNTY_NAME;            //거주국가명       C[60]
		private String A_NATV_OR_FRGR_SECT_NAME;    //내외국인구분명   C[60]
		private String PRMT_RSDT_YN;                //영주권자여부     C[1]
		private String WDNG_STAS_SECT_CODE;         //결혼상태구분코드 C[1]
		private String WDNG_CMRT_DATE;              //결혼기념일자     C[8]
		private String JOB_CODE;                    //직업코드         C[3]
		private String CLAS_CODE;                   //직급코드         C[1]
		private String BLCR_CODE;                   //소속사코드       C[12]
		private String JOB_SITE_NAME;               //회사명           C[30]
		private String RLCM_CLNT_SECT_CODE;         //관계사고객구분코드C[1]
		private String PRB_RCST_SECT_CODE;          //홍보물수령처구분C[2]드
		private String HBBY_NAME;                   //취미명           C[30]
		private String HOUS_PSSN_CODE;              //주택소유코드     C[1]
		private String TRDG_MTV_SECT_CODE;          //거래동기구분코드 C[2]
		private String MOBL_PHON_HOLD_SECT_CODE;    //휴대전화보유구분C[1]드
		private String EMAL_HOLD_SECT_CODE;         //E-mail보유구분코드C[1]
		private String CLCR_MNCN_MEAN_CODE;         //콜센터주연락수단C[1]드
		private String SOLR_LUNR_CLNR_SECT_CODE;    //양력음력구분코드 C[1]
		private String NDVL_IFMN_USE_ASNT_YN1;      //마케팅전화동의여부C[1]
		private String NDVL_IFMN_USE_ASNT_YN2;      //마케팅SMS동의여부C[1]
		private String NDVL_IFMN_USE_ASNT_YN3;      //마케팅E-mail동의C[1]부
		private String MAIN_CLNT_YN;                //주고객여부       C[1]
		private String A_STX_TXTN_YN;               //거래세과세여부   C[1]
		private String A_STX_TXTN_AMNT;             //거래세과세액     C[18]
		private String HOME_PSTL_CODE;              //자택우편번호     C[6]
		private String HOME_ADRS;                   //자택주소         C[150]
		private String SITE_PSTL_CODE;              //직장우편번호     C[6]
		private String SITE_ADRS;                   //직장주소         C[150]
		private String EMAL_ADRS;                   //E-mail주소       C[150]
		private String EMAL_SNBC_YN;                //E-mail반송여부   C[1]
		private String MOBL_PHON_NO;                //휴대전화번호     C[20]
		private String HOME_PHON_NO;                //자택전화번호     C[20]
		private String HOME_FAX_NO;                 //자택팩스번호     C[20]
		private String SITE_PHON_NO;                //직장전화번호     C[20]
		private String SITE_FAX_NO;                 //직장팩스번호     C[20]
		private String A_GNDR_SECT_NAME;            //성별구분명       C[10]
		private String A_CURR_AGE;                  //현재연령         C[13]
		private String AGE;                         //(만)나이         C[6]
		private String ETF_RISK_ANCM_YN;            //ETF위험고지여부  C[1]
		private String MNCL_NAME;                   //주고객명         C[60]
		private String FRGN_STCK_CTRT_YN;           //해외주식약정여부 C[1]
		private String RGSN_DATE;                   //등록일자         C[8]
		private String PHC_SVC_NAME;                //PHC서비스명      C[100]
                                                    
		/* Getters/Setters */
		public void setData(
				String CLNT_NAME,
				String ENG_CLNT_NAME,
				String BRDT,
				String CLNT_CLSN_NAME,
				String A_INVR_SECT_CODE_NAME,
				String A_NTNY_NAME,
				String A_RSDT_CNTY_NAME,
				String A_NATV_OR_FRGR_SECT_NAME,
				String PRMT_RSDT_YN,
				String WDNG_STAS_SECT_CODE,
				String WDNG_CMRT_DATE,
				String JOB_CODE,
				String CLAS_CODE,
				String BLCR_CODE,
				String JOB_SITE_NAME,
				String RLCM_CLNT_SECT_CODE,
				String PRB_RCST_SECT_CODE,
				String HBBY_NAME,
				String HOUS_PSSN_CODE,
				String TRDG_MTV_SECT_CODE,
				String MOBL_PHON_HOLD_SECT_CODE,
				String EMAL_HOLD_SECT_CODE,
				String CLCR_MNCN_MEAN_CODE,
				String SOLR_LUNR_CLNR_SECT_CODE,
				String NDVL_IFMN_USE_ASNT_YN1,
				String NDVL_IFMN_USE_ASNT_YN2,
				String NDVL_IFMN_USE_ASNT_YN3,
				String MAIN_CLNT_YN,
				String A_STX_TXTN_YN,
				String A_STX_TXTN_AMNT,
				String HOME_PSTL_CODE,
				String HOME_ADRS,
				String SITE_PSTL_CODE,
				String SITE_ADRS,
				String EMAL_ADRS,
				String EMAL_SNBC_YN,
				String MOBL_PHON_NO,
				String HOME_PHON_NO,
				String HOME_FAX_NO,
				String SITE_PHON_NO,
				String SITE_FAX_NO,
				String A_GNDR_SECT_NAME,
				String A_CURR_AGE,
				String AGE,
				String ETF_RISK_ANCM_YN,
				String MNCL_NAME,
				String FRGN_STCK_CTRT_YN,
				String RGSN_DATE,
				String PHC_SVC_NAME
			)	{
			this.CLNT_NAME                = CLNT_NAME;
			this.ENG_CLNT_NAME            = ENG_CLNT_NAME;
			this.BRDT                     = BRDT;
			this.CLNT_CLSN_NAME           = CLNT_CLSN_NAME;
			this.A_INVR_SECT_CODE_NAME    = A_INVR_SECT_CODE_NAME;
			this.A_NTNY_NAME              = A_NTNY_NAME;
			this.A_RSDT_CNTY_NAME         = A_RSDT_CNTY_NAME;
			this.A_NATV_OR_FRGR_SECT_NAME = A_NATV_OR_FRGR_SECT_NAME;
			this.PRMT_RSDT_YN             = PRMT_RSDT_YN;
			this.WDNG_STAS_SECT_CODE      = WDNG_STAS_SECT_CODE;
			this.WDNG_CMRT_DATE           = WDNG_CMRT_DATE;
			this.JOB_CODE                 = JOB_CODE;
			this.CLAS_CODE                = CLAS_CODE;
			this.BLCR_CODE                = BLCR_CODE;
			this.JOB_SITE_NAME            = JOB_SITE_NAME;
			this.RLCM_CLNT_SECT_CODE      = RLCM_CLNT_SECT_CODE;
			this.PRB_RCST_SECT_CODE       = PRB_RCST_SECT_CODE;
			this.HBBY_NAME                = HBBY_NAME;
			this.HOUS_PSSN_CODE           = HOUS_PSSN_CODE;
			this.TRDG_MTV_SECT_CODE       = TRDG_MTV_SECT_CODE;
			this.MOBL_PHON_HOLD_SECT_CODE = MOBL_PHON_HOLD_SECT_CODE;
			this.EMAL_HOLD_SECT_CODE      = EMAL_HOLD_SECT_CODE;
			this.CLCR_MNCN_MEAN_CODE      = CLCR_MNCN_MEAN_CODE;
			this.SOLR_LUNR_CLNR_SECT_CODE = SOLR_LUNR_CLNR_SECT_CODE;
			this.NDVL_IFMN_USE_ASNT_YN1   = NDVL_IFMN_USE_ASNT_YN1;
			this.NDVL_IFMN_USE_ASNT_YN2   = NDVL_IFMN_USE_ASNT_YN2;
			this.NDVL_IFMN_USE_ASNT_YN3   = NDVL_IFMN_USE_ASNT_YN3;
			this.MAIN_CLNT_YN             = MAIN_CLNT_YN;
			this.A_STX_TXTN_YN            = A_STX_TXTN_YN;
			this.A_STX_TXTN_AMNT          = A_STX_TXTN_AMNT;
			this.HOME_PSTL_CODE           = HOME_PSTL_CODE;
			this.HOME_ADRS                = HOME_ADRS;
			this.SITE_PSTL_CODE           = SITE_PSTL_CODE;
			this.SITE_ADRS                = SITE_ADRS;
			this.EMAL_ADRS                = EMAL_ADRS;
			this.EMAL_SNBC_YN             = EMAL_SNBC_YN;
			this.MOBL_PHON_NO             = MOBL_PHON_NO;
			this.HOME_PHON_NO             = HOME_PHON_NO;
			this.HOME_FAX_NO              = HOME_FAX_NO;
			this.SITE_PHON_NO             = SITE_PHON_NO;
			this.SITE_FAX_NO              = SITE_FAX_NO;
			this.A_GNDR_SECT_NAME         = A_GNDR_SECT_NAME;
			this.A_CURR_AGE               = A_CURR_AGE;
			this.AGE                      = AGE;
			this.ETF_RISK_ANCM_YN         = ETF_RISK_ANCM_YN;
			this.MNCL_NAME                = MNCL_NAME;
			this.FRGN_STCK_CTRT_YN        = FRGN_STCK_CTRT_YN;
			this.RGSN_DATE                = RGSN_DATE;
			this.PHC_SVC_NAME             = PHC_SVC_NAME;
		}

		public String getCLNT_NAME() {return CLNT_NAME;}
		public void setCLNT_NAME(String cLNT_NAME) {CLNT_NAME = cLNT_NAME;}
		public String getENG_CLNT_NAME() {return ENG_CLNT_NAME;}
		public void setENG_CLNT_NAME(String eNG_CLNT_NAME) {ENG_CLNT_NAME = eNG_CLNT_NAME;}
		public String getBRDT() {return BRDT;}
		public void setBRDT(String bRDT) {BRDT = bRDT;}
		public String getCLNT_CLSN_NAME() {return CLNT_CLSN_NAME;}
		public void setCLNT_CLSN_NAME(String cLNT_CLSN_NAME) {CLNT_CLSN_NAME = cLNT_CLSN_NAME;}
		public String getA_INVR_SECT_CODE_NAME() {return A_INVR_SECT_CODE_NAME;}
		public void setA_INVR_SECT_CODE_NAME(String a_INVR_SECT_CODE_NAME) {A_INVR_SECT_CODE_NAME = a_INVR_SECT_CODE_NAME;}
		public String getA_NTNY_NAME() {return A_NTNY_NAME;}
		public void setA_NTNY_NAME(String a_NTNY_NAME) {A_NTNY_NAME = a_NTNY_NAME;}
		public String getA_RSDT_CNTY_NAME() {return A_RSDT_CNTY_NAME;}
		public void setA_RSDT_CNTY_NAME(String a_RSDT_CNTY_NAME) {A_RSDT_CNTY_NAME = a_RSDT_CNTY_NAME;}
		public String getA_NATV_OR_FRGR_SECT_NAME() {return A_NATV_OR_FRGR_SECT_NAME;}
		public void setA_NATV_OR_FRGR_SECT_NAME(String a_NATV_OR_FRGR_SECT_NAME) {A_NATV_OR_FRGR_SECT_NAME = a_NATV_OR_FRGR_SECT_NAME;}
		public String getPRMT_RSDT_YN() {return PRMT_RSDT_YN;}
		public void setPRMT_RSDT_YN(String pRMT_RSDT_YN) {PRMT_RSDT_YN = pRMT_RSDT_YN;}
		public String getWDNG_STAS_SECT_CODE() {return WDNG_STAS_SECT_CODE;}
		public void setWDNG_STAS_SECT_CODE(String wDNG_STAS_SECT_CODE) {WDNG_STAS_SECT_CODE = wDNG_STAS_SECT_CODE;}
		public String getWDNG_CMRT_DATE() {return WDNG_CMRT_DATE;}
		public void setWDNG_CMRT_DATE(String wDNG_CMRT_DATE) {WDNG_CMRT_DATE = wDNG_CMRT_DATE;}
		public String getJOB_CODE() {return JOB_CODE;}
		public void setJOB_CODE(String jOB_CODE) {JOB_CODE = jOB_CODE;}
		public String getCLAS_CODE() {return CLAS_CODE;}
		public void setCLAS_CODE(String cLAS_CODE) {CLAS_CODE = cLAS_CODE;}
		public String getBLCR_CODE() {return BLCR_CODE;}
		public void setBLCR_CODE(String bLCR_CODE) {BLCR_CODE = bLCR_CODE;}
		public String getJOB_SITE_NAME() {return JOB_SITE_NAME;}
		public void setJOB_SITE_NAME(String jOB_SITE_NAME) {JOB_SITE_NAME = jOB_SITE_NAME;}
		public String getRLCM_CLNT_SECT_CODE() {return RLCM_CLNT_SECT_CODE;}
		public void setRLCM_CLNT_SECT_CODE(String rLCM_CLNT_SECT_CODE) {RLCM_CLNT_SECT_CODE = rLCM_CLNT_SECT_CODE;}
		public String getPRB_RCST_SECT_CODE() {return PRB_RCST_SECT_CODE;}
		public void setPRB_RCST_SECT_CODE(String pRB_RCST_SECT_CODE) {PRB_RCST_SECT_CODE = pRB_RCST_SECT_CODE;}
		public String getHBBY_NAME() {return HBBY_NAME;}
		public void setHBBY_NAME(String hBBY_NAME) {HBBY_NAME = hBBY_NAME;}
		public String getHOUS_PSSN_CODE() {return HOUS_PSSN_CODE;}
		public void setHOUS_PSSN_CODE(String hOUS_PSSN_CODE) {HOUS_PSSN_CODE = hOUS_PSSN_CODE;}
		public String getTRDG_MTV_SECT_CODE() {return TRDG_MTV_SECT_CODE;}
		public void setTRDG_MTV_SECT_CODE(String tRDG_MTV_SECT_CODE) {TRDG_MTV_SECT_CODE = tRDG_MTV_SECT_CODE;}
		public String getMOBL_PHON_HOLD_SECT_CODE() {return MOBL_PHON_HOLD_SECT_CODE;}
		public void setMOBL_PHON_HOLD_SECT_CODE(String mOBL_PHON_HOLD_SECT_CODE) {MOBL_PHON_HOLD_SECT_CODE = mOBL_PHON_HOLD_SECT_CODE;}
		public String getEMAL_HOLD_SECT_CODE() {return EMAL_HOLD_SECT_CODE;}
		public void setEMAL_HOLD_SECT_CODE(String eMAL_HOLD_SECT_CODE) {EMAL_HOLD_SECT_CODE = eMAL_HOLD_SECT_CODE;}
		public String getCLCR_MNCN_MEAN_CODE() {return CLCR_MNCN_MEAN_CODE;}
		public void setCLCR_MNCN_MEAN_CODE(String cLCR_MNCN_MEAN_CODE) {CLCR_MNCN_MEAN_CODE = cLCR_MNCN_MEAN_CODE;}
		public String getSOLR_LUNR_CLNR_SECT_CODE() {return SOLR_LUNR_CLNR_SECT_CODE;}
		public void setSOLR_LUNR_CLNR_SECT_CODE(String sOLR_LUNR_CLNR_SECT_CODE) {SOLR_LUNR_CLNR_SECT_CODE = sOLR_LUNR_CLNR_SECT_CODE;}
		public String getNDVL_IFMN_USE_ASNT_YN1() {return NDVL_IFMN_USE_ASNT_YN1;}
		public void setNDVL_IFMN_USE_ASNT_YN1(String nDVL_IFMN_USE_ASNT_YN1) {NDVL_IFMN_USE_ASNT_YN1 = nDVL_IFMN_USE_ASNT_YN1;}
		public String getNDVL_IFMN_USE_ASNT_YN2() {return NDVL_IFMN_USE_ASNT_YN2;}
		public void setNDVL_IFMN_USE_ASNT_YN2(String nDVL_IFMN_USE_ASNT_YN2) {NDVL_IFMN_USE_ASNT_YN2 = nDVL_IFMN_USE_ASNT_YN2;}
		public String getNDVL_IFMN_USE_ASNT_YN3() {return NDVL_IFMN_USE_ASNT_YN3;}
		public void setNDVL_IFMN_USE_ASNT_YN3(String nDVL_IFMN_USE_ASNT_YN3) {NDVL_IFMN_USE_ASNT_YN3 = nDVL_IFMN_USE_ASNT_YN3;}
		public String getMAIN_CLNT_YN() {return MAIN_CLNT_YN;}
		public void setMAIN_CLNT_YN(String mAIN_CLNT_YN) {MAIN_CLNT_YN = mAIN_CLNT_YN;}
		public String getA_STX_TXTN_YN() {return A_STX_TXTN_YN;}
		public void setA_STX_TXTN_YN(String a_STX_TXTN_YN) {A_STX_TXTN_YN = a_STX_TXTN_YN;}
		public String getA_STX_TXTN_AMNT() {return A_STX_TXTN_AMNT;}
		public void setA_STX_TXTN_AMNT(String a_STX_TXTN_AMNT) {A_STX_TXTN_AMNT = a_STX_TXTN_AMNT;}
		public String getHOME_PSTL_CODE() {return HOME_PSTL_CODE;}
		public void setHOME_PSTL_CODE(String hOME_PSTL_CODE) {HOME_PSTL_CODE = hOME_PSTL_CODE;}
		public String getHOME_ADRS() {return HOME_ADRS;}
		public void setHOME_ADRS(String hOME_ADRS) {HOME_ADRS = hOME_ADRS;}
		public String getSITE_PSTL_CODE() {return SITE_PSTL_CODE;}
		public void setSITE_PSTL_CODE(String sITE_PSTL_CODE) {SITE_PSTL_CODE = sITE_PSTL_CODE;}
		public String getSITE_ADRS() {return SITE_ADRS;}
		public void setSITE_ADRS(String sITE_ADRS) {SITE_ADRS = sITE_ADRS;}
		public String getEMAL_ADRS() {return EMAL_ADRS;}
		public void setEMAL_ADRS(String eMAL_ADRS) {EMAL_ADRS = eMAL_ADRS;}
		public String getEMAL_SNBC_YN() {return EMAL_SNBC_YN;}
		public void setEMAL_SNBC_YN(String eMAL_SNBC_YN) {EMAL_SNBC_YN = eMAL_SNBC_YN;}
		public String getMOBL_PHON_NO() {return MOBL_PHON_NO;}
		public void setMOBL_PHON_NO(String mOBL_PHON_NO) {MOBL_PHON_NO = mOBL_PHON_NO;}
		public String getHOME_PHON_NO() {return HOME_PHON_NO;}
		public void setHOME_PHON_NO(String hOME_PHON_NO) {HOME_PHON_NO = hOME_PHON_NO;}
		public String getHOME_FAX_NO() {return HOME_FAX_NO;}
		public void setHOME_FAX_NO(String hOME_FAX_NO) {HOME_FAX_NO = hOME_FAX_NO;}
		public String getSITE_PHON_NO() {return SITE_PHON_NO;}
		public void setSITE_PHON_NO(String sITE_PHON_NO) {SITE_PHON_NO = sITE_PHON_NO;}
		public String getSITE_FAX_NO() {return SITE_FAX_NO;}
		public void setSITE_FAX_NO(String sITE_FAX_NO) {SITE_FAX_NO = sITE_FAX_NO;}
		public String getA_GNDR_SECT_NAME() {return A_GNDR_SECT_NAME;}
		public void setA_GNDR_SECT_NAME(String a_GNDR_SECT_NAME) {A_GNDR_SECT_NAME = a_GNDR_SECT_NAME;}
		public String getA_CURR_AGE() {return A_CURR_AGE;}
		public void setA_CURR_AGE(String a_CURR_AGE) {A_CURR_AGE = a_CURR_AGE;}
		public String getAGE() {return AGE;}
		public void setAGE(String aGE) {AGE = aGE;}
		public String getETF_RISK_ANCM_YN() {return ETF_RISK_ANCM_YN;}
		public void setETF_RISK_ANCM_YN(String eTF_RISK_ANCM_YN) {ETF_RISK_ANCM_YN = eTF_RISK_ANCM_YN;}
		public String getMNCL_NAME() {return MNCL_NAME;}
		public void setMNCL_NAME(String mNCL_NAME) {MNCL_NAME = mNCL_NAME;}
		public String getFRGN_STCK_CTRT_YN() {return FRGN_STCK_CTRT_YN;}
		public void setFRGN_STCK_CTRT_YN(String fRGN_STCK_CTRT_YN) {FRGN_STCK_CTRT_YN = fRGN_STCK_CTRT_YN;}
		public String getRGSN_DATE() {return RGSN_DATE;}
		public void setRGSN_DATE(String rGSN_DATE) {RGSN_DATE = rGSN_DATE;}
		public String getPHC_SVC_NAME() {return PHC_SVC_NAME;}
		public void setPHC_SVC_NAME(String pHC_SVC_NAME) {PHC_SVC_NAME = pHC_SVC_NAME;}
		
		
		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			
			sb.append("{");
			sb.append("[CLNT_NAME]"+CLNT_NAME);
			sb.append(",");
			sb.append("[ENG_CLNT_NAME]"+ENG_CLNT_NAME);
			sb.append(",");
			sb.append("[BRDT]"+BRDT);
			sb.append(",");
			sb.append("[CLNT_CLSN_NAME]"+CLNT_CLSN_NAME);
			sb.append(",");
			sb.append("[A_INVR_SECT_CODE_NAME]"+A_INVR_SECT_CODE_NAME);
			sb.append(",");
			sb.append("[A_NTNY_NAME]"+A_NTNY_NAME);
			sb.append(",");
			sb.append("[A_RSDT_CNTY_NAME]"+A_RSDT_CNTY_NAME);
			sb.append(",");
			sb.append("[A_NATV_OR_FRGR_SECT_NAME]"+A_NATV_OR_FRGR_SECT_NAME);
			sb.append(",");
			sb.append("[PRMT_RSDT_YN]"+PRMT_RSDT_YN);
			sb.append(",");
			sb.append("[WDNG_STAS_SECT_CODE]"+WDNG_STAS_SECT_CODE);
			sb.append(",");
			sb.append("[WDNG_CMRT_DATE]"+WDNG_CMRT_DATE);
			sb.append(",");
			sb.append("[JOB_CODE]"+JOB_CODE);
			sb.append(",");
			sb.append("[CLAS_CODE]"+CLAS_CODE);
			sb.append(",");
			sb.append("[BLCR_CODE]"+BLCR_CODE);
			sb.append(",");
			sb.append("[JOB_SITE_NAME]"+JOB_SITE_NAME);
			sb.append(",");
			sb.append("[RLCM_CLNT_SECT_CODE]"+RLCM_CLNT_SECT_CODE);
			sb.append(",");
			sb.append("[PRB_RCST_SECT_CODE]"+PRB_RCST_SECT_CODE);
			sb.append(",");
			sb.append("[HBBY_NAME]"+HBBY_NAME);
			sb.append(",");
			sb.append("[HOUS_PSSN_CODE]"+HOUS_PSSN_CODE);
			sb.append(",");
			sb.append("[TRDG_MTV_SECT_CODE]"+TRDG_MTV_SECT_CODE);
			sb.append(",");
			sb.append("[MOBL_PHON_HOLD_SECT_CODE]"+MOBL_PHON_HOLD_SECT_CODE);
			sb.append(",");
			sb.append("[EMAL_HOLD_SECT_CODE]"+EMAL_HOLD_SECT_CODE);
			sb.append(",");
			sb.append("[CLCR_MNCN_MEAN_CODE]"+CLCR_MNCN_MEAN_CODE);
			sb.append(",");
			sb.append("[SOLR_LUNR_CLNR_SECT_CODE]"+SOLR_LUNR_CLNR_SECT_CODE);
			sb.append(",");
			sb.append("[NDVL_IFMN_USE_ASNT_YN1]"+NDVL_IFMN_USE_ASNT_YN1);
			sb.append(",");
			sb.append("[NDVL_IFMN_USE_ASNT_YN2]"+NDVL_IFMN_USE_ASNT_YN2);
			sb.append(",");
			sb.append("[NDVL_IFMN_USE_ASNT_YN3]"+NDVL_IFMN_USE_ASNT_YN3);
			sb.append(",");
			sb.append("[MAIN_CLNT_YN]"+MAIN_CLNT_YN);
			sb.append(",");
			sb.append("[A_STX_TXTN_YN]"+A_STX_TXTN_YN);
			sb.append(",");
			sb.append("[A_STX_TXTN_AMNT]"+A_STX_TXTN_AMNT);
			sb.append(",");
			sb.append("[HOME_PSTL_CODE]"+HOME_PSTL_CODE);
			sb.append(",");
			sb.append("[HOME_ADRS]"+HOME_ADRS);
			sb.append(",");
			sb.append("[SITE_PSTL_CODE]"+SITE_PSTL_CODE);
			sb.append(",");
			sb.append("[SITE_ADRS]"+SITE_ADRS);
			sb.append(",");
			sb.append("[EMAL_ADRS]"+EMAL_ADRS);
			sb.append(",");
			sb.append("[EMAL_SNBC_YN]"+EMAL_SNBC_YN);
			sb.append(",");
			sb.append("[MOBL_PHON_NO]"+MOBL_PHON_NO);
			sb.append(",");
			sb.append("[HOME_PHON_NO]"+HOME_PHON_NO);
			sb.append(",");
			sb.append("[HOME_FAX_NO]"+HOME_FAX_NO);
			sb.append(",");
			sb.append("[SITE_PHON_NO]"+SITE_PHON_NO);
			sb.append(",");
			sb.append("[SITE_FAX_NO]"+SITE_FAX_NO);
			sb.append(",");
			sb.append("[A_GNDR_SECT_NAME]"+A_GNDR_SECT_NAME);
			sb.append(",");
			sb.append("[A_CURR_AGE]"+A_CURR_AGE);
			sb.append(",");
			sb.append("[AGE]"+AGE);
			sb.append(",");
			sb.append("[ETF_RISK_ANCM_YN]"+ETF_RISK_ANCM_YN);
			sb.append(",");
			sb.append("[MNCL_NAME]"+MNCL_NAME);
			sb.append(",");
			sb.append("[FRGN_STCK_CTRT_YN]"+FRGN_STCK_CTRT_YN);
			sb.append(",");
			sb.append("[RGSN_DATE]"+RGSN_DATE);
			sb.append(",");
			sb.append("[PHC_SVC_NAME]"+PHC_SVC_NAME);
			sb.append("}");	
			return sb.toString();
		}
		

		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("CLNT_NAME",CLNT_NAME);
			m.put("ENG_CLNT_NAME",ENG_CLNT_NAME);
			m.put("BRDT",BRDT);
			m.put("CLNT_CLSN_NAME",CLNT_CLSN_NAME);
			m.put("A_INVR_SECT_CODE_NAME",A_INVR_SECT_CODE_NAME);
			m.put("A_NTNY_NAME",A_NTNY_NAME);
			m.put("A_RSDT_CNTY_NAME",A_RSDT_CNTY_NAME);
			m.put("A_NATV_OR_FRGR_SECT_NAME",A_NATV_OR_FRGR_SECT_NAME);
			m.put("PRMT_RSDT_YN",PRMT_RSDT_YN);
			m.put("WDNG_STAS_SECT_CODE",WDNG_STAS_SECT_CODE);
			m.put("WDNG_CMRT_DATE",WDNG_CMRT_DATE);
			m.put("JOB_CODE",JOB_CODE);
			m.put("CLAS_CODE",CLAS_CODE);
			m.put("BLCR_CODE",BLCR_CODE);
			m.put("JOB_SITE_NAME",JOB_SITE_NAME);
			m.put("RLCM_CLNT_SECT_CODE",RLCM_CLNT_SECT_CODE);
			m.put("PRB_RCST_SECT_CODE",PRB_RCST_SECT_CODE);
			m.put("HBBY_NAME",HBBY_NAME);
			m.put("HOUS_PSSN_CODE",HOUS_PSSN_CODE);
			m.put("TRDG_MTV_SECT_CODE",TRDG_MTV_SECT_CODE);
			m.put("MOBL_PHON_HOLD_SECT_CODE",MOBL_PHON_HOLD_SECT_CODE);
			m.put("EMAL_HOLD_SECT_CODE",EMAL_HOLD_SECT_CODE);
			m.put("CLCR_MNCN_MEAN_CODE",CLCR_MNCN_MEAN_CODE);
			m.put("SOLR_LUNR_CLNR_SECT_CODE",SOLR_LUNR_CLNR_SECT_CODE);
			m.put("NDVL_IFMN_USE_ASNT_YN1",NDVL_IFMN_USE_ASNT_YN1);
			m.put("NDVL_IFMN_USE_ASNT_YN2",NDVL_IFMN_USE_ASNT_YN2);
			m.put("NDVL_IFMN_USE_ASNT_YN3",NDVL_IFMN_USE_ASNT_YN3);
			m.put("MAIN_CLNT_YN",MAIN_CLNT_YN);
			m.put("A_STX_TXTN_YN",A_STX_TXTN_YN);
			m.put("A_STX_TXTN_AMNT",A_STX_TXTN_AMNT);
			m.put("HOME_PSTL_CODE",HOME_PSTL_CODE);
			m.put("HOME_ADRS",HOME_ADRS);
			m.put("SITE_PSTL_CODE",SITE_PSTL_CODE);
			m.put("SITE_ADRS",SITE_ADRS);
			m.put("EMAL_ADRS",EMAL_ADRS);
			m.put("EMAL_SNBC_YN",EMAL_SNBC_YN);
			m.put("MOBL_PHON_NO",MOBL_PHON_NO);
			m.put("HOME_PHON_NO",HOME_PHON_NO);
			m.put("HOME_FAX_NO",HOME_FAX_NO);
			m.put("SITE_PHON_NO",SITE_PHON_NO);
			m.put("SITE_FAX_NO",SITE_FAX_NO);
			m.put("A_GNDR_SECT_NAME",A_GNDR_SECT_NAME);
			m.put("A_CURR_AGE",A_CURR_AGE);
			m.put("AGE",AGE);
			m.put("ETF_RISK_ANCM_YN",ETF_RISK_ANCM_YN);
			m.put("MNCL_NAME",MNCL_NAME);
			m.put("FRGN_STCK_CTRT_YN",FRGN_STCK_CTRT_YN);
			m.put("RGSN_DATE",RGSN_DATE);
			m.put("PHC_SVC_NAME",PHC_SVC_NAME);
			
			return m;
		}
	}
	
	/* constructor */
	public Sgd1611pVO (String ipAddr, String userID, String mediaType) {
		setHeaderUserID(userID);
		setHeaderTrCode("sgd1611p");
		setHeaderDestination((char)'6');
		setHeaderPacketDataType((char)0);
		setHeaderMediaType(mediaType);
		setHeaderIpAddr(ipAddr);
		setCharSet("1");
        setIsActive("0");		
	}

	/* constructor */
	public Sgd1611pVO (String ipAddr, String userID, String mediaType, String brchCD, String deptCD, String acctCD) {
		setHeaderUserID(userID);
		setHeaderTrCode("sgd1611p");
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
				MCAUtil.writeString(inRec1.CRM_CLNT_ID, 19, bos);
			}
		}
		
		return bos.toByteArray();
	}
		
	/* parse */
	public void parse(StringInputStream sis) throws Exception {
		
		int cntRecord=0;
		
		// OutRec1
		outRec1.CLNT_NAME = StringUtil.changBlankToDash(sis.readString(90));
		outRec1.ENG_CLNT_NAME = StringUtil.changBlankToDash(sis.readString(60));
		outRec1.BRDT = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.CLNT_CLSN_NAME = StringUtil.changBlankToDash(sis.readString(30));
		outRec1.A_INVR_SECT_CODE_NAME = StringUtil.changBlankToDash(sis.readString(60));
		outRec1.A_NTNY_NAME = StringUtil.changBlankToDash(sis.readString(60));
		outRec1.A_RSDT_CNTY_NAME = StringUtil.changBlankToDash(sis.readString(60));
		outRec1.A_NATV_OR_FRGR_SECT_NAME = StringUtil.changBlankToDash(sis.readString(60));
		outRec1.PRMT_RSDT_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.WDNG_STAS_SECT_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.WDNG_CMRT_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.JOB_CODE = StringUtil.changBlankToDash(sis.readString(3));
		outRec1.CLAS_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.BLCR_CODE = StringUtil.changBlankToDash(sis.readString(12));
		outRec1.JOB_SITE_NAME = StringUtil.changBlankToDash(sis.readString(30));
		outRec1.RLCM_CLNT_SECT_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.PRB_RCST_SECT_CODE = StringUtil.changBlankToDash(sis.readString(2));
		outRec1.HBBY_NAME = StringUtil.changBlankToDash(sis.readString(30));
		outRec1.HOUS_PSSN_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.TRDG_MTV_SECT_CODE = StringUtil.changBlankToDash(sis.readString(2));
		outRec1.MOBL_PHON_HOLD_SECT_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.EMAL_HOLD_SECT_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.CLCR_MNCN_MEAN_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.SOLR_LUNR_CLNR_SECT_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.NDVL_IFMN_USE_ASNT_YN1 = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.NDVL_IFMN_USE_ASNT_YN2 = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.NDVL_IFMN_USE_ASNT_YN3 = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.MAIN_CLNT_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.A_STX_TXTN_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.A_STX_TXTN_AMNT = StringUtil.changBlankToDash(sis.readString(18));
		outRec1.HOME_PSTL_CODE = StringUtil.changBlankToDash(sis.readString(6));
		outRec1.HOME_ADRS = StringUtil.changBlankToDash(sis.readString(150));
		outRec1.SITE_PSTL_CODE = StringUtil.changBlankToDash(sis.readString(6));
		outRec1.SITE_ADRS = StringUtil.changBlankToDash(sis.readString(150));
		outRec1.EMAL_ADRS = StringUtil.changBlankToDash(sis.readString(150));
		outRec1.EMAL_SNBC_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.MOBL_PHON_NO = StringUtil.changBlankToDash(sis.readString(20));
		outRec1.HOME_PHON_NO = StringUtil.changBlankToDash(sis.readString(20));
		outRec1.HOME_FAX_NO = StringUtil.changBlankToDash(sis.readString(20));
		outRec1.SITE_PHON_NO = StringUtil.changBlankToDash(sis.readString(20));
		outRec1.SITE_FAX_NO = StringUtil.changBlankToDash(sis.readString(20));
		outRec1.A_GNDR_SECT_NAME = StringUtil.changBlankToDash(sis.readString(10));
		outRec1.A_CURR_AGE = StringUtil.changBlankToDash(sis.readString(13));
		outRec1.AGE = StringUtil.changBlankToDash(sis.readString(6));
		outRec1.ETF_RISK_ANCM_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.MNCL_NAME = StringUtil.changBlankToDash(sis.readString(60));
		outRec1.FRGN_STCK_CTRT_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.RGSN_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.PHC_SVC_NAME = StringUtil.changBlankToDash(sis.readString(100));

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
