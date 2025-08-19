/*
* This class is automatically generated.
* Please do not modify the original source.
* 
* Supervisor      : 모승환
* Last modified date  : 2016.07.29
* VOGenerator     : v2.2.5
*
* Release info    :
* v2.2.5   	20160729  - OCSP 관련 단축/Full서명 기능 추가
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
 * Tran Code     : maa0421p
 * Tran Name     : 계좌정보조회
 * Tran Type     : 0
 * Domain ID     : MAA
 * Target Server : 6
 * Customization : FALSE
 */ 
public class Maa0421pVO extends VOSupport {
	
	/* Input Records */
	private InRec1 inRec1 = new InRec1();

	/* Output Records */
	private OutRec1 outRec1 = new OutRec1();
	private OutRec2 outRec2 = new OutRec2();
	private OutRec3 outRec3 = new OutRec3();
	private OutRec4 outRec4 = new OutRec4();
	private OutRec6 outRec6 = new OutRec6();
	private OutRec7 outRec7 = new OutRec7();
	private ArrayList<OutRec8> outRec8 = new ArrayList<OutRec8>();
	
	/* Get Instances */
	public InRec1 getInRec1() { return inRec1; }
	public OutRec1 getOutRec1() { return outRec1; }
	public OutRec2 getOutRec2() { return outRec2; }
	public OutRec3 getOutRec3() { return outRec3; }
	public OutRec4 getOutRec4() { return outRec4; }
	public OutRec6 getOutRec6() { return outRec6; }
	public OutRec7 getOutRec7() { return outRec7; }
	public ArrayList<OutRec8> getOutRec8() { return outRec8; }

	/* Set Instances */
	public void setInRec1(InRec1 inRec1) { this.inRec1 = inRec1; }
	public void setOutRec1(OutRec1 outRec1) { this.outRec1 = outRec1; }
	public void setOutRec2(OutRec2 outRec2) { this.outRec2 = outRec2; }
	public void setOutRec3(OutRec3 outRec3) { this.outRec3 = outRec3; }
	public void setOutRec4(OutRec4 outRec4) { this.outRec4 = outRec4; }
	public void setOutRec6(OutRec6 outRec6) { this.outRec6 = outRec6; }
	public void setOutRec7(OutRec7 outRec7) { this.outRec7 = outRec7; }
	public void setOutRec8(ArrayList<OutRec8> outRec8) { this.outRec8 = outRec8; }

	/* InRec Classes */	
	public static class InRec1 {

		// Constructor
		public InRec1() {
			pText = null;
		}

		/* Attributes */
		private String ACNT_NO;			// 계좌번호, CHAR(20)				
		private String A_PSWD_CRYP;			// 비밀번호암호문, CHAR(44)				

		private String pText;
		
