package com.nowcoder.controller;

import com.nowcoder.aspect.LogAspect;
import com.nowcoder.model.User;
import com.nowcoder.service.WendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author kwin
 * @create 2019-12-22 19:43
 */
@Controller
public class IndexController {

    @Autowired
    WendaService wendaService;

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 首页显示
     * @return
     */
    @RequestMapping(path={"/","/index"})
    @ResponseBody
    public String index(HttpSession httpSession){
        logger.info("VISIT HOME");
        return wendaService.getMessage(666) + " Hello Nowcoder" + httpSession.getAttribute("msg");
    }

    /**
     * 获取
     * @param userId 路径里面的参数
     * @param groupId 路径里面的参数
     * @param type 请求里面的参数
     * @param key 请求里面的参数
     * @return
     */
    @RequestMapping(path={"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String index(@PathVariable("userId") int userId,
                        @PathVariable("groupId") String groupId,
                        @RequestParam(value = "type",defaultValue = "1") int type,
                        @RequestParam(value = "key",defaultValue = "zz",required = false) String key){
        return String.format("Profile Page of %s / %d, t:%d k:%s",groupId,userId,type,key);
    }

    @RequestMapping(path={"/vm"},method = {RequestMethod.GET})
    public String template(Model model){
        model.addAttribute("value1","vvvvv1");
        List<String> colors = Arrays.asList(new String[]{"RED","GREEN","BLUE"});

        Map<String,String> map = new HashMap<>();
        for (int i = 0;i < 4;i++){
            map.put(String.valueOf(i),String.valueOf(i*i));
        }
        model.addAttribute("map",map);

        model.addAttribute("user",new User("LEE"));

        model.addAttribute("colors",colors);
        return "home";
    }

    @RequestMapping(path={"/request"},method = {RequestMethod.GET})
    @ResponseBody
    public String request(Model model, HttpServletResponse response,
                           HttpServletRequest request,
                           HttpSession httpSession,
                          @CookieValue("JSESSIONID") String sessionId){

        StringBuffer sb = new StringBuffer();
        sb.append("COOKIEVALUE:" + sessionId);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            sb.append(name + ":" + request.getHeader(name) + "<br>");
        }
        if (request.getCookies() != null){
            for (Cookie cookie:request.getCookies()){
                sb.append("Cookie:" + cookie.getName() + " value:" + cookie.getValue());
            }
        }
        sb.append(request.getMethod() + "<br>");
        sb.append(request.getQueryString() + "<br>");
        sb.append(request.getPathInfo() + "<br>");
        sb.append(request.getRequestURI() + "<br>");

        response.addHeader("nowcoderId","hello");
        response.addCookie(new Cookie("username","nowcoder"));
        return sb.toString();
    }

    @RequestMapping(path={"/redirect/{code}"},method = {RequestMethod.GET})
    public String redirect(@PathVariable("code") int code,
                           HttpSession httpSession){
        httpSession.setAttribute("msg"," jump from redirect");
        return "redirect:/";
    }

    /**
     * 301的跳转
     * @param code
     * @param httpSession
     * @return
     */
    @RequestMapping(path={"/redirect2/{code}"},method = {RequestMethod.GET})
    public RedirectView redirect2(@PathVariable("code") int code,
                                  HttpSession httpSession){
        httpSession.setAttribute("msg"," jump from redirect2");
        RedirectView red = new RedirectView("/",true);
        if (code == 301){
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return red;
    }

    @RequestMapping(path = {"/admin"},method = {RequestMethod.GET})
    @ResponseBody
    public String admin(@RequestParam("key") String key){
        if ("admin".equals(key)){
            return "hello admin";
        }

        throw new IllegalArgumentException("参数不对");
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e){
        return "error:" + e.getMessage();
    }
}
