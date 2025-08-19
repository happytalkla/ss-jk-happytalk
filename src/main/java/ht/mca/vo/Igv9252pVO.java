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
 * 펀드 기간별 수익률
 * @author TD5181
 *
 */
public class Igv9252pVO extends VOSupport{

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
		private String STNR_DATE;
		private String ZRN_FUND_NM;
		private String FUND_NAME;
		private String STNG_DATE;
		private String A_STNG_AMNT1;
		private String A_STNG_AMNT2;
		private String ZRN_M3_RTN;
		private String ZRN_W13_P_RANK;
		private String ZRN_M3_BM_RTN;
		private String A_PRFT_1_RATE;
		private String ZRN_W13_FUND_SHARP;
		private String ZRN_W13_FUND_BETA;
		private String A_TAGT_YN1;
		private String A_TAGT_YN2;
		private String A_TAGT_YN3;
		private String A_TAGT_YN4;
		private String A_TAGT_YN5;
		
		// Constructor
		public OutRec1() {
		}


		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("STNR_DATE", STNR_DATE );
			m.put("ZRN_FUND_NM", ZRN_FUND_NM );
			m.put("FUND_NAME", FUND_NAME );
			m.put("STNG_DATE", STNG_DATE );
			m.put("A_STNG_AMNT1", A_STNG_AMNT1 );
			m.put("A_STNG_AMNT2", A_STNG_AMNT2 );
			m.put("ZRN_M3_RTN", ZRN_M3_RTN );
			m.put("ZRN_W13_P_RANK", ZRN_W13_P_RANK );
			m.put("ZRN_M3_BM_RTN", ZRN_M3_BM_RTN );
			m.put("A_PRFT_1_RATE", A_PRFT_1_RATE );
			m.put("ZRN_W13_FUND_SHARP", ZRN_W13_FUND_SHARP );
			m.put("ZRN_W13_FUND_BETA", ZRN_W13_FUND_BETA );
			m.put("A_TAGT_YN1", A_TAGT_YN1 );
			m.put("A_TAGT_YN2", A_TAGT_YN2 );
			m.put("A_TAGT_YN3", A_TAGT_YN3 );
			m.put("A_TAGT_YN4", A_TAGT_YN4 );
			m.put("A_TAGT_YN5", A_TAGT_YN5 );
			return m;
		}
	}
	
	/* constructor */
	public Igv9252pVO (String ipAddr, String userID, String mediaType) {
		setHeaderUserID(userID);
		setHeaderTrCode("igv9252p");
		setHeaderDestination((char)'6');
		setHeaderPacketDataType((char)0);
		setHeaderMediaType(mediaType);
		setHeaderIpAddr(ipAddr);
		setCharSet("1");
        setIsActive("0");		
	}

	/* constructor */
	public Igv9252pVO (String ipAddr, String userID, String mediaType, String brchCD, String deptCD, String acctCD) {
		setHeaderUserID(userID);
		setHeaderTrCode("igv9252p");
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
		outRec1.STNR_DATE	 = StringUtil.changBlankToDash(sis.readString(8));	
		outRec1.ZRN_FUND_NM	 = StringUtil.changBlankToDash(sis.readString(150));	
		outRec1.FUND_NAME	 = StringUtil.changBlankToDash(sis.readString(60));	
		outRec1.STNG_DATE	 = StringUtil.changBlankToDash(sis.readString(8));	
		outRec1.A_STNG_AMNT1	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));	
		outRec1.A_STNG_AMNT2	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));	
		outRec1.ZRN_M3_RTN	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));	
		outRec1.ZRN_W13_P_RANK	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));	
		outRec1.ZRN_M3_BM_RTN	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));	
		outRec1.A_PRFT_1_RATE	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(17)));	
		outRec1.ZRN_W13_FUND_SHARP	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));	
		outRec1.ZRN_W13_FUND_BETA	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));	
		outRec1.A_TAGT_YN1	 = StringUtil.changBlankToDash(sis.readString(1));	
		outRec1.A_TAGT_YN2	 = StringUtil.changBlankToDash(sis.readString(1));	
		outRec1.A_TAGT_YN3	 = StringUtil.changBlankToDash(sis.readString(1));	
		outRec1.A_TAGT_YN4	 = StringUtil.changBlankToDash(sis.readString(1));	
		outRec1.A_TAGT_YN5	 = StringUtil.changBlankToDash(sis.readString(1));	


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
