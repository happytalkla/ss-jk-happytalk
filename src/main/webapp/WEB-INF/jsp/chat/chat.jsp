<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<title>디지털채팅상담시스템</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">	
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/include.css' />" /> 
	<!--[if lt IE 9]>
	<script type="text/javascript" src="<c:url value='/js/html5shiv.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/html5shiv.printshiv.js' />"></script>
	<![endif]-->
	
	<script type="text/javascript" src="<c:url value='/js/jquery-1.12.2.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/jquery-ui.1.9.2.min.js' />"></script>

	<script type="text/javascript" src="<c:url value='/js/jquery.bxslider.min.js' />"></script>
</head>

<body id="chat">	
	<!-- 00.채팅창:head-->
	<div id="chat_head">
		<button type="button" class="btn_go_history">상담내역 가기</button>
		<!--button type="button" class="btn_go_prev">뒤로가기</button-->
		<h1 class="chat_logo"><img src="<c:url value='/images/chat/logo.png' />" alt="삼성증권"></h1>
		<button type="button" class="btn_close_chat"><i></i>종료</button>
	</div>
	<!--// 00.채팅창:head-->

	<!-- 00.채팅창:컨텐츠영역-->
	<div id="chat_body">
		<!-- 상담직원 챗 영역:기본텍스트 -->
		<div class="left_area">
			<i class="icon_bot character">상담직원</i>
			<div class="text_box">
				아래의 목록에서 문의사항을 선택하시면 
				봇상담을 통해 빠르게 안내 받으실 수 있습니다.
				해당하는 문의사항이 없을 경우 1:1채팅 상담을 
				신청해주세요. 관계법령에 의거하여 고객님과의
				상담은 자동 기록됩니다.

				<!-- 작성시간 -->
				<span class="date">오후 05:24</span>
				<!--// 작성시간 -->
			</div>
		</div>
		<!--// 상담직원 챗 영역:기본텍스트 -->

		<!-- 상담직원 챗 영역:링크/핫키 -->
		<div class="left_area">
			<i class="icon_bot character">캐릭터</i>
			<div class="text_box">
				<div class="inner_text">
					보험을 선택해주세요.
				</div>
				<ul class="go_link">
					<li><a href="#">1. 연금 보험</a></li>
					<li><a href="#">2. 저축 보험</a></li>
				</ul>
				<a href="#" class="btn_hot_key">1:1 상담하기<i></i></a>

				<!-- 작성시간 -->
				<span class="date">오후 05:24</span>
				<!--// 작성시간 -->
			</div>
		</div>
		<!--// 상담직원 챗 영역:링크/핫키 -->

		<!-- 상담직원 챗 영역:200자 이상 -->
		<div class="left_area">
			<i class="icon_bot character">캐릭터</i>
			<div class="text_box">
				<div class="inner_text">
					아래의 목록에서 문의 사항을 선택하시면 봇상담을 통해 빠르게 안내 받으실 수 있습니다. 해당하는 문의사항이 없을 경우
					1:1 채팅 상담을 신청해주세요.
				</div>				
				<ul class="go_link">
					<li><a href="#" class="btn_view_more">법령보기</a></li>
				</ul>
				
				<!-- 작성시간 -->
				<span class="date">오후 05:24</span>
				<!--// 작성시간 -->
			</div>
		</div>
		<!--// 상담직원 챗 영역:200자 이상 -->

		<!-- 상담직원 챗 영역:전체조합 -->
		<div class="left_area">
			<i class="icon_bot character">캐릭터</i>
			<div class="text_box">
				<div class="inner_text">
					아래의 목록에서 문의 사항을 선택하시면 봇상담을 통해 빠르게 안내 받으실 수 있습니다. 
				</div>	
				<div class="img_area">
					<img src="<c:url value='/images/chat/chat_img01.gif' />" alt="이미지설명">
				</div>	
				<ul class="go_link">
					<li><a href="#">1. 연금 보험</a></li>
					<li><a href="#">2. 저축 보험</a></li>
				</ul>
				<a href="#" class="btn_hot_key">1:1 상담하기<i></i></a>
				
				<!-- 작성시간 -->
				<span class="date">오후 05:24</span>
				<!--// 작성시간 -->
			</div>
		</div>
		<!--// 상담직원 챗 영역:전체조합 -->

		<!-- 상담직원 챗 영역:전체조합_슬라이더 -->
		<div class="left_area">
			<i class="icon_bot character">캐릭터</i>			
				<div class="text_box">
					<div class="chat_text_slide">
						<ul>
							<li>
								<div class="inner_text">
									아래의 목록에서 문의 사항을 선택하시면 봇상담을 통해 빠르게 안내 받으실 수 있습니다. 
								</div>	
								<div class="img_area">
									<img src="<c:url value='/images/chat/chat_img01.gif' />" alt="이미지설명">
								</div>	
								<ul class="go_link">
									<li><a href="#">1. 연금 보험</a></li>
									<li><a href="#">2. 저축 보험</a></li>
								</ul>
								<a href="#" class="btn_hot_key">1:1 상담하기<i></i></a>
							</li>		
							<li>
								<div class="inner_text">
									아래의 목록에서 문의 사항을 선택하시면 봇상담을 통해 빠르게 안내 받으실 수 있습니다. 
								</div>	
								<div class="img_area">
									<img src="<c:url value='/images/chat/chat_img01.gif' />" alt="이미지설명">
								</div>	
								<ul class="go_link">
									<li><a href="#">1. 연금 보험</a></li>
									<li><a href="#">2. 저축 보험</a></li>
								</ul>
								<a href="#" class="btn_hot_key">1:1 상담하기<i></i></a>
							</li>	
						</ul>
						<div class="chat_slide_controls">
							<a href="#" class="btn_chat_prev">이전으로</a>
							<a href="#" class="btn_chat_next">다음으로</a>
						</div>
					</div>

					<!-- 작성시간 -->
					<span class="date">오후 05:24</span>
					<!--// 작성시간 -->
				</div>
		</div>
		<!--// 상담직원 챗 영역:전체조합_슬라이더 -->

		<!-- 상담직원 챗 영역:안내메세지 -->
		<div class="left_area">
			<i class="icon_bot counselor">상담직원</i>
			<div class="text_box">
				<div class="img_area">
					<img src="<c:url value='/images/chat/chat_img01.gif' />" alt="이미지설명">
				</div>
				<p class="text_bye">안내를 종료합니다. 다시만나요~!</p>

				<!-- 작성시간 -->
				<span class="date">오후 05:24</span>
				<!--// 작성시간 -->
			</div>
		</div>
		<!--// 상담직원 챗 영역:안내메세지 -->

		<!-- 고객 챗 영역 -->
		<div class="right_area">
			<div class="text_box">
				보험이 알고 싶어요~ 

				<!-- 작성시간 -->
				<span class="date">				
					오후 05:24
				</span>
				<!--// 작성시간 -->
			</div>			
		</div>
		<!--// 고객 챗 영역 -->

		<!-- 고객 챗 영역:안읽음 표시 -->
		<div class="right_area">
			<div class="text_box">
				보험이 알고 싶어요~ 
				아래의 목록에서 문의사항을 선택하시면 
				봇상담을 통해 빠르게 안내 받으실 수 있습니다.
				해당하는 문의사항이 없을 경우 1:1채팅 상담을 
				신청해주세요. 관계법령에 의거하여 고객님과의
				상담은 자동 기록됩니다.

				<!-- 작성시간 -->
				<span class="date">				
					<span class="no_read">안읽음</span>
					오후 05:24
				</span>
				<!--// 작성시간 -->
			</div>			
		</div>
		<!--// 고객 챗 영역:안읽음 표시 -->

		<!-- 챗 종료 -->
		<div class="chat_over">
			상담이 종료되었습니다. (챗봇 종료)
		</div>
	</div>
	<!--// 00.채팅창:컨텐츠영역-->
	
	<!-- 00.채팅창:bottom -->
	<div id="chat_bottom">
		<textarea class="form_text" placeholder="질문을 입력해 주세요."></textarea>
		<button type="button" class="btn_send">보내기</button>
	</div>	
	<!--// 00.채팅창:bottom -->

	<!-- 00.채팅창:bottom:이미지전송
	<div id="chat_bottom">
		<button type="button" class="btn_send_file">파일보내기</button>
		<textarea class="form_text_send" placeholder="질문을 입력해 주세요."></textarea>
		<button type="button" class="btn_send">보내기</button>
	</div>	
	<!--// 00.채팅창:bottom:이미지전송-->

	<!-- 00.채팅창:bottom 챗완료후 평가하기 
	<div id="chat_bottom">
		<div class="star_area">
			<i class="icon_star active">별평가</i>
			<i class="icon_star active">별평가</i>
			<i class="icon_star">별평가</i>
			<i class="icon_star">별평가</i>
			<i class="icon_star">별평가</i>
		</div>
		<textarea class="form_star_send" placeholder="상담직원에 대한 상담 별점을 입력해 주세요.&#13;&#10;한줄평가도 부탁해요(10자 이내)"></textarea>
		<button type="button" class="btn_send">보내기</button>
	</div>	
	<!--// 00.채팅창:head 챗완료후 평가하기 -->

	<!-- layer:자세히보기 -->
	<div class="poopup_chat_detail">
		<div class="inner">
			<div class="popup_head">
				<h2 class="popup_title">법령 제 212조</h2>
				<button type="button" class="btn_close_detail">창닫기</button>
			</div>
			<div class="popup_body">
				아래의 목록에서 문의사항을 선택하시면 봇상담을 통해 빠르게 안내 박으실 수 있습니다.<br>
				해당하는 문의사항이 없을 경우 1:1채팅 상담을 신청해주세요. 관계법령에 의거하여 고객님과의 상담은 자동을 기록됩니다.
				아래의 목록에서 문의사항을 선택하시면 봇상담을 통해 빠르게 안내 박으실 수 있습니다.<br>
				해당하는 문의사항이 없을 경우 1:1채팅 상담을 신청해주세요. 관계법령에 의거하여 고객님과의 상담은 자동을 기록됩니다.
				아래의 목록에서 문의사항을 선택하시면 봇상담을 통해 빠르게 안내 박으실 수 있습니다.<br>
				해당하는 문의사항이 없을 경우 1:1채팅 상담을 신청해주세요. 관계법령에 의거하여 고객님과의 상담은 자동을 기록됩니다.
				아래의 목록에서 문의사항을 선택하시면 봇상담을 통해 빠르게 안내 박으실 수 있습니다.<br>
				해당하는 문의사항이 없을 경우 1:1채팅 상담을 신청해주세요. 관계법령에 의거하여 고객님과의 상담은 자동을 기록됩니다.
			</div>
		</div>
	</div>
	<!-- layer:자세히보기 -->
</body>
</html>