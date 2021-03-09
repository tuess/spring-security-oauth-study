package com.kdyzm.spring.security.auth.center.controller;

import com.kdyzm.spring.security.auth.center.mapper.UserMapper;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.endpoint.WhitelabelApprovalEndpoint;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author kdyzm
 */
@Controller
@SessionAttributes("authorizationRequest")
public class GrantController {


    @Resource
    UserMapper userMapper;

    /**
     * @param model
     * @param request
     * @return
     * @throws Exception
     * @see WhitelabelApprovalEndpoint#getAccessConfirmation(java.util.Map, javax.servlet.http.HttpServletRequest)
     */
    @RequestMapping("/custom/confirm_access")
    public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        ModelAndView view = new ModelAndView();
        view.setViewName("grant");
        view.addObject("clientId", authorizationRequest.getClientId());
        view.addObject("scopes", authorizationRequest.getScope());
        return view;
    }


    @GetMapping("/test")
    @Transactional
    public void testTransactionl() throws Exception {
        try {
            userMapper.insertOne(3, "test");
            throw new Exception("测试事务");
        } catch (Exception e) {
            userMapper.insertException(3, 3, 3);
            // System.out.println(e.toString());
            // TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception(e.toString());
        }
    }

}
