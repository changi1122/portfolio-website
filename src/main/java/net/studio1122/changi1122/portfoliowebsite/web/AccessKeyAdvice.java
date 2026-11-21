package net.studio1122.changi1122.portfoliowebsite.web;

import net.studio1122.changi1122.portfoliowebsite.global.AppConstants;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.regex.Pattern;

/**
 * 요청의 ack 쿼리 파라미터를 모든 뷰에 "ack" 모델 속성으로 노출합니다.
 * 템플릿은 이 값으로 내부 링크에 ack를 이어 붙입니다.
 */
@ControllerAdvice
public class AccessKeyAdvice {

    private static final Pattern VALID_ACK = Pattern.compile("[A-Za-z0-9_-]{1,64}");

    @ModelAttribute("ack")
    public String ack(@RequestParam(value = "ack", required = false) String ack) {
        if (ack == null || AppConstants.DEFAULT_ACCESS_KEY.equals(ack) || !VALID_ACK.matcher(ack).matches()) {
            return null;
        }
        return ack;
    }
}
