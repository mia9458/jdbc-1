package koreait.jdbc.day4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import koreait.jdbc.day2.OracleUtility;

public class JBuyDao {		//구매와 관련된 CRUD 실행 SQL. DAO : JCustomerDao , JProductDao
//메소드 이름은 insert , update , delete , select , selectByPname 등등으로 이름을 작성하세요.

		//*트랜잭션을 처리하는 예시 : auto commit 을 해제하고 직접 commit을 합니다.*
		//try catch 를 직접하세요.throws 아닙니다.
		public int insertMany(List<JBuy> carts) {
			Connection conn = OracleUtility.getConnection();
		//5. 상품 구매(결제)하기 - 장바구니의 데이터를 `구매` 테이블에 입력하기 (여러개 insert)	
			String sql ="insert into j_buy \n" + 
					"values (jbuy_seq.nextval , ? ,?,?,sysdate)";
			int count = 0;
			PreparedStatement ps = null;
			try {
				conn.setAutoCommit(false);			//auto commit 설정 - false
				ps = conn.prepareStatement(sql);
				for(JBuy b : carts) {		//리스트의 사이즈 만큼 반복
					ps.setString(1,b.getCustomid());
					ps.setString(2,b.getPcode());
					ps.setInt(3,b.getQuantity());
					count += ps.executeUpdate();
				}
				conn.commit();				//커밋
			}catch (SQLException e) {
				System.out.println("장바구니 상품 구매하기 예외 : " + e.getMessage());
				System.out.println("장바구니 상품 구매를 취소합니다.");
				try {
					conn.rollback();		//롤백
				} catch (SQLException e1) {
				}
			}
			return count;
		} 
//	 select * from mypage_buy where customid='twice';
	 public List<MyPageBuy> mypageBuy(String customid) throws SQLException{
		 Connection conn = OracleUtility.getConnection();
			String sql = "select * from mypage_buy where customid= ?"; 
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, customid);
			ResultSet rs = ps.executeQuery();
			
			List<MyPageBuy> list = new ArrayList<>();
			while(rs.next()) {
				list.add(new MyPageBuy(rs.getDate(1), 
						rs.getString(2), 
						rs.getString(3), 
						rs.getString(4), 
						rs.getInt(5), 
						rs.getInt(6),
						rs.getLong(7)));
			}
			return list;
	 }

//	 select sum(total) from mypage_buy where customid='twice';
	 public long myMoney(String customid) throws SQLException {
		 Connection conn = OracleUtility.getConnection();
		 String sql = "select sum(total) from mypage_buy where customid= ?"; 
		 //함수 조회하는 select는 항상 결과행이 1개, 컬럼도 1개
		 
		 PreparedStatement ps = conn.prepareStatement(sql);
		 ps.setString(1, customid);
		 ResultSet rs = ps.executeQuery();
		 
		 rs.next();
		 long sum = rs.getLong(1);
		 
		 return sum;
	 }
	 
	public int insert(JBuy buy) {
		return 1;
	}
	public JBuy selectOne(int buyseq) throws SQLException {
		// sql 실행을 구현을 하고 테스트 케이스 확인하기
		Connection conn = OracleUtility.getConnection();
		String sql = "select * from j_buy where buy_seq = ?";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, buyseq);
		
		JBuy buy = null;
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			buy = new JBuy(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getDate(5));
		}
		return buy;
	}
}
