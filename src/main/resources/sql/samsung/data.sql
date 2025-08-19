--------------------------------------------------------
--  파일이 생성됨 - 목요일-4월-23-2020   
--------------------------------------------------------
REM INSERTING into SHFDA.TB_BOT_SCENARIO
SET DEFINE OFF;
Insert into SHFDA.TB_BOT_SCENARIO (ID,CODE,DESCRIPTION,FALLBACK_BLOCK_ID,FIRST_BLOCK_ID,MODIFIED,MODIFIER,NAME,SORT,USE_FIRST,USE_PREV,USE_YN) values (1,'default','웹채팅',1,1,to_timestamp('20/04/23 12:54:34.728000000','RR/MM/DD HH24:MI:SSXFF'),1,'웹채팅',1,'Y','Y','Y');





insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 1, '펀드', '수익증권', '수익증권은 계약형 펀드로, 다수의 고객으로부터 자금을 모아서 대규모의 공동 기금을 형성하여, 전문적인 운용기관이 주식, 채권 등에 투자하고, 그에 따른 성과를 고객에게 배분하는 집합투자상품이에요.
펀드는 설립형태에 따라 계약형 펀드와 회사형 펀드등 2종류로 분류되는데, 이 중에서 계약형 펀드를 수익증권이라고 해요.', '계약형펀드, 수익 증권, 계약형, 증권', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 2, '펀드', '집합투자기구', '"집합투자기구라 함은 집합투자를 수행하기 위한 투자신탁과 투자회사 및 투자전문회사를 말하는 것으로 일반적으로 펀드라고 이해하시면 돼요.
예를 들어 주식을 주로 투자하는 집합투자기구가 있다고 하면 그 집합투자기구를 지칭할 때 일반적으로 주식형 펀드라고 해요."
', '투자 기구, 집합 투자, 집합기구, 집합투자', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 3, '펀드', '벤치마크 지수(BM)', '"벤치마크(BM: Bench Mark)란, 펀드의 수익률을 평가하기 위한 기준 잣대라고 생각하시면 돼요.
BM 초과율은 펀드 수익률에서 벤치마크 수익률을 뺀 값이므로, 펀드의 운용성과 평가시에 주로 벤치마크 지수를 사용해요.
주식형 펀드는 주가지수, 채권형펀드는 채권지수가 주로 벤치마크 지수로 사용돼요."
', '벤치 마크, 밴치마크, 밴치 마크, 운용 성과, 운영성과, 운영 성과, 펀드성과, bm', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 4, '펀드', '임의식 펀드', '임의식 펀드는, 저축계좌나 펀드 가입 시 매수횟수, 저축기간에 상관없이 자유롭게 매수/매도가 가능한 펀드 형태 중 하나예요.
', '임의펀드, 자유펀드, 적립식말고, 임의식, 임의식펀드', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 5, '펀드', '거치식 펀드', '"거치식 펀드는, 저축계좌나 펀드가입 시 저축 기간 내에 매수는 1회만 가능한 펀드 형태 중 하나예요.
투자금액은 유지하며, 수익금만 찾을 수 있는 것이 특징이에요."
', '거치펀드, 목돈, 종잣돈, 적립식말고, 거치식, 거치식펀드', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 6, '펀드', '자유적립식 펀드', '자유적립식 펀드란, 은행의 적금과 같이 저축계좌나 펀드 가입 시 저축 기간을 지정하여 자유롭게 납입이 가능한 저축 형태를 말하며, 저축 기간을 지정 하더라도 만기 이후 납입도 가능한 저축 형태예요.
', '자유적립식펀드, 자유펀드, 자유로운, 적립식펀드, 자유적립식', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 7, '펀드', '재간접 펀드', '재간접 펀드는, 다른 펀드(집합투자기구)가 발행한 집합투자증권에 펀드재산의 40% 이상을 투자하는 펀드를 의미해요.
', '간접펀드, 재간접펀드, 재펀드, 재간접', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 8, '펀드', '주식형 펀드', '"주식형 펀드는 운용 대상 중 최소 주식편입률이 60% 이상인 펀드예요.
큰 자본이득을 얻고자 하는 고수익추구형 상품으로, 매우 공격적이며 주식시장의 하락에 따라 손실을 입을 위험이 높아서 위험을 감수하더라도 고수익을 얻으려는 투자자들에게 적합한 상품이에요."
', '주식펀드, 고수익, 수익높은, 주식형펀드, 주식형', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 9, '펀드', '주식 혼합형 펀드', '주식 혼합형 펀드는, 펀드 운용대상 중 주식 편입비율이 50%이상~60%미만인 펀드예요.
', '혼합펀드, 주식혼합, 주식채권, 주식혼합형펀드, 혼합형펀드, 혼합 펀드, 주식혼합형', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 10, '펀드', '채권형 펀드', '"채권형 펀드는, 운용 대상 중 최소 채권 편입비율이 60%이상인 펀드예요.
가입 시 가장 유의할 점은 편입된 자산의 종류와 금리 방향이에요. 부실기업이 포함되거나 금리가 상승하는 경우 원금 손실 가능성이 존재해요. 그러나 대체적으로 투자기간이 길어질수록 안정적인 자금운용이 가능해지기 때문에 안정적인 수익을 기대할 수 있어요. "
', '채권펀드, 안전한, 안정적인, 채권형펀드, 채권, 채권형', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 11, '펀드', '채권혼합형 펀드', '채권혼합형 펀드란, 펀드 운용대상 중 주식 편입비율이 50% 미만인 펀드예요.
', '채권 혼합, 채권주식형, 채권혼합형펀드, 채권혼합형', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 12, '펀드', '인덱스 펀드', '인덱스 펀드란, 개별 주식종목이 아닌 주가지수의 변동과 동일한 투자성과 실현을 목표로 운용하는 펀드예요.
', '지수펀드, 인덱스펀드, 지수형, 코스피, 지수 펀드, 지수, index, 인덱스', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 13, '펀드', 'MMF', 'MMF란 Money Market Fund의 줄임말로, 은행의 보통예금과 같이 입출금이 자유로우나 하루를 투자해도 수익금이 발생하는 펀드예요.
', '예금, 예적금, 이자, CMA, 수시, 머니마켓, 은행비슷한, 은행 비슷, mmf, cma', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 14, '펀드', '파생상품형 펀드', '파생상품형 펀드는, 선물, 옵션 등 파생상품에 투자하며 파생상품을 통한 구조화된 수익을 추구하는 펀드예요.
', '파생펀드, 선물펀드, 위험한, 파생형펀드, 파생형, 파생상품형', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 15, '펀드', '액티브 펀드', '"액티브 펀드(Active Fund)란, 펀드매니저가 시장의 상황에 따라 주식 편입비율과 투자종목을 탄력적으로 운용하는 펀드를 말해요.
강세장이 기대되면 주식 편입비율을 높여 수익을 늘리고, 약세장이 예상되면 주식 편입비율을 줄여 주식 하락으로 인한 손실을 줄이는 전략으로 운영하는 펀드예요."
', '엑티브, 액티브펀드, 액티브, 능동형, 능동적, 변동성, active', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 16, '펀드', '패시브 펀드', '"패시브 펀드(Passive Fund)란, 초과수익보다는 안정적인 기준치만큼의 수익을 추구하는 펀드로 특정 지수를 따라 가도록 설정된 인덱스 펀드를 의미해요.
지수만큼의 수익률을 목표로 하기 때문에 적극적으로 운용할 필요가 없어 상대적으로 보수가 낮은 것이 특징이에요."
', '패시브펀드, 페시브, 패시브, 보수낮은, 수동적, 회피, 지수형, passive', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 17, '펀드', '전환형 펀드', '전환형 펀드는, 공통으로 적용되는 집합투자규약에 의하여 복수의 펀드(집합투자기구)로 구성되는 펀드 그룹을 설정하여 그룹 내의 펀드에 투자한 투자자에게 동일 그룹내 펀드로 전환할 수 있는 권한을 부여한 펀드를 의미해요.
', '전환형펀드, 전환형, 변환', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 18, '펀드', '역내 펀드', '역내펀드는, 국내 법률에 의하여 국내에서 설정, 설립되어 국내 펀드로도 불리우며, 국내자산에 주로 투자하는 펀드를 국내투자 펀드, 해외자산에 주로 투자하는 펀드를 해외투자 펀드로 구분해요.
', '역내, 역내펀드, 국내펀드, 한국펀드', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 19, '펀드', '역외 펀드', '역외펀드는, 외국 법률에 의하여 외국에서 설정, 설립된 펀드로 외국펀드라고도 불려요.
', '역외, 역외펀드, 해외펀드, 외국펀드', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 20, '펀드', '인프라 펀드', '"인프라 펀드는, 집합투자재산을 사회기반시설사업(민간투자사업)을 영위하는 법인에 투자하여, 그 수익을 투자자에게 배분하는 것을 목적으로 하는 펀드를 의미해요. 
인프라 펀드는 투자자에게 민간투자사업에 대한 연금형태의 투자수단을 제공하고, 정부에는 사회간접자본(SOC) 등을 확충하는데 소요되는 재정상 부담을 완화시키며, 사회기반시설 시장에는 장기적이고 다양한 투자재원(주식, 채권, 대출)을 공급하는 기능을 하는 펀드예요."
', '인프라, 인프라펀드, 사회펀드, 간접자본펀드, SOC펀드, soc', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 21, '펀드', '헤지펀드', '"헤지펀드는 소수의 거액투자자로부터 사모방식으로 모집한 자금을 주식, 채권, 통화, 파생상품 등 다양한 자산에 투자하여 수익을 배분하는 집합투자기구예요.
다양한 투자전략으로 위험을 분산하며 유동성을 공급하는 등 금융시장의 효율성을 제고하는 반면, 고수익, 고위험을 추구하기 위한 단기매매, 공격적 투자 행위로 시장의 안정성을 저해하는 양면성을 가지는 펀드예요."
', '헤지, 헷지, 헷지펀드, 헤지 펀드, 헷지 펀드, hedge', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 22, '펀드', '펀드 좌수', '"펀드 좌수는, 수익증권의 매매 기본단위로 펀드가 만들어질 당시에는 통상 1좌의 가치는 1원이에요.
그러나 편의상 1,000좌당 몇원으로 표현되며 이를 기준가격, 혹은 기준가라고 불러요. 
기준가는 또 1,000.00으로 소수점 둘째자리까지 나타내며 처음 펀드가 만들어질 때는 보통 1,000좌당 1,000.00원으로 출발해요. 이후 펀드가 투자한 주식 채권 등의 가치가 변함에 따라 좌당 가치도 변하게 돼요. 그러나 외국인전용 수익증권 등 특수한 경우 최초기준가격이 5,000.00원, 10,000.00원인 펀드도 있어요."
', '펀드기본, 좟수, 좌수, 펀드좌수, 매매단위, 펀드 기준, 펀드 단위', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 23, '펀드', '펀드 기준가', '펀드에서 기준가란 "펀드의 가치"와 동일한 개념으로 생각하시면 돼요.
다시 말해, 펀드가 시장에서 주식 및 채권 등에 투자해서 얻은 운용의 결과가 반영된 "펀드의 가격"으로 펀드의 순자산가치를 나타내요.
기준가격을 구하는 산식은 "기준가격=펀드의 순자산 총액/총좌수" 예요.
그러나 일반적으로 펀드의 최초 설정시에 1좌당 1원으로 계산하여 1,000좌를 기준해서 1,000원으로 계산되므로, 실제 기준가격은 ""(펀드의 순자산총액/총좌수)×1,000""으로 계산돼요. 또한, 기준가는 투자자들이 투자자금으로 수익증권을 매입 및 환매 거래 시에 "기준"이 되는 "가격"이기도 해요.
', '기준가격, 펀드기준가, 펀드가치, 펀드가격, 기준가, 펀드 가격', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 24, '펀드', '펀드 과표기준가', '펀드 과표기준가란, 과표가라고도 불리는데, 주식/채권 등의 매매차익인 자본소득은 반영하지 않으며, 이자/배당소득에 대해서만 반영하여 수익증권 투자자의 과세표준 산정에 적용되는 기준가를 의미해요.
', '과표, 과표기준, 과세표준, 소득, 펀드세금, 과표 기준가, 펀드과표, 펀드 기준가', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 25, '펀드', '과세표준', '"과세표준은 세액 계산의 기준이 되는 금액으로, 각 세법이 정하는 바에 따라 계산돼요. 
따라서 과세표준에 세율을 곱하여 산출세액이 계산돼요."
', '과세 표준, 펀드세금, 산출세액, 펀드 세금 기준', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 26, '펀드', '펀드 세금', '"펀드투자시 세금은, 편입되어 있는 채권에서 발생하는 이자, 배당소득 및 투자증권의 매매차익 중 주식에서 발생하는 배당소득에 과세돼요. 
과세 적용은 펀드의 결산에 따른 이익분배금 지급이나 환매하실 때 징수가 되며 과세율은 개인의 경우 15.4%(주민세 포함)이에요.
하지만, 펀드는 모든 금융소득에 대해 세금납부하는 것이 아니며 주식, 채권 등의 매매차익에 대해서는 과세가 되지 않아요."
', '펀드세금, 펀드이자, 펀드 이자, 이자소득세, 이자세금', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 27, '펀드', '펀드 기산일', '"펀드 기산일이란, 자본거래, 외환거래 등의 금융거래에 있어서 자금의 수도가 실제로 이루어지는 일자를 의미해요. 
입금일이 아닌 매수 결제일을 기준으로 일자를 산정해요."
', '펀드기산일, 매수기준, 매매기준, 펀드 거래일', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 28, '펀드', '펀드 결제 기준일', '펀드 결제 기준일은, 고객이 펀드 매도 신청을 하는 경우, 매도 대금이 지급되는 날을 의미해요.
', '결제기준일, 결제일, 펀드결제, 결재일, 펀드결재, 결재기준', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 29, '펀드', '펀드 환매 수수료', '"펀드 환매 수수료는, 펀드에 가입한 후 일정기간 이내에 환매할 때에 투자자에게 부과하는 수수료를 의미해요. 
환매 수수료 제도를 만든 이유는 잦은 자금 유출입을 사전에 방지함으로써 펀드 운용의 안정성을 도모하고, 동시에 환매 청구에 따라 유가증권을 매각할 때 발생할 수 있는 손실 위험을 잔류 투자자들에게 보상하기 위한 제도예요.
환매 수수료는 펀드별로 부과 여부와 환매 수수료율이 상이하므로, 펀드 상세내역을 확인해주세요."
', '환매수수료, 펀드환매, 매도수수료, 펀드 파는, 수수료, 매도, 환매, 팔때, 펀드수수료', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 30, '펀드', '펀드 선취수수료', '"펀드 선취수수료는, 펀드 매수 시점에 지불하는 수수료를 의미해요.
펀드별로 수수료 징수 방식(선취/후취 등)과 수수료율이 상이하므로, 펀드 상세내역을 확인하셔야 돼요.
 
