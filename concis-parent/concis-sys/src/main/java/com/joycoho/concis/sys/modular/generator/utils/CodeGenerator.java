package com.joycoho.concis.sys.modular.generator.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.File;
import java.util.Collections;

/**
 * @Classname CodeGenerator
 * @Description 代码生成器工具类
 * @Version 1.0.0
 * @Date 2022/11/14 20:29
 * @Created by Leo
 */
public class CodeGenerator {
    /**
     * 数据库链接
     */
    public static final String DB_URL = "jdbc:mysql://127.0.0.1:13306/concis?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false";
    /**
     * 数据库用户名
     */
    public static final String DB_USER = "admin";
    /**
     * 数据库密码
     */
    public static final String DB_PASS = "admin";
    /**
     * 数据库表名
     */
    public static final String DB_TABLE = "sys_user";
    /**
     * 数据库表前缀
     */
    public static final String DB_TABLE_PREFIX = "sys_";
    /**
     * 设置父包名
     */
    public static final String PARENT_PACKAGE_NAME = "com.tangketech.portal.modular";
    /**
     * 设置模块名
     */
    public static final String MODULAR_NAME = "user";
    /**
     * Mapper.java 文件输出路径
     */
    public static final String CODE_OUTPUT = "/Users/shengu/Env/codeGen/portal-admin/src/main/java";
    /**
     * Mapper.xml 文件输出路径
     */
    public static final String MAPXML_OUTPUT = CODE_OUTPUT + transPackageName2Path(PARENT_PACKAGE_NAME) + MODULAR_NAME + "/dao/mapping/";

    public static void main(String[] args) {
        FastAutoGenerator.create(DB_URL, DB_USER, DB_PASS)
                .globalConfig(builder -> {
                    builder.author("Leo")//设置作者
                            //.enableSwagger()//开启swagger模式
                            .dateType(DateType.ONLY_DATE)
                            .fileOverride()//覆盖已生成文件
                            .outputDir(CODE_OUTPUT);
                })
                .packageConfig(builder -> {
                    builder.parent(PARENT_PACKAGE_NAME)
                            .moduleName(MODULAR_NAME)
                            .mapper("dao")
                            .pathInfo(Collections.singletonMap(OutputFile.xml, MAPXML_OUTPUT));
                })
                .strategyConfig(builder -> {
                    builder.addInclude(DB_TABLE)
                            .addTablePrefix(DB_TABLE_PREFIX)
                            .entityBuilder().enableLombok().superClass("Model<" + upperCaseFirstChar(MODULAR_NAME) + ">");
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }

    /**
     * 将模块包名的首字母大写
     * @param moduleName
     * @return
     */
    private static String upperCaseFirstChar(String moduleName) {
        char first = moduleName.charAt(0);
        return (String.valueOf(first).toUpperCase() + moduleName.substring(1));
    }

    /**
     * 将包名转为文件路径格式
     * @param packageName
     * @return
     */
    private static String transPackageName2Path(String packageName) {
        String SEP = File.separator;
        String[] aa = packageName.split("\\.");
        StringBuilder sb = new StringBuilder();
        for (String a : aa) {
            sb.append(SEP).append(a);
        }
        return sb.append(SEP).toString();
    }
}
