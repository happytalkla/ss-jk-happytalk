package ht.mca.vo;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.ss.common.channel.mca.common.net.StringInputStream;
import com.ss.common.channel.mca.util.MCAUtil;
import com.ss.common.channel.mca.vo.VOSupport;
import com.ss.common.util.StringUtility;

import lombok.Data;

public class Sge1011pVO extends VOSupport{

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
		private String H_iprce_sect_code         ; /* 처리구분코드 */	
		private String H_icnsl_prhs_id           ; /* 상담이력ID */	
		private String H_iclnt_yn                ; /* 고객여부 */	
		private String H_iclnt_enty_id           ; /* 고객엔티티ID */	
		private String H_iacnt_no                ; /* 계좌번호 */	
		private String H_ia_mncl_no              ; /* 주고객번호 */	
		private String H_ia_sub_clnt_no          ; /* 부고객번호 */	
		private String H_iclnt_clss_code         ; /* 고객등급코드 */	
		private String H_icnsl_prhs_date         ; /* 상담이력일자 */	
		private String H_icnsl_strt_time         ; /* 상담시작시간 */	
		private String H_iend_date               ; /* 종료일자 */	
		private String H_ia_end_time             ; /* 종료시간 */	
		private String H_iprdt_sect_code         ; /* 상품구분코드 */	
		private String H_imngg_brnh_enty_code    ; /* 관리점코드 */	
		private String H_iclcr_prce_stas_code    ; /* 콜센터처리상태코드 */	
		private String H_icall_larg_clsn_code    ; /* 콜대분류코드 */	
		private String H_icall_mdum_clsn_code    ; /* 콜중분류코드 */	
		private String H_icnsl_larg_clsn_code    ; /* 상담대분류코드 */	
		private String H_icnsl_mdum_clsn_code    ; /* 상담중분류코드 */	
		private String H_icnsl_smal_clsn_code    ; /* 상담소분류코드 */	
		private String H_icrny_ned_time          ; /* 통화소요시간 */	
		private String H_ia_dprt_code            ; /* 부서코드 */	
		private String H_icnsl_team_code         ; /* 상담팀코드 */	
		private String H_icnsl_empy_no           ; /* 상담사원번호 */	
		private String H_ire_crny_rqst_type_code ; /* 재통화요청유형코드 */	
		private String H_ire_crny_phon_no        ; /* 재통화전화번호 */	
		private String H_itch_path_sect_code     ; /* 접촉경로구분코드 */	
		private String H_iclnt_cnsl_ctnt         ; /* 고객상담내용 */	
		private String H_isvc_rqst_no            ; /* 서비스요청번호 */	
		private String H_icrny_rslt_code         ; /* 통화결과코드 */	
		private String H_iphon_cnsd_id           ; /* 전화연결ID */	
		private String H_ihnop_phon_cnsd_id      ; /* 수동전화연결ID */	
		private String H_iorgl_cnsl_prhs_id      ; /* 원상담이력ID */	
		private String H_iclnt_phon_no           ; /* 고객전화번호 */	
		private String H_iotbn_no                ; /* 아웃바운드번호 */	
		private String H_irsrv_date              ; /* 예약일자 */	
		private String H_irsrv_time              ; /* 예약시간 */	

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
		String H_oa_prce_rslt_code;					//처리결과