- 산식 : 매수신청금액 * 선취수수료율(%)"
', '선취, 수수료, 매수, 살때, 펀드매수, 펀드 사는, 매수수수료, 펀드수수료', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 31, '펀드', '펀드 후취수수료', '펀드 후취수수료는, 저축기간과 상관없이 펀드 매도 시점에 지불하는 수수료를 의미해요.
펀드별로 수수료 징수 방식(선취/후취 등)과 수수료율이 상이하므로, 펀드 상세내역을 확인하셔야 돼요.
 
- 산식 : 매도결제금액 * 후취수수료율(%)', '후취, 수수료, 펀드수수료, 매도수수료, 매도, 팔때, 펀드환매, 펀드매도', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 32, '펀드', '펀드 환매수수료 면제일', '펀드 환매수수료 면제일이란, 환매수수료를 징구없이 출금 가능한 최초일을 의미해요.
펀드별 환매수수료 적용이 상이하오니 펀드 상세내역에서 확인 후 가입 해주세요.', '수수료 면제, 환매, 매도수수료 면제, 수수료 없는, 수수료 없이', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 33, '펀드', '펀드 만기일', '많은 펀드 투자자들이 오해하는 것 중 하나가 펀드의 만기일이에요. 펀드는 특별한 경우를 제외하면 만기가 없어요. 
특별한 경우는 펀드의 만기가 언제까지라고 특별히 명시한 경우로 이런 펀드는 대부분 펀드 추가 설정이 불가한 단위형, 폐쇄형 펀드예요.', '펀드만기, 만기일, 펀드 만기, 펀드종료, 펀드 끝, 펀드 종료', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 34, '펀드', '펀드 결산', '펀드는 정기적으로 "결산"이란 것을 하게 돼요. 결산을 하게 되면 기준가는 1,000원으로 다시 돌아가요. 그 사이에 펀드의 이익이 발생했다면, 그 이익금으로 펀드에 다시 투자하게 되는데 이 과정을 재투자라고 하고 보통 1년에 한번씩 정기적으로 진행해요.', '펀드결산, 결산기준, 정산, 펀드정산', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 35, '펀드', '장마감후 거래', '장마감후 거래(Late Trading)란, 집합투자증권의 매수, 환매와 관련하여 펀드의 기준가를 계산하는 시점을 경과하여 매매주문을 하였음에도 그 이전에 주문한 것으로 처리하는 것을 말하는데 이러한 관행은 특정 투자자가 특정 시장정보를 기초로 수익증권 매입 또는 환매를 함으로 인해 타 투자자의 이익을 해칠 우려가 있어요. 
그에 따라 자본시장법과 금융투자업 규정에서는 Late Trading에 대한 규제를 두어, 주식이 50% 이상 편입된 펀드의 경우 15시 30분, 그 외 펀드는 17시 이전으로 집합투자규약에서 정한 시점을 기준으로 기준가격을 정하도록 하고 있어요.', '펀드 기준, 기준시점, 기준가', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 36, '펀드', '펀드 Class', '펀드 Class(펀드 클래스)는, 선취수수료, 후취수수료, 운용보수를 부여하는 방식에 따른 구분을 의미해요.
Class별 수수료 및 운용보수 체계는 아래와 같아요.
 
- Class A형 : 선취 판매수수료 부과 펀드
- Class C형 : 후취 판매수수료 부과 펀드
- Class Ae, Ce형 : 온라인 전용 상품
- Class CP, C-P형 : 퇴직연금, 개인연금 상품', '펀드클래스, 클래스, 펀드 종류, 펀드종류, a, b, ce, c, p', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 37, '펀드', '선취형 펀드', '선취형 펀드란, 펀드 매수 결제시점에 매수신청금액에서 선취판매수수료를 징구하는 펀드예요.', '펀드 선취형, 펀드선취, 선취형', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 38, '펀드', '후취형 펀드', '후취형 펀드란, 저축기간과 상관없이 매도시점에 매도거래금액에서 일정금액의 후취판매수수료를 징구하는 펀드예요. ', '펀드 후취형, 펀드 후취, 후취형', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 39, '펀드', '펀드 신탁보수', '펀드 신탁보수는, 펀드 자산을 직접 운용하고 관리하며, 펀드를 판매하는데 드는 제반 비용으로 운용(운용회사)보수, 판매(판매사)보수, 수탁(수탁은행)보수, 사무수탁회사 보수 등이 포함되어 있어요.
단, 신탁보수는 매일 기준가에 반영되어, 고객이 추가로 지불해야 하는 비용은 아니예요.
각 펀드마다 보수는 상이하오니 펀드의 투자설명서를 통해 확인해주세요.', '펀드보수, 펀드신탁보수, 운용보수, 보수', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 40, '펀드', '펀드 보수 및 수수료', '펀드 보수 및 수수료는 펀드 거래시 발생하는 보수 및 수수료로 아래와 같아요.
 
