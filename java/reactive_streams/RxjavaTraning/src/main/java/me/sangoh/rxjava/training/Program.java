package me.sangoh.rxjava.training;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

import java.util.concurrent.Flow;

public class Program {

    public static void main(String[] args) {
        
        //데이터를 통지하는 생성자를 생성
        Flowable<String> flowable = Flowable.just("Hello", "Word!");
        
        //데이터를 통지받는 소비자를 생성
        Consumer<String> subscriber = str -> {
            System.out.println("통지: " + str);
        };
        
        //구독하기
        flowable.subscribe(subscriber);
    }

}
