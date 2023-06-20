package koreait.jdbc.day4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import koreait.jdbc.day2.OracleUtility;

public class JProductDao {		//구매와 관련된 CRUD 실행 SQL. DAO : JCustomerDao , JProductDao
//메소드 이름은 insert , update , delete , select , selectByPname 등등으로 이름을 작성하세요.
	//2. 상품 목록 보기
	public List<JProduct> selectAll() throws SQLException{
		Connection conn = OracleUtility.getConnection();
		String sql = "select * from j_product";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		List<JProduct> list = new ArrayList<>();
		while(rs.next()) {
			list.add(new JProduct(rs.getString(1), 
					rs.getString(2), 
					rs.getString(3), 
					rs.getInt(4)));
		}
		ps.close();
		conn.close();
		return list;
	}
	
	//3. 상품명으로 검색하기 (유사검색-> **`검색어가 포함된 상품명`**을 목록 조회하기)
	public List<JProduct> selectByPname(String pname) throws SQLException{
		//pname은 사용자가 입력한 검색어
		Connection conn = OracleUtility.getConnection();
		String sql = "select * from j_product "
				+ "where pname like '%' || ? || '%'";   //like는 유사 비교. % 기호 사용
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, pname);
		ResultSet rs = ps.executeQuery();
		
		List<JProduct> list = new ArrayList<>();
		while(rs.next()) {
			list.add(new JProduct(rs.getString(1), 
					rs.getString(2), 
					rs.getString(3), 
					rs.getInt(4)));
		}
		return list;
	}
}
