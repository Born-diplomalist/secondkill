package com.born.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.born.domain.entity.User;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserUtil {
	
	private static void createUser(int count) throws Exception{
		List<User> userList = new ArrayList<User>(count);
		//生成用户
		for(int i=0;i<count;i++) {
            User user = new User();
            user.setUserName(1300000000000L+i+"");
            user.setUserSalt("7c9sr6");
            user.setUserPassword(MD5Util.inputPassToDbPass("123456", user.getUserSalt()));
            user.setUserRoleId(0);
            user.setUserPhone(1300000000000L+i);
            userList.add(user);
		}
		System.out.println("create user");

		/*
		 * 创建文件，向文件中写入生成的主键ID
		 */
		String fileName="generateid.txt";
		File file = new File(fileName);
		try {
            file.createNewFile();//返回值创建是否成功
        } catch (IOException e) {
            e.printStackTrace();
        }


//		//插入数据库
		Connection conn = DBUtil.getConn();
		String sql = "insert into user(user_name,user_password,user_salt,user_role_id,user_phone,user_last_login_date)values(?,?,?,?,?,now())";
		PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		for(int i=0;i<userList.size();i++) {
			User user = userList.get(i);
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserSalt());
			pstmt.setInt(4, user.getUserRoleId());
			pstmt.setLong(5,user.getUserPhone());
			//pstmt.setTimestamp(6, new java.sql.Timestamp(user.getUserLastLoginDate().toInstant(ZoneOffset.ofHours(8)).toEpochMilli()));
			pstmt.execute();
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs != null&&rs.next()) {
				writeFile(fileName,""+rs.getInt(1));
			}

			//pstmt.addBatch();
		}
		pstmt.close();
		conn.close();
		//pstmt.executeBatch();
		System.out.println("insert to db");
		System.out.println("over");
	}


	//指定内容写入文件并换行
	public static void writeFile(String fileName,String context){
		try {
            BufferedWriter out = null;
            out = new BufferedWriter(new FileWriter(fileName,true));
            out.write(context+"\n");
            out.close();
        }
            catch (IOException e) {
            System.out.println("exception occoured"+ e);
        }
	}
	
	public static void main(String[] args)throws Exception {
		createUser(5000);
	}
}
