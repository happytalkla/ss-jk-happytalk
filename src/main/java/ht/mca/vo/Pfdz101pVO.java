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
 * 상품전환 프로그램
 * @author TD5181
 *
 */
public class Pfdz101pVO extends VOSupport{

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
		private String PRDT_ID;			// 상품ID			
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
		String STND_PRDT_CLSN_CODE;			//표준상품분류코드5자리 string으로 첫번째 문자가 G:펀드, E:ELS, D:채권
		String RPRS_ISCD;					//대표종목코드 펀드:협회표준펀드코드, ELS/채권 : 종목코드
		String PRDT_NAME;
		String STNR_DATE;					//펀드만 사용, 기준일자
		String FUND_CODE;					//펀드만 사용, 자체펀드코드
		String ZRN_CLASS_CD;				//펀드만 사용, KFR유형정보
		String FIRST_PRDT_CODE;				//코드의 첫글자  G:펀드, E:ELS, D:채권
		
		// Constructor
		public OutRec1() {
		}


		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("STND_PRDT_CLSN_CODE", STND_PRDT_CLSN_CODE);
			m.put("RPRS_ISCD",RPRS_ISCD);
			m.put("PRDT_NAME",PRDT_NAME);
			m.put("STNR_DATE",STNR_DATE);
			m.put("FUND_CODE",FUND_CODE);
			m.put("ZRN_CLASS_CD",ZRN_CLASS_CD);
			m.put("FIRST_PRDT_CODE",FIRST_PRDT_CODE);
			return m;
		}
	}
	
	/* constructor */
	public Pfdz101pVO (String ipAddr, String userID, String mediaType) {
		setHeaderUserID(userID);
		setHeaderTrCode("pfdz101p");
		setHeaderDestination((char)'6');
		setHeaderPacketDataType((char)0);
		setHeaderMediaType(mediaType);
		setHeaderIpAddr(ipAddr);
		setCharSet("1");
        setIsActive("0");		
	}

	/* constructor */
	public Pfdz101pVO (String ipAddr, String userID, String mediaType, String brchCD, String deptCD, String acctCD) {
		setHeaderUserID(userID);
		setHeaderTrCode("pfdz101p");
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
				MCAUtil.writeString(inRec1.PRDT_ID, 19, bos);
			}				
		}
		
		return bos.toByteArray();
	}
		
	/* parse */
	public void parse(StringInputStream sis) throws Exception {
		
		// OutRec1
		outRec1.STND_PRDT_CLSN_CODE = StringUtil.changBlankToDash(sis.readString(5));
		outRec1.RPRS_ISCD = StringUtil.changBlankToDash(sis.readString(50));
		outRec1.PRDT_NAME = StringUtil.changBlankToDash(sis.readString(90));
		outRec1.STNR_DATE = StringUtil.changBlankToDash(sis.readString(8));
		outRec1.FUND_CODE = StringUtil.changBlankToDash(sis.readString(7));
		outRec1.ZRN_CLASS_CD = StringUtil.changBlankToDash(sis.readString(13));
		
		if(outRec1.STND_PRDT_CLSN_CODE != null && outRec1.STND_PRDT_CLSN_CODE.length() > 0) {
			outRec1.FIRST_PRDT_CODE = outRec1.STND_PRDT_CLSN_CODE.substring(0, 1);
		}else {
			outRec1.FIRST_PRDT_CODE ="";
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
		m.put("outRec1",outRec1.toMap());
		
		return m;
	}
}