- 신탁보수 : 펀드에서 부담 (펀드 기준가에 반영되어, 고객이 추가로 지불하는 비용은 아님)
- 운용보수 : 운용을 담당하는 운용사가 받는 보수 
- 판매보수 : 증권회사, 은행 등 투자신탁 상품의 판매를 담당하는 회사가 판매 및 고객서비스에 대한 대가로 받는 수수료 
- 수탁보수 : 고객의 자산을 별도로 보관하는 은행에서 받는 보수
- 사무관리보수 : 회계 등 일반 사무업무를 담당하는 일반사무관리회사가 받는 보수 
- 수수료 : 고객이 부담 
- 판매수수료 : 펀드 가입시 판매사에 지급되는 비용 
- 환매수수료 : 펀드 환매시(중도해지) 수익자에게 징수하는 일정액의 수수료 

※ 이외에 유가증권 매매비용 및 회계감사비용 등 펀드관리를 위해 필요한 비용도 펀드에서 부담', '펀드보수, 펀드신탁보수, 펀드수수료, 운용수수료, 운용보수, 보수, fee', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 41, '펀드', '펀드 잔고번호', '펀드 잔고번호는, 펀드 신규 매수시 마다 생성되는 번호로, 펀드 매수신청은 잔고번호 단위로만 가능해요.', '펀드 잔고, 펀드잔고번호, 잔고번호, 펀드 번호', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 42, '펀드', '펀드', '펀드란, 다수의 고객으로부터 자금을 모아서 대규모의 공동 기금을 형성하여, 전문적인 운용기관이 주식, 채권 등에 투자하고, 그에 따른 성과를 고객에게 배분하는 집합투자상품이에요. 
펀드는 설립형태에 따라 계약형 펀드와 회사형 펀드등 2종류로 분류되는데, 계약형 펀드를 수익증권이라 부르고 회사형 펀드를 뮤추얼 펀드라고 해요.', '펀드정의, 펀드기본, 펀드용어, fund', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 43, '펀드', '펀드 가입방법', '펀드는 종합 또는 투신계좌 등을 개설 후 지점 또는 온라인상에서 가입이 가능하며, mPOP에서는 펀드신규매수 화면에서 처리 가능해요. 
단, 일부 펀드 가입은 지점을 통해서만 가능하오니 이점 참고하세요. 

□ 펀드 가입 가능 시간
- 지점 : 영업일 09:00 ~ 16:00
- 온라인 : 평일 08:40 ~ 23:00, 휴일 00:30 ~ 23:30', '펀드가입, 가입방법, 가입절차, 펀드매매, 펀드매수, 신규매수, 펀드 사기, 펀드 사는', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 44, '펀드', '온라인전용 펀드', '온라인전용 펀드는, 지점에서 신규가입이 불가한 펀드로, 신규매수는 POP HTS, 홈페이지, mPOP 등 온라인 매체에서만 가입 가능한 펀드예요.', '온라인 펀드, 온라인전용, 펀드 온라인, 온라인용, ce, Ce 클래스 ', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 45, '펀드', '펀드 저축유형', '펀드의 저축유형은, 펀드 가입 시 저축기간이 없는 임의식, 1회만 납입이 가능한 거치식, 적금과 같이 주기적으로 납입하는 적립식(금액을 동일하게 지정 경우 정액적립식, 자유롭게 납입하는 자유적립식)으로 저축 형태가 나누어져요.
일반적으로 임의식과 자유적립식으로 가입 가능해요.', '펀드 유형, 펀드저축유형, 임의식, 자유적립', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 46, '펀드', '펀드 매수 가능시간', '펀드 매수 가능시간을 알려드릴게요.

- 지점 : 영업일 09:00 ~16:00
- 온라인 : 평일 08:40~23:00, 휴일 00:30~23:30

참고로, 펀드별 기준가 적용시간 및 결제예정일이 다르므로 가입 전에 펀드 상세 정보를 확인해주세요.', '펀드 매매, 매수 가능, 펀드 가능, 펀드 매수, 펀드 시간, 펀드 사기, 펀드 사는', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 47, '펀드', '펀드 추가매수방법', '현재 보유하신 펀드는 지점, 유선, Family Center(1588-2323) 및 온라인상에서 추가 매수 가능해요.
mPOP에서는 펀드추가매수 화면에서 처리 하실 수 있어요.

□ 펀드 추가 매수 시간
- 지점 : 영업일 09:00~16:00
- 온라인 : 평일 08:40~23:00, 휴일 00:30~23:30', '펀드 매매, 펀드 매수, 추가 매수, 펀드 시간, 펀드 추가', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 48, '펀드', '펀드 저축기간', '펀드 저축기간은 저축유형에 따라 달라요.
임의식 펀드의 경우 매수횟수, 저축기간에 상관없이 매수 가능하며, 적립식, 자유적립식 펀드 저축기간은 최저 12개월부터 지정 가능해요.', '펀드 기간, 펀드 저축, 펀드 약정', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 49, '펀드', '펀드 재투자', '특별한 펀드를 제외하면 대부분의 펀드는 펀드 설정일로부터 매 1년 단위로 펀드 투자실적을 확정하는 결산을 하게 돼요. 결산 시 펀드 투자 실적의 이익이 발생한 경우에는 그 이익 중 세금 등을 제외하고 펀드 투자자들에게 수익증권으로 다시 분배하는데 이러한 과정을 재투자라고 해요.
재투자 후 펀드 기준가격은 최초 기준가격(보통 1,000원)으로 환원되며, 투자자들은 재투자분 만큼 수익증권 수량이 늘어나게 돼요.', '펀드재투자, 펀드추가투자, 펀드 분배, 분배금', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 50, '펀드', '펀드 매도 방법', '펀드 매도는 지점, 유선 또는 온라인상에서 가능해요.
mPOP에서는 "펀드매도/해지"화면에서 펀드 매도가 가능해요.

□ 펀드 매도 가능시간
- 지점 : 영업일 09:00~16:00
- 온라인 : 평일 08:40 ~ 17:00 (예약매도는 23:00), 휴일 00:30 ~ 23:00', '펀드 매매, 펀드 매도, 매매방법, 매도 방법, 펀드 파는, 펀드 팔기', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 51, '펀드', '펀드 매도구분 입력방법', '펀드 매도 시 매도자금을 금액, 수량, 해지의 방법으로 신청이 가능해요.
  
□ 금액 : 입력한 금액만큼 매도 결제 처리 되며, 금액 매도 후 잔고수량이 "0"이 되어도 잔고번호는 해지되지 않음.
□ 수량 : 매도할 수량 선택 가능(전체수량 또는 일부)
□ 해지 : 해당 펀드의 모든 잔고수량 매도 후 펀드 해지 처리(해지신청 시 추가매수불가)', '펀드 매도 방법, 매도 신청, 펀드 매매 방법, 금액, 수량, 해지, 전체수량, 해지 방법', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 52, '펀드', '펀드 예약매도 신청방법', '펀드 예약매도는 HTS 및 홈페이지에서 처리가 가능하며, 매도 화면에서 예약매도 선택 후 매도 스케줄러 버튼을 통하여 고객님께서 매도 대금 수령하고자 하시는 날짜를 선택하여 신청해 주세요.
예약매도는 mPOP에서 지원되지 않으니 POP HTS 또는 홈페이지를 이용해 주세요.
 
□ POP HTS : (4711) 펀드추가매수/매도
□ 홈페이지 : 금융상품 > 펀드 > 펀드매매 > 매도/해지', '펀드예약, 펀드 매도 예약, 매도 신청, 매도예약, 매도 방법', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 53, '펀드', '펀드 매도금액 출금 방법', '펀드 매도 후 매도 결제대금 출금 방법으로는 예수금, 계좌이체, 매수예약 등의 방법이 있어요.
 
□ 예수금 : 매도 결제대금이 예수금으로 존재하도록 처리되는 방법
□ 계좌이체 : 매도 결제대금이 삼성증권 계좌 또는 은행계좌로 이체가 가능하도록 설정하는 방법
□ 매수예약 : 매도 처리된 펀드의 결제일에 예약하신 펀드가 자동으로 매수 신청되게 하는 방법(매도금액 전액 또는 일부금액 신청 가능)

※ 단, 매도 출금 방법 지정은 홈페이지, POP HTS 에서 지정이 가능하며, mPOP에서는 예수금/매수예약 신청만 가능', '펀드 매도 출금, 매도 후 출금, 펀드 출금, 펀드매도', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 54, '펀드', '펀드 중도환매 방법', '펀드의 중도환매는 mPOP 펀드매도/해지 화면에서 가능해요. 
펀드의 환매는 편입된 자산을 시장에 매각하여 현금화한 후 이를 고객에게 지급하는 것으로 바로 현금화가 되지 않아요.

