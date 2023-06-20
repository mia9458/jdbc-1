select * from TBL_STUDENT where stuno = '2023002';
select * from TBL_SCORE;
select * from TBL_STUDENT;
-- 여기서부터는 다른 테이블로 연습 합니다.
/*
1. 회원 로그인 - 간단히 회원아이디를 입력해서 존재하면 로그인 성공
2. 상품 목록 보기
3. 상품명으로 검색하기 (그외에 가격대 별 검색)
4. 상품 장바구니 담기 - 장바구니 테이블이 없으므로 구매를 원하는 상품을 main에서 List 로 담기
5. 상품 구매(결제)하기 - 장바구니의 데이터를 `구매` 테이블에 입력하기 (여러개 insert)
6. 나의 구매 내역 보기. 총 구매 금액을 출력해 주기
*/
select * from TBL_CUSTOM;
select * from TBL_PRODUCT;
select * from TBL_PRODUCT where pname like '%' || '동원' || '%';

select * from tbl_buy;		-- 구매 정보 테이블
select * from tbl_buy where customid='mina012';
-- 기존에 연습했던 테이블을 변경하지 않도록 새롭게 복사해서 jdbc 구현합니다.
create table j_custom
as
select * from tbl_custom;		

select * from j_custom;

create table j_product
as
select * from TBL_PRODUCT;	

select * from j_product;

create table j_buy
as
select * from tbl_buy;

select * from j_buy;

-- pk,fk는 필요하면 제약조건을 추가 합니다.
--  custom_id , pcode, buy_seq 컬럼으로 pk 설정하기
--  tbl_buy 테이블에는 외래키도 2개가 있습니다.(j_buy 외래키 설정 제외하고 하겠습니다.)

alter table j_custom add constraint custom_pk primary key (custom_id);
alter table j_product add constraint product_pk primary key (pcode);
alter table j_buy add constraint buy_pk primary key (buy_seq);


-- 추가 데이터 입력
insert into j_product values ('ZZZ01','B1','오뚜기바몬드카레',2400);
insert into j_product values ('APP11','A1','얼음골사과 1박스',32500);
insert into j_product values ('APP99','A1','씻은사과 10개',25000);

-- j_buy 테이블에 사용할 시퀀스
drop sequence jbuy_seq;			-- 시퀀스 삭제
-- 적절한 시작값으로 다시 생성하기
create sequence jbuy_seq start with 1008;	
select jbuy_seq.nextval from dual;

-- delete from j_buy where buy_seq = 1029;
-- rollback 테스트
select * from j_buy;
alter table j_buy add constraint q_check check (quantity between 1 and 30);
-- check 제약조건 오류
insert into j_buy values (jbuy_seq.nextval,'twice','APP99',33,sysdate);		

-- 6.마이페이지 구매내역
-- 2개 테이블 join 하여 행단위로 합계(수량*가격) 수식까지 조회하기
select buy_date , p.pcode ,pname, quantity, price, quantity*price total from j_buy  b
join j_product p
on p.pcode=b.pcode
and b.customid='twice'
order by buy_date desc
;
-- 자주 사용될 join 결과는 view 로 만들기. view 는 create or replace 로 생성후 에 수정까지 가능.
-- view 는 물리적인 테이블인 아닙니다. 물리적 테이블을 이용해서 만들어진 가상의 테이블(논리적 테이블)
create or replace view mypage_buy
as
select buy_date , customid, p.pcode ,pname, quantity, price, 
quantity*price total from j_buy  b
join j_product p
on p.pcode=b.pcode
order by buy_date desc
;

select * from mypage_buy where customid='twice';


select sum(total) from mypage_buy where customid='twice';


-- 6월 19일 로그인 구현하기 위하 패스워드 컬럼 추가를 합니다.
-- 패스워드 컬럼은 해시값 64문자를 저장합니다.
alter table j_custom add password char(64);

-- twice 만 패스워드 값 저장하기
update j_custom set 
password = '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4'
where custom_id ='twice';

select * from j_custom;
















