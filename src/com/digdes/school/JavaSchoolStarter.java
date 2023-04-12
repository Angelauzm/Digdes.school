package com.digdes.school;

import java.util.*;

public class JavaSchoolStarter {
    List<Map<String, Object>> data;
    Map<String, String> map;
    String symb = "1234567890-.";

    // Дефолтный конструктор
    public JavaSchoolStarter() {

        data = new ArrayList<>();
        map = new LinkedHashMap<>();

        //колонки
        map.put("'id'", "column");
        map.put("'lastname'", "column");
        map.put("'age'", "column");
        map.put("'cost'", "column");
        map.put("'active'", "column");

        //команды
        map.put("insert", "a");
        map.put("update", "b");
        map.put("delete", "c");
        map.put("select", "d");
        map.put("where", "f");
        map.put("values", "g");

        //операнды
        map.put("=", "compare");
        map.put("!=", "compare");
        map.put(">=", "compare");
        map.put("<=", "compare");
        map.put("<", "compare");
        map.put(">", "compare");
        //логические
        map.put("and", "and");
        map.put("or", "or");

        map.put("like", "operand");
        map.put("ilike", "operand");

        //булевые переменные
        map.put("true", "bool");
        map.put("false", "bool");

        //лексема запятой
        map.put(",", "zap");
    }

    // На вход запрос, на выход результат выполнения запроса
    public List<Map<String, Object>> execute(String request) throws Exception {
        System.out.println(request);
        //Создание списка лексем
        List<Lexema> lexems = getLexems(request);

        /*for (var item : lexems) {
            System.out.println(item.key + " " + item.value);
        }*/

        List<Lexema> whereLexems = getWhereLexems(lexems);
        List<Lexema> val = getValueLexems(lexems);

        /*System.out.println("whereLexems");
        for (var item : whereLexems) {
            System.out.println(item.key + " " + item.value);
        }

        System.out.println("valueLexems");
        for (var item : val) {
            System.out.println(item.key + " " + item.value);
        }*/

        Map<String, Object> valuesMap = getValuesMap(val);

        //INSERT
        if (lexems.get(0).key == "a") {
            data.add(valuesMap);
            return data;
        }

        //UPDATE
        if (lexems.get(0).key == "b") {
            List<Map<String, Object>> filteredRows;

            if (whereLexems.size() > 0) {
                filteredRows = getFilteredRows(whereLexems);

            } else {
                filteredRows = data;
            }

            for (var ix : filteredRows) {
                for (var item : valuesMap.entrySet()) {
                    ix.put(item.getKey(), item.getValue());
                }
            }

            return data;

        }

        //DELETE
        if (lexems.get(0).key == "c") {
            List<Map<String, Object>> filteredRows;

            if (whereLexems.size() > 0) {
                filteredRows = getFilteredRows(whereLexems);

            } else {
                filteredRows = data;
            }

            List<Map<String, Object>> new_data = new ArrayList<>();
            for (var ix : data) {
                if (!filteredRows.contains(ix)) { //
                    new_data.add(ix);
                }
            }
            data = new_data;
            return data;
        }

        //SELECT
        if (lexems.get(0).key == "d") {
            List<Map<String, Object>> filteredRows;

            if (whereLexems.size() > 0) {
                filteredRows = getFilteredRows(whereLexems); //

            } else {
                filteredRows = data;
            }

            return filteredRows;
        }

        return data;
    }

    private Map<String, Object> getValuesMap(List<Lexema> lex1) {
        Map<String, Object> row = new HashMap<>();

        for (int i = 0; i < lex1.size(); i += 4) {
            Object lex_value = lex1.get(i + 2).value; //значение лексемы
            row.put(lex1.get(i).value.toString(), lex_value);

        }

        return row;
    }

    private int sub(Object op1, Object op2) throws Exception {

        if (op1 instanceof Long) {
            if (!(op2 instanceof Long)) {
                throw new Exception("неверный тип");
            }
            return (int) ((Long) op2 - (Long) op1);
        }

        if (op1 instanceof Double) {
            if (!(op2 instanceof Double)) {
                throw new Exception("неверный тип");
            }
            return (int) ((Double) op2 - (Double) op1);
        }

        if (op1 instanceof String) {
            if (!(op2 instanceof String)) {
                throw new Exception("неверный тип");
            }
            if ((String) op2 == (String) op1) {
                return 0;
            } else {
                throw new Exception("строки не равны");
            }
        }

        if (op1 instanceof Boolean) {
            if (!(op2 instanceof Boolean)) {
                throw new Exception("неверный тип");
            }
            if ((Boolean) op2 == (Boolean) op1) {
                return 0;
            } else {
                throw new Exception("неверный тип");
            }
        }
        throw new Exception("операция не найдена");
    }


