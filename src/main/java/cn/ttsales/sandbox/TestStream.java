package cn.ttsales.sandbox;

import cn.ttsales.domain.PreBureau;
import cn.ttsales.util.KeyValue;
import net.sf.json.JSONObject;

import java.lang.annotation.ElementType;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.ttsales.util.Constant.*;

/**
 * Created by 露青 on 2016/10/19.
 */
public class TestStream {

    private static List<Materail> datas = new ArrayList<Materail>(){{
        add(new Materail(1,1,1,1,"爱爸爸妈妈"));
        add(new Materail(2,1,1,2,"爷爷和小树"));
        add(new Materail(3,1,1,3,"比尾巴"));
        add(new Materail(4,1,1,4,"影子"));
        add(new Materail(5,1,1,5,"小竹排在画中游"));
        add(new Materail(6,1,1,6,"哪座房子最漂亮"));
        add(new Materail(7,1,1,7,"神奇的塔"));
        add(new Materail(8,1,1,8,"我坐上了飞船"));
        add(new Materail(9,1,1,9,"猜一猜"));
        add(new Materail(10,1,1,10,"小小的船"));
        add(new Materail(11,1,1,11,"自己去吧"));
        add(new Materail(12,1,1,12,"三个小学生"));
        add(new Materail(13,1,1,13,"老公公"));
        add(new Materail(14,1,1,14,"画"));
        add(new Materail(15,1,1,15,"雪地里的小画家"));
        add(new Materail(16,1,1,16,"小猫种鱼"));
        add(new Materail(17,1,1,17,"小山羊"));
        add(new Materail(18,1,1,18,"小公鸡和小鸭子"));
        add(new Materail(19,1,2,1,"草"));
        add(new Materail(20,1,2,2,"春晓"));
        add(new Materail(21,1,2,3,"古朗月行"));
        add(new Materail(22,1,2,4,"登鹳雀楼"));
        add(new Materail(23,1,2,5,"寻隐者不遇"));
        add(new Materail(24,1,2,6,"春风吹"));
        add(new Materail(25,1,2,7,"我爱祖国"));
        add(new Materail(26,1,2,8,"王二小"));
        add(new Materail(27,1,2,9,"我选我"));
        add(new Materail(28,1,2,10,"人有两件宝"));
        add(new Materail(29,1,2,11,"司马光"));
        add(new Materail(30,1,2,12,"聪明的华佗"));
        add(new Materail(31,1,2,13,"小猴子下山"));
        add(new Materail(32,1,2,14,"浪花"));
        add(new Materail(33,1,2,15,"大自然的语言"));
        add(new Materail(34,1,2,16,"要下雨了"));
        add(new Materail(35,1,2,17,"“红领巾”真好"));
        add(new Materail(36,1,2,18,"小狐狸卖空气"));
        add(new Materail(37,1,2,19,"好孩子"));
        add(new Materail(38,1,2,20,"明明上学"));
        add(new Materail(39,1,2,21,"两只小狮子"));
        add(new Materail(40,1,2,22,"小白兔和小灰兔"));
        add(new Materail(41,1,2,23,"王冕学画"));
        add(new Materail(42,1,2,24,"达尔文和小松鼠"));
        add(new Materail(43,1,2,25,"小壁虎借尾巴"));
        add(new Materail(44,1,2,26,"三只白鹤"));
        add(new Materail(45,1,3,1,"悯农（二）"));
        add(new Materail(46,1,3,2,"蚕妇"));
        add(new Materail(47,1,3,3,"夜宿山寺"));
        add(new Materail(48,1,3,4,"梅花"));
        add(new Materail(49,1,3,5,"江雪"));
        add(new Materail(50,1,3,6,"秋天"));

    }};

    private static class Materail{
        private Long id;
        private Long pubId;
        private Integer grade;
        private Integer lessonNumber;
        private String lessonName;

        @Override
        public String toString(){
            return "{\"id\":" + id + ",\"pubId\":" + pubId + ",\"grade\":" + grade + ",\"lessonNumber\":" + lessonNumber + ",\"lessonName\":\"" + lessonName + "\"}";
//            return JSONObject.fromObject(this).toString();
        }

        public Materail(){}

