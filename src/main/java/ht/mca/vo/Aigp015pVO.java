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
/**
 * 채권 기본 정보
 * @author TD5181
 *
 */
public class Aigp015pVO extends VOSupport{

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
		
		/* Attributes */
		private String ISCD;			// 종목코드	
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
		String PRDT_NAME;					//상품명
		String ISUS_ABBR_NAME;				//종목축약명
		String ISUS_ENNM;    				//종목영문명
		String ISUS_ABBR_ENNM;				//종목축약영문명
		String BOND_KIND_CODE;				//채권종류코드
		String KIND_ABNM;					//종류축약명
		String ISSD_DATE;					//발행일자
		String XPRN_DATE;					//만기일자
		String SALS_DATE;					//매출일자
		String CPRT;						//표면이자율
		String INTT_PYMT_TYPE_CODE;			//이자지급유형코드
		String INTT_PYMT_TYPE_NM;			//이자지급유형이름
		String INTT_PYMT_UNIT_MNTH_CONT;	//이자지급단위월수
		String INCC_UNIT_TYPE_CODE;			//이자계산단위유형코드
		String INCC_UNIT_TYPE_NM;			//이자계산단위유형이름
		String PRPT_POST_PYMT_TYPE_CODE;	//선급후급유형코드
		String PRPT_POST_PYMT_TYPE_NM;		//선급후급유형이름
		String PRNL_RDMN_TYPE_CODE;			//원금상환유형코드
		String PRNL_RDMN_TYPE_NM;			//원금상환유형이름
		String FRST_INTT_PYMT_DATE;			//첫번째이자지급일자
		String PRV_INPD_DATE;				//직전이자지급일자
		String NEXT_TERM_INPD_DATE;			//차기이자지급일자
		String FACE_VALU;					//액면가
		String PRNL_INTT_PRCE_TYPE_CODE;	//원리금처리유형코드
		String PRNL_INTT_PRCE_TYPE_NM;		//원리금처리유형이름
		String INTT_LRD_TYPE_CODE;			//이자기산일유형코드
		String INTT_LRD_TYPE_NM;			//이자기산일유형이름
		String DCRT;						//할인율
		String XPRN_RDMN_INTT_RATE;			//만기상환이자율
		String LAST_INPD_DATE_TYPE_CODE;	//최종이자지급일자유형코드
		String LAST_INPD_DATE_TYPE_NM;	    //최종이자지급일자유형이름
		String PRLS_INTT_PYMT_TYPE_CODE;	//선매출이자지급유형코드
		String PRLS_INTT_PYMT_TYPE_NM;	   	//선매출이자지급유형이름
		String BOND_ISUS_SELL_AMNT;			//채권종목매도금액
		String BOND_ISUS_RDMN_PRIC_AMNT;	//채권종목상환가격금액
		String STRY_PYMT_TYPE_CODE;			//토요일지급유형코드
		String STRY_PYMT_TYPE_NM;			//토요일지급유형이름
		String DEFD_PERD;					//원금거치기간
		String INRD_CNT;					//분할상환횟수
		String BOND_LSTG_TYPE_CODE;			//채권상장유형코드
		String BOND_LSTG_TYPE_NM;			//채권상장유형이름
		String PBLC_OR_PRVT_PRTN_TYPE_CODE;	//공사모유형코드
		String JNR_TYPE_CODE;				//후순위유형코드
		String JNR_TYPE_NM;				//후순위유형이름
		String LSTG_DATE;					//상장일자
		String DLSG_DATE;					//상장폐지일자
		String GRNY_INSN_CMPY_CODE;			//보증기관회사코드
		String MNCM_CMPY_CODE;				//주간사회사코드
		String BAS_CRNY_CODE;				//기초통화코드
		String BAS_CRNY_NM;				//기초통화이름
		String KRC_CRDT_RTNG_CODE;			//한기평신용등급코드
		String NICE_CRDT_RTNG_CODE;			//한신정신용등급코드
		String KIS_CRDT_RTNG_CODE;			//한신평신용등급코드
		String SCI_CRDT_RTNG_CODE;			//서신평신용등급코드
		String DTCT_TYPE_CODE;				//일자계산유형코드
		String DTCT_TYPE_NM;				//일자계산유형이름
		String ODD_SCTN_CODE;				//비정형구간코드
		String ODD_SCTN_NM;				//비정형구간이름
		String STCK_RLTD_BOND_TYPE_CODE;	//주식관련채권유형코드
		String STCK_RLTD_BOND_TYPE_NM;		//주식관련채권유형이름
		String KSD_OPTS_KIND_CODE;			//예탁원옵션종류코드
		String KSD_OPTS_KIND_NM;			//예탁원옵션종류이름
		String FRN_YN;						//FRN여부
		String INFN_INDD_TRSY_BOND_YN;		//물가연동국고채여부
		String INFN_INDD_TRSY_BOND_YN_NM;		//물가연동국고채여부이름
		String ASST_CURT_TYPE_CODE;			//자산유동화유형코드
		String ASST_CURT_TYPE_NM;			//자산유동화유형이름
		String STRP_TYPE_CODE;				//스트립유형코드
		String KRW_SBPC;					//원화대용가
		String POST_CTRT_INPD_DATE;			//약정후이자지급일자
		String POST_CTRT_INTT_RATE;			//약정후이자율
		String POST_CTRT_INTT_APLY_YN;		//약정후이자적용여부
		String GNRL_INVR_DLNG_ALWD_YN;		//일반투자자매매가능여부
		String LEVL_CLSN_CODE;				//레벨분류코드
		String XCHG_BOND_DLNG_KIND_CODE;	//거래소채권매매종류코드
		String RTBN_KIND_CODE;				//소매채권종류코드
		String OWCM_ACPT_BOND_CNTL_YN;		//당사인수채권통제여부
		String TRDG_STOP_YN;				//거래정지여부
		String TRDG_STOP_YN_NM;				//거래정지여부이름
		String UNIT_PRIC_PRCE_SECT_CODE;	//단가처리구분코드
		String INRD_BOND_RDMN_PERD_STRT_DATE;	//분할상환채권상환기간시작일자
		String GRNY_INSN_NAME;				//보증기관명
		String RPRS_MNCM_NAME;				//대표주간사명
		String DPSD_ALWD_YN;				//예탁가능여부
		String DPSD_ALWD_YN_NM;				//예탁가능여부이름
		String TTDD_DLNG_ALWD_YN;			//당일매매가능여부
		String TTDD_DLNG_ALWD_YN_NM;			//당일매매가능여부이름
		String SMBN_ALWD_YN;				//소액채권가능여부
		String SMBN_ALWD_YN_NM;				//소액채권가능여부이름
		String BOND_ISSD_INSN_CODE;			//채권발행기관코드
		String CPN_CLCL_STNR_CODE;			//이표계산기준코드
		String GRNY_SECT_CODE;				//보증구분코드
		String GRNY_SECT_NM;				//보증구분이름
		String NWKN_CPTL_SCRS_YN;			//신종자본증권여부
		String CNDN_CPTL_SCRS_TYPE_CODE;	//조건부자본증권유형코드
		String CNDN_CPTL_SCRS_TYPE_NM;		//조건부자본증권유형이름
		String CPTL_SCRS_CNVN_STNR_DATE;	//자본증권전환기준일자
		String PRNL_TXTN_YN;				//원금과세여부
		String CRFN_YN;						//크라우드펀딩여부
		String BOND_PRIC_UNIT_RL_CODE;		//채권가격단위규칙코드
		String BOND_PRIC_UNIT_RL_NM;		//채권가격단위규칙이름
		String SPPI_TEST_YN;				//SPPI테스트여부
		String PRIV_BOND_VLTN_YN;			//사모채권평가여부
		String UNUS_BOND_TYPE_CODE;			//특이채권유형코드
		String INRD_DATE;					//분할상환일자
		String INTT_STNR_CLCL_CODE;			//이자기준계산코드
		String ELCT_SCRS_YN;				//전자증권여부
		String QRTR_TRDG_RSTT_DATE;			//쿼터거래제한일자
		String MNTN_YN;						//모니터링여부
		String PRDT_RTNG_TYPE_CODE;			//상품등급유형코드
		String PRDT_RTNG_TYPE_NM;			//상품등급유형이름
		String CNSL_SCRT_CODE;				//상담스크립트코드
		String PRDT_ID;						//상품ID
		String BOND_ISSD_TAMT;				//채권발행총액
		String KSD_ISSD_RQTY_AMNT;			//예탁원발행잔량금액
		String LSTG_AMNT;					//상장금액
		