		// Constructor
		public OutRec1() {
		}


		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("H_oa_prce_rslt_code", H_oa_prce_rslt_code);
			return m;
		}
	}
	
	/* constructor */
	public Sge1011pVO (String ipAddr, String userID, String mediaType) {
		setHeaderUserID(userID);
		setHeaderTrCode("sge1011p");
		setHeaderDestination((char)'6');
		setHeaderPacketDataType((char)0);
		setHeaderMediaType(mediaType);
		setHeaderIpAddr(ipAddr);
		setCharSet("1");
        setIsActive("0");		
	}

	/* constructor */
	public Sge1011pVO (String ipAddr, String userID, String mediaType, String brchCD, String deptCD, String acctCD) {
		setHeaderUserID(userID);
		setHeaderTrCode("sge1011p");
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
				MCAUtil.writeString(inRec1.H_iprce_sect_code        ,   1, bos);           /* 처리구분코드 */        
				MCAUtil.writeString(inRec1.H_icnsl_prhs_id          ,  20, bos);           /* 상담이력ID */        
				MCAUtil.writeString(inRec1.H_iclnt_yn               ,   1, bos);           /* 고객여부 */          
				MCAUtil.writeString(inRec1.H_iclnt_enty_id          ,  19, bos);           /* 고객엔티티ID */       
				MCAUtil.writeString(inRec1.H_iacnt_no               ,  20, bos);           /* 계좌번호 */          
				MCAUtil.writeString(inRec1.H_ia_mncl_no             ,  13, bos);           /* 주고객번호 */         
				MCAUtil.writeString(inRec1.H_ia_sub_clnt_no         ,  13, bos);           /* 부고객번호 */         
				MCAUtil.writeString(inRec1.H_iclnt_clss_code        ,   1, bos);           /* 고객등급코드 */        
				MCAUtil.writeString(inRec1.H_icnsl_prhs_date        ,   8, bos);           /* 상담이력일자 */        
				MCAUtil.writeString(inRec1.H_icnsl_strt_time        ,   6, bos);           /* 상담시작시간 */        
				MCAUtil.writeString(inRec1.H_iend_date              ,   8, bos);           /* 종료일자 */          
				MCAUtil.writeString(inRec1.H_ia_end_time            ,   6, bos);           /* 종료시간 */          
				MCAUtil.writeString(inRec1.H_iprdt_sect_code        ,   2, bos);           /* 상품구분코드 */        
				MCAUtil.writeString(inRec1.H_imngg_brnh_enty_code   ,   5, bos);           /* 관리점코드 */         
				MCAUtil.writeString(inRec1.H_iclcr_prce_stas_code   ,   1, bos);           /* 콜센터처리상태코드 */     
				MCAUtil.writeString(inRec1.H_icall_larg_clsn_code   ,   2, bos);           /* 콜대분류코드 */        
				MCAUtil.writeString(inRec1.H_icall_mdum_clsn_code   ,   2, bos);           /* 콜중분류코드 */        
				MCAUtil.writeString(inRec1.H_icnsl_larg_clsn_code   ,   2, bos);           /* 상담대분류코드 */       
				MCAUtil.writeString(inRec1.H_icnsl_mdum_clsn_code   ,   2, bos);           /* 상담중분류코드 */       
				MCAUtil.writeString(inRec1.H_icnsl_smal_clsn_code   ,   3, bos);           /* 상담소분류코드 */       
				MCAUtil.writeString(inRec1.H_icrny_ned_time         ,   6, bos);           /* 통화소요시간 */        
				MCAUtil.writeString(inRec1.H_ia_dprt_code           ,   5, bos);           /* 부서코드 */          
				MCAUtil.writeString(inRec1.H_icnsl_team_code        ,   2, bos);           /* 상담팀코드 */         
				MCAUtil.writeString(inRec1.H_icnsl_empy_no          ,  16, bos);           /* 상담사원번호 */        
				MCAUtil.writeString(inRec1.H_ire_crny_rqst_type_code,   1, bos);           /* 재통화요청유형코드 */     
				MCAUtil.writeString(inRec1.H_ire_crny_phon_no       ,  20, bos);           /* 재통화전화번호 */       
				MCAUtil.writeString(inRec1.H_itch_path_sect_code    ,   2, bos);           /* 접촉경로구분코드 */      
				MCAUtil.writeString(inRec1.H_iclnt_cnsl_ctnt        , 600, bos);           /* 고객상담내용 */        
				MCAUtil.writeString(inRec1.H_isvc_rqst_no           ,  13, bos);           /* 서비스요청번호 */       
				MCAUtil.writeString(inRec1.H_icrny_rslt_code        ,   2, bos);           /* 통화결과코드 */        
				MCAUtil.writeString(inRec1.H_iphon_cnsd_id          ,  20, bos);           /* 전화연결ID */        
				MCAUtil.writeString(inRec1.H_ihnop_phon_cnsd_id     ,  20, bos);           /* 수동전화연결ID */      
				MCAUtil.writeString(inRec1.H_iorgl_cnsl_prhs_id     ,  20, bos);           /* 원상담이력ID */       
				MCAUtil.writeString(inRec1.H_iclnt_phon_no          ,  20, bos);           /* 고객전화번호 */        
				MCAUtil.writeString(inRec1.H_iotbn_no               ,  10, bos);           /* 아웃바운드번호 */       
				MCAUtil.writeString(inRec1.H_irsrv_date             ,   8, bos);           /* 예약일자 */          
				MCAUtil.writeString(inRec1.H_irsrv_time             ,   6, bos);           /* 예약시간 */          
                                                                                                    
			}			
		}
		
		return bos.toByteArray();
	}
		
	/* parse */
	public void parse(StringInputStream sis) throws Exception {
		
		// OutRec1
		outRec1.H_oa_prce_rslt_code = sis.readString(4);

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