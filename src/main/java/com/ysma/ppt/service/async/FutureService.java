package com.ysma.ppt.service.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class FutureService {

    @Async
    public CompletableFuture<String> guess(String asker){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            log.error("FutureTask.guess InterruptedException");
        }

        return CompletableFuture.completedFuture(asker + ":good");
    }

    @Async
    public CompletableFuture<String> guessAgain(String asker){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            log.error("FutureTask.guess InterruptedException");
        }

        return CompletableFuture.completedFuture(asker + ":bad");
    }
}
