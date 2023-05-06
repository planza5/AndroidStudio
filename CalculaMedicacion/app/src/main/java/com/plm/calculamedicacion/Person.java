package com.plm.calculamedicacion;

public class Person {
    private long id;
    private String name;
    private String surname;
    private int age;
    private boolean dead;

    public Person(String name, String surname, int age, boolean dead){
        this.name=name;
        this.surname=surname;
        this.age=age;
        this.dead=dead;
    }
}
