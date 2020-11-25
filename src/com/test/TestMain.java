package com.test;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.test.JDBCUtils;
import java.sql.Statement;

import static java.lang.System.out;
//import org.junit.Test;



public class TestMain {
	public static void main(String[] args) throws DocumentException, ParseException {

		Connection conn = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		//String str = "202001010000";
		//Date date = sdf.parse(str);
		//System.out.println("date="+date+", str="+str);
		//System.out.println("seconds="+date.getTime());
		Calendar calendar = Calendar.getInstance();
		//calendar.setTimeInMillis(date.getTime());//×ª»»ÎªºÁÃë
		//System.out.println("data="+calendar.getTime());


		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://47.92.48.239:3306/pan", "root", "WJSytp3262");

			stmt = conn.createStatement();
			String sql = "select * from T_KB";
			String ZZT = "";
			Long data_msec=0L;

			int m=0;
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				m++;
				if (m > 5) {
					//break;
				}

				String XN_DATE = rs.getString("XN")+"01010000";
				Date date_start =  sdf.parse(XN_DATE);
				calendar.setTime(date_start);
				int START_XQJ = calendar.get(Calendar.DAY_OF_WEEK)-1;
				System.out.println("XN="+calendar.getTime());
				System.out.println("20200101ÐÇÆÚ£º"+START_XQJ);
				ZZT = rs.getString("ZZT");
				for (int i = 0; i < ZZT.length(); i++) {
					if(rs.getString("XN").equals("2020") && i<40 ){
						continue;
					}
					//System.out.println(ZZT.charAt(i));
					if(ZZT.charAt(i) == '1'){

						String sql1 = "insert into T_JSZYSJ values (?,?,?,?,?,null,null)";
						pstmt = conn.prepareStatement(sql1);
						pstmt.setString(1,rs.getString("WID")+"-"+(i+1)+"-"+rs.getString("XQJ"));
						pstmt.setString(2,rs.getString("JSDM"));

						data_msec = date_start.getTime()+i*604800L*1000L+(Integer.valueOf(rs.getString("XQJ"))- START_XQJ)*86400L*1000L;
						calendar.setTimeInMillis(data_msec);
						System.out.println("re_data="+calendar.getTime());

						pstmt.setString(3,calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH));
						//pstmt.setString(3,rs.getString("XN")+"-"+(i+1)+"-"+rs.getString("XQJ"));
						pstmt.setString(4,rs.getString("KSSJ"));
						pstmt.setString(5,rs.getString("JSSJ"));
						pstmt.executeUpdate();
						System.out.println(rs.getString("XN")+"-"+(i+1)+"-"+rs.getString("XQJ"));
					}else{
						continue;
					}
				}


			}

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(pstmt != null){
				try {
					pstmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				pstmt = null;
			}

			if(stmt != null){
				try {
					stmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				stmt = null;
			}
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
				conn = null;
			}
		}

		
		
	}

}
