package com.palestra.palestra.Services;

import com.palestra.palestra.Repositories.UserRepository;
import com.palestra.palestra.Services.Auth.PasswordChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserUtils {
    private final UserRepository repo;
    private final PasswordChecker pwChecker;

    @Autowired
    public UserUtils(UserRepository repo, PasswordChecker pwChecker) {
        this.repo = repo;
        this.pwChecker = pwChecker;
    }

    public boolean changePassword(String username, String newPassword) {
        if(pwChecker.checkPassword(newPassword)) {
            repo.changePassword(username, newPassword);
            return true;
        }else {
            return false;
        }
    }

    public Authentication changeRole(String username, SimpleGrantedAuthority newRole, Authentication auth) throws Exception {
        String[] roles = new String[] {"ROLE_USER_PROVA", "ROLE_USER_BASIC", "ROLE_USER_PRO"};
        if(Arrays.stream(roles).filter((s) -> s.equals(newRole.toString())).count() != 1) {
            throw new Exception("Unknown role");
        }
        boolean wasTrial = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER_PROVA"));

        Authentication ret = repo.changeUserRole(username, newRole, auth);

        // Assicurati che l'utente prova diventi abilitato dopo un upgrade
        if(wasTrial) {
            repo.enableUser(username);
        }

        return ret;
    }

}
