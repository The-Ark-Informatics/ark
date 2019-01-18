/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * Reference: https://stackoverflow.com/questions/9908556/can-i-use-re-captcha-with-wicket- Sanjayam
 ******************************************************************************/
package au.org.theark.web.pages.reset;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;



public class ReCaptchaV2 {
	private final static Logger logger = Logger.getLogger(ReCaptchaV2.class);

    private final static String VERIFICATION_URL = "https://www.google.com/recaptcha/api/siteverify";
    private final static String SECRET = "6LcOo3oUAAAAABt__VMQdGmly4ILkJf1Yp2OyacQ";

    private static ReCaptchaV2 instance = new ReCaptchaV2();

    private ReCaptchaV2() {}

    public static ReCaptchaV2 getInstance() {
        return instance;
    }

    private boolean verify(String recaptchaUserResponse, String remoteip) {
        boolean ret = false;
        if (recaptchaUserResponse == null) {
            return ret;
        }

        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("secret", SECRET);
        map.add("response", recaptchaUserResponse);
        if (remoteip != null) {
            map.add("remoteip", remoteip);
        }
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> res = null;
        try {
            res = rt.exchange(VERIFICATION_URL, HttpMethod.POST, httpEntity, String.class);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage());
        }

        if (res == null || res.getBody() == null) {
            return ret;
        }

        Response response = null;
        try {
            response = new ObjectMapper().readValue(res.getBody(), Response.class);
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage());
        }

        if (response != null && response.isSuccess()) {
            ret = true;
        }

        logger.info("Verification result: " + ret);
        return ret;
    }

    public boolean verify(HttpServletRequest httpServletRequest) {
        boolean ret = false;
        if (httpServletRequest == null) {
            return ret;
        }

        String recaptchaUserResponse = httpServletRequest.getParameter("g-recaptcha-response");
        String remoteAddr = httpServletRequest.getRemoteAddr();
        return verify(recaptchaUserResponse, remoteAddr);
    }
}
