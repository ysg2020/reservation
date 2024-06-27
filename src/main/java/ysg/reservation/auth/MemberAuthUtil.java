package ysg.reservation.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import ysg.reservation.exception.impl.ReservationException;
import ysg.reservation.type.ErrorCode;

@Slf4j
public class MemberAuthUtil {


    // 현재 로그인한 사용자 ID 가져오기
    public static String getLoginUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        return principal.getUsername();

    }

    // 현재 로그인한 사용자 체크
    public static void loginUserCheck(String userId){
        String loginUserId = getLoginUserId();
        if (loginUserId != null && !userId.equals(loginUserId)){
            log.info("현재 로그인한 사용자가 다른 사용자 정보 접근");
            throw new ReservationException(ErrorCode.INVALID_USER_ACCESS);
        }
    }


}
