package com.yunweb.controller;

import com.yun.service.SayHelloService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangyunlong
 * @date 2018/9/11 21:33
 */
@RestController
public class CodeAnalyseController {

    @Resource
    private SayHelloService sayHelloService;

    public static void main(String[] args) {

        File project = new File("C:/Users/wangyunlong/Desktop/name.txt");
        File post = new File("C:/Users/wangyunlong/Desktop/post.txt");
        List<String> projects = getNames(project);
        List<String> posts = getNames(post);

        projects = splitNames(projects);
        setNames(projects, "C:/Users/wangyunlong/Desktop/newProjectsName.txt");
        posts = splitNames(posts);
        setNames(posts, "C:/Users/wangyunlong/Desktop/newPostsName.txt");

        Map<Integer, Integer> projectsMap = countNamesLength(projects);
        Map<Integer, Integer> postsMap = countNamesLength(posts);

        Map<Character, Integer> projectsSymbolMap = countNamesSymbol(projects);
        Map<Character, Integer> postsSymbolMap = countNamesSymbol(posts);

        System.out.println("111");
    }

    /**
     * 1. 根据符号(-,—)分隔出文本主要内容.--测试':'分隔效果不好--
     * 2. 去除数字? 还是只去除时间格式的数字
     * 3. 去除特殊符号
     * 4. 文字过滤
     * 5. 去除首尾空格，中文间空格，中英文间空格
     * @param names
     * @return
     */
    public static List<String> splitNames(List<String> names){
        if (CollectionUtils.isEmpty(names)) {
            return Collections.emptyList();
        }
        for (int i=0; i<names.size(); i++) {
            String name = names.get(i);


            // 1
            if (name.contains("-")) {
                String[] split = name.split("\\-+");
                name = Arrays.stream(split).max((o1, o2) -> o1.length()>o2.length()?1:o1.length()==o2.length()?0:-1).get();
            }
            if (name.contains("—")) {
                String[] split = name.split("\\—+");
                name = Arrays.stream(split).max((o1, o2) -> o1.length()>o2.length()?1:o1.length()==o2.length()?0:-1).get();
            }
            // 2
            name = name.replaceAll("\\d{4}.\\d{1,2}.\\d{1,2}日","");
            name = name.replaceAll("\\d{4}.\\d{1,2}.\\d{1,2}","");
            name = name.replaceAll("\\d{4}年\\d{1,2}月","");
            name = name.replaceAll("\\d{1,2}月\\d{1,2}日","");
            name = name.replaceAll("\\d{4}年","");

            name = name.replaceAll("\\d+","");
            // 3
            name = removeSpecialCharacters(name);
            // 4
            name = wordFilter(name);
            // 5
            name = name.trim();
            name = name.replaceAll("([\\u4e00-\\u9fa5])\\s+([\\u4e00-\\u9fa5])","$1$2");
            name = name.replaceAll("([\\u4e00-\\u9fa5])\\s+([a-zA-Z])","$1$2");
            name = name.replaceAll("([a-zA-Z])\\s+([\\u4e00-\\u9fa5])","$1$2");
            names.set(i, name);
        }
        return names;
    }

    /**
     * 词汇长度分布
     * @param names
     * @return
     */
    public static Map<Integer, Integer> countNamesLength(List<String> names){
        Map<Integer, Integer> map = new HashMap<>(16);
        names.forEach(name -> {
            int len = name.length();
            map.put(len, Objects.isNull(map.get(len))?1:map.get(len)+1);
        });
        return map;
    }

    /**
     * 特殊符号统计
     * @param names
     * @return
     */
    public static Map<Character, Integer> countNamesSymbol(List<String> names){
        Map<Character, Integer> charMap = new HashMap<>(16);
        // 中文正则
        String REGEX_CHINESE = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(REGEX_CHINESE);
        names.forEach(name -> {
            Matcher mat = pat.matcher(name);
            name = mat.replaceAll("");
            name = name.replaceAll("[a-zA-Z]","");
            name = name.replaceAll("\\d+","");
            char[] chars = name.toCharArray();
            for (char ch : chars) {
                charMap.put(ch, Objects.isNull(charMap.get(ch))?1:charMap.get(ch)+1);
            }
        });
        return charMap;
    }

    /**
     * 去除特殊字符
     * @param name
     * @return
     */
    public static String removeSpecialCharacters(String name){
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？π❀♂▄∇ト《》－ろ．『●ハ』０═→２こ９⊙ホ\\ボ_Ⅰ◡\"✥∩♪ルの┳·︻┻ー�\uFEFF✿タ｀＃☆⒈\tω⒊○「」－⒎⒏０⒐←№√｜＝～＞_■éí′［ー］]";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(name);
        return mat.replaceAll("").trim();
    }

    /**
     * 去除无意义文字
     * @param name
     * @return
     */
    public static String wordFilter(String name) {
        String[] source = {"怎么办","该怎么走","求问大神","好心人","求大神","望大神","求帮助","该如何","怎么样","最有效","要如何","最新","有什么","为什么","怎么回事","等问题","请问","大神","能否","怎么","如何","请","各位","大哥","大姐","大神","我是","新手","小白","怎么","大神","请教","指教","指导","可以","一下","前辈","跪谢","啦","帮忙","急","如下","关于","问题","啊","急求","谢","哪位","求","哪些","哪种","介绍","第*章","第*讲","第*课","浅谈","浅析","什么","常用"  ,"大家好","你知道",  "的作品","转载"};
        for (int i = 0; i < source.length; i++) {
            name = name.replace(source[i], "");
        }
        return name;
    }


    /**
     * 读标题
     * @param file
     * @return
     */
    public static List<String> getNames(File file){
        List<String> names = new ArrayList<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String name;
            while((name = br.readLine())!=null){
                names.add(name);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return names;
    }

    /**
     * 写文件
     * @param names
     * @param path
     */
    public static void setNames(List<String> names, String path){
        try{
            File file = new File(path);
            //如果没有文件就创建
            if (!file.isFile()) {
                file.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            for (String l: names){
                writer.write(l + "\r\n");
            }
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 写文件(Symbol)
     * @param symbols
     * @param path
     */
    public static void setSymbols(Map<Character, Integer> symbols, String path){
        try{
            File file = new File(path);
            //如果没有文件就创建
            if (!file.isFile()) {
                file.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            symbols.forEach((character, integer) -> {
                try {
                    writer.write(character);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
