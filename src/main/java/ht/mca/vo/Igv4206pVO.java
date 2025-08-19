package ht.mca.vo;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ss.common.channel.mca.common.net.StringInputStream;
import com.ss.common.channel.mca.util.MCAUtil;
import com.ss.common.channel.mca.vo.VOSupport;
import com.ss.common.util.StringUtility;

import ht.util.StringUtil;
import lombok.Data;
/**
 * 펀드-종목/업종/비중
 * @author TD5181
 *
 */
public class Igv4206pVO extends VOSupport{

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
		private String STNR_DATE;		// 기준일자
		private String ZRN_FUND_CD;		// 펀드평가정보협회표준펀드코드
		private String ZRN_ZRN_CD;		// 펀드평가정보KFR유형코드
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
		String GABAGE_STR;
		String ZRN_SEC_NM_0;		//펀드평가정보종목명
		String ZRN_SECTOR_NM_0;	//펀드평가정보업종명
		String A_INSE_RATE1_0;	//편입비율1
		String A_INSE_RATE2_0;	//편입비율2
		String A_INSE_RATE3_0;	//편입비율3

		String ZRN_SEC_NM_1;		//펀드평가정보종목명
		String ZRN_SECTOR_NM_1;	//펀드평가정보업종명
		String A_INSE_RATE1_1;	//편입비율1
		String A_INSE_RATE2_1;	//편입비율2
		String A_INSE_RATE3_1;	//편입비율3

		String ZRN_SEC_NM_2;		//펀드평가정보종목명
		String ZRN_SECTOR_NM_2;	//펀드평가정보업종명
		String A_INSE_RATE1_2;	//편입비율1
		String A_INSE_RATE2_2;	//편입비율2
		String A_INSE_RATE3_2;	//편입비율3

		String ZRN_SEC_NM_3;		//펀드평가정보종목명
		String ZRN_SECTOR_NM_3;	//펀드평가정보업종명
		String A_INSE_RATE1_3;	//편입비율1
		String A_INSE_RATE2_3;	//편입비율2
		String A_INSE_RATE3_3;	//편입비율3

		String ZRN_SEC_NM_4;		//펀드평가정보종목명
		String ZRN_SECTOR_NM_4;	//펀드평가정보업종명
		String A_INSE_RATE1_4;	//편입비율1
		String A_INSE_RATE2_4;	//편입비율2
		String A_INSE_RATE3_4;	//편입비율3

		// Constructor
		public OutRec1() {
		}

		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("ZRN_SEC_NM_0", ZRN_SEC_NM_0);
			m.put("ZRN_SECTOR_NM_0", ZRN_SECTOR_NM_0);
			m.put("A_INSE_RATE1_0", A_INSE_RATE1_0);
			m.put("A_INSE_RATE2_0", A_INSE_RATE2_0);
			m.put("A_INSE_RATE3_0", A_INSE_RATE3_0);

			m.put("ZRN_SEC_NM_1", ZRN_SEC_NM_1);
			m.put("ZRN_SECTOR_NM_1", ZRN_SECTOR_NM_1);
			m.put("A_INSE_RATE1_1", A_INSE_RATE1_1);
			m.put("A_INSE_RATE2_1", A_INSE_RATE2_1);
			m.put("A_INSE_RATE3_1", A_INSE_RATE3_1);

			m.put("ZRN_SEC_NM_2", ZRN_SEC_NM_2);
			m.put("ZRN_SECTOR_NM_2", ZRN_SECTOR_NM_2);
			m.put("A_INSE_RATE1_2", A_INSE_RATE1_2);
			m.put("A_INSE_RATE2_2", A_INSE_RATE2_2);
			m.put("A_INSE_RATE3_2", A_INSE_RATE3_2);

			m.put("ZRN_SEC_NM_3", ZRN_SEC_NM_3);
			m.put("ZRN_SECTOR_NM_3", ZRN_SECTOR_NM_3);
			m.put("A_INSE_RATE1_3", A_INSE_RATE1_3);
			m.put("A_INSE_RATE2_3", A_INSE_RATE2_3);
			m.put("A_INSE_RATE3_3", A_INSE_RATE3_3);

