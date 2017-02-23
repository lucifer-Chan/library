package cn.ttsales.controller;

import net.sf.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 露青 on 2016/10/12.
 */
@Controller
@RequestMapping("mv")
public class TestController {
    @RequestMapping("test1")
    public String jump(){
        return "test1";
    }

    @RequestMapping("test2")
    public String jump2(){
        return "test2.html";
    }

//    @RequestMapping("test3")
//    public ModelAndView defaultErrorHandler(){
//        System.out.println("In GlobalExceptionHandler'defaultErrorHandler");
//        ModelAndView mav = new ModelAndView();
//        mav.addObject("exception", "aiaiaiaiaiaia");
//        mav.addObject("url", "http://ppppppppppp");
//        mav.setViewName("/error");
//        return mav;
//    }

    public static void main(String [] atrsgs){
        JSONArray j = new JSONArray();
        List<String> items = new ArrayList<>();
        items.add("A");
        items.add("B");
        items.add("C");
        items.add("D");
        items.add("E");
        items.forEach(item->j.add(item));
        System.out.println(j);

        List<A> list = new ArrayList<A>();
        list.add(new A(10));
        list.add(new A(8));
        list.add(new A(9));
        list.add(new A(3));
        list.add(new A(6));
        list.add(new A(7));

        List<String> ret = list.parallelStream()
                .filter(a -> a.i > 5 && a.i < 10)
                .sorted((final A a1, final A a2) -> a1.i.compareTo(a2.i))
                .map(A::getS)
                .collect(Collectors.toList());

        System.out.println(JSONArray.fromObject(ret));
    }

    public static class A{
        public A(Integer i){
            this.i = i;
            this.s = i + "~";
        }

        Integer i;
        String s;

        public String getS(){
            return s;
        }
    }
}
