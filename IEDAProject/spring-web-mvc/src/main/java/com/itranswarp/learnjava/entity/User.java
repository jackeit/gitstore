package com.itranswarp.learnjava.entity;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class User {

	private Long id;
	private String email;
	private String password;
	private String name;

	private long createdat;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getCreatedAt() {
		return createdat;
	}

	public void setCreatedAt(long createdat) {
		this.createdat = createdat;
	}

	public String getCreatedDateTime() {
		return Instant.ofEpochMilli(this.createdat).atZone(ZoneId.systemDefault())
				.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}

	public String getImageUrl() {
		try {
//			MessageDigest md = MessageDigest.getInstance("MD5");
//			byte[] hash = md.digest(this.email.trim().toLowerCase().getBytes(StandardCharsets.UTF_8));
//			return "https://www.gravatar.com/avatar/" + String.format("%032x", new BigInteger(1, hash));
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hash = md.digest(this.email.trim().toLowerCase().getBytes(StandardCharsets.UTF_8));
//			return "https://www.gravatar.com/avatar/" + String.format("%032x", new BigInteger(1, hash));
			return "/static/image/1.jpg";
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("User[id=%s, email=%s, name=%s, password=%s, createdAt=%s, createdDateTime=%s]", getId(),
				getEmail(), getName(), getPassword(), getCreatedAt(), getCreatedDateTime());
	}
}
