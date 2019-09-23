package com.example.mytravellerapp.utils;


import io.reactivex.Scheduler;

/**
 * We use a scheduler to make testing our presenters easier
 * @author zsiegel
 */
public interface IScheduler {

    Scheduler mainThread();

    Scheduler backgroundThread();
}
