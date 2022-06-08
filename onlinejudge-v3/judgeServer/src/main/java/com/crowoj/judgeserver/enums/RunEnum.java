package com.crowoj.judgeserver.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author crow
 * @create 2022/2/18 23:50
 * @description
 */

@AllArgsConstructor
@Getter
public enum RunEnum {
    JAVA("Main.java","Main.class",new String[]{"/usr/bin/javac","-encoding","UTF-8","Main.java"},
            new String[] {"/usr/bin/java","-Dfile.encoding=utf-8","Main"}),
    CPP("main.cc","main",new String[]{"/usr/bin/g++","main.cc","-o","main"},new String[]{"main"});
    private String source;
    private String output;
    private String[] compileArgs;
    private String[] runArgs;
}
