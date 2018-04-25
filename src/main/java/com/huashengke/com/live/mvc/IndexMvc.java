package com.huashengke.com.live.mvc;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangc on 2018/4/20.
 */
@RestController
public class IndexMvc {

    @RequestMapping("/createLive")
    public ModelAndView createLive(HttpServletRequest request){
        String title = request.getParameter("title");

        Map<String, String> datas = new HashMap<>();
        System.out.println("--------------index:"+title);

        datas.put("title",title);
        return new ModelAndView("/html/index.html",datas);
    }

}
