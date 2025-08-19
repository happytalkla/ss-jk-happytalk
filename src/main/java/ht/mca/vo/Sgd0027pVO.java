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

import lombok.Data;

public class Sgd0027pVO extends VOSupport {

	/* Input Records */
	private InRec1 inRec1 = new InRec1();

	/* Output Records */
	private OutRec1 outRec1 = new OutRec1();
	private ArrayList<OutRec2> outRec2 = new ArrayList<>();

	/* Get Instances */
	public InRec1 getInRec1() {
		return inRec1;
	}

	public OutRec1 getOutRec1() {
		return outRec1;
	}

	public ArrayList<OutRec2> getOutRec2() {
		return outRec2;
	}

	/* Set Instances */
	public void setInRec1(InRec1 inRec1) {
		this.inRec1 = inRec1;
	}

	public void setOutRec1(OutRec1 outRec1) {
		this.outRec1 = outRec1;
	}

	public void setOutRec2(ArrayList<OutRec2> outRec2) {
		this.outRec2 = outRec2;
	}

	/* InRec Classes */
	@Data
	public static class InRec1 {

		// Constructor
		public InRec1() {
			pText = null;
		}

		/* Attributes */
		/* Attributes */
		private String H_ia_rfrn_date1;
		private String H_ia_rfrn_date2;
		private String H_iclnt_enty_id;
		private String H_icrm_clnt_sect_code;
		private String H_iacti_ctnt;
		private String H_icnsd_clnt_icls_yn;
		private String H_iempy_no;
		private String H_icrm_tch_path_sect_code;
		private String H_itch_chnl_sect_code;
		private String H_ia_nxt_date;
		private String H_ia_len_6_cnti_rfrn_kyvl;
		private String H_ia_nxt_clnt_enty_id;
		private String H_ia_len_30_cnti_rfrn_kyvl;

		private String pText;

	}

	/* OutRec Classes */
	@Data
	public static class OutRec1 {

		// Constructor
		public OutRec1() {
		}

