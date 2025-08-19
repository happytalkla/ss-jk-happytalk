package ht.util;

import java.util.List;
import java.util.Map;

public class LogUtil {

	public static String getCont(Map<String, Object> param, String type) {
		String logCont = "";
		try {
			if("updateSet".equals(type)) {
				String selBtn = StringUtil.nvl(param.get("selBtn"));
				if(selBtn.equals("workStatusCd")) {
					String cd = StringUtil.nvl(param.get("workStatusCd"));
					if(cd.equals("W")) {
						logCont = "전 상담직원 상담가능 설정 변경 : 상담가능";
					}else if(cd.equals("R")) {
						logCont = "전 상담직원 상담가능 설정 변경 : 상담불가";
					}
				}else if(selBtn.equals("autoMatUseYn")) {
					String cd = StringUtil.nvl(param.get("autoMatUseYn"));
					if(cd.equals("Y")) {
						logCont = "상담직원 자동 매칭 설정 변경 : 상담가능";
					}else if(cd.equals("N")) {
						logCont = "상담직원 자동 매칭 설정 변경 : 상담불가";
					}
				}else if(selBtn.equals("selfChoiUseYn")) {
					String cd = StringUtil.nvl(param.get("selfChoiUseYn"));
					if(cd.equals("Y")) {
						logCont = "상담직원 채팅 선택 설정 변경 : 상담가능";
					}else if(cd.equals("N")) {
						logCont = "상담직원 채팅 선택 설정 변경 : 상담불가";
					}
				}else if(selBtn.equals("ctgMappingUseYn")) {
					String cd = StringUtil.nvl(param.get("ctgMappingUseYn"));
					if(cd.equals("Y")) {
						logCont = "상담직원 매핑 카테고리 사용 설정 변경 : 상담가능";
					}else if(cd.equals("N")) {
						logCont = "상담직원 매핑 카테고리 사용 설정 변경 : 상담불가";
					}
				}else if(selBtn.equals("chatbotUseYn")) {
					String cd = StringUtil.nvl(param.get("chatbotUseYn"));
					if(cd.equals("Y")) {
						logCont = "챗봇(해피봇) 사용 설정 변경 : 사용";
					}else if(cd.equals("N")) {
						logCont = "챗봇(해피봇) 사용 설정 변경 : 사용안함";
					}
				}else if(selBtn.equals("autoChngApprYn")) {
					String cd = StringUtil.nvl(param.get("autoChngApprYn"));
					if(cd.equals("Y")) {
						logCont = "상담직원 변경 자동승인 설정 변경 : 자동승인";
					}else if(cd.equals("N")) {
						logCont = "상담직원 변경 자동승인 설정 변경 : 수동승인";
					}
				}else if(selBtn.equals("cnsrMaxCnt")) {
					String cnt = StringUtil.nvl(param.get("cnsrMaxCnt"));
					logCont = "전체 상담직원 수 설정 변경 : "+cnt;
				}else if(selBtn.equals("cnsrOnceMaxCnt")) {
					String cnt = StringUtil.nvl(param.get("cnsrOnceMaxCnt"));
					logCont = "상담직원당 최대 상담 건수 설정 변경 : "+cnt;
				}else if(selBtn.equals("unsocialAcceptYn")) {
					String cd = StringUtil.nvl(param.get("unsocialAcceptYn"));
					if(cd.equals("Y")) {
						logCont = "근무시간 외 상담 접수 설정 변경 : 접수가능";
					}else if(cd.equals("N")) {
						logCont = "근무시간 외 상담 접수 설정 변경 : 접수불가";
					}
					String weekStartTime = StringUtil.nvl(param.get("weekStartTime"));
					String weekEndTime = StringUtil.nvl(param.get("weekEndTime"));
					String satWorkYn = StringUtil.nvl(param.get("satWorkYn"));
					String satStartTime = StringUtil.nvl(param.get("satStartTime"));
					String satEndTime = StringUtil.nvl(param.get("satEndTime"));
					String sunWorkYn = StringUtil.nvl(param.get("sunWorkYn"));
					String sunStartTime = StringUtil.nvl(param.get("sunStartTime"));
					String sunEndTime = StringUtil.nvl(param.get("sunEndTime"));
					logCont += "<br> 평일 : " + weekStartTime + "~" + weekEndTime;
					logCont += "<br> 토요일(" + satWorkYn + ") : " + satStartTime + "~" + satEndTime;
					logCont += "<br> 일요일(" + sunWorkYn + ") : " + sunStartTime + "~" + sunEndTime;
				}else if(selBtn.equals("pwdTerm")) {
					String cd = StringUtil.nvl(param.get("pwdTermUseYn"));
					String cnt = StringUtil.nvl(param.get("pwdTerm"));
					if(cd.equals("Y")) {
						logCont = "비밀번호 변경 주기 설정 변경 : 사용(" + cnt + ")일";
					}else if(cd.equals("N")) {
						logCont = "비밀번호 변경 주기 설정 변경 : 미사용";
					}
				}else if(selBtn.equals("passTimeUseYn")) {
					String cd = StringUtil.nvl(param.get("passTimeUseYn"));
					if(cd.equals("Y")) {
						logCont = "상담 경과 시간 표시 설정 변경 : 표시";
					}else if(cd.equals("N")) {
						logCont = "상담 경과 시간 표시 설정 변경 : 표시안함";
					}
				}else if(selBtn.equals("ctgMgtDpt")) {
					String cd = StringUtil.nvl(param.get("ctgMgtDpt"));
					if(cd.equals("3")) {
						logCont = "상담분류 소분류 사용 설정 변경 : 사용";
					}else if(cd.equals("2")) {
						logCont = "상담분류 소분류 사용 설정 변경 : 사용안함";
					}
				}
			}else if("updateMessage".equals(type)) {
				String selBtn = StringUtil.nvl(param.get("selBtn"));
				String cnsFrtMsg = StringUtil.nvl(param.get("cnsFrtMsg"));
				if(selBtn.equals("cnsFrtMsg")) {
					String cd = StringUtil.nvl(param.get("firstMsgTextUseYn"));
					if(cd.equals("Y")) {
						logCont = "상담 인사말 설정 변경 : 사용  ";
					}else if(cd.equals("N")) {
						logCont = "상담 인사말 설정 변경 : 미사용";
					}
					logCont += " <br> 변경메세지 : " + cnsFrtMsg;
				}else if(selBtn.equals("cnsFrtMsgImg")) {
					String cd = StringUtil.nvl(param.get("firstMsgImgUseYn"));
					if(cd.equals("Y")) {
						logCont = "상담 인사말 이미지 설정 변경 : 사용";
					}else if(cd.equals("N")) {
						logCont = "상담 인사말 이미지 설정 변경 : 미사용";
					}
					logCont += " <br> 변경메세지 : " + cnsFrtMsg;
				}else if(selBtn.equals("busyMsg")) {
					String cd = StringUtil.nvl(param.get("busyMsgUseYn"));
					String busyMsg = StringUtil.nvl(param.get("busyMsg"));
					if(cd.equals("Y")) {
						logCont = "바쁜 시간 메세지 설정 변경 : 사용";
					}else if(cd.equals("N")) {
						logCont = "바쁜 시간 메세지 설정 변경 : 미사용";
					}
					logCont += "<br>변경메세지 : " + busyMsg;
				}else if(selBtn.equals("delayGuideMsg")) {
					String cd = StringUtil.nvl(param.get("delayGuideUseYn"));
					String delayGuideMsg = StringUtil.nvl(param.get("delayGuideMsg"));
					String delayGuideTime = StringUtil.nvl(param.get("delayGuideTime"));
					if(cd.equals("Y")) {
						logCont = "고객 답변 지연 안내 설정 변경 : 사용";
					}else if(cd.equals("N")) {
						logCont = "고객 답변 지연 안내 설정 변경 : 미사용";
					}
					logCont += " / 지연시간 : " + delayGuideTime + "분";
					logCont += "<br>변경메세지 : " + delayGuideMsg;
				}else if(selBtn.equals("delayStopMsg")) {
					String cd = StringUtil.nvl(param.get("delayStopUseYn"));
					String delayStopMsg = StringUtil.nvl(param.get("delayStopMsg"));
					String delayStopTime = StringUtil.nvl(param.get("delayStopTime"));
					if(cd.equals("Y")) {
						logCont = "고객 답변 지연 종료 설정 변경 : 사용";
					}else if(cd.equals("N")) {
						logCont = "고객 답변 지연 종료 설정 변경 : 미사용";
					}
					logCont += " / 지연시간 : " + delayStopTime + "분";
					logCont += "<br>변경메세지 : " + delayStopMsg;
				}else if(selBtn.equals("cnsrEndMsg")) {
					//String cd = StringUtil.nvl(param.get("cnsrEndMsgUseYn"));
					String cstmEndMsg = StringUtil.nvl(param.get("cstmEndMsg"));
//					if(cd.equals("Y")) {
						logCont = "상담직원 종료 메세지 변경 : " + cstmEndMsg;
//					}else if(cd.equals("N")) {
//						logCont = "상담직원 종료 메세지 설정 변경 : 미사용";
//					}
				}else if(selBtn.equals("cstmEndMsg")) {
					//String cd = StringUtil.nvl(param.get("cstmEndMsgUseYn"));
					String cstmEndMsg = StringUtil.nvl(param.get("cstmEndMsg"));
//					if(cd.equals("Y")) {
						logCont = "고객 종료 메세지 설정 변경 : " + cstmEndMsg;
/*					}else if(cd.equals("N")) {
						logCont = "고객 종료 메세지 설정 변경 : 미사용";
					}*/
				}else if(selBtn.equals("cnsEvlGuideMsg")) {
					String cd = StringUtil.nvl(param.get("cnsEvlUseYn"));
					String cnsEvlGuideMsg = StringUtil.nvl(param.get("cnsEvlUseYn"));
					if(cd.equals("Y")) {
						logCont = "상담 평가 안내 메세지 설정 변경 : 사용";
					}else if(cd.equals("N")) {
						logCont = "상담 평가 안내 메세지 설정 변경 : 미사용";
					}
					logCont += "<br>변경메세지 : " + cnsEvlGuideMsg;
				}else if(selBtn.equals("cstmProhMsg")) {
					String cd = StringUtil.nvl(param.get("cstmProhUseYn"));
					String cstmProhMsg = StringUtil.nvl(param.get("cstmProhMsg"));
					if(cd.equals("Y")) {
						logCont = "고객금지어 사용 자동응답 메세지 설정 변경 : 사용";
					}else if(cd.equals("N")) {
						logCont = "고객금지어 사용 자동응답 메세지 설정 변경 : 미사용";
					}
					logCont += "<br>변경메세지 : "+cstmProhMsg;
				}else if(selBtn.equals("cnsWaitPersMsg")) {
					String cd = StringUtil.nvl(param.get("cnsWaitPersUseYn"));
					String cnsWaitPersMsg = StringUtil.nvl(param.get("cnsWaitPersMsg"));
					if(cd.equals("Y")) {
						logCont = "상담 대기 인원 설정 메세지 설정 변경 : 사용";
					}else if(cd.equals("N")) {
						logCont = "상담 대기 인원 설정 메세지 설정 변경 : 미사용";
					}
					logCont += "<br>변경메세지 : " + cnsWaitPersMsg;
				}else if(selBtn.equals("unsocialMsg")) {
					//String cd = StringUtil.nvl(param.get("unsocialMsgUseYn"));
					String unsocialMsg =StringUtil.nvl(param.get("unsocialMsg"));
//					if(cd.equals("Y")) {
						logCont = "근무 시간 외 메세지 설정 변경 :" + unsocialMsg;
//					}else if(cd.equals("N")) {
//						logCont = "근무 시간 외 메세지 설정 변경 : 미사용";
//					}
				}else if(selBtn.equals("notCnsMsg")) {
					//String cd = StringUtil.nvl(param.get("notCnsMsgUseYn"));
					String notCnsMsg = StringUtil.nvl(param.get("notCnsMsg"));
//					if(cd.equals("Y")) {
						logCont = "상담 불가 안내 메세지 변경 : " + notCnsMsg;
//				}else if(cd.equals("N")) {
//						logCont = "상담 불가 안내 메세지 설정 변경 : 미사용";
//					}
				}else if(selBtn.equals("cnsWaitMsg")) {
					String cnsWaitMsg = StringUtil.nvl(param.get("cnsWaitMsg"));
					logCont = "상담 대기 안내 메세지 변경 : " + cnsWaitMsg;
				}

			}else if("insertHoliday".equals(type)) {
				String holidayDate = StringUtil.nvl(param.get("holidayDate"));
				String workYn = StringUtil.nvl(param.get("workYn"));
				String startTime = StringUtil.nvl(param.get("startTime"));
				String endTime = StringUtil.nvl(param.get("endTime"));
				String memo = StringUtil.nvl(param.get("memo"));
				logCont = "추가휴무일 : " + holidayDate + " " + startTime.substring(2) + ":" + startTime.substring(2,4) + " ~ " + endTime.substring(2) + ":" + endTime.substring(2,4);
				logCont += "<br>근무여부 : " + workYn + " 내용 : " + memo;
			}else if("deleteHoliday".equals(type)) {
				@SuppressWarnings("unchecked")
				List<String> delHolidayDateList = (List<String>)param.get("delHolidayDateList");
				logCont = "휴일 삭제 : ";
				for(String date : delHolidayDateList) {
					logCont += date + " ";
				}
			}else if("updateCnsrHolidayWorkYn".equals(type)) {
				String cnsPossibleYn = StringUtil.nvl(param.get("cnsPossibleYn"));
				String counselorName = StringUtil.nvl(param.get("counselorName"));
				String counselorId = StringUtil.nvl(param.get("counselorId"));
				String schDate = StringUtil.nvl(param.get("schDate"));
				logCont = "상담직원 휴일 설정 변경 - 근무여부 : " + " " + counselorName + "(" + counselorId + ") " + schDate + " : " + cnsPossibleYn;
			}else if("updateCnsrHolidayTime".equals(type)) {
				String counselorName = StringUtil.nvl(param.get("counselorName"));
				String counselorId = StringUtil.nvl(param.get("counselorId"));
				String schDate = StringUtil.nvl(param.get("schDate"));
				String startTime = StringUtil.nvl(param.get("startTime"));
				String endTime = StringUtil.nvl(param.get("endTime"));
				logCont = "상담직원 휴일 설정 변경 - 근무시간 : " + counselorName + "(" + counselorId + ")" + " " + schDate + " " + startTime.substring(2) + ":" + startTime.substring(2,4)  + " ~ " + endTime.substring(2) + ":" + endTime.substring(2,4);
			}
			else if("updateChatCntSetting".equals(type)) {
				//String counselorName = StringUtil.nvl(param.get("counselorName"));
				String counselorId = StringUtil.nvl(param.get("memberUid"));
				String cnsrMaxCnt = StringUtil.nvl(param.get("cnsrMaxCnt"));
				logCont = "상담직원 개별 상담수 설정 변경 -  " + counselorId + " 개별상담수 : " + cnsrMaxCnt;
			}
		}catch(Exception e) {
			HTUtils.batmanNeverDie(e);
		}

		return logCont;
	}

}