□ 주식형 펀드 환매시 기준가 적용 기준
- 15시 30분 이전 환매 : 2영업일 기준가로 4영업일에 출금가능
- 15시 30분 이후 환매 : 3영업일 고시 기준가로 4영업일에 출금가능

※ 환매 제한된 펀드(폐쇄형)의 가입시에는 투자자의 각별한 주의가 요구되며 환매방법은 펀드유형에 따라 다를 수 있기에 반드시 확인해주세요.', '펀드 환매, 펀드환매, 펀드 파는 방법', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 55, '펀드', '펀드 매매 취소 방법', '펀드 매매 취소는 지점, Family Center(1588-2323) 및 온라인에서 처리 가능해요.
mPOP에서는 "펀드신청현황/취소" 화면을 이용하시면 돼요.

□ 펀드 매매 취소 가능시간
- 매수 취소 : 당일 [평일] 08:40 ~ 23:00
- 일반매도취소 : 당일 [평일] 08:40 ~ 17:00
- 예약매도취소 : 당일 08:40 ~ 23:00
 
※ 단, 취소가능 시간인 경우에도 해당 펀드의 기준가 적용시간 이전에만 취소가 가능하며 휴일인 경우 00:30~23:30까지 취소 가능해요.', '펀드 취소, 매매취소, 신청취소, 펀드취소, 매도취소, 매수취소', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 56, '펀드', '펀드 이동', '펀드 이동이란, 현재 거래 중인 펀드를 환매하지 않고, 그대로 판매사(금융회사)만 변경하여 기존 가입/납입정보를 유지한 채 지속적으로 거래할 수 있는 서비스예요.', '펀드이동, 펀드 이관, 펀드 옮기기, 옮기는, 펀드 은행, 펀드 타사, 펀드 가져오기, 펀드 수관, 펀드 보내기, 이수관', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 57, '펀드', '타사 이동 가능 펀드', '타사 이동 가능 펀드에 대해 알려드릴게요.

- 국내설정 펀드
- 최초 설정일이 2010년 1월 1일 이후인 해외펀드(해외펀드 비과세 제도 일몰 후 설정펀드)', '펀드이동, 펀드 이관, 펀드 옮기기, 옮기는, 펀드 은행, 펀드 타사, 이동가능, 펀드 수관, 펀드 보내기, 이수관', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 58, '펀드', '타사 이동 불가 펀드', '타사 이동 불가 펀드는 대표적으로 아래와 같이 있어요.
 
- MMF
- 간투법 적용펀드
- 최초 설정일이 2009. 12.31 이전인 해외투자 주식형 펀드
- 역외펀드
- 전환형 펀드
- 사모펀드 
- 온라인 전용펀드
- 밸류라이프플랜 펀드(구 웰스플랜펀드)
- 세제혜택 상품(생계형, 세금우대, 장기주식형 등) 
- 최종결산일 이후 소득정산이 발생한 잔고
- 최종결산일 이후 양수된 잔고
- 세금우대→ 일반 전환잔고(원천징수 미발생 잔고는 가능)

※ 추가 불가사항이나 펀드이동 가능여부는 지점 또는 Family Center(1588-2323)을 통해 확인해주세요.', '펀드이동, 펀드이관, 펀드 옮기기, 옮기는, 펀드 은행, 펀드 타사, 이동불가능', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 59, '펀드', '펀드이동 방법(타사→당사)', '타사에서 당사(삼성증권)로 펀드를 옮기는 경우, 펀드 이동 간소화에 따라 수관 금융기관을 방문하여 펀드 이동 신청이 가능해졌어요.

① 해당 펀드의 이동가능 여부 및 삼성증권 판매 펀드인지 확인
② 기존 판매회사(은행, 보험, 증권등)에서 계좌확인서 발급
③ 5영업일이내에 삼성증권에 내점 또는 온라인으로 펀드 이동 신청
- 삼성증권에 이동할 동일 펀드 상품 가입
- 펀드 이동신청
- 펀드 이동신청 후 익일 당사계좌로 펀드 입고

□ 펀드이동 신청 가능 매체
- 홈페이지 : 금융상품 > 펀드 > 펀드관리 > 펀드이동(타사→당사) 
- POP HTS : (4722) 펀드이동 (타사>삼성증권) 

※ 펀드이동시에는 별도의 수수료는 부과되지 않으며, 펀드를 보유하고 있는 금융권에서 펀드이동 신청하시면 돼요.
단, 펀드이동 "계좌확인서" 유효기간은 5영업일 이내에 신청이 필요하고, 펀드이동 신청 가능시간은 09:00 ~ 16:00 까지예요.
(단, 온라인으로 가입 불가한 펀드는 가까운 지점 방문하여 가입해주세요.)', '펀드 이동, 펀드 옮기기, 옮기는, 펀드 은행, 펀드 타사, 펀드 수관, 펀드 받기, 펀드 가져오기, 펀드 이수관', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 60, '펀드', '펀드이동 방법(당사→타사)', '삼성증권에서 타사로 펀드를 옮기는 경우, 펀드 이동 간소화에 따라 삼성증권 방문 또는 온라인으로 펀드 이동이 가능해요.

① 옮기고자 하는 타사(수관사)에 해당 펀드가 이동 가능한 펀드인지 확인
② 당사(이관사)에서 계좌확인서 발급 (당사 내점 및 온라인)
③ 옮기고자하는 타사(수관사)에 내점하여 수관처리
④ 수관처리일 익일부터 정상거래 가능

□ 펀드 이동 신청 매체
- 홈페이지 : 금융상품 > 펀드 > 펀드관리 > 펀드이동(삼성증권→타사) 
- POP HTS : (4723) 펀드이동신청 

- 판매사 이동에 따라 제공되는 서비스의 범위, 수수료 등이 상이 할 수 있음
- 이동신청 이후 이동 예정 판매화사에 계좌개설 전까지는 본 계좌에 대한 환매, 추가납입 등 각종 거래 제한
- 펀드이동 신청 가능시간은 09:00 ~ 16:00 까지예요.', '펀드 이동, 펀드 옮기기, 옮기는, 펀드 은행, 펀드 타사, 펀드 이관, 펀드 보내기, 펀드 내보내기, 이수관', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 61, '펀드', '펀드 계좌확인서 발급방법', '고객님 계좌확인서 발급 전 이동 예정 판매회사의 고객센터를 통해 이동 가능 여부를 확인하셨나요?

가능 여부 확인 되셨으면 펀드 계좌확인서 발급 신청은 2가지 방법이 있어요.
 
① 삼성증권 지점 방문 시 신분증 지참하여 판매사 이동을 위한 "계좌확인서" 발급
② 온라인으로 발급
- 홈페이지 : 금융상품 > 펀드 > 펀드관리>펀드이동(삼성증권→타사)
- POP HTS : (4721) 펀드이동(삼성증권>타사) 

※ 신청가능 시간은 평일 8:00 ~ 16:00 이며,  "계좌확인서" 발급 후 5영업일 내 이동 요청 하셔야 해요.', '펀드 증명서, 펀드 확인서, 계좌 확인서', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 62, '펀드', '펀드 계좌 확인서 유효기간', '펀드 계좌확인서는 금융기관에서 발급 받으신 후 5영업일 안에 펀드이동 신청이 가능해요.
5영업일 이후 사용이 불가하오니 꼭 기간내에 신청해주세요.', '증명서 유효, 계좌확인서', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 63, '펀드', '펀드 이동 신청 접수기간', '펀드 이동 신청 접수시간은 평일 9:00~ 16:00까지 가능해요.
16:00 이후에는 이동 요청이 불가하므로, 만약 삼성증권 첫 거래이시면 계좌개설 및 잔고번호 생성시간을 감안해서 신청 접수 해주셔야 해요. ', '펀드이동, 이동신청, 펀드이동 접수, 이수관 접수', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 64, '펀드', '펀드이동 완료일', '펀드이동 완료일은 영업일 기준 신청일(T)로부터 T+1 안에 이동 완료돼요.', '이동처리, 이동완료, 펀드이수관, 이관완료, 수관완료, 이관처리, 수관처리, 이수관 완료', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 65, '펀드', '펀드사후관리 서비스', '펀드사후관리 서비스란, 고객님께서 가입하신 펀드에 대해서 목표수익률 도달안내, 수익률 정기안내 등의 펀드 정보를 SMS로 안내해드리는 사후관리서비스예요.', '펀드 관리, 펀드 사후, 펀드 서비스', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 66, '펀드', '펀드 사후관리 서비스 종류', '펀드사후관리 서비스 종류에 대해 알려드릴게요.

□ 목표수익률 도달안내 : 펀드의 원하는 수익률을 사전에 등록하고 수익률 도달 시 고객님의 휴대폰 SMS에 자동으로 전송
□ 수익률 정기안내 : 주 또는 월별 지정한 일자에 지정한 펀드의 수익률을 휴대폰 SMS에 자동으로 전송', '펀드 관리, 펀드 사후, 펀드 서비스, 사후관리 종류', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 67, '펀드', '펀드 가입정보 정정 방법', '펀드 잔고번호와 관련된 월저축금, 저축기간, 만기후납입여부, 분기약정금액, 저축계약금액등의 정보를 변경 할 수 있어요.
펀드 가입정보 정정은 지점, Family Center(1588-2323) 또는 아래 매체 에서 가능해요.
□ 홈페이지 : 금융상품 > 펀드 > 펀드관리 > 가입정보 조회/변경
□ POP HTS : (4717) 펀드가입정보정정/해지', '펀드 수정, 펀드 정정, 정보 변경, 정보 수정', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 68, '펀드', '펀드 저축종류 정정방법', '□ 임의식, (자유)적립식 → 거치식으로 변경
- 매수건이 1회만 있는 경우 변경 가능
□ 자유(또는 정액)적립식 → 임의식 변경
-가능(임의식 변경 후 잔고가 "0"원인 경우에도 정기대체 및 적립식자동이체 약정유지 가능)

