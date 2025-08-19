package ht.mca.vo;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.ss.common.channel.mca.common.net.StringInputStream;
import com.ss.common.channel.mca.util.MCAUtil;
import com.ss.common.channel.mca.vo.VOSupport;
import com.ss.common.util.StringUtility;

import lombok.Data;

public class Sgd0038pVO extends VOSupport{

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
		private String PRCE_SECT_CODE; /* 처리구분코드*/	
		private String CNSL_PRHS_ID; /* 상담이력ID*/	
		private String CLNT_YN; /* 고객여부*/	
		private String CLNT_ENTY_ID; /* 고객엔티티ID*/	
		private String ACNT_NO; /* 계좌번호*/	
		private String A_MNCL_NO; /* 주고객번호*/	
		private String A_SUB_CLNT_NO; /* 부고객번호*/	
		private String CLNT_CLSS_CODE; /* 고객등급코드*/	
		private String CNSL_PRHS_DATE; /* 상담이력일자*/	
		private String CNSL_STRT_TIME; /* 상담시작시간*/	
		private String END_DATE; /* 종료일자*/	
		private String A_END_TIME; /* 종료시간*/	
		private String PRDT_SECT_CODE; /* 상품구분코드*/	
		private String MNGG_BRNH_ENTY_CODE; /* 관리점코드*/	
		private String CLCR_PRCE_STAS_CODE; /* 콜센터처리상태코드*/	
		private String CALL_LARG_CLSN_CODE; /* 콜대분류코드*/	
		private String CALL_MDUM_CLSN_CODE; /* 콜중분류코드*/	
		private String CNSL_LARG_CLSN_CODE; /* 상담대분류코드*/	
		private String CNSL_MDUM_CLSN_CODE; /* 상담중분류코드*/	
		private String CNSL_SMAL_CLSN_CODE; /* 상담소분류코드*/	
		private String CRNY_NED_TIME; /* 통화소요시간*/	
		private String A_DPRT_CODE; /* 부서코드*/	
		private String CNSL_TEAM_CODE; /* 상담팀코드*/	
		private String CNSL_EMPY_NO; /* 상담사원번호*/	
		private String RE_CRNY_RQST_TYPE_CODE; /* 재통화요청유형코드*/	
		private String RE_CRNY_PHON_NO; /* 재통화전화번호*/	
		private String TCH_PATH_SECT_CODE; /* 접촉경로구분코드*/	
		private String CLNT_CNSL_CTNT; /* 고객상담내용*/	
		private String SVC_RQST_NO; /* 서비스요청번호*/	
		private String CRNY_RSLT_CODE; /* 통화결과코드*/	
		private String PHON_CNSD_ID; /* 전화연결ID*/	
		private String HNOP_PHON_CNSD_ID; /* 수동전화연결ID*/	
		private String ORGL_CNSL_PRHS_ID; /* 원상담이력ID*/	
		private String CLNT_PHON_NO; /* 고객전화번호*/	
		private String OTBN_NO; /* 아웃바운드번호*/	
		private String RSRV_DATE; /* 예약일자*/	
		private String RSRV_TIME; /* 예약시간*/	
		private String TCH_TYPE_SECT_CODE; /* 접촉유형구분코드*/	
		private String BSNS_DLRC_RFLC_YN; /* 영업일지반영여부*/	
		private String ONLN_TCH_RSLT_CODE; /* 온라인접촉결과코드*/	
		private String ONLN_CLNT_RACT_CODE; /* 온라인고객반응코드*/	
		private String FLFL_CMPG_NO; /* 수행캠페인번호*/	
		private String FLFL_CMPG_ACTI_NO; /* 수행캠페인활동번호*/	
		private String EVEN_OCRN_NO; /* 이벤트발생번호*/	
		private String APPL_DATE; /* 신청일자*/	
		private String RGSN_SQNO; /* 등록일련번호*/	
		private String SVC_PRCE_STAS_CODE; /* 서비스처리상태코드*/	
		private String XCLN_YN; /* 제외여부*/	

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
		String A_PRCE_RSLT_CODE;					//처리결과

