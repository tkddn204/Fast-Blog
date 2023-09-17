package fc.side.fastboard.user.dto;


import fc.side.fastboard.user.entity.User;
import groovy.util.logging.Slf4j;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


public class MemberDto {

    @Getter
    @Setter
    @Builder
    @Slf4j
    @NoArgsConstructor
    @AllArgsConstructor
    public static  class Request{
        private String userEmail;
        private String userPassword;
        private String userName;


    }

    @Getter
    @Setter
    @Builder
    @Slf4j
    @NoArgsConstructor
    @AllArgsConstructor
    public static  class Response {
        private String userEmail;
        private String userPassword;
        private String userName;

        public static Response fromEntity(User user) {
            return Response.builder()
                    .userEmail(user.getEmail())
                    .userName(user.getUsername())
                    .userPassword(user.getPassword())
                    .build();

        }
    }
}
