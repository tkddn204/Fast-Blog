package fc.side.fastboard.user.service;

import fc.side.fastboard.common.exception.InvalidParamException;
import fc.side.fastboard.user.dto.MemberDto;
import fc.side.fastboard.user.entity.User;
import fc.side.fastboard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidParamException("존재하지 않는 E-MAIL 입니다."));
    }


    public MemberDto.Response saveNewMember(MultipartFile multipartFile, MemberDto.Request newMemberDto) {

        String fileName = saveFile(multipartFile,newMemberDto);
        User newUser = User.builder()
                .email(newMemberDto.getUserEmail())
                .username(newMemberDto.getUserName())
                .password(passwordEncoder.encode(newMemberDto.getUserPassword()))
                .fileName(fileName)
                .build();

        userRepository.save(newUser);
        return MemberDto.Response.fromEntity(newUser);
    }


    public MemberDto.Response updateMemberInfo(MultipartFile multipartFile, MemberDto.Request newMemberInfo)  {

        String fileName = saveFile(multipartFile,newMemberInfo);

        User editUser = User.builder()
                .id(newMemberInfo.getId())
                .email(newMemberInfo.getUserEmail())
                .username(newMemberInfo.getUserName())
                .password(passwordEncoder.encode(newMemberInfo.getUserPassword()))
                .fileName(fileName)
                .build();
        userRepository.save(editUser);
        return MemberDto.Response.fromEntity(editUser);
    }

    public void deleteMember(String email){
        User user = findByEmail(email);
        userRepository.delete(user);
    }


    public String saveFile(MultipartFile multipartFile, MemberDto.Request newMemberDto) {

        Path fullPath;
        String fileName ="";

        if(multipartFile.isEmpty()){
            System.out.println("비어있는 파일입니다.");
            return "default.jpeg";
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            final String filePath ="/Users/jun/Desktop/Back-Git/side project/Fast-Blog/src/main/resources/static/img/";
            fileName =  newMemberDto.getUserEmail() + multipartFile.getOriginalFilename();
            fullPath = Path.of(filePath + fileName);
            Files.copy(inputStream, fullPath,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }

        return fileName;

    }




}
