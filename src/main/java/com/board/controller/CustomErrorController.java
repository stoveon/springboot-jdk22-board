package com.board.controller;

import com.google.gson.Gson;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * <pre>
 *     error controller
 * </pre>
 *
 * @author jejeong
 * @since 2024. 07. 15.
 */
@Slf4j
@Controller
public class CustomErrorController implements ErrorController {

    private static final Gson GSON = new Gson();

    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView handleError(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        int statusCode = -1;
        String message = "";
        String viewName = "";
        viewName = "error/error";
        // error로 들어온 에러의 status를 불러온다 (ex:404,500,403...)
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        log.debug("error status code :: {}", GSON.toJson(status));
        if (status != null) {
            statusCode = Integer.valueOf(status.toString());
            switch (statusCode) {
                case 400:
                    message= "Bad Request";
                    break;
                case 403:
                    message= "Forbidden";
                    break;
                case 404:
                    message= "Page Not Found";
                    break;
                case 405:
                    message= "Method Not Allowed";
                    break;
                case 500:
                    message= "Internal Server Error";
                    break;
            }

        }
        mav.setViewName(viewName);
        mav.addObject("statusCode", statusCode);
        mav.addObject("message", message);
        return mav;
    }
}
