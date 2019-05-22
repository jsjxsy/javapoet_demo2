package com.xsy.test;

import static com.mattel.Hoverboard.createNimbus;
import static java.util.Collections.*;

import com.mattel.Hoverboard;
import java.lang.String;
import java.lang.System;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HelloWorld {
  private final String greeting;

  String name = "aaa";

  public HelloWorld(String greeting) {
    this.greeting = greeting;
  }

  public void sayHello(String str) {
    System.out.println("Hello World!");
  }

  public int add(int a, int b) {
    return a+b;
  }

  public void test() {
    int total = 0;
    for (int i = 0; i < 10; i++) {
      total += i;
    }
  }

  public void test2() {
    int total = 0;
    for (int i = 0; i < 10; i++) {
       total += i;
    }
  }

  Date getDate() {
    return Date();
  }

  int test2() {
    int result = 0;
    for (int i = 10; i < 20; i++) {
      result = result * i;
    }
    return result;
  }

  Hoverboard tomorrow() {
    return new Hoverboard();
  }

  List<Hoverboard> beyond() {
    List<Hoverboard> result = new ArrayList<>();
    result.add(new Hoverboard());
    result.add(new Hoverboard());
    result.add(new Hoverboard());
    return result;
  }

  char hexDigit(int i) {
    return (char) (i < 10 ? i + '0' : i - 10 + 'a');
  }

  String byteToHex(int b) {
    char[] result = new char[2];
    result[0] = hexDigit((b >>> 4) & 0xf);
    result[1] = hexDigit(b & 0xf);
    return new String(result);
  }
}