펀드 저축종류 정정은 지점, Family Center(1588-2323) 또는 아래 매체 에서 가능해요.
□ 홈페이지 : 금융상품 > 펀드 > 펀드관리 > 가입정보 조회/변경
□ POP HTS : (4717) 펀드가입정보정정/해지', '펀드 저축 종류, 펀드 종류, 저축 변경, 저축 정정', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 69, '펀드', '펀드 원금보장 여부', '펀드는 원금보장이 되지 않는 상품이에요. 펀드는 고객이 맡긴 돈을 자산운용회사가 운용하여 그 결과를 돌려주는 상품으로 운용 결과에 따라 수익을 얻을 수도 있고, 반대로 원금에 손실이 발생할 수 있어요.', '펀드 보장, 펀드 예금자, 펀드 원금 보장, 예금자보호법', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 70, '펀드', '펀드 기준가 조회방법', '펀드의 기준가는 수익증권을 사고 팔 때 기준이 되는 가격이에요. 운영성과에 따라 매일 변동 되고, 최초 설정일 또는 결산일을 기준으로 하며 1,000좌당 1,000으로 계산되어 매 영업일마다 공시되고 있어요.

- 미 보유펀드 : "펀드찾기" 메뉴를 통해 검색하신 후 해당 펀드명을 선택하여 조회 가능
- 보유펀드 : "계좌잔고" 메뉴에서 해당 펀드명을 선택하여 펀드상세 화면에서 조회 가능', '펀드기준가격, 기준가조회, 가격조회, 펀드 조회', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 71, '펀드', '펀드 기준가격과 과표 기준가격 차이 사유', '펀드 기준가격과 과표 기준가격의 차이는 펀드에서 투자한 자산들의 투자소득중에서 과세가 되지 않은(비과세) 투자소득이 때문이에요.
대표적인 비과세 투자소득으로는 국내 상장주식의 매매차익이나 평가차익으로 국내 상장주식에 주로 투자하는 국내주식형펀드의 경우에는 대부분 기준가격과 과표 기준가격이 차이가 있어요.', '펀드 이유, 펀드 사유, 펀드 가격 차이', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 72, '펀드', '펀드 예금자보호법 적용여부', '예금은 만기일에 약정된 원리금만을 지급하겠다는 약속하에 고객의 돈을 예치 받은 금융상품만을 말하며, 실적배당 신탁과 같이 고객이 맡긴 돈을 운용실적에 따라 지급하는 투자상품은 예금이 아니므로, 펀드 역시 예금자 보호법 대상이 아니라 예금자보호법에 적용받지 않아요.
다만, 펀드는 그 구조상 투자자가 맡긴 자금을 수탁회사(은행 등)이 보관하면서 자산운용회사의 운용지시에 따라 투자하며, 수탁회사는 펀드 투자자의 자금을 엄격히 구별하여 관리하므로 판매회사나 자산운용회사가 상관없이 투자금을 받을 수 있어요.', '펀드 보장, 펀드 예금자, 펀드 원금 보장, 예금자보호법, 펀드 보호, 예금보호', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 73, '펀드', '해외 뮤추얼펀드', '해외 뮤추얼펀드란, 외국 위탁회사가 외국법령에 의하여 외국에서 설정하거나 발행한 펀드 또는 외국 위탁회사의 의하여 그 재산이 투자 및 운용되는 펀드예요.', '해외 펀드, 해외펀드, 외국펀드, 외국 펀드, 해외뮤추얼', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 74, '파생결합증권', '파생결합증권', '파생결합상품은, 기초자산(주가, 채권금리, 원자재가격, 펀드,환율)등과 연계하여 미리 정한 방법에 따라 지급금액(또는 회수금액)이 결정되는 상품이에요.
기초자산의 종류에 따라 주가연계파생결합증권/사채(ELS/ELB), 기타파생결합증권/사채(DLS/DLB)로 분류돼요.', 'els, 딘, 띤, dls, dlb, elb, 이엘에스, 이앨에스, 파생', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 75, '파생결합증권', 'ELS(주가연계증권)', 'ELS(Equity Linked Securities)는 이자, 원금 또는 원리금의 지급이 특정 주가지수 또는 개별주식 등에 연동하여 수익구조가 결정되는 증권이에요. ', 'els, 딘, 띤, 이엘에스, 이앨에스, 파생', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 76, '파생결합증권', 'ELB(주가연계 파생결합사채)', 'ELB(Equity Linked Bond)는 주가연계 파생결합사채예요. 개별주식(국내, 해외), 주가지수를 연동하여 수익구조가 결정되는 증권이에요.
단, ELS는 원금 비보장형 상품인 반면 ELB는 원금지급형 상품이에요.', 'elb, 딩, 띵, 파생, 이엘비, 이앨비', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 77, '파생결합증권', 'DLS(기타 파생결합증권)', 'DLS(기타파생결합증권)는 금리, 환율, 원자재, 펀드등에 연동하여 수익구조가 결정되는 증권이며 원금 비보장형이에요.', 'dls, 인, 파생, 디엘에스, 디앨에스', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 78, '파생결합증권', 'DLB(기타 파생결합사채)', 'DLB(기타파생결합사채)는 DLS와 같은 형태로 금리, 환율, 원자재, 펀드등에 연동하여 수익구조가 결정되는 증권이에요.
단, DLS는 원금 비보장형 상품인 반면 DLB는 원금지급형 상품이에요.', 'dlb, 이ㅠ, 파생, 디엘비, 디앨비', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 79, '파생결합증권', '파생결합증권 원금지급여부', '파생결합증권은 원금지급형과 원금비보장형으로 나눠져 있어 기대 수익에 따른 리스크를 본인이 사전에 선택 할 수 있어요.
   
□ 원금비보장형 : 주가연계증권(ELS)/기타파생결합증권(DLS)
□ 원금지급형 : 주가연계 파생결합사채(ELB)/ 기타파생결합사채(DLB)

※ 참고사항
ELB/DLB(파생결합사채)는 약속한 상환 시점 또는 만기에 원금 이상의 지급을 보장하는 상품으로, 그 이전에는 원금 미만의 가격으로 평가될 수 있므며, 중도환매(투자자의 요청에 의해 중도상환)를 하는 경우에는 원금손실이 발생할 수 있음.', '파생, 원금보장, 원금지급, 손해없이, 손실안나게', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 80, '파생결합증권', '월지급식 파생결합증권', '월지급식 파생결합증권(월지급ELS)은, 매월 월수익지급 기준가격결정일에 기초자산의 가격이 사전에 정한 기준 이상이면 일정수준을 월수익금액으로 지급하는 상품이에요.
매월 지급된 금액은 정기자동대체를 통해 출금하여 다른 상품으로 투자가 가능한 장점이 있어요.', '월지급, 매달이자, 달마다, 생활비, 월지급보장', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 81, '파생결합증권', '파생결합증권 상환 지급', '파생결합증권은 상환 지급시, 파생결합증권 상품의 최종수익 확정 후 해당 상품 가입 고객에게 일괄적으로 SMS 안내 해드려요.
 
□ 당사 발행 파생결합증권 : 오전 10시 20분부터 지급 시작 
□ 타사 발행 파생결합증권 : 오후 3시 20분부터 지급 시작

파생결합증권 상품별 조기(만기)상환조건이 상이하므로, 자세한 사항은 지점 PB 또는 투자상담(1577-4100)으로 문의해주세요.', '상환, 원금, 이자, 원리금, 이자 지급', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 82, '파생결합증권', '스텝다운 파생결합증권', '스텝다운 파생결합증권은, 조기상환시점 또는 만기가격이 사전에 정한 수준 이상인 경우 수익 상환되며, 그 가격 수준이 점점 낮아지는 구조의 상품이에요.', 'ELS구조, 파생구조, 스텝다운', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 83, '파생결합증권', '낙아웃콜 파생결합증권', '낙아웃콜 파생결합증권은, 일정구간 내에서 기초자산의 가격상승에 대한 사전에 정한 참여율의 손익을 지급하는 상품이에요.', '낙아웃, ELS구조, 파생구조', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 84, '파생결합증권', '양방향 낙아웃 파생결합증권', '양방향 낙아웃 파생결합증권은, 일정구간 내에서 기초자산의 가격변동에 대한 사전에 정한 참여율의 손익을 지급하는 구조로서, 가격 상승뿐만 아니라 가격 하락 구간에서도 수익발생이 가능한 상품이에요.', '양방향, 낙아웃, ELS구조, 파생구조', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 85, '파생결합증권', '파생결합증권 청약방법', '파생결합증권(ELS/DLS)는 종합(01)계좌에서 가입이 가능하며, 지점 및 온라인상에서 청약 가능해요.
mPOP에서는 "ELS/DLS 청약신청" 화면에서 청약하실 수 있어요.

□ 청약 가능시간
- 지점 : 영업일 09:00 ~ 16:00
- 온라인 : 영업일 08:30 ~ 23:00
※ 단, 청약 마감일에는 13:00까지만 가능', 'ELS 청약, ELS 가입,els가입, els청약, ELS 매수, els매수, els사는법, els', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 86, '파생결합증권', '파생결합증권 청약가능시간', '파생결합증권 청약 가능시간에 대해 알려드릴게요.

- 지점 : 영업일 09:00 ~ 16:00
- 온라인 : 영업일 08:30 ~ 23:00
※ 단, 청약마감일에는 13:00까지만 가능

