package com.bankapp;

import com.bankapp.config.ConfigurationProperties;
import com.bankapp.service.OperationsConsoleListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ConfigurationProperties.class);
        OperationsConsoleListener operationsConsoleListener = ctx.getBean(OperationsConsoleListener.class);
        operationsConsoleListener.listen();
    }
}