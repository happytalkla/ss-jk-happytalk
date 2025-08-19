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
 * Tran Code     : sgd0030p
 * Tran Name     : 종합잔고( 상품유형별)조회
 * Tran Type     : 0
 * Domain ID     : IRN
 * Target Server : 6
 * Customization : FALSE
 */ 
public class Sgd0030pVO extends VOSupport {
	
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
		private String           A_RFRN_DATE1;
		private String           A_RFRN_DATE2;
		private String           CLNT_ENTY_ID;
		private String           CRM_CLNT_SECT_CODE;
		private String           ACTI_CTNT;
		private String           CNSD_CLNT_ICLS_YN;
		private String           EMPY_NO;
		private String           CRM_TCH_PATH_SECT_CODE;
		private String           TCH_CHNL_SECT_CODE;
		private String           A_NXT_DATE;
		private String           A_LEN_6_CNTI_RFRN_KYVL;
		private String           A_NXT_CLNT_ENTY_ID;
		private String           A_LEN_20_CNTI_RFRN_KYVL;
		private String           A_ACKN_YN;
		private String           A_RFRN_SECT_CODE;
			

		private String pText;
		
		/* Getters/Setters */
		public void setData(
				String           A_RFRN_DATE1,            
				String           A_RFRN_DATE2,            
				String           CLNT_ENTY_ID,            
				String           CRM_CLNT_SECT_CODE,      
				String           ACTI_CTNT,               
				String           CNSD_CLNT_ICLS_YN,       
				String           EMPY_NO,                 
				String           CRM_TCH_PATH_SECT_CODE,  
				String           TCH_CHNL_SECT_CODE,      
				String           A_NXT_DATE,              
				String           A_LEN_6_CNTI_RFRN_KYVL,  
				String           A_NXT_CLNT_ENTY_ID,      
				String           A_LEN_20_CNTI_RFRN_KYVL, 
				String           A_ACKN_YN,               
				String           A_RFRN_SECT_CODE
 		) {
			this.A_RFRN_DATE1= A_RFRN_DATE1                       ;
			this.A_RFRN_DATE2= A_RFRN_DATE2                       ;
			this.CLNT_ENTY_ID= CLNT_ENTY_ID                       ;
			this.CRM_CLNT_SECT_CODE= CRM_CLNT_SECT_CODE           ;
			this.ACTI_CTNT= ACTI_CTNT                             ;
			this.CNSD_CLNT_ICLS_YN= CNSD_CLNT_ICLS_YN             ;
			this.EMPY_NO= EMPY_NO                                 ;
			this.CRM_TCH_PATH_SECT_CODE= CRM_TCH_PATH_SECT_CODE   ;
			this.TCH_CHNL_SECT_CODE= TCH_CHNL_SECT_CODE           ;
			this.A_NXT_DATE= A_NXT_DATE                           ;
			this.A_LEN_6_CNTI_RFRN_KYVL= A_LEN_6_CNTI_RFRN_KYVL   ;
			this.A_NXT_CLNT_ENTY_ID= A_NXT_CLNT_ENTY_ID           ;
			this.A_LEN_20_CNTI_RFRN_KYVL= A_LEN_20_CNTI_RFRN_KYVL ;
			this.A_ACKN_YN= A_ACKN_YN                             ;
			this.A_RFRN_SECT_CODE= A_RFRN_SECT_CODE               ;

		}
		public String getPText() {
			return this.pText;
		}
		public void setPText(String pText) {
			this.pText = pText;
		}
		
