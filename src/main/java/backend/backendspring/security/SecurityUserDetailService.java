package backend.backendspring.security;

import backend.backendspring.src.user.UserRepository;
import backend.backendspring.src.user.model.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public SecurityUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findById(Long.valueOf(username))
                .orElseThrow(() -> new UsernameNotFoundException(username + " 사용자 없음"));
        return new SecurityUser(userEntity); // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
    }

}