		// Constructor
		public OutRec1() {
		}


		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("A_PRCE_RSLT_CODE", A_PRCE_RSLT_CODE);
			return m;
		}
	}
	
	/* constructor */
	public Sgd0038pVO (String ipAddr, String userID, String mediaType) {
		setHeaderUserID(userID);
		setHeaderTrCode("sgd0038p");
		setHeaderDestination((char)'6');
		setHeaderPacketDataType((char)0);
		setHeaderMediaType(mediaType);
		setHeaderIpAddr(ipAddr);
		setCharSet("1");
        setIsActive("0");		
	}

	/* constructor */
	public Sgd0038pVO (String ipAddr, String userID, String mediaType, String brchCD, String deptCD, String acctCD) {
		setHeaderUserID(userID);
		setHeaderTrCode("sgd0038p");
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
				MCAUtil.writeString(inRec1.PRCE_SECT_CODE,   1, bos);           /* 처리구분코드 */        
				MCAUtil.writeString(inRec1.CNSL_PRHS_ID,   20, bos);           /* 상담이력ID */        
				MCAUtil.writeString(inRec1.CLNT_YN,   1, bos);           /* 고객여부 */        
				MCAUtil.writeString(inRec1.CLNT_ENTY_ID,   19, bos);           /* 고객엔티티ID */        
				MCAUtil.writeString(inRec1.ACNT_NO,   20, bos);           /* 계좌번호 */        
				MCAUtil.writeString(inRec1.A_MNCL_NO,   13, bos);           /* 주고객번호 */        
				MCAUtil.writeString(inRec1.A_SUB_CLNT_NO,   13, bos);           /* 부고객번호 */        
				MCAUtil.writeString(inRec1.CLNT_CLSS_CODE,   1, bos);           /* 고객등급코드 */        
				MCAUtil.writeString(inRec1.CNSL_PRHS_DATE,   8, bos);           /* 상담이력일자 */        
				MCAUtil.writeString(inRec1.CNSL_STRT_TIME,   6, bos);           /* 상담시작시간 */        
				MCAUtil.writeString(inRec1.END_DATE,   8, bos);           /* 종료일자 */        
				MCAUtil.writeString(inRec1.A_END_TIME,   6, bos);           /* 종료시간 */        
				MCAUtil.writeString(inRec1.PRDT_SECT_CODE,   2, bos);           /* 상품구분코드 */        
				MCAUtil.writeString(inRec1.MNGG_BRNH_ENTY_CODE,   5, bos);           /* 관리점코드 */        
				MCAUtil.writeString(inRec1.CLCR_PRCE_STAS_CODE,   1, bos);           /* 콜센터처리상태코드 */        
				MCAUtil.writeString(inRec1.CALL_LARG_CLSN_CODE,   2, bos);           /* 콜대분류코드 */        
				MCAUtil.writeString(inRec1.CALL_MDUM_CLSN_CODE,   2, bos);           /* 콜중분류코드 */        
				MCAUtil.writeString(inRec1.CNSL_LARG_CLSN_CODE,   2, bos);           /* 상담대분류코드 */        
				MCAUtil.writeString(inRec1.CNSL_MDUM_CLSN_CODE,   2, bos);           /* 상담중분류코드 */        
				MCAUtil.writeString(inRec1.CNSL_SMAL_CLSN_CODE,   3, bos);           /* 상담소분류코드 */        
				MCAUtil.writeString(inRec1.CRNY_NED_TIME,   6, bos);           /* 통화소요시간 */        
				MCAUtil.writeString(inRec1.A_DPRT_CODE,   5, bos);           /* 부서코드 */        
				MCAUtil.writeString(inRec1.CNSL_TEAM_CODE,   2, bos);           /* 상담팀코드 */        
				MCAUtil.writeString(inRec1.CNSL_EMPY_NO,   16, bos);           /* 상담사원번호 */        
				MCAUtil.writeString(inRec1.RE_CRNY_RQST_TYPE_CODE,   1, bos);           /* 재통화요청유형코드 */        
				MCAUtil.writeString(inRec1.RE_CRNY_PHON_NO,   20, bos);           /* 재통화전화번호 */        
				MCAUtil.writeString(inRec1.TCH_PATH_SECT_CODE,   2, bos);           /* 접촉경로구분코드 */        
				MCAUtil.writeString(inRec1.CLNT_CNSL_CTNT,   600, bos);           /* 고객상담내용 */        
				MCAUtil.writeString(inRec1.SVC_RQST_NO,   13, bos);           /* 서비스요청번호 */        
				MCAUtil.writeString(inRec1.CRNY_RSLT_CODE,   2, bos);           /* 통화결과코드 */        
				MCAUtil.writeString(inRec1.PHON_CNSD_ID,   20, bos);           /* 전화연결ID */        
				MCAUtil.writeString(inRec1.HNOP_PHON_CNSD_ID,   20, bos);           /* 수동전화연결ID */        
				MCAUtil.writeString(inRec1.ORGL_CNSL_PRHS_ID,   20, bos);           /* 원상담이력ID */        
				MCAUtil.writeString(inRec1.CLNT_PHON_NO,   20, bos);           /* 고객전화번호 */        
				MCAUtil.writeString(inRec1.OTBN_NO,   10, bos);           /* 아웃바운드번호 */        
				MCAUtil.writeString(inRec1.RSRV_DATE,   8, bos);           /* 예약일자 */        
				MCAUtil.writeString(inRec1.RSRV_TIME,   6, bos);           /* 예약시간 */        
				MCAUtil.writeString(inRec1.TCH_TYPE_SECT_CODE,   2, bos);           /* 접촉유형구분코드 */        
				MCAUtil.writeString(inRec1.BSNS_DLRC_RFLC_YN,   1, bos);           /* 영업일지반영여부 */        
				MCAUtil.writeString(inRec1.ONLN_TCH_RSLT_CODE,   2, bos);           /* 온라인접촉결과코드 */        
				MCAUtil.writeString(inRec1.ONLN_CLNT_RACT_CODE,   5, bos);           /* 온라인고객반응코드 */        
				MCAUtil.writeString(inRec1.FLFL_CMPG_NO,   16, bos);           /* 수행캠페인번호 */        
				MCAUtil.writeString(inRec1.FLFL_CMPG_ACTI_NO,   16, bos);           /* 수행캠페인활동번호 */        
				MCAUtil.writeString(inRec1.EVEN_OCRN_NO,   16, bos);           /* 이벤트발생번호 */        
				MCAUtil.writeString(inRec1.APPL_DATE,   8, bos);           /* 신청일자 */        
				MCAUtil.writeString(inRec1.RGSN_SQNO,   6, bos);           /* 등록일련번호 */        
				MCAUtil.writeString(inRec1.SVC_PRCE_STAS_CODE,   2, bos);           /* 서비스처리상태코드 */        
				MCAUtil.writeString(inRec1.XCLN_YN,   1, bos);           /* 제외여부 */        
                                                                                                   
			}			
		}
		
		return bos.toByteArray();
	}
		
	/* parse */
	public void parse(StringInputStream sis) throws Exception {
		
		// OutRec1
		outRec1.A_PRCE_RSLT_CODE = sis.readString(4);

	}
	/* validate */
	public boolean validate() throws Exception {
		//implement here
		return true;
	}

	/* toMap */
	public Map<String, Object> toMap() {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("outRec1",outRec1.toMap());
		
		return m;
	}
}                                            