		// Constructor
		public OutRec1() {
		}


		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("PRDT_NAME", PRDT_NAME);
			m.put("ISUS_ABBR_NAME", ISUS_ABBR_NAME);
			m.put("ISUS_ENNM", ISUS_ENNM);
			m.put("ISUS_ABBR_ENNM", ISUS_ABBR_ENNM);
			m.put("BOND_KIND_CODE", BOND_KIND_CODE);
			m.put("KIND_ABNM", KIND_ABNM);
			m.put("ISSD_DATE", ISSD_DATE);
			m.put("XPRN_DATE", XPRN_DATE);
			m.put("SALS_DATE", SALS_DATE);
			m.put("CPRT", CPRT);
			m.put("INTT_PYMT_TYPE_CODE", INTT_PYMT_TYPE_CODE);
			m.put("INTT_PYMT_TYPE_NM", INTT_PYMT_TYPE_NM);
			m.put("INTT_PYMT_UNIT_MNTH_CONT", INTT_PYMT_UNIT_MNTH_CONT);
			m.put("INCC_UNIT_TYPE_CODE", INCC_UNIT_TYPE_CODE);
			m.put("INCC_UNIT_TYPE_NM", INCC_UNIT_TYPE_NM);
			m.put("PRPT_POST_PYMT_TYPE_CODE", PRPT_POST_PYMT_TYPE_CODE);
			m.put("PRPT_POST_PYMT_TYPE_NM", PRPT_POST_PYMT_TYPE_NM);
			m.put("PRNL_RDMN_TYPE_CODE", PRNL_RDMN_TYPE_CODE);
			m.put("PRNL_RDMN_TYPE_NM", PRNL_RDMN_TYPE_NM);
			m.put("FRST_INTT_PYMT_DATE", FRST_INTT_PYMT_DATE);
			m.put("PRV_INPD_DATE", PRV_INPD_DATE);
			m.put("NEXT_TERM_INPD_DATE", NEXT_TERM_INPD_DATE);
			m.put("FACE_VALU", FACE_VALU);
			m.put("PRNL_INTT_PRCE_TYPE_CODE", PRNL_INTT_PRCE_TYPE_CODE);
			m.put("PRNL_INTT_PRCE_TYPE_NM", PRNL_INTT_PRCE_TYPE_NM);
			m.put("INTT_LRD_TYPE_CODE", INTT_LRD_TYPE_CODE);
			m.put("INTT_LRD_TYPE_NM", INTT_LRD_TYPE_NM);
			m.put("DCRT", DCRT);
			m.put("XPRN_RDMN_INTT_RATE", XPRN_RDMN_INTT_RATE);
			m.put("LAST_INPD_DATE_TYPE_CODE", LAST_INPD_DATE_TYPE_CODE);
			m.put("LAST_INPD_DATE_TYPE_NM", LAST_INPD_DATE_TYPE_NM);
			m.put("PRLS_INTT_PYMT_TYPE_CODE", PRLS_INTT_PYMT_TYPE_CODE);
			m.put("PRLS_INTT_PYMT_TYPE_NM", PRLS_INTT_PYMT_TYPE_NM);
			m.put("BOND_ISUS_SELL_AMNT", BOND_ISUS_SELL_AMNT);
			m.put("BOND_ISUS_RDMN_PRIC_AMNT", BOND_ISUS_RDMN_PRIC_AMNT);
			m.put("STRY_PYMT_TYPE_CODE", STRY_PYMT_TYPE_CODE);
			m.put("STRY_PYMT_TYPE_NM", STRY_PYMT_TYPE_NM);
			m.put("DEFD_PERD", DEFD_PERD);
			m.put("INRD_CNT", INRD_CNT);
			m.put("BOND_LSTG_TYPE_CODE", BOND_LSTG_TYPE_CODE);
			m.put("BOND_LSTG_TYPE_NM", BOND_LSTG_TYPE_NM);
			m.put("PBLC_OR_PRVT_PRTN_TYPE_CODE", PBLC_OR_PRVT_PRTN_TYPE_CODE);
			m.put("JNR_TYPE_CODE", JNR_TYPE_CODE);
			m.put("JNR_TYPE_NM", JNR_TYPE_NM);
			m.put("LSTG_DATE", LSTG_DATE);
			m.put("DLSG_DATE", DLSG_DATE);
			m.put("GRNY_INSN_CMPY_CODE", GRNY_INSN_CMPY_CODE);
			m.put("MNCM_CMPY_CODE", MNCM_CMPY_CODE);
			m.put("BAS_CRNY_CODE", BAS_CRNY_CODE);
			m.put("BAS_CRNY_NM", BAS_CRNY_NM);
			m.put("KRC_CRDT_RTNG_CODE", KRC_CRDT_RTNG_CODE);
			m.put("NICE_CRDT_RTNG_CODE", NICE_CRDT_RTNG_CODE);
			m.put("KIS_CRDT_RTNG_CODE", KIS_CRDT_RTNG_CODE);
			m.put("SCI_CRDT_RTNG_CODE", SCI_CRDT_RTNG_CODE);
			m.put("DTCT_TYPE_CODE", DTCT_TYPE_CODE);
			m.put("DTCT_TYPE_NM", DTCT_TYPE_NM);
			m.put("ODD_SCTN_CODE", ODD_SCTN_CODE);
			m.put("ODD_SCTN_NM", ODD_SCTN_NM);
			m.put("STCK_RLTD_BOND_TYPE_CODE", STCK_RLTD_BOND_TYPE_CODE);
			m.put("STCK_RLTD_BOND_TYPE_NM", STCK_RLTD_BOND_TYPE_NM);
			m.put("KSD_OPTS_KIND_CODE", KSD_OPTS_KIND_CODE);
			m.put("KSD_OPTS_KIND_NM", KSD_OPTS_KIND_NM);
			m.put("FRN_YN", FRN_YN);
			m.put("INFN_INDD_TRSY_BOND_YN", INFN_INDD_TRSY_BOND_YN);
			m.put("INFN_INDD_TRSY_BOND_YN_NM", INFN_INDD_TRSY_BOND_YN_NM);
			m.put("ASST_CURT_TYPE_CODE", ASST_CURT_TYPE_CODE);
			m.put("ASST_CURT_TYPE_NM", ASST_CURT_TYPE_NM);
			m.put("STRP_TYPE_CODE", STRP_TYPE_CODE);
			m.put("KRW_SBPC", KRW_SBPC);
			m.put("POST_CTRT_INPD_DATE", POST_CTRT_INPD_DATE);
			m.put("POST_CTRT_INTT_RATE", POST_CTRT_INTT_RATE);
			m.put("POST_CTRT_INTT_APLY_YN", POST_CTRT_INTT_APLY_YN);
			m.put("GNRL_INVR_DLNG_ALWD_YN", GNRL_INVR_DLNG_ALWD_YN);
			m.put("LEVL_CLSN_CODE", LEVL_CLSN_CODE);
			m.put("XCHG_BOND_DLNG_KIND_CODE", XCHG_BOND_DLNG_KIND_CODE);
			m.put("RTBN_KIND_CODE", RTBN_KIND_CODE);
			m.put("OWCM_ACPT_BOND_CNTL_YN", OWCM_ACPT_BOND_CNTL_YN);
			m.put("TRDG_STOP_YN", TRDG_STOP_YN);
			m.put("TRDG_STOP_YN_NM", TRDG_STOP_YN_NM);
			m.put("UNIT_PRIC_PRCE_SECT_CODE", UNIT_PRIC_PRCE_SECT_CODE);
			m.put("INRD_BOND_RDMN_PERD_STRT_DATE", INRD_BOND_RDMN_PERD_STRT_DATE);
			m.put("GRNY_INSN_NAME", GRNY_INSN_NAME);
			m.put("RPRS_MNCM_NAME", RPRS_MNCM_NAME);
			m.put("DPSD_ALWD_YN", DPSD_ALWD_YN);
			m.put("DPSD_ALWD_YN_NM", DPSD_ALWD_YN_NM);
			m.put("TTDD_DLNG_ALWD_YN", TTDD_DLNG_ALWD_YN);
			m.put("TTDD_DLNG_ALWD_YN_NM", TTDD_DLNG_ALWD_YN_NM);
			m.put("SMBN_ALWD_YN", SMBN_ALWD_YN);
			m.put("SMBN_ALWD_YN_NM", SMBN_ALWD_YN_NM);
			m.put("BOND_ISSD_INSN_CODE", BOND_ISSD_INSN_CODE);
			m.put("CPN_CLCL_STNR_CODE", CPN_CLCL_STNR_CODE);
			m.put("GRNY_SECT_CODE", GRNY_SECT_CODE);
			m.put("GRNY_SECT_NM", GRNY_SECT_NM);
			m.put("NWKN_CPTL_SCRS_YN", NWKN_CPTL_SCRS_YN);
			m.put("CNDN_CPTL_SCRS_TYPE_CODE", CNDN_CPTL_SCRS_TYPE_CODE);
			m.put("CNDN_CPTL_SCRS_TYPE_NM", CNDN_CPTL_SCRS_TYPE_NM);
			m.put("CPTL_SCRS_CNVN_STNR_DATE", CPTL_SCRS_CNVN_STNR_DATE);
			m.put("PRNL_TXTN_YN", PRNL_TXTN_YN);
			m.put("CRFN_YN", CRFN_YN);
			m.put("BOND_PRIC_UNIT_RL_CODE", BOND_PRIC_UNIT_RL_CODE);
			m.put("BOND_PRIC_UNIT_RL_NM", BOND_PRIC_UNIT_RL_NM);
			m.put("SPPI_TEST_YN", SPPI_TEST_YN);
			m.put("PRIV_BOND_VLTN_YN", PRIV_BOND_VLTN_YN);
			m.put("UNUS_BOND_TYPE_CODE", UNUS_BOND_TYPE_CODE);
			m.put("INRD_DATE", INRD_DATE);
			m.put("INTT_STNR_CLCL_CODE", INTT_STNR_CLCL_CODE);
			m.put("ELCT_SCRS_YN", ELCT_SCRS_YN);
			m.put("QRTR_TRDG_RSTT_DATE", QRTR_TRDG_RSTT_DATE);
			m.put("MNTN_YN", MNTN_YN);
			m.put("PRDT_RTNG_TYPE_CODE", PRDT_RTNG_TYPE_CODE);
			m.put("PRDT_RTNG_TYPE_NM", PRDT_RTNG_TYPE_NM);
			m.put("CNSL_SCRT_CODE", CNSL_SCRT_CODE);
			m.put("PRDT_ID", PRDT_ID);
			m.put("BOND_ISSD_TAMT", BOND_ISSD_TAMT);
			m.put("KSD_ISSD_RQTY_AMNT", KSD_ISSD_RQTY_AMNT);
			m.put("LSTG_AMNT", LSTG_AMNT);

