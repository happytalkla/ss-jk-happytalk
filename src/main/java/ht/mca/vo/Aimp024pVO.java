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
import lombok.Getter;
/**
 * ELS 기준자산, 관련된 가격정보 가져오는 함수
 * @author TD5181
 *
 */
public class Aimp024pVO extends VOSupport{
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
		private String A_CHK_CODE;		// 체크코드	1, 2, 3, 4
		private String PRDT_TYPE_CODE;	// 상품유형코드	 02로 동일
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
		String PRCE_YN;			//처리여부
		String UNAS_DSCN_CTNT;	//타입별 내용
		@Getter
		String UNAS_DSCN_CTNT_LABEL;	//타입별 내용의 라벨
		
		// Constructor
		public OutRec1() {
		}

		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("PRCE_YN", PRCE_YN);
			m.put("UNAS_DSCN_CTNT", UNAS_DSCN_CTNT);
			m.put("UNAS_DSCN_CTNT_LABEL", UNAS_DSCN_CTNT_LABEL);
			return m;
		}
		
		public void setUNAS_DSCN_CTNT_LABEL(String chkCode) {
			this.UNAS_DSCN_CTNT_LABEL="no label";
			if("1".equals(chkCode)) {
				this.UNAS_DSCN_CTNT_LABEL = "기초자산";
			}else if("2".equals(chkCode)) {
				this.UNAS_DSCN_CTNT_LABEL = "최초기준가격";			
			}else if("3".equals(chkCode)) {
				this.UNAS_DSCN_CTNT_LABEL = "최종기준가격";
			}else if("4".equals(chkCode)) {
				this.UNAS_DSCN_CTNT_LABEL = "관련가격";
			}
		}		
	}
	
	/* constructor */
	public Aimp024pVO (String ipAddr, String userID, String mediaType) {
		setHeaderUserID(userID);
		setHeaderTrCode("aimp024p");
		setHeaderDestination((char)'6');
		setHeaderPacketDataType((char)0);
		setHeaderMediaType(mediaType);
		setHeaderIpAddr(ipAddr);
		setCharSet("1");
        setIsActive("0");		
	}

	/* constructor */
	public Aimp024pVO (String ipAddr, String userID, String mediaType, String brchCD, String deptCD, String acctCD) {
		setHeaderUserID(userID);
		setHeaderTrCode("aimp024p");
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
				MCAUtil.writeString(inRec1.A_CHK_CODE, 1, bos);
				MCAUtil.writeString(inRec1.PRDT_TYPE_CODE, 2, bos);
				MCAUtil.writeString(inRec1.ISCD, 12, bos);
			}
		}
		
		return bos.toByteArray();
	}
		
	/* parse */
	public void parse(StringInputStream sis) throws Exception {
		
		// OutRec1
		outRec1.PRCE_YN = StringUtil.changBlankToDash(sis.readString(1));
		outRec1.UNAS_DSCN_CTNT = StringUtil.changBlankToDash(sis.readString(1500));
		outRec1.setUNAS_DSCN_CTNT_LABEL(inRec1.getA_CHK_CODE());
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
