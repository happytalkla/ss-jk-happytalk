package ht.mca.vo;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ss.common.channel.mca.common.net.StringInputStream;
import com.ss.common.channel.mca.util.MCAUtil;
import com.ss.common.channel.mca.vo.VOSupport;
import com.ss.common.util.StringUtility;

import ht.util.StringUtil;
import lombok.Data;

public class Pga0011pVO extends VOSupport {

	/* Input Records */
	private InRec1 inRec1 = new InRec1();

	/* Output Records */
	private OutRec1 outRec1 = new OutRec1();
	private ArrayList<OutRec2> outRec2List = new ArrayList<>();
	
	/* Get Instances */
	public InRec1 getInRec1() { return inRec1; }
	public OutRec1 getOutRec1() { return outRec1; }
	public ArrayList<OutRec2> getOutRec2List() { return outRec2List; }

	/* Set Instances */
	public void setInRec1(InRec1 inRec1) { this.inRec1 = inRec1; }
	public void setOutRec1(OutRec1 outRec1) { this.outRec1 = outRec1; }
	public void setOutRec2(ArrayList<OutRec2> outRec2List) { this.outRec2List = outRec2List; }

	/* InRec Classes */	
	@Data
	public static class InRec1 {
		
		// Constructor
		public InRec1() {
			pText = null;
		}
	
		/* Attributes */
		private String ANCT_DATE;
		private String CLNT_SECT_CODE;		
		private String ACNT_TYPE_CODE;		
		private String A_STRT_MTRY_DATE;		
		private String A_END_MTRY_DATE;		
		private String YILD_SECT_CODE;		
		private String STNR_YILD;		
		private String BOND_PRDT_SECT_CODE;		
		private String STND_ISCD;		
		private String A_ANCT_SECT_CODE;		
		private String A_LEN_27_CNTI_RFRN_KYVL;			
	
		private String pText;

	}

	/* OutRec Classes */
	@Data
	public static class OutRec1 { 
	
		// Constructor
		public OutRec1() {
		}
		
