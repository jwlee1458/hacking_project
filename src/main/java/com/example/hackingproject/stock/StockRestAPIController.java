package com.example.hackingproject.stock;

import com.example.hackingproject.common.JwtTokenUtil;
import com.example.hackingproject.mypage.dto.MyUserData;
import com.example.hackingproject.mypage.service.MyPageService;
import com.example.hackingproject.notice.dto.NoticeReq;
import com.example.hackingproject.notice.service.NoticeService;
import com.example.hackingproject.stock.service.StockService;
import com.example.hackingproject.stock.vo.StockVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class StockRestAPIController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private StockService stockService;

    @Autowired
    private MyPageService myPageService;

    @RequestMapping(value = {"/main","/"}, method = RequestMethod.GET)
    public ModelAndView main(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();

        // 공지사항 목록 가져오기
        List<NoticeReq> noticeList = noticeService.getNoticeList();
        List<StockVO> stockList = stockService.getStockList();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String stockListJson = objectMapper.writeValueAsString(stockList);
            mav.addObject("stockListJson", stockListJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        mav.addObject("noticeList", noticeList);
        Object user_id_obj = request.getSession().getAttribute("user_id");
        boolean loginFlag = false;
        if(user_id_obj != null){
            loginFlag = true;
            MyUserData myUserData = myPageService.getUserInfo((String)user_id_obj);
            mav.addObject("user_nm", myUserData.getUSER_NM());
        }

        mav.addObject("login", loginFlag);

        mav.setViewName("main_menu");
        return mav;
    }
}
