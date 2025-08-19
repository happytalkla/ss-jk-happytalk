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
 * 펀드 상세 정보
 * @author TD5181
 *
 */
public class Igv9251pVO extends VOSupport{

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
		private String ZRN_FUND_CD;			// 상품ID			
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
		private String ZRN_CLASS_NM;
		private String ZRN_ESTAB_DT;
		private String ZRN_BEFORE_FEE;
		private String ZRN_AFTER_FEE;
		private String ZRN_MNG_FEE;
		private String ZRN_SAL_FEE_R;
		private String ZRN_TB_FEE;
		private String ZRN_AT_FEE;
		private String ZRN_FST_SET_AM;
		private String ZRN_MIX_BM_NM;
		private String ZRN_REDEM_TXT;
		private String ZRN_CURR_MNGR_CONT;
		private String ZRN_CMNT;
		private String ZRN_RMK_CONT;
		private String MGCM_ABNM;
		private String ZRN_MANAGE_NM;
		private String A_MGCM_FUND_CODE;
		private String MGCM_FUND_NAME;
		private String STND_FUND_CODE;
		private String ZRN_DT;
		private String ZRN_FUND_LNM;
		private String ZRN_FUND_NM;
		private String FUND_CODE;
		
		// Constructor
		public OutRec1() {
		}


		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("ZRN_CLASS_NM", ZRN_CLASS_NM );
			m.put("ZRN_ESTAB_DT", ZRN_ESTAB_DT );
			m.put("ZRN_BEFORE_FEE", ZRN_BEFORE_FEE );
			m.put("ZRN_AFTER_FEE", ZRN_AFTER_FEE );
			m.put("ZRN_MNG_FEE", ZRN_MNG_FEE );
			m.put("ZRN_SAL_FEE_R", ZRN_SAL_FEE_R );
			m.put("ZRN_TB_FEE", ZRN_TB_FEE );
			m.put("ZRN_AT_FEE", ZRN_AT_FEE );
			m.put("ZRN_FST_SET_AM", ZRN_FST_SET_AM );
			m.put("ZRN_MIX_BM_NM", ZRN_MIX_BM_NM );
			m.put("ZRN_REDEM_TXT", ZRN_REDEM_TXT );
			m.put("ZRN_CURR_MNGR_CONT", ZRN_CURR_MNGR_CONT );
			m.put("ZRN_CMNT", ZRN_CMNT );
			m.put("ZRN_RMK_CONT", ZRN_RMK_CONT );
			m.put("MGCM_ABNM", MGCM_ABNM );
			m.put("ZRN_MANAGE_NM", ZRN_MANAGE_NM );
			m.put("A_MGCM_FUND_CODE", A_MGCM_FUND_CODE );
			m.put("MGCM_FUND_NAME", MGCM_FUND_NAME );
			m.put("STND_FUND_CODE", STND_FUND_CODE );
			m.put("ZRN_DT", ZRN_DT );
			m.put("ZRN_FUND_LNM", ZRN_FUND_LNM );
			m.put("ZRN_FUND_NM", ZRN_FUND_NM );
			m.put("FUND_CODE", FUND_CODE );

			return m;
		}
	}
	
	/* constructor */
	public Igv9251pVO (String ipAddr, String userID, String mediaType) {
		setHeaderUserID(userID);
		setHeaderTrCode("igv9251p");
		setHeaderDestination((char)'6');
		setHeaderPacketDataType((char)0);
		setHeaderMediaType(mediaType);
		setHeaderIpAddr(ipAddr);
		setCharSet("1");
        setIsActive("0");		
	}

	/* constructor */
	public Igv9251pVO (String ipAddr, String userID, String mediaType, String brchCD, String deptCD, String acctCD) {
		setHeaderUserID(userID);
		setHeaderTrCode("igv9251p");
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
			}			
		}
		
		return bos.toByteArray();
	}
		
	/* parse */
	public void parse(StringInputStream sis) throws Exception {
		
		int cntRecord=0;

		// OutRec1
		 outRec1.ZRN_CLASS_NM	 = StringUtil.changBlankToDash(sis.readString(80));
		 outRec1.ZRN_ESTAB_DT	 = StringUtil.changBlankToDash(sis.readString(8));
		 outRec1.ZRN_BEFORE_FEE	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));
		 outRec1.ZRN_AFTER_FEE	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));
		 outRec1.ZRN_MNG_FEE	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));
		 outRec1.ZRN_SAL_FEE_R	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));
		 outRec1.ZRN_TB_FEE	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));
		 outRec1.ZRN_AT_FEE	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));
		 outRec1.ZRN_FST_SET_AM	 = StringUtil.changBlankToDash(StringUtil.changeStrToLong(sis.readString(18)));
		 outRec1.ZRN_MIX_BM_NM	 = StringUtil.changBlankToDash(sis.readString(100));
		 outRec1.ZRN_REDEM_TXT	 = StringUtil.changBlankToDash(sis.readString(200));
		 outRec1.ZRN_CURR_MNGR_CONT	 = StringUtil.changBlankToDash(sis.readString(500));
		 outRec1.ZRN_CMNT	 = StringUtil.changBlankToDash(sis.readString(3000));
		 outRec1.ZRN_RMK_CONT	 = StringUtil.changBlankToDash(sis.readString(300));
		 outRec1.MGCM_ABNM	 = StringUtil.changBlankToDash(sis.readString(60));
		 outRec1.ZRN_MANAGE_NM	 = StringUtil.changBlankToDash(sis.readString(80));
		 outRec1.A_MGCM_FUND_CODE	 = StringUtil.changBlankToDash(sis.readString(15));
		 outRec1.MGCM_FUND_NAME	 = StringUtil.changBlankToDash(sis.readString(150));
		 outRec1.STND_FUND_CODE	 = StringUtil.changBlankToDash(sis.readString(12));
		 outRec1.ZRN_DT	 = StringUtil.changBlankToDash(sis.readString(8));
		 outRec1.ZRN_FUND_LNM	 = StringUtil.changBlankToDash(sis.readString(150));
		 outRec1.ZRN_FUND_NM	 = StringUtil.changBlankToDash(sis.readString(150));
		 outRec1.FUND_CODE	 = StringUtil.changBlankToDash(sis.readString(7));

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
