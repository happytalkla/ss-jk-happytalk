package ht.mca.vo;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.ss.common.channel.mca.common.net.StringInputStream;
import com.ss.common.channel.mca.util.MCAUtil;
import com.ss.common.channel.mca.vo.VOSupport;
import com.ss.common.util.StringUtility;

import ht.util.StringUtil;
import lombok.Data;

public class Igv2001pVO extends VOSupport{

	/* Input Records */
	private InRec1 inRec1 = new InRec1();

	/* Output Records */
	private OutRec1 outRec1 = new OutRec1();

	/* Output Records */
	private OutRec2 outRec2 = new OutRec2();
	
	/* Get Instances */
	public InRec1 getInRec1() { return inRec1; }
	public OutRec1 getOutRec1() { return outRec1; }
	public OutRec2 getOutRec2() { return outRec2; }

	/* Set Instances */
	public void setInRec1(InRec1 inRec1) { this.inRec1 = inRec1; }
	public void setOutRec1(OutRec1 outRec1) { this.outRec1 = outRec1; }
	public void setOutRec2(OutRec2 outRec2) { this.outRec2 = outRec2; }

	/* InRec Classes */
	@Data
	public static class InRec1 {
		
		/* Attributes */
		private String ZRN_FUND_CD;			//협회표준쿄드
		private String ZRN_DT;				//펀드평가정보일자
		private String FUND_CODE;			//자체펀드코드
		private String pText;
		
		// Constructor
		public InRec1() {
			pText = null;
		}
	}

	/* OutRec Classes */
	@Data
	public static class OutRec1 { 
		
