package com.app.controller;

import java.io.IOException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
//import org.json.JSONObject;
import com.app.helper.JwtUtil;
import com.app.model.JwtRequest;
import com.app.model.JwtResponse;
import com.app.model.UserData;
import com.app.service.CustomUserDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@ResponseBody
public class JwtController {

	
	 @org.springframework.beans.factory.annotation.Autowired(required=true)
	  private BCryptPasswordEncoder bCryptPasswordEncoder;
	 
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailService customuserdetailsservice;

	@Autowired
	private JwtUtil jwtutil;
//	@RequestMapping("/chatBot")
	@RequestMapping(value="/token",method =RequestMethod.POST)
	public String  generateToken(@RequestBody JwtRequest jwtRequest ) throws Exception{
        
		System.out.println("Hello Controller");
		System.out.println(jwtRequest.toString());
		try {
           System.out.println("Hi");
			this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),jwtRequest.getPassword()));	
		 System.out.println("Hi");
		}
		catch(UsernameNotFoundException user) {
			user.printStackTrace();
			throw new Exception("Bad Credential Exception");
		}
		catch(BadCredentialsException e) {
			e.printStackTrace();
			throw new Exception("Bad Credential Exception");
		}

		//fine are go a head
		UserDetails userDetails	=this.customuserdetailsservice.loadUserByUsername(jwtRequest.getUsername());
		System.out.println("Hello token generater-1");
         String token=this.jwtutil.generateToken(userDetails);
         System.out.println("Hello token generater-2");
         System.out.println(token);
         
//         JSONObject jsonObject = new JSONObject();
//         jsonObject.put("id_token", token);
//         System.out.println("jsonObject===>"+jsonObject);
         
         ObjectMapper mapper = new ObjectMapper();
         try {
         	
         	JwtToken jt = new JwtToken();
         	jt.setId_token(token);
         	String test = mapper.writeValueAsString(jt);
         	  System.out.println("test==>"+test);
         	
             return test;
         } catch (IOException e) {
             e.printStackTrace();
             return null;
         }
         
         
//		return ResponseEntity.ok(new JwtResponse(token));
	}
	

//    @PostMapping("/insertUserData")
	@RequestMapping(value="chatBot/insertUserData",method =RequestMethod.POST)
    public UserData createChatBot(@RequestBody UserData userData) {
    	 System.out.println("insert user data here ====>"+userData);
        return customuserdetailsservice.saveOrUpdateChatBot(new UserData(userData.getUsername(), bCryptPasswordEncoder.encode(userData.getPassword())));
    }
	
	
	
static class JwtToken{
	private String id_token;

	public String getId_token() {
		return id_token;
	}

	public void setId_token(String id_token) {
		this.id_token = id_token;
	}
	
}
	
	
}

