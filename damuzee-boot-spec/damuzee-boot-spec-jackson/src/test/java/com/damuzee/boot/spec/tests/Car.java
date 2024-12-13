package com.damuzee.boot.spec.tests;

import com.damuzee.boot.spec.jackson.utils.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 */
@Data
public class Car implements Serializable {

    private Car car;
    private List<Car> cars;

    public static void main1(String[] args) {
        Car car = new Car();
        List<Car> cars = new ArrayList<>();
        cars.add(car);
        car.setCars(cars);
        System.out.println(JSON.toJSONString(car));
    }

    public static class A {

        public List<B> b = new ArrayList<>();
    }

    public static class B {

        public A a;
        public String content = "B content";
    }

    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        A a = new A();
        B b = new B();

        a.b.add(b);
        b.a = a;

        System.out.println(mapper.writeValueAsString(a));
        System.out.println(mapper.writeValueAsString(b));
    }
}
