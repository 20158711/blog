package org.wingstudio.blog;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<Item> items=new ArrayList<>();
        items.add(new Item(1,"first"));
        items.add(new Item(2,"second"));
        items.add(new Item(1,"fire"));
        items.forEach(System.out::print);
        items.remove(new Item(1,null));
        items.forEach(System.out::println);
    }
}

@Getter @Setter @AllArgsConstructor @EqualsAndHashCode(of = "id")
class Item{
    private int id;
    private String desc;

    @Override
    public String toString() {
        return "Item{" +
                "desc='" + desc + '\'' +
                '}';
    }
}