			return m;
		}
	}
	
	/* constructor */
	public Aigp015pVO (String ipAddr, String userID, String mediaType) {
		setHeaderUserID(userID);
		setHeaderTrCode("aigp015p");
		setHeaderDestination((char)'6');
		setHeaderPacketDataType((char)0);
		setHeaderMediaType(mediaType);
		setHeaderIpAddr(ipAddr);
		setCharSet("1");
        setIsActive("0");		
	}

	/* constructor */
	public Aigp015pVO (String ipAddr, String userID, String mediaType, String brchCD, String deptCD, String acctCD) {
		setHeaderUserID(userID);
		setHeaderTrCode("aigp015p");
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
				MCAUtil.writeString(inRec1.ISCD, 12, bos);
			}			
		}
		
		return bos.toByteArray();
	}
		
	/* parse */
	public void parse(StringInputStream sis) throws Exception {
		
		// OutRec1
		outRec1.PRDT_NAME = StringUtil.changBlankToDash(sis.readString(90));
		outRec1.ISUS_ABBR_NAME = StringUtil.changBlankToDash(sis.readString(60));
		outRec1.ISUS_ENNM = StringUtil.changBlankToDash(sis.readString(150));
		outRec1.ISUS_ABBR_ENNM = StringUtil.changBlankToDash(sis.readString(60));
		outRec1.BOND_KIND_CODE = StringUtil.changBlankToDash(sis.readString(5));
		outRec1.KIND_ABNM = StringUtil.changBlankToDash(sis.readString(60));
		outRec1.ISSD_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.XPRN_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.SALS_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.CPRT = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(29)));
		outRec1.INTT_PYMT_TYPE_CODE = StringUtil.changBlankToDash(sis.readString(4));
		outRec1.INTT_PYMT_UNIT_MNTH_CONT = StringUtil.changBlankToDash(sis.readString(11));
		outRec1.INCC_UNIT_TYPE_CODE = StringUtil.changBlankToDash(sis.readString(4));
		outRec1.PRPT_POST_PYMT_TYPE_CODE = StringUtil.changBlankToDash(sis.readString(4));
		outRec1.PRNL_RDMN_TYPE_CODE = StringUtil.changBlankToDash(sis.readString(4));
		outRec1.FRST_INTT_PYMT_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.PRV_INPD_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.NEXT_TERM_INPD_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.FACE_VALU = StringUtil.changBlankToDash(sis.readString(14));
		outRec1.PRNL_INTT_PRCE_TYPE_CODE = StringUtil.changBlankToDash(sis.readString(4));
		outRec1.INTT_LRD_TYPE_CODE = StringUtil.changBlankToDash(sis.readString(4));
		outRec1.DCRT = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(11)));
		outRec1.XPRN_RDMN_INTT_RATE = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(23)));
		outRec1.LAST_INPD_DATE_TYPE_CODE = StringUtil.changBlankToDash(sis.readString(4));
		outRec1.PRLS_INTT_PYMT_TYPE_CODE = StringUtil.changBlankToDash(sis.readString(4));
		outRec1.BOND_ISUS_SELL_AMNT = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(15)));
		outRec1.BOND_ISUS_RDMN_PRIC_AMNT = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(15)));
		outRec1.STRY_PYMT_TYPE_CODE = StringUtil.changBlankToDash(sis.readString(4));
		outRec1.DEFD_PERD = StringUtil.changBlankToDash(sis.readString(6));
		outRec1.INRD_CNT = StringUtil.changBlankToDash(sis.readString(6));
		outRec1.BOND_LSTG_TYPE_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.PBLC_OR_PRVT_PRTN_TYPE_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.JNR_TYPE_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.LSTG_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.DLSG_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.GRNY_INSN_CMPY_CODE = StringUtil.changBlankToDash(sis.readString(5));
		outRec1.MNCM_CMPY_CODE = StringUtil.changBlankToDash(sis.readString(5));
		outRec1.BAS_CRNY_CODE = StringUtil.changBlankToDash(sis.readString(3));
		outRec1.KRC_CRDT_RTNG_CODE = StringUtil.changBlankToDash(sis.readString(4));
		outRec1.NICE_CRDT_RTNG_CODE = StringUtil.changBlankToDash(sis.readString(4));
		outRec1.KIS_CRDT_RTNG_CODE = StringUtil.changBlankToDash(sis.readString(4));
		outRec1.SCI_CRDT_RTNG_CODE = StringUtil.changBlankToDash(sis.readString(4));
		outRec1.DTCT_TYPE_CODE = StringUtil.changBlankToDash(sis.readString(4));
		outRec1.ODD_SCTN_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.STCK_RLTD_BOND_TYPE_CODE = StringUtil.changBlankToDash(sis.readString(2));
		outRec1.KSD_OPTS_KIND_CODE = StringUtil.changBlankToDash(sis.readString(4));
		outRec1.FRN_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.INFN_INDD_TRSY_BOND_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.ASST_CURT_TYPE_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.STRP_TYPE_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.KRW_SBPC = StringUtil.changBlankToDash(sis.readString(14));
		outRec1.POST_CTRT_INPD_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.POST_CTRT_INTT_RATE = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(23)));
		outRec1.POST_CTRT_INTT_APLY_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.GNRL_INVR_DLNG_ALWD_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.LEVL_CLSN_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.XCHG_BOND_DLNG_KIND_CODE = StringUtil.changBlankToDash(sis.readString(3));
		outRec1.RTBN_KIND_CODE = StringUtil.changBlankToDash(sis.readString(2));
		outRec1.OWCM_ACPT_BOND_CNTL_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.TRDG_STOP_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.UNIT_PRIC_PRCE_SECT_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.INRD_BOND_RDMN_PERD_STRT_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.GRNY_INSN_NAME = StringUtil.changBlankToDash(sis.readString(60));
		outRec1.RPRS_MNCM_NAME = StringUtil.changBlankToDash(sis.readString(60));
		outRec1.DPSD_ALWD_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.TTDD_DLNG_ALWD_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.SMBN_ALWD_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.BOND_ISSD_INSN_CODE = StringUtil.changBlankToDash(sis.readString(5));
		outRec1.CPN_CLCL_STNR_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.GRNY_SECT_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.NWKN_CPTL_SCRS_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.CNDN_CPTL_SCRS_TYPE_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.CPTL_SCRS_CNVN_STNR_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.PRNL_TXTN_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.CRFN_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.BOND_PRIC_UNIT_RL_CODE = StringUtil.changBlankToDash(sis.readString(10));
		outRec1.SPPI_TEST_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.PRIV_BOND_VLTN_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.UNUS_BOND_TYPE_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.INRD_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.INTT_STNR_CLCL_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.ELCT_SCRS_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.QRTR_TRDG_RSTT_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.MNTN_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.PRDT_RTNG_TYPE_CODE = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.CNSL_SCRT_CODE = StringUtil.changBlankToDash(sis.readString(3));
		outRec1.PRDT_ID = StringUtil.changBlankToDash(sis.readString(19));
		outRec1.BOND_ISSD_TAMT = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(19)));
		outRec1.KSD_ISSD_RQTY_AMNT = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(29)));
		outRec1.LSTG_AMNT = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(29)));
		
		if("GE".equals(outRec1.STCK_RLTD_BOND_TYPE_CODE)) {
			outRec1.STCK_RLTD_BOND_TYPE_NM = "일반";
		}else if("CB".equals(outRec1.STCK_RLTD_BOND_TYPE_CODE)) {
			outRec1.STCK_RLTD_BOND_TYPE_NM = "CB";
		}else if("EB".equals(outRec1.STCK_RLTD_BOND_TYPE_CODE)) {
			outRec1.STCK_RLTD_BOND_TYPE_NM = "EB";
		}else if("BW".equals(outRec1.STCK_RLTD_BOND_TYPE_CODE)) {
			outRec1.STCK_RLTD_BOND_TYPE_NM = "비분리BW";
		}else if("SB".equals(outRec1.STCK_RLTD_BOND_TYPE_CODE)) {
			outRec1.STCK_RLTD_BOND_TYPE_NM = "분리형BW";
		}else if("PB".equals(outRec1.STCK_RLTD_BOND_TYPE_CODE)) {
			outRec1.STCK_RLTD_BOND_TYPE_NM = "이익참가부사채";			
		}else {
			outRec1.STCK_RLTD_BOND_TYPE_NM = outRec1.STCK_RLTD_BOND_TYPE_CODE;
		}
		
		if("1".equals(outRec1.KSD_OPTS_KIND_CODE)) {
			outRec1.KSD_OPTS_KIND_NM = "Call";
		}else if("2".equals(outRec1.KSD_OPTS_KIND_CODE)) {
			outRec1.KSD_OPTS_KIND_NM = "Put";
		}else if("3".equals(outRec1.KSD_OPTS_KIND_CODE)) {
			outRec1.KSD_OPTS_KIND_NM = "Call/Put";		
		}else {
			outRec1.KSD_OPTS_KIND_CODE = outRec1.KSD_OPTS_KIND_CODE;
		}		
		
		if("N".equals(outRec1.INFN_INDD_TRSY_BOND_YN)) {
			outRec1.INFN_INDD_TRSY_BOND_YN_NM = "X";
		}else if("Y".equals(outRec1.INFN_INDD_TRSY_BOND_YN)) {
			outRec1.INFN_INDD_TRSY_BOND_YN_NM = "O";
		}else {
			outRec1.INFN_INDD_TRSY_BOND_YN_NM = outRec1.INFN_INDD_TRSY_BOND_YN;
		}				
		
		if("1".equals(outRec1.ASST_CURT_TYPE_CODE)) {
			outRec1.ASST_CURT_TYPE_NM = "ABS";
		}else if("2".equals(outRec1.ASST_CURT_TYPE_CODE)) {
			outRec1.ASST_CURT_TYPE_NM = "MBS";
		}else {
			outRec1.ASST_CURT_TYPE_NM = outRec1.ASST_CURT_TYPE_CODE;
		}				
		
		if("CMPX".equals(outRec1.INTT_PYMT_TYPE_CODE)) {
			outRec1.INTT_PYMT_TYPE_NM = "복할인";
		}else if("COMP".equals(outRec1.INTT_PYMT_TYPE_CODE)) {
			outRec1.INTT_PYMT_TYPE_NM = "복리";
		}else if("CPFL".equals(outRec1.INTT_PYMT_TYPE_CODE)) {
			outRec1.INTT_PYMT_TYPE_NM = "이표변동";			
		}else if("CPLK".equals(outRec1.INTT_PYMT_TYPE_CODE)) {
			outRec1.INTT_PYMT_TYPE_NM = "이표연동";
		}else if("CPON".equals(outRec1.INTT_PYMT_TYPE_CODE)) {
			outRec1.INTT_PYMT_TYPE_NM = "이표확정";			
		}else if("DFCM".equals(outRec1.INTT_PYMT_TYPE_CODE)) {
			outRec1.INTT_PYMT_TYPE_NM = "거치복리";
		}else if("DFCP".equals(outRec1.INTT_PYMT_TYPE_CODE)) {
			outRec1.INTT_PYMT_TYPE_NM = "거치이표";			
		}else if("DFSP".equals(outRec1.INTT_PYMT_TYPE_CODE)) {
			outRec1.INTT_PYMT_TYPE_NM = "거치단리";
		}else if("DISC".equals(outRec1.INTT_PYMT_TYPE_CODE)) {
			outRec1.INTT_PYMT_TYPE_NM = "할인";			
		}else if("FCTS".equals(outRec1.INTT_PYMT_TYPE_CODE)) {
			outRec1.INTT_PYMT_TYPE_NM = "5복2단";
		}else if("OTHR".equals(outRec1.INTT_PYMT_TYPE_CODE)) {
			outRec1.INTT_PYMT_TYPE_NM = "기타";			
		}else if("SIMP".equals(outRec1.INTT_PYMT_TYPE_CODE)) {
			outRec1.INTT_PYMT_TYPE_NM = "단리";		
		}else {
			outRec1.INTT_PYMT_TYPE_NM = outRec1.INTT_PYMT_TYPE_CODE;
		}
		
		
		if("1".equals(outRec1.BOND_LSTG_TYPE_CODE)) {
			outRec1.BOND_LSTG_TYPE_NM = "비상장";
		}else if("2".equals(outRec1.BOND_LSTG_TYPE_CODE)) {
			outRec1.BOND_LSTG_TYPE_NM = "상장";
		}else if("3".equals(outRec1.BOND_LSTG_TYPE_CODE)) {
			outRec1.BOND_LSTG_TYPE_NM = "보통거래정지";
		}else if("4".equals(outRec1.BOND_LSTG_TYPE_CODE)) {
			outRec1.BOND_LSTG_TYPE_NM = "코드변경폐지";
		}else if("9".equals(outRec1.BOND_LSTG_TYPE_CODE)) {
			outRec1.BOND_LSTG_TYPE_NM = "상장폐지";
		}else {
			outRec1.BOND_LSTG_TYPE_NM = outRec1.BOND_LSTG_TYPE_CODE;
		}						
		
		if("1".equals(outRec1.CNDN_CPTL_SCRS_TYPE_CODE)) {
			outRec1.CNDN_CPTL_SCRS_TYPE_NM = "1.전환형";
		}else if("2".equals(outRec1.CNDN_CPTL_SCRS_TYPE_CODE)) {
			outRec1.CNDN_CPTL_SCRS_TYPE_NM = "2.상각형";
		}else {
			outRec1.CNDN_CPTL_SCRS_TYPE_NM = outRec1.CNDN_CPTL_SCRS_TYPE_CODE;
		}		
		
		if("MNTH".equals(outRec1.INCC_UNIT_TYPE_CODE)) {
			outRec1.INCC_UNIT_TYPE_NM = "개월";
		}else if("YEAR".equals(outRec1.INCC_UNIT_TYPE_CODE)) {
			outRec1.INCC_UNIT_TYPE_NM = "년";
		}else {
			outRec1.INCC_UNIT_TYPE_NM = outRec1.INCC_UNIT_TYPE_CODE;
		}			
		
		if("0".equals(outRec1.JNR_TYPE_CODE)) {
			outRec1.JNR_TYPE_NM = "선순위";
		}else if("1".equals(outRec1.JNR_TYPE_CODE)) {
			outRec1.JNR_TYPE_NM = "후순위";
		}else {
			outRec1.JNR_TYPE_NM = outRec1.JNR_TYPE_CODE;
		}		
		
		if("PRPD".equals(outRec1.PRPT_POST_PYMT_TYPE_CODE)) {
			outRec1.PRPT_POST_PYMT_TYPE_NM = "선급";
		}else if("MTPY".equals(outRec1.PRPT_POST_PYMT_TYPE_CODE)) {
			outRec1.PRPT_POST_PYMT_TYPE_NM = "후급";
		}else {
			outRec1.PRPT_POST_PYMT_TYPE_NM = outRec1.PRPT_POST_PYMT_TYPE_CODE;
		}					
		
		if("MATY".equals(outRec1.PRNL_RDMN_TYPE_CODE)) {
			outRec1.PRNL_RDMN_TYPE_NM = "만기일시상환";
		}else if("EQAL".equals(outRec1.PRNL_RDMN_TYPE_CODE)) {
			outRec1.PRNL_RDMN_TYPE_NM = "균등분할상환";
		}else if("UNEQ".equals(outRec1.PRNL_RDMN_TYPE_CODE)) {
			outRec1.PRNL_RDMN_TYPE_NM = "불균등상환";
		}else if("OCAN".equals(outRec1.PRNL_RDMN_TYPE_CODE)) {
			outRec1.PRNL_RDMN_TYPE_NM = "수시상환";
		}else if("PIEQ".equals(outRec1.PRNL_RDMN_TYPE_CODE)) {
			outRec1.PRNL_RDMN_TYPE_NM = "원리금균등상환";
		}else if("PPTB".equals(outRec1.PRNL_RDMN_TYPE_CODE)) {
			outRec1.PRNL_RDMN_TYPE_NM = "영구채권";
		}else {
			outRec1.PRNL_RDMN_TYPE_NM = outRec1.PRNL_RDMN_TYPE_CODE;
		}
		
		if("Y".equals(outRec1.DPSD_ALWD_YN)) {
			outRec1.DPSD_ALWD_YN_NM = "가능";
		}else if("N".equals(outRec1.DPSD_ALWD_YN)) {
			outRec1.DPSD_ALWD_YN_NM = "불가능";
		}else {
			outRec1.DPSD_ALWD_YN_NM = outRec1.DPSD_ALWD_YN;
		}				
		
		if("1".equals(outRec1.GRNY_SECT_CODE)) {
			outRec1.GRNY_SECT_NM = "보증";
		}else if("2".equals(outRec1.GRNY_SECT_CODE)) {
			outRec1.GRNY_SECT_NM = "무보증";
		}else if("3".equals(outRec1.GRNY_SECT_CODE)) {
			outRec1.GRNY_SECT_NM = "담보부";		
		}else if("4".equals(outRec1.GRNY_SECT_CODE)) {
			outRec1.GRNY_SECT_NM = "일반";		
		}else {
			outRec1.GRNY_SECT_NM = outRec1.GRNY_SECT_CODE;
		}		

		if("Y".equals(outRec1.TTDD_DLNG_ALWD_YN)) {
			outRec1.TTDD_DLNG_ALWD_YN_NM = "가능";
		}else if("N".equals(outRec1.TTDD_DLNG_ALWD_YN)) {
			outRec1.TTDD_DLNG_ALWD_YN_NM = "불가능";
		}else {
			outRec1.TTDD_DLNG_ALWD_YN_NM = outRec1.TTDD_DLNG_ALWD_YN;
		}				
				
		if("Y".equals(outRec1.SMBN_ALWD_YN)) {
			outRec1.SMBN_ALWD_YN_NM = "가능";
		}else if("N".equals(outRec1.SMBN_ALWD_YN)) {
			outRec1.SMBN_ALWD_YN_NM = "불가능";
		}else {
			outRec1.SMBN_ALWD_YN_NM = outRec1.SMBN_ALWD_YN;
		}	
		
		if("KRW".equals(outRec1.BAS_CRNY_CODE)) {
			outRec1.BAS_CRNY_NM = "원화";
		}else if("USD".equals(outRec1.BAS_CRNY_CODE)) {
			outRec1.BAS_CRNY_NM = "USD";
		}else if("JPY".equals(outRec1.BAS_CRNY_CODE)) {
			outRec1.BAS_CRNY_NM = "엔화";
		}else if("EUR".equals(outRec1.BAS_CRNY_CODE)) {
			outRec1.BAS_CRNY_NM = "EURO";
		}else if("CNY".equals(outRec1.BAS_CRNY_CODE)) {
			outRec1.BAS_CRNY_NM = "위안화";
		}else if("GBP".equals(outRec1.BAS_CRNY_CODE)) {
			outRec1.BAS_CRNY_NM = "영국파운드";
		}else if("HKD".equals(outRec1.BAS_CRNY_CODE)) {
			outRec1.BAS_CRNY_NM = "홍콩달러";
		}else if("AUD".equals(outRec1.BAS_CRNY_CODE)) {
			outRec1.BAS_CRNY_NM = "호주달러";
		}else if("SGD".equals(outRec1.BAS_CRNY_CODE)) {
			outRec1.BAS_CRNY_NM = "싱가폴달러";
		}else {
			outRec1.BAS_CRNY_NM = outRec1.BAS_CRNY_CODE;
		}
		
		if("Y".equals(outRec1.TRDG_STOP_YN)) {
			outRec1.TRDG_STOP_YN_NM = "거래정지";
		}else if("N".equals(outRec1.TRDG_STOP_YN)) {
			outRec1.TRDG_STOP_YN_NM = "정상";
		}else {
			outRec1.TRDG_STOP_YN_NM = outRec1.TRDG_STOP_YN;
		}	
				
		if("FLOR".equals(outRec1.PRNL_INTT_PRCE_TYPE_CODE)) {
			outRec1.PRNL_INTT_PRCE_TYPE_NM = "절사";
		}else if("CEIL".equals(outRec1.PRNL_INTT_PRCE_TYPE_CODE)) {
			outRec1.PRNL_INTT_PRCE_TYPE_NM = "정상";
		}else if("ROND".equals(outRec1.PRNL_INTT_PRCE_TYPE_CODE)) {
			outRec1.PRNL_INTT_PRCE_TYPE_NM = "사사오입";
		}else if("REAL".equals(outRec1.PRNL_INTT_PRCE_TYPE_CODE)) {
			outRec1.PRNL_INTT_PRCE_TYPE_NM = "원미만인정";
		}else {
			outRec1.PRNL_INTT_PRCE_TYPE_NM = outRec1.PRNL_INTT_PRCE_TYPE_CODE;
		}	
		
		if("ISDT".equals(outRec1.INTT_LRD_TYPE_CODE)) {
			outRec1.INTT_LRD_TYPE_NM = "발행일기준";
		}else if("RDDT".equals(outRec1.INTT_LRD_TYPE_CODE)) {
			outRec1.INTT_LRD_TYPE_NM = "상환일기준";
		}else {
			outRec1.INTT_LRD_TYPE_NM = outRec1.INTT_LRD_TYPE_CODE;
		}			
		
		if("AACT".equals(outRec1.DTCT_TYPE_CODE)) {
			outRec1.DTCT_TYPE_NM = "Actual/Actual";
		}else if("A365".equals(outRec1.DTCT_TYPE_CODE)) {
			outRec1.DTCT_TYPE_NM = "Actual/365";
		}else if("U360".equals(outRec1.DTCT_TYPE_CODE)) {
			outRec1.DTCT_TYPE_NM = "30/360";
		}else {
			outRec1.DTCT_TYPE_NM = outRec1.DTCT_TYPE_CODE;
		}			
		
		if("1".equals(outRec1.PRDT_RTNG_TYPE_CODE)) {
			outRec1.PRDT_RTNG_TYPE_NM = "초저위험";
		}else if("2".equals(outRec1.PRDT_RTNG_TYPE_CODE)) {
			outRec1.PRDT_RTNG_TYPE_NM = "저위험";
		}else if("3".equals(outRec1.PRDT_RTNG_TYPE_CODE)) {
			outRec1.PRDT_RTNG_TYPE_NM = "중위험";
		}else if("4".equals(outRec1.PRDT_RTNG_TYPE_CODE)) {
			outRec1.PRDT_RTNG_TYPE_NM = "고위험";
		}else if("5".equals(outRec1.PRDT_RTNG_TYPE_CODE)) {
			outRec1.PRDT_RTNG_TYPE_NM = "초고위험";
		}else {
			outRec1.PRDT_RTNG_TYPE_NM = outRec1.PRDT_RTNG_TYPE_CODE;
		}			
		
		if("ISDT".equals(outRec1.LAST_INPD_DATE_TYPE_CODE)) {
			outRec1.LAST_INPD_DATE_TYPE_NM = "일자기준";
		}else if("MTDT".equals(outRec1.LAST_INPD_DATE_TYPE_CODE)) {
			outRec1.LAST_INPD_DATE_TYPE_NM = "말일기준";
		}else {
			outRec1.LAST_INPD_DATE_TYPE_NM = outRec1.LAST_INPD_DATE_TYPE_CODE;
		}				
		
		if("0".equals(outRec1.ODD_SCTN_CODE)) {
			outRec1.ODD_SCTN_NM = "NON";
		}else if("1".equals(outRec1.ODD_SCTN_CODE)) {
			outRec1.ODD_SCTN_NM = "이자지급주기";
		}else if("3".equals(outRec1.ODD_SCTN_CODE)) {
			outRec1.ODD_SCTN_NM = " Actual day";
		}else {
			outRec1.ODD_SCTN_NM = outRec1.ODD_SCTN_CODE;
		}
		
		if("RDDT".equals(outRec1.PRLS_INTT_PYMT_TYPE_CODE)) {
			outRec1.PRLS_INTT_PYMT_TYPE_NM = "만기시";
		}else if("FIDT".equals(outRec1.PRLS_INTT_PYMT_TYPE_CODE)) {
			outRec1.PRLS_INTT_PYMT_TYPE_NM = "첫이자락시";
		}else {
			outRec1.PRLS_INTT_PYMT_TYPE_NM = outRec1.PRLS_INTT_PYMT_TYPE_CODE;
		}		
		
		if("PRCD".equals(outRec1.STRY_PYMT_TYPE_CODE)) {
			outRec1.STRY_PYMT_TYPE_NM = "전영업일";
		}else if("FOLW".equals(outRec1.STRY_PYMT_TYPE_CODE)) {
			outRec1.STRY_PYMT_TYPE_NM = "익영업일";
		}else {
			outRec1.STRY_PYMT_TYPE_NM = outRec1.STRY_PYMT_TYPE_CODE;
		}	
		
		if("A1".equals(outRec1.BOND_PRIC_UNIT_RL_CODE)) {
			outRec1.BOND_PRIC_UNIT_RL_NM = "1원/Point";
		}else if("A0.5".equals(outRec1.BOND_PRIC_UNIT_RL_CODE)) {
			outRec1.BOND_PRIC_UNIT_RL_NM = "0.5원/Point";
		}else if("A0.1".equals(outRec1.BOND_PRIC_UNIT_RL_CODE)) {
			outRec1.BOND_PRIC_UNIT_RL_NM = "0.1원/Point";
		}else {
			outRec1.BOND_PRIC_UNIT_RL_NM = outRec1.BOND_PRIC_UNIT_RL_CODE;
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
		
		return m;
	}
}
