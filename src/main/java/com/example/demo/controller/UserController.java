package com.example.demo.controller;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.util.SendMail;
import com.example.demo.util.md5.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;
@Slf4j
@Controller
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;
    //登录中心
    @GetMapping()
    public ModelAndView loginPage(HttpServletRequest request){
        //TODO cookie
        Cookie[] cookies = request.getCookies();
        String uname = "";
        if(cookies != null){
            for (Cookie cookie : cookies) {
                switch(cookie.getName()){
                    case "uname":
                        if(cookie.getValue() != null){
                            ModelAndView mv = new ModelAndView();
                            String value[] = cookie.getValue().split("&");
                            uname = value[1];
                            System.out.println("login: "+uname);
                            mv.addObject("uname",uname);
                            mv.setViewName("userHome");
                            return mv;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return new ModelAndView("userLogin");//返回登录注册界面
    }
    //修改个人信息管理中心
    @RequestMapping("/update")
    public ModelAndView update(HttpServletRequest request){
        ModelAndView mv = new ModelAndView();
        Cookie[] cookies = request.getCookies();
        String uname = "";
        if(cookies != null){
            for (Cookie cookie : cookies) {
                switch(cookie.getName()){
                    case "uname":
                        if(cookie.getValue() != null){
                            String value[] = cookie.getValue().split("&");
                            uname = value[1];
                            mv.addObject("uname",uname);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        mv.addObject("state",0);
        mv.setViewName("userUpdate");
        return mv;//返回修改信息界面
    }
    //退出当前账号
    @RequestMapping("/retreat")
    public String retreat(HttpServletRequest request,HttpServletResponse response) throws ServletException {
        Cookie[] cookies = request.getCookies();
        String uname = "";
        if(cookies != null){
            for (Cookie cookie : cookies) {
                switch(cookie.getName()){
                    case "uname":
                        String value[] = cookie.getValue().split("&");
                        uname = value[1];
                        System.out.println("Before retreat: "+uname);
                        cookie.setValue(null);
                        cookie.setMaxAge(0);
                        cookie.setPath("/");
                        uname = cookie.getValue();
                        System.out.println("After retreat: "+uname);
                        response.addCookie(cookie);
                        break;
                    default:
                        break;
                }
            }
        }
        return "index";//返回初始界面
    }
    //查看个人信息
    @RequestMapping("/checkPersonInformation")
    public ModelAndView checkPersonInformation(HttpServletRequest request){
        ModelAndView mv = new ModelAndView();
        Cookie[] cookies = request.getCookies();
        String uname = "";
        if(cookies != null){
            for (Cookie cookie : cookies) {
                switch(cookie.getName()){
                    case "uname":
                        String value[] = cookie.getValue().split("&");
                        uname = value[1];
                        User user = userMapper.selectUname(uname);
                        mv.addObject("uname",user.getUname());
                        mv.addObject("mail",user.getMail());
                        mv.addObject("phone",user.getPhone());
                        break;
                    default:
                        break;
                }
            }
        }
        mv.setViewName("personInformation");
        return mv;
    }
    //实现登录功能
    @RequestMapping(value="/login",method=RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uname = request.getParameter("username");
        String password = request.getParameter("password");
        log.debug("uname:{}",uname);
        log.debug( "password:{}",password );
        User user = userMapper.selectUser(uname, Md5Utils.code(password));
        ModelAndView mv = new ModelAndView();
        if(user != null){
            if(user.getActived() == 0)
            {
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<script>");
                out.println("alert('抱歉！登录失败！请先绑定邮箱进行激活账号');");
                out.println("</script>");
                out.flush();
                mv.addObject("state",1);
                mv.addObject("uname",uname);
                mv.setViewName("userUpdate");
            }
            else {
                Cookie cookie = new Cookie("uname","user&"+uname);
                cookie.setMaxAge(60*60*24);
                cookie.setPath("/");
                response.addCookie(cookie);
                Date date = new Date();
                Timestamp t= new Timestamp(date.getTime());
                userMapper.updateTimeOfLastLogin(uname,Md5Utils.code(password),t);
                mv.addObject("uname",uname);
                mv.setViewName("userHome");
            }
        }
        else{
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('抱歉！登录失败,请确保用户名和密码输入正确且已注册本系统');");
            out.println("history.back();");
            out.println("</script>");
            out.flush();
            mv.setViewName("index");
        }
        return mv;
    }
    //实现注册功能
    @RequestMapping(value = "/register",method=RequestMethod.POST)
    public String Register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uname = request.getParameter("username");
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");
        log.debug("uname:{}",uname);
        log.debug( "password1:{}",password1);
        log.debug( "password2:{}",password2);
        User user = userMapper.selectUname(uname);
        if(user == null && password1.equals(password2)){
            userMapper.saveUser(uname,Md5Utils.code(password1));
            userMapper.updateActived(uname,Md5Utils.code(password1),0);
            userMapper.updateSecurity(uname,Md5Utils.code(password1),"000000");
            return "userLogin";
        }
        else if(user != null)
        {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('抱歉！注册失败,该用户名已被占用！');");
            out.println("history.back();");
            out.println("</script>");
            out.flush();
            return "index";
        }
        else{
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('抱歉！注册失败,请确保两次输入的密码相同！');");
            out.println("history.back();");
            out.println("</script>");
            out.flush();
            return "index";
        }
    }

    //实现修改密码功能
    @RequestMapping(value="/updatePassword",method=RequestMethod.POST)
    public String updatePassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uname = request.getParameter("username");
        String oldPassword = request.getParameter("oldPassword");
        String newPassword1 = request.getParameter("newPassword1");
        String newPassword2 = request.getParameter("newPassword2");
        log.debug("uname:{}",uname);
        log.debug("oldPassword:{}",oldPassword);
        log.debug("newPassword1:{}",newPassword1);
        log.debug("newPassword2:{}",newPassword2);
        User user = userMapper.selectUser(uname,Md5Utils.code(oldPassword));
        if(user != null && newPassword1.equals(newPassword2)){
            userMapper.updatePassword(uname,Md5Utils.code(newPassword1));
            return "userHome";
        }
        else if(user == null)
        {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('抱歉！修改失败,输入的用户名和密码不正确！');");
            out.println("history.back();");
            out.println("</script>");
            out.flush();
            return "index";
        }
        else{
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('抱歉！修改失败,请确保两次输入的密码相同！');");
            out.println("history.back();");
            out.println("</script>");
            out.flush();
            return "index";
        }
    }

    //实现修改用户名功能
    @RequestMapping(value = "/updateUsername",method=RequestMethod.POST)
    public String updateUsername(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uname = request.getParameter("username");
        String password = request.getParameter("password");
        String newUname = request.getParameter("newUsername");
        log.debug("uname:{}",uname);
        log.debug("password:{}",password);
        log.debug("newUserName:{}",newUname);
        User user = userMapper.selectUser(uname,Md5Utils.code(password));
        User user2 = userMapper.selectUname(newUname);//用于判断新用户名是否可用
        if(user != null && user2 == null){
            userMapper.updateUser(uname,Md5Utils.code(password),newUname);
            return "userHome";
        }
        else if(user == null)
        {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('抱歉！修改失败,输入的用户名和密码不正确！');");
            out.println("history.back();");
            out.println("</script>");
            out.flush();
            return "index";
        }
        else{
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('抱歉！修改失败,该用户名已被占用！');");
            out.println("history.back();");
            out.println("</script>");
            out.flush();
            return "index";
        }
    }

    //实现发送激活验证码功能
    @RequestMapping(value="/actived",method = RequestMethod.POST)
    public void sendSecurityCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String mail = request.getParameter("mail");

        String code = String.format("%06d", (new Random().nextInt(1000000)+1)%1000000);//随机生成6位非全0验证码
        User user = userMapper.selectUser(name,Md5Utils.code(password));
        String mailP = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        boolean flag = Pattern.matches(mailP,mail);
        if(user != null && flag){
            userMapper.updateSecurity(name,Md5Utils.code(password),code);
            System.out.println(name + " - " + password + " - " + mail + " - " + code);
            SendMail.sendMail(mail,"您的账号激活验证码是"+code);
        }
        else if(user == null){
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('抱歉！获取验证码失败！请正确用户名和密码！');");
            out.println("history.back();");
            out.println("</script>");
            out.flush();
        }
        else{
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('抱歉！获取验证码失败！请正确输入邮箱号码！');");
            out.println("history.back();");
            out.println("</script>");
            out.flush();
        }
    }
    //实现绑定邮箱功能
    @RequestMapping(value = "/updateMail",method=RequestMethod.POST)
    public String updateMail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uname = request.getParameter("username");
        String password = request.getParameter("password");
        String mail = request.getParameter("mail");
        String securityCode = request.getParameter("securityCode");
        log.debug("uname:{}",uname);
        log.debug("password:{}",password);
        log.debug("mail:{}",mail);
        log.debug("securityCode:{}",securityCode);
        User user = userMapper.selectUser(uname,Md5Utils.code(password));
        System.out.println(user.getUname() + " --- " + user.getSecurityCode());
        if(user != null && securityCode.equals(user.getSecurityCode())){
            userMapper.updateMail(uname,Md5Utils.code(password),mail);//更新邮箱
            userMapper.updateActived(uname,Md5Utils.code(password),1);//更新激活状态
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('恭喜你！绑定成功！');");
            out.println("</script>");
            return "userHome";
        }
        else if(user == null){
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('抱歉！绑定失败！请输入正确的用户名和密码！');");
            out.println("history.back();");
            out.println("</script>");
            out.flush();
            return "index";
        }
        else{
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('抱歉！绑定失败！验证码错误');");
            out.println("history.back();");
            out.println("</script>");
            out.flush();
            return "index";
        }
    }
    //实现绑定手机号功能
    @RequestMapping(value = "/updatePhone",method=RequestMethod.POST)
    public String updatePhone(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uname = request.getParameter("username");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String phoneP = "^([1][3,4,5,6,7,8,9])\\d{9}$";
        boolean flag = Pattern.matches(phoneP,phone);//正则表达式匹配手机号码
        //flag = true;//测试前端正则判断
        log.info("uname:{}",uname);
        log.info("password:{}",password);
        log.info("phone:{}",phone);
        User user = userMapper.selectUser(uname,Md5Utils.code(password));
        if(user != null && flag){
            userMapper.updatePhone(uname,Md5Utils.code(password),phone);
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('恭喜你！绑定成功！');");
            out.println("history.back();");
            out.println("</script>");
            return "userHome";
        }
        else if(user == null)
        {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('抱歉！绑定失败,请输入正确的用户名和密码');");
            out.println("history.back();");
            out.println("</script>");
            out.flush();
            return "index";
        }
        else{
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('抱歉！绑定失败,请输入正确手机号码');");
            out.println("history.back();");
            out.println("</script>");
            out.flush();
            return "index";
        }
    }
}
