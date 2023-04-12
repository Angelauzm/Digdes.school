package com.digdes.school;

import java.util.List;
import java.util.Map;

public class Main {

   public static void main(String... args){
       JavaSchoolStarter starter = new JavaSchoolStarter();
       try {
           //Вставка строки в коллекцию
           printResult(starter.execute("INSERT VALUES 'lastName' = 'Федоров' , 'id'=3, 'age'=40, 'active'=true"));
           printResult(starter.execute("INSERT VALUES 'lastName' = 'Тихов' , 'id'=4, 'age'=27, 'active'=true"));
           printResult(starter.execute("INSERT VALUES 'lastName' = 'Фалько' , 'id'=5, 'age'=29, 'active'=false"));
           printResult(starter.execute("SELECT WHERE 'age'>=29"));
           printResult(starter.execute("SELECT WHERE 'id'=3"));
           printResult(starter.execute("SELECT WHERE 'active'=true AND 'age'=27"));
           printResult(starter.execute("SELECT WHERE 'id'=4 OR 'age'=40"));
           //Изменение значения которое выше записывали
           printResult(starter.execute("UPDATE VALUES 'active'=false, 'cost'=10.1 where 'id'=3"));
           //Получение всех данных из коллекции (т.е. в данном примере вернется 1 запись)
           printResult(starter.execute("SELECT "));
           printResult(starter.execute("SELECT WHERE 'id'=3"));
           printResult(starter.execute("DELETE WHERE 'id'=3"));
           printResult(starter.execute("DELETE WHERE 'id'=4"));
           
       }catch (Exception ex){
           ex.printStackTrace();
       }
   }

    private static void printResult(List<Map<String, Object>> result) {
        System.out.printf("============================================================================\n");
        System.out.printf("id\t\tlastname\tage\t\t\tcost\t\tactive\n\n");
        for (var item : result) {
            System.out.printf("%d\t\t%s\t\t%d\t\t%f\t\t%b\n\n", item.get("id"), item.get("lastname"), item.get("age"),
                    item.get("cost"), item.get("active"));
        }
        System.out.printf("============================================================================\n\n");
    }
}
