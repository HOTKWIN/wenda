package com.nowcoder.service;

import org.springframework.stereotype.Service;

/**
 * @author kwin
 * @create 2019-12-23 19:22
 */
@Service
public class WendaService {
    public String getMessage(int userId){
        return "Hello Message:" + String.valueOf(userId);
    }
}
