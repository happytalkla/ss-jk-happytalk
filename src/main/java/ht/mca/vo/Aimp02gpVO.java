package ht.mca.vo;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.ss.common.channel.mca.common.net.StringInputStream;
import com.ss.common.channel.mca.util.MCAUtil;
import com.ss.common.channel.mca.vo.VOSupport;
import com.ss.common.util.StringUtility;

import ht.mca.vo.Igv4206pVO.InRec1;
import ht.util.StringUtil;
import lombok.Data;
/**
 * ELS 상세정보
 * @author TD5181
 *
 */
public class Aimp02gpVO extends VOSupport{

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
		private String PRDT_TYPE_CODE;	// 상품유형코드	
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
		String A_OTC_DRVT_DL_CODE;			//예탁원종목코드
		String KSD_ISCD;					//예탁원종목코드
		String PRDT_NAME;					//상품명
		String STRD_SCRS_DSCN_IFMN_CTNT;	//구조화증권설명정보내용
		String OTCP_ISSD_YN;				//타사발행여부
		String ISSD_CMPY_NAME;				//발행회사명
		String ISSD_DATE;					//발행일자
		String XPRN_DATE;					//만기일자
		String XPRN_PERD_DSCN_CTNT;			//만기기간설명내용
		String PRNL_GRNT_YN;				//원금보장여부
		String PRFT_CNFD_YN;				//수익확정여부
		String PRNL_LOSS_ALWD_YN;			//원금손실가능여부
		String MIDL_DVDD_OCRN_YN;			//중간배당발생여부
		String RDMN_YN;						//상환여부
		String PRIV_CLCL_YN;				//사모계산여부
		String PRFT_CNFD_DATE;				//수익확정일자
		String PRNL_LOSS_PSBL_DCSN_DATE;	//원금손실가능결정일자
		String MIDL_DVDD_PYMT_YN;			//중간배당지급여부
		String RDMN_DATE;					//상환일자
		String FRCT_RDMN_UNIT_PRIC;			//예상상환단가
		String UNAS_DSCN_CTNT;				//기초자산설명내용
		String OTC_MEMO_CTNT;				//장외메모내용
		String APLY_YN;						//적용여부
		String STNR_TM;						//기준시각
		String RDMP_REQT_RSTT_DSCN_CTNT;	//환매청구제한설명내용
		String PREV_RDMP_PRIC_DCSN_DAYS;	//이전환매가격결정일수
		String RDMP_PRIC_ANCT_DAYS;			//환매가격공시일수
		String PREV_RDMP_PRIC_PYMT_DAYS;	//이전환매가격지급일수
		String RDMP_PRIC_DCSN_DAYS;			//환매가격결정일수
		String PREV_RDMP_PRIC_ANCT_DAYS;	//이전환매가격공시일수
		String RDMP_AMNT_PYMT_DAYS;			//환매금액지급일수
		String MDWY_RDMN_PYMT_DAYS;			//중도상환지급일수
		String RDMP_APPL_STRT_DATE;			//환매신청시작일자
		String RDMP_APPL_END_DATE;			//환매신청종료일자
		String PBCM_UNQ_NO;					//발행사고유번호
		String FRST_PRIC_DCSN_DD_DSCN_CTNT;	//최초가격결정일설명내용
		String LAST_PRIC_DCSN_DD_DSCN_CTNT;	//최종가격결정일설명내용
		String MIDL_PRIC_DCSN_DD_DSCN_CTNT;	//중간가격결정일설명내용
		String PRDT_TYPE_2_NAME;			//상품유형2명
		String A_UNAS_CLSN_CODE;			//기초자산분류코드
		String A_UNAS_FRM_CODE;				//기초자산형태코드
		String UNAS_KIND_CODE;				//기초자산종류코드
		String PRDT_ATTR_CNTY_CODE;			//상품속성국가코드
		String PRDT_ATTR_AR_CODE;			//상품속성지역코드
		String PRDT_ATTR_MRKT_CODE;			//상품속성시장코드
			
