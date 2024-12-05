package org.nandwal.spring.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Controller
public class TopicController {

    @Autowired
    private TopicService topicService;

    private <T> DeferredResult<ResponseEntity<T>> createDeferredResult(Consumer<DeferredResult<ResponseEntity<T>>> task) {
        DeferredResult<ResponseEntity<T>> deferredResult = new DeferredResult<>();
        deferredResult.onError((error) -> {
            deferredResult.setErrorResult(ResponseEntity.status(500).body("Error occurred!"));
        });
        deferredResult.onTimeout(() -> {
            deferredResult.setErrorResult(ResponseEntity.status(408).body("Request timed out!"));
        });
        task.accept(deferredResult);
        return deferredResult;
    }

    @RequestMapping("/topics")
    public DeferredResult<ResponseEntity<List<Topic>>> getAllTopics(Model model) {
        return createDeferredResult(deferredResult ->
                topicService.getAllTopics()
                        .thenAccept(topics -> deferredResult.setResult(new ResponseEntity<>(topics, HttpStatus.OK)))
                        .exceptionally(ex -> {
                            Map<String, Object> error = new HashMap<>();
                            error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                            error.put("message", ex.getMessage());
                            deferredResult.setErrorResult(new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR));
                            return null;
                        })
        );
    }

    @RequestMapping("/topics/{idx}")
    public DeferredResult<ResponseEntity<Topic>> getTopic(@PathVariable("idx") String id, Model model) {
        return createDeferredResult(deferredResult ->
                topicService.getTopic(id)
                        .thenAccept(topic -> deferredResult.setResult(new ResponseEntity<>(topic, HttpStatus.OK)))
                        .exceptionally(ex -> {
                            Map<String, Object> error = new HashMap<>();
                            error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                            error.put("message", ex.getMessage());
                            deferredResult.setErrorResult(new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR));
                            return null;
                        })
        );
    }

    @RequestMapping(method = RequestMethod.POST, value = "/topics")
    public DeferredResult<ResponseEntity<Void>> addTopic(@RequestBody Topic topic) {
        return createDeferredResult(deferredResult ->
                topicService.addTopic(topic)
                        .thenRun(() -> deferredResult.setResult(new ResponseEntity<>(HttpStatus.CREATED)))
                        .exceptionally(ex -> {
                            Map<String, Object> error = new HashMap<>();
                            error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                            error.put("message", ex.getMessage());
                            deferredResult.setErrorResult(new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR));
                            return null;
                        })
        );
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/topics/{id}")
    public DeferredResult<ResponseEntity<Void>> updateTopic(@RequestBody Topic topic, @PathVariable String id, Model model) {
        return createDeferredResult(deferredResult ->
                topicService.updateTopic(topic, id)
                        .thenRun(() -> deferredResult.setResult(new ResponseEntity<>(HttpStatus.OK)))
                        .exceptionally(ex -> {
                            Map<String, Object> error = new HashMap<>();
                            error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                            error.put("message", ex.getMessage());
                            deferredResult.setErrorResult(new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR));
                            return null;
                        })
        );
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/topics/{id}")
    public DeferredResult<ResponseEntity<Void>> deleteTopic(@PathVariable String id, Model model) {
        return createDeferredResult(deferredResult ->
                topicService.deleteTopic(id)
                        .thenRun(() -> deferredResult.setResult(new ResponseEntity<>(HttpStatus.NO_CONTENT)))
                        .exceptionally(ex -> {
                            Map<String, Object> error = new HashMap<>();
                            error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                            error.put("message", ex.getMessage());
                            deferredResult.setErrorResult(new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR));
                            return null;
                        })
        );
    }
}