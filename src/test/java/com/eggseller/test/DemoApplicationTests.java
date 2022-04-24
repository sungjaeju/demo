package com.eggseller.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.eggseller.test.controller.BasicController;
import com.eggseller.test.model.Task;
import com.eggseller.test.model.User;
import com.eggseller.test.repository.eggseller.TaskMapper;
import com.eggseller.test.repository.test.UserMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class DemoApplicationTests {
	
	@Autowired
	BasicController basicController;
	
	@Test
	public void testController() {
		assert basicController.admin().equals("admin");
	}

	@Test
	public void testBreakIf() {
		int i = 0;
		p1:
		if (10 > i) {
			log.info("########### first i: {}", i);
			i++;
			if (0 < i) break p1;
			log.info("########### second i: {}", i);
		}
		
		log.info("########### end i: {}", i);
	}

	@Test
	public void getMethode() {
	    StackWalker walker = StackWalker.getInstance();
	    Optional<String> methodName = walker.walk(frames -> frames
	      .findFirst()
	      .map(StackWalker.StackFrame::getMethodName));

	    assertTrue(methodName.isPresent());
	    assertEquals("optionalTest", methodName.get());

	}
	
	@Test
	public void optionalTest() {
		User user = new User();
		Optional.of(user)
			.map(User::getUsername)
			.ifPresent( System.out::println);
		
		String username = Optional.of(user)
				.filter(u -> null != u.getUsername() && !u.getUsername().isBlank())
				.map(User::getUsername)
				.orElse("noname");
		log.info("username: {}", username);
		
		Set<Object> list = Arrays.asList(1, 2, 2).stream().map(a -> a + 10).collect(Collectors.toSet());
		log.info("#### list: {}", list);
		
	}
	
	
	
	
	@Test
	void contextLoads() {
	}

	public static Map<String, String> articleMapOne;
	static {
	    articleMapOne = new HashMap<>();
	    articleMapOne.put("ar01", "Intro to Map");
	    articleMapOne.put("ar02", "Some article");
	}
	
	@Test
	public void mapTest() {
	    
	    this.articleMapOne.put(
	      "NewArticle1", "Convert array to List"
	    );
	    
	    assertEquals(
	      this.articleMapOne.get("NewArticle1"), 
	      "Convert array to List"
	    );  
	}
	
	

	@Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(2);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://localhost/test?autoReconnect=true&serverTimezone=UTC");
        config.setUsername("eggseller");
        config.setPassword("387421");
        return new HikariDataSource(config);
    }
	
	@Test
    public void testConection() throws Exception{
        try(Connection con = dataSource().getConnection()){        	
            log.info("######## mysql con: {}", con);
            log.info("######## mysql getCatalog: {}", con.getCatalog());
            
            Statement stat = con.createStatement();
            String sql = "select * from users";
            ResultSet rs = stat.executeQuery(sql);
            while(rs.next()) {            	
            	log.info("######## mysql result: {} {} {}", rs.getString(1), rs.getString(2), rs.getString(3));
            }
            
            rs.close();
            stat.close();
            con.close();
            
            
        } catch(Exception e){
            e.printStackTrace();
        }
    }
	
	@Autowired
	@Qualifier("testJdbcTemplate")
    private JdbcTemplate tesDb;
	
	@Autowired
	@Qualifier("eggsellerJdbcTemplate")
	private JdbcTemplate eggsellerDb;
	
	@Test
	public void testJdbc() {
        int count = tesDb.queryForObject("SELECT COUNT(*) FROM USERS", Integer.class);
        log.info("######## jdbc.test userCount: {}", count);
        List<Map<String,Object>> tasks = eggsellerDb.queryForList(
				"SELECT * FROM tasks where uid >= ? and taskName <> ?", new Object[] {1, "vuejs"}
			);
		log.info("####### jdbc.eggseller tasks: {}\n", tasks);
    }
	
	/*
	 * Test for SqlSession
	 */
	@Autowired
	@Qualifier("testSqlSessionFactory")
	private SqlSessionFactory testSqlSessionFactory;

	@Test
	public void testTestSqlSessionFactory() {
		try(Connection con = testSqlSessionFactory.openSession().getConnection()) {
			log.info("##### Connection is success: {}", con.getCatalog());
			
			//Mybatis
			UserMapper userMapper = testSqlSessionFactory.openSession().getMapper(UserMapper.class);

			List<User> getUsers = userMapper.getUsersXml();
			log.info("###### getUsers: {}", getUsers.toString());

//			User user = userMapper.getUserByUsername("jsj");
//			log.info("##### getUser: {}\n", user);
			User user = userMapper.getUser("jsj");
			log.info("##### getUser: {}\n", user);
			
		} catch(Exception e) {
			log.info("####### sql error: {}\n", e);
		}
		
	}
	
	@Autowired
	@Qualifier("eggsellerSqlSessionFactory")
	private SqlSessionFactory eggsellerSqlSessionFactory;

	@Test
	public void testEggsellerSqlSessionFactory() {
		try(Connection con = eggsellerSqlSessionFactory.openSession().getConnection()) {
			log.info("##### Connection is success: {}", con.getCatalog());
			
			//Mybatis
			TaskMapper taskMapper = eggsellerSqlSessionFactory.openSession().getMapper(TaskMapper.class);
			List<Task> getTasks = taskMapper.getTasksXml();
			log.info("###### getTasks: {}\n", getTasks.toString());
			
		} catch(Exception e) {
			log.info("####### sql error: {}\n", e);
		}
		
	}
	
	@Test
	public void encodePassword() {
		String password = new BCryptPasswordEncoder().encode("jsj");
		log.info("##### password: {}", password);
	}
	
	@Test
    void jasypt() {
        String username = "eggseller";
        String password = "387421";

        System.out.println(jasyptEncoding(username));
        System.out.println(jasyptEncoding(password));
    }

    public String jasyptEncoding(String value) {

        String key = "mykey";
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword(key);
        return pbeEnc.encrypt(value);
    }
	
}