지점에 내방하여 입금과 동시에 청약하실 경우에는 15시30분 전에 방문해주세요.
또한 상담을 통해 가입이 진행되는 관계로 상담시간이 그만큼 더 소요될 수 있다는 점 참고해주시고, 특히 청약 마감일에는 13:00까지 청약이 가능하다는 점도 잊지 마세요.', 'ELS 청약, ELS 가입,els 가입, els청약, ELS 매수, els 매수, ELS 시간, els 시간', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 87, '파생결합증권', '파생결합증권 경쟁률', '현재 진행중인 파생결합증권 상품의 청약경쟁률은 mPOP ELS/DLS청약경쟁률 화면에서 확인 가능해요.
청약수량이 공모수량의 100%에 미달하는 경우에는 해당 청약 수량만큼만 발행하여 전량 배정되며, 청약수량이 공모수량의 100% 이상인 경우 청약 수량에 비례하여 안분 배분해요.', 'ELS 경쟁, els 경쟁, ELS 청약', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 88, '파생결합증권', '파생결합증권 가입금액', '파생결합증권(ELS)청약 최소금액은 종목마다 다를 수 있기때문에 청약 전 최소가입금액을 확인해주세요.
최소 가입금액은 일반적으로 100만원(만원단위 청약)이나, 온라인전용 상품 중에는 최소금액 10만원(만원단위 청약)인 종목도 있어요.
최대 가입금액은 정해져 있지 않으나, 전체 투자자의 청약금액이 발행한도를 초과한 경우 안분하여 배정될 수 있어요.', 'ELS 청약, els 청약, els 금액, ELS 금액, 최소가입, 최소금액', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 89, '파생결합증권', '파생결합증권 청약 증거금', '파생상품거래 시 파생결합증권 청약 증거금은, 청약금액의 100%를 청약증거금으로 징구해요.', 'ELS증거금, els증거금, 청약증거금, 청약금', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 90, '파생결합증권', '파생결합증권 환불계좌 등록', '파생결합증권 환불계좌 등록에 대해 알려드릴게요.

- 현금청약시(타인명의 계좌로 지정가능) : 청약계좌, 당사계좌(CMA), 은행계좌
- 대체청약시(본인명의 계좌로만 지정가능) : 청약계좌, 당사계좌(CMA), 약정은행계좌', 'ELS 환불, els 환불', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 91, '파생결합증권', '파생결합증권 환불계좌 변경 여부', '파생결합증권 청약 시 선택한 환불계좌는 변경 할 수 없어요. 
청약 후 환불계좌 변경을 원하시는 경우 청약 취소 후 재 청약 처리 해주세요.', 'ELS 환불, els 환불', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 92, '파생결합증권', '은행연계계좌 파생결합증권 청약 가능여부', '은행연계계좌로 청약할 때는 아래 은행에서 가입된 삼성증권 은행연계계좌(종합계좌)에서 파생결합증권 청약이 가능해요.
당사 예수금방식계좌 : 국민/우체국/기업/광주/SC제일/경남/씨티/새마을금고/우리/KEB하나/농협', '은행계좌, 청약, 은행 ELS', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 93, '파생결합증권', '파생결합증권 청약 배정내역 확인방법', '파생결합증권(ELS) 청약 배정내역은, 청약 후 배정일 당일 오전 11시경에 배정 및 환불 내역을 확인 하실 수 있어요.
배정내역 등은 홈페이지와 mPOP, POP HTS를 통해 직접 확인 가능해요.', '청약내역, 배정내역, ELS 배정', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 94, '파생결합증권', '파생결합증권 청약 취소방법', '파생결합증권 청약취소는 청약기간 내에 처리지점(내방/유선) 및 온라인상에서 취소 가능하며, mPOP에서는 ELS/DLS청약현황/취소 화면에서 처리하실 수 있어요.
단, 온라인 전용 상품인 경우 지점에서 처리 불가하며, 청약마감일에는 당일 청약 마감전(13시)까지 취소 가능해요.', '청약취소, ELS 취소', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 95, '파생결합증권', '파생결합증권 중도환매 신청방법', '파생결합증권 중도환매 신청은 지점, 유선 및 온라인상에서 가능하며, mPOP에서는 ELS/DLS환매신청 화면에서 처리 하실 수 있어요.
중도환매금액은 평가금액의 95%이상으로 지급되지만, 중도환매 시 원금손실의 우려가 있으므로 지점 또는 투자상담(1577-4100)을 통해 상담 후 중도환매를 신청 해주세요.

□ 참고사항 
- 중도환매는 전액환매 및 일부환매(100만원 이상) 가능
- 중도환매금액 결정일 : 중도환매신청일 ＋ 2영업일
- 중도환매금액 지급일 : 중도환매금액결정일 ＋ 1영업일(즉, 중도환매신청일 ＋ 3영업일)    ', 'ELS 환매, 현금, ELS 매도, 해지', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 96, '파생결합증권', '파생결합증권 중도환매 신청 가능시간', '파생결합증권 온라인 중도환매 신청은 당사 영업일 08:30~16:00까지 가능해요.
상품에 따라 환매(투자자의 요청에 의한 중도상환)요청 가능기간이 아니거나, 환매신청이 불가한 경우 지점 또는 Family Center(☎1588-2323)로 문의해주세요.', 'ELS 환매, 현금, ELS 매도, 환매시간, 해지', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 97, '파생결합증권', '파생결합증권 중도 환매 후 예상가격', '파생결합증권 중도 환매 후 예상가격은 지점 또는 Family Center(☎1588-2323)로 문의 해주세요.', '환매가격, 환매예상', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 98, '파생결합증권', '파생결합증권 중도상환 가능여부', 'ELS/DLS는 사전에 정해진 만기일(또는 자동조기상환이 결정되었을 경우 자동조기상환일)에 지급이 이루어지는 것이 원칙이에요.
그렇지만 투자자가 원할 경우 중도상환을 요청할 수 있으며, 투자설명서에 명시된 중도상환 지급 내용을 우선 원칙으로 하되 최소 중도상환금액은 ELS/ELB의 경우 중도상환 기준가격의 95%이상(단, 발행 후 6개월까지는 기준가격의 90%이상), DLS/DLB의 경우 중도상환 기준가격의 90%이상으로 산정하여 지급하며 이 경우 원금손실이 발생할 수 있어요.', 'ELS 가능, ELS 상환, 상환 가능, 해지', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 99, '파생결합증권', '파생결합증권 조기상환', '파생상품의 조기상환조건은 상품별 상이하므로 ELS상세 현황에서 조기상환 조건 및 간이투자설명서를 확인해주세요.', '조기상환, ELS 상환', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 100, '파생결합증권', '파생결합증권 상환 통보 방법', '파생결합증권(ELS)의 상환 통보는, 수익조건이 충족되면 최종수익 확정 후 대상 고객님께 일괄적으로 SMS로 대금지급일을 안내해드려요.
고객님의 연락처 불분명한 경우 누락될 수 있으니 고객님의 정보를 확인 해주세요.', 'ELS 통보, 상환 알림', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 101, '파생결합증권', '파생결합증권 상환 대금 입금시간', '파생결합증권 상환 대금 입금시간에 대해 알려드릴게요.

□ 입금시간
- 당사발행 : 상환일 오전 10시 20분 ~ 10시 30분  
- 타사발행 : 상환일 오후 3시 ~ 3시 30분

※ 위 입금시간은 지연될 수 있으니 자세한 내용은 지점 또는 Family Center(☎1588-2323)로 문의 해주세요.', 'ELS 입금, 상환입금, 현금 입금', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 102, '파생결합증권', '파생결합증권 환매방법', '파생결합증권 환매는 지점, 유선 및 온라인상에서 가능하며, mPOP에서는 ELS/DLS환매신청 화면에서 신청하실 수 있어요.

□ 환매가능 시간
- 평일(영업일) : 08:30 ~ 16:00
 
참고로, 전액환매 및 일부환매(100만원 이상) 가능하며, ELS 중도환매 신청 시 원금손실의 우려가 있어요. 환매 철회는 불가하오니 지점 또는 자산관리상담센터(☎1577-4100)로 상담 후 신청해 주세요.            ', 'ELS 환매, ELS 파는법, ELS 팔기, 해지', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 103, '파생결합증권', '파생결합증권 세금', '파생결합증권 상품에서 발생하는 소득은 원금 대비 초과 수익에 대해서만 배당소득으로 원천징수하며 손실이 발생한 경우 과세하지 않아요.
원천징수 시기는 만기/중도 상환 시, 이자지급 시 또는 중도환매시에 발생하며, 세금은 주민세 포함 15.4%가 원천징수돼요.', 'ELS 세금, 이자소득, 이자배당, 소득세금', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 104, '채권', 'RP', 'RP(환매조건부채권, Repurchase Agreement)는, 금융기관이 일정기간 후에 다시 사는 조건으로 채권을 고객에게 판매하고 일정 기간이 경과하면 소정의 이자를 붙여 되사는 채권이예요.
채권투자의 약점인 환금성을 보완하기 위한 약정수익형 투자상품이에요.', '조건환매, 환매채권, 예금, 은행, rp', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 105, '채권', '수시 RP', '수시 RP는, 은행의 MMDA나 투신사의 MMF와 단기상품이라는 점에서 비슷한 상품으로, 수시 입출금이 가능하며 경과기간에 따라 약정된 이자를 제공하는 상품이에요.
단, 약정 RP는 약정된 이자율의 고정되어 있지만, 수시 RP는 시장금리와 연동하여 1년에 12회까지 금리변동이 가능해요.', '조건환매, 환매채권, 예금, 은행, 기간없이, rp', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 106, '채권', '약정 RP', '약정RP, 약정식 RP는 은행의 정기예금과 비슷한 상품이에요. 약정기간 후에 약정한 이자율로 이자를 지급해 드려요.
약정기간 내 환매시에는 중도환매이율이 적용되며 약정 RP 만기 이후에는 약정 후 이율이 적용되오니 만기 시 매도처리 하셔야 해요.', '조건환매, 환매채권, 예금, 은행, 기간 약정', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 107, '', '특판 RP', '특판 RP의 경우 특정대상을 상대로 판매되는 상품으로, 판매 여부는 지점으로 확인해 주세요. 
참고로, 특판 RP는 약정기간, 최소가입금액에 맞춰 매수가 되며 약정기간 내 환매 시에는 중도환매이율이 적용돼요.', '조건환매, 환매채권, 예금, 은행, 우대, rp', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 108, '채권', 'RP 중도환매 가능여부', 'RP는 약정기간내 중도 해지가 가능하나, 약정식RP의 경우 중도환매시에는 약정이율이 적용되지 않고 중도환매이율이 적용돼요.
환매 전 중도환매 이율을 확인해 주세요.', 'RP 환매, RP 매도, 현금화, rp, 해지', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 109, '채권', 'RP 매수 방법', 'RP매수는 지점, 유선, Family Center 및 온라인상에서 가능하며, mPOP에서는 RP매매 화면에서 처리하시면 돼요.
 