		/* Attributes */
		private String H_oa_nxt_date; // 다음일자 C[8]
		private String H_oa_len_6_cnti_rfrn_kyvl; // 길이6연속조회키값 C[6]
		private String H_oa_nxt_clnt_enty_id; // 다음고객엔티티ID C[19]
		private String H_oa_len_30_cnti_rfrn_kyvl; // 길이30연속조회키값 C[30]

		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("H_oa_nxt_date", H_oa_nxt_date);
			m.put("H_oa_len_6_cnti_rfrn_kyvl", H_oa_len_6_cnti_rfrn_kyvl);
			m.put("H_oa_nxt_clnt_enty_id", H_oa_nxt_clnt_enty_id);
			m.put("H_oa_len_30_cnti_rfrn_kyvl", H_oa_len_30_cnti_rfrn_kyvl);
			return m;
		}
	}

	public static class OutRec2 {

		// Constructor
		public OutRec2() {
		}

		/* Attributes */
		private String H_oa_sect_code;
		private String H_oclnt_cnsl_prhs_id;
		private String H_otch_date;
		private String H_otch_tm;
		private String H_oclnt_enty_id;
		private String H_oclnt_name;
		private String H_oacti_type_code;
		private String H_oacti_ttl_name;
		private String H_oprce_acti_ctnt;
		private String H_obuy_sell_type_code;
		private String H_oprdt_name;
		private String H_orcmn_resn_ctnt;
		private String H_ocrm_tch_path_sect_code;
		private String H_oonln_clnt_ract_code;
		private String H_oa_unsl_yn;
		private String H_otch_type_sect_code;
		private String H_oonln_tch_type_code;
		private String H_oonln_tch_rslt_code;
		private String H_opcpr_id;
		private String H_oprce_empy_name;

		/* toMap */
		public Map<String, Object> toMap() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("H_oa_sect_code", H_oa_sect_code);
			m.put("H_oclnt_cnsl_prhs_id", H_oclnt_cnsl_prhs_id);
			m.put("H_otch_date", H_otch_date);
			m.put("H_otch_tm", H_otch_tm);
			m.put("H_oclnt_enty_id", H_oclnt_enty_id);
			m.put("H_oclnt_name", H_oclnt_name);
			m.put("H_oacti_type_code", H_oacti_type_code);
			m.put("H_oacti_ttl_name", H_oacti_ttl_name);
			m.put("H_oprce_acti_ctnt", H_oprce_acti_ctnt);
			m.put("H_obuy_sell_type_code", H_obuy_sell_type_code);
			m.put("H_oprdt_name", H_oprdt_name);
			m.put("H_orcmn_resn_ctnt", H_orcmn_resn_ctnt);
			m.put("H_ocrm_tch_path_sect_code", H_ocrm_tch_path_sect_code);
			m.put("H_oonln_clnt_ract_code", H_oonln_clnt_ract_code);
			m.put("H_oa_unsl_yn", H_oa_unsl_yn);
			m.put("H_otch_type_sect_code", H_otch_type_sect_code);
			m.put("H_oonln_tch_type_code", H_oonln_tch_type_code);
			m.put("H_oonln_tch_rslt_code", H_oonln_tch_rslt_code);
			m.put("H_opcpr_id", H_opcpr_id);
			m.put("H_oprce_empy_name", H_oprce_empy_name);

			return m;
		}
	}

	/* constructor */
	public Sgd0027pVO(String ipAddr, String userID, String mediaType) {
		setHeaderUserID(userID);
		setHeaderTrCode("sgd0027p");
		setHeaderDestination((char) '6');
		setHeaderPacketDataType((char) 0);
		setHeaderMediaType(mediaType);
		setHeaderIpAddr(ipAddr);
		setCharSet("1");
		setIsActive("0");
	}

	/* constructor */
	public Sgd0027pVO(String ipAddr, String userID, String mediaType, String brchCD, String deptCD, String acctCD) {
		setHeaderUserID(userID);
		setHeaderTrCode("sgd0027p");
		setHeaderDestination((char) '6');
		setHeaderPacketDataType((char) 0);
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
				MCAUtil.writeString(inRec1.H_ia_rfrn_date1, 8, bos);
				MCAUtil.writeString(inRec1.H_ia_rfrn_date2, 8, bos);
				MCAUtil.writeString(inRec1.H_iclnt_enty_id, 19, bos);
				MCAUtil.writeString(inRec1.H_icrm_clnt_sect_code, 1, bos);
				MCAUtil.writeString(inRec1.H_iacti_ctnt, 450, bos);
				MCAUtil.writeString(inRec1.H_icnsd_clnt_icls_yn, 1, bos);
				MCAUtil.writeString(inRec1.H_iempy_no, 16, bos);
				MCAUtil.writeString(inRec1.H_icrm_tch_path_sect_code, 2, bos);
				MCAUtil.writeString(inRec1.H_itch_chnl_sect_code, 3, bos);
				MCAUtil.writeString(inRec1.H_ia_nxt_date, 8, bos);
				MCAUtil.writeString(inRec1.H_ia_len_6_cnti_rfrn_kyvl, 6, bos);
				MCAUtil.writeString(inRec1.H_ia_nxt_clnt_enty_id, 19, bos);
				MCAUtil.writeString(inRec1.H_ia_len_30_cnti_rfrn_kyvl, 30, bos);

			}
		}

		return bos.toByteArray();
	}

	/* parse */
	public void parse(StringInputStream sis) throws Exception{

			int cntRecord = 6;
			int bufLength = sis.getBufLength();
			
			sis.readString(4).trim();

			// OutRec1
			outRec1.H_oa_nxt_date = sis.readString(8);
			outRec1.H_oa_len_6_cnti_rfrn_kyvl = sis.readString(6);
			outRec1.H_oa_nxt_clnt_enty_id = sis.readString(19);
			outRec1.H_oa_len_30_cnti_rfrn_kyvl = sis.readString(30);

			for (int i = 0; i < cntRecord; i++) {

				if(bufLength <= sis.getPos())
					break;
				
				OutRec2 outRecord = new OutRec2();
				outRecord.H_oa_sect_code = sis.readString(1);
				outRecord.H_oclnt_cnsl_prhs_id = sis.readString(20);
				outRecord.H_otch_date = sis.readString(8);
				outRecord.H_otch_tm = sis.readString(6);
				outRecord.H_oclnt_enty_id = sis.readString(19);
				outRecord.H_oclnt_name = sis.readString(90);
				outRecord.H_oacti_type_code = sis.readString(2);
				outRecord.H_oacti_ttl_name = sis.readString(150);
				outRecord.H_oprce_acti_ctnt = sis.readString(1500);
				outRecord.H_obuy_sell_type_code = sis.readString(1);
				outRecord.H_oprdt_name = sis.readString(90);
				outRecord.H_orcmn_resn_ctnt = sis.readString(300);
				outRecord.H_ocrm_tch_path_sect_code = sis.readString(2);
				outRecord.H_oonln_clnt_ract_code = sis.readString(5);
				outRecord.H_oa_unsl_yn = sis.readString(1);
				outRecord.H_otch_type_sect_code = sis.readString(2);
				outRecord.H_oonln_tch_type_code = sis.readString(2);
				outRecord.H_oonln_tch_rslt_code = sis.readString(2);
				outRecord.H_opcpr_id = sis.readString(20);
				outRecord.H_oprce_empy_name = sis.readString(60);
				outRec2.add(outRecord);
			}

	}

	/* validate */
	public boolean validate() throws Exception {
		// implement here
		return true;
	}

	/* toString */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[inRec1]\n");
		sb.append("\t" + inRec1.toString() + "\n");
		sb.append("\n");

		sb.append("[outRec1]\n");
		sb.append("\t" + outRec1.toString() + "\n");

		sb.append("[outRec2]\n");
		sb.append("\t" + outRec2.toString() + "\n");

		return sb.toString();
	}

	/* toMap */
	public Map<String, Object> toMap() {
		Map<String, Object> m = new HashMap<String, Object>();

		m.put("outRec1", outRec1.toMap());

		List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < outRec2.size(); i++) {
			list2.add(outRec2.get(i).toMap());
		}
		m.put("outRec2", list2);

		return m;
	}
}
