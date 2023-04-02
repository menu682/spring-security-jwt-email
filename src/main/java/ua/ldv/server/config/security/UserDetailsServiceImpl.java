package ua.ldv.server.config.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.ldv.server.exeptions.EExceptionMessage;
import ua.ldv.server.persistance.entity.security.UserEntity;
import ua.ldv.server.persistance.repository.security.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(EExceptionMessage.USER_NOT_FOUND.getMessage() + email));

        return UserDetailsImpl.build(user);
    }


}
