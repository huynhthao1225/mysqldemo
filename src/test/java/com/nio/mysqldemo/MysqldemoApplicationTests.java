package com.nio.mysqldemo;

import com.thao.GetData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MysqldemoApplicationTests {

	@Test
	public void contextLoads() {
		GetData getData = new GetData();
		System.out.println(getData.getValue());
	}

}