		public String getA_RFRN_DATE1() {
			return A_RFRN_DATE1;
		}
		public void setA_RFRN_DATE1(String a_RFRN_DATE1) {
			A_RFRN_DATE1 = a_RFRN_DATE1;
		}
		public String getA_RFRN_DATE2() {
			return A_RFRN_DATE2;
		}
		public void setA_RFRN_DATE2(String a_RFRN_DATE2) {
			A_RFRN_DATE2 = a_RFRN_DATE2;
		}
		public String getCLNT_ENTY_ID() {
			return CLNT_ENTY_ID;
		}
		public void setCLNT_ENTY_ID(String cLNT_ENTY_ID) {
			CLNT_ENTY_ID = cLNT_ENTY_ID;
		}
		public String getCRM_CLNT_SECT_CODE() {
			return CRM_CLNT_SECT_CODE;
		}
		public void setCRM_CLNT_SECT_CODE(String cRM_CLNT_SECT_CODE) {
			CRM_CLNT_SECT_CODE = cRM_CLNT_SECT_CODE;
		}
		public String getACTI_CTNT() {
			return ACTI_CTNT;
		}
		public void setACTI_CTNT(String aCTI_CTNT) {
			ACTI_CTNT = aCTI_CTNT;
		}
		public String getCNSD_CLNT_ICLS_YN() {
			return CNSD_CLNT_ICLS_YN;
		}
		public void setCNSD_CLNT_ICLS_YN(String cNSD_CLNT_ICLS_YN) {
			CNSD_CLNT_ICLS_YN = cNSD_CLNT_ICLS_YN;
		}
		public String getEMPY_NO() {
			return EMPY_NO;
		}
		public void setEMPY_NO(String eMPY_NO) {
			EMPY_NO = eMPY_NO;
		}
		public String getCRM_TCH_PATH_SECT_CODE() {
			return CRM_TCH_PATH_SECT_CODE;
		}
		public void setCRM_TCH_PATH_SECT_CODE(String cRM_TCH_PATH_SECT_CODE) {
			CRM_TCH_PATH_SECT_CODE = cRM_TCH_PATH_SECT_CODE;
		}
		public String getTCH_CHNL_SECT_CODE() {
			return TCH_CHNL_SECT_CODE;
		}
		public void setTCH_CHNL_SECT_CODE(String tCH_CHNL_SECT_CODE) {
			TCH_CHNL_SECT_CODE = tCH_CHNL_SECT_CODE;
		}
		public String getA_NXT_DATE() {
			return A_NXT_DATE;
		}
		public void setA_NXT_DATE(String a_NXT_DATE) {
			A_NXT_DATE = a_NXT_DATE;
		}
		public String getA_LEN_6_CNTI_RFRN_KYVL() {
			return A_LEN_6_CNTI_RFRN_KYVL;
		}
		public void setA_LEN_6_CNTI_RFRN_KYVL(String a_LEN_6_CNTI_RFRN_KYVL) {
			A_LEN_6_CNTI_RFRN_KYVL = a_LEN_6_CNTI_RFRN_KYVL;
		}
		public String getA_NXT_CLNT_ENTY_ID() {
			return A_NXT_CLNT_ENTY_ID;
		}
		public void setA_NXT_CLNT_ENTY_ID(String a_NXT_CLNT_ENTY_ID) {
			A_NXT_CLNT_ENTY_ID = a_NXT_CLNT_ENTY_ID;
		}
		public String getA_LEN_20_CNTI_RFRN_KYVL() {
			return A_LEN_20_CNTI_RFRN_KYVL;
		}
		public void setA_LEN_20_CNTI_RFRN_KYVL(String a_LEN_20_CNTI_RFRN_KYVL) {
			A_LEN_20_CNTI_RFRN_KYVL = a_LEN_20_CNTI_RFRN_KYVL;
		}
		public String getA_ACKN_YN() {
			return A_ACKN_YN;
		}
		public void setA_ACKN_YN(String a_ACKN_YN) {
			A_ACKN_YN = a_ACKN_YN;
		}
		public String getA_RFRN_SECT_CODE() {
			return A_RFRN_SECT_CODE;
		}
		public void setA_RFRN_SECT_CODE(String a_RFRN_SECT_CODE) {
			A_RFRN_SECT_CODE = a_RFRN_SECT_CODE;
		}
		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			if (pText != null) {
				sb.append("[pText]"+pText);
			} else {
				sb.append("[A_RFRN_DATE1]"+A_RFRN_DATE1);
				sb.append(",");
				sb.append("[A_RFRN_DATE2]"+A_RFRN_DATE2);
				sb.append(",");
				sb.append("[CLNT_ENTY_ID]"+CLNT_ENTY_ID);
				sb.append(",");
				sb.append("[CRM_CLNT_SECT_CODE]"+CRM_CLNT_SECT_CODE);
				sb.append(",");
				sb.append("[ACTI_CTNT]"+ACTI_CTNT);
				sb.append(",");
				sb.append("[CNSD_CLNT_ICLS_YN]"+CNSD_CLNT_ICLS_YN);
				sb.append(",");
				sb.append("[EMPY_NO]"+EMPY_NO);
				sb.append(",");
				sb.append("[CRM_TCH_PATH_SECT_CODE]"+CRM_TCH_PATH_SECT_CODE);
				sb.append(",");
				sb.append("[TCH_CHNL_SECT_CODE]"+TCH_CHNL_SECT_CODE);
				sb.append(",");
				sb.append("[A_NXT_DATE]"+A_NXT_DATE);
				sb.append(",");
				sb.append("[A_LEN_6_CNTI_RFRN_KYVL]"+A_LEN_6_CNTI_RFRN_KYVL);
				sb.append(",");
				sb.append("[A_NXT_CLNT_ENTY_ID]"+A_NXT_CLNT_ENTY_ID);
				sb.append(",");
				sb.append("[A_LEN_20_CNTI_RFRN_KYVL]"+A_LEN_20_CNTI_RFRN_KYVL);
				sb.append(",");
				sb.append("[A_ACKN_YN]"+A_ACKN_YN);
				sb.append(",");
				sb.append("[A_RFRN_SECT_CODE]"+A_RFRN_SECT_CODE);
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
		private String            A_NXT_DATE;
		private String            A_LEN_6_CNTI_RFRN_KYVL;
		private String            A_NXT_CLNT_ENTY_ID;
		private String            A_LEN_20_CNTI_RFRN_KYVL  ;


		/* Getters/Setters */
		public void setData(
				String           A_NXT_DATE                ,
				String           A_LEN_6_CNTI_RFRN_KYVL    ,
				String           A_NXT_CLNT_ENTY_ID        ,
				String           A_LEN_20_CNTI_RFRN_KYVL  

		 ) {                                                                        
					this.A_NXT_DATE =             A_NXT_DATE                        ;
					this.A_LEN_6_CNTI_RFRN_KYVL =     A_LEN_6_CNTI_RFRN_KYVL        ;
					this.A_NXT_CLNT_ENTY_ID =     A_NXT_CLNT_ENTY_ID                ;
					this.A_LEN_20_CNTI_RFRN_KYVL =     A_LEN_20_CNTI_RFRN_KYVL      ;
					
					
					
					
					
					
					

		}
		

		public String getA_NXT_DATE() {
			return A_NXT_DATE;
		}


		public void setA_NXT_DATE(String a_NXT_DATE) {
			A_NXT_DATE = a_NXT_DATE;
		}


		public String getA_LEN_6_CNTI_RFRN_KYVL() {
			return A_LEN_6_CNTI_RFRN_KYVL;
		}


		public void setA_LEN_6_CNTI_RFRN_KYVL(String a_LEN_6_CNTI_RFRN_KYVL) {
			A_LEN_6_CNTI_RFRN_KYVL = a_LEN_6_CNTI_RFRN_KYVL;
		}


		public String getA_NXT_CLNT_ENTY_ID() {
			return A_NXT_CLNT_ENTY_ID;
		}


		public void setA_NXT_CLNT_ENTY_ID(String a_NXT_CLNT_ENTY_ID) {
			A_NXT_CLNT_ENTY_ID = a_NXT_CLNT_ENTY_ID;
		}


		public String getA_LEN_20_CNTI_RFRN_KYVL() {
			return A_LEN_20_CNTI_RFRN_KYVL;
		}


		public void setA_LEN_20_CNTI_RFRN_KYVL(String a_LEN_20_CNTI_RFRN_KYVL) {
			A_LEN_20_CNTI_RFRN_KYVL = a_LEN_20_CNTI_RFRN_KYVL;
		}


		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("[A_NXT_DATE]"+A_NXT_DATE);
			sb.append(",");
			sb.append("[A_LEN_6_CNTI_RFRN_KYVL]"+A_LEN_6_CNTI_RFRN_KYVL);
			sb.append(",");
			sb.append("[A_NXT_CLNT_ENTY_ID]"+A_NXT_CLNT_ENTY_ID);
			sb.append(",");
			sb.append("[A_LEN_20_CNTI_RFRN_KYVL]"+A_LEN_20_CNTI_RFRN_KYVL);
			sb.append(",");
			sb.append("}");
			return sb.toString();
		}

		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("A_NXT_DATE",A_NXT_DATE);
			m.put("A_LEN_6_CNTI_RFRN_KYVL",A_LEN_6_CNTI_RFRN_KYVL);
			m.put("A_NXT_CLNT_ENTY_ID",A_NXT_CLNT_ENTY_ID);
			m.put("A_LEN_20_CNTI_RFRN_KYVL",A_LEN_20_CNTI_RFRN_KYVL);
			return m;
		}
	}
	
	public static class OutRec2 { 
		
		// Constructor
		public OutRec2() {
		}

		/* Attributes */
		private String A_SECT_CODE;					// 구분코드, CHAR(1)
		private String CLNT_CNSL_PRHS_ID;			// 고객상담이력ID, CHAR(20)
		private String TCH_DATE;					// 접촉일자, CHAR(8)
		private String TCH_TM;						// 접촉시각, CHAR(6)
		private String CLNT_ENTY_ID;				// 고객엔티티ID, CHAR(19)	
		private String CLNT_NAME;					// 고객명, CHAR(90)
		private String ACTI_TYPE_CODE;				// 활동유형코드, CHAR(2)
		private String ACTI_TTL_NAME;				// 활동제목명, CHAR(150)	
		private String ACTN_CMPL_CTNT;				// 조치완료내용, CHAR(2000)	
		private String BUY_SELL_TYPE_CODE;			// 매수매도유형코드, CHAR(1)	
		private String PRDT_NAME;					// 상품명, CHAR(90)	
		private String RCMN_RESN_CTNT;				// 추천사유내용, CHAR(300)
		private String CRM_TCH_PATH_SECT_CODE;		// CRM접촉경로구분코드, CHAR(2)	
		private String ONLN_CLNT_RACT_CODE;			// 온라인고객반응코드, CHAR(5)	
		private String A_UNSL_YN;					// 개봉여부, CHAR(1)	
		private String TCH_TYPE_SECT_CODE;			// 접촉유형구분코드, CHAR(2)	
		private String ONLN_TCH_TYPE_CODE;			// 온라인접촉유형코드, CHAR(2)	
		private String ONLN_TCH_RSLT_CODE;			// 온라인접촉결과코드, CHAR(2)	
		private String PCPR_ID;						// 처리자ID, CHAR(20)	
		private String PRCE_EMPY_NAME;				// 처리사원명, CHAR(60)	
		private String A_MODE_FILE_PATH_NAME;		// 양식파일경로명, CHAR(150)	
		private String DPCH_SQNO;					// 발송일련번호, CHAR(11)	
		private String FLFL_CMPG_NO;				// 수행캠페인번호, CHAR(16)	
		private String FLFL_CMPG_ACTI_NO;			// 수행캠페인활동번호, CHAR(16)	

		/* Getters/Setters */
		public void setData(
				String A_SECT_CODE,
				String CLNT_CNSL_PRHS_ID,
				String TCH_DATE,				
				String TCH_TM,					
				String CLNT_ENTY_ID,			
				String CLNT_NAME,				
				String ACTI_TYPE_CODE,			
				String ACTI_TTL_NAME,			
				String ACTN_CMPL_CTNT,			
				String BUY_SELL_TYPE_CODE,		
				String PRDT_NAME,				
				String RCMN_RESN_CTNT,			
				String CRM_TCH_PATH_SECT_CODE,	
				String ONLN_CLNT_RACT_CODE,		
				String A_UNSL_YN,				
				String TCH_TYPE_SECT_CODE,		
				String ONLN_TCH_TYPE_CODE,		
				String ONLN_TCH_RSLT_CODE,		
				String PCPR_ID,					
				String PRCE_EMPY_NAME,			
				String A_MODE_FILE_PATH_NAME,	
				String DPCH_SQNO,				
				String FLFL_CMPG_NO,			
				String FLFL_CMPG_ACTI_NO
			 ) {
				this.A_SECT_CODE = A_SECT_CODE;
				this.CLNT_CNSL_PRHS_ID = CLNT_CNSL_PRHS_ID;
				this.TCH_DATE = TCH_DATE;
				this.TCH_TM = TCH_TM;					
				this.CLNT_ENTY_ID = CLNT_ENTY_ID;	
				this.CLNT_NAME = CLNT_NAME;			
				this.ACTI_TYPE_CODE = ACTI_TYPE_CODE;			
				this.ACTI_TTL_NAME = ACTI_TTL_NAME;			
				this.ACTN_CMPL_CTNT = ACTN_CMPL_CTNT;		
				this.BUY_SELL_TYPE_CODE = BUY_SELL_TYPE_CODE;	
				this.PRDT_NAME = PRDT_NAME;				
				this.RCMN_RESN_CTNT = RCMN_RESN_CTNT;		
				this.CRM_TCH_PATH_SECT_CODE = CRM_TCH_PATH_SECT_CODE;
				this.ONLN_CLNT_RACT_CODE = ONLN_CLNT_RACT_CODE;	
				this.A_UNSL_YN = A_UNSL_YN;				
				this.TCH_TYPE_SECT_CODE = TCH_TYPE_SECT_CODE;
				this.ONLN_TCH_TYPE_CODE = ONLN_TCH_TYPE_CODE;		
				this.ONLN_TCH_RSLT_CODE = ONLN_TCH_RSLT_CODE;		
				this.PCPR_ID = PCPR_ID;				
				this.PRCE_EMPY_NAME = PRCE_EMPY_NAME;		
				this.A_MODE_FILE_PATH_NAME = A_MODE_FILE_PATH_NAME;
				this.DPCH_SQNO = DPCH_SQNO;				
				this.FLFL_CMPG_NO = FLFL_CMPG_NO;		
				this.FLFL_CMPG_ACTI_NO = FLFL_CMPG_ACTI_NO;
		}
		public String getA_SECT_CODE() { return A_SECT_CODE; }
		public void setA_SECT_CODE(String A_SECT_CODE) { this.A_SECT_CODE = A_SECT_CODE; }
		public String getCLNT_CNSL_PRHS_ID() { return CLNT_CNSL_PRHS_ID; }
		public void setCLNT_CNSL_PRHS_ID(String CLNT_CNSL_PRHS_ID) { this.CLNT_CNSL_PRHS_ID = CLNT_CNSL_PRHS_ID; }
		public String getTCH_DATE() { return TCH_DATE; }
		public void setTCH_DATE(String TCH_DATE) { this.TCH_DATE = TCH_DATE; }
		public String getTCH_TM() { return TCH_TM; }
		public void setTCH_TM(String TCH_TM) { this.TCH_TM = TCH_TM; }
		public String getCLNT_ENTY_ID() { return CLNT_ENTY_ID; }
		public void setCLNT_ENTY_ID(String CLNT_ENTY_ID) { this.CLNT_ENTY_ID = CLNT_ENTY_ID; }
		public String getCLNT_NAME() { return CLNT_NAME; }
		public void setCLNT_NAME(String CLNT_NAME) { this.CLNT_NAME = CLNT_NAME; }
		public String getACTI_TYPE_CODE() { return ACTI_TYPE_CODE; }
		public void setACTI_TYPE_CODE(String ACTI_TYPE_CODE) { this.ACTI_TYPE_CODE = ACTI_TYPE_CODE; }
		public String getACTI_TTL_NAME() { return ACTI_TTL_NAME; }
		public void setACTI_TTL_NAME(String ACTI_TTL_NAME) { this.ACTI_TTL_NAME = ACTI_TTL_NAME; }
		public String getACTN_CMPL_CTNT() { return ACTN_CMPL_CTNT; }
		public void setACTN_CMPL_CTNT(String ACTN_CMPL_CTNT) { this.ACTN_CMPL_CTNT = ACTN_CMPL_CTNT; }
		public String getBUY_SELL_TYPE_CODE() { return BUY_SELL_TYPE_CODE; }
		public void setBUY_SELL_TYPE_CODE(String BUY_SELL_TYPE_CODE) { this.BUY_SELL_TYPE_CODE = BUY_SELL_TYPE_CODE; }
		public String getPRDT_NAME() { return PRDT_NAME; }
		public void setPRDT_NAME(String PRDT_NAME) { this.PRDT_NAME = PRDT_NAME; }
		public String getRCMN_RESN_CTNT() { return RCMN_RESN_CTNT; }
		public void setRCMN_RESN_CTNT(String RCMN_RESN_CTNT) { this.RCMN_RESN_CTNT = RCMN_RESN_CTNT; }
		public String getCRM_TCH_PATH_SECT_CODE() { return CRM_TCH_PATH_SECT_CODE; }
		public void setCRM_TCH_PATH_SECT_CODE(String CRM_TCH_PATH_SECT_CODE) { this.CRM_TCH_PATH_SECT_CODE = CRM_TCH_PATH_SECT_CODE; }
		public String getONLN_CLNT_RACT_CODE() { return ONLN_CLNT_RACT_CODE; }
		public void setONLN_CLNT_RACT_CODE(String ONLN_CLNT_RACT_CODE) { this.ONLN_CLNT_RACT_CODE = ONLN_CLNT_RACT_CODE; }
		public String getA_UNSL_YN() { return A_UNSL_YN; }
		public void setA_UNSL_YN(String A_UNSL_YN) { this.A_UNSL_YN = A_UNSL_YN; }
		public String getTCH_TYPE_SECT_CODE() { return TCH_TYPE_SECT_CODE; }
		public void setTCH_TYPE_SECT_CODE(String TCH_TYPE_SECT_CODE) { this.TCH_TYPE_SECT_CODE = TCH_TYPE_SECT_CODE; }
		public String getONLN_TCH_TYPE_CODE() { return ONLN_TCH_TYPE_CODE; }
		public void setONLN_TCH_TYPE_CODE(String ONLN_TCH_TYPE_CODE) { this.ONLN_TCH_TYPE_CODE = ONLN_TCH_TYPE_CODE; }
		public String getONLN_TCH_RSLT_CODE() { return ONLN_TCH_RSLT_CODE; }
		public void setONLN_TCH_RSLT_CODE(String ONLN_TCH_RSLT_CODE) { this.ONLN_TCH_RSLT_CODE = ONLN_TCH_RSLT_CODE; }
		public String getPCPR_ID() { return PCPR_ID; }
		public void setPCPR_ID(String PCPR_ID) { this.PCPR_ID = PCPR_ID; }
		public String getPRCE_EMPY_NAME() { return PRCE_EMPY_NAME; }
		public void setPRCE_EMPY_NAME(String PRCE_EMPY_NAME) { this.PRCE_EMPY_NAME = PRCE_EMPY_NAME; }
		public String getA_MODE_FILE_PATH_NAME() { return A_MODE_FILE_PATH_NAME; }
		public void setA_MODE_FILE_PATH_NAME(String A_MODE_FILE_PATH_NAME) { this.A_MODE_FILE_PATH_NAME = A_MODE_FILE_PATH_NAME; }
		public String getDPCH_SQNO() { return DPCH_SQNO; }
		public void setDPCH_SQNO(String DPCH_SQNO) { this.DPCH_SQNO = DPCH_SQNO; }
		public String getFLFL_CMPG_NO() { return FLFL_CMPG_NO; }
		public void setFLFL_CMPG_NO(String FLFL_CMPG_NO) { this.FLFL_CMPG_NO = FLFL_CMPG_NO; }
		public String getFLFL_CMPG_ACTI_NO() { return FLFL_CMPG_ACTI_NO; }
		public void setFLFL_CMPG_ACTI_NO(String FLFL_CMPG_ACTI_NO) { this.FLFL_CMPG_ACTI_NO = FLFL_CMPG_ACTI_NO; }		

		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("[A_SECT_CODE]"+A_SECT_CODE);
			sb.append(",");
			sb.append("[CLNT_CNSL_PRHS_ID]"+CLNT_CNSL_PRHS_ID);
			sb.append(",");
			sb.append("[TCH_DATE]"+TCH_DATE);
			sb.append(",");
			sb.append("[TCH_TM]"+TCH_TM);
			sb.append(",");
			sb.append("[CLNT_ENTY_ID]"+CLNT_ENTY_ID);
			sb.append(",");
			sb.append("[CLNT_NAME]"+CLNT_NAME);
			sb.append(",");
			sb.append("[ACTI_TYPE_CODE]"+ACTI_TYPE_CODE);
			sb.append(",");
			sb.append("[ACTI_TTL_NAME]"+ACTI_TTL_NAME);
			sb.append(",");
			sb.append("[ACTN_CMPL_CTNT]"+ACTN_CMPL_CTNT);
			sb.append(",");
			sb.append("[BUY_SELL_TYPE_CODE]"+BUY_SELL_TYPE_CODE);
			sb.append(",");
			sb.append("[PRDT_NAME]"+PRDT_NAME);
			sb.append(",");
			sb.append("[RCMN_RESN_CTNT]"+RCMN_RESN_CTNT);
			sb.append(",");
			sb.append("[CRM_TCH_PATH_SECT_CODE]"+CRM_TCH_PATH_SECT_CODE);
			sb.append(",");
			sb.append("[ONLN_CLNT_RACT_CODE]"+ONLN_CLNT_RACT_CODE);
			sb.append(",");
			sb.append("[A_UNSL_YN]"+A_UNSL_YN);
			sb.append(",");
			sb.append("[TCH_TYPE_SECT_CODE]"+TCH_TYPE_SECT_CODE);
			sb.append(",");
			sb.append("[ONLN_TCH_TYPE_CODE]"+ONLN_TCH_TYPE_CODE);
			sb.append(",");
			sb.append("[ONLN_TCH_RSLT_CODE]"+ONLN_TCH_RSLT_CODE);
			sb.append(",");
			sb.append("[PCPR_ID]"+PCPR_ID);
			sb.append(",");
			sb.append("[PRCE_EMPY_NAME]"+PRCE_EMPY_NAME);
			sb.append(",");
			sb.append("[A_MODE_FILE_PATH_NAME]"+A_MODE_FILE_PATH_NAME);
			sb.append(",");
			sb.append("[DPCH_SQNO]"+DPCH_SQNO);
			sb.append(",");
			sb.append("[FLFL_CMPG_NO]"+FLFL_CMPG_NO);
			sb.append(",");
			sb.append("[FLFL_CMPG_ACTI_NO]"+FLFL_CMPG_ACTI_NO);
			sb.append("}");
			return sb.toString();
		}

		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("A_SECT_CODE", A_SECT_CODE);
			m.put("CLNT_CNSL_PRHS_ID", CLNT_CNSL_PRHS_ID);
			m.put("TCH_DATE", TCH_DATE);
			m.put("TCH_TM", TCH_TM);
			m.put("CLNT_ENTY_ID", CLNT_ENTY_ID);
			m.put("CLNT_NAME", CLNT_NAME);
			m.put("ACTI_TYPE_CODE", ACTI_TYPE_CODE);
			m.put("ACTI_TTL_NAME", ACTI_TTL_NAME);
			m.put("ACTN_CMPL_CTNT", ACTN_CMPL_CTNT);
			m.put("BUY_SELL_TYPE_CODE", BUY_SELL_TYPE_CODE);
			m.put("PRDT_NAME", PRDT_NAME);
			m.put("RCMN_RESN_CTNT", RCMN_RESN_CTNT);
			m.put("CRM_TCH_PATH_SECT_CODE", CRM_TCH_PATH_SECT_CODE);
			m.put("ONLN_CLNT_RACT_CODE", ONLN_CLNT_RACT_CODE);
			m.put("A_UNSL_YN", A_UNSL_YN);
			m.put("TCH_TYPE_SECT_CODE", TCH_TYPE_SECT_CODE);
			m.put("ONLN_TCH_TYPE_CODE", ONLN_TCH_TYPE_CODE);
			m.put("ONLN_TCH_RSLT_CODE", ONLN_TCH_RSLT_CODE);
			m.put("PCPR_ID", PCPR_ID);
			m.put("PRCE_EMPY_NAME", PRCE_EMPY_NAME);
			m.put("A_MODE_FILE_PATH_NAME", A_MODE_FILE_PATH_NAME);
			m.put("DPCH_SQNO", DPCH_SQNO);
			m.put("FLFL_CMPG_NO", FLFL_CMPG_NO);
			m.put("FLFL_CMPG_ACTI_NO", FLFL_CMPG_ACTI_NO);
			return m;
		}
	}

	/* constructor */
	public Sgd0030pVO (String ipAddr, String userID, String mediaType) {
		setHeaderUserID(userID);
		setHeaderTrCode("sgd0030p");
		setHeaderDestination((char)'6');
		setHeaderPacketDataType((char)0);
		setHeaderMediaType(mediaType);
		setHeaderIpAddr(ipAddr);
		setCharSet("1");
        setIsActive("0");		
	}

	/* constructor */
	public Sgd0030pVO (String ipAddr, String userID, String mediaType, String brchCD, String deptCD, String acctCD) {
		setHeaderUserID(userID);
		setHeaderTrCode("sgd0030p");
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
				MCAUtil.writeString(inRec1.A_RFRN_DATE1, 20, bos);
				MCAUtil.writeString(inRec1.A_RFRN_DATE2, 20, bos);
				MCAUtil.writeString(inRec1.CLNT_ENTY_ID, 20, bos);
				MCAUtil.writeString(inRec1.CRM_CLNT_SECT_CODE, 20, bos);
				MCAUtil.writeString(inRec1.ACTI_CTNT, 20, bos);
				MCAUtil.writeString(inRec1.CNSD_CLNT_ICLS_YN, 20, bos);
				MCAUtil.writeString(inRec1.EMPY_NO, 20, bos);
				MCAUtil.writeString(inRec1.CRM_TCH_PATH_SECT_CODE, 20, bos);
				MCAUtil.writeString(inRec1.TCH_CHNL_SECT_CODE, 20, bos);
				MCAUtil.writeString(inRec1.A_NXT_DATE, 20, bos);
				MCAUtil.writeString(inRec1.A_LEN_6_CNTI_RFRN_KYVL, 20, bos);
				MCAUtil.writeString(inRec1.A_NXT_CLNT_ENTY_ID, 20, bos);
				MCAUtil.writeString(inRec1.A_LEN_20_CNTI_RFRN_KYVL, 20, bos);
				MCAUtil.writeString(inRec1.A_ACKN_YN, 20, bos);
				MCAUtil.writeString(inRec1.A_RFRN_SECT_CODE, 20, bos);
			}
		}
		
		return bos.toByteArray();
	}
		
	/* parse */
	public void parse(StringInputStream sis) throws Exception {
		
		int cntRecord=0;

		// OutRec1
		outRec1.A_NXT_DATE               = sis.readString(8);
		outRec1.A_LEN_6_CNTI_RFRN_KYVL   = sis.readString(6);
		outRec1.A_NXT_CLNT_ENTY_ID       = sis.readString(19);
		outRec1.A_LEN_20_CNTI_RFRN_KYVL  = sis.readString(20);
		
		// OutRec2
		try {
			cntRecord=Integer.parseInt(sis.readString(4).trim());
		} catch (NumberFormatException e) {
			throw new Exception("OutRec2 NumberFormatException");
		}
		
		if (cntRecord < 0) cntRecord = 0;
		
		for (int i=0;i<cntRecord;i++) {
			OutRec2 outRecord = new OutRec2();
			outRecord.A_SECT_CODE				= sis.readString(1);
			outRecord.CLNT_CNSL_PRHS_ID			= sis.readString(20);
			outRecord.TCH_DATE				 	= sis.readString(8);
			outRecord.TCH_TM					= sis.readString(6);
			outRecord.CLNT_ENTY_ID			 	= sis.readString(19);
			outRecord.CLNT_NAME				 	= sis.readString(90);
			outRecord.ACTI_TYPE_CODE			= sis.readString(2);
			outRecord.ACTI_TTL_NAME			 	= sis.readString(150);
			outRecord.ACTN_CMPL_CTNT			= sis.readString(2000);
			outRecord.BUY_SELL_TYPE_CODE		= sis.readString(1);
			outRecord.PRDT_NAME				 	= sis.readString(90);
			outRecord.RCMN_RESN_CTNT			= sis.readString(300);
			outRecord.CRM_TCH_PATH_SECT_CODE 	= sis.readString(2);
			outRecord.ONLN_CLNT_RACT_CODE		= sis.readString(5);
			outRecord.A_UNSL_YN				 	= sis.readString(1);
			outRecord.TCH_TYPE_SECT_CODE		= sis.readString(2);
			outRecord.ONLN_TCH_TYPE_CODE		= sis.readString(2);
			outRecord.ONLN_TCH_RSLT_CODE		= sis.readString(2);
			outRecord.PCPR_ID					= sis.readString(20);
			outRecord.PRCE_EMPY_NAME			= sis.readString(60);
			outRecord.A_MODE_FILE_PATH_NAME	 	= sis.readString(150);
			outRecord.DPCH_SQNO				 	= sis.readString(11);
			outRecord.FLFL_CMPG_NO		   		= sis.readString(16);
			outRecord.FLFL_CMPG_ACTI_NO      	= sis.readString(16);
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

		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();

		for (int i=0;i<outRec2.size();i++) {
			list1.add(outRec2.get(i).toMap());
		}
		m.put("outRec2",list1);

		return m;
	}
}
