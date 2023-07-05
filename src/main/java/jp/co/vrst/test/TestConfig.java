package jp.co.vrst.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.Objects;

import jp.co.vrst.basis.annotations.component.Bean;
import jp.co.vrst.basis.annotations.component.Config;

@Config
public class TestConfig {

    BufferedReader bufferedReader;
    
    @Bean
    public BufferedReader bufferedReader() {
        if (Objects.isNull(this.bufferedReader)) {
            this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        }
        return this.bufferedReader;
    }
}
