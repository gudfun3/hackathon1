package com.db.dsg.util;

import com.db.dsg.model.MemberUser;
import com.db.dsg.model.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static MemberUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (MemberUser) auth.getPrincipal();
    }

    public static boolean hasRole(Role role) {
        return getCurrentUser().getRoles().contains(role);
    }

    public static Long getCurrentGroupId() {
        return getCurrentUser().getGroup().getId();
    }
}
