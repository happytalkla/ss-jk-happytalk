package ht.service;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.mca.MCALib;

import ht.constants.CommonConstants;
import ht.domain.MemberVO;
import ht.mca.vo.Aigp015pVO;
import ht.mca.vo.Aimp024pVO;
import ht.mca.vo.Aimp02gpVO;
import ht.mca.vo.Bsu0200pVO;
import ht.mca.vo.Haa0421pVO;
import ht.mca.vo.Hfca_oa_105aq01VO;
import ht.mca.vo.Igv2001pVO;
import ht.mca.vo.Igv4205pVO;
import ht.mca.vo.Igv4206pVO;
import ht.mca.vo.Igv9251pVO;
import ht.mca.vo.Igv9252pVO;
import ht.mca.vo.Igv9253pVO;
import ht.mca.vo.Mag0081pVO;
import ht.mca.vo.Mah0363pVO;
import ht.mca.vo.Mah0368pVO;
import ht.mca.vo.Pfdz101pVO;
import ht.mca.vo.Pfdz102pVO;
import ht.mca.vo.Pfdz103pVO;
import ht.mca.vo.Pga0011pVO;
import ht.mca.vo.Sdb1624pVO;
import ht.mca.vo.Sgd0027pVO;
import ht.mca.vo.Sgd0038pVO;
import ht.mca.vo.Sgd1611pVO;
import ht.mca.vo.Sgd1616pVO;
import ht.mca.vo.Sge1011pVO;
import ht.mca.vo.Sge1012pVO;
import ht.mca.vo.Sge9901pVO;
import ht.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class McaService {

	/**
	 * programType 	: 고객 ID변환
	 * programId 	: bsu0200p
	 */
	private String channelCd = CommonConstants.MCA_CHANNEL_CODE;

	@Transactional
	public Map<String, Object> bsu0200p(@NotNull String entityId, @NotNull String channelCode) throws Exception {

		String localhost = InetAddress.getLocalHost().getHostAddress();

		Bsu0200pVO bsu0200pVO = new Bsu0200pVO(localhost, entityId, channelCode);
		bsu0200pVO.getInRec1().setData("2", entityId, "");
		bsu0200pVO = (Bsu0200pVO) MCALib.call(bsu0200pVO, false);

		Map<String, Object> result = bsu0200pVO.toMap();
		return result;
	}
	/*
	 *  ENTITY ID로 고객번호 반환
	 */
	@Transactional
	public Map<String, Object> sge9901p(@NotNull String entityId, @NotNull String channelCode) throws Exception {
		MemberVO memberVO = MemberAuthService.getCurrentUser();
		String localhost = InetAddress.getLocalHost().getHostAddress();

		Sge9901pVO vo = new Sge9901pVO(localhost, memberVO.getId(), channelCode);
		vo.getInRec1().setA_PRCE_SECT_CODE("3");
		vo.getInRec1().setRBNO("");
		vo.getInRec1().setENTY_ID(entityId);
		vo.getInRec1().setACNT_NO("");
		vo = (Sge9901pVO) MCALib.call(vo, false);

		Map<String, Object> result = vo.getOutRec1().toMap();
		return result;
	}

	/**
	 * programType 	: 고객기본정보
	 * programId 	: sgd1611p
	 */
	@Transactional
	public Map<String, Object> sgd1611p(@NotNull String entityId, @NotNull String channelCode) throws Exception {
		if(StringUtil.isEmpty(entityId))
			return null;
		String localhost = InetAddress.getLocalHost().getHostAddress();
		Map<String, Object> result = new HashMap<>();
		Sgd1611pVO sgd1611pVO = new Sgd1611pVO(localhost, entityId, channelCode);
		sgd1611pVO.getInRec1().setData(entityId);
		sgd1611pVO = (Sgd1611pVO) MCALib.call(sgd1611pVO, false);
		result.put("errFlag", sgd1611pVO.getTrErrorFlag());
		result.put("errCode", sgd1611pVO.getTrErrorCode());

		result.putAll(sgd1611pVO.getOutRec1().toMap());
		return result;
	}

	/**
	 * programType 	: 고객유형정보
	 * programId 	: mah0368p
	 */
	@Transactional
	public Map<String, Object> mah0368p(@NotNull String entityId, @NotNull String channelCode) throws Exception {

		String localhost = InetAddress.getLocalHost().getHostAddress();

		Mah0368pVO mah0368pVO = new Mah0368pVO(localhost, entityId, channelCode);
		mah0368pVO.getInRec1().setData("", "", entityId);
		mah0368pVO = (Mah0368pVO) MCALib.call(mah0368pVO, false);
		Map<String, Object> result = mah0368pVO.getOutRec1().toMap();
		return result;
	}

	/**
	 * programType 	: 고객유형정보
	 * programId 	: mah0363p
	 */
	@Transactional
	public Map<String, Object> mah0363p(@NotNull String entityId, @NotNull String channelCode) throws Exception {
		MemberVO memberVO = MemberAuthService.getCurrentUser();
		String localhost = InetAddress.getLocalHost().getHostAddress();

		Mah0363pVO mah0363pVO = new Mah0363pVO(localhost, memberVO.getId(), channelCode);
		mah0363pVO.getInRec1().setData("", "", entityId);
		mah0363pVO = (Mah0363pVO) MCALib.call(mah0363pVO, false);
		Map<String, Object> result = mah0363pVO.getOutRec1().toMap();
		return result;
	}

	/**
	 * programType 	: 고객유형정보
	 * programId 	: sdb1624p
	 */
	@Transactional
	public Map<String, Object> sdb1624p(@NotNull String entityId, @NotNull String channelCode) throws Exception {

		String localhost = InetAddress.getLocalHost().getHostAddress();

		Sdb1624pVO sdb1624pVO = new Sdb1624pVO(localhost, entityId, channelCode);
		sdb1624pVO.getInRec1().setData(entityId);
		sdb1624pVO = (Sdb1624pVO) MCALib.call(sdb1624pVO, false);
		Map<String, Object> result = sdb1624pVO.getOutRec1().toMap();
		return result;
	}

	/**
	 * programType 	: 고객약정정보
	 * programId 	: mag0081p
	 */
	@Transactional
	public Map<String, Object> mag0081p(@NotNull String entityId, String acntNo) throws Exception {
		MemberVO memberVO = MemberAuthService.getCurrentUser();
		//계좌정보 가져오기
		String localhost = InetAddress.getLocalHost().getHostAddress();
		Map<String, Object> result = new HashMap<String, Object>();

		Mag0081pVO v = new Mag0081pVO(localhost, memberVO.getId(), CommonConstants.MCA_CHANNEL_CODE, memberVO.getOdtbrCode(), "", "");
		v.getInRec1().setACNT_NO(acntNo);
		v.getInRec1().setCLNT_RNNO("");
		v.getInRec1().setA_PSWD_CRYP(memberVO.getHonorsPwd());
		v.getInRec1().setA_ACNT_STAS_DTLS_CODE("1");
		v.getInRec1().setA_NXT_ACNT_ID("");

		v = (Mag0081pVO) MCALib.call(v, false);

		result.putAll(v.toMap());

		return result;
	}

	/**
	 * programType 	: 계좌정보
	 * programId 	: sgd1616p
	 */
	@Transactional
	public Map<String, Object> sgd1616p(@NotNull String entityId, @NotNull String channelCode) throws Exception {

		String localhost = InetAddress.getLocalHost().getHostAddress();

		Sgd1616pVO sgd1616pVO = new Sgd1616pVO(localhost, "", channelCode);
		sgd1616pVO.getInRec1().setCRM_CLNT_ID(entityId);
		sgd1616pVO.getInRec1().setACNT_STAS_TYPE_CODE("AC");
		sgd1616pVO.getInRec1().setA_LEN_10_CNTI_RFRN_KYVL("0");
		try {
		sgd1616pVO = (Sgd1616pVO) MCALib.call(sgd1616pVO, false);
		}catch (Exception e) {
			// TODO: handle exception
			log.info("Mca err : {}", e.toString());
		}
		return sgd1616pVO==null? new HashMap<>(): sgd1616pVO.toMap();
	}

	/**
	 * programType 	: 계좌정보d
	 * programId 	: maa0421p
	 */
	@Transactional
	public Map<String, Object> maa0421p(@NotNull String entityId, @NotNull String channelCode, String acntNo) throws Exception {
		MemberVO memberVO = MemberAuthService.getCurrentUser();
		String localhost = InetAddress.getLocalHost().getHostAddress();

		Haa0421pVO maa0421pVO = new Haa0421pVO(localhost, memberVO.getId(), channelCode);
		maa0421pVO.getInRec1().setData(acntNo, memberVO.getHonorsPwd());
		maa0421pVO = (Haa0421pVO) MCALib.call(maa0421pVO, false);
		Map<String, Object> result = maa0421pVO.getOutRec1().toMap();
		return result;
	}

	/**
	 * programType 	: 접촉정보
	 * programId 	: sgd0027p
	 */
	@Transactional
	public ArrayList<Map<String, Object>> sgd0027p(@NotNull String entityId, @NotNull String channelCode) throws Exception {
		MemberVO memberVO = MemberAuthService.getCurrentUser();
		String localhost = InetAddress.getLocalHost().getHostAddress();
		ArrayList<Map<String, Object>> list = new ArrayList<>();

		//최근 3개월
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		String endDt = sdf.format(cal.getTime());	//검색 종료 일자

		cal.add(Calendar.MONTH, -3);
		String startDt = sdf.format(cal.getTime());	//검색 시작 일자

		Sgd0027pVO vo = new Sgd0027pVO(localhost, memberVO.getId(), channelCode);
		vo.getInRec1().setH_ia_rfrn_date1(startDt);
		vo.getInRec1().setH_ia_rfrn_date2(endDt);
		vo.getInRec1().setH_iclnt_enty_id(entityId);
		vo.getInRec1().setH_icrm_clnt_sect_code("");
		vo.getInRec1().setH_iacti_ctnt("");
		vo.getInRec1().setH_icnsd_clnt_icls_yn("");
		vo.getInRec1().setH_iempy_no("");
		vo.getInRec1().setH_icrm_tch_path_sect_code("00");
		vo.getInRec1().setH_itch_chnl_sect_code("");
		vo.getInRec1().setH_ia_nxt_date("99991231");
		vo.getInRec1().setH_ia_len_6_cnti_rfrn_kyvl("999999");
		vo.getInRec1().setH_ia_nxt_clnt_enty_id("0");
		vo.getInRec1().setH_ia_len_30_cnti_rfrn_kyvl("0");

		vo = (Sgd0027pVO) MCALib.call(vo, false);

		list = (ArrayList<Map<String, Object>>)vo.toMap().get("outRec2");
		return list;
	}

	/**
	 * programType 	: 고객정보
	 * programId 	: hfca_oa_105aq01
	 */
	@Transactional
	public Map<String, Object>hfca_oa_105aq01(@NotNull String entityId, @NotNull String channelCode, String custNo) throws Exception {

		String localhost = InetAddress.getLocalHost().getHostAddress();

		Hfca_oa_105aq01VO hfca_oa_105aq01VO = new Hfca_oa_105aq01VO(localhost, entityId, channelCode);
		hfca_oa_105aq01VO.getInRec1().setData("2", "", custNo, "");
		hfca_oa_105aq01VO = (Hfca_oa_105aq01VO) MCALib.call(hfca_oa_105aq01VO, false);

		return hfca_oa_105aq01VO.getOutRec1().toMap();
	}
	/**
	 * programType 	: 상품전환 프로그램
	 * programId 	: pfdz101p
	 */

	public Map<String, Object> pfdz101p(@NotNull String entityId, String PRDT_ID) throws Exception {

		String localhost = InetAddress.getLocalHost().getHostAddress();

		Pfdz101pVO vo = new Pfdz101pVO(localhost, entityId, channelCd);
		vo.getInRec1().setPRDT_ID(PRDT_ID);
		vo = (Pfdz101pVO) MCALib.call(vo, false);
		Map<String, Object> result = new HashMap<>();
		result = vo.getOutRec1().toMap();

		return result;
	}
	/**
	 * programType 	: 아너스넷 비밀번호 암호화
	 * programId 	: pfdz102p
	 */

	public Map<String, Object> pfdz102p(@NotNull String entityId, String H_iuser_id, String H_iuser_pswd) throws Exception {
		String localhost = InetAddress.getLocalHost().getHostAddress();

		Pfdz102pVO vo = new Pfdz102pVO(localhost, entityId, channelCd);

		vo.getInRec1().setH_iuser_id(H_iuser_id);
		vo.getInRec1().setH_iuser_pswd(H_iuser_pswd);

		vo = (Pfdz102pVO) MCALib.call(vo, false);
		Map<String, Object> result = new HashMap<>();
		result = vo.getOutRec1().toMap();

		return result;
	}
	/**
	 * programType 	: 아너스넷 비밀번호 암호화 검증
	 * programId 	: pfdz103p
	 */

	public Map<String, Object> pfdz103p(@NotNull String entityId, String H_iuser_id, String H_ipswd_cryp) throws Exception {
		String localhost = InetAddress.getLocalHost().getHostAddress();

		Pfdz103pVO vo = new Pfdz103pVO(localhost, entityId, channelCd);
		vo.getInRec1().setH_iuser_id(H_iuser_id);
		vo.getInRec1().setH_ipswd_cryp(H_ipswd_cryp);
		vo = (Pfdz103pVO) MCALib.call(vo, false);
		Map<String, Object> result = new HashMap<>();
		result = vo.getOutRec1().toMap();

		return result;
	}

	/**
	 * programType 	: 펀드1
	 * programId 	: igv2001p
	 */

	public Map<String, Object> igv2001p(@NotNull String entityId, @NotNull String zrnFundCd, @NotNull String yyyyMMdd, @NotNull String zrnZrnCd) throws Exception {

		String localhost = InetAddress.getLocalHost().getHostAddress();

		Igv2001pVO vo = new Igv2001pVO(localhost, entityId, channelCd);
		vo.getInRec1().setZRN_FUND_CD(zrnFundCd);
		vo.getInRec1().setZRN_DT(yyyyMMdd);
		vo.getInRec1().setFUND_CODE(zrnZrnCd);

		vo = (Igv2001pVO) MCALib.call(vo, false);
		Map<String, Object> result = new HashMap<>();
		result = vo.toMap();
		return result;
	}

	/**
	 * programType 	: 펀드2-1
	 * programId 	: igv9251p
	 */

	public Map<String, Object> igv9251p(@NotNull String entityId, @NotNull String fundCd) throws Exception {

		String localhost = InetAddress.getLocalHost().getHostAddress();

		Igv9251pVO vo = new Igv9251pVO(localhost, "", channelCd);
		vo.getInRec1().setZRN_FUND_CD(fundCd);

		vo = (Igv9251pVO) MCALib.call(vo, false);
		Map<String, Object> result = new HashMap<>();
		result = vo.toMap();

		return result;
	}

	/**
	 * programType 	: 펀드2-2
	 * programId 	: igv9252p
	 */
	public Map<String, Object> igv9252p(@NotNull String entityId, @NotNull String zrnFundCd) throws Exception {

		String localhost = InetAddress.getLocalHost().getHostAddress();

		Igv9252pVO vo = new Igv9252pVO(localhost, entityId, channelCd);
		vo.getInRec1().setZRN_FUND_CD(zrnFundCd);

		vo = (Igv9252pVO) MCALib.call(vo, false);
		Map<String, Object> result = new HashMap<>();
		result = vo.toMap();

		return result;
	}

	/**
	 * programType 	: 펀드2-3
	 * programId 	: igv9253p
	 */
	public Map<String, Object> igv9253p(@NotNull String entityId, @NotNull String zrnFundCd) throws Exception {

		String localhost = InetAddress.getLocalHost().getHostAddress();

		Igv9253pVO vo = new Igv9253pVO(localhost, entityId, channelCd);
		vo.getInRec1().setZRN_FUND_CD(zrnFundCd);

		vo = (Igv9253pVO) MCALib.call(vo, false);
		Map<String, Object> result = new HashMap<>();
		result = vo.toMap();
		return result;
	}

	/**
	 * programType 	: 펀드3
	 * programId 	: igv4205p
	 */
	public Map<String, Object> igv4205p(@NotNull String entityId, @NotNull String yyyyMMdd, @NotNull String zrnFundCd, @NotNull String zrnZrnCd) throws Exception {

		String localhost = InetAddress.getLocalHost().getHostAddress();

		Igv4205pVO vo = new Igv4205pVO(localhost, entityId, channelCd);
		vo.getInRec1().setSTNR_DATE(yyyyMMdd);
		vo.getInRec1().setZRN_FUND_CD(zrnFundCd);
		vo.getInRec1().setZRN_ZRN_CD(zrnZrnCd);

		vo = (Igv4205pVO) MCALib.call(vo, false);
		Map<String, Object> result = new HashMap<>();

		result = vo.toMap();
		return result;
	}

	/**
	 * programType 	: 펀드4
	 * programId 	: igv4206p
	 */
	public Map<String, Object> igv4206p(@NotNull String entityId, @NotNull String yyyyMMdd, @NotNull String zrnFundCd, @NotNull String zrnZrnCd) throws Exception {

		String localhost = InetAddress.getLocalHost().getHostAddress();

		Igv4206pVO vo = new Igv4206pVO(localhost, entityId, channelCd);
		vo.getInRec1().setSTNR_DATE(yyyyMMdd);
		vo.getInRec1().setZRN_FUND_CD(zrnFundCd);
		vo.getInRec1().setZRN_ZRN_CD(zrnZrnCd);

		vo = (Igv4206pVO) MCALib.call(vo, false);
		Map<String, Object> result = new HashMap<>();

		result = vo.toMap();
		return result;
	}

	/**
	 * programType 	: ELS1
	 * programId 	: aimp02gp
	 */
	public Map<String, Object> aimp02gp(@NotNull String entityId, @NotNull String prodTypeCd, @NotNull String iscd) throws Exception {

		String localhost = InetAddress.getLocalHost().getHostAddress();

		Aimp02gpVO vo = new Aimp02gpVO(localhost, entityId, channelCd);
		vo.getInRec1().setPRDT_TYPE_CODE(prodTypeCd);
		vo.getInRec1().setISCD(iscd);

		vo = (Aimp02gpVO) MCALib.call(vo, false);
		Map<String, Object> result = new HashMap<>();
		result = vo.toMap();
		return result;
	}

	/**
	 * programType 	: ELS2
	 * programId 	: aimp024p
	 */
	public Map<String, Object> aimp024p(@NotNull String entityId, @NotNull String aChkCd, @NotNull String prodTypeCd, @NotNull String iscd) throws Exception {

		String localhost = InetAddress.getLocalHost().getHostAddress();

		Aimp024pVO vo = new Aimp024pVO(localhost, entityId, channelCd);
		vo.getInRec1().setA_CHK_CODE(aChkCd);
		vo.getInRec1().setPRDT_TYPE_CODE(prodTypeCd);
		vo.getInRec1().setISCD(iscd);

		vo = (Aimp024pVO) MCALib.call(vo, false);
		Map<String, Object> result = new HashMap<>();
		result = vo.toMap();
		return result;
	}

	/**
	 * programType 	: 채권
	 * programId 	: aigp015p
	 */
	public Map<String, Object> aigp015p(@NotNull String entityId, @NotNull String iscd) throws Exception {

		String localhost = InetAddress.getLocalHost().getHostAddress();

		Aigp015pVO vo = new Aigp015pVO(localhost, entityId, channelCd);
		vo.getInRec1().setISCD(iscd);

		vo = (Aigp015pVO) MCALib.call(vo, false);
		Map<String, Object> result = new HashMap<>();
		result = vo.toMap();
		return result;
	}

	/**
	 * programType 	: 채권2 채권수익률
	 * programId 	: pga0011p
	 */
	public Map<String, Object> pga0011p(@NotNull String entityId, @NotNull String anctDate, @NotNull String iscd) throws Exception {

		String localhost = InetAddress.getLocalHost().getHostAddress();

		Pga0011pVO vo = new Pga0011pVO(localhost, entityId, channelCd);
		vo.getInRec1().setANCT_DATE(anctDate);
		vo.getInRec1().setCLNT_SECT_CODE("0");
		vo.getInRec1().setACNT_TYPE_CODE("GNAC");
		vo.getInRec1().setA_STRT_MTRY_DATE("0000");
		vo.getInRec1().setA_END_MTRY_DATE("9912");
		vo.getInRec1().setYILD_SECT_CODE("0");
		vo.getInRec1().setSTNR_YILD("0");
		vo.getInRec1().setBOND_PRDT_SECT_CODE("0");;
		vo.getInRec1().setSTND_ISCD(iscd);
		vo.getInRec1().setA_ANCT_SECT_CODE("0");
		vo.getInRec1().setA_LEN_27_CNTI_RFRN_KYVL("");

		vo = (Pga0011pVO) MCALib.call(vo, false);
		Map<String, Object> result = new HashMap<>();

		result = vo.toMap();

		return result;
	}
	
	/**
	 * programType 	: 상담이력 채번
	 * programId 	: sge1012p
	 */
	public Map<String, Object> sge1012p(@NotNull String entityId) throws Exception {

		String localhost = InetAddress.getLocalHost().getHostAddress();

		Sge1012pVO vo = new Sge1012pVO(localhost, entityId, channelCd);
		vo.getInRec1().setH_A_RFRN_PRCE_SECT_CODE("I");

		vo = (Sge1012pVO) MCALib.call(vo, false);
		Map<String, Object> result = new HashMap<>();
		result = vo.getOutRec1().toMap();
		log.info(">>>>>>>> sge1012p : {} " , result);
		return result;
	}
	
	/**
	 * programType 	: 상담이력등록
	 * programId 	: sge1011p
	 */
	public Map<String, Object> sge1011p(@NotNull String entityId, Map<String, Object> params) throws Exception {
		String icnsl_prhs_id = null;
		if(params.get("sectCode").equals("I")) {
		/// 신규일 경우 상담이력 등록 전 채번
			 Map<String, Object> result = sge1012p(entityId);
			 log.info(" >>>>>>>>>>>>>>>>> result=sge1012p : {}", result);
			icnsl_prhs_id = result.get("H_CNSL_PRHS_ID").toString();
		}
		if(params.get("sectCode").equals("U")) {
			icnsl_prhs_id = params.get("mcaNum").toString();
		}
		MemberVO memberVO = MemberAuthService.getCurrentUser();

		//주고객번호를 받아야 함
		HashMap<String, Object> cstmInfo = (HashMap<String, Object>) sge9901p(entityId, CommonConstants.MCA_CHANNEL_CODE);

		String localhost = InetAddress.getLocalHost().getHostAddress();

		Sge1011pVO vo = new Sge1011pVO(localhost, memberVO.getId(), channelCd);

		String inboundChannel = "70";
		if("A".endsWith(params.get("channel").toString()) ) {
			inboundChannel = "71";
		}

		vo.getInRec1().setH_iprce_sect_code(""+params.get("sectCode"));
		vo.getInRec1().setH_icnsl_prhs_id(""+icnsl_prhs_id);
		vo.getInRec1().setH_iclnt_yn((StringUtil.isEmpty(entityId)? "N": "Y"));
		vo.getInRec1().setH_iclnt_enty_id((StringUtil.isEmpty(entityId)? "": entityId));
		vo.getInRec1().setH_iacnt_no("");
		vo.getInRec1().setH_ia_mncl_no(""+cstmInfo.get("RBNO"));
		vo.getInRec1().setH_ia_sub_clnt_no("");
		vo.getInRec1().setH_iclnt_clss_code("");
		vo.getInRec1().setH_icnsl_prhs_date("");
		vo.getInRec1().setH_icnsl_strt_time("");
		vo.getInRec1().setH_iend_date("");
		vo.getInRec1().setH_ia_end_time("");
		vo.getInRec1().setH_iprdt_sect_code("");
		vo.getInRec1().setH_imngg_brnh_enty_code("");
		vo.getInRec1().setH_iclcr_prce_stas_code("1");
		vo.getInRec1().setH_icall_larg_clsn_code("01");
		vo.getInRec1().setH_icall_mdum_clsn_code("" + inboundChannel);
		vo.getInRec1().setH_icnsl_larg_clsn_code("" + params.get("dep1CtgCd"));
		vo.getInRec1().setH_icnsl_mdum_clsn_code("" + params.get("dep2CtgCd"));
		vo.getInRec1().setH_icnsl_smal_clsn_code("" + params.get("dep3CtgCd"));
		vo.getInRec1().setH_icrny_ned_time("");
		vo.getInRec1().setH_ia_dprt_code(memberVO.getOdtbrCode());
		vo.getInRec1().setH_icnsl_team_code("");
		vo.getInRec1().setH_icnsl_empy_no(memberVO.getId());
		vo.getInRec1().setH_ire_crny_rqst_type_code("");
		vo.getInRec1().setH_ire_crny_phon_no("");
		vo.getInRec1().setH_itch_path_sect_code("");
		vo.getInRec1().setH_iclnt_cnsl_ctnt(""+params.get("memo"));
		vo.getInRec1().setH_isvc_rqst_no("");
		vo.getInRec1().setH_icrny_rslt_code("");
		vo.getInRec1().setH_iphon_cnsd_id("");
		vo.getInRec1().setH_ihnop_phon_cnsd_id("");
		vo.getInRec1().setH_iorgl_cnsl_prhs_id("");
		vo.getInRec1().setH_iclnt_phon_no("");
		vo.getInRec1().setH_iotbn_no("");
		vo.getInRec1().setH_irsrv_date("");
		vo.getInRec1().setH_irsrv_time("");

		log.info("=========>>>>>>>>>vo.getInRec : {}", vo.getInRec1());
		vo = (Sge1011pVO) MCALib.call(vo, false);
		Map<String, Object> result = new HashMap<>();
		result = vo.getOutRec1().toMap();
		result.put("mcaNum", icnsl_prhs_id);
		log.info("==========>>>>>>>>>>>>result : {}", result);
		return result;
	}


	/**
	 * programType 	: 상담이력등록 ( 카카오 )
	 * programId 	: sgd0038p
	 */
	public Map<String, Object> sgd0038p(@NotNull String entityId, Map<String, Object> params) throws Exception {
		MemberVO memberVO = MemberAuthService.getCurrentUser();
		String localhost = InetAddress.getLocalHost().getHostAddress();

		Sgd0038pVO vo = new Sgd0038pVO(localhost, memberVO.getId(), channelCd);
		vo.getInRec1().setPRCE_SECT_CODE("I");
		vo.getInRec1().setCNSL_PRHS_ID("");
		vo.getInRec1().setCLNT_YN((StringUtil.isEmpty(entityId)? "N": "Y"));
		vo.getInRec1().setCLNT_ENTY_ID((StringUtil.isEmpty(entityId)? "": entityId));
		vo.getInRec1().setACNT_NO("");
		vo.getInRec1().setA_MNCL_NO("");
		vo.getInRec1().setA_SUB_CLNT_NO("");
		vo.getInRec1().setCLNT_CLSS_CODE("");
		vo.getInRec1().setCNSL_PRHS_DATE("");
		vo.getInRec1().setCNSL_STRT_TIME("");
		vo.getInRec1().setEND_DATE("");
		vo.getInRec1().setA_END_TIME("");
		vo.getInRec1().setPRDT_SECT_CODE("");
		vo.getInRec1().setMNGG_BRNH_ENTY_CODE("");
		vo.getInRec1().setCLCR_PRCE_STAS_CODE("1");
		vo.getInRec1().setCALL_LARG_CLSN_CODE("01");
		vo.getInRec1().setCALL_MDUM_CLSN_CODE("");
		vo.getInRec1().setCNSL_LARG_CLSN_CODE("");
		vo.getInRec1().setCNSL_MDUM_CLSN_CODE("");
		vo.getInRec1().setCNSL_SMAL_CLSN_CODE("");
		vo.getInRec1().setCRNY_NED_TIME("");
		vo.getInRec1().setA_DPRT_CODE(memberVO.getOdtbrCode());
		vo.getInRec1().setCNSL_TEAM_CODE("");
		vo.getInRec1().setCNSL_EMPY_NO(memberVO.getId());
		vo.getInRec1().setRE_CRNY_RQST_TYPE_CODE("");
		vo.getInRec1().setRE_CRNY_PHON_NO("");
		vo.getInRec1().setTCH_PATH_SECT_CODE("18");
		vo.getInRec1().setCLNT_CNSL_CTNT(""+params.get("memo"));
		vo.getInRec1().setSVC_RQST_NO("");
		vo.getInRec1().setCRNY_RSLT_CODE("");
		vo.getInRec1().setPHON_CNSD_ID("");
		vo.getInRec1().setHNOP_PHON_CNSD_ID("");
		vo.getInRec1().setORGL_CNSL_PRHS_ID("");
		vo.getInRec1().setCLNT_PHON_NO("");
		vo.getInRec1().setOTBN_NO("");
		vo.getInRec1().setRSRV_DATE("");
		vo.getInRec1().setRSRV_TIME("");
		vo.getInRec1().setTCH_TYPE_SECT_CODE("40");
		vo.getInRec1().setBSNS_DLRC_RFLC_YN("");
		vo.getInRec1().setONLN_TCH_RSLT_CODE("10");
		vo.getInRec1().setONLN_CLNT_RACT_CODE("");
		vo.getInRec1().setFLFL_CMPG_NO("");
		vo.getInRec1().setFLFL_CMPG_ACTI_NO("");
		vo.getInRec1().setEVEN_OCRN_NO("");
		vo.getInRec1().setAPPL_DATE("");
		vo.getInRec1().setRGSN_SQNO("");
		vo.getInRec1().setSVC_PRCE_STAS_CODE("");
		vo.getInRec1().setXCLN_YN("N");

		vo = (Sgd0038pVO) MCALib.call(vo, false);
		Map<String, Object> result = new HashMap<>();
		result = vo.getOutRec1().toMap();
		return result;
	}


}

