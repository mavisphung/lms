package com.lmsapp.project.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.lmsapp.project.role.RoleRepository;
import com.lmsapp.project.user.User;
import com.lmsapp.project.user.service.UserService;

public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest request, 
			HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		 
		handle(request, response, authentication);
	    clearAuthenticationAttributes(request);
	}
	
	protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
	
	public void handle(
			HttpServletRequest request, 
			HttpServletResponse response,
			Authentication authentication) throws IOException {
		String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            System.err.println(
                    "Response has already been committed. Unable to redirect to "
                            + targetUrl);
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
	}
	//S???a ch??? comment th??iiii
	//X??? l?? chuy???n trang t???i h??m n??y
	public String determineTargetUrl(Authentication auth) {
		/*
		 * ?????i t?????ng authentication ch???a nh???ng th??? sau:
		 * 	auth.getName(): l???y username
		 *  auth.getAuthorities(): l???y role
		 *  auth.getPrincipal(): l???y nguy??n ?????i t?????ng UserDetails
		 *  ...
		 */
		
		String url = "";
		//------------------------------------------------
		//l???y authorities c???a logged in user
		Collection<? extends GrantedAuthority> authorities
        					= auth.getAuthorities();
		List<String> roles = new ArrayList<String>();
		System.out.println("CustomAuthSuccessHandler >> User " + auth.getName() + " has " + authorities.toString());
		
		//------------------------------------------------------------
		for (GrantedAuthority grantedAuthority : authorities) {
			String authorityName = grantedAuthority.getAuthority();
			//System.out.println("CustomAuthSuccessHandler >> Add " + authorityName + " to role list");
			roles.add(authorityName);
		}
		
		if (roles.contains("ROLE_ADMIN")) {
			url = "/admin/";
		}else if (roles.contains("ROLE_INSTRUCTOR")) {
			url = "/instructor/";
		}
		//throw new IllegalStateException();
		return url;
	}
	
	
	//getter setter
	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }
}