		/* Attributes */
		private String A_LEN_27_CNTI_RFRN_KYVL;     //길이27연속조회키값 C[27]
 
		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("A_LEN_27_CNTI_RFRN_KYVL", A_LEN_27_CNTI_RFRN_KYVL);			
			return m;
		}
	}
	
	@Data
	public static class OutRec2 { 
		// Constructor
		public OutRec2() {
		}

		/* Attributes */
		private String STND_ISCD;
		private String PRDT_ID;
		private String ISUS_ABBR_NAME;
		private String BKCV_YILD;					//은행환산 수익률, 세전수익률
		private String BFTX_YILD;
		private String DLNG_ANCT_AFTX_YILD;			//매매공시 수익률, 세후수익률
		private String SPRN_TXTN_AFTE_YILD;
		private String A_RMN_MMS_NAME;
		private String A_ANCT_BLNC_QNTY;
		private String DLNG_ANCT_YILD;
		private String STNR_QNTY;
		private String DLNG_ANCT_UNIT_PRIC;
		private String A_BFTX_AFTX_SECT_NAME;
		private String XPRN_DATE;
		private String ISSD_DATE;
		private String ISSD_INTT_RATE;
		private String STLT_DATE;
		private String ANCT_TYPE_NAME;
		private String A_BUY_ACNT_NO;
		private String BUY_ACNT_ID;
		private String A_ACNT_TYPE_NAME;
		private String ACNT_NAME;
		private String STRT_STNR_QNTY;
		private String A_END_STNR_QNTY;
		private String DTBR_CODE;
		private String STLT_SECT_CODE;
		private String A_FNPR_MRGN_APLY_YN;
		private String A_ANCT_SECT_CODE;
		private String CIA_YN;
		private String A_DLNG_TAGT_SECT_CODE;
		private String MINM_ANCT_YILD;
		private String MAXM_ANCT_YILD;
		private String SPRN_TXTN_AFTX_YILD;
		private String ENTR_DLNG_ANCT_AFTX_YILD;
		private String INTT_PYMT_TYPE_CODE;
		private String DPSD_TRDG_UNIT_AMNT;
		private String QRTR_QNTY;
		private String QRTR_YN;

		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("STND_ISCD", STND_ISCD);
			m.put("PRDT_ID", PRDT_ID);
			m.put("ISUS_ABBR_NAME", ISUS_ABBR_NAME);
			m.put("BKCV_YILD", BKCV_YILD);
			m.put("BFTX_YILD", BFTX_YILD);
			m.put("DLNG_ANCT_AFTX_YILD", DLNG_ANCT_AFTX_YILD);
			m.put("SPRN_TXTN_AFTE_YILD", SPRN_TXTN_AFTE_YILD);
			m.put("A_RMN_MMS_NAME", A_RMN_MMS_NAME);
			m.put("A_ANCT_BLNC_QNTY", A_ANCT_BLNC_QNTY);
			m.put("DLNG_ANCT_YILD", DLNG_ANCT_YILD);
			m.put("STNR_QNTY", STNR_QNTY);
			m.put("DLNG_ANCT_UNIT_PRIC", DLNG_ANCT_UNIT_PRIC);
			m.put("A_BFTX_AFTX_SECT_NAME", A_BFTX_AFTX_SECT_NAME);
			m.put("XPRN_DATE", XPRN_DATE);
			m.put("ISSD_DATE", ISSD_DATE);
			m.put("ISSD_INTT_RATE", ISSD_INTT_RATE);
			m.put("STLT_DATE", STLT_DATE);
			m.put("ANCT_TYPE_NAME", ANCT_TYPE_NAME);
			m.put("A_BUY_ACNT_NO", A_BUY_ACNT_NO);
			m.put("BUY_ACNT_ID", BUY_ACNT_ID);
			m.put("A_ACNT_TYPE_NAME", A_ACNT_TYPE_NAME);
			m.put("ACNT_NAME", ACNT_NAME);
			m.put("STRT_STNR_QNTY", STRT_STNR_QNTY);
			m.put("A_END_STNR_QNTY", A_END_STNR_QNTY);
			m.put("DTBR_CODE", DTBR_CODE);
			m.put("STLT_SECT_CODE", STLT_SECT_CODE);
			m.put("A_FNPR_MRGN_APLY_YN", A_FNPR_MRGN_APLY_YN);
			m.put("A_ANCT_SECT_CODE", A_ANCT_SECT_CODE);
			m.put("CIA_YN", CIA_YN);
			m.put("A_DLNG_TAGT_SECT_CODE", A_DLNG_TAGT_SECT_CODE);
			m.put("MINM_ANCT_YILD", MINM_ANCT_YILD);
			m.put("MAXM_ANCT_YILD", MAXM_ANCT_YILD);
			m.put("SPRN_TXTN_AFTX_YILD", SPRN_TXTN_AFTX_YILD);
			m.put("ENTR_DLNG_ANCT_AFTX_YILD", ENTR_DLNG_ANCT_AFTX_YILD);
			m.put("INTT_PYMT_TYPE_CODE", INTT_PYMT_TYPE_CODE);
			m.put("DPSD_TRDG_UNIT_AMNT", DPSD_TRDG_UNIT_AMNT);
			m.put("QRTR_QNTY", QRTR_QNTY);
			m.put("QRTR_YN", QRTR_YN);
			
			return m;
		}
	}
	
	/* constructor */
	public Pga0011pVO (String ipAddr, String userID, String mediaType) {
		setHeaderUserID(userID);
		setHeaderTrCode("pga0011p");
		setHeaderDestination((char)'6');
		setHeaderPacketDataType((char)0);
		setHeaderMediaType(mediaType);
		setHeaderIpAddr(ipAddr);
		setCharSet("1");
        setIsActive("0");		
	}

	/* constructor */
	public Pga0011pVO (String ipAddr, String userID, String mediaType, String brchCD, String deptCD, String acctCD) {
		setHeaderUserID(userID);
		setHeaderTrCode("pga0011p");
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
				MCAUtil.writeString(inRec1.ANCT_DATE, 8, bos);
				MCAUtil.writeString(inRec1.CLNT_SECT_CODE, 1, bos);
				MCAUtil.writeString(inRec1.ACNT_TYPE_CODE, 4, bos);
				MCAUtil.writeString(inRec1.A_STRT_MTRY_DATE, 8, bos);
				MCAUtil.writeString(inRec1.A_END_MTRY_DATE, 8, bos);
				MCAUtil.writeString(inRec1.YILD_SECT_CODE, 1, bos);
				MCAUtil.writeString(inRec1.STNR_YILD, 11, bos);
				MCAUtil.writeString(inRec1.BOND_PRDT_SECT_CODE, 1, bos);
				MCAUtil.writeString(inRec1.STND_ISCD, 12, bos);
				MCAUtil.writeString(inRec1.A_ANCT_SECT_CODE, 1, bos);
				MCAUtil.writeString(inRec1.A_LEN_27_CNTI_RFRN_KYVL, 27, bos);
			}
		}
		
		return bos.toByteArray();
	}
		
	/* parse */
	public void parse(StringInputStream sis) throws Exception {
		
		int cntRecord= 1;
		int bufLength = sis.getBufLength();
		// OutRec1
		outRec1.A_LEN_27_CNTI_RFRN_KYVL = StringUtil.changBlankToDash(sis.readString(27));
		sis.readString(4).trim();
		// OutRec2

		for (int i=0;i<cntRecord;i++) {

			if(bufLength <= sis.getPos())
				break;
			
			OutRec2 outRecord = new OutRec2();
			outRecord.STND_ISCD					= StringUtil.changBlankToDash(sis.readString(12));
			outRecord.PRDT_ID					= StringUtil.changBlankToDash(sis.readString(19));
			outRecord.ISUS_ABBR_NAME			= StringUtil.changBlankToDash(sis.readString(60));
			outRecord.BKCV_YILD					= StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(11)));
			outRecord.BFTX_YILD					= StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(11)));
			outRecord.DLNG_ANCT_AFTX_YILD		= StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(11)));
			outRecord.SPRN_TXTN_AFTE_YILD		= StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(11)));
			outRecord.A_RMN_MMS_NAME			= StringUtil.changBlankToDash(sis.readString(30));
			outRecord.A_ANCT_BLNC_QNTY			= StringUtil.changBlankToDash(sis.readString(18));
			outRecord.DLNG_ANCT_YILD			= StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(11)));
			outRecord.STNR_QNTY					= StringUtil.changBlankToDash(sis.readString(16));
			outRecord.DLNG_ANCT_UNIT_PRIC		= StringUtil.changBlankToDash(sis.readString(14));
			outRecord.A_BFTX_AFTX_SECT_NAME		= StringUtil.changBlankToDash(sis.readString(6));
			outRecord.XPRN_DATE					= StringUtil.changBlankToDash(sis.readString(8));
			outRecord.ISSD_DATE					= StringUtil.changBlankToDash(sis.readString(8));  
			outRecord.ISSD_INTT_RATE			= StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(11))); 
			outRecord.STLT_DATE					= StringUtil.changBlankToDash(sis.readString(8));   
			outRecord.ANCT_TYPE_NAME			= StringUtil.changBlankToDash(sis.readString(60));  
			outRecord.A_BUY_ACNT_NO				= StringUtil.changBlankToDash(sis.readString(20));  
			outRecord.BUY_ACNT_ID				= StringUtil.changBlankToDash(sis.readString(19));  
			outRecord.A_ACNT_TYPE_NAME			= StringUtil.changBlankToDash(sis.readString(90));  
			outRecord.ACNT_NAME					= StringUtil.changBlankToDash(sis.readString(90));  
			outRecord.STRT_STNR_QNTY			= StringUtil.changBlankToDash(sis.readString(16));  
			outRecord.A_END_STNR_QNTY			= StringUtil.changBlankToDash(sis.readString(16));  
			outRecord.DTBR_CODE					= StringUtil.changBlankToDash(sis.readString(5));   
			outRecord.STLT_SECT_CODE			= StringUtil.changBlankToDash(sis.readString(2));   
			outRecord.A_FNPR_MRGN_APLY_YN		= StringUtil.changBlankToDash(sis.readString(1));   
			outRecord.A_ANCT_SECT_CODE			= StringUtil.changBlankToDash(sis.readString(1));   
			outRecord.CIA_YN					= StringUtil.changBlankToDash(sis.readString(1));   
			outRecord.A_DLNG_TAGT_SECT_CODE		= StringUtil.changBlankToDash(sis.readString(1));   
			outRecord.MINM_ANCT_YILD			= StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(11)));  
			outRecord.MAXM_ANCT_YILD			= StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(11)));  
			outRecord.SPRN_TXTN_AFTX_YILD		= StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(11)));  
			outRecord.ENTR_DLNG_ANCT_AFTX_YILD	= StringUtil.changBlankToDash(StringUtil.changeStrToDouble(sis.readString(11)));  
			outRecord.INTT_PYMT_TYPE_CODE		= StringUtil.changBlankToDash(sis.readString(4));   
			outRecord.DPSD_TRDG_UNIT_AMNT		= StringUtil.changBlankToDash(sis.readString(18));   
			outRecord.QRTR_QNTY					= StringUtil.changBlankToDash(sis.readString(16));  
			outRecord.QRTR_YN					= StringUtil.changBlankToDash(sis.readString(1));  

			outRec2List.add(outRecord);
		}

	}
	/* validate */
	public boolean validate() throws Exception {
		//implement here
		return true;
	}

	/* toString */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[inRec1]\n");
		sb.append("\t"+inRec1.toString()+"\n");
		sb.append("\n");
		
		sb.append("[outRec1]\n");
		sb.append("\t"+outRec1.toString()+"\n");
		sb.append("[outRec2]\n");
		for (int i=0;i<outRec2List.size();i++) {
			sb.append("\t"+outRec2List.get(i).toString()+"\n");
		}

		return sb.toString();
	}

	/* toMap */
	public Map<String, Object> toMap() {
		Map<String, Object> m = new HashMap<String, Object>();
		m.putAll(outRec1.toMap());

		if(outRec2List.size()>0) {
			m.putAll(outRec2List.get(0).toMap());
		}

		return m;
	}
}
