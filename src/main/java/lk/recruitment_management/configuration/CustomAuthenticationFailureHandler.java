package lk.recruitment_management.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

@Component( "customAuthenticationFailureHandler" )
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
  //to further reading : https://www.baeldung.com/spring-security-block-brute-force-authentication-attempts

  @Override
  public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                      AuthenticationException e) throws IOException, ServletException {
    //set our response to UNAUTHORIZED status
    httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());

    String jsonPayload = "{\"message\" : \"%s\", \"timestamp\" : \"%s\" }";
    httpServletResponse.getOutputStream().println(String.format(jsonPayload, e.getMessage(),
                                                                Calendar.getInstance().getTime()));


    //since we have created our custom success handler, its up to us to where we will redirect the user after
    // AuthenticationFailure
    httpServletResponse.sendRedirect("/login?err=entered username and password is error");

  }

  /*
   @Autowired
    private UserServices userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        String email = request.getParameter("email");
        User user = userService.getByEmail(email);

        if (user != null) {
            if (user.isEnabled() && user.isAccountNonLocked()) {
                if (user.getFailedAttempt() < UserServices.MAX_FAILED_ATTEMPTS - 1) {
                    userService.increaseFailedAttempts(user);
                } else {
                    userService.lock(user);
                    exception = new LockedException("Your account has been locked due to 3 failed attempts."
                            + " It will be unlocked after 24 hours.");
                }
            } else if (!user.isAccountNonLocked()) {
                if (userService.unlockWhenTimeExpired(user)) {
                    exception = new LockedException("Your account has been unlocked. Please try to login again.");
                }
            }

        }

        super.setDefaultFailureUrl("/login?error");
        super.onAuthenticationFailure(request, response, exception);
    }

}

  * */
}
