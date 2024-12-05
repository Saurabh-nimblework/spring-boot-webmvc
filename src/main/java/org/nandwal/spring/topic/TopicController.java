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
import java.util.concurrent.CompletableFuture;

@Controller
public class TopicController {

    @Autowired
    private TopicService topicService;

    @RequestMapping("/topics")
    public DeferredResult<ResponseEntity<List<Topic>>> getAllTopics(Model model) {
        DeferredResult<ResponseEntity<List<Topic>>> deferredResult = new DeferredResult<>();
        deferredResult.onError((error) -> {
            deferredResult.setErrorResult(ResponseEntity.status(500).body("Error occurred!"));
        });
        deferredResult.onTimeout(() -> {
            deferredResult.setErrorResult(ResponseEntity.status(408).body("Request timed out!"));
        });

        topicService.getAllTopics()
                .thenAccept(topics -> {
                    deferredResult.setResult(new ResponseEntity<>(topics, HttpStatus.OK));
                })
                .exceptionally(ex -> {
                    Map<String, Object> error = new HashMap<>();
                    error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                    error.put("message", ex.getMessage());
                    deferredResult.setErrorResult(new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR));
                    return null;
                });

        return deferredResult;
    }

    @RequestMapping("/topics/{idx}")
    public DeferredResult<ResponseEntity<Topic>> getTopic(@PathVariable("idx") String id, Model model) {
        DeferredResult<ResponseEntity<Topic>> deferredResult = new DeferredResult<>();
        deferredResult.onError((error) -> {
            deferredResult.setErrorResult(ResponseEntity.status(500).body("Error occurred!"));
        });
        deferredResult.onTimeout(() -> {
            deferredResult.setErrorResult(ResponseEntity.status(408).body("Request timed out!"));
        });

        topicService.getTopic(id)
                .thenAccept(topics -> {
                    deferredResult.setResult(new ResponseEntity<>(topics, HttpStatus.OK));
                })
                .exceptionally(ex -> {
                    Map<String, Object> error = new HashMap<>();
                    error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                    error.put("message", ex.getMessage());
                    deferredResult.setErrorResult(new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR));
                    return null;
                });

        return deferredResult;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/topics")
    public DeferredResult<ResponseEntity<Void>> addTopic(@RequestBody Topic topic) {
        DeferredResult<ResponseEntity<Void>> deferredResult = new DeferredResult<>();
        deferredResult.onError((error) -> {
            deferredResult.setErrorResult(ResponseEntity.status(500).body("Error occurred!"));
        });
        deferredResult.onTimeout(() -> {
            deferredResult.setErrorResult(ResponseEntity.status(408).body("Request timed out!"));
        });

        topicService.addTopic(topic)
                .thenRun(() -> {
                    deferredResult.setResult(new ResponseEntity<>(HttpStatus.CREATED));
                })
                .exceptionally(ex -> {
                    Map<String, Object> error = new HashMap<>();
                    error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                    error.put("message", ex.getMessage());
                    deferredResult.setErrorResult(new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR));
                    return null;
                });

        return deferredResult;
    }

    @RequestMapping(method= RequestMethod.PUT, value="/topics/{id}")
    public DeferredResult<ResponseEntity<Void>> updateTopic(@RequestBody Topic topic, @PathVariable String id, Model model) {
        DeferredResult<ResponseEntity<Void>> deferredResult = new DeferredResult<>();
        deferredResult.onError((error) -> {
            deferredResult.setErrorResult(ResponseEntity.status(500).body("Error occurred!"));
        });
        deferredResult.onTimeout(() -> {
            deferredResult.setErrorResult(ResponseEntity.status(408).body("Request timed out!"));
        });
        topicService.updateTopic(topic, id)
                .thenRun(() -> {
                    deferredResult.setResult(new ResponseEntity<>(HttpStatus.OK));
                })
                .exceptionally(ex -> {
                    Map<String, Object> error = new HashMap<>();
                    error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                    error.put("message", ex.getMessage());
                    deferredResult.setErrorResult(new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR));
                    return null;
                });
        return deferredResult;
    }

    @RequestMapping(method= RequestMethod.DELETE, value="/topics/{id}")
    public DeferredResult<ResponseEntity<Void>> deleteTopic(@PathVariable String id, Model model) {
        DeferredResult<ResponseEntity<Void>> deferredResult = new DeferredResult<>();
        deferredResult.onError((error) -> {
            deferredResult.setErrorResult(ResponseEntity.status(500).body("Error occurred!"));
        });
        deferredResult.onTimeout(() -> {
            deferredResult.setErrorResult(ResponseEntity.status(408).body("Request timed out!"));
        });
        topicService.deleteTopic(id)
                .thenRun(() -> {
                    deferredResult.setResult(new ResponseEntity<>(HttpStatus.NO_CONTENT));
                })
                .exceptionally(ex -> {
                    Map<String, Object> error = new HashMap<>();
                    error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                    error.put("message", ex.getMessage());
                    deferredResult.setErrorResult(new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR));
                    return null;
                })
                .exceptionally(ex -> {
                    Map<String, Object> error = new HashMap<>();
                    error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                    error.put("message", ex.getMessage());
                    deferredResult.setErrorResult(new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR));
                    return null;
                });
        return deferredResult;
    }
}