		// Constructor
		public OutRec1() {
		}

		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("A_OTC_DRVT_DL_CODE", A_OTC_DRVT_DL_CODE);
			m.put("KSD_ISCD", KSD_ISCD);
			m.put("PRDT_NAME", PRDT_NAME);
			m.put("STRD_SCRS_DSCN_IFMN_CTNT", STRD_SCRS_DSCN_IFMN_CTNT);
			m.put("OTCP_ISSD_YN", OTCP_ISSD_YN);
			m.put("ISSD_CMPY_NAME", ISSD_CMPY_NAME);
			m.put("ISSD_DATE", ISSD_DATE);
			m.put("XPRN_DATE", XPRN_DATE);
			m.put("XPRN_PERD_DSCN_CTNT", XPRN_PERD_DSCN_CTNT);
			m.put("PRNL_GRNT_YN", PRNL_GRNT_YN);
			m.put("PRFT_CNFD_YN", PRFT_CNFD_YN);
			m.put("PRNL_LOSS_ALWD_YN", PRNL_LOSS_ALWD_YN);
			m.put("MIDL_DVDD_OCRN_YN", MIDL_DVDD_OCRN_YN);
			m.put("RDMN_YN", RDMN_YN);
			m.put("PRIV_CLCL_YN", PRIV_CLCL_YN);
			m.put("PRFT_CNFD_DATE", PRFT_CNFD_DATE);
			m.put("PRNL_LOSS_PSBL_DCSN_DATE", PRNL_LOSS_PSBL_DCSN_DATE);
			m.put("MIDL_DVDD_PYMT_YN", MIDL_DVDD_PYMT_YN);
			m.put("RDMN_DATE", RDMN_DATE);
			m.put("FRCT_RDMN_UNIT_PRIC", FRCT_RDMN_UNIT_PRIC);
			m.put("UNAS_DSCN_CTNT", UNAS_DSCN_CTNT);
			m.put("OTC_MEMO_CTNT", OTC_MEMO_CTNT);
			m.put("APLY_YN", APLY_YN);
			m.put("STNR_TM", STNR_TM);
			m.put("RDMP_REQT_RSTT_DSCN_CTNT", RDMP_REQT_RSTT_DSCN_CTNT);
			m.put("PREV_RDMP_PRIC_DCSN_DAYS", PREV_RDMP_PRIC_DCSN_DAYS);
			m.put("RDMP_PRIC_ANCT_DAYS", RDMP_PRIC_ANCT_DAYS);
			m.put("PREV_RDMP_PRIC_PYMT_DAYS", PREV_RDMP_PRIC_PYMT_DAYS);
			m.put("RDMP_PRIC_DCSN_DAYS", RDMP_PRIC_DCSN_DAYS);
			m.put("PREV_RDMP_PRIC_ANCT_DAYS", PREV_RDMP_PRIC_ANCT_DAYS);
			m.put("RDMP_AMNT_PYMT_DAYS", RDMP_AMNT_PYMT_DAYS);
			m.put("MDWY_RDMN_PYMT_DAYS", MDWY_RDMN_PYMT_DAYS);
			m.put("RDMP_APPL_STRT_DATE", RDMP_APPL_STRT_DATE);
			m.put("RDMP_APPL_END_DATE", RDMP_APPL_END_DATE);
			m.put("PBCM_UNQ_NO", PBCM_UNQ_NO);
			m.put("FRST_PRIC_DCSN_DD_DSCN_CTNT", FRST_PRIC_DCSN_DD_DSCN_CTNT);
			m.put("LAST_PRIC_DCSN_DD_DSCN_CTNT", LAST_PRIC_DCSN_DD_DSCN_CTNT);
			m.put("MIDL_PRIC_DCSN_DD_DSCN_CTNT", MIDL_PRIC_DCSN_DD_DSCN_CTNT);
			m.put("PRDT_TYPE_2_NAME", PRDT_TYPE_2_NAME);
			m.put("A_UNAS_CLSN_CODE", A_UNAS_CLSN_CODE);
			m.put("A_UNAS_FRM_CODE", A_UNAS_FRM_CODE);
			m.put("UNAS_KIND_CODE", UNAS_KIND_CODE);
			m.put("PRDT_ATTR_CNTY_CODE", PRDT_ATTR_CNTY_CODE);
			m.put("PRDT_ATTR_AR_CODE", PRDT_ATTR_AR_CODE);
			m.put("PRDT_ATTR_MRKT_CODE", PRDT_ATTR_MRKT_CODE);

