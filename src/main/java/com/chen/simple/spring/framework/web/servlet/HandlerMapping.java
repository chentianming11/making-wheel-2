package com.chen.simple.spring.framework.web.servlet;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 陈添明
 * @date 2019/5/19
 */
public interface HandlerMapping {

    HandlerExecutionChain getHandler(HttpServletRequest request);
}
