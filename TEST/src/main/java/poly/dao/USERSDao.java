package poly.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import poly.entity.USERS;
import poly.utils.JdbcHelper;

public class USERSDao extends Dao<USERS, String> {
	final String INSERT_SQL = "INSERT INTO USERS(Id, Password, Fullname, Email, Admin) VALUES(?,?,?,?,?)";
	final String UPDATE_SQL = "UPDATE USERS SET Password=?, Fullname=?, Email=?, Admin=? WHERE Id=?";
	final String DELETE_SQL = "DELETE FROM USERS WHERE Id=?";
	final String SELECT_ALL_SQL = "SELECT * FROM USERS";
	final String SELECT_BY_ID_SQL = "SELECT * FROM USERS WHERE Id=?";

	@Override
	public void insert(USERS entity) {
		JdbcHelper.update(INSERT_SQL, entity.getId(), entity.getPassword(), entity.getFullname(), entity.getEmail(),
				entity.getAdmin()); // Remove unnecessary fields
	}

	@Override
	public void update(USERS entity) {
		JdbcHelper.update(UPDATE_SQL, entity.getPassword(), entity.getFullname(), entity.getEmail(), entity.getAdmin(),
				entity.getId());
	}

	@Override
	public void delete(String id) {
		JdbcHelper.update(DELETE_SQL, id);
	}

	@Override
	public List<USERS> selectAll() {
		return selectBySql(SELECT_ALL_SQL);
	}

	@Override
	public USERS selectByid(String id) {
		List<USERS> list = selectBySql(SELECT_BY_ID_SQL, id);
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public List<USERS> selectBySql(String sql, Object... args) {
		List<USERS> list = new ArrayList<>();
		try (ResultSet rs = JdbcHelper.query(sql, args)) {
			while (rs.next()) {
				USERS entity = new USERS();
				entity.setId(rs.getString("id"));
				entity.setPassword(rs.getString("Password"));
				entity.setFullname(rs.getString("Fullname"));
				entity.setEmail(rs.getString("Email"));
				entity.setAdmin(rs.getBoolean("Admin")); // Adjust field to match Admin
				list.add(entity);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Database error: " + e.getMessage(), e);
		}
		return list;
	}

}