    private List<Map<String, Object>> getFilteredRows(List<Lexema> whereLexems) {

        List<Map<String, Object>> result = new ArrayList<>();

        for (var ix : data) {

            List<Lexema> workLexems = new ArrayList<Lexema>();
            workLexems.addAll(whereLexems);

            for (int i = 0; i < workLexems.size(); i++) {
                if (workLexems.get(i).key == "column") {
                    //значение лексемы с ключом column равно
                    String key;
                    key = workLexems.get(i).value.toString();
                    Lexema lex1 = new Lexema("var", ix.get(key));
                    workLexems.remove(i);
                    workLexems.add(i, lex1);
                }
            }
            int i = 0;
            while (i < workLexems.size()) {

                if (workLexems.get(i).key == "compare") {
                    Boolean res = false;
                    try {
                        int r = sub(workLexems.get(i - 1).value, workLexems.get(i + 1).value);
                        switch (workLexems.get(i).value.toString()) {
                            case ">":
                                res = r < 0;
                                break;
                            case "<":
                                res = r > 0;
                                break;
                            case "=":
                                res = (r == 0);
                                break;
                            case "!=":
                                res = (r != 0);
                                break;
                            case ">=":
                                res = r <= 0;
                                break;
                            case "<=":
                                res = r >= 0;
                                break;
                        }
                    } catch (Exception e) {
                        res = false;
                    }
                    Lexema lex2 = new Lexema("var", res);
                    workLexems.remove(i - 1);
                    workLexems.remove(i - 1);
                    workLexems.remove(i - 1);
                    workLexems.add(i - 1, lex2);

                } else {
                    i++;
                }
            }
            i = 0;
            while (i < workLexems.size()) {
                if (workLexems.get(i).key == "and") {
                    Lexema lex2 = new Lexema("var", (Boolean) workLexems.get(i - 1).value && (Boolean) workLexems.get(i + 1).value);
                    workLexems.remove(i - 1);
                    workLexems.remove(i - 1);
                    workLexems.remove(i - 1);
                    workLexems.add(i - 1, lex2);

                } else {
                    i++;
                }
            }
            i = 0;
            while (i < workLexems.size()) {
                if (workLexems.get(i).key == "or") {
                    Lexema lex2 = new Lexema("var", (Boolean) workLexems.get(i - 1).value || (Boolean) workLexems.get(i + 1).value);
                    workLexems.remove(i - 1);
                    workLexems.remove(i - 1);
                    workLexems.remove(i - 1);
                    workLexems.add(i - 1, lex2);

                } else {
                    i++;
                }
            }
            if ((Boolean) workLexems.get(0).value){
                result.add(ix);
            }
        }
        return result;

    }


    List<Lexema> getLexems(String request) {
        List<Lexema> lexems = new ArrayList<>();

        while (true) {
            request = request.trim();
            if (request.isEmpty()) {
                break;
            }

            Boolean isFind = false;

            String req = request.toLowerCase();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String lex;
                int idx;
                int pos = req.indexOf(entry.getKey());

                if (pos == 0) {
                    lex = entry.getValue();
                    Object object = entry.getKey().replace("'", "");

                    if (lex.toLowerCase() == "bool") {
                        object = Boolean.valueOf(object.toString());
                    }

                    lexems.add(new Lexema(lex, object));
                    idx = entry.getKey().length();
                    request = request.trim().substring(idx);
                    isFind = true;
                    break;
                }
            }

            if (isFind) {
                continue;
            }
            //создание новой лексемы - данные в таблицу
            char firstChar = request.charAt(0);
            if (firstChar == '\'') {
                int pos2 = request.indexOf('\'', 1);
                lexems.add(new Lexema("var", request.substring(1, pos2)));
                request = request.substring(pos2 + 1);
                continue;
            }

            int idx = 0;
            while (idx < request.length() && symb.indexOf(request.charAt(idx)) >= 0) {
                idx++;
            }

            if (idx > 0) {
                Object num_val;
                String num = request.substring(0, idx);
                if (num.contains(".")) {
                    num_val = Double.valueOf(num);

                } else {
                    num_val = Long.valueOf(num);
                }

                lexems.add(new Lexema("var", num_val));
                request = request.substring(idx);
            }

        }
        return lexems;
    }

    //VALUES
    List<Lexema> getValueLexems(List<Lexema> lexems) {
        int start = -1;
        for (int i = 0; i < lexems.size(); i++) {
            if (lexems.get(i).key == "g") {
                start = i;
                break;
            }
        }

        if (start == -1) {
            return new ArrayList<Lexema>();
        }

        int end = start;
        for (int i = start + 1; i < lexems.size(); i++) {
            if (lexems.get(i).key == "f") {
                end = i;
                break;
            }
        }

        if (start == end) {
            end = lexems.size();
        }
        return lexems.subList(start + 1, end);
    }

    //WHERE
    List<Lexema> getWhereLexems(List<Lexema> lexems) {
        int start = -1;
        for (int i = 0; i < lexems.size(); i++) {
            if (lexems.get(i).key == "f") {
                start = i;
                break;
            }
        }
        if (start == -1) {
            return new ArrayList<Lexema>();
        }

        return lexems.subList(start + 1, lexems.size());

    }
}



