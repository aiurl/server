package io.theurl.identity.application.implement;

import io.theurl.framework.application.BaseApplicationService;
import io.theurl.identity.application.contract.UserApplicationService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

@Service
@RequestScope
public class UserApplicationServiceImpl extends BaseApplicationService implements UserApplicationService {

    public UserApplicationServiceImpl(ApplicationContext applicationContext) {
        super(applicationContext);
    }
}
