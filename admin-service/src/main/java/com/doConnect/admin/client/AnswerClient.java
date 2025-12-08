package com.doConnect.admin.client;

import com.doConnect.admin.dto.Answer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "DOCONNECT-USER-SERVICE", contextId = "answerClient")
public interface AnswerClient {

    @GetMapping("/api/questions/{questionId}/answers")
    List<Answer> getAnswers(@PathVariable("questionId") Long questionId);
}