		/* Attributes */
		String ZRN_CLASS_NM;				//유형명
		String ZRN_ESTAB_DT;				//설정일자
		String ZRN_MANAGE_NM;				//운용역
		String ZRN_BEFORE_FEE;				//선취보수
		String ZRN_OPER_CO_NM;				//운용사축약형
		String CMPY_NAME;					//회사명
		String ZRN_MNG_FEE;					//운용보수율
		String ZRN_SAL_FEE_R;				//판매보수율
		String ZRN_TB_FEE;					//수탁보수율
		String ZRN_AT_FEE;					//사무수탁보수율
		String ZRN_ADV_FEE;					//자문보수율
		String ZRN_FST_SET_AM;				//설정액
		String ZRN_PRI;						//기준가
		String ZRN_MIX_BM_NM;				//복합벤치마크지수명
		String ZRN_REDEM_TXT;				//환매수수료서술
		String SLCP_INSN_NAME;				//복수펀드판매사명
		String A_MNGT_STRG_1_CTNT;			//운영전략1
		String A_MNGT_STRG_2_CTNT;			//운용전략2
		String A_MNGT_STRG_3_CTNT;			//운용전략3
		String NOTE_CTNT;					//비고
		String ZRN_FUND_SECT_CD;			//펀드구분코드
		String ZRN_FUND_SECT_NM;			//펀드구분이름, ZRN_FUND_SECT_CD가 1:수익증권, 2:뮤추얼
		String ZRN_OPER_FUND_CD;			//운영사펀드코드
		String MGCM_FUND_NAME;				//운용사펀드명
		String ZRN_FUND_CD;					//표준 펀드코드
		String TRST_SECT_CODE;						//신탁구분코드
		String RWRD_CLCL_PERD_MNTH_CONT;			//보수계산기간월수
		String FUND_ACNG_PERD;						//펀드회계기간
		String FUND_CTRC_PERD;						//펀드계약기간
		String RDMN_CNCN_DATE;						//상환해지일자
		String STLN_DATE;							//결산일자
		String NEXT_TERM_STLN_XPCN_DATE;			//차기결산예정일자
		String STND_PRIC_APLY_DAYS;					//기준가적용일자
		String A_FUND_KIND_SECT_CODE;				//펀드종류코드
		String STCK_IN_XPCN_DATE;					//주식입고 예정일자
		String STLT_DAYS;							//결제일수
		String ZRN_TRUST_ACC_PRD;					//신탁회계기간
		String BSNS_DAY_SECT_CODE;					//영업일구분코드
		String BSNS_DAY_SECT_NM;					//영업일구분이름		
		String WRAP_SECT_CODE;						//투신펀드랩 구분코드
		String CMA_YN;								//CMA 여부
		String A_MNGD_WRAP_YN;						//일임가능여부
		String STLT_PRID_TYPE_CODE;					//결제주기코드
		String STLT_PRID_TYPE_NM;					//결제주기이름
		String ZRN_SET_SECT_CD;						//설정구분코드
		String ZRN_SET_SECT_NM;						//설정구분이름
		String LATE_TRDG_STNR_TIME;					//마감후거래기준 시간
		String DSCY_ALWD_YN;						//임의식가능여부
		String DFRT_TYPE_ALWD_YN;					//거치식 가능여부
		String ACMV_ALWD_YN;						//적립식 가능여부
		String VATY_ALWD_YN;						//자유적립식 가능여부
		String NEW_PSBL_YN;							//신규가능여부
		String BUY_ALWD_YN;							//매수가능여부
		String SELL_ALWD_YN;						//매도가능여부
		String NDVL_ALWD_YN;						//개인가능여부
		String INSN_ALWD_YN;						//법인가능여부
		String NON_RSDT_ALWD_YN;					//비거주자 가능여부
		String FRGR_ALWD_YN;						//외국인가능여부
		String TTDD_TOTL_DSPS_QNTY;					//당일총매각수량
		String UNSA_QNTY;							//미매각수량
		String TOTL_STNG_QNTY;						//총설정수량
		String RNVT_DATE;							//재투자일자
		String STLN_DTRN_RATE;						//결사분배비율
		String WHST_STND_PRIC;						//결산시기준가
		String WHST_TXTN_STND_PRIC;					//결산시과세기준가
		String STLN_HY_TSP;							//결산HY과세기준가
		String IG_STLN_STND_PRIC;					//IG결산기준가
		String WHRN_STND_PRIC;						//재투자시기준가
		String HOLD_LSTG_SECT_CODE;					//투신펀드 상장구분코드
		String LSTG_DATE;							//상장일자
		String ISCD;								//종목코드
		String ITEM_ABBR_NM;						//종목축약명
		String AGRT_STCK_INSE_MAXM_RATE;			//약관주식편입최대비율
		String BOND_INSE_MAXM_RATE;					//채권편입최대비율
		String FTRS_OPTS_INVT_RATE;					//선물옵션투자비율
		String ETC_INVT_RATE;						//기타투자비율
		String FUND_FEE_SECT_CODE;					//투신펀드수수료구분코드
		String FUND_FEE_SECT_NM;					//투신펀드수수료구분이름, 1=환매;2=선취판매;3=후취판매;4=환매+선취;5=환매+후취
		String APLY_YN;								//적용여부
		String XPRN_AFTE_PRT_RDMP_ALWD_YN;			//계좌만기후부분환매가능
		String SPCL_MDWY_CNCN_FCLC_YN;				//특별중도해지수수료 징수여부
		String SKTY_FUND_YN;						//주식형펀드여부
		String OFRG_UNIT_QNTY;						//모집수량단위
		String OFRG_UNIT_AMNT;						//모집단위금액
		String JN_LMT_LWST_QNTY;					//가입한도최저수량
		String ATFL_PATH_NAME;						//펀드리포트
		String OPRN_BEGN_DATE;						//운영개시일자
		String HTS_BUY_PSBL_YN;						//HTS매수가능여부
		String A_FRGN_SECT_CODE;					//해외구분코드
		String A_FRGN_SECT_NM;					//해외구분이름
		String PRDT_RTNG_SECT_CODE;					//상품등급
		String A_CMMT_CTNT;							//상품특성
		String A_PRDT_RTNG_NAME;					//상품등급명
		String FUND_DTLS_CMEN_CTNT;					//펀드상세주석내용
		String FUND_SRS_CODE;						//펀드시리즈코드
		String NOTE_NAME;							//비고명
		String CPTL_MRKT_FUND_TYPE_CODE;			//자본시장펀드유형코드
		String CPTL_MRKT_FUND_TYPE_NM;			//자본시장펀드유형이름
		String FUND_APLY_LAW_SECT_CODE;				//펀드적용법 구분코드
		String FUND_APLY_LAW_SECT_NM;				//펀드적용법 구분이름 1:간투법, 2:자본시장법
		String FUND_SECT_CODE;						//설립구분코드
		String FUND_SECT_NM;						//설립구분이름, 1:공모, 2:사모
		String SLCP_MVE_ALWD_YN;					//판매사이동가능여부
		String SLCP_MVE_ALWD_YN_NM;					//판매사이동가능여부 이름 Y:가능, N:불가능
		String A_TRDG_ALWD_YN;						//거래가능여부
		String PRFT_DFR_FUND_YN;					//이익유보펀드여부
		String UNI_SVNS_PRDT_CODE;					//연합회저축상품코드(상품정보)
		String UNI_SVNS_PRDT_NM;					//연합회저축상품이름(상품정보)
		String A_STNR_DATE1;						//기준일자(기준가적용일)
		String A_STNR_DATE2;						//기준일자(매도결제일)
		String A_STNR_DATE3;						//기준일자(매수결제일)
		String OFRG_UNIT_CAMT;						//모집단위통화금액
		
