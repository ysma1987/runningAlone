import com.alibaba.fastjson.JSON;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import lombok.Data;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) {
        List<Long> policyLabs = Arrays.asList(1L,2L,34L,56L,78L);
        List<Integer> labIds = policyLabs.stream()
                .mapToInt(Long::intValue)
                .collect(ArrayList::new, List::add, List::addAll);
        System.out.println(labIds);

    }
    public static void main5(String[] args) {
        List<Person> list = Arrays.asList(new Person("ysma", 18),
                new Person("ysma", 20),
                new Person("zsy",18));
        Map<String, Person> map = list.stream().collect(Collectors.toMap(Person::getName, v->v, (v1, v2)->v2));
        System.out.println(JSON.toJSONString(map));
    }
    public static void main4(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10000);
        queue.offer(1);
        Integer val;
        while ((val = queue.poll()) != null){

            System.out.println(val);
        }
        /*for(int i =0; i< 58; i++)
            queue.offer(i);

        int times = (queue.size() + 9)/10;
        for(int i =0; i< times; i++){
            List<Integer> list = new ArrayList<>(10);
            queue.drainTo(list, 10);
            System.out.println(JSON.toJSONString(list));
        }*/
    }
    public static void main3(String[] args) {
        ClassToInstanceMap<Integer> classToInstanceMap = MutableClassToInstanceMap.create();
        classToInstanceMap.put(Integer.class, 1024);
        classToInstanceMap.compute(Integer.class, (aClass, integer) -> {
            if(integer != null){
                System.out.println("old val:" + integer);
                return integer + 1;
            }
            return 1026;
        });
        System.out.println(classToInstanceMap.get(Integer.class));
    }

    @Data
    static class Person{
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public Person() {
        }

    }
}