□ 매매 가능시간
- 지점 : 09:00~16:00
- 온라인 : 08:00~16:45
', 'rp, RP 사자, RP 사는법, 매수방법', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 110, '채권', 'RP 매도 방법', 'RP매도는 지점, 유선, Family Center 및 온라인상에서 가능하며, mPOP에서는 RP매매 화면에서 처리하시면 돼요.
약정RP 중도 매도시에는 약정이율 적용이 되지 않고, 중도환매이율이 적용 되오니 확인 후 매도 신청해 주세요.

□ 매매 가능시간
- 지점 : 09:00~16:00
- 온라인 : 08:00~16:45', 'rp, RP 팔자, RP 파는법, 매도방법, 해지', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 111, '채권', 'RP 매매가능시간', 'RP(수시, 약정식, 특판) 매매 가능시간은 아래와 같아요.
 
□ 지점 : 평일 09:00 ~ 16:00
□ 온라인 : 평일 08:00 ~ 16:45', 'rp, RP매매, 매매시간', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 112, '채권', 'RP 중도환매이율', '약정식 RP(특판 RP 포함)를 약정 기간내 중도 환매시에는 아래와 같이 중도환매이율이 적용돼요.
참고로, 약정식 RP를 만기일에 환매하지 않은 경우에는 약정 후 이율이 적용되어 금리상 불이익이 발생할 수 있어요.

- 약정기간의 20% 미만까지 : 약정이율 * 15% * (경과일수/365)지급
- 약정기간의 50% 미만까지 : 약정이율 * 30% * (경과일수/365)지급
- 약정기간의 90% 미만까지 : 약정이율 * 60% * (경과일수/365)지급
- 만기일 전까지 : 약정이율 * 90% * (경과일수/365)지급
- 만기일 후 : 약정이율 * 0.75% * (경과일수/365)지급', 'rp, RP환매, 중도매도, 해지', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 113, '채권', 'RP 원금보장 여부', 'RP는 예금자보호법에 따라 예금보험공사가 보호하지 않아 원금 손실 가능성이 있어요. 
단, 국공채나 AAA 이상의 귬융채에 투자하고, 당사에서 재매입을 보장하므로 안정성이 높아요.', 'RP예금자, 원금보호, RP보호, 은행예금', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 114, '채권', '채권', '채권이란, 정부, 공공기관과 주식회사 등이 비교적 거액의 자금을 일시에 조달하기 위해서 발행하는 유가증권을 말해요. 
변동금리상품과는 달리, 매수할 때에 만기까지 보유 시 받을 수 있는 원금과 이자가 약정되어 있으며, 원금의 상환기간이 발행 시 정해져 있는 상품이에요.', '채권상품, 예금, 국채, 회사채, 지역채, 단기, bond, 금리, 이자', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 115, '채권', '장외채권 시장', '장외채권 시장은, 상장, 비상장채권을 한국거래소를 통하지 않고 거래되는 시장을 의미해요.', '채권, 채권매매', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 116, '채권', '국고채', '국고채는, 정부를 발행 주체로 하는 채권(국채)를 종합관리하기 위해 발행되는 채권을 말해요.
한국은행이 발행 취급을 맡고 있으며, 시장실세금리로 발행되는 것이 특징이에요. 
1년, 3년, 5년, 10년, 20년, 30년짜리가 발행되었는데, 만기 3년짜리가 주로 발행되고 있어요. 특히 3년 만기 국고채 유통수익률은 대표적인 시장금리 중의 하나로 우리나라의 시중자금 사정을 나타내는 기준금리로 사용되고 있어요.', '국채, 지방채, 국가채권, 한국채권', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 117, '채권', 'CP', 'CP(Commercial Paper)란, 신용도가 높은 우량기업이 자금조달을 목적으로 발행하는 단기의 무담보 단명어음이에요.', '어음, 기업어음, 기업', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 118, '채권', 'CD', 'CD(Certificate of Deposit)란, 양도성 예금증서라고 하며 은행 정기예금에 양도성을 부여한 것으로 은행이 발행하고 증권회사와 종합금융회사의 중개를 통해 매매되고 있어요.
예금통장과 달리 통장에 이름을 쓰지않아 무기명이며, 중도해지는 불가능하나 양도가 자유로워 현금화가 용이한 유동성 높은 상품이에요.', '', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 119, '채권', '물가연동국채', '물가연동국채는, 재경부에서 발행하는 국채로 원금 및 이자지급액을 물가에 연동시켜 국채투자에 따른 물가변동위험을 제거함으로써 채권의 실질구매력(purchasing power)을 보장하는 국채예요.
물가에 연동하는 원금 상승분에 대해서는 비과세 혜택으로 절세효과가 있으며, 이표는 분리과세 가능한 채권이에요.', '물가채, 물가채권, 국가채권', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 120, '채권', '국민주택채권', '국민주택채권은, 서민주택건설등에 필요한 자금 조달을 위해 국민주택기금에서 발행되는 국채 (5년 만기, 3%)를 말해요.
정부가 국민주택사업에 필요한 자금을 조달하기 위하여 발행하는 채권으로 1종과 2종이 있어요. 
국가/지방자치단체로부터 면허/허가/인가를 받거나 등기/등록을 신청하는 자, 국가/지방자치단체/정부투자기관과 건설공사의 도급계약을 체결하는 자는 제1종 국민주택채권을 매입하여야 해요.', '국민채권, 보상채, 아파트, 집채권', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 121, '채권', '지역개발채권', '지역개발채권은, 지방자치단체인 서울특별시, 광역시, 도 등이 지방재정법의 규정에 의거하여 특수목적 달성에 필요한 자금을 조달하기 위해 첨가소화방식으로 발행하는 채권이에요.', '지역채, 특수채, 첨가채', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 122, '채권', '이표채', '이표채란, 일정기간마다 이자를 지급하는 채권으로 대부분의 회사채, 3년 이하 국고채 등이 있어요.', '채권이자, 이자, 예금', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 123, '채권', '할인채', '할인채란, 이자를 선지급하는 채권으로 재정증권, 통화안정증권 등이 있어요.', '선이자, 이자 먼저', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 124, '채권', '복리채', '복리채는, 이자를 지급하지 않고 복리로 재투자하여 만기상환시에 원금과 이자를 동시에 지급하는 채권이에요. 
국민주택채권, 지역개발공채 등이 해당 돼요.', '이자 불리는, 불리기, 복리채권', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 125, '채권', '단리채', '단리채는, 정기적으로 이자가 지급되는 대신에 단리로 재투자되어 만기상환시에 원금과 이자를 동시에 지급하는 채권이에요.', '딘리채권', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 126, '채권', '장외채권 주문방법', '장외 채권 매매는 지점, 유선 및 온라인 상에서 가능하며, mPOP에서는 장외채권매매 화면을 통해 거래하실 수 있어요.  

□ 유선 주문 가능 채권
- 초저위험인 국채, 지방채, 보증채의 경우 유선 매매 가능
- 물가채는 매수 시에는 지점내방, 매도 시에는 유선가능  
 
매매가능종목 확인 및 매매 불가 사유 발생 시 지점을 통해 확인해 주세요.', '채권매매, 채권상품, 채권 사는법, 채권 파는법, 채권 매수, 채권 매도', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 127, '채권', '장외채권 주문시간', '장외채권 주문은 지점 내방 또는 유선접수시 09:00 ~ 16:00이며, 온라인상에서는 영업일 08:00 ~ 16:45까지 주문이 가능해요.', '채권매매, 채권주문, 채권시간', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 128, '채권', '장외채권 매매 수수료', '장외채권은 매매수수료가 발생하지 않아요.', '채권수수료, 채권매매', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 129, '채권', '장외채권 주문 취소방법', '장외채권매매취소는 지점, 유선, 온라인상에서 가능하며, mPOP에서는 장외채권매매 화면에서 처리하실 수 있어요.
 
□ 매매가능시간
- 지점 : 영업일 09:00 ~ 16:00
- 온라인 : 영업일 08:00 ~ 16:45', '채권취소, 매매취소, 채권주문, 채권매매', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 130, '채권', '채권 분리과세 신청 방법', '종합과세소득의 범위에 속하는 소득 중 종합소득 과세표준에 합산하지 아니하는 소득에 대한 세금을 분리과세 해요.

