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
 * 펀드 동일유형 수익률
 * @author TD5181
 *
 */
public class Igv9253pVO extends VOSupport{

	/* Input Records */
	private InRec1 inRec1 = new InRec1();

	/* Output Records */
	private OutRec1 outRec1 = new OutRec1();
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

		// Constructor
		public OutRec1() {
		}


		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("STNR_DATE", STNR_DATE );
			return m;
		}
	}
	/* OutRec Classes */
	@Data
	public static class OutRec2 {

		/* Attributes */
		private String ZRN_M1_RTN_SIZE;
		private String ZRN_M1_RTN_0;
		private String ZRN_M3_RTN_0;
		private String ZRN_M6_RTN_0;
		private String ZRN_M12_RTN_0;
		private String ZRN_ESTAB_RTN_0;
		private String ZRN_M1_RTN_1;
		private String ZRN_M3_RTN_1;
		private String ZRN_M6_RTN_1;
		private String ZRN_M12_RTN_1;
		private String ZRN_ESTAB_RTN_1;
		private String ZRN_M1_RTN_2;
		private String ZRN_M3_RTN_2;
		private String ZRN_M6_RTN_2;
		private String ZRN_M12_RTN_2;
		private String ZRN_ESTAB_RTN_2;
		// Constructor
		public OutRec2() {
		}


		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("ZRN_M1_RTN_SIZE", ZRN_M1_RTN_SIZE );
			m.put("ZRN_M1_RTN_0", ZRN_M1_RTN_0 );
			m.put("ZRN_M3_RTN_0", ZRN_M3_RTN_0 );
			m.put("ZRN_M6_RTN_0", ZRN_M6_RTN_0 );
			m.put("ZRN_M12_RTN_0", ZRN_M12_RTN_0 );
			m.put("ZRN_ESTAB_RTN_0", ZRN_ESTAB_RTN_0 );
			m.put("ZRN_M1_RTN_1", ZRN_M1_RTN_1 );
			m.put("ZRN_M3_RTN_1", ZRN_M3_RTN_1 );
			m.put("ZRN_M6_RTN_1", ZRN_M6_RTN_1 );
			m.put("ZRN_M12_RTN_1", ZRN_M12_RTN_1 );
			m.put("ZRN_ESTAB_RTN_1", ZRN_ESTAB_RTN_1 );
			m.put("ZRN_M1_RTN_2", ZRN_M1_RTN_2 );
			m.put("ZRN_M3_RTN_2", ZRN_M3_RTN_2 );
			m.put("ZRN_M6_RTN_2", ZRN_M6_RTN_2 );
			m.put("ZRN_M12_RTN_2", ZRN_M12_RTN_2 );
			m.put("ZRN_ESTAB_RTN_2", ZRN_ESTAB_RTN_2 );
			return m;
		}
	}

	/* constructor */
	public Igv9253pVO (String ipAddr, String userID, String mediaType) {
		setHeaderUserID(userID);
		setHeaderTrCode("igv9253p");
		setHeaderDestination('6');
		setHeaderPacketDataType((char)0);
		setHeaderMediaType(mediaType);
		setHeaderIpAddr(ipAddr);
		setCharSet("1");
        setIsActive("0");
	}

	/* constructor */
	public Igv9253pVO (String ipAddr, String userID, String mediaType, String brchCD, String deptCD, String acctCD) {
		setHeaderUserID(userID);
		setHeaderTrCode("igv9253p");
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
				MCAUtil.writeString(inRec1.ZRN_FUND_CD, 12, bos);
			}
		}

		return bos.toByteArray();
	}

	/* parse */
	@Override
	public void parse(StringInputStream sis) throws Exception {

		int cntRecord=3;

		// OutRec1
		outRec1.STNR_DATE	 = StringUtil.changBlankToDash(sis.readString(8));
		//for(int i=0; i<cntRecord; i++) {
		//	OutRec2 out = new OutRec2();
		sis.readString(4);
		outRec2.ZRN_M1_RTN_0	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));
		outRec2.ZRN_M3_RTN_0	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));
		outRec2.ZRN_M6_RTN_0	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));
		outRec2.ZRN_M12_RTN_0	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));
		outRec2.ZRN_ESTAB_RTN_0	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));

		outRec2.ZRN_M1_RTN_1	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));
		outRec2.ZRN_M3_RTN_1	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));
		outRec2.ZRN_M6_RTN_1	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));
		outRec2.ZRN_M12_RTN_1	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));
		outRec2.ZRN_ESTAB_RTN_1	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));

		outRec2.ZRN_M1_RTN_2	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));
		outRec2.ZRN_M3_RTN_2	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));
		outRec2.ZRN_M6_RTN_2	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));
		outRec2.ZRN_M12_RTN_2	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));
		outRec2.ZRN_ESTAB_RTN_2	 = StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(19)));
		//	outRec2List.add(out);
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
		m.putAll(outRec2.toMap());

		return m;
	}
}