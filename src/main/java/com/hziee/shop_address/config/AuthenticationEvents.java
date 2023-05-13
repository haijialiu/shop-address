package com.hziee.shop_address.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEvents {
    Logger logger = LoggerFactory.getLogger(AuthenticationEvents.class);
    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        // ...
        logger.info("用户 "+success.getAuthentication().getName()+" 认证成功");
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failures) {
        // ...

        logger.warn("用户："+failures.getAuthentication().getName()+" 认证失败，失败原因："+failures.getException().getMessage());
    }
}
