package com.xsushirollx.sushibyte.user.controllers;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xsushirollx.sushibyte.user.service.UserService;

/**
 * @author dyltr
 * Controller for user registration and verification
 */
@RestController
public class AuthorizationController {
	@Autowired
	UserService userService;
	
	static Logger log = LogManager.getLogger(RegistrationController.class.getName());

	/**
	 * @param code
	 * @return
	 * @throws IOException 
	 */
	@GetMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response, @Param("id") String id,
			@Param("password") String password) throws IOException {
		Integer key = userService.logIn(id, password);
		
		if (key!=null) {
			HttpSession session = request.getSession();
			log.log(Level.WARN, "Session created");
			session.setAttribute("key", key);
			response.sendRedirect("/portal.html");	//Temporary redirect location for now
			return "login_success";
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return "login_fail";
		}
	}
	
	@GetMapping("/logout")
	public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		Integer key = (Integer)session.getAttribute("key");
		log.log(Level.DEBUG, key);
		session.invalidate();
		userService.logOut(key);
		response.sendRedirect("/login.html");
	}

}
