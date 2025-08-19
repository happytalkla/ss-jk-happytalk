package ht.mca.vo;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.ss.common.channel.mca.common.net.StringInputStream;
import com.ss.common.channel.mca.util.MCAUtil;
import com.ss.common.channel.mca.vo.VOSupport;
import com.ss.common.util.StringUtility;

import lombok.Data;

public class Sgd038pVO extends VOSupport{

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
		private String  PRCE_SECT_CODE          ;
		private String  CNSL_PRHS_ID            ;
		private String  CLNT_YN                 ;
		private String  CLNT_ENTY_ID            ;
		private String  ACNT_NO                 ;
		private String  A_MNCL_NO               ;
		private String  A_SUB_CLNT_NO           ;
		private String  CLNT_CLSS_CODE          ;
		private String  CNSL_PRHS_DATE          ;
		private String  CNSL_STRT_TIME          ;
		private String  END_DATE                ;
		private String  A_END_TIME              ;
		private String  PRDT_SECT_CODE          ;
		private String  MNGG_BRNH_ENTY_CODE     ;
		private String  CLCR_PRCE_STAS_CODE     ;
		private String  CALL_LARG_CLSN_CODE     ;
		private String  CALL_MDUM_CLSN_CODE     ;
		private String  CNSL_LARG_CLSN_CODE     ;
		private String  CNSL_MDUM_CLSN_CODE     ;
		private String  CNSL_SMAL_CLSN_CODE     ;
		private String  CRNY_NED_TIME           ;
		private String  A_DPRT_CODE             ;
		private String  CNSL_TEAM_CODE          ;
		private String  CNSL_EMPY_NO            ;
		private String  RE_CRNY_RQST_TYPE_CODE  ;
		private String  RE_CRNY_PHON_NO         ;
		private String  TCH_PATH_SECT_CODE      ;
		private String  CLNT_CNSL_CTNT          ;
		private String  SVC_RQST_NO             ;
		private String  CRNY_RSLT_CODE          ;
		private String  PHON_CNSD_ID            ;
		private String  HNOP_PHON_CNSD_ID       ;
		private String  ORGL_CNSL_PRHS_ID       ;
		private String  CLNT_PHON_NO            ;
		private String  OTBN_NO                 ;
		private String  RSRV_DATE               ;
		private String  RSRV_TIME               ;
		private String  TCH_TYPE_SECT_CODE      ;
		private String  BSNS_DLRC_RFLC_YN       ;
		private String  ONLN_TCH_RSLT_CODE      ;
		private String  ONLN_CLNT_RACT_CODE     ;
		private String  FLFL_CMPG_NO            ;
		private String  FLFL_CMPG_ACTI_NO       ;
		private String  EVEN_OCRN_NO            ;
		private String  APPL_DATE               ;
		private String  RGSN_SQNO               ;
		private String  SVC_PRCE_STAS_CODE      ;
		private String  XCLN_YN                 ;
         
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
	public Sgd038pVO (String ipAddr, String userID, String mediaType) {
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
	public Sgd038pVO (String ipAddr, String userID, String mediaType, String brchCD, String deptCD, String acctCD) {
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
				MCAUtil.writeString(inRec1.PRCE_SECT_CODE              ,  1, bos);
				MCAUtil.writeString(inRec1.CNSL_PRHS_ID                , 20, bos);
				MCAUtil.writeString(inRec1.CLNT_YN                     ,  1, bos);
				MCAUtil.writeString(inRec1.CLNT_ENTY_ID                , 19, bos);
				MCAUtil.writeString(inRec1.ACNT_NO                     , 20, bos);
				MCAUtil.writeString(inRec1.A_MNCL_NO                   , 13, bos);
				MCAUtil.writeString(inRec1.A_SUB_CLNT_NO               , 13, bos);
				MCAUtil.writeString(inRec1.CLNT_CLSS_CODE              ,  1, bos);
				MCAUtil.writeString(inRec1.CNSL_PRHS_DATE              ,  8, bos);
				MCAUtil.writeString(inRec1.CNSL_STRT_TIME              ,  6, bos);
				MCAUtil.writeString(inRec1.END_DATE                    ,  8, bos);
				MCAUtil.writeString(inRec1.A_END_TIME                  ,  6, bos);
				MCAUtil.writeString(inRec1.PRDT_SECT_CODE              ,  2, bos);
				MCAUtil.writeString(inRec1.MNGG_BRNH_ENTY_CODE         ,  5, bos);
				MCAUtil.writeString(inRec1.CLCR_PRCE_STAS_CODE         ,  1, bos);
				MCAUtil.writeString(inRec1.CALL_LARG_CLSN_CODE         ,  2, bos);
				MCAUtil.writeString(inRec1.CALL_MDUM_CLSN_CODE         ,  2, bos);
				MCAUtil.writeString(inRec1.CNSL_LARG_CLSN_CODE         ,  2, bos);
				MCAUtil.writeString(inRec1.CNSL_MDUM_CLSN_CODE         ,  2, bos);
				MCAUtil.writeString(inRec1.CNSL_SMAL_CLSN_CODE         ,  3, bos);
				MCAUtil.writeString(inRec1.CRNY_NED_TIME               ,  6, bos);
				MCAUtil.writeString(inRec1.A_DPRT_CODE                 ,  5, bos);
				MCAUtil.writeString(inRec1.CNSL_TEAM_CODE              ,  2, bos);
				MCAUtil.writeString(inRec1.CNSL_EMPY_NO                , 16, bos);
				MCAUtil.writeString(inRec1.RE_CRNY_RQST_TYPE_CODE      ,  1, bos);
				MCAUtil.writeString(inRec1.RE_CRNY_PHON_NO             , 20, bos);
				MCAUtil.writeString(inRec1.TCH_PATH_SECT_CODE          ,  2, bos);
				MCAUtil.writeString(inRec1.CLNT_CNSL_CTNT              ,600, bos);
				MCAUtil.writeString(inRec1.SVC_RQST_NO                 , 13, bos);
				MCAUtil.writeString(inRec1.CRNY_RSLT_CODE              ,  2, bos);
				MCAUtil.writeString(inRec1.PHON_CNSD_ID                , 20, bos);
				MCAUtil.writeString(inRec1.HNOP_PHON_CNSD_ID           , 20, bos);
				MCAUtil.writeString(inRec1.ORGL_CNSL_PRHS_ID           , 20, bos);
				MCAUtil.writeString(inRec1.CLNT_PHON_NO                , 20, bos);
				MCAUtil.writeString(inRec1.OTBN_NO                     , 10, bos);
				MCAUtil.writeString(inRec1.RSRV_DATE                   ,  8, bos);
				MCAUtil.writeString(inRec1.RSRV_TIME                   ,  6, bos);
				MCAUtil.writeString(inRec1.TCH_TYPE_SECT_CODE          ,  2, bos);
				MCAUtil.writeString(inRec1.BSNS_DLRC_RFLC_YN           ,  1, bos);
				MCAUtil.writeString(inRec1.ONLN_TCH_RSLT_CODE          ,  2, bos);
				MCAUtil.writeString(inRec1.ONLN_CLNT_RACT_CODE         ,  5, bos);
				MCAUtil.writeString(inRec1.FLFL_CMPG_NO                , 16, bos);
				MCAUtil.writeString(inRec1.FLFL_CMPG_ACTI_NO           , 16, bos);
				MCAUtil.writeString(inRec1.EVEN_OCRN_NO                , 16, bos);
				MCAUtil.writeString(inRec1.APPL_DATE                   ,  8, bos);
				MCAUtil.writeString(inRec1.RGSN_SQNO                   ,  6, bos);
				MCAUtil.writeString(inRec1.SVC_PRCE_STAS_CODE          ,  2, bos);
				MCAUtil.writeString(inRec1.XCLN_YN                     ,  1, bos);             
                                                                                                                                        
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