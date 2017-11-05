package springbook.user.dao;

import springbook.user.domain.User;

import java.sql.*;

public class UserDao {

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection c = DriverManager.getConnection("jdbc:sqlite:./sample.db");
        return c;
    }

    public void add(User user) throws ClassNotFoundException , SQLException {

        Connection c  = getConnection();

        PreparedStatement pstmt = c.prepareStatement(
                "insert into users( id , name , password ) values( ? , ? , ?)");
        pstmt.setString( 1 , user.getId());
        pstmt.setString( 2 , user.getName());
        pstmt.setString( 3 , user.getPassword());

        pstmt.executeUpdate();

        pstmt.close();
        c.close();
    }

    public User get( String id ) throws ClassNotFoundException, SQLException {
        Connection c  = getConnection();

        PreparedStatement pstmt = c.prepareStatement("select * from users where id = ?");
        pstmt.setString(1,id );
        ResultSet rs = pstmt.executeQuery();

        rs.next();

        User user =new User();
        user.setId( rs.getString("id"));
        user.setName( rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        pstmt.close();
        c.close();
        return user;
    }

    public void deleteAll() throws ClassNotFoundException, SQLException {
        Connection c  = getConnection();
        c.createStatement().execute("delete from users");
        c.close();
    }
}
