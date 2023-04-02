package ua.ldv.server.service.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.ldv.server.config.security.UserDetailsImpl;
import ua.ldv.server.config.security.jwt.JwtUtils;

import ua.ldv.server.dto.EResponseMessage;
import ua.ldv.server.dto.MessageResponseDto;
import ua.ldv.server.dto.security.JwtResponseDto;
import ua.ldv.server.dto.security.LoginRequestDto;
import ua.ldv.server.dto.security.SignupRequestDto;
import ua.ldv.server.exeptions.BadDataRequestException;
import ua.ldv.server.exeptions.EExceptionMessage;
import ua.ldv.server.persistance.ERole;
import ua.ldv.server.persistance.EUserStatus;
import ua.ldv.server.persistance.entity.security.RoleEntity;
import ua.ldv.server.persistance.entity.security.UserEntity;
import ua.ldv.server.persistance.repository.security.RoleRepository;
import ua.ldv.server.persistance.repository.security.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder encoder,
                       JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    public JwtResponseDto loginUser(LoginRequestDto loginRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(),
                        loginRequestDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        String refreshJwt = jwtUtils.generateJwtRefreshToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new JwtResponseDto(
                jwt,
                refreshJwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles
        );
    }


    public MessageResponseDto registerUser(SignupRequestDto signupRequestDTO) {

        if (signupRequestDTO.getUsername().isEmpty()
                || signupRequestDTO.getPassword().isEmpty()
                || signupRequestDTO.getEmail().isEmpty()) {
            throw new BadDataRequestException(EExceptionMessage.FIELDS_MUST_NOT_BE_EMPTY.getMessage());
        }

        if (Boolean.TRUE.equals(userRepository.existsByUsername(signupRequestDTO.getUsername()))) {
            throw new BadDataRequestException(EExceptionMessage.NAME_IS_ALREADY_TAKEN.getMessage());
        }

        if (Boolean.TRUE.equals(userRepository.existsByEmail(signupRequestDTO.getEmail()))) {
            throw new BadDataRequestException(EExceptionMessage.EMAIL_IS_ALREADY_TAKEN.getMessage());
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(signupRequestDTO.getUsername());
        userEntity.setEmail(signupRequestDTO.getEmail());
        userEntity.setPhone(signupRequestDTO.getPhone());
        userEntity.setPassword(encoder.encode(signupRequestDTO.getPassword()));
        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new BadDataRequestException(EExceptionMessage.NO_SUCH_ROLE.getMessage()));

        roles.add(userRole);

        userEntity.setStatus(EUserStatus.ACTIVE);
        userEntity.setRoles(roles);
        //TODO здесь может быть ошибка если телефон или почта уже есть в базе - отловить
        userRepository.save(userEntity);
        return new MessageResponseDto(EResponseMessage.REGISTER_SUCCESSFULLY.getMessage());

    }

}