			m.put("ZRN_SEC_NM_4", ZRN_SEC_NM_4);
			m.put("ZRN_SECTOR_NM_4", ZRN_SECTOR_NM_4);
			m.put("A_INSE_RATE1_4", A_INSE_RATE1_4);
			m.put("A_INSE_RATE2_4", A_INSE_RATE2_4);
			m.put("A_INSE_RATE3_4", A_INSE_RATE3_4);
			return m;
		}
	}

	/* constructor */
	public Igv4206pVO (String ipAddr, String userID, String mediaType) {
		setHeaderUserID(userID);
		setHeaderTrCode("igv4206p");
		setHeaderDestination('6');
		setHeaderPacketDataType((char)0);
		setHeaderMediaType(mediaType);
		setHeaderIpAddr(ipAddr);
		setCharSet("1");
        setIsActive("0");
	}

	/* constructor */
	public Igv4206pVO (String ipAddr, String userID, String mediaType, String brchCD, String deptCD, String acctCD) {
		setHeaderUserID(userID);
		setHeaderTrCode("igv4206p");
		setHeaderDestination('6');
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
	@Override
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
				MCAUtil.writeString(inRec1.STNR_DATE, 8, bos);
				MCAUtil.writeString(inRec1.ZRN_FUND_CD, 12, bos);
				MCAUtil.writeString(inRec1.ZRN_ZRN_CD, 13, bos);
			}
		}

		return bos.toByteArray();
	}

	/* parse */
	@Override
	public void parse(StringInputStream sis) throws Exception {
		// OutRec1
		int cntRecord=20;
		//for(int i=0;i<cntRecord;i++) {
		//	OutRec1 out = new OutRec1();
			outRec1.GABAGE_STR = StringUtil.changBlankToDash(sis.readString(4));
			outRec1.ZRN_SEC_NM_0 = StringUtil.changBlankToDash(sis.readString(80));
			outRec1.ZRN_SECTOR_NM_0 = StringUtil.changBlankToDash(sis.readString(100));
			outRec1.A_INSE_RATE1_0 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(7)));
			outRec1.A_INSE_RATE2_0 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(7)));
			outRec1.A_INSE_RATE3_0 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(7)));

			outRec1.ZRN_SEC_NM_1 = StringUtil.changBlankToDash(sis.readString(80));
			outRec1.ZRN_SECTOR_NM_1 = StringUtil.changBlankToDash(sis.readString(100));
			outRec1.A_INSE_RATE1_1 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(7)));
			outRec1.A_INSE_RATE2_1 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(7)));
			outRec1.A_INSE_RATE3_1 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(7)));

			outRec1.ZRN_SEC_NM_2 = StringUtil.changBlankToDash(sis.readString(80));
			outRec1.ZRN_SECTOR_NM_2 = StringUtil.changBlankToDash(sis.readString(100));
			outRec1.A_INSE_RATE1_2 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(7)));
			outRec1.A_INSE_RATE2_2 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(7)));
			outRec1.A_INSE_RATE3_2 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(7)));

			outRec1.ZRN_SEC_NM_3 = StringUtil.changBlankToDash(sis.readString(80));
			outRec1.ZRN_SECTOR_NM_3 = StringUtil.changBlankToDash(sis.readString(100));
			outRec1.A_INSE_RATE1_3 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(7)));
			outRec1.A_INSE_RATE2_3 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(7)));
			outRec1.A_INSE_RATE3_3 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(7)));

			outRec1.ZRN_SEC_NM_4 = StringUtil.changBlankToDash(sis.readString(80));
			outRec1.ZRN_SECTOR_NM_4 = StringUtil.changBlankToDash(sis.readString(100));
			outRec1.A_INSE_RATE1_4 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(7)));
			outRec1.A_INSE_RATE2_4 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(7)));
			outRec1.A_INSE_RATE3_4 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(7)));
		//	outRec1List.add(out);
		//}
	}
	/* validate */
	@Override
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
