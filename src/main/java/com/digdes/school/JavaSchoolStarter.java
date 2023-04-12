package com.digdes.school;

import com.digdes.school.task.executor.CRUDRequestExecutor;
import com.digdes.school.task.executor.RequestExecutor;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JavaSchoolStarter {

    private final RequestExecutor executor = new CRUDRequestExecutor();

    public JavaSchoolStarter() {

    }

    public List<Map<String, Object>> execute(String request) throws Exception {
        request = Objects.requireNonNull(request, "Request is null").trim();
        return executor.executeRequest(request);
    }

//    public static void main(String[] args) throws Exception {
//        JavaSchoolStarter app = new JavaSchoolStarter();
//        app.execute("insert values 'id'=0,  'lasTname'='aba', 'age'=10, 'cost'=31.12,   'active'=true"); //
//        app.execute("insert values 'id'=1,  'lastname'='bba', 'age'=20, 'cost'=31.12,   'active'=false"); //
//        app.execute("insert values 'id'=2,  'lastname'='cBa', 'age'=30, 'cost'=null,   'active'=true");   //
//        app.execute("insert values 'id'=3,  'lastname'='DBA', 'age'=null, 'cost'=31.12,   'active'=false"); //
//        app.execute("insert values 'id'=4,  'lastname'='faf', 'age'=50, 'cost'=3132.12, 'active'=true");
//        app.execute("insert values 'id'=5,  'lastname'='oqo', 'age'=60, 'cost'=31.1642, 'active'=false"); //
//        app.execute("insert values 'id'=6,  'lastname'='LoL', 'age'=70, 'cost'=31.12,   'active'=false");//
//        app.execute("insert values 'id'=7,  'lastname'='lOl', 'age'=80, 'cost'=31.12,   'active'=false");//
//        app.execute("insert values 'id'=8,  'lastname'='fas', 'age'=90, 'cost'=31.12,   'active'=true");
//        app.execute("insert values 'id'=9, 'lastname'='faa', 'age'=10, 'cost'=31.12,   'active'=true");
//        app.execute("insert values 'id'=10, 'lastname'='213', 'age'=20, 'cost'=31.12,   'active'=false");//10
//        app.execute("insert values 'id'=11, 'lastname'='*', 'age'=20, 'cost'=31.12,   'active'=false");//10
////        System.out.println(app.execute("select"));
////        System.out.println(app.execute("select").size());
//        System.out.println(app.execute("select"));
//        System.out.println(app.execute("select where 'id'>=10"));
//        System.out.println(app.execute("select where 'id'>=10 aNd 'id'!= 12"));
//        System.out.println(app.execute("sElect wHere ('id'>3 aNd ('age'<=50 or ('active'=true and 'age'>70)) AND'id'!=10)"));//
//        System.out.println(app.execute("sElect wHere ('id'>3 aNd ('age'<=50 or ('active'=true and 'age'>70 ) and 'lastname' ilike '%a%') AND'id'!=10)"));//
//        System.out.println(app.execute("sElect wHere ('id'>3 & ('age'<=50 or ('active'=true and 'age'>70)) AND'id'!=10)"));//
//        System.out.println(app.execute("select where 'active'=true"));
//        System.out.println(app.execute("select where 'lastname' like 'aba'"));
//        System.out.println(app.execute("select where 'lastname' like '%ba'"));
//        System.out.println(app.execute("select where 'laSTname' ilike '%o%'"));
//        System.out.println(app.execute("select where 'laSTname' like 'l%l'"));
//        System.out.println(app.execute("select where 'lastname' = 'aba'"));
//    }
}
