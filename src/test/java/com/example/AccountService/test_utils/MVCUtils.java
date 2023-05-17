package com.example.AccountService.test_utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class MVCUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static String convertToJSONString(Object content) throws JsonProcessingException {
        return objectMapper.writeValueAsString(content);
    }
    private final MockMvc mockMvc;

    public MVCUtils(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    public ResultActions performGet(String url) throws Exception {
        return mockMvc.perform(get(url));
    }

    public ResultActions performGet(String url, Object... uriVariables) throws Exception {
        return mockMvc.perform(get(url, uriVariables));
    }

    public ResultActions performPost(String url, Object content) throws Exception {
        return mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertToJSONString(content)));
    }

    public ResultActions performPost(String url, Object content, Object... uriVariables) throws Exception {
        return mockMvc.perform(post(url, uriVariables)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertToJSONString(content)));
    }

    public ResultActions performPut(String url, Object content) throws Exception {
        return mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertToJSONString(content)));
    }

    public ResultActions performPut(String url, Object content, Object... uriVariables) throws Exception {
        return mockMvc.perform(put(url, uriVariables)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertToJSONString(content)));
    }
}
