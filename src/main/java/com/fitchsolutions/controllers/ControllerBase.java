package com.fitchsolutions.controllers;

import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.directory.CustomData;
import com.stormpath.sdk.servlet.account.AccountResolver;
import com.stormpath.sdk.servlet.http.Resolver;
import com.stormpath.sdk.servlet.idsite.IdSiteOrganizationContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ControllerBase {
    @Autowired
    Application application;

    @Autowired
    Resolver<IdSiteOrganizationContext> stormpathIdSiteOrganizationResolver;

    protected boolean isAuthenticated(HttpServletRequest req) {
        return AccountResolver.INSTANCE.getAccount(req) != null;
    }

    protected boolean isAdmin(HttpServletRequest req) {
        Map<String, String> orgCustomData = getOrgCustomData(req);

        return
            isAuthenticated(req) &&
            orgCustomData != null &&
            AccountResolver.INSTANCE.getAccount(req).isMemberOfGroup(orgCustomData.get("admin-group-href"));
    }

    protected String getLogoHref(HttpServletRequest req) {
        Map<String, String> orgCustomData = getOrgCustomData(req);

        return orgCustomData.get("logo-image-href");
    }

    private Map<String, String> getOrgCustomData(HttpServletRequest req) {
        String organizationNameKey = stormpathIdSiteOrganizationResolver.get(req, null).getOrganizationNameKey();

        CustomData customData = application.getCustomData();
        return (Map<String, String>)customData.get(organizationNameKey);
    }
}
