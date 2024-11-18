package com.exampletest.testtt.Controllers;

import com.exampletest.testtt.Configurations.DataBaseConfigure;
import com.exampletest.testtt.Configurations.DataMemory;
import com.exampletest.testtt.Schedulers.SchedulerNew;
import com.exampletest.testtt.Schedulers.SchedulerSearchOrders;
import com.exampletest.testtt.Service.TokenService;
import com.exampletest.testtt.WebJobs.WebSocket;
import com.exampletest.testtt.models.tms.Entity.Token;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/scheduler")
public class SchedulerController {

    private static final Logger log = LoggerFactory.getLogger(SchedulerController.class);
    @Autowired
    private ScheduledAnnotationBeanPostProcessor postProcessor;

    @Autowired
    private SchedulerSearchOrders schedulerSearchOrders;

    @Autowired
    private SchedulerNew schedulerNew;

    @Autowired
    private WebSocket webSocket;

    @Autowired
    TokenService tokenService;

    @Autowired
    DataMemory dataMemory;


    @GetMapping(value = "/stop/{schedulerName}")
    public String stopScheduleName(@PathVariable String schedulerName) {

        try {
            switch (schedulerName) {
                case "searchNew": postProcessor.postProcessBeforeDestruction(schedulerNew,  schedulerName);
                case "searchOrders": postProcessor.postProcessBeforeDestruction(schedulerSearchOrders,  schedulerName);
            }
        } catch (NoSuchBeanDefinitionException ignored) {}

        return "OK";
    }

    @GetMapping(value = "/start/{schedulerName}")
    public Mono<String> startScheduleName(@PathVariable String schedulerName) {
        try {
            if (postProcessor.getScheduledTasks().stream().map(m -> {
                if (m.toString().contains(".")) {
                    int size = m.toString().split("\\.").length;
                    return m.toString().split("\\.")[size - 1];
                }
                return m.toString();
            }).noneMatch(schedulerName::equals)) {
                switch (schedulerName) {
                    case "searchNew": postProcessor.postProcessAfterInitialization(schedulerNew,  schedulerName);
                    case "searchOrders": postProcessor.postProcessAfterInitialization(schedulerSearchOrders,  schedulerName);
                }

            } else {
                return Mono.just("Задача " + schedulerName + " уже запущена");
            }

        } catch (NoSuchBeanDefinitionException ignored) {}
        return Mono.just("OK");
    }

    @GetMapping(value = "/list")
    public String listSchedules() throws JsonProcessingException {
        Set<ScheduledTask> setTasks = postProcessor.getScheduledTasks().stream().filter(f -> !f.toString().contains("checkToken")).collect(Collectors.toSet());
        if (!setTasks.isEmpty()) {
            return setTasks.stream()
                    .map(m -> {
                        if (m.toString().contains(".")) {
                            int size = m.toString().split("\\.").length;
                            return m.toString().split("\\.")[size - 1];
                        }
                        return m.toString();
                    }).toList().toString();
        } else {
            return "Нет запущенных планировщиков";
        }
    }

    @GetMapping(value = "/start/ws")
    public Mono<String> startWS() {
        return tokenService.getToken()
                .map(fm -> {
                            try {
                                Mono<Void> receive = webSocket.receive(new URI("wss://api.transport2.ru/ws/notifications/?token=" + fm.getToken()));
                                new Thread(receive::block, "WebSocket Thread").start();
                            } catch (URISyntaxException e) {
                                log.info(e.getMessage(), e);
                            }
                            return "OK";
                        }

                );
    }

    @GetMapping(value = "/stop/ws")
    public String stopWS() {
        Thread.getAllStackTraces().keySet().forEach( f -> {
            if (f.getName().equals("WebSocket Thread")){
                f.interrupt();
            }
        });
        return "OK";
    }


}
