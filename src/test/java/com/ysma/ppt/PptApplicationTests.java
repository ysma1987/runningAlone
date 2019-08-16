package com.ysma.ppt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * springboot2 + junit5
 * 测试框架
 */
@ExtendWith(SpringExtension.class)//导入spring测试框架[2]
@SpringBootTest//提供spring依赖注入
@Transactional//事务管理，默认回滚,如果配置了多数据源记得指定事务管理器
@DisplayName("PPT Test")
class PptApplicationTests {

	@Test
	void contextLoads() {
	}

}
