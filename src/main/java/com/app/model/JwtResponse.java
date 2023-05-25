package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtResponse {

	private String token;

	public JwtResponse(String token) {
		super();
		this.token = token;
	}
}
