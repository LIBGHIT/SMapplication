package com.smartmaint.web.Auth;

import com.smartmaint.web.Confirmation.ConfirmationToken;
import com.smartmaint.web.Confirmation.Email.EmailSender;
import com.smartmaint.web.Models.Role;
import com.smartmaint.web.Models.User;
import com.smartmaint.web.Repositorises.UserRepo;
import com.smartmaint.web.Services.ConfirmationTokenService;
import com.smartmaint.web.Services.FileService;
import com.smartmaint.web.Services.UserService;
import com.smartmaint.web.jwt.JwtHelper;
import com.smartmaint.web.utilityClasses.Skills;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;
    private final AuthenticationManager authenticationManager;
    private final FileService fileService;
    private final ConfirmationTokenService tokenService;
    private final UserService userService;
    private final EmailSender emailSender;


    public AuthenticationResponse register(User userForm, MultipartFile cvFile) throws IOException {

        Role role;

        if (userForm.getRoleStr().equals("ROLE_PERSONEL")){
            role = Role.ROLE_PERSONEL;
        } else if (userForm.getRoleStr().equals("ROLE_USER")) {
            role = Role.ROLE_ADMIN;
        }else {
            role = Role.ROLE_USER;
        }

        var user = User.builder()
                .firstName(userForm.getFirstName())
                .lastName(userForm.getLastName())
                .email(userForm.getEmail())
                .password(passwordEncoder.encode(userForm.getPassword()))
                .role(role)
                .sector(userForm.getSector())
                .enabled(false)
                .locked(false)
                .skills(Skills.builder()
                        .skill_1(userForm.getSkills().getSkill_1())
                        .skill_1_experience(userForm.getSkills().getSkill_1_experience())
                        .skill_2(userForm.getSkills().getSkill_2())
                        .skill_2_experience(userForm.getSkills().getSkill_2_experience())
                        .skill_3(userForm.getSkills().getSkill_3())
                        .skill_3_experience(userForm.getSkills().getSkill_3_experience())
                        .skill_4(userForm.getSkills().getSkill_4())
                        .skill_4_experience(userForm.getSkills().getSkill_4_experience())
                        .build())
                .build();
        if (cvFile != null && !cvFile.isEmpty()) {
            if (cvFile.getSize() > 5242880) {
                throw new IllegalStateException("CV file is more than 5MB");
            }
            String contentType = cvFile.getContentType();
            if (!contentType.equals("application/pdf") && !contentType.startsWith("image/")) {
                throw new IllegalStateException("Invalid file type. Only PDF or image files are allowed.");
            }
            String id_cv = fileService.addFile(cvFile);
            user.setId_CV(id_cv);
        }

        var jwtToken = jwtHelper.generateToken(user);

        userRepo.save(user);

        String confToken = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                confToken,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(30),
                user.getEmail()
        );

        tokenService.saveConfirmationToken(confirmationToken);

        String link = "http://localhost:8080/public/registrationConfirm?confirmToken="+confToken;

        emailSender.send(user.getEmail(),buildEmail(user.getFirstName().concat(" ").concat(user.getLastName()),link));

        return AuthenticationResponse.builder().jwtToken(jwtToken).confirmationToken(confToken).build();
    }


    public AuthenticationResponse register(User userForm) {

        Role role;

        if (userForm.getRoleStr().equals("ROLE_PERSONEL")){
            role = Role.ROLE_PERSONEL;
        } else if (userForm.getRoleStr().equals("ROLE_ADMIN")) {
            role = Role.ROLE_ADMIN;
        }else {
            role = Role.ROLE_USER;
        }

        var user = User.builder()
                .firstName(userForm.getFirstName())
                .lastName(userForm.getLastName())
                .email(userForm.getEmail())
                .password(passwordEncoder.encode(userForm.getPassword()))
                .enabled(true)
                .locked(false)
                .role(role)
                .build();

        userRepo.save(user);
        var jwtToken = jwtHelper.generateToken(user);
        return AuthenticationResponse.builder().jwtToken(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        var user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalStateException("Invalid email or password!"));

        if(!user.getEnabled()){
            throw  new IllegalStateException("Email is not validated!");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        }catch (AuthenticationException e){
            throw new IllegalStateException("Invalid email or password!");
        }



        var jwtToken = jwtHelper.generateToken(user);

        return AuthenticationResponse.builder().jwtToken(jwtToken).build();
    }


    public void confirmToken(String token) {
        ConfirmationToken confirmationToken = tokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        tokenService.setConfirmedAt(token);
        userService.enableUser(
                confirmationToken.getEmail());
    }


    public void regenerateToken(String email){

        var user = userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Email does not exist!"));

        tokenService.deleteTokenByEmail(email);

        String confToken = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                confToken,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(30),
                email
        );

        tokenService.saveConfirmationToken(confirmationToken);

        String link = "http://localhost:8080/public/registrationConfirm?confirmToken="+confToken;

        emailSender.send(user.getEmail(),buildEmail(user.getFirstName().concat(" ").concat(user.getLastName()),link));
    }


    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 30 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}