        public Materail(int id, int pubId, int grade, int lessonNumber, String lessonName) {
            this.id = (long)id;
            this.pubId =  (long)pubId;
            this.grade = grade;
            this.lessonNumber = lessonNumber;
            this.lessonName = lessonName;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getPubId() {
            return pubId;
        }

        public void setPubId(Long pubId) {
            this.pubId = pubId;
        }

        public Integer getGrade() {
            return grade;
        }

        public void setGrade(Integer grade) {
            this.grade = grade;
        }

        public Integer getLessonNumber() {
            return lessonNumber;
        }

        public void setLessonNumber(Integer lessonNumber) {
            this.lessonNumber = lessonNumber;
        }

        public String getLessonName() {
            return lessonName;
        }

        public void setLessonName(String lessonName) {
            this.lessonName = lessonName;
        }
    }

    public void t1(){
        //1、按照grade分组，每组按照lessonNumber排序
        Map<Integer, List<Materail>> ret =
                datas.stream()
                        .sorted((m1, m2) -> m1.getLessonNumber().compareTo(m2.getLessonNumber()))
                        .collect(Collectors.groupingBy(Materail::getGrade, Collectors.toList()));
        System.out.println(ret);
        System.out.println("**************************");
        //2、按照grade排序，取每个grade的第一节课打印出来
        ret.keySet()
                .stream()
                .sorted((i1,i2)->i1.compareTo(i2))
                .collect(Collectors.toList())
                .forEach(i -> System.out.println(ret.get(i).get(0)));
        //3、List转Map
        Map<Long, Materail> map = datas.stream().collect(Collectors.toMap(i->i.getId(), i->i));
        System.out.println(map.get(new Long(5)));
    }

    public static void main(String[] args) {
        System.out.println(ElementType.TYPE);
//        tree();
    }

    public static void tree(){
        PreBureau b1 = new PreBureau(){{
            setCityCode(1001);
            setCityName("杭州");
            setProvinceCode(1000);
            setProvinceName("浙江");
            setName("西湖区");
            setId(new Long(1));
        }};

        PreBureau b2= new PreBureau(){{
            setCityCode(1001);
            setCityName("杭州");
            setProvinceCode(1000);
            setProvinceName("浙江");
            setName("拱墅区");
            setId(new Long(2));
        }};

        PreBureau b3 = new PreBureau(){{
            setCityCode(1002);
            setCityName("温州");
            setProvinceCode(1000);
            setProvinceName("浙江");
            setName("鹿城区");
            setId(new Long(3));
        }};

        PreBureau b4 = new PreBureau(){{
            setCityCode(2001);
            setCityName("呼和浩特");
            setProvinceCode(2000);
            setProvinceName("内蒙古");
            setName("XX区");
            setId(new Long(4));
        }};

        List<PreBureau> preBureaus = Arrays.asList(b1,b2,b3,b4);
        //1、省
        Set<KeyValue> provinces = new HashSet<>();
        preBureaus.stream().forEach(p->provinces.add(new KeyValue(p.getProvinceCode(), p.getProvinceName())));
        System.out.println(provinces);
        //2、市
        Set<KeyValue> citys = new HashSet<>();
        Integer provinceCode = 1000;
        preBureaus.stream().forEach(p->{
            if(p.getProvinceCode().equals(provinceCode))
                citys.add(new KeyValue(p.getCityCode(), p.getCityName()));
        });
        System.out.println(citys);

        //3、市2
        Map<KeyValue, Set<KeyValue>> cities = new HashMap<>();
        provinces.stream().forEach(p->{
            Set<KeyValue> citySet = new HashSet<>();
            preBureaus.stream().forEach(c->{
                if(p.getKey().equals(c.getProvinceCode()))
                    citySet.add(new KeyValue(c.getCityCode(), c.getCityName()));
            });
            cities.put(p, citySet);
        });
        System.out.println(cities);

//        Map<Integer, String>
//        Set<Integer> provinceIds = preBureaus.stream().
//                map(PreBureau::getProvinceCode).
//                collect(Collectors.toSet());



//        Map<KeyValue, JSONObject> ps = preBureaus.stream().collect(Collectors.toMap(i-> new KeyValue(i.getId(), i.getProvinceName()), i->i.toCommonJSONObject()));

//        Map<KeyValue, Object> provinces
//        Map<KeyValue, PreBureau> ps =
//                preBureaus.stream().collect(Collectors.groupingBy(p -> new KeyValue(p.getCityCode(), p.get)))

//        Map<KeyValue, Map<KeyValue, Map<Long, String>>> tree =
//        Object o =
//                preBureaus
//                .stream()
//                .collect(Collectors.groupingBy(p->{return new KeyValue(p.getProvinceCode() + "", p.getProvinceName());}, Collectors.toList().));
//        System.out.println(o);
    }
}
