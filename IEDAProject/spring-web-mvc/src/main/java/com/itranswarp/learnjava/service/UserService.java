package com.itranswarp.learnjava.service;

import java.util.List;

import com.itranswarp.learnjava.mapper.UserMapper;
import com.itranswarp.learnjava.entity.Diary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.itranswarp.learnjava.entity.User;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class UserService {

	final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	UserMapper userMapper;
//	@Autowired
//	JdbcTemplate jdbcTemplate;

//	RowMapper<User> userRowMapper = new BeanPropertyRowMapper<>(User.class);

	public User getUserById(long id) {
//		return jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?", new Object[] { id }, userRowMapper);
		return userMapper.getById(id);
	}

	public User getUserByEmail(String email) {
//		return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email = ?", new Object[] { email },
//				userRowMapper);
		return userMapper.getByEmail(email);
	}

	public User signin(String email, String password) {
		logger.info("try login by {}...", email);
		User user = getUserByEmail(email);
		if (user.getPassword().equals(password)) {
			return user;
		}
		throw new RuntimeException("login failed.");
	}

	public User register(String email, String password, String name) {
		if((userMapper.getByEmail(email)!=null)||(userMapper.getByName(name)!=null)){
			throw new RuntimeException("Insert failed.");
		}
		logger.info("try register by {}...", email);
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setName(name);
		user.setCreatedAt(System.currentTimeMillis());
//		KeyHolder holder = new GeneratedKeyHolder();
		if (1 != userMapper.insert(user)) {
			throw new RuntimeException("Insert failed.");
		}
//		user.setId(holder.getKey().longValue());
//		userMapper.createTable(name);
		return user;
	}

	public List<Diary> GetDiaryList(String name){
		return userMapper.getDiary(name);
	}
	public int insertDiary(Diary diary){
		return userMapper.insertDiary(diary);
	}

	public Diary getDiaryByid(long id){
		return userMapper.getDiaryById(id);
	}

	public int deleteDiaryById(long id){
		return userMapper.deleteDiaryById(id);
	}
//	public void updateUser(User user) {
//		if (1 != jdbcTemplate.update("UPDATE user SET name = ? WHERE id=?", user.getName(), user.getId())) {
//			throw new RuntimeException("User not found by id");
//		}
//	}

}