□ 분리과세 가능채권
- 2004년 1월 이전 발행분 5년 이상의 장기 채권
- 2004년 1월 발행분부터 10년 이상의 장기채권
→ 전환사채의 경우 전환기간이 10년 이상이어야함.
- 2013년 1월 1일 이후 발행되는 10년 이상 장기 채권에 대해서는 매입일로부터 3년이 지난 후에 발생하는 이자에 대해서만 분리과세 가능
- 2018년 1월 발행분부터 장기채권 분리과세 폐지

□ 지정방법
- 이자지급일 및 원리금 상환 지급일 포함 4일전(영업일 기준)까지 지정/해제 가능
- 실물입고된 채권의 경우 관리팀 입고 확인 후 분리과세지정 가능
- 개인만 가능하며(법인 불가) 지점 내방/유선처리 가능
', '종소득, 개별과세, 과세, 금융소득, 채권세금', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 131, '채권', '채권 세금', '채권투자시 세금은, 매도 시 보유 기간에 따른 채권의 이자 소득을 계산하여 매도 시 과세돼요.
 
□ 이자소득 세율 : 15.4% (이자소득세 14% + 지방소득세 1.4%(이자소득세의 10%))', '채권세금, 채권이자, 채권과세', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 132, '계좌관리', 'CMA', 'CMA는, 계좌에 예치된 금액을 단기 고금리 상품에 운용하여 자유로운 수시 입출금이 가능하면서도 일반적인 시중금리형 예금상품보다 더 높은 수익률을 드림과 동시에 각종 투자혜택까지 더한 상품이에요.
CMA+ 자동투자상품은 단기 금융상품인 CMA RP형(환매조건부채권), CMA MMF 형, CMA MMW형 등의 유형으로 투자되며, CMA+연계 신용/체크카드를 통해 카드 한장으로 증권/뱅킹 거래와 체크/신용카드의 편리한 기능은 물론 다양한 혜택까지 받을 수 있어요.  ', '씨엠에이, 예금, 수시, 입출금, cma', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 133, '계좌관리', 'CMA 혜택', '삼성증권 CMA+ 에 가입하시면 높은 수익률(우대 수익률), 자유로운 이체/입출금, 자동납부서비스(급여이체 및 공과금과 카드대금납부), 캐쉬리워드, 공모주청약한도우대 등의 서비스를 받아보실 수 있어요.

※ 위 혜택은 조건에 따라 적용이 다를 수 있으니 상세 내역은 홈페이지 또는 Family Center(1588-2323)로 문의 해주세요.', '씨엠에이, 예금, 수시, 입출금', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 134, '계좌관리', 'CMA 계좌개설 방법', 'CMA 신규계좌개설은 지점 내방 또는 mPOP 비대면계좌개설 화면에서 투신> CMA RP만 개설 가능하며, 원하시는 경우 CMA 기초자산 유형을 MMF, MMW로 변경이 가능해요.
단, MMW로 변경시 온라인에서 약정 불가하고 지점 내점으로만 가능해요.
기존 삼성증권 계좌가 있으시다면 mPOP 온라인추가계좌개설 화면에서 CMA계좌를 개설하실 수 있어요.', '씨엠에이, 예금, 수시, 입출금, 개설방법, CMA개설', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 135, '계좌관리', 'CMA 가입자격', '삼성증권 CMA는 내국인 개인 중 국내거주자가 가입이 가능해요.
단, 만18세 이상 미성년자의 경우 본인 실명확인증표 지참하여 지점 방문 또는 법정대리인이 징구서류 지참 시 투신계좌개설 및 CMA 가입이 가능해요.', '씨엠에이, 예금, 수시, 입출금, 개설방법, CMA개설, CMA자격', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 136, '계좌관리', 'CMA 약정 가능 계좌', 'CMA 약정은 투신, 연금저축 계좌에서 가능해요.
단, 연금저축계좌는 MMF로만 약정 가능해요.', 'CMA약정, 씨엠에이', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 137, '계좌관리', 'CMA 우대수익률 적용조건', 'CMA RP형에 가입하시고 아래 조건 2개 이상 조건 만족시 최대 2.05% 우대수익률을 제공해 드려요.
 
① 급여 월 50만원 또는 공적연금 수령/등록
(국민연금, 공무원연금, 군인연금, 사학연금에 한하며 금액 제한 없음)
※ 공무원연금, 군인연금, 사학연금은 별도로 지점에 신청 해야함.
② 카드, 보험, 휴대폰요금 등 공과금자동이체, 국민연금을 연금관리공단에 자동이체납부시 월 1건이상

- 조건 중 1개 만족 (CMA RP금리 + 1.55%추가)
- 조건 중 2개 만족 (CMA RP금리 + 2.05%추가)
- 제공기간 : 고객 기준으로 최대 6개월  
- 우대수익률 적용한도 : 500만원 한도 적용
- 위 조건 일치 시 별도의 신청없이 조건 만족 익월 자동 적용', 'CMA우대, 우대기준', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 138, '계좌관리', 'CMA 약정 신청방법', 'CMA약정은 RP, MMF, MMW 기초자산으로 선택하여 약정이 가능해요. 
단, MMW는 온라인에서 약정 불가하고 지점 내점으로만 가능해요.

□ 기존 투신계좌가 있는 경우 
- 지점(내방/유선), 홈페이지, HTS, mPOP 약정신청가능
- mPOP에서는 CMA서비스신청 화면에서 신청 가능
 
□ 투신계좌가 없는 경우
- 온라인추가계좌개설 또는 비대면계좌개설 시 CMA 약정 가능 ', 'CMA약정, 씨엠에이', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 139, '계좌관리', 'CMA 기초자산', 'CMA 기초자산에 대해 알려드릴게요.

□ CMA RP형(환매조건부채권) :  국채, 통안채, AAA이상의 은행채 등 우량채권만을 엄선하여 수익성과 안정성을 동시에 추구
□ CMA MMF형 : 펀드의 일종인 MMF에 투자하는 상품으로 안정성과 환급성이 뛰어나며 수익률은 매일 변동
□ CMA MMW형 : AAA신용등급인 한국증권금융의 1일물예금(익영업일 만기)에 투자하는 상품으로 매 영업일 이자를 정산하여 복리 효과 발생', 'CMA자산, 씨엠에이, mmf, mmw, rp', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 140, '계좌관리', 'CMA 기초자산 변경방법', 'CMA 기초자산 변경은 언제든지 유선/온라인/모바일을 통해서 변경가능해요. 
단, MMW는 온라인에서 약정 불가하고 지점 내점으로만 가능해요.
CMA서비스 신청 시 변경되는 CMA기초자산의 서비스 약관 및 투자설명서, 핵심설명서를 먼저 확인 해주세요.

※ CMA 변경, 해지 시 유의사항
- 변경 가능 시간은 영업일 09:00 ~ 16:00까지 처리 가능
- 기초자산 변경 시 변경 전 기초자산으로 정기자동이체(타사→당사)신청된 건은 자동이체 되지 않으므로 변경 후 기초자산으로 다시 자동이체 신청 필요', 'CMA자산, 씨엠에이', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 141, '계좌관리', 'CMA 이율', 'CMA는 3가지 기초자산(RP, MMF, MMW)으로 운영되며, 이율은 기초자산별로 상이해요.
CMA RP 형의 경우 세전 연 0.4% 약정수익을 받으실 수 있으며, 약정 이율은 당해 수익률 시장금리 상황에 따라 변동될 수 있어요.(2020년3월18일기준)
CMA이율 문의 사항은 지점 또는 Family Center(1588-2323) 통해 문의 해주세요.', 'CMA 이자율, CMA 금리, 씨엠에이, CMA이율, dlwk', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 142, '계좌관리', 'CMA 해지 방법', 'CMA 해지는 지점내점/유선, 홈페이지, HTS, mPOP 모든 매체를 통해 해지가 가능해요
mPOP에서는 CMA서비스신청 화면에서 가능하며, 영업일 09:00~16:00에 신청 하실 수 있어요.', 'CMA해지', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 143, '계좌관리', 'CMA 예금자보호 적용 여부', 'CMA는 RP, MMF, MMW에 투자하는 상품으로, 예금자보호법에 의한 보호대상이 아니예요.', '원금보장, 예금보호, 은행예금, 예금, 보장', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 144, '계좌관리', 'CMA 이자금액 조회방법', 'CMA 이자는 mPOP 종합잔고 화면 평가손익 항목에서 확인하실 수 있어요.', 'CMA이자, 원리금', 'N', sysdate, '1999999991', sysdate, '1999999991');
insert into tb_term_mgt (term_num, term_div_nm, title, cont, term_tag, del_yn, create_dt, creater, update_dt, updater) values  ( 145, '계좌관리', 'CMA 급여계좌 등록', 'CMA통장을 급여계좌로 등록하면, 급여 관리, 공모주, 이체수수료 무료 혜택을 받아보 실 수 있어요
등록방법은 고객님의 재직중인 회사에 삼성증권 계좌번호를 등록하여 급여일에 입금이 되면 자동으로 인식되어 급여계좌가 지정돼요.
급여계좌 수기등록을 원하신다면 지점 내방 또는 유선으로 급여계좌등록 신청이 가능하며, 급여 50만원 이상 지정하신 날짜에 급여가 입금되어야 해요.', 'CMA급여, 우대, 급여통장', 'N', sysdate, '1999999991', sysdate, '1999999991');