		// Constructor
		public OutRec1() {
		}


		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("ZRN_CLASS_NM", ZRN_CLASS_NM );
			m.put("ZRN_ESTAB_DT", ZRN_ESTAB_DT );
			m.put("ZRN_MANAGE_NM", ZRN_MANAGE_NM );
			m.put("ZRN_BEFORE_FEE", ZRN_BEFORE_FEE );
			m.put("ZRN_OPER_CO_NM", ZRN_OPER_CO_NM );
			m.put("CMPY_NAME", CMPY_NAME );
			m.put("ZRN_MNG_FEE", ZRN_MNG_FEE );
			m.put("ZRN_SAL_FEE_R", ZRN_SAL_FEE_R );
			m.put("ZRN_TB_FEE", ZRN_TB_FEE );
			m.put("ZRN_AT_FEE", ZRN_AT_FEE );
			m.put("ZRN_ADV_FEE", ZRN_ADV_FEE );
			m.put("ZRN_FST_SET_AM", ZRN_FST_SET_AM );
			m.put("ZRN_PRI", ZRN_PRI );
			m.put("ZRN_MIX_BM_NM", ZRN_MIX_BM_NM );
			m.put("ZRN_REDEM_TXT", ZRN_REDEM_TXT );
			m.put("SLCP_INSN_NAME", SLCP_INSN_NAME );
			m.put("A_MNGT_STRG_1_CTNT", A_MNGT_STRG_1_CTNT );
			m.put("A_MNGT_STRG_2_CTNT", A_MNGT_STRG_2_CTNT );
			m.put("A_MNGT_STRG_3_CTNT", A_MNGT_STRG_3_CTNT );
			m.put("NOTE_CTNT", NOTE_CTNT );
			m.put("ZRN_FUND_SECT_CD", ZRN_FUND_SECT_CD );
			m.put("ZRN_FUND_SECT_NM", ZRN_FUND_SECT_NM );
			m.put("ZRN_OPER_FUND_CD", ZRN_OPER_FUND_CD );
			m.put("MGCM_FUND_NAME", MGCM_FUND_NAME );
			m.put("ZRN_FUND_CD", ZRN_FUND_CD );
			m.put("TRST_SECT_CODE", TRST_SECT_CODE );
			m.put("RWRD_CLCL_PERD_MNTH_CONT", RWRD_CLCL_PERD_MNTH_CONT );
			m.put("FUND_ACNG_PERD", FUND_ACNG_PERD );
			m.put("FUND_CTRC_PERD", FUND_CTRC_PERD );
			m.put("RDMN_CNCN_DATE", RDMN_CNCN_DATE );
			m.put("STLN_DATE", STLN_DATE );
			m.put("NEXT_TERM_STLN_XPCN_DATE", NEXT_TERM_STLN_XPCN_DATE );
			m.put("STND_PRIC_APLY_DAYS", STND_PRIC_APLY_DAYS );
			m.put("A_FUND_KIND_SECT_CODE", A_FUND_KIND_SECT_CODE );
			m.put("STCK_IN_XPCN_DATE", STCK_IN_XPCN_DATE );
			m.put("STLT_DAYS", STLT_DAYS );
			m.put("ZRN_TRUST_ACC_PRD", ZRN_TRUST_ACC_PRD );
			m.put("BSNS_DAY_SECT_CODE", BSNS_DAY_SECT_CODE );
			m.put("BSNS_DAY_SECT_NM", BSNS_DAY_SECT_NM );
			m.put("WRAP_SECT_CODE", WRAP_SECT_CODE );
			m.put("CMA_YN", CMA_YN  );
			m.put("A_MNGD_WRAP_YN", A_MNGD_WRAP_YN );
			m.put("STLT_PRID_TYPE_CODE", STLT_PRID_TYPE_CODE );
			m.put("STLT_PRID_TYPE_NM", STLT_PRID_TYPE_NM );
			m.put("ZRN_SET_SECT_CD", ZRN_SET_SECT_CD );
			m.put("ZRN_SET_SECT_NM", ZRN_SET_SECT_NM );
			m.put("LATE_TRDG_STNR_TIME", LATE_TRDG_STNR_TIME );
			m.put("DSCY_ALWD_YN", DSCY_ALWD_YN );
			m.put("DFRT_TYPE_ALWD_YN", DFRT_TYPE_ALWD_YN );
			m.put("ACMV_ALWD_YN", ACMV_ALWD_YN );
			m.put("VATY_ALWD_YN", VATY_ALWD_YN );
			m.put("NEW_PSBL_YN", NEW_PSBL_YN );
			m.put("BUY_ALWD_YN", BUY_ALWD_YN );
			m.put("SELL_ALWD_YN", SELL_ALWD_YN );
			m.put("NDVL_ALWD_YN", NDVL_ALWD_YN );
			m.put("INSN_ALWD_YN", INSN_ALWD_YN );
			m.put("NON_RSDT_ALWD_YN", NON_RSDT_ALWD_YN );
			m.put("FRGR_ALWD_YN", FRGR_ALWD_YN );
			m.put("TTDD_TOTL_DSPS_QNTY", TTDD_TOTL_DSPS_QNTY );
			m.put("UNSA_QNTY", UNSA_QNTY );
			m.put("TOTL_STNG_QNTY", TOTL_STNG_QNTY );
			m.put("RNVT_DATE", RNVT_DATE );
			m.put("STLN_DTRN_RATE", STLN_DTRN_RATE );
			m.put("WHST_STND_PRIC", WHST_STND_PRIC );
			m.put("WHST_TXTN_STND_PRIC", WHST_TXTN_STND_PRIC );
			m.put("STLN_HY_TSP", STLN_HY_TSP );
			m.put("IG_STLN_STND_PRIC", IG_STLN_STND_PRIC );
			m.put("WHRN_STND_PRIC", WHRN_STND_PRIC );
			m.put("HOLD_LSTG_SECT_CODE", HOLD_LSTG_SECT_CODE );
			m.put("LSTG_DATE", LSTG_DATE );
			m.put("ISCD", ISCD  );
			m.put("ITEM_ABBR_NM", ITEM_ABBR_NM );
			m.put("AGRT_STCK_INSE_MAXM_RATE", AGRT_STCK_INSE_MAXM_RATE );
			m.put("BOND_INSE_MAXM_RATE", BOND_INSE_MAXM_RATE );
			m.put("FTRS_OPTS_INVT_RATE", FTRS_OPTS_INVT_RATE );
			m.put("ETC_INVT_RATE", ETC_INVT_RATE );
			m.put("FUND_FEE_SECT_CODE", FUND_FEE_SECT_CODE );
			m.put("FUND_FEE_SECT_NM", FUND_FEE_SECT_NM );
			m.put("APLY_YN", APLY_YN );
			m.put("XPRN_AFTE_PRT_RDMP_ALWD_YN", XPRN_AFTE_PRT_RDMP_ALWD_YN );
			m.put("SPCL_MDWY_CNCN_FCLC_YN", SPCL_MDWY_CNCN_FCLC_YN );
			m.put("SKTY_FUND_YN", SKTY_FUND_YN );
			m.put("OFRG_UNIT_QNTY", OFRG_UNIT_QNTY );
			m.put("OFRG_UNIT_AMNT", OFRG_UNIT_AMNT );
			m.put("JN_LMT_LWST_QNTY", JN_LMT_LWST_QNTY );
			m.put("ATFL_PATH_NAME", ATFL_PATH_NAME );
			m.put("OPRN_BEGN_DATE", OPRN_BEGN_DATE );
			m.put("HTS_BUY_PSBL_YN", HTS_BUY_PSBL_YN );
			m.put("A_FRGN_SECT_CODE", A_FRGN_SECT_CODE );
			m.put("A_FRGN_SECT_NM", A_FRGN_SECT_NM );
			m.put("PRDT_RTNG_SECT_CODE", PRDT_RTNG_SECT_CODE );
			m.put("A_CMMT_CTNT", A_CMMT_CTNT );
			m.put("A_PRDT_RTNG_NAME", A_PRDT_RTNG_NAME );
			m.put("FUND_DTLS_CMEN_CTNT", FUND_DTLS_CMEN_CTNT );
			m.put("FUND_SRS_CODE", FUND_SRS_CODE );
			m.put("NOTE_NAME", NOTE_NAME );
			m.put("CPTL_MRKT_FUND_TYPE_CODE", CPTL_MRKT_FUND_TYPE_CODE );
			m.put("CPTL_MRKT_FUND_TYPE_NM", CPTL_MRKT_FUND_TYPE_NM );
			m.put("FUND_APLY_LAW_SECT_CODE", FUND_APLY_LAW_SECT_CODE );
			m.put("FUND_APLY_LAW_SECT_NM", FUND_APLY_LAW_SECT_NM );
			m.put("FUND_SECT_CODE", FUND_SECT_CODE );
			m.put("FUND_SECT_NM", FUND_SECT_NM );
			m.put("SLCP_MVE_ALWD_YN", SLCP_MVE_ALWD_YN );
			m.put("SLCP_MVE_ALWD_YN_NM", SLCP_MVE_ALWD_YN_NM );
			m.put("A_TRDG_ALWD_YN", A_TRDG_ALWD_YN );
			m.put("PRFT_DFR_FUND_YN", PRFT_DFR_FUND_YN );
			m.put("UNI_SVNS_PRDT_CODE", UNI_SVNS_PRDT_CODE );
			m.put("UNI_SVNS_PRDT_NM", UNI_SVNS_PRDT_NM );
			m.put("A_STNR_DATE1", A_STNR_DATE1 );
			m.put("A_STNR_DATE2", A_STNR_DATE2 );
			m.put("A_STNR_DATE3", A_STNR_DATE3 );
			m.put("OFRG_UNIT_CAMT", OFRG_UNIT_CAMT );
			return m;
		}
	}
	

	/* OutRec Classes */
	@Data
	public static class OutRec2 { 

		/* Attributes */
		private String APLY_STNR_DATE;		// 적용기준일자1
		private String XPRN_PERD_DSCN_CTNT;
		private String A_PRNR_DSCN_CTNT;
		private String RDMP_FEE_RATE1;
		private String ASIG_FEE_RATE;
		private String SALE_AMNT;
		private String SALE_FEE_RATE;
		private String ZRN_RMK_CONT;
		private String NOTE_CTNT1;
		private String SALE_FEE_APLY_END_CAMT;
		// Constructor
		public OutRec2() {
		}
		
		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("APLY_STNR_DATE", APLY_STNR_DATE);
			m.put("XPRN_PERD_DSCN_CTNT", XPRN_PERD_DSCN_CTNT);
			m.put("A_PRNR_DSCN_CTNT", A_PRNR_DSCN_CTNT);
			m.put("RDMP_FEE_RATE1", RDMP_FEE_RATE1);
			m.put("ASIG_FEE_RATE", ASIG_FEE_RATE);
			m.put("SALE_AMNT", SALE_AMNT);
			m.put("SALE_FEE_RATE", SALE_FEE_RATE);
			m.put("ZRN_RMK_CONT", ZRN_RMK_CONT);
			m.put("NOTE_CTNT1", NOTE_CTNT1);
			m.put("SALE_FEE_APLY_END_CAMT", SALE_FEE_APLY_END_CAMT);			
			return m;
		}
	}
	
	/* constructor */
	public Igv2001pVO (String ipAddr, String userID, String mediaType) {
		setHeaderUserID(userID);
		setHeaderTrCode("igv2001p");
		setHeaderDestination((char)'6');
		setHeaderPacketDataType((char)0);
		setHeaderMediaType(mediaType);
		setHeaderIpAddr(ipAddr);
		setCharSet("1");
        setIsActive("0");		
	}

	/* constructor */
	public Igv2001pVO (String ipAddr, String userID, String mediaType, String brchCD, String deptCD, String acctCD) {
		setHeaderUserID(userID);
		setHeaderTrCode("igv2001p");
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
				MCAUtil.writeString(inRec1.ZRN_FUND_CD, 12, bos);
				MCAUtil.writeString(inRec1.ZRN_DT, 8, bos);
				MCAUtil.writeString(inRec1.FUND_CODE, 7, bos);
			}			
		}
		
		return bos.toByteArray();
	}
		
	/* parse */
	public void parse(StringInputStream sis) throws Exception {
		
		outRec1.ZRN_CLASS_NM = StringUtil.changBlankToDash(sis.readString(80));
		outRec1.ZRN_ESTAB_DT = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.ZRN_MANAGE_NM = StringUtil.changBlankToDash(sis.readString(80));
		outRec1.ZRN_BEFORE_FEE = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(19)));
		outRec1.ZRN_OPER_CO_NM = StringUtil.changBlankToDash(sis.readString(80));
		outRec1.CMPY_NAME  = StringUtil.changBlankToDash(sis.readString(150));
		outRec1.ZRN_MNG_FEE  = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(19)));
		outRec1.ZRN_SAL_FEE_R = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(19)));
		outRec1.ZRN_TB_FEE = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(19)));
		outRec1.ZRN_AT_FEE = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(19)));
		outRec1.ZRN_ADV_FEE  = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(19)));
		outRec1.ZRN_FST_SET_AM = StringUtil.changeStrToLong(StringUtil.changBlankToDash(sis.readString(18)));
		outRec1.ZRN_PRI = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(19)));
		outRec1.ZRN_MIX_BM_NM = StringUtil.changBlankToDash(sis.readString(100));
		outRec1.ZRN_REDEM_TXT = StringUtil.changBlankToDash(sis.readString(200));
		outRec1.SLCP_INSN_NAME = StringUtil.changBlankToDash(sis.readString(150));
		outRec1.A_MNGT_STRG_1_CTNT = StringUtil.changBlankToDash(sis.readString(1000));
		outRec1.A_MNGT_STRG_2_CTNT = StringUtil.changBlankToDash(sis.readString(1000));
		outRec1.A_MNGT_STRG_3_CTNT = StringUtil.changBlankToDash(sis.readString(1000));
		outRec1.NOTE_CTNT  = StringUtil.changBlankToDash(sis.readString(300));
		outRec1.ZRN_FUND_SECT_CD = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.ZRN_OPER_FUND_CD = StringUtil.changBlankToDash(sis.readString(12));
		outRec1.MGCM_FUND_NAME = StringUtil.changBlankToDash(sis.readString(150));
		outRec1.ZRN_FUND_CD  = StringUtil.changBlankToDash(sis.readString(12));
		outRec1.TRST_SECT_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.RWRD_CLCL_PERD_MNTH_CONT = StringUtil.changeStrToLong(StringUtil.changBlankToDash(sis.readString(6)));
		outRec1.FUND_ACNG_PERD = StringUtil.changeStrToLong(StringUtil.changBlankToDash(sis.readString(6)));
		outRec1.FUND_CTRC_PERD = StringUtil.changeStrToLong(StringUtil.changBlankToDash(sis.readString(6)));
		outRec1.RDMN_CNCN_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.STLN_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.NEXT_TERM_STLN_XPCN_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.STND_PRIC_APLY_DAYS  = StringUtil.changeStrToLong(StringUtil.changBlankToDash(sis.readString(6)));
		outRec1.A_FUND_KIND_SECT_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.STCK_IN_XPCN_DATE  = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.STLT_DAYS = StringUtil.changeStrToLong(StringUtil.changBlankToDash(sis.readString(6)));
		outRec1.ZRN_TRUST_ACC_PRD  = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.BSNS_DAY_SECT_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.WRAP_SECT_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.CMA_YN  = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.A_MNGD_WRAP_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.STLT_PRID_TYPE_CODE  = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.ZRN_SET_SECT_CD  = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.LATE_TRDG_STNR_TIME  = StringUtil.changBlankToDash(sis.readString(6));
		outRec1.DSCY_ALWD_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.DFRT_TYPE_ALWD_YN  = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.ACMV_ALWD_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.VATY_ALWD_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.NEW_PSBL_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.BUY_ALWD_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.SELL_ALWD_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.NDVL_ALWD_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.INSN_ALWD_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.NON_RSDT_ALWD_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.FRGR_ALWD_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.TTDD_TOTL_DSPS_QNTY  = StringUtil.changeStrToLong(StringUtil.changBlankToDash(sis.readString(16)));
		outRec1.UNSA_QNTY = StringUtil.changeStrToLong(StringUtil.changBlankToDash(sis.readString(16)));
		outRec1.TOTL_STNG_QNTY = StringUtil.changeStrToLong(StringUtil.changBlankToDash(sis.readString(16)));
		outRec1.RNVT_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.STLN_DTRN_RATE = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(17)));
		outRec1.WHST_STND_PRIC = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(20)));
		outRec1.WHST_TXTN_STND_PRIC  = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(20)));
		outRec1.STLN_HY_TSP = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(20)));
		outRec1.IG_STLN_STND_PRIC  = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(20)));
		outRec1.WHRN_STND_PRIC = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(12)));
		outRec1.HOLD_LSTG_SECT_CODE  = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.LSTG_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.ISCD  = StringUtil.changBlankToDash(sis.readString(12));
		outRec1.ITEM_ABBR_NM = StringUtil.changBlankToDash(sis.readString(60));
		outRec1.AGRT_STCK_INSE_MAXM_RATE = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(7)));
		outRec1.BOND_INSE_MAXM_RATE  = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(7)));
		outRec1.FTRS_OPTS_INVT_RATE  = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(7)));
		outRec1.ETC_INVT_RATE  = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(7)));
		outRec1.FUND_FEE_SECT_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.APLY_YN   = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.XPRN_AFTE_PRT_RDMP_ALWD_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.SPCL_MDWY_CNCN_FCLC_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.SKTY_FUND_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.OFRG_UNIT_QNTY = StringUtil.changeStrToLong(StringUtil.changBlankToDash(sis.readString(16)));
		outRec1.OFRG_UNIT_AMNT = StringUtil.changeStrToLong(StringUtil.changBlankToDash(sis.readString(16)));
		outRec1.JN_LMT_LWST_QNTY = StringUtil.changeStrToLong(StringUtil.changBlankToDash(sis.readString(16)));
		outRec1.ATFL_PATH_NAME = StringUtil.changBlankToDash(sis.readString(300));
		outRec1.OPRN_BEGN_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.HTS_BUY_PSBL_YN  = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.A_FRGN_SECT_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.PRDT_RTNG_SECT_CODE  = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.A_CMMT_CTNT = StringUtil.changBlankToDash(sis.readString(4000));
		outRec1.A_PRDT_RTNG_NAME = StringUtil.changBlankToDash(sis.readString(60));
		outRec1.FUND_DTLS_CMEN_CTNT  = StringUtil.changBlankToDash(sis.readString(450));
		outRec1.FUND_SRS_CODE  = StringUtil.changBlankToDash(sis.readString(4));
		outRec1.NOTE_NAME = StringUtil.changBlankToDash(sis.readString(60));
		outRec1.CPTL_MRKT_FUND_TYPE_CODE = StringUtil.changBlankToDash(sis.readString(2));
		outRec1.FUND_APLY_LAW_SECT_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.FUND_SECT_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.SLCP_MVE_ALWD_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.A_TRDG_ALWD_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.PRFT_DFR_FUND_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.UNI_SVNS_PRDT_CODE = StringUtil.changBlankToDash(sis.readString(2));
		outRec1.A_STNR_DATE1 = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.A_STNR_DATE2 = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.A_STNR_DATE3 = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.OFRG_UNIT_CAMT = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(19)));
		
		sis.readString(4);
		
		outRec2.APLY_STNR_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec2.XPRN_PERD_DSCN_CTNT = StringUtil.changBlankToDash(sis.readString(150));
		outRec2.A_PRNR_DSCN_CTNT = StringUtil.changBlankToDash(sis.readString(20));
		outRec2.RDMP_FEE_RATE1 = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(17)));
		outRec2.ASIG_FEE_RATE = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(17)));
		outRec2.SALE_AMNT = StringUtil.changeStrToLong(StringUtil.changBlankToDash(sis.readString(16)));
		outRec2.SALE_FEE_RATE = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(16)));
		outRec2.ZRN_RMK_CONT = StringUtil.changBlankToDash(sis.readString(300));
		outRec2.NOTE_CTNT1 = StringUtil.changBlankToDash(sis.readString(150));
		outRec2.SALE_FEE_APLY_END_CAMT = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(19)));
		
		if("1".equals(outRec1.ZRN_FUND_SECT_CD)) {
			outRec1.ZRN_FUND_SECT_NM = "수익증권";
		}else if("2".equals(outRec1.ZRN_FUND_SECT_CD)) {
			outRec1.ZRN_FUND_SECT_NM = "뮤추얼";
		}else {
			outRec1.ZRN_FUND_SECT_NM = "";
		}
		
		if("1".equals(outRec1.ZRN_SET_SECT_CD)) {
			outRec1.ZRN_SET_SECT_NM = "추가";
		}else if("2".equals(outRec1.ZRN_SET_SECT_CD) || "3".equals(outRec1.ZRN_SET_SECT_CD)) {
			outRec1.ZRN_SET_SECT_NM = "단위";
		}else {
			outRec1.ZRN_SET_SECT_NM = "";
		}
				
		if("1".equals(outRec1.FUND_SECT_CODE)) {
			outRec1.FUND_SECT_NM = "공모";
		}else if("2".equals(outRec1.FUND_SECT_CODE)) {
			outRec1.FUND_SECT_NM = "사모";
		}else {
			outRec1.FUND_SECT_NM = "";
		}
		
		if("1".equals(outRec1.FUND_APLY_LAW_SECT_CODE)) {
			outRec1.FUND_APLY_LAW_SECT_NM = "간투법";
		}else if("2".equals(outRec1.FUND_APLY_LAW_SECT_CODE)) {
			outRec1.FUND_APLY_LAW_SECT_NM = "자본시장법";
		}else {
			outRec1.FUND_APLY_LAW_SECT_NM = "";
		}
		
		if("Y".equals(outRec1.SLCP_MVE_ALWD_YN)) {
			outRec1.SLCP_MVE_ALWD_YN_NM = "가능";
		}else if("N".equals(outRec1.SLCP_MVE_ALWD_YN)) {
			outRec1.SLCP_MVE_ALWD_YN = "불가능";
		}else {
			outRec1.SLCP_MVE_ALWD_YN = "";
		}		
		
		if("1".equals(outRec1.FUND_FEE_SECT_CODE)) {
			outRec1.FUND_FEE_SECT_NM = "환매";
		}else if("2".equals(outRec1.FUND_FEE_SECT_CODE)) {
			outRec1.FUND_FEE_SECT_NM = "선취판매";
		}else if("3".equals(outRec1.FUND_FEE_SECT_CODE)) {
			outRec1.FUND_FEE_SECT_NM = "후취판매";
		}else if("4".equals(outRec1.FUND_FEE_SECT_CODE)) {
			outRec1.FUND_FEE_SECT_NM = "환매+선취";			
		}else if("5".equals(outRec1.FUND_FEE_SECT_CODE)) {
			outRec1.FUND_FEE_SECT_NM = "환매+후취";
		}else {
			outRec1.FUND_FEE_SECT_NM = "";
		}				
		
		if("11".equals(outRec1.CPTL_MRKT_FUND_TYPE_CODE)) {
			outRec1.CPTL_MRKT_FUND_TYPE_NM = "채권형";
		}else if("12".equals(outRec1.CPTL_MRKT_FUND_TYPE_CODE)) {
			outRec1.CPTL_MRKT_FUND_TYPE_NM = "채권파생형";
		}else if("21".equals(outRec1.CPTL_MRKT_FUND_TYPE_CODE)) {
			outRec1.CPTL_MRKT_FUND_TYPE_NM = "주식형";
		}else if("22".equals(outRec1.CPTL_MRKT_FUND_TYPE_CODE)) {
			outRec1.CPTL_MRKT_FUND_TYPE_NM = "주식파생형";			
		}else if("31".equals(outRec1.CPTL_MRKT_FUND_TYPE_CODE)) {
			outRec1.CPTL_MRKT_FUND_TYPE_NM = "혼합주식형";
		}else if("32".equals(outRec1.CPTL_MRKT_FUND_TYPE_CODE)) {
			outRec1.CPTL_MRKT_FUND_TYPE_NM = "혼합주식파생형";
		}else if("41".equals(outRec1.CPTL_MRKT_FUND_TYPE_CODE)) {
			outRec1.CPTL_MRKT_FUND_TYPE_NM = "혼합채권형";
		}else if("42".equals(outRec1.CPTL_MRKT_FUND_TYPE_CODE)) {
			outRec1.CPTL_MRKT_FUND_TYPE_NM = "혼합채권파생형";		
		}else if("51".equals(outRec1.CPTL_MRKT_FUND_TYPE_CODE)) {
			outRec1.CPTL_MRKT_FUND_TYPE_NM = "투자계약증권";
		}else if("52".equals(outRec1.CPTL_MRKT_FUND_TYPE_CODE)) {
			outRec1.CPTL_MRKT_FUND_TYPE_NM = "투자계약증권파생";
		}else if("61".equals(outRec1.CPTL_MRKT_FUND_TYPE_CODE)) {
			outRec1.CPTL_MRKT_FUND_TYPE_NM = "재간접형";
		}else if("62".equals(outRec1.CPTL_MRKT_FUND_TYPE_CODE)) {
			outRec1.CPTL_MRKT_FUND_TYPE_NM = "재간접파생형";		
		}else if("71".equals(outRec1.CPTL_MRKT_FUND_TYPE_CODE)) {
			outRec1.CPTL_MRKT_FUND_TYPE_NM = "부동산";
		}else if("72".equals(outRec1.CPTL_MRKT_FUND_TYPE_CODE)) {
			outRec1.CPTL_MRKT_FUND_TYPE_NM = "부동산파생";
		}else if("81".equals(outRec1.CPTL_MRKT_FUND_TYPE_CODE)) {
			outRec1.CPTL_MRKT_FUND_TYPE_NM = "특별자산";
		}else if("82".equals(outRec1.CPTL_MRKT_FUND_TYPE_CODE)) {
			outRec1.CPTL_MRKT_FUND_TYPE_NM = "특별자산파생";		
		}else if("91".equals(outRec1.CPTL_MRKT_FUND_TYPE_CODE)) {
			outRec1.CPTL_MRKT_FUND_TYPE_NM = "혼합자산";
		}else if("92".equals(outRec1.CPTL_MRKT_FUND_TYPE_CODE)) {
			outRec1.CPTL_MRKT_FUND_TYPE_NM = "혼합자산파생";
		}else if("EE".equals(outRec1.CPTL_MRKT_FUND_TYPE_CODE)) {
			outRec1.CPTL_MRKT_FUND_TYPE_NM = "단기금융(MMF)";					
		}else {
			outRec1.CPTL_MRKT_FUND_TYPE_NM = "";
		}
		
		if("00".equals(outRec1.UNI_SVNS_PRDT_CODE)) {
			outRec1.UNI_SVNS_PRDT_NM = "일반";
		}else if("10".equals(outRec1.UNI_SVNS_PRDT_CODE)) {
			outRec1.UNI_SVNS_PRDT_NM = "세금우대종합";
		}else if("11".equals(outRec1.UNI_SVNS_PRDT_CODE)) {
			outRec1.UNI_SVNS_PRDT_NM = "소액가계저축";
		}else if("16".equals(outRec1.UNI_SVNS_PRDT_CODE)) {
			outRec1.UNI_SVNS_PRDT_NM = "하이일드";		
		}else if("22".equals(outRec1.UNI_SVNS_PRDT_CODE)) {
			outRec1.UNI_SVNS_PRDT_NM = "근로자장기증권저축";
		}else if("23".equals(outRec1.UNI_SVNS_PRDT_CODE)) {
			outRec1.UNI_SVNS_PRDT_NM = "근로자증권저축";
		}else if("32".equals(outRec1.UNI_SVNS_PRDT_CODE)) {
			outRec1.UNI_SVNS_PRDT_NM = "장기주택마련저축";
		}else if("33".equals(outRec1.UNI_SVNS_PRDT_CODE)) {
			outRec1.UNI_SVNS_PRDT_NM = "근로자우대";
		}else if("34".equals(outRec1.UNI_SVNS_PRDT_CODE)) {
			outRec1.UNI_SVNS_PRDT_NM = "가계장기";
		}else if("35".equals(outRec1.UNI_SVNS_PRDT_CODE)) {
			outRec1.UNI_SVNS_PRDT_NM = "구개인연금";
		}else if("36".equals(outRec1.UNI_SVNS_PRDT_CODE)) {
			outRec1.UNI_SVNS_PRDT_NM = "신개인연금";
		}else if("37".equals(outRec1.UNI_SVNS_PRDT_CODE)) {
			outRec1.UNI_SVNS_PRDT_NM = "비과세";
		}else if("41".equals(outRec1.UNI_SVNS_PRDT_CODE)) {
			outRec1.UNI_SVNS_PRDT_NM = "비과세종합저축";
		}else if("42".equals(outRec1.UNI_SVNS_PRDT_CODE)) {
			outRec1.UNI_SVNS_PRDT_NM = "근로자주식";
		}else if("43".equals(outRec1.UNI_SVNS_PRDT_CODE)) {
			outRec1.UNI_SVNS_PRDT_NM = "비과세고수익";
		}else if("44".equals(outRec1.UNI_SVNS_PRDT_CODE)) {
			outRec1.UNI_SVNS_PRDT_NM = "장기증권";
		}else if("46".equals(outRec1.UNI_SVNS_PRDT_CODE)) {
			outRec1.UNI_SVNS_PRDT_NM = "비과세장기주식형";
		}else if("47".equals(outRec1.UNI_SVNS_PRDT_CODE)) {
			outRec1.UNI_SVNS_PRDT_NM = "장기주식형";
		}else if("48".equals(outRec1.UNI_SVNS_PRDT_CODE)) {
			outRec1.UNI_SVNS_PRDT_NM = "장기회사채형";			
		}else if("52".equals(outRec1.UNI_SVNS_PRDT_CODE)) {
			outRec1.UNI_SVNS_PRDT_NM = "퇴직연금(DC)";		
		}else if("53".equals(outRec1.UNI_SVNS_PRDT_CODE)) {
			outRec1.UNI_SVNS_PRDT_NM = "퇴직연금(기업형IRP)";					
		}else {
			outRec1.UNI_SVNS_PRDT_NM = "";
		}		
		
		if("1".equals(outRec1.A_FRGN_SECT_CODE)) {
			outRec1.A_FRGN_SECT_NM = "국내";
		}else if("2".equals(outRec1.A_FRGN_SECT_CODE)) {
			outRec1.A_FRGN_SECT_NM = "해외";
		}else {
			outRec1.A_FRGN_SECT_NM = "";
		}		
		
		if("1".equals(outRec1.STLT_PRID_TYPE_CODE)) {
			outRec1.STLT_PRID_TYPE_NM = "순연";
		}else if("2".equals(outRec1.STLT_PRID_TYPE_CODE)) {
			outRec1.STLT_PRID_TYPE_NM = "기준가+1";
		}else {
			outRec1.STLT_PRID_TYPE_NM = "";
		}				
		
		if("1".equals(outRec1.BSNS_DAY_SECT_CODE)) {
			outRec1.BSNS_DAY_SECT_NM = "당사";
		}else if("2".equals(outRec1.BSNS_DAY_SECT_CODE)) {
			outRec1.BSNS_DAY_SECT_NM = "거래소";
		}else if("3".equals(outRec1.BSNS_DAY_SECT_CODE)) {
			outRec1.BSNS_DAY_SECT_NM = "해외";			
		}else {
			outRec1.BSNS_DAY_SECT_NM = "";
		}					
	}
	/* validate */
	public boolean validate() throws Exception {
		//implement here
		return true;
	}

	/* toMap */
	public Map<String, Object> toMap() {
		Map<String, Object> m = new HashMap<String, Object>();
		m.putAll(outRec1.toMap());
		m.putAll(outRec2.toMap());
		//m.put("outRec1",outRec1.toMap());
		//m.put("outRec2",outRec2.toMap());
		return m;
	}
}