			return m;
		}
	}
	
	/* constructor */
	public Aimp02gpVO (String ipAddr, String userID, String mediaType) {
		setHeaderUserID(userID);
		setHeaderTrCode("aimp02gp");
		setHeaderDestination((char)'6');
		setHeaderPacketDataType((char)0);
		setHeaderMediaType(mediaType);
		setHeaderIpAddr(ipAddr);
		setCharSet("1");
        setIsActive("0");		
	}

	/* constructor */
	public Aimp02gpVO (String ipAddr, String userID, String mediaType, String brchCD, String deptCD, String acctCD) {
		setHeaderUserID("");
		setHeaderTrCode("aimp02gp");
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
				MCAUtil.writeString(inRec1.pText, inRec1.getPText().getBytes().length, bos);
			} else {
				MCAUtil.writeString(inRec1.PRDT_TYPE_CODE, 2, bos);
				MCAUtil.writeString(inRec1.ISCD, 12, bos);
			}
		}
		
		return bos.toByteArray();
	}
		
	/* parse */
	public void parse(StringInputStream sis) throws Exception {
		
		// OutRec1
		outRec1.A_OTC_DRVT_DL_CODE = StringUtil.changBlankToDash(sis.readString(12));
		outRec1.KSD_ISCD = StringUtil.changBlankToDash(sis.readString(12));
		outRec1.PRDT_NAME = StringUtil.changBlankToDash(sis.readString(90));
		outRec1.STRD_SCRS_DSCN_IFMN_CTNT = StringUtil.changBlankToDash(sis.readString(300));
		outRec1.OTCP_ISSD_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.ISSD_CMPY_NAME = StringUtil.changBlankToDash(sis.readString(60));
		outRec1.ISSD_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.XPRN_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.XPRN_PERD_DSCN_CTNT = StringUtil.changBlankToDash(sis.readString(150));
		outRec1.PRNL_GRNT_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.PRFT_CNFD_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.PRNL_LOSS_ALWD_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.MIDL_DVDD_OCRN_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.RDMN_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.PRIV_CLCL_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.PRFT_CNFD_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.PRNL_LOSS_PSBL_DCSN_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.MIDL_DVDD_PYMT_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.RDMN_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.FRCT_RDMN_UNIT_PRIC = StringUtil.changeStrToDouble(StringUtil.changBlankToDash(sis.readString(15)));
		outRec1.UNAS_DSCN_CTNT = StringUtil.changBlankToDash(sis.readString(1500));
		outRec1.OTC_MEMO_CTNT = StringUtil.changBlankToDash(sis.readString(450));
		outRec1.APLY_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.STNR_TM = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.RDMP_REQT_RSTT_DSCN_CTNT = StringUtil.changBlankToDash(sis.readString(450));
		outRec1.PREV_RDMP_PRIC_DCSN_DAYS = StringUtil.changBlankToDash(sis.readString(6));
		outRec1.RDMP_PRIC_ANCT_DAYS = StringUtil.changBlankToDash(sis.readString(6));
		outRec1.PREV_RDMP_PRIC_PYMT_DAYS = StringUtil.changBlankToDash(sis.readString(6));
		outRec1.RDMP_PRIC_DCSN_DAYS = StringUtil.changBlankToDash(sis.readString(6));
		outRec1.PREV_RDMP_PRIC_ANCT_DAYS = StringUtil.changBlankToDash(sis.readString(6));
		outRec1.RDMP_AMNT_PYMT_DAYS = StringUtil.changBlankToDash(sis.readString(6));
		outRec1.MDWY_RDMN_PYMT_DAYS = StringUtil.changBlankToDash(sis.readString(6));
		outRec1.RDMP_APPL_STRT_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.RDMP_APPL_END_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.PBCM_UNQ_NO = StringUtil.changBlankToDash(sis.readString(5));
		outRec1.FRST_PRIC_DCSN_DD_DSCN_CTNT = StringUtil.changBlankToDash(sis.readString(450));
		outRec1.LAST_PRIC_DCSN_DD_DSCN_CTNT = StringUtil.changBlankToDash(sis.readString(450));
		outRec1.MIDL_PRIC_DCSN_DD_DSCN_CTNT = StringUtil.changBlankToDash(sis.readString(450));
		outRec1.PRDT_TYPE_2_NAME = StringUtil.changBlankToDash(sis.readString(150));
		outRec1.A_UNAS_CLSN_CODE = StringUtil.changBlankToDash(sis.readString(6));
		outRec1.A_UNAS_FRM_CODE = StringUtil.changBlankToDash(sis.readString(6));
		outRec1.UNAS_KIND_CODE = StringUtil.changBlankToDash(sis.readString(6));
		outRec1.PRDT_ATTR_CNTY_CODE = StringUtil.changBlankToDash(sis.readString(2));
		outRec1.PRDT_ATTR_AR_CODE = StringUtil.changBlankToDash(sis.readString(2));
		outRec1.PRDT_ATTR_MRKT_CODE = StringUtil.changBlankToDash(sis.readString(2));
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