		/* Getters/Setters */
		public void setData(
			String ACNT_NO,
			String A_PSWD_CRYP
 		) {
			this.ACNT_NO = ACNT_NO;
			this.A_PSWD_CRYP = A_PSWD_CRYP;
		}
		public String getPText() {
			return this.pText;
		}
		public void setPText(String pText) {
			this.pText = pText;
		}
		public String getACNT_NO() { return ACNT_NO; }
		public void setACNT_NO(String ACNT_NO) { this.ACNT_NO = ACNT_NO; }				
		public String getA_PSWD_CRYP() { return A_PSWD_CRYP; }
		public void setA_PSWD_CRYP(String A_PSWD_CRYP) { this.A_PSWD_CRYP = A_PSWD_CRYP; }				

		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			if (pText != null) {
				sb.append("[pText]"+pText);
			} else {
				sb.append("[ACNT_NO]"+ACNT_NO);
				sb.append(",");
				sb.append("[A_PSWD_CRYP]"+A_PSWD_CRYP);
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
		private String CLNT_RNNO;			// 고객실명번호, CHAR(13)	
		private String VIP_CLNT_YN;			// VIP고객여부, CHAR(1)	
		private String MNGG_BRNH_ENTY_CODE;			// 관리점코드, CHAR(5)	
		private String A_MNGG_BRNH_ENTY_NAME;			// 관리점명, CHAR(60)	
		private String BFMV_DMBE_CODE;			// 이동전상세관리점코드, CHAR(5)	
		private String A_PREV_MNGG_BRNH_ENTY_NAME;			// 이전관리점명, CHAR(90)	
		private String PRFT_CNTR_ENTY_CODE;			// 실적점코드, CHAR(5)	
		private String A_PRFT_CNTR_ENTY_NAME;			// 실적점명, CHAR(60)	
		private String A_RM_ACRS_BNCD;			// RM실적지점코드, CHAR(5)	
		private String A_RM_PRFT_CNTR_ENTY_NAME;			// RM실적점명, CHAR(30)	
		private String A_MAIN_MNGR_EMPY_NO;			// 정관리자사원번호, CHAR(16)	
		private String A_MAIN_MNGR_NAME;			// 정관리자명, CHAR(90)	
		private String SBMN_EMPY_NO;			// 부관리자사원번호, CHAR(16)	
		private String A_SBMN_EMPY_NAME;			// 부관리자사원명, CHAR(30)	
		private String ATRN_EMPY_NO;			// 유치사원번호, CHAR(16)	
		private String A_ATPS_NAME;			// 유치자명, CHAR(60)	
		private String INVT_INVT_AGNT_EMPY_NO;			// 투자권유대리인사원번호, CHAR(16)	
		private String INVT_INVT_AGNT_NAME;			// 투자권유대리인명, CHAR(60)	
		private String A_RM_MNGR_EMPY_NO;			// RM관리자사원번호, CHAR(16)	
		private String A_RM_MNGR_EMPY_NAME;			// RM관리자사원명, CHAR(30)	
		private String ACNT_STAS_SECT_CODE;			// 계좌상태구분코드, CHAR(2)	
		private String A_ACNT_STAS_NAME;			// 계좌상태명, CHAR(30)	
		private String ACNT_TYPE_CODE;			// 계좌유형코드, CHAR(4)	
		private String A_ACNT_TYPE_NAME;			// 계좌유형명, CHAR(90)	
		private String INVR_CLSN_CODE;			// 투자자분류코드, CHAR(4)	
		private String A_INVR_CLSN_NAME;			// 투자자분류명, CHAR(60)	
		private String ACCT_YN;			// 사고여부, CHAR(1)	
		private String A_FNC_IFMN_PRTC_YN;			// 금융정보보호여부, CHAR(1)	
		private String ACNT_OPNG_DATE;			// 계좌개설일자, CHAR(8)	
		private String A_ACNT_OPNG_BRNH_NAME;			// 계좌개설지점명, CHAR(60)	
		private String A_ACNT_UNFC_DATE;			// 계좌통합일자, CHAR(8)	
		private String ACNT_CLSG_DATE;			// 계좌폐쇄일자, CHAR(8)	
		private String A_ACNT_CLSG_BRNH_NAME;			// 계좌폐쇄지점명, CHAR(90)	
		private String LAST_TRDG_DATE;			// 최종거래일자, CHAR(8)	
		private String A_CRDT_CTRT_RGSN_DATE;			// 신용약정등록일자, CHAR(8)	
		private String A_RGSN_BRNH_NAME;			// 등록지점명, CHAR(90)	
		private String A_FUNS_ATTR_SECT_CODE;			// 자금속성구분코드, CHAR(1)	
		private String A_FUNS_ATTR_NAME;			// 자금속성명, CHAR(30)	
		private String ERNR_SECT_CODE;			// 소득자구분코드, CHAR(3)	
		private String A_ERNR_SECT_NAME;			// 소득자구분명, CHAR(60)	
		private String FRGN_CRNY_TRDG_ACON_TYPE_CODE;			// 외화거래계정유형코드, CHAR(2)	
		private String A_FRGN_CRNY_BSWR_SECT_NAME;			// 외화업무구분명, CHAR(90)	
		private String A_AGNT_YN;			// 대리인여부, CHAR(1)	
		private String AGNT_SECT_CODE;			// 대리인구분코드, CHAR(1)	
		private String A_AGNT_SECT_NAME;			// 대리인구분명, CHAR(30)	
		private String AGNT_RNNO;			// 대리인실명번호, CHAR(13)	
		private String PSTN_UNIT_TYPE_CODE;			// 포지션유닛유형코드, CHAR(4)	
		private String BANK_NAME;			// 은행명, VARCHAR(60)	
		private String BKPN_NAME;			// 부기명, VARCHAR(150)	
		private String A_MRGN_CLTN_SECT_CODE_NAME;			// 증거금징수구분코드명, CHAR(30)	
		private String A_WTHN_ENTS_YN_NAME;			// 원천징수위임여부명, CHAR(30)	
		private String A_STX_NAME;			// 거래세명, CHAR(30)	
		private String A_ODLT_TXTN_SECT_NAME;			// 단주과세구분명, CHAR(30)	
		private String A_DSCS_FEE_SECT_CODE_NAME;			// 협의수수료구분코드명, CHAR(30)	
		private String A_FEE_SECT_NAME;			// 수수료구분명, CHAR(30)	
		private String A_CTRC_STAS_CODE_NAME;			// 계약상태코드명, CHAR(30)	
		private String CTRT_DATE;			// 약정일자, CHAR(8)	
		private String CTRT_XPRN_DATE;			// 약정만기일자, CHAR(8)	
		private String CLNT_NAME;			// 고객명, VARCHAR(90)	
		private String A_MEDI_KIND_NAME;			// 매체종류명, CHAR(90)	
		private String A_RLNM_YN;			// 실명여부, CHAR(1)	
		private String A_RLNM_CNFM_NAME;			// 실명확인명, CHAR(30)	
		private String CPAC_RLNM_CFDT;			// 제휴계좌실명확인일자, CHAR(8)	
		private String A_RESP_MSG_CTNT1;			// 응답메시지내용1, CHAR(60)	
		private String EMAL_SNBC_YN;			// E-mail반송여부, CHAR(1)	
		private String SNBC_YN;			// 반송여부, CHAR(1)	
		private String A_ACNT_NO20;			// 계좌번호20, CHAR(20)	
		private String A_OPNG_DATE2;			// 개설일자2, CHAR(8)	
		private String BANK_PASS_ACNT_NO;			// 은행패스계좌번호, CHAR(20)	
		private String A_OPNG_SECT_NAME;			// 개설구분명, CHAR(60)	
		private String A_SNOT_PRCE_EMPY_NAME;			// 파출처리사원명, CHAR(60)	
		private String A_MEDI_HOLD_YN;			// 매체보유여부, CHAR(1)	
		private String ACNT_ENNM;			// 계좌영문명, VARCHAR(90)	
		private String A_ORTR_ACNT_NO;			// 원거래계좌번호, CHAR(20)	
		private String A_RP_DLNG_ALWD_YN;			// RP매매가능여부, CHAR(1)	
		private String A_ACNT_FRM_NAME;			// 계좌형태상세명, CHAR(30)	
		private String CMA_ATMC_SBTT_YN;			// CMA자동대체여부, CHAR(1)	
		private String GLEG_LINK_YN;			// 골든에그연계여부, CHAR(1)	
		private String ALGR_ACNT_YN;			// 알고리즘계좌여부, CHAR(1)	
		private String BSNS_PRPS_ACNT_YN;			// 사업용계좌여부, CHAR(1)	
		private String BANK_ACNT_NAME;			// 금현물거래은행계좌, VARCHAR(60)	
		private String TRDG_PRPS_NAME;			// 금현물매매목적명, VARCHAR(60)	
		private String A_CLNT_ACNT_SECT_CODE;			// 선물계좌구분코드, CHAR(1)	
		private String A_LN_BANK_NAME;			// 대출은행명, CHAR(30)	
		private String LN_BANK_ACNT_NO;			// 대출은행계좌번호, CHAR(20)	
		private String A_VRAC_BANK_NAME;			// 외화가상계좌은행명, CHAR(30)	
		private String A_VRAC_NO;			// 외화가상계좌번호, CHAR(20)	
		private String RFNC_CTNT;			// QI 유형, VARCHAR(75)	

		/* Getters/Setters */
		public void setData(
			String CLNT_RNNO,
			String VIP_CLNT_YN,
			String MNGG_BRNH_ENTY_CODE,
			String A_MNGG_BRNH_ENTY_NAME,
			String BFMV_DMBE_CODE,
			String A_PREV_MNGG_BRNH_ENTY_NAME,
			String PRFT_CNTR_ENTY_CODE,
			String A_PRFT_CNTR_ENTY_NAME,
			String A_RM_ACRS_BNCD,
			String A_RM_PRFT_CNTR_ENTY_NAME,
			String A_MAIN_MNGR_EMPY_NO,
			String A_MAIN_MNGR_NAME,
			String SBMN_EMPY_NO,
			String A_SBMN_EMPY_NAME,
			String ATRN_EMPY_NO,
			String A_ATPS_NAME,
			String INVT_INVT_AGNT_EMPY_NO,
			String INVT_INVT_AGNT_NAME,
			String A_RM_MNGR_EMPY_NO,
			String A_RM_MNGR_EMPY_NAME,
			String ACNT_STAS_SECT_CODE,
			String A_ACNT_STAS_NAME,
			String ACNT_TYPE_CODE,
			String A_ACNT_TYPE_NAME,
			String INVR_CLSN_CODE,
			String A_INVR_CLSN_NAME,
			String ACCT_YN,
			String A_FNC_IFMN_PRTC_YN,
			String ACNT_OPNG_DATE,
			String A_ACNT_OPNG_BRNH_NAME,
			String A_ACNT_UNFC_DATE,
			String ACNT_CLSG_DATE,
			String A_ACNT_CLSG_BRNH_NAME,
			String LAST_TRDG_DATE,
			String A_CRDT_CTRT_RGSN_DATE,
			String A_RGSN_BRNH_NAME,
			String A_FUNS_ATTR_SECT_CODE,
			String A_FUNS_ATTR_NAME,
			String ERNR_SECT_CODE,
			String A_ERNR_SECT_NAME,
			String FRGN_CRNY_TRDG_ACON_TYPE_CODE,
			String A_FRGN_CRNY_BSWR_SECT_NAME,
			String A_AGNT_YN,
			String AGNT_SECT_CODE,
			String A_AGNT_SECT_NAME,
			String AGNT_RNNO,
			String PSTN_UNIT_TYPE_CODE,
			String BANK_NAME,
			String BKPN_NAME,
			String A_MRGN_CLTN_SECT_CODE_NAME,
			String A_WTHN_ENTS_YN_NAME,
			String A_STX_NAME,
			String A_ODLT_TXTN_SECT_NAME,
			String A_DSCS_FEE_SECT_CODE_NAME,
			String A_FEE_SECT_NAME,
			String A_CTRC_STAS_CODE_NAME,
			String CTRT_DATE,
			String CTRT_XPRN_DATE,
			String CLNT_NAME,
			String A_MEDI_KIND_NAME,
			String A_RLNM_YN,
			String A_RLNM_CNFM_NAME,
			String CPAC_RLNM_CFDT,
			String A_RESP_MSG_CTNT1,
			String EMAL_SNBC_YN,
			String SNBC_YN,
			String A_ACNT_NO20,
			String A_OPNG_DATE2,
			String BANK_PASS_ACNT_NO,
			String A_OPNG_SECT_NAME,
			String A_SNOT_PRCE_EMPY_NAME,
			String A_MEDI_HOLD_YN,
			String ACNT_ENNM,
			String A_ORTR_ACNT_NO,
			String A_RP_DLNG_ALWD_YN,
			String A_ACNT_FRM_NAME,
			String CMA_ATMC_SBTT_YN,
			String GLEG_LINK_YN,
			String ALGR_ACNT_YN,
			String BSNS_PRPS_ACNT_YN,
			String BANK_ACNT_NAME,
			String TRDG_PRPS_NAME,
			String A_CLNT_ACNT_SECT_CODE,
			String A_LN_BANK_NAME,
			String LN_BANK_ACNT_NO,
			String A_VRAC_BANK_NAME,
			String A_VRAC_NO,
			String RFNC_CTNT
		 ) {
			this.CLNT_RNNO = CLNT_RNNO;
			this.VIP_CLNT_YN = VIP_CLNT_YN;
			this.MNGG_BRNH_ENTY_CODE = MNGG_BRNH_ENTY_CODE;
			this.A_MNGG_BRNH_ENTY_NAME = A_MNGG_BRNH_ENTY_NAME;
			this.BFMV_DMBE_CODE = BFMV_DMBE_CODE;
			this.A_PREV_MNGG_BRNH_ENTY_NAME = A_PREV_MNGG_BRNH_ENTY_NAME;
			this.PRFT_CNTR_ENTY_CODE = PRFT_CNTR_ENTY_CODE;
			this.A_PRFT_CNTR_ENTY_NAME = A_PRFT_CNTR_ENTY_NAME;
			this.A_RM_ACRS_BNCD = A_RM_ACRS_BNCD;
			this.A_RM_PRFT_CNTR_ENTY_NAME = A_RM_PRFT_CNTR_ENTY_NAME;
			this.A_MAIN_MNGR_EMPY_NO = A_MAIN_MNGR_EMPY_NO;
			this.A_MAIN_MNGR_NAME = A_MAIN_MNGR_NAME;
			this.SBMN_EMPY_NO = SBMN_EMPY_NO;
			this.A_SBMN_EMPY_NAME = A_SBMN_EMPY_NAME;
			this.ATRN_EMPY_NO = ATRN_EMPY_NO;
			this.A_ATPS_NAME = A_ATPS_NAME;
			this.INVT_INVT_AGNT_EMPY_NO = INVT_INVT_AGNT_EMPY_NO;
			this.INVT_INVT_AGNT_NAME = INVT_INVT_AGNT_NAME;
			this.A_RM_MNGR_EMPY_NO = A_RM_MNGR_EMPY_NO;
			this.A_RM_MNGR_EMPY_NAME = A_RM_MNGR_EMPY_NAME;
			this.ACNT_STAS_SECT_CODE = ACNT_STAS_SECT_CODE;
			this.A_ACNT_STAS_NAME = A_ACNT_STAS_NAME;
			this.ACNT_TYPE_CODE = ACNT_TYPE_CODE;
			this.A_ACNT_TYPE_NAME = A_ACNT_TYPE_NAME;
			this.INVR_CLSN_CODE = INVR_CLSN_CODE;
			this.A_INVR_CLSN_NAME = A_INVR_CLSN_NAME;
			this.ACCT_YN = ACCT_YN;
			this.A_FNC_IFMN_PRTC_YN = A_FNC_IFMN_PRTC_YN;
			this.ACNT_OPNG_DATE = ACNT_OPNG_DATE;
			this.A_ACNT_OPNG_BRNH_NAME = A_ACNT_OPNG_BRNH_NAME;
			this.A_ACNT_UNFC_DATE = A_ACNT_UNFC_DATE;
			this.ACNT_CLSG_DATE = ACNT_CLSG_DATE;
			this.A_ACNT_CLSG_BRNH_NAME = A_ACNT_CLSG_BRNH_NAME;
			this.LAST_TRDG_DATE = LAST_TRDG_DATE;
			this.A_CRDT_CTRT_RGSN_DATE = A_CRDT_CTRT_RGSN_DATE;
			this.A_RGSN_BRNH_NAME = A_RGSN_BRNH_NAME;
			this.A_FUNS_ATTR_SECT_CODE = A_FUNS_ATTR_SECT_CODE;
			this.A_FUNS_ATTR_NAME = A_FUNS_ATTR_NAME;
			this.ERNR_SECT_CODE = ERNR_SECT_CODE;
			this.A_ERNR_SECT_NAME = A_ERNR_SECT_NAME;
			this.FRGN_CRNY_TRDG_ACON_TYPE_CODE = FRGN_CRNY_TRDG_ACON_TYPE_CODE;
			this.A_FRGN_CRNY_BSWR_SECT_NAME = A_FRGN_CRNY_BSWR_SECT_NAME;
			this.A_AGNT_YN = A_AGNT_YN;
			this.AGNT_SECT_CODE = AGNT_SECT_CODE;
			this.A_AGNT_SECT_NAME = A_AGNT_SECT_NAME;
			this.AGNT_RNNO = AGNT_RNNO;
			this.PSTN_UNIT_TYPE_CODE = PSTN_UNIT_TYPE_CODE;
			this.BANK_NAME = BANK_NAME;
			this.BKPN_NAME = BKPN_NAME;
			this.A_MRGN_CLTN_SECT_CODE_NAME = A_MRGN_CLTN_SECT_CODE_NAME;
			this.A_WTHN_ENTS_YN_NAME = A_WTHN_ENTS_YN_NAME;
			this.A_STX_NAME = A_STX_NAME;
			this.A_ODLT_TXTN_SECT_NAME = A_ODLT_TXTN_SECT_NAME;
			this.A_DSCS_FEE_SECT_CODE_NAME = A_DSCS_FEE_SECT_CODE_NAME;
			this.A_FEE_SECT_NAME = A_FEE_SECT_NAME;
			this.A_CTRC_STAS_CODE_NAME = A_CTRC_STAS_CODE_NAME;
			this.CTRT_DATE = CTRT_DATE;
			this.CTRT_XPRN_DATE = CTRT_XPRN_DATE;
			this.CLNT_NAME = CLNT_NAME;
			this.A_MEDI_KIND_NAME = A_MEDI_KIND_NAME;
			this.A_RLNM_YN = A_RLNM_YN;
			this.A_RLNM_CNFM_NAME = A_RLNM_CNFM_NAME;
			this.CPAC_RLNM_CFDT = CPAC_RLNM_CFDT;
			this.A_RESP_MSG_CTNT1 = A_RESP_MSG_CTNT1;
			this.EMAL_SNBC_YN = EMAL_SNBC_YN;
			this.SNBC_YN = SNBC_YN;
			this.A_ACNT_NO20 = A_ACNT_NO20;
			this.A_OPNG_DATE2 = A_OPNG_DATE2;
			this.BANK_PASS_ACNT_NO = BANK_PASS_ACNT_NO;
			this.A_OPNG_SECT_NAME = A_OPNG_SECT_NAME;
			this.A_SNOT_PRCE_EMPY_NAME = A_SNOT_PRCE_EMPY_NAME;
			this.A_MEDI_HOLD_YN = A_MEDI_HOLD_YN;
			this.ACNT_ENNM = ACNT_ENNM;
			this.A_ORTR_ACNT_NO = A_ORTR_ACNT_NO;
			this.A_RP_DLNG_ALWD_YN = A_RP_DLNG_ALWD_YN;
			this.A_ACNT_FRM_NAME = A_ACNT_FRM_NAME;
			this.CMA_ATMC_SBTT_YN = CMA_ATMC_SBTT_YN;
			this.GLEG_LINK_YN = GLEG_LINK_YN;
			this.ALGR_ACNT_YN = ALGR_ACNT_YN;
			this.BSNS_PRPS_ACNT_YN = BSNS_PRPS_ACNT_YN;
			this.BANK_ACNT_NAME = BANK_ACNT_NAME;
			this.TRDG_PRPS_NAME = TRDG_PRPS_NAME;
			this.A_CLNT_ACNT_SECT_CODE = A_CLNT_ACNT_SECT_CODE;
			this.A_LN_BANK_NAME = A_LN_BANK_NAME;
			this.LN_BANK_ACNT_NO = LN_BANK_ACNT_NO;
			this.A_VRAC_BANK_NAME = A_VRAC_BANK_NAME;
			this.A_VRAC_NO = A_VRAC_NO;
			this.RFNC_CTNT = RFNC_CTNT;
		}
		public String getCLNT_RNNO() { return CLNT_RNNO; }
		public void setCLNT_RNNO(String CLNT_RNNO) { this.CLNT_RNNO = CLNT_RNNO; }				
		public String getVIP_CLNT_YN() { return VIP_CLNT_YN; }
		public void setVIP_CLNT_YN(String VIP_CLNT_YN) { this.VIP_CLNT_YN = VIP_CLNT_YN; }				
		public String getMNGG_BRNH_ENTY_CODE() { return MNGG_BRNH_ENTY_CODE; }
		public void setMNGG_BRNH_ENTY_CODE(String MNGG_BRNH_ENTY_CODE) { this.MNGG_BRNH_ENTY_CODE = MNGG_BRNH_ENTY_CODE; }				
		public String getA_MNGG_BRNH_ENTY_NAME() { return A_MNGG_BRNH_ENTY_NAME; }
		public void setA_MNGG_BRNH_ENTY_NAME(String A_MNGG_BRNH_ENTY_NAME) { this.A_MNGG_BRNH_ENTY_NAME = A_MNGG_BRNH_ENTY_NAME; }				
		public String getBFMV_DMBE_CODE() { return BFMV_DMBE_CODE; }
		public void setBFMV_DMBE_CODE(String BFMV_DMBE_CODE) { this.BFMV_DMBE_CODE = BFMV_DMBE_CODE; }				
		public String getA_PREV_MNGG_BRNH_ENTY_NAME() { return A_PREV_MNGG_BRNH_ENTY_NAME; }
		public void setA_PREV_MNGG_BRNH_ENTY_NAME(String A_PREV_MNGG_BRNH_ENTY_NAME) { this.A_PREV_MNGG_BRNH_ENTY_NAME = A_PREV_MNGG_BRNH_ENTY_NAME; }				
		public String getPRFT_CNTR_ENTY_CODE() { return PRFT_CNTR_ENTY_CODE; }
		public void setPRFT_CNTR_ENTY_CODE(String PRFT_CNTR_ENTY_CODE) { this.PRFT_CNTR_ENTY_CODE = PRFT_CNTR_ENTY_CODE; }				
		public String getA_PRFT_CNTR_ENTY_NAME() { return A_PRFT_CNTR_ENTY_NAME; }
		public void setA_PRFT_CNTR_ENTY_NAME(String A_PRFT_CNTR_ENTY_NAME) { this.A_PRFT_CNTR_ENTY_NAME = A_PRFT_CNTR_ENTY_NAME; }				
		public String getA_RM_ACRS_BNCD() { return A_RM_ACRS_BNCD; }
		public void setA_RM_ACRS_BNCD(String A_RM_ACRS_BNCD) { this.A_RM_ACRS_BNCD = A_RM_ACRS_BNCD; }				
		public String getA_RM_PRFT_CNTR_ENTY_NAME() { return A_RM_PRFT_CNTR_ENTY_NAME; }
		public void setA_RM_PRFT_CNTR_ENTY_NAME(String A_RM_PRFT_CNTR_ENTY_NAME) { this.A_RM_PRFT_CNTR_ENTY_NAME = A_RM_PRFT_CNTR_ENTY_NAME; }				
		public String getA_MAIN_MNGR_EMPY_NO() { return A_MAIN_MNGR_EMPY_NO; }
		public void setA_MAIN_MNGR_EMPY_NO(String A_MAIN_MNGR_EMPY_NO) { this.A_MAIN_MNGR_EMPY_NO = A_MAIN_MNGR_EMPY_NO; }				
		public String getA_MAIN_MNGR_NAME() { return A_MAIN_MNGR_NAME; }
		public void setA_MAIN_MNGR_NAME(String A_MAIN_MNGR_NAME) { this.A_MAIN_MNGR_NAME = A_MAIN_MNGR_NAME; }				
		public String getSBMN_EMPY_NO() { return SBMN_EMPY_NO; }
		public void setSBMN_EMPY_NO(String SBMN_EMPY_NO) { this.SBMN_EMPY_NO = SBMN_EMPY_NO; }				
		public String getA_SBMN_EMPY_NAME() { return A_SBMN_EMPY_NAME; }
		public void setA_SBMN_EMPY_NAME(String A_SBMN_EMPY_NAME) { this.A_SBMN_EMPY_NAME = A_SBMN_EMPY_NAME; }				
		public String getATRN_EMPY_NO() { return ATRN_EMPY_NO; }
		public void setATRN_EMPY_NO(String ATRN_EMPY_NO) { this.ATRN_EMPY_NO = ATRN_EMPY_NO; }				
		public String getA_ATPS_NAME() { return A_ATPS_NAME; }
		public void setA_ATPS_NAME(String A_ATPS_NAME) { this.A_ATPS_NAME = A_ATPS_NAME; }				
		public String getINVT_INVT_AGNT_EMPY_NO() { return INVT_INVT_AGNT_EMPY_NO; }
		public void setINVT_INVT_AGNT_EMPY_NO(String INVT_INVT_AGNT_EMPY_NO) { this.INVT_INVT_AGNT_EMPY_NO = INVT_INVT_AGNT_EMPY_NO; }				
		public String getINVT_INVT_AGNT_NAME() { return INVT_INVT_AGNT_NAME; }
		public void setINVT_INVT_AGNT_NAME(String INVT_INVT_AGNT_NAME) { this.INVT_INVT_AGNT_NAME = INVT_INVT_AGNT_NAME; }				
		public String getA_RM_MNGR_EMPY_NO() { return A_RM_MNGR_EMPY_NO; }
		public void setA_RM_MNGR_EMPY_NO(String A_RM_MNGR_EMPY_NO) { this.A_RM_MNGR_EMPY_NO = A_RM_MNGR_EMPY_NO; }				
		public String getA_RM_MNGR_EMPY_NAME() { return A_RM_MNGR_EMPY_NAME; }
		public void setA_RM_MNGR_EMPY_NAME(String A_RM_MNGR_EMPY_NAME) { this.A_RM_MNGR_EMPY_NAME = A_RM_MNGR_EMPY_NAME; }				
		public String getACNT_STAS_SECT_CODE() { return ACNT_STAS_SECT_CODE; }
		public void setACNT_STAS_SECT_CODE(String ACNT_STAS_SECT_CODE) { this.ACNT_STAS_SECT_CODE = ACNT_STAS_SECT_CODE; }				
		public String getA_ACNT_STAS_NAME() { return A_ACNT_STAS_NAME; }
		public void setA_ACNT_STAS_NAME(String A_ACNT_STAS_NAME) { this.A_ACNT_STAS_NAME = A_ACNT_STAS_NAME; }				
		public String getACNT_TYPE_CODE() { return ACNT_TYPE_CODE; }
		public void setACNT_TYPE_CODE(String ACNT_TYPE_CODE) { this.ACNT_TYPE_CODE = ACNT_TYPE_CODE; }				
		public String getA_ACNT_TYPE_NAME() { return A_ACNT_TYPE_NAME; }
		public void setA_ACNT_TYPE_NAME(String A_ACNT_TYPE_NAME) { this.A_ACNT_TYPE_NAME = A_ACNT_TYPE_NAME; }				
		public String getINVR_CLSN_CODE() { return INVR_CLSN_CODE; }
		public void setINVR_CLSN_CODE(String INVR_CLSN_CODE) { this.INVR_CLSN_CODE = INVR_CLSN_CODE; }				
		public String getA_INVR_CLSN_NAME() { return A_INVR_CLSN_NAME; }
		public void setA_INVR_CLSN_NAME(String A_INVR_CLSN_NAME) { this.A_INVR_CLSN_NAME = A_INVR_CLSN_NAME; }				
		public String getACCT_YN() { return ACCT_YN; }
		public void setACCT_YN(String ACCT_YN) { this.ACCT_YN = ACCT_YN; }				
		public String getA_FNC_IFMN_PRTC_YN() { return A_FNC_IFMN_PRTC_YN; }
		public void setA_FNC_IFMN_PRTC_YN(String A_FNC_IFMN_PRTC_YN) { this.A_FNC_IFMN_PRTC_YN = A_FNC_IFMN_PRTC_YN; }				
		public String getACNT_OPNG_DATE() { return ACNT_OPNG_DATE; }
		public void setACNT_OPNG_DATE(String ACNT_OPNG_DATE) { this.ACNT_OPNG_DATE = ACNT_OPNG_DATE; }				
		public String getA_ACNT_OPNG_BRNH_NAME() { return A_ACNT_OPNG_BRNH_NAME; }
		public void setA_ACNT_OPNG_BRNH_NAME(String A_ACNT_OPNG_BRNH_NAME) { this.A_ACNT_OPNG_BRNH_NAME = A_ACNT_OPNG_BRNH_NAME; }				
		public String getA_ACNT_UNFC_DATE() { return A_ACNT_UNFC_DATE; }
		public void setA_ACNT_UNFC_DATE(String A_ACNT_UNFC_DATE) { this.A_ACNT_UNFC_DATE = A_ACNT_UNFC_DATE; }				
		public String getACNT_CLSG_DATE() { return ACNT_CLSG_DATE; }
		public void setACNT_CLSG_DATE(String ACNT_CLSG_DATE) { this.ACNT_CLSG_DATE = ACNT_CLSG_DATE; }				
		public String getA_ACNT_CLSG_BRNH_NAME() { return A_ACNT_CLSG_BRNH_NAME; }
		public void setA_ACNT_CLSG_BRNH_NAME(String A_ACNT_CLSG_BRNH_NAME) { this.A_ACNT_CLSG_BRNH_NAME = A_ACNT_CLSG_BRNH_NAME; }				
		public String getLAST_TRDG_DATE() { return LAST_TRDG_DATE; }
		public void setLAST_TRDG_DATE(String LAST_TRDG_DATE) { this.LAST_TRDG_DATE = LAST_TRDG_DATE; }				
		public String getA_CRDT_CTRT_RGSN_DATE() { return A_CRDT_CTRT_RGSN_DATE; }
		public void setA_CRDT_CTRT_RGSN_DATE(String A_CRDT_CTRT_RGSN_DATE) { this.A_CRDT_CTRT_RGSN_DATE = A_CRDT_CTRT_RGSN_DATE; }				
		public String getA_RGSN_BRNH_NAME() { return A_RGSN_BRNH_NAME; }
		public void setA_RGSN_BRNH_NAME(String A_RGSN_BRNH_NAME) { this.A_RGSN_BRNH_NAME = A_RGSN_BRNH_NAME; }				
		public String getA_FUNS_ATTR_SECT_CODE() { return A_FUNS_ATTR_SECT_CODE; }
		public void setA_FUNS_ATTR_SECT_CODE(String A_FUNS_ATTR_SECT_CODE) { this.A_FUNS_ATTR_SECT_CODE = A_FUNS_ATTR_SECT_CODE; }				
		public String getA_FUNS_ATTR_NAME() { return A_FUNS_ATTR_NAME; }
		public void setA_FUNS_ATTR_NAME(String A_FUNS_ATTR_NAME) { this.A_FUNS_ATTR_NAME = A_FUNS_ATTR_NAME; }				
		public String getERNR_SECT_CODE() { return ERNR_SECT_CODE; }
		public void setERNR_SECT_CODE(String ERNR_SECT_CODE) { this.ERNR_SECT_CODE = ERNR_SECT_CODE; }				
		public String getA_ERNR_SECT_NAME() { return A_ERNR_SECT_NAME; }
		public void setA_ERNR_SECT_NAME(String A_ERNR_SECT_NAME) { this.A_ERNR_SECT_NAME = A_ERNR_SECT_NAME; }				
		public String getFRGN_CRNY_TRDG_ACON_TYPE_CODE() { return FRGN_CRNY_TRDG_ACON_TYPE_CODE; }
		public void setFRGN_CRNY_TRDG_ACON_TYPE_CODE(String FRGN_CRNY_TRDG_ACON_TYPE_CODE) { this.FRGN_CRNY_TRDG_ACON_TYPE_CODE = FRGN_CRNY_TRDG_ACON_TYPE_CODE; }				
		public String getA_FRGN_CRNY_BSWR_SECT_NAME() { return A_FRGN_CRNY_BSWR_SECT_NAME; }
		public void setA_FRGN_CRNY_BSWR_SECT_NAME(String A_FRGN_CRNY_BSWR_SECT_NAME) { this.A_FRGN_CRNY_BSWR_SECT_NAME = A_FRGN_CRNY_BSWR_SECT_NAME; }				
		public String getA_AGNT_YN() { return A_AGNT_YN; }
		public void setA_AGNT_YN(String A_AGNT_YN) { this.A_AGNT_YN = A_AGNT_YN; }				
		public String getAGNT_SECT_CODE() { return AGNT_SECT_CODE; }
		public void setAGNT_SECT_CODE(String AGNT_SECT_CODE) { this.AGNT_SECT_CODE = AGNT_SECT_CODE; }				
		public String getA_AGNT_SECT_NAME() { return A_AGNT_SECT_NAME; }
		public void setA_AGNT_SECT_NAME(String A_AGNT_SECT_NAME) { this.A_AGNT_SECT_NAME = A_AGNT_SECT_NAME; }				
		public String getAGNT_RNNO() { return AGNT_RNNO; }
		public void setAGNT_RNNO(String AGNT_RNNO) { this.AGNT_RNNO = AGNT_RNNO; }				
		public String getPSTN_UNIT_TYPE_CODE() { return PSTN_UNIT_TYPE_CODE; }
		public void setPSTN_UNIT_TYPE_CODE(String PSTN_UNIT_TYPE_CODE) { this.PSTN_UNIT_TYPE_CODE = PSTN_UNIT_TYPE_CODE; }				
		public String getBANK_NAME() { return BANK_NAME; }
		public void setBANK_NAME(String BANK_NAME) { this.BANK_NAME = BANK_NAME; }				
		public String getBKPN_NAME() { return BKPN_NAME; }
		public void setBKPN_NAME(String BKPN_NAME) { this.BKPN_NAME = BKPN_NAME; }				
		public String getA_MRGN_CLTN_SECT_CODE_NAME() { return A_MRGN_CLTN_SECT_CODE_NAME; }
		public void setA_MRGN_CLTN_SECT_CODE_NAME(String A_MRGN_CLTN_SECT_CODE_NAME) { this.A_MRGN_CLTN_SECT_CODE_NAME = A_MRGN_CLTN_SECT_CODE_NAME; }				
		public String getA_WTHN_ENTS_YN_NAME() { return A_WTHN_ENTS_YN_NAME; }
		public void setA_WTHN_ENTS_YN_NAME(String A_WTHN_ENTS_YN_NAME) { this.A_WTHN_ENTS_YN_NAME = A_WTHN_ENTS_YN_NAME; }				
		public String getA_STX_NAME() { return A_STX_NAME; }
		public void setA_STX_NAME(String A_STX_NAME) { this.A_STX_NAME = A_STX_NAME; }				
		public String getA_ODLT_TXTN_SECT_NAME() { return A_ODLT_TXTN_SECT_NAME; }
		public void setA_ODLT_TXTN_SECT_NAME(String A_ODLT_TXTN_SECT_NAME) { this.A_ODLT_TXTN_SECT_NAME = A_ODLT_TXTN_SECT_NAME; }				
		public String getA_DSCS_FEE_SECT_CODE_NAME() { return A_DSCS_FEE_SECT_CODE_NAME; }
		public void setA_DSCS_FEE_SECT_CODE_NAME(String A_DSCS_FEE_SECT_CODE_NAME) { this.A_DSCS_FEE_SECT_CODE_NAME = A_DSCS_FEE_SECT_CODE_NAME; }				
		public String getA_FEE_SECT_NAME() { return A_FEE_SECT_NAME; }
		public void setA_FEE_SECT_NAME(String A_FEE_SECT_NAME) { this.A_FEE_SECT_NAME = A_FEE_SECT_NAME; }				
		public String getA_CTRC_STAS_CODE_NAME() { return A_CTRC_STAS_CODE_NAME; }
		public void setA_CTRC_STAS_CODE_NAME(String A_CTRC_STAS_CODE_NAME) { this.A_CTRC_STAS_CODE_NAME = A_CTRC_STAS_CODE_NAME; }				
		public String getCTRT_DATE() { return CTRT_DATE; }
		public void setCTRT_DATE(String CTRT_DATE) { this.CTRT_DATE = CTRT_DATE; }				
		public String getCTRT_XPRN_DATE() { return CTRT_XPRN_DATE; }
		public void setCTRT_XPRN_DATE(String CTRT_XPRN_DATE) { this.CTRT_XPRN_DATE = CTRT_XPRN_DATE; }				
		public String getCLNT_NAME() { return CLNT_NAME; }
		public void setCLNT_NAME(String CLNT_NAME) { this.CLNT_NAME = CLNT_NAME; }				
		public String getA_MEDI_KIND_NAME() { return A_MEDI_KIND_NAME; }
		public void setA_MEDI_KIND_NAME(String A_MEDI_KIND_NAME) { this.A_MEDI_KIND_NAME = A_MEDI_KIND_NAME; }				
		public String getA_RLNM_YN() { return A_RLNM_YN; }
		public void setA_RLNM_YN(String A_RLNM_YN) { this.A_RLNM_YN = A_RLNM_YN; }				
		public String getA_RLNM_CNFM_NAME() { return A_RLNM_CNFM_NAME; }
		public void setA_RLNM_CNFM_NAME(String A_RLNM_CNFM_NAME) { this.A_RLNM_CNFM_NAME = A_RLNM_CNFM_NAME; }				
		public String getCPAC_RLNM_CFDT() { return CPAC_RLNM_CFDT; }
		public void setCPAC_RLNM_CFDT(String CPAC_RLNM_CFDT) { this.CPAC_RLNM_CFDT = CPAC_RLNM_CFDT; }				
		public String getA_RESP_MSG_CTNT1() { return A_RESP_MSG_CTNT1; }
		public void setA_RESP_MSG_CTNT1(String A_RESP_MSG_CTNT1) { this.A_RESP_MSG_CTNT1 = A_RESP_MSG_CTNT1; }				
		public String getEMAL_SNBC_YN() { return EMAL_SNBC_YN; }
		public void setEMAL_SNBC_YN(String EMAL_SNBC_YN) { this.EMAL_SNBC_YN = EMAL_SNBC_YN; }				
		public String getSNBC_YN() { return SNBC_YN; }
		public void setSNBC_YN(String SNBC_YN) { this.SNBC_YN = SNBC_YN; }				
		public String getA_ACNT_NO20() { return A_ACNT_NO20; }
		public void setA_ACNT_NO20(String A_ACNT_NO20) { this.A_ACNT_NO20 = A_ACNT_NO20; }				
		public String getA_OPNG_DATE2() { return A_OPNG_DATE2; }
		public void setA_OPNG_DATE2(String A_OPNG_DATE2) { this.A_OPNG_DATE2 = A_OPNG_DATE2; }				
		public String getBANK_PASS_ACNT_NO() { return BANK_PASS_ACNT_NO; }
		public void setBANK_PASS_ACNT_NO(String BANK_PASS_ACNT_NO) { this.BANK_PASS_ACNT_NO = BANK_PASS_ACNT_NO; }				
		public String getA_OPNG_SECT_NAME() { return A_OPNG_SECT_NAME; }
		public void setA_OPNG_SECT_NAME(String A_OPNG_SECT_NAME) { this.A_OPNG_SECT_NAME = A_OPNG_SECT_NAME; }				
		public String getA_SNOT_PRCE_EMPY_NAME() { return A_SNOT_PRCE_EMPY_NAME; }
		public void setA_SNOT_PRCE_EMPY_NAME(String A_SNOT_PRCE_EMPY_NAME) { this.A_SNOT_PRCE_EMPY_NAME = A_SNOT_PRCE_EMPY_NAME; }				
		public String getA_MEDI_HOLD_YN() { return A_MEDI_HOLD_YN; }
		public void setA_MEDI_HOLD_YN(String A_MEDI_HOLD_YN) { this.A_MEDI_HOLD_YN = A_MEDI_HOLD_YN; }				
		public String getACNT_ENNM() { return ACNT_ENNM; }
		public void setACNT_ENNM(String ACNT_ENNM) { this.ACNT_ENNM = ACNT_ENNM; }				
		public String getA_ORTR_ACNT_NO() { return A_ORTR_ACNT_NO; }
		public void setA_ORTR_ACNT_NO(String A_ORTR_ACNT_NO) { this.A_ORTR_ACNT_NO = A_ORTR_ACNT_NO; }				
		public String getA_RP_DLNG_ALWD_YN() { return A_RP_DLNG_ALWD_YN; }
		public void setA_RP_DLNG_ALWD_YN(String A_RP_DLNG_ALWD_YN) { this.A_RP_DLNG_ALWD_YN = A_RP_DLNG_ALWD_YN; }				
		public String getA_ACNT_FRM_NAME() { return A_ACNT_FRM_NAME; }
		public void setA_ACNT_FRM_NAME(String A_ACNT_FRM_NAME) { this.A_ACNT_FRM_NAME = A_ACNT_FRM_NAME; }				
		public String getCMA_ATMC_SBTT_YN() { return CMA_ATMC_SBTT_YN; }
		public void setCMA_ATMC_SBTT_YN(String CMA_ATMC_SBTT_YN) { this.CMA_ATMC_SBTT_YN = CMA_ATMC_SBTT_YN; }				
		public String getGLEG_LINK_YN() { return GLEG_LINK_YN; }
		public void setGLEG_LINK_YN(String GLEG_LINK_YN) { this.GLEG_LINK_YN = GLEG_LINK_YN; }				
		public String getALGR_ACNT_YN() { return ALGR_ACNT_YN; }
		public void setALGR_ACNT_YN(String ALGR_ACNT_YN) { this.ALGR_ACNT_YN = ALGR_ACNT_YN; }				
		public String getBSNS_PRPS_ACNT_YN() { return BSNS_PRPS_ACNT_YN; }
		public void setBSNS_PRPS_ACNT_YN(String BSNS_PRPS_ACNT_YN) { this.BSNS_PRPS_ACNT_YN = BSNS_PRPS_ACNT_YN; }				
		public String getBANK_ACNT_NAME() { return BANK_ACNT_NAME; }
		public void setBANK_ACNT_NAME(String BANK_ACNT_NAME) { this.BANK_ACNT_NAME = BANK_ACNT_NAME; }				
		public String getTRDG_PRPS_NAME() { return TRDG_PRPS_NAME; }
		public void setTRDG_PRPS_NAME(String TRDG_PRPS_NAME) { this.TRDG_PRPS_NAME = TRDG_PRPS_NAME; }				
		public String getA_CLNT_ACNT_SECT_CODE() { return A_CLNT_ACNT_SECT_CODE; }
		public void setA_CLNT_ACNT_SECT_CODE(String A_CLNT_ACNT_SECT_CODE) { this.A_CLNT_ACNT_SECT_CODE = A_CLNT_ACNT_SECT_CODE; }				
		public String getA_LN_BANK_NAME() { return A_LN_BANK_NAME; }
		public void setA_LN_BANK_NAME(String A_LN_BANK_NAME) { this.A_LN_BANK_NAME = A_LN_BANK_NAME; }				
		public String getLN_BANK_ACNT_NO() { return LN_BANK_ACNT_NO; }
		public void setLN_BANK_ACNT_NO(String LN_BANK_ACNT_NO) { this.LN_BANK_ACNT_NO = LN_BANK_ACNT_NO; }				
		public String getA_VRAC_BANK_NAME() { return A_VRAC_BANK_NAME; }
		public void setA_VRAC_BANK_NAME(String A_VRAC_BANK_NAME) { this.A_VRAC_BANK_NAME = A_VRAC_BANK_NAME; }				
		public String getA_VRAC_NO() { return A_VRAC_NO; }
		public void setA_VRAC_NO(String A_VRAC_NO) { this.A_VRAC_NO = A_VRAC_NO; }				
		public String getRFNC_CTNT() { return RFNC_CTNT; }
		public void setRFNC_CTNT(String RFNC_CTNT) { this.RFNC_CTNT = RFNC_CTNT; }				

		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("[CLNT_RNNO]"+CLNT_RNNO);
			sb.append(",");
			sb.append("[VIP_CLNT_YN]"+VIP_CLNT_YN);
			sb.append(",");
			sb.append("[MNGG_BRNH_ENTY_CODE]"+MNGG_BRNH_ENTY_CODE);
			sb.append(",");
			sb.append("[A_MNGG_BRNH_ENTY_NAME]"+A_MNGG_BRNH_ENTY_NAME);
			sb.append(",");
			sb.append("[BFMV_DMBE_CODE]"+BFMV_DMBE_CODE);
			sb.append(",");
			sb.append("[A_PREV_MNGG_BRNH_ENTY_NAME]"+A_PREV_MNGG_BRNH_ENTY_NAME);
			sb.append(",");
			sb.append("[PRFT_CNTR_ENTY_CODE]"+PRFT_CNTR_ENTY_CODE);
			sb.append(",");
			sb.append("[A_PRFT_CNTR_ENTY_NAME]"+A_PRFT_CNTR_ENTY_NAME);
			sb.append(",");
			sb.append("[A_RM_ACRS_BNCD]"+A_RM_ACRS_BNCD);
			sb.append(",");
			sb.append("[A_RM_PRFT_CNTR_ENTY_NAME]"+A_RM_PRFT_CNTR_ENTY_NAME);
			sb.append(",");
			sb.append("[A_MAIN_MNGR_EMPY_NO]"+A_MAIN_MNGR_EMPY_NO);
			sb.append(",");
			sb.append("[A_MAIN_MNGR_NAME]"+A_MAIN_MNGR_NAME);
			sb.append(",");
			sb.append("[SBMN_EMPY_NO]"+SBMN_EMPY_NO);
			sb.append(",");
			sb.append("[A_SBMN_EMPY_NAME]"+A_SBMN_EMPY_NAME);
			sb.append(",");
			sb.append("[ATRN_EMPY_NO]"+ATRN_EMPY_NO);
			sb.append(",");
			sb.append("[A_ATPS_NAME]"+A_ATPS_NAME);
			sb.append(",");
			sb.append("[INVT_INVT_AGNT_EMPY_NO]"+INVT_INVT_AGNT_EMPY_NO);
			sb.append(",");
			sb.append("[INVT_INVT_AGNT_NAME]"+INVT_INVT_AGNT_NAME);
			sb.append(",");
			sb.append("[A_RM_MNGR_EMPY_NO]"+A_RM_MNGR_EMPY_NO);
			sb.append(",");
			sb.append("[A_RM_MNGR_EMPY_NAME]"+A_RM_MNGR_EMPY_NAME);
			sb.append(",");
			sb.append("[ACNT_STAS_SECT_CODE]"+ACNT_STAS_SECT_CODE);
			sb.append(",");
			sb.append("[A_ACNT_STAS_NAME]"+A_ACNT_STAS_NAME);
			sb.append(",");
			sb.append("[ACNT_TYPE_CODE]"+ACNT_TYPE_CODE);
			sb.append(",");
			sb.append("[A_ACNT_TYPE_NAME]"+A_ACNT_TYPE_NAME);
			sb.append(",");
			sb.append("[INVR_CLSN_CODE]"+INVR_CLSN_CODE);
			sb.append(",");
			sb.append("[A_INVR_CLSN_NAME]"+A_INVR_CLSN_NAME);
			sb.append(",");
			sb.append("[ACCT_YN]"+ACCT_YN);
			sb.append(",");
			sb.append("[A_FNC_IFMN_PRTC_YN]"+A_FNC_IFMN_PRTC_YN);
			sb.append(",");
			sb.append("[ACNT_OPNG_DATE]"+ACNT_OPNG_DATE);
			sb.append(",");
			sb.append("[A_ACNT_OPNG_BRNH_NAME]"+A_ACNT_OPNG_BRNH_NAME);
			sb.append(",");
			sb.append("[A_ACNT_UNFC_DATE]"+A_ACNT_UNFC_DATE);
			sb.append(",");
			sb.append("[ACNT_CLSG_DATE]"+ACNT_CLSG_DATE);
			sb.append(",");
			sb.append("[A_ACNT_CLSG_BRNH_NAME]"+A_ACNT_CLSG_BRNH_NAME);
			sb.append(",");
			sb.append("[LAST_TRDG_DATE]"+LAST_TRDG_DATE);
			sb.append(",");
			sb.append("[A_CRDT_CTRT_RGSN_DATE]"+A_CRDT_CTRT_RGSN_DATE);
			sb.append(",");
			sb.append("[A_RGSN_BRNH_NAME]"+A_RGSN_BRNH_NAME);
			sb.append(",");
			sb.append("[A_FUNS_ATTR_SECT_CODE]"+A_FUNS_ATTR_SECT_CODE);
			sb.append(",");
			sb.append("[A_FUNS_ATTR_NAME]"+A_FUNS_ATTR_NAME);
			sb.append(",");
			sb.append("[ERNR_SECT_CODE]"+ERNR_SECT_CODE);
			sb.append(",");
			sb.append("[A_ERNR_SECT_NAME]"+A_ERNR_SECT_NAME);
			sb.append(",");
			sb.append("[FRGN_CRNY_TRDG_ACON_TYPE_CODE]"+FRGN_CRNY_TRDG_ACON_TYPE_CODE);
			sb.append(",");
			sb.append("[A_FRGN_CRNY_BSWR_SECT_NAME]"+A_FRGN_CRNY_BSWR_SECT_NAME);
			sb.append(",");
			sb.append("[A_AGNT_YN]"+A_AGNT_YN);
			sb.append(",");
			sb.append("[AGNT_SECT_CODE]"+AGNT_SECT_CODE);
			sb.append(",");
			sb.append("[A_AGNT_SECT_NAME]"+A_AGNT_SECT_NAME);
			sb.append(",");
			sb.append("[AGNT_RNNO]"+AGNT_RNNO);
			sb.append(",");
			sb.append("[PSTN_UNIT_TYPE_CODE]"+PSTN_UNIT_TYPE_CODE);
			sb.append(",");
			sb.append("[BANK_NAME]"+BANK_NAME);
			sb.append(",");
			sb.append("[BKPN_NAME]"+BKPN_NAME);
			sb.append(",");
			sb.append("[A_MRGN_CLTN_SECT_CODE_NAME]"+A_MRGN_CLTN_SECT_CODE_NAME);
			sb.append(",");
			sb.append("[A_WTHN_ENTS_YN_NAME]"+A_WTHN_ENTS_YN_NAME);
			sb.append(",");
			sb.append("[A_STX_NAME]"+A_STX_NAME);
			sb.append(",");
			sb.append("[A_ODLT_TXTN_SECT_NAME]"+A_ODLT_TXTN_SECT_NAME);
			sb.append(",");
			sb.append("[A_DSCS_FEE_SECT_CODE_NAME]"+A_DSCS_FEE_SECT_CODE_NAME);
			sb.append(",");
			sb.append("[A_FEE_SECT_NAME]"+A_FEE_SECT_NAME);
			sb.append(",");
			sb.append("[A_CTRC_STAS_CODE_NAME]"+A_CTRC_STAS_CODE_NAME);
			sb.append(",");
			sb.append("[CTRT_DATE]"+CTRT_DATE);
			sb.append(",");
			sb.append("[CTRT_XPRN_DATE]"+CTRT_XPRN_DATE);
			sb.append(",");
			sb.append("[CLNT_NAME]"+CLNT_NAME);
			sb.append(",");
			sb.append("[A_MEDI_KIND_NAME]"+A_MEDI_KIND_NAME);
			sb.append(",");
			sb.append("[A_RLNM_YN]"+A_RLNM_YN);
			sb.append(",");
			sb.append("[A_RLNM_CNFM_NAME]"+A_RLNM_CNFM_NAME);
			sb.append(",");
			sb.append("[CPAC_RLNM_CFDT]"+CPAC_RLNM_CFDT);
			sb.append(",");
			sb.append("[A_RESP_MSG_CTNT1]"+A_RESP_MSG_CTNT1);
			sb.append(",");
			sb.append("[EMAL_SNBC_YN]"+EMAL_SNBC_YN);
			sb.append(",");
			sb.append("[SNBC_YN]"+SNBC_YN);
			sb.append(",");
			sb.append("[A_ACNT_NO20]"+A_ACNT_NO20);
			sb.append(",");
			sb.append("[A_OPNG_DATE2]"+A_OPNG_DATE2);
			sb.append(",");
			sb.append("[BANK_PASS_ACNT_NO]"+BANK_PASS_ACNT_NO);
			sb.append(",");
			sb.append("[A_OPNG_SECT_NAME]"+A_OPNG_SECT_NAME);
			sb.append(",");
			sb.append("[A_SNOT_PRCE_EMPY_NAME]"+A_SNOT_PRCE_EMPY_NAME);
			sb.append(",");
			sb.append("[A_MEDI_HOLD_YN]"+A_MEDI_HOLD_YN);
			sb.append(",");
			sb.append("[ACNT_ENNM]"+ACNT_ENNM);
			sb.append(",");
			sb.append("[A_ORTR_ACNT_NO]"+A_ORTR_ACNT_NO);
			sb.append(",");
			sb.append("[A_RP_DLNG_ALWD_YN]"+A_RP_DLNG_ALWD_YN);
			sb.append(",");
			sb.append("[A_ACNT_FRM_NAME]"+A_ACNT_FRM_NAME);
			sb.append(",");
			sb.append("[CMA_ATMC_SBTT_YN]"+CMA_ATMC_SBTT_YN);
			sb.append(",");
			sb.append("[GLEG_LINK_YN]"+GLEG_LINK_YN);
			sb.append(",");
			sb.append("[ALGR_ACNT_YN]"+ALGR_ACNT_YN);
			sb.append(",");
			sb.append("[BSNS_PRPS_ACNT_YN]"+BSNS_PRPS_ACNT_YN);
			sb.append(",");
			sb.append("[BANK_ACNT_NAME]"+BANK_ACNT_NAME);
			sb.append(",");
			sb.append("[TRDG_PRPS_NAME]"+TRDG_PRPS_NAME);
			sb.append(",");
			sb.append("[A_CLNT_ACNT_SECT_CODE]"+A_CLNT_ACNT_SECT_CODE);
			sb.append(",");
			sb.append("[A_LN_BANK_NAME]"+A_LN_BANK_NAME);
			sb.append(",");
			sb.append("[LN_BANK_ACNT_NO]"+LN_BANK_ACNT_NO);
			sb.append(",");
			sb.append("[A_VRAC_BANK_NAME]"+A_VRAC_BANK_NAME);
			sb.append(",");
			sb.append("[A_VRAC_NO]"+A_VRAC_NO);
			sb.append(",");
			sb.append("[RFNC_CTNT]"+RFNC_CTNT);
			sb.append("}");
			return sb.toString();
		}

		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("CLNT_RNNO",CLNT_RNNO);
			m.put("VIP_CLNT_YN",VIP_CLNT_YN);
			m.put("MNGG_BRNH_ENTY_CODE",MNGG_BRNH_ENTY_CODE);
			m.put("A_MNGG_BRNH_ENTY_NAME",A_MNGG_BRNH_ENTY_NAME);
			m.put("BFMV_DMBE_CODE",BFMV_DMBE_CODE);
			m.put("A_PREV_MNGG_BRNH_ENTY_NAME",A_PREV_MNGG_BRNH_ENTY_NAME);
			m.put("PRFT_CNTR_ENTY_CODE",PRFT_CNTR_ENTY_CODE);
			m.put("A_PRFT_CNTR_ENTY_NAME",A_PRFT_CNTR_ENTY_NAME);
			m.put("A_RM_ACRS_BNCD",A_RM_ACRS_BNCD);
			m.put("A_RM_PRFT_CNTR_ENTY_NAME",A_RM_PRFT_CNTR_ENTY_NAME);
			m.put("A_MAIN_MNGR_EMPY_NO",A_MAIN_MNGR_EMPY_NO);
			m.put("A_MAIN_MNGR_NAME",A_MAIN_MNGR_NAME);
			m.put("SBMN_EMPY_NO",SBMN_EMPY_NO);
			m.put("A_SBMN_EMPY_NAME",A_SBMN_EMPY_NAME);
			m.put("ATRN_EMPY_NO",ATRN_EMPY_NO);
			m.put("A_ATPS_NAME",A_ATPS_NAME);
			m.put("INVT_INVT_AGNT_EMPY_NO",INVT_INVT_AGNT_EMPY_NO);
			m.put("INVT_INVT_AGNT_NAME",INVT_INVT_AGNT_NAME);
			m.put("A_RM_MNGR_EMPY_NO",A_RM_MNGR_EMPY_NO);
			m.put("A_RM_MNGR_EMPY_NAME",A_RM_MNGR_EMPY_NAME);
			m.put("ACNT_STAS_SECT_CODE",ACNT_STAS_SECT_CODE);
			m.put("A_ACNT_STAS_NAME",A_ACNT_STAS_NAME);
			m.put("ACNT_TYPE_CODE",ACNT_TYPE_CODE);
			m.put("A_ACNT_TYPE_NAME",A_ACNT_TYPE_NAME);
			m.put("INVR_CLSN_CODE",INVR_CLSN_CODE);
			m.put("A_INVR_CLSN_NAME",A_INVR_CLSN_NAME);
			m.put("ACCT_YN",ACCT_YN);
			m.put("A_FNC_IFMN_PRTC_YN",A_FNC_IFMN_PRTC_YN);
			m.put("ACNT_OPNG_DATE",ACNT_OPNG_DATE);
			m.put("A_ACNT_OPNG_BRNH_NAME",A_ACNT_OPNG_BRNH_NAME);
			m.put("A_ACNT_UNFC_DATE",A_ACNT_UNFC_DATE);
			m.put("ACNT_CLSG_DATE",ACNT_CLSG_DATE);
			m.put("A_ACNT_CLSG_BRNH_NAME",A_ACNT_CLSG_BRNH_NAME);
			m.put("LAST_TRDG_DATE",LAST_TRDG_DATE);
			m.put("A_CRDT_CTRT_RGSN_DATE",A_CRDT_CTRT_RGSN_DATE);
			m.put("A_RGSN_BRNH_NAME",A_RGSN_BRNH_NAME);
			m.put("A_FUNS_ATTR_SECT_CODE",A_FUNS_ATTR_SECT_CODE);
			m.put("A_FUNS_ATTR_NAME",A_FUNS_ATTR_NAME);
			m.put("ERNR_SECT_CODE",ERNR_SECT_CODE);
			m.put("A_ERNR_SECT_NAME",A_ERNR_SECT_NAME);
			m.put("FRGN_CRNY_TRDG_ACON_TYPE_CODE",FRGN_CRNY_TRDG_ACON_TYPE_CODE);
			m.put("A_FRGN_CRNY_BSWR_SECT_NAME",A_FRGN_CRNY_BSWR_SECT_NAME);
			m.put("A_AGNT_YN",A_AGNT_YN);
			m.put("AGNT_SECT_CODE",AGNT_SECT_CODE);
			m.put("A_AGNT_SECT_NAME",A_AGNT_SECT_NAME);
			m.put("AGNT_RNNO",AGNT_RNNO);
			m.put("PSTN_UNIT_TYPE_CODE",PSTN_UNIT_TYPE_CODE);
			m.put("BANK_NAME",BANK_NAME);
			m.put("BKPN_NAME",BKPN_NAME);
			m.put("A_MRGN_CLTN_SECT_CODE_NAME",A_MRGN_CLTN_SECT_CODE_NAME);
			m.put("A_WTHN_ENTS_YN_NAME",A_WTHN_ENTS_YN_NAME);
			m.put("A_STX_NAME",A_STX_NAME);
			m.put("A_ODLT_TXTN_SECT_NAME",A_ODLT_TXTN_SECT_NAME);
			m.put("A_DSCS_FEE_SECT_CODE_NAME",A_DSCS_FEE_SECT_CODE_NAME);
			m.put("A_FEE_SECT_NAME",A_FEE_SECT_NAME);
			m.put("A_CTRC_STAS_CODE_NAME",A_CTRC_STAS_CODE_NAME);
			m.put("CTRT_DATE",CTRT_DATE);
			m.put("CTRT_XPRN_DATE",CTRT_XPRN_DATE);
			m.put("CLNT_NAME",CLNT_NAME);
			m.put("A_MEDI_KIND_NAME",A_MEDI_KIND_NAME);
			m.put("A_RLNM_YN",A_RLNM_YN);
			m.put("A_RLNM_CNFM_NAME",A_RLNM_CNFM_NAME);
			m.put("CPAC_RLNM_CFDT",CPAC_RLNM_CFDT);
			m.put("A_RESP_MSG_CTNT1",A_RESP_MSG_CTNT1);
			m.put("EMAL_SNBC_YN",EMAL_SNBC_YN);
			m.put("SNBC_YN",SNBC_YN);
			m.put("A_ACNT_NO20",A_ACNT_NO20);
			m.put("A_OPNG_DATE2",A_OPNG_DATE2);
			m.put("BANK_PASS_ACNT_NO",BANK_PASS_ACNT_NO);
			m.put("A_OPNG_SECT_NAME",A_OPNG_SECT_NAME);
			m.put("A_SNOT_PRCE_EMPY_NAME",A_SNOT_PRCE_EMPY_NAME);
			m.put("A_MEDI_HOLD_YN",A_MEDI_HOLD_YN);
			m.put("ACNT_ENNM",ACNT_ENNM);
			m.put("A_ORTR_ACNT_NO",A_ORTR_ACNT_NO);
			m.put("A_RP_DLNG_ALWD_YN",A_RP_DLNG_ALWD_YN);
			m.put("A_ACNT_FRM_NAME",A_ACNT_FRM_NAME);
			m.put("CMA_ATMC_SBTT_YN",CMA_ATMC_SBTT_YN);
			m.put("GLEG_LINK_YN",GLEG_LINK_YN);
			m.put("ALGR_ACNT_YN",ALGR_ACNT_YN);
			m.put("BSNS_PRPS_ACNT_YN",BSNS_PRPS_ACNT_YN);
			m.put("BANK_ACNT_NAME",BANK_ACNT_NAME);
			m.put("TRDG_PRPS_NAME",TRDG_PRPS_NAME);
			m.put("A_CLNT_ACNT_SECT_CODE",A_CLNT_ACNT_SECT_CODE);
			m.put("A_LN_BANK_NAME",A_LN_BANK_NAME);
			m.put("LN_BANK_ACNT_NO",LN_BANK_ACNT_NO);
			m.put("A_VRAC_BANK_NAME",A_VRAC_BANK_NAME);
			m.put("A_VRAC_NO",A_VRAC_NO);
			m.put("RFNC_CTNT",RFNC_CTNT);
			return m;
		}
	}

	public static class OutRec2 { 
	
		// Constructor
		public OutRec2() {
		}

		/* Attributes */
		private String TRST_MNGT_YN;			// 신탁운용여부, CHAR(1)	
		private String TRST_FUND_CODE;			// 신탁펀드코드, CHAR(10)	
		private String A_INVT_INVT_YN;			// 투자권유여부, CHAR(1)	
		private String A_PSBO_RGST_FUND_CONT;			// 통장기장펀드수, DECIMAL(6)	
		private String A_PSBO_STAS_NAME;			// 통장상태명, CHAR(30)	
		private String A_PSBO_ISNC_DATE;			// 통장발급일자, CHAR(8)	
		private String A_PSBO_ISNC_BRNH_NAME;			// 통장발급지점명, CHAR(90)	
		private String PSBO_ISNC_CNT;			// 통장발급횟수, DECIMAL(11)	
		private String CLSG_DATE;			// 폐쇄일자, CHAR(8)	
		private String A_TRDG_ENBO_FRST_ISNC_YN;			// 거래기입장최초발급여부, CHAR(1)	
		private String PSBO_PRNT_SECT_CODE;			// 통장인쇄구분코드, CHAR(1)	
		private String A_SECT_NAME;			// 구분명, CHAR(30)	
		private String A_PSBO_ISNC_RESN_CTNT;			// 통장발급사유내용, CHAR(30)	
		private String A_LAST_RGST_DATE;			// 최종기장일자, CHAR(8)	
		private String A_LAST_RGST_TRDG_NO;			// 최종기장거래번호, CHAR(6)	
		private String UN_RGST_CONT;			// 미기장건수, DECIMAL(6)	
		private String A_LAST_PSBO_LINE_CONT;			// 최종통장라인수, DECIMAL(6)	
		private String RMS_LN_DATE;			// RMS대출일자, CHAR(8)	
		private String A_RMS_INTM_INSN_NAME;			// RMS중개기관명, CHAR(60)	
		private String A_RMS_LN_INSN_NAME;			// RMS대출기관명, CHAR(60)	

		/* Getters/Setters */
		public void setData(
			String TRST_MNGT_YN,
			String TRST_FUND_CODE,
			String A_INVT_INVT_YN,
			String A_PSBO_RGST_FUND_CONT,
			String A_PSBO_STAS_NAME,
			String A_PSBO_ISNC_DATE,
			String A_PSBO_ISNC_BRNH_NAME,
			String PSBO_ISNC_CNT,
			String CLSG_DATE,
			String A_TRDG_ENBO_FRST_ISNC_YN,
			String PSBO_PRNT_SECT_CODE,
			String A_SECT_NAME,
			String A_PSBO_ISNC_RESN_CTNT,
			String A_LAST_RGST_DATE,
			String A_LAST_RGST_TRDG_NO,
			String UN_RGST_CONT,
			String A_LAST_PSBO_LINE_CONT,
			String RMS_LN_DATE,
			String A_RMS_INTM_INSN_NAME,
			String A_RMS_LN_INSN_NAME
		 ) {
			this.TRST_MNGT_YN = TRST_MNGT_YN;
			this.TRST_FUND_CODE = TRST_FUND_CODE;
			this.A_INVT_INVT_YN = A_INVT_INVT_YN;
			this.A_PSBO_RGST_FUND_CONT = A_PSBO_RGST_FUND_CONT;
			this.A_PSBO_STAS_NAME = A_PSBO_STAS_NAME;
			this.A_PSBO_ISNC_DATE = A_PSBO_ISNC_DATE;
			this.A_PSBO_ISNC_BRNH_NAME = A_PSBO_ISNC_BRNH_NAME;
			this.PSBO_ISNC_CNT = PSBO_ISNC_CNT;
			this.CLSG_DATE = CLSG_DATE;
			this.A_TRDG_ENBO_FRST_ISNC_YN = A_TRDG_ENBO_FRST_ISNC_YN;
			this.PSBO_PRNT_SECT_CODE = PSBO_PRNT_SECT_CODE;
			this.A_SECT_NAME = A_SECT_NAME;
			this.A_PSBO_ISNC_RESN_CTNT = A_PSBO_ISNC_RESN_CTNT;
			this.A_LAST_RGST_DATE = A_LAST_RGST_DATE;
			this.A_LAST_RGST_TRDG_NO = A_LAST_RGST_TRDG_NO;
			this.UN_RGST_CONT = UN_RGST_CONT;
			this.A_LAST_PSBO_LINE_CONT = A_LAST_PSBO_LINE_CONT;
			this.RMS_LN_DATE = RMS_LN_DATE;
			this.A_RMS_INTM_INSN_NAME = A_RMS_INTM_INSN_NAME;
			this.A_RMS_LN_INSN_NAME = A_RMS_LN_INSN_NAME;
		}
		public String getTRST_MNGT_YN() { return TRST_MNGT_YN; }
		public void setTRST_MNGT_YN(String TRST_MNGT_YN) { this.TRST_MNGT_YN = TRST_MNGT_YN; }				
		public String getTRST_FUND_CODE() { return TRST_FUND_CODE; }
		public void setTRST_FUND_CODE(String TRST_FUND_CODE) { this.TRST_FUND_CODE = TRST_FUND_CODE; }				
		public String getA_INVT_INVT_YN() { return A_INVT_INVT_YN; }
		public void setA_INVT_INVT_YN(String A_INVT_INVT_YN) { this.A_INVT_INVT_YN = A_INVT_INVT_YN; }				
		public String getA_PSBO_RGST_FUND_CONT() { return A_PSBO_RGST_FUND_CONT; }
		public void setA_PSBO_RGST_FUND_CONT(String A_PSBO_RGST_FUND_CONT) { this.A_PSBO_RGST_FUND_CONT = A_PSBO_RGST_FUND_CONT; }				
		public String getA_PSBO_STAS_NAME() { return A_PSBO_STAS_NAME; }
		public void setA_PSBO_STAS_NAME(String A_PSBO_STAS_NAME) { this.A_PSBO_STAS_NAME = A_PSBO_STAS_NAME; }				
		public String getA_PSBO_ISNC_DATE() { return A_PSBO_ISNC_DATE; }
		public void setA_PSBO_ISNC_DATE(String A_PSBO_ISNC_DATE) { this.A_PSBO_ISNC_DATE = A_PSBO_ISNC_DATE; }				
		public String getA_PSBO_ISNC_BRNH_NAME() { return A_PSBO_ISNC_BRNH_NAME; }
		public void setA_PSBO_ISNC_BRNH_NAME(String A_PSBO_ISNC_BRNH_NAME) { this.A_PSBO_ISNC_BRNH_NAME = A_PSBO_ISNC_BRNH_NAME; }				
		public String getPSBO_ISNC_CNT() { return PSBO_ISNC_CNT; }
		public void setPSBO_ISNC_CNT(String PSBO_ISNC_CNT) { this.PSBO_ISNC_CNT = PSBO_ISNC_CNT; }				
		public String getCLSG_DATE() { return CLSG_DATE; }
		public void setCLSG_DATE(String CLSG_DATE) { this.CLSG_DATE = CLSG_DATE; }				
		public String getA_TRDG_ENBO_FRST_ISNC_YN() { return A_TRDG_ENBO_FRST_ISNC_YN; }
		public void setA_TRDG_ENBO_FRST_ISNC_YN(String A_TRDG_ENBO_FRST_ISNC_YN) { this.A_TRDG_ENBO_FRST_ISNC_YN = A_TRDG_ENBO_FRST_ISNC_YN; }				
		public String getPSBO_PRNT_SECT_CODE() { return PSBO_PRNT_SECT_CODE; }
		public void setPSBO_PRNT_SECT_CODE(String PSBO_PRNT_SECT_CODE) { this.PSBO_PRNT_SECT_CODE = PSBO_PRNT_SECT_CODE; }				
		public String getA_SECT_NAME() { return A_SECT_NAME; }
		public void setA_SECT_NAME(String A_SECT_NAME) { this.A_SECT_NAME = A_SECT_NAME; }				
		public String getA_PSBO_ISNC_RESN_CTNT() { return A_PSBO_ISNC_RESN_CTNT; }
		public void setA_PSBO_ISNC_RESN_CTNT(String A_PSBO_ISNC_RESN_CTNT) { this.A_PSBO_ISNC_RESN_CTNT = A_PSBO_ISNC_RESN_CTNT; }				
		public String getA_LAST_RGST_DATE() { return A_LAST_RGST_DATE; }
		public void setA_LAST_RGST_DATE(String A_LAST_RGST_DATE) { this.A_LAST_RGST_DATE = A_LAST_RGST_DATE; }				
		public String getA_LAST_RGST_TRDG_NO() { return A_LAST_RGST_TRDG_NO; }
		public void setA_LAST_RGST_TRDG_NO(String A_LAST_RGST_TRDG_NO) { this.A_LAST_RGST_TRDG_NO = A_LAST_RGST_TRDG_NO; }				
		public String getUN_RGST_CONT() { return UN_RGST_CONT; }
		public void setUN_RGST_CONT(String UN_RGST_CONT) { this.UN_RGST_CONT = UN_RGST_CONT; }				
		public String getA_LAST_PSBO_LINE_CONT() { return A_LAST_PSBO_LINE_CONT; }
		public void setA_LAST_PSBO_LINE_CONT(String A_LAST_PSBO_LINE_CONT) { this.A_LAST_PSBO_LINE_CONT = A_LAST_PSBO_LINE_CONT; }				
		public String getRMS_LN_DATE() { return RMS_LN_DATE; }
		public void setRMS_LN_DATE(String RMS_LN_DATE) { this.RMS_LN_DATE = RMS_LN_DATE; }				
		public String getA_RMS_INTM_INSN_NAME() { return A_RMS_INTM_INSN_NAME; }
		public void setA_RMS_INTM_INSN_NAME(String A_RMS_INTM_INSN_NAME) { this.A_RMS_INTM_INSN_NAME = A_RMS_INTM_INSN_NAME; }				
		public String getA_RMS_LN_INSN_NAME() { return A_RMS_LN_INSN_NAME; }
		public void setA_RMS_LN_INSN_NAME(String A_RMS_LN_INSN_NAME) { this.A_RMS_LN_INSN_NAME = A_RMS_LN_INSN_NAME; }				

		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("[TRST_MNGT_YN]"+TRST_MNGT_YN);
			sb.append(",");
			sb.append("[TRST_FUND_CODE]"+TRST_FUND_CODE);
			sb.append(",");
			sb.append("[A_INVT_INVT_YN]"+A_INVT_INVT_YN);
			sb.append(",");
			sb.append("[A_PSBO_RGST_FUND_CONT]"+A_PSBO_RGST_FUND_CONT);
			sb.append(",");
			sb.append("[A_PSBO_STAS_NAME]"+A_PSBO_STAS_NAME);
			sb.append(",");
			sb.append("[A_PSBO_ISNC_DATE]"+A_PSBO_ISNC_DATE);
			sb.append(",");
			sb.append("[A_PSBO_ISNC_BRNH_NAME]"+A_PSBO_ISNC_BRNH_NAME);
			sb.append(",");
			sb.append("[PSBO_ISNC_CNT]"+PSBO_ISNC_CNT);
			sb.append(",");
			sb.append("[CLSG_DATE]"+CLSG_DATE);
			sb.append(",");
			sb.append("[A_TRDG_ENBO_FRST_ISNC_YN]"+A_TRDG_ENBO_FRST_ISNC_YN);
			sb.append(",");
			sb.append("[PSBO_PRNT_SECT_CODE]"+PSBO_PRNT_SECT_CODE);
			sb.append(",");
			sb.append("[A_SECT_NAME]"+A_SECT_NAME);
			sb.append(",");
			sb.append("[A_PSBO_ISNC_RESN_CTNT]"+A_PSBO_ISNC_RESN_CTNT);
			sb.append(",");
			sb.append("[A_LAST_RGST_DATE]"+A_LAST_RGST_DATE);
			sb.append(",");
			sb.append("[A_LAST_RGST_TRDG_NO]"+A_LAST_RGST_TRDG_NO);
			sb.append(",");
			sb.append("[UN_RGST_CONT]"+UN_RGST_CONT);
			sb.append(",");
			sb.append("[A_LAST_PSBO_LINE_CONT]"+A_LAST_PSBO_LINE_CONT);
			sb.append(",");
			sb.append("[RMS_LN_DATE]"+RMS_LN_DATE);
			sb.append(",");
			sb.append("[A_RMS_INTM_INSN_NAME]"+A_RMS_INTM_INSN_NAME);
			sb.append(",");
			sb.append("[A_RMS_LN_INSN_NAME]"+A_RMS_LN_INSN_NAME);
			sb.append("}");
			return sb.toString();
		}

		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("TRST_MNGT_YN",TRST_MNGT_YN);
			m.put("TRST_FUND_CODE",TRST_FUND_CODE);
			m.put("A_INVT_INVT_YN",A_INVT_INVT_YN);
			m.put("A_PSBO_RGST_FUND_CONT",A_PSBO_RGST_FUND_CONT);
			m.put("A_PSBO_STAS_NAME",A_PSBO_STAS_NAME);
			m.put("A_PSBO_ISNC_DATE",A_PSBO_ISNC_DATE);
			m.put("A_PSBO_ISNC_BRNH_NAME",A_PSBO_ISNC_BRNH_NAME);
			m.put("PSBO_ISNC_CNT",PSBO_ISNC_CNT);
			m.put("CLSG_DATE",CLSG_DATE);
			m.put("A_TRDG_ENBO_FRST_ISNC_YN",A_TRDG_ENBO_FRST_ISNC_YN);
			m.put("PSBO_PRNT_SECT_CODE",PSBO_PRNT_SECT_CODE);
			m.put("A_SECT_NAME",A_SECT_NAME);
			m.put("A_PSBO_ISNC_RESN_CTNT",A_PSBO_ISNC_RESN_CTNT);
			m.put("A_LAST_RGST_DATE",A_LAST_RGST_DATE);
			m.put("A_LAST_RGST_TRDG_NO",A_LAST_RGST_TRDG_NO);
			m.put("UN_RGST_CONT",UN_RGST_CONT);
			m.put("A_LAST_PSBO_LINE_CONT",A_LAST_PSBO_LINE_CONT);
			m.put("RMS_LN_DATE",RMS_LN_DATE);
			m.put("A_RMS_INTM_INSN_NAME",A_RMS_INTM_INSN_NAME);
			m.put("A_RMS_LN_INSN_NAME",A_RMS_LN_INSN_NAME);
			return m;
		}
	}

	public static class OutRec3 { 
	
		// Constructor
		public OutRec3() {
		}

		/* Attributes */
		private String UNI_SVNS_PRDT_CODE;			// 연합회저축상품코드, CHAR(2)	
		private String PNSN_SVNS_PRDT_NAME;			// 연금저축상품명, VARCHAR(90)	
		private String PMNT_TYPE_CODE;			// 납입유형코드, CHAR(2)	
		private String A_TYPE_NAME;			// 유형명, CHAR(60)	
		private String SVNS_TYPE_CODE;			// 저축유형코드, CHAR(2)	
		private String A_SVNS_TYPE_NAME;			// 저축유형명, CHAR(30)	
		private String TAX_BNFT_DPLC_TYPE_SECT_CODE;			// 세금우대중복유형구분코드, CHAR(1)	
		private String DLNG_TYPE_SECT_NAME;			// 매매유형구분명, CHAR(30)	
		private String CTRC_AMNT;			// 계약금액, DECIMAL(16)	
		private String CTRC_PERD;			// 계약기간, DECIMAL(6)	
		private String A_PRDT_CNCN_DATE;			// 상품해지일자, CHAR(8)	
		private String A_PRDT_MTRY_DATE;			// 상품만기일자, CHAR(8)	
		private String MNTH_PMNT_CTRC_AMNT;			// 월납입계약금액, DECIMAL(16)	
		private String A_ASM_PMNT_AMNT;			// 누계납입금액, DECIMAL(18)	
		private String PMNT_TCNT;			// 납입회차, DECIMAL(6)	
		private String CTRC_DATE;			// 계약일자, CHAR(8)	

		/* Getters/Setters */
		public void setData(
			String UNI_SVNS_PRDT_CODE,
			String PNSN_SVNS_PRDT_NAME,
			String PMNT_TYPE_CODE,
			String A_TYPE_NAME,
			String SVNS_TYPE_CODE,
			String A_SVNS_TYPE_NAME,
			String TAX_BNFT_DPLC_TYPE_SECT_CODE,
			String DLNG_TYPE_SECT_NAME,
			String CTRC_AMNT,
			String CTRC_PERD,
			String A_PRDT_CNCN_DATE,
			String A_PRDT_MTRY_DATE,
			String MNTH_PMNT_CTRC_AMNT,
			String A_ASM_PMNT_AMNT,
			String PMNT_TCNT,
			String CTRC_DATE
		 ) {
			this.UNI_SVNS_PRDT_CODE = UNI_SVNS_PRDT_CODE;
			this.PNSN_SVNS_PRDT_NAME = PNSN_SVNS_PRDT_NAME;
			this.PMNT_TYPE_CODE = PMNT_TYPE_CODE;
			this.A_TYPE_NAME = A_TYPE_NAME;
			this.SVNS_TYPE_CODE = SVNS_TYPE_CODE;
			this.A_SVNS_TYPE_NAME = A_SVNS_TYPE_NAME;
			this.TAX_BNFT_DPLC_TYPE_SECT_CODE = TAX_BNFT_DPLC_TYPE_SECT_CODE;
			this.DLNG_TYPE_SECT_NAME = DLNG_TYPE_SECT_NAME;
			this.CTRC_AMNT = CTRC_AMNT;
			this.CTRC_PERD = CTRC_PERD;
			this.A_PRDT_CNCN_DATE = A_PRDT_CNCN_DATE;
			this.A_PRDT_MTRY_DATE = A_PRDT_MTRY_DATE;
			this.MNTH_PMNT_CTRC_AMNT = MNTH_PMNT_CTRC_AMNT;
			this.A_ASM_PMNT_AMNT = A_ASM_PMNT_AMNT;
			this.PMNT_TCNT = PMNT_TCNT;
			this.CTRC_DATE = CTRC_DATE;
		}
		public String getUNI_SVNS_PRDT_CODE() { return UNI_SVNS_PRDT_CODE; }
		public void setUNI_SVNS_PRDT_CODE(String UNI_SVNS_PRDT_CODE) { this.UNI_SVNS_PRDT_CODE = UNI_SVNS_PRDT_CODE; }				
		public String getPNSN_SVNS_PRDT_NAME() { return PNSN_SVNS_PRDT_NAME; }
		public void setPNSN_SVNS_PRDT_NAME(String PNSN_SVNS_PRDT_NAME) { this.PNSN_SVNS_PRDT_NAME = PNSN_SVNS_PRDT_NAME; }				
		public String getPMNT_TYPE_CODE() { return PMNT_TYPE_CODE; }
		public void setPMNT_TYPE_CODE(String PMNT_TYPE_CODE) { this.PMNT_TYPE_CODE = PMNT_TYPE_CODE; }				
		public String getA_TYPE_NAME() { return A_TYPE_NAME; }
		public void setA_TYPE_NAME(String A_TYPE_NAME) { this.A_TYPE_NAME = A_TYPE_NAME; }				
		public String getSVNS_TYPE_CODE() { return SVNS_TYPE_CODE; }
		public void setSVNS_TYPE_CODE(String SVNS_TYPE_CODE) { this.SVNS_TYPE_CODE = SVNS_TYPE_CODE; }				
		public String getA_SVNS_TYPE_NAME() { return A_SVNS_TYPE_NAME; }
		public void setA_SVNS_TYPE_NAME(String A_SVNS_TYPE_NAME) { this.A_SVNS_TYPE_NAME = A_SVNS_TYPE_NAME; }				
		public String getTAX_BNFT_DPLC_TYPE_SECT_CODE() { return TAX_BNFT_DPLC_TYPE_SECT_CODE; }
		public void setTAX_BNFT_DPLC_TYPE_SECT_CODE(String TAX_BNFT_DPLC_TYPE_SECT_CODE) { this.TAX_BNFT_DPLC_TYPE_SECT_CODE = TAX_BNFT_DPLC_TYPE_SECT_CODE; }				
		public String getDLNG_TYPE_SECT_NAME() { return DLNG_TYPE_SECT_NAME; }
		public void setDLNG_TYPE_SECT_NAME(String DLNG_TYPE_SECT_NAME) { this.DLNG_TYPE_SECT_NAME = DLNG_TYPE_SECT_NAME; }				
		public String getCTRC_AMNT() { return CTRC_AMNT; }
		public void setCTRC_AMNT(String CTRC_AMNT) { this.CTRC_AMNT = CTRC_AMNT; }				
		public String getCTRC_PERD() { return CTRC_PERD; }
		public void setCTRC_PERD(String CTRC_PERD) { this.CTRC_PERD = CTRC_PERD; }				
		public String getA_PRDT_CNCN_DATE() { return A_PRDT_CNCN_DATE; }
		public void setA_PRDT_CNCN_DATE(String A_PRDT_CNCN_DATE) { this.A_PRDT_CNCN_DATE = A_PRDT_CNCN_DATE; }				
		public String getA_PRDT_MTRY_DATE() { return A_PRDT_MTRY_DATE; }
		public void setA_PRDT_MTRY_DATE(String A_PRDT_MTRY_DATE) { this.A_PRDT_MTRY_DATE = A_PRDT_MTRY_DATE; }				
		public String getMNTH_PMNT_CTRC_AMNT() { return MNTH_PMNT_CTRC_AMNT; }
		public void setMNTH_PMNT_CTRC_AMNT(String MNTH_PMNT_CTRC_AMNT) { this.MNTH_PMNT_CTRC_AMNT = MNTH_PMNT_CTRC_AMNT; }				
		public String getA_ASM_PMNT_AMNT() { return A_ASM_PMNT_AMNT; }
		public void setA_ASM_PMNT_AMNT(String A_ASM_PMNT_AMNT) { this.A_ASM_PMNT_AMNT = A_ASM_PMNT_AMNT; }				
		public String getPMNT_TCNT() { return PMNT_TCNT; }
		public void setPMNT_TCNT(String PMNT_TCNT) { this.PMNT_TCNT = PMNT_TCNT; }				
		public String getCTRC_DATE() { return CTRC_DATE; }
		public void setCTRC_DATE(String CTRC_DATE) { this.CTRC_DATE = CTRC_DATE; }				

		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("[UNI_SVNS_PRDT_CODE]"+UNI_SVNS_PRDT_CODE);
			sb.append(",");
			sb.append("[PNSN_SVNS_PRDT_NAME]"+PNSN_SVNS_PRDT_NAME);
			sb.append(",");
			sb.append("[PMNT_TYPE_CODE]"+PMNT_TYPE_CODE);
			sb.append(",");
			sb.append("[A_TYPE_NAME]"+A_TYPE_NAME);
			sb.append(",");
			sb.append("[SVNS_TYPE_CODE]"+SVNS_TYPE_CODE);
			sb.append(",");
			sb.append("[A_SVNS_TYPE_NAME]"+A_SVNS_TYPE_NAME);
			sb.append(",");
			sb.append("[TAX_BNFT_DPLC_TYPE_SECT_CODE]"+TAX_BNFT_DPLC_TYPE_SECT_CODE);
			sb.append(",");
			sb.append("[DLNG_TYPE_SECT_NAME]"+DLNG_TYPE_SECT_NAME);
			sb.append(",");
			sb.append("[CTRC_AMNT]"+CTRC_AMNT);
			sb.append(",");
			sb.append("[CTRC_PERD]"+CTRC_PERD);
			sb.append(",");
			sb.append("[A_PRDT_CNCN_DATE]"+A_PRDT_CNCN_DATE);
			sb.append(",");
			sb.append("[A_PRDT_MTRY_DATE]"+A_PRDT_MTRY_DATE);
			sb.append(",");
			sb.append("[MNTH_PMNT_CTRC_AMNT]"+MNTH_PMNT_CTRC_AMNT);
			sb.append(",");
			sb.append("[A_ASM_PMNT_AMNT]"+A_ASM_PMNT_AMNT);
			sb.append(",");
			sb.append("[PMNT_TCNT]"+PMNT_TCNT);
			sb.append(",");
			sb.append("[CTRC_DATE]"+CTRC_DATE);
			sb.append("}");
			return sb.toString();
		}

		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("UNI_SVNS_PRDT_CODE",UNI_SVNS_PRDT_CODE);
			m.put("PNSN_SVNS_PRDT_NAME",PNSN_SVNS_PRDT_NAME);
			m.put("PMNT_TYPE_CODE",PMNT_TYPE_CODE);
			m.put("A_TYPE_NAME",A_TYPE_NAME);
			m.put("SVNS_TYPE_CODE",SVNS_TYPE_CODE);
			m.put("A_SVNS_TYPE_NAME",A_SVNS_TYPE_NAME);
			m.put("TAX_BNFT_DPLC_TYPE_SECT_CODE",TAX_BNFT_DPLC_TYPE_SECT_CODE);
			m.put("DLNG_TYPE_SECT_NAME",DLNG_TYPE_SECT_NAME);
			m.put("CTRC_AMNT",CTRC_AMNT);
			m.put("CTRC_PERD",CTRC_PERD);
			m.put("A_PRDT_CNCN_DATE",A_PRDT_CNCN_DATE);
			m.put("A_PRDT_MTRY_DATE",A_PRDT_MTRY_DATE);
			m.put("MNTH_PMNT_CTRC_AMNT",MNTH_PMNT_CTRC_AMNT);
			m.put("A_ASM_PMNT_AMNT",A_ASM_PMNT_AMNT);
			m.put("PMNT_TCNT",PMNT_TCNT);
			m.put("CTRC_DATE",CTRC_DATE);
			return m;
		}
	}

	public static class OutRec4 { 
	
		// Constructor
		public OutRec4() {
		}

		/* Attributes */
		private String A_BSC_DPMY_SECT_NAME;			// 기본예탁금구분명, CHAR(50)	
		private String CNSD_ACNT_NO;			// 연결계좌번호, CHAR(20)	
		private String CNST_TYPE_CODE;			// 위탁유형코드, CHAR(2)	
		private String A_GLBL_NGHT_MRKT_PARC_YN;			// 글로벌야간시장참여여부, CHAR(1)	
		private String A_CNGR_ALRD_SECT_NAME;			// 위탁자기구분명, CHAR(60)	

		/* Getters/Setters */
		public void setData(
			String A_BSC_DPMY_SECT_NAME,
			String CNSD_ACNT_NO,
			String CNST_TYPE_CODE,
			String A_GLBL_NGHT_MRKT_PARC_YN,
			String A_CNGR_ALRD_SECT_NAME
		 ) {
			this.A_BSC_DPMY_SECT_NAME = A_BSC_DPMY_SECT_NAME;
			this.CNSD_ACNT_NO = CNSD_ACNT_NO;
			this.CNST_TYPE_CODE = CNST_TYPE_CODE;
			this.A_GLBL_NGHT_MRKT_PARC_YN = A_GLBL_NGHT_MRKT_PARC_YN;
			this.A_CNGR_ALRD_SECT_NAME = A_CNGR_ALRD_SECT_NAME;
		}
		public String getA_BSC_DPMY_SECT_NAME() { return A_BSC_DPMY_SECT_NAME; }
		public void setA_BSC_DPMY_SECT_NAME(String A_BSC_DPMY_SECT_NAME) { this.A_BSC_DPMY_SECT_NAME = A_BSC_DPMY_SECT_NAME; }				
		public String getCNSD_ACNT_NO() { return CNSD_ACNT_NO; }
		public void setCNSD_ACNT_NO(String CNSD_ACNT_NO) { this.CNSD_ACNT_NO = CNSD_ACNT_NO; }				
		public String getCNST_TYPE_CODE() { return CNST_TYPE_CODE; }
		public void setCNST_TYPE_CODE(String CNST_TYPE_CODE) { this.CNST_TYPE_CODE = CNST_TYPE_CODE; }				
		public String getA_GLBL_NGHT_MRKT_PARC_YN() { return A_GLBL_NGHT_MRKT_PARC_YN; }
		public void setA_GLBL_NGHT_MRKT_PARC_YN(String A_GLBL_NGHT_MRKT_PARC_YN) { this.A_GLBL_NGHT_MRKT_PARC_YN = A_GLBL_NGHT_MRKT_PARC_YN; }				
		public String getA_CNGR_ALRD_SECT_NAME() { return A_CNGR_ALRD_SECT_NAME; }
		public void setA_CNGR_ALRD_SECT_NAME(String A_CNGR_ALRD_SECT_NAME) { this.A_CNGR_ALRD_SECT_NAME = A_CNGR_ALRD_SECT_NAME; }				

		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("[A_BSC_DPMY_SECT_NAME]"+A_BSC_DPMY_SECT_NAME);
			sb.append(",");
			sb.append("[CNSD_ACNT_NO]"+CNSD_ACNT_NO);
			sb.append(",");
			sb.append("[CNST_TYPE_CODE]"+CNST_TYPE_CODE);
			sb.append(",");
			sb.append("[A_GLBL_NGHT_MRKT_PARC_YN]"+A_GLBL_NGHT_MRKT_PARC_YN);
			sb.append(",");
			sb.append("[A_CNGR_ALRD_SECT_NAME]"+A_CNGR_ALRD_SECT_NAME);
			sb.append("}");
			return sb.toString();
		}

		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("A_BSC_DPMY_SECT_NAME",A_BSC_DPMY_SECT_NAME);
			m.put("CNSD_ACNT_NO",CNSD_ACNT_NO);
			m.put("CNST_TYPE_CODE",CNST_TYPE_CODE);
			m.put("A_GLBL_NGHT_MRKT_PARC_YN",A_GLBL_NGHT_MRKT_PARC_YN);
			m.put("A_CNGR_ALRD_SECT_NAME",A_CNGR_ALRD_SECT_NAME);
			return m;
		}
	}

	public static class OutRec6 { 
	
		// Constructor
		public OutRec6() {
		}

		/* Attributes */
		private String PSTL_CODE;			// 우편번호, CHAR(6)	
		private String A_ADRS;			// 주소, CHAR(150)	
		private String A_CNTT_ADDR_TYPE_NAME;			// 연락처유형명, CHAR(30)	
		private String PHON_NO;			// 전화번호, CHAR(20)	
		private String FAX_NO;			// 팩스번호, CHAR(20)	
		private String SMS_MOBL_PHON_NO;			// SMS휴대전화번호, CHAR(20)	
		private String EMAL_ADRS;			// E-mail주소, VARCHAR(150)	
		private String A_BLNC_TRDG_INFM_MTHD_NAME;			// 잔고거래통보방법명, CHAR(30)	
		private String A_MNGT_RPOT_IFTG_SECT_NAME;			// 운용보고서통보지구분명, CHAR(30)	
		private String A_INFM_MTHD_NAME;			// 통보방법명, CHAR(30)	
		private String A_INFM_MTHD_SECT_NAME;			// 통보방법구분명, CHAR(30)	
		private String IFMN_OFRN_RECV_MTHD_CODE;			// 정보제공수신방법코드, CHAR(1)	
		private String CMPC_GIDE_MTHD_CODE;			// 컴플라이언스안내방법코드, CHAR(1)	

		/* Getters/Setters */
		public void setData(
			String PSTL_CODE,
			String A_ADRS,
			String A_CNTT_ADDR_TYPE_NAME,
			String PHON_NO,
			String FAX_NO,
			String SMS_MOBL_PHON_NO,
			String EMAL_ADRS,
			String A_BLNC_TRDG_INFM_MTHD_NAME,
			String A_MNGT_RPOT_IFTG_SECT_NAME,
			String A_INFM_MTHD_NAME,
			String A_INFM_MTHD_SECT_NAME,
			String IFMN_OFRN_RECV_MTHD_CODE,
			String CMPC_GIDE_MTHD_CODE
		 ) {
			this.PSTL_CODE = PSTL_CODE;
			this.A_ADRS = A_ADRS;
			this.A_CNTT_ADDR_TYPE_NAME = A_CNTT_ADDR_TYPE_NAME;
			this.PHON_NO = PHON_NO;
			this.FAX_NO = FAX_NO;
			this.SMS_MOBL_PHON_NO = SMS_MOBL_PHON_NO;
			this.EMAL_ADRS = EMAL_ADRS;
			this.A_BLNC_TRDG_INFM_MTHD_NAME = A_BLNC_TRDG_INFM_MTHD_NAME;
			this.A_MNGT_RPOT_IFTG_SECT_NAME = A_MNGT_RPOT_IFTG_SECT_NAME;
			this.A_INFM_MTHD_NAME = A_INFM_MTHD_NAME;
			this.A_INFM_MTHD_SECT_NAME = A_INFM_MTHD_SECT_NAME;
			this.IFMN_OFRN_RECV_MTHD_CODE = IFMN_OFRN_RECV_MTHD_CODE;
			this.CMPC_GIDE_MTHD_CODE = CMPC_GIDE_MTHD_CODE;
		}
		public String getPSTL_CODE() { return PSTL_CODE; }
		public void setPSTL_CODE(String PSTL_CODE) { this.PSTL_CODE = PSTL_CODE; }				
		public String getA_ADRS() { return A_ADRS; }
		public void setA_ADRS(String A_ADRS) { this.A_ADRS = A_ADRS; }				
		public String getA_CNTT_ADDR_TYPE_NAME() { return A_CNTT_ADDR_TYPE_NAME; }
		public void setA_CNTT_ADDR_TYPE_NAME(String A_CNTT_ADDR_TYPE_NAME) { this.A_CNTT_ADDR_TYPE_NAME = A_CNTT_ADDR_TYPE_NAME; }				
		public String getPHON_NO() { return PHON_NO; }
		public void setPHON_NO(String PHON_NO) { this.PHON_NO = PHON_NO; }				
		public String getFAX_NO() { return FAX_NO; }
		public void setFAX_NO(String FAX_NO) { this.FAX_NO = FAX_NO; }				
		public String getSMS_MOBL_PHON_NO() { return SMS_MOBL_PHON_NO; }
		public void setSMS_MOBL_PHON_NO(String SMS_MOBL_PHON_NO) { this.SMS_MOBL_PHON_NO = SMS_MOBL_PHON_NO; }				
		public String getEMAL_ADRS() { return EMAL_ADRS; }
		public void setEMAL_ADRS(String EMAL_ADRS) { this.EMAL_ADRS = EMAL_ADRS; }				
		public String getA_BLNC_TRDG_INFM_MTHD_NAME() { return A_BLNC_TRDG_INFM_MTHD_NAME; }
		public void setA_BLNC_TRDG_INFM_MTHD_NAME(String A_BLNC_TRDG_INFM_MTHD_NAME) { this.A_BLNC_TRDG_INFM_MTHD_NAME = A_BLNC_TRDG_INFM_MTHD_NAME; }				
		public String getA_MNGT_RPOT_IFTG_SECT_NAME() { return A_MNGT_RPOT_IFTG_SECT_NAME; }
		public void setA_MNGT_RPOT_IFTG_SECT_NAME(String A_MNGT_RPOT_IFTG_SECT_NAME) { this.A_MNGT_RPOT_IFTG_SECT_NAME = A_MNGT_RPOT_IFTG_SECT_NAME; }				
		public String getA_INFM_MTHD_NAME() { return A_INFM_MTHD_NAME; }
		public void setA_INFM_MTHD_NAME(String A_INFM_MTHD_NAME) { this.A_INFM_MTHD_NAME = A_INFM_MTHD_NAME; }				
		public String getA_INFM_MTHD_SECT_NAME() { return A_INFM_MTHD_SECT_NAME; }
		public void setA_INFM_MTHD_SECT_NAME(String A_INFM_MTHD_SECT_NAME) { this.A_INFM_MTHD_SECT_NAME = A_INFM_MTHD_SECT_NAME; }				
		public String getIFMN_OFRN_RECV_MTHD_CODE() { return IFMN_OFRN_RECV_MTHD_CODE; }
		public void setIFMN_OFRN_RECV_MTHD_CODE(String IFMN_OFRN_RECV_MTHD_CODE) { this.IFMN_OFRN_RECV_MTHD_CODE = IFMN_OFRN_RECV_MTHD_CODE; }				
		public String getCMPC_GIDE_MTHD_CODE() { return CMPC_GIDE_MTHD_CODE; }
		public void setCMPC_GIDE_MTHD_CODE(String CMPC_GIDE_MTHD_CODE) { this.CMPC_GIDE_MTHD_CODE = CMPC_GIDE_MTHD_CODE; }				

		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("[PSTL_CODE]"+PSTL_CODE);
			sb.append(",");
			sb.append("[A_ADRS]"+A_ADRS);
			sb.append(",");
			sb.append("[A_CNTT_ADDR_TYPE_NAME]"+A_CNTT_ADDR_TYPE_NAME);
			sb.append(",");
			sb.append("[PHON_NO]"+PHON_NO);
			sb.append(",");
			sb.append("[FAX_NO]"+FAX_NO);
			sb.append(",");
			sb.append("[SMS_MOBL_PHON_NO]"+SMS_MOBL_PHON_NO);
			sb.append(",");
			sb.append("[EMAL_ADRS]"+EMAL_ADRS);
			sb.append(",");
			sb.append("[A_BLNC_TRDG_INFM_MTHD_NAME]"+A_BLNC_TRDG_INFM_MTHD_NAME);
			sb.append(",");
			sb.append("[A_MNGT_RPOT_IFTG_SECT_NAME]"+A_MNGT_RPOT_IFTG_SECT_NAME);
			sb.append(",");
			sb.append("[A_INFM_MTHD_NAME]"+A_INFM_MTHD_NAME);
			sb.append(",");
			sb.append("[A_INFM_MTHD_SECT_NAME]"+A_INFM_MTHD_SECT_NAME);
			sb.append(",");
			sb.append("[IFMN_OFRN_RECV_MTHD_CODE]"+IFMN_OFRN_RECV_MTHD_CODE);
			sb.append(",");
			sb.append("[CMPC_GIDE_MTHD_CODE]"+CMPC_GIDE_MTHD_CODE);
			sb.append("}");
			return sb.toString();
		}

		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("PSTL_CODE",PSTL_CODE);
			m.put("A_ADRS",A_ADRS);
			m.put("A_CNTT_ADDR_TYPE_NAME",A_CNTT_ADDR_TYPE_NAME);
			m.put("PHON_NO",PHON_NO);
			m.put("FAX_NO",FAX_NO);
			m.put("SMS_MOBL_PHON_NO",SMS_MOBL_PHON_NO);
			m.put("EMAL_ADRS",EMAL_ADRS);
			m.put("A_BLNC_TRDG_INFM_MTHD_NAME",A_BLNC_TRDG_INFM_MTHD_NAME);
			m.put("A_MNGT_RPOT_IFTG_SECT_NAME",A_MNGT_RPOT_IFTG_SECT_NAME);
			m.put("A_INFM_MTHD_NAME",A_INFM_MTHD_NAME);
			m.put("A_INFM_MTHD_SECT_NAME",A_INFM_MTHD_SECT_NAME);
			m.put("IFMN_OFRN_RECV_MTHD_CODE",IFMN_OFRN_RECV_MTHD_CODE);
			m.put("CMPC_GIDE_MTHD_CODE",CMPC_GIDE_MTHD_CODE);
			return m;
		}
	}

	public static class OutRec7 { 
	
		// Constructor
		public OutRec7() {
		}

		/* Attributes */
		private String A_BLNC_TRDG_INSO_SECT_CODE;			// 잔고거래통보처구분코드, CHAR(1)	
		private String MNGT_RPOT_SECT_CODE;			// 운용보고서구분코드, CHAR(1)	
		private String A_RP_INFM_SECT_CODE;			// RP통보구분코드, CHAR(1)	
		private String A_DLNG_CTRA_MTHD_CODE;			// 매매체결방법코드, CHAR(1)	
		private String ACNT_ID;			// 계좌ID, BIGINT(19)	

		/* Getters/Setters */
		public void setData(
			String A_BLNC_TRDG_INSO_SECT_CODE,
			String MNGT_RPOT_SECT_CODE,
			String A_RP_INFM_SECT_CODE,
			String A_DLNG_CTRA_MTHD_CODE,
			String ACNT_ID
		 ) {
			this.A_BLNC_TRDG_INSO_SECT_CODE = A_BLNC_TRDG_INSO_SECT_CODE;
			this.MNGT_RPOT_SECT_CODE = MNGT_RPOT_SECT_CODE;
			this.A_RP_INFM_SECT_CODE = A_RP_INFM_SECT_CODE;
			this.A_DLNG_CTRA_MTHD_CODE = A_DLNG_CTRA_MTHD_CODE;
			this.ACNT_ID = ACNT_ID;
		}
		public String getA_BLNC_TRDG_INSO_SECT_CODE() { return A_BLNC_TRDG_INSO_SECT_CODE; }
		public void setA_BLNC_TRDG_INSO_SECT_CODE(String A_BLNC_TRDG_INSO_SECT_CODE) { this.A_BLNC_TRDG_INSO_SECT_CODE = A_BLNC_TRDG_INSO_SECT_CODE; }				
		public String getMNGT_RPOT_SECT_CODE() { return MNGT_RPOT_SECT_CODE; }
		public void setMNGT_RPOT_SECT_CODE(String MNGT_RPOT_SECT_CODE) { this.MNGT_RPOT_SECT_CODE = MNGT_RPOT_SECT_CODE; }				
		public String getA_RP_INFM_SECT_CODE() { return A_RP_INFM_SECT_CODE; }
		public void setA_RP_INFM_SECT_CODE(String A_RP_INFM_SECT_CODE) { this.A_RP_INFM_SECT_CODE = A_RP_INFM_SECT_CODE; }				
		public String getA_DLNG_CTRA_MTHD_CODE() { return A_DLNG_CTRA_MTHD_CODE; }
		public void setA_DLNG_CTRA_MTHD_CODE(String A_DLNG_CTRA_MTHD_CODE) { this.A_DLNG_CTRA_MTHD_CODE = A_DLNG_CTRA_MTHD_CODE; }				
		public String getACNT_ID() { return ACNT_ID; }
		public void setACNT_ID(String ACNT_ID) { this.ACNT_ID = ACNT_ID; }				

		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("[A_BLNC_TRDG_INSO_SECT_CODE]"+A_BLNC_TRDG_INSO_SECT_CODE);
			sb.append(",");
			sb.append("[MNGT_RPOT_SECT_CODE]"+MNGT_RPOT_SECT_CODE);
			sb.append(",");
			sb.append("[A_RP_INFM_SECT_CODE]"+A_RP_INFM_SECT_CODE);
			sb.append(",");
			sb.append("[A_DLNG_CTRA_MTHD_CODE]"+A_DLNG_CTRA_MTHD_CODE);
			sb.append(",");
			sb.append("[ACNT_ID]"+ACNT_ID);
			sb.append("}");
			return sb.toString();
		}

		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("A_BLNC_TRDG_INSO_SECT_CODE",A_BLNC_TRDG_INSO_SECT_CODE);
			m.put("MNGT_RPOT_SECT_CODE",MNGT_RPOT_SECT_CODE);
			m.put("A_RP_INFM_SECT_CODE",A_RP_INFM_SECT_CODE);
			m.put("A_DLNG_CTRA_MTHD_CODE",A_DLNG_CTRA_MTHD_CODE);
			m.put("ACNT_ID",ACNT_ID);
			return m;
		}
	}

	public static class OutRec8 { 
	
		// Constructor
		public OutRec8() {
		}

		/* Attributes */
		private String A_CLNT_ACNT_NAME;			// 고객계좌명, CHAR(60)	

		/* Getters/Setters */
		public void setData(
			String A_CLNT_ACNT_NAME
		 ) {
			this.A_CLNT_ACNT_NAME = A_CLNT_ACNT_NAME;
		}
		public String getA_CLNT_ACNT_NAME() { return A_CLNT_ACNT_NAME; }
		public void setA_CLNT_ACNT_NAME(String A_CLNT_ACNT_NAME) { this.A_CLNT_ACNT_NAME = A_CLNT_ACNT_NAME; }				

		/* toString */
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("[A_CLNT_ACNT_NAME]"+A_CLNT_ACNT_NAME);
			sb.append("}");
			return sb.toString();
		}

		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("A_CLNT_ACNT_NAME",A_CLNT_ACNT_NAME);
			return m;
		}
	}

	/* constructor */
	public Maa0421pVO (String ipAddr, String userID, String mediaType) {
		setHeaderUserID(userID);
		setHeaderTrCode("maa0421p");
		setHeaderDestination((char)'6');
		setHeaderPacketDataType((char)0);
		setHeaderMediaType(mediaType);
		setHeaderIpAddr(ipAddr);
		setCharSet("1");
        setIsActive("0");		
        setIsUseCert(0);
	}

	/* constructor */
	public Maa0421pVO (String ipAddr, String userID, String mediaType, String brchCD, String deptCD, String acctCD) {
		setHeaderUserID(userID);
		setHeaderTrCode("maa0421p");
		setHeaderDestination((char)'6');
		setHeaderPacketDataType((char)0);
		setHeaderMediaType(mediaType);
		setHeaderIpAddr(ipAddr);
		setHeaderBrchCD(brchCD);
		setHeaderDeptCD(deptCD);
		setHeaderAcctCD(acctCD);
		setCharSet("1");
        setIsActive("0");
        setIsUseCert(0);		
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
				MCAUtil.writeString(inRec1.A_PSWD_CRYP, 44, bos);
			}
		}
		
		return bos.toByteArray();
	}
		
	/* parse */
	public void parse(StringInputStream sis) throws Exception {
		
		int cntRecord=0;

		// OutRec1
		outRec1.CLNT_RNNO = sis.readString(13);
		outRec1.VIP_CLNT_YN = sis.readString(1);
		outRec1.MNGG_BRNH_ENTY_CODE = sis.readString(5);
		outRec1.A_MNGG_BRNH_ENTY_NAME = sis.readString(60);
		outRec1.BFMV_DMBE_CODE = sis.readString(5);
		outRec1.A_PREV_MNGG_BRNH_ENTY_NAME = sis.readString(90);
		outRec1.PRFT_CNTR_ENTY_CODE = sis.readString(5);
		outRec1.A_PRFT_CNTR_ENTY_NAME = sis.readString(60);
		outRec1.A_RM_ACRS_BNCD = sis.readString(5);
		outRec1.A_RM_PRFT_CNTR_ENTY_NAME = sis.readString(30);
		outRec1.A_MAIN_MNGR_EMPY_NO = sis.readString(16);
		outRec1.A_MAIN_MNGR_NAME = sis.readString(90);
		outRec1.SBMN_EMPY_NO = sis.readString(16);
		outRec1.A_SBMN_EMPY_NAME = sis.readString(30);
		outRec1.ATRN_EMPY_NO = sis.readString(16);
		outRec1.A_ATPS_NAME = sis.readString(60);
		outRec1.INVT_INVT_AGNT_EMPY_NO = sis.readString(16);
		outRec1.INVT_INVT_AGNT_NAME = sis.readString(60);
		outRec1.A_RM_MNGR_EMPY_NO = sis.readString(16);
		outRec1.A_RM_MNGR_EMPY_NAME = sis.readString(30);
		outRec1.ACNT_STAS_SECT_CODE = sis.readString(2);
		outRec1.A_ACNT_STAS_NAME = sis.readString(30);
		outRec1.ACNT_TYPE_CODE = sis.readString(4);
		outRec1.A_ACNT_TYPE_NAME = sis.readString(90);
		outRec1.INVR_CLSN_CODE = sis.readString(4);
		outRec1.A_INVR_CLSN_NAME = sis.readString(60);
		outRec1.ACCT_YN = sis.readString(1);
		outRec1.A_FNC_IFMN_PRTC_YN = sis.readString(1);
		outRec1.ACNT_OPNG_DATE = sis.readString(8);
		outRec1.A_ACNT_OPNG_BRNH_NAME = sis.readString(60);
		outRec1.A_ACNT_UNFC_DATE = sis.readString(8);
		outRec1.ACNT_CLSG_DATE = sis.readString(8);
		outRec1.A_ACNT_CLSG_BRNH_NAME = sis.readString(90);
		outRec1.LAST_TRDG_DATE = sis.readString(8);
		outRec1.A_CRDT_CTRT_RGSN_DATE = sis.readString(8);
		outRec1.A_RGSN_BRNH_NAME = sis.readString(90);
		outRec1.A_FUNS_ATTR_SECT_CODE = sis.readString(1);
		outRec1.A_FUNS_ATTR_NAME = sis.readString(30);
		outRec1.ERNR_SECT_CODE = sis.readString(3);
		outRec1.A_ERNR_SECT_NAME = sis.readString(60);
		outRec1.FRGN_CRNY_TRDG_ACON_TYPE_CODE = sis.readString(2);
		outRec1.A_FRGN_CRNY_BSWR_SECT_NAME = sis.readString(90);
		outRec1.A_AGNT_YN = sis.readString(1);
		outRec1.AGNT_SECT_CODE = sis.readString(1);
		outRec1.A_AGNT_SECT_NAME = sis.readString(30);
		outRec1.AGNT_RNNO = sis.readString(13);
		outRec1.PSTN_UNIT_TYPE_CODE = sis.readString(4);
		outRec1.BANK_NAME = sis.readString(60);
		outRec1.BKPN_NAME = sis.readString(150);
		outRec1.A_MRGN_CLTN_SECT_CODE_NAME = sis.readString(30);
		outRec1.A_WTHN_ENTS_YN_NAME = sis.readString(30);
		outRec1.A_STX_NAME = sis.readString(30);
		outRec1.A_ODLT_TXTN_SECT_NAME = sis.readString(30);
		outRec1.A_DSCS_FEE_SECT_CODE_NAME = sis.readString(30);
		outRec1.A_FEE_SECT_NAME = sis.readString(30);
		outRec1.A_CTRC_STAS_CODE_NAME = sis.readString(30);
		outRec1.CTRT_DATE = sis.readString(8);
		outRec1.CTRT_XPRN_DATE = sis.readString(8);
		outRec1.CLNT_NAME = sis.readString(90);
		outRec1.A_MEDI_KIND_NAME = sis.readString(90);
		outRec1.A_RLNM_YN = sis.readString(1);
		outRec1.A_RLNM_CNFM_NAME = sis.readString(30);
		outRec1.CPAC_RLNM_CFDT = sis.readString(8);
		outRec1.A_RESP_MSG_CTNT1 = sis.readString(60);
		outRec1.EMAL_SNBC_YN = sis.readString(1);
		outRec1.SNBC_YN = sis.readString(1);
		outRec1.A_ACNT_NO20 = sis.readString(20);
		outRec1.A_OPNG_DATE2 = sis.readString(8);
		outRec1.BANK_PASS_ACNT_NO = sis.readString(20);
		outRec1.A_OPNG_SECT_NAME = sis.readString(60);
		outRec1.A_SNOT_PRCE_EMPY_NAME = sis.readString(60);
		outRec1.A_MEDI_HOLD_YN = sis.readString(1);
		outRec1.ACNT_ENNM = sis.readString(90);
		outRec1.A_ORTR_ACNT_NO = sis.readString(20);
		outRec1.A_RP_DLNG_ALWD_YN = sis.readString(1);
		outRec1.A_ACNT_FRM_NAME = sis.readString(30);
		outRec1.CMA_ATMC_SBTT_YN = sis.readString(1);
		outRec1.GLEG_LINK_YN = sis.readString(1);
		outRec1.ALGR_ACNT_YN = sis.readString(1);
		outRec1.BSNS_PRPS_ACNT_YN = sis.readString(1);
		outRec1.BANK_ACNT_NAME = sis.readString(60);
		outRec1.TRDG_PRPS_NAME = sis.readString(60);
		outRec1.A_CLNT_ACNT_SECT_CODE = sis.readString(1);
		outRec1.A_LN_BANK_NAME = sis.readString(30);
		outRec1.LN_BANK_ACNT_NO = sis.readString(20);
		outRec1.A_VRAC_BANK_NAME = sis.readString(30);
		outRec1.A_VRAC_NO = sis.readString(20);
		outRec1.RFNC_CTNT = sis.readString(75);

		// OutRec2
		outRec2.TRST_MNGT_YN = sis.readString(1);
		outRec2.TRST_FUND_CODE = sis.readString(10);
		outRec2.A_INVT_INVT_YN = sis.readString(1);
		outRec2.A_PSBO_RGST_FUND_CONT = sis.readString(6);
		outRec2.A_PSBO_STAS_NAME = sis.readString(30);
		outRec2.A_PSBO_ISNC_DATE = sis.readString(8);
		outRec2.A_PSBO_ISNC_BRNH_NAME = sis.readString(90);
		outRec2.PSBO_ISNC_CNT = sis.readString(11);
		outRec2.CLSG_DATE = sis.readString(8);
		outRec2.A_TRDG_ENBO_FRST_ISNC_YN = sis.readString(1);
		outRec2.PSBO_PRNT_SECT_CODE = sis.readString(1);
		outRec2.A_SECT_NAME = sis.readString(30);
		outRec2.A_PSBO_ISNC_RESN_CTNT = sis.readString(30);
		outRec2.A_LAST_RGST_DATE = sis.readString(8);
		outRec2.A_LAST_RGST_TRDG_NO = sis.readString(6);
		outRec2.UN_RGST_CONT = sis.readString(6);
		outRec2.A_LAST_PSBO_LINE_CONT = sis.readString(6);
		outRec2.RMS_LN_DATE = sis.readString(8);
		outRec2.A_RMS_INTM_INSN_NAME = sis.readString(60);
		outRec2.A_RMS_LN_INSN_NAME = sis.readString(60);

		// OutRec3
		outRec3.UNI_SVNS_PRDT_CODE = sis.readString(2);
		outRec3.PNSN_SVNS_PRDT_NAME = sis.readString(90);
		outRec3.PMNT_TYPE_CODE = sis.readString(2);
		outRec3.A_TYPE_NAME = sis.readString(60);
		outRec3.SVNS_TYPE_CODE = sis.readString(2);
		outRec3.A_SVNS_TYPE_NAME = sis.readString(30);
		outRec3.TAX_BNFT_DPLC_TYPE_SECT_CODE = sis.readString(1);
		outRec3.DLNG_TYPE_SECT_NAME = sis.readString(30);
		outRec3.CTRC_AMNT = sis.readString(16);
		outRec3.CTRC_PERD = sis.readString(6);
		outRec3.A_PRDT_CNCN_DATE = sis.readString(8);
		outRec3.A_PRDT_MTRY_DATE = sis.readString(8);
		outRec3.MNTH_PMNT_CTRC_AMNT = sis.readString(16);
		outRec3.A_ASM_PMNT_AMNT = sis.readString(18);
		outRec3.PMNT_TCNT = sis.readString(6);
		outRec3.CTRC_DATE = sis.readString(8);

		// OutRec4
		outRec4.A_BSC_DPMY_SECT_NAME = sis.readString(50);
		outRec4.CNSD_ACNT_NO = sis.readString(20);
		outRec4.CNST_TYPE_CODE = sis.readString(2);
		outRec4.A_GLBL_NGHT_MRKT_PARC_YN = sis.readString(1);
		outRec4.A_CNGR_ALRD_SECT_NAME = sis.readString(60);

		// OutRec6
		outRec6.PSTL_CODE = sis.readString(6);
		outRec6.A_ADRS = sis.readString(150);
		outRec6.A_CNTT_ADDR_TYPE_NAME = sis.readString(30);
		outRec6.PHON_NO = sis.readString(20);
		outRec6.FAX_NO = sis.readString(20);
		outRec6.SMS_MOBL_PHON_NO = sis.readString(20);
		outRec6.EMAL_ADRS = sis.readString(150);
		outRec6.A_BLNC_TRDG_INFM_MTHD_NAME = sis.readString(30);
		outRec6.A_MNGT_RPOT_IFTG_SECT_NAME = sis.readString(30);
		outRec6.A_INFM_MTHD_NAME = sis.readString(30);
		outRec6.A_INFM_MTHD_SECT_NAME = sis.readString(30);
		outRec6.IFMN_OFRN_RECV_MTHD_CODE = sis.readString(1);
		outRec6.CMPC_GIDE_MTHD_CODE = sis.readString(1);

		// OutRec7
		outRec7.A_BLNC_TRDG_INSO_SECT_CODE = sis.readString(1);
		outRec7.MNGT_RPOT_SECT_CODE = sis.readString(1);
		outRec7.A_RP_INFM_SECT_CODE = sis.readString(1);
		outRec7.A_DLNG_CTRA_MTHD_CODE = sis.readString(1);
		outRec7.ACNT_ID = sis.readString(19);

		// OutRec8
		try {
			cntRecord=Integer.parseInt(sis.readString(4).trim());
		} catch (NumberFormatException e) {
			throw new Exception("OutRec8 NumberFormatException");
		}
		
		if (cntRecord < 0) {
		    cntRecord = 0;
		}
		
		for (int i=0;i<cntRecord;i++) {
			OutRec8 outRecord = new OutRec8();
			outRecord.A_CLNT_ACNT_NAME = sis.readString(60);
			outRec8.add(outRecord);
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
		sb.append("\t"+outRec2.toString()+"\n");
		sb.append("[outRec3]\n");
		sb.append("\t"+outRec3.toString()+"\n");
		sb.append("[outRec4]\n");
		sb.append("\t"+outRec4.toString()+"\n");
		sb.append("[outRec6]\n");
		sb.append("\t"+outRec6.toString()+"\n");
		sb.append("[outRec7]\n");
		sb.append("\t"+outRec7.toString()+"\n");
		sb.append("[outRec8]\n");
		for (int i=0;i<outRec8.size();i++) {
			sb.append("\t"+outRec8.get(i).toString()+"\n");
		}

		return sb.toString();
	}

	/* toMap */
	public Map<String, Object> toMap() {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("outRec1",outRec1.toMap());
		m.put("outRec2",outRec2.toMap());
		m.put("outRec3",outRec3.toMap());
		m.put("outRec4",outRec4.toMap());
		m.put("outRec6",outRec6.toMap());
		m.put("outRec7",outRec7.toMap());

		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();

		for (int i=0;i<outRec8.size();i++) {
			list1.add(outRec8.get(i).toMap());
		}
		m.put("outRec8",list1);

		return m;
	}
}
