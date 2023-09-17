package fc.side.fastboard.user.service;

import fc.side.fastboard.common.exception.InvalidParamException;
import fc.side.fastboard.user.dto.MemberDto;
import fc.side.fastboard.user.entity.User;
import fc.side.fastboard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidParamException("존재하지 않는 E-MAIL 입니다."));
    }


    public MemberDto.Response saveNewMember(MemberDto.Request newMemberDto){
        User newUser = User.builder()
                .email(newMemberDto.getUserEmail())
                .username(newMemberDto.getUserName())
                .password(newMemberDto.getUserPassword())
                .build();
        userRepository.save(newUser);
        return MemberDto.Response.fromEntity(newUser);
    }
}
