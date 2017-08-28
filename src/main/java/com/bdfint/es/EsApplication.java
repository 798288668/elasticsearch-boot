package com.bdfint.es;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

/**
 * @author fengcheng
 * @version 2017/8/26
 */
@SpringBootApplication
public class EsApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(EsApplication.class, args);
    }

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public void run(String... strings) throws Exception {
        System.out.println(elasticsearchTemplate);
    }
}
