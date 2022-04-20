package com.eggseller.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.annotation.sql.DataSourceDefinition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import com.eggseller.test.model.Task;
import com.eggseller.test.model.User;
import com.eggseller.test.repository.eggseller.TaskMapper;
import com.eggseller.test.repository.test.UserMapper;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
	
	private Logger logger = LoggerFactory.getLogger(DemoApplication.class);
	
	@Autowired
	@Qualifier("testJdbcTemplate")
    private JdbcTemplate tesDb;
	
	@Autowired
	@Qualifier("eggsellerJdbcTemplate")
	private JdbcTemplate eggsellerDb;
	
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private TaskMapper taskMapper;
	
	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		List<User> usersXml = userMapper.getUsersXml();
//		logger.info("####### users: {}", usersXml.toString());
//		List<Task> tasksXml = taskMapper.getTasksXml();
//		logger.info("####### tasks: {}\n", tasksXml.toString());
		
		//List<Map>
//		List<Map<String,Object>> users = new ArrayList<>();
//		Map<String, Object> user = new HashMap<>();
//		user.put("name", "sungjae");
//		user.put("name1", "sungjae1");
//		logger.info("###### user: {}", user);
//		users.add(user);
//		users.add(new HashMap<>(Map.of("name", "yanghee")));
//		users.add(new HashMap<>() {
//			private static final long serialVersionUID = 1L;
//			{ put("name", "piljae"); }
//		});
//		logger.info("###### users: {}\n", users.toString());
//		users.forEach(us -> { 
//			logger.info("#### name: {}", us.get("name"));
//		});
//		users.stream().forEach(us -> { 
//			logger.info("#### stream name: {}", us.get("name"));
//		});
//		List<Object> names = users.stream().map(us -> { 
//			return us.get("name");
//		}).filter(name -> "sungjae".equalsIgnoreCase((String) name))
//				.collect(Collectors.toList());
//		
//		names.forEach(System.out::println);
	}
	
//	@Override
	public void runs(String... args) throws Exception {
		//Mybatis
		List<User> usersXml = userMapper.getUsersXml();
		logger.info("####### users: {}", usersXml.toString());
		List<Task> tasksXml = taskMapper.getTasksXml();
		logger.info("####### tasks: {}\n", tasksXml.toString());
		
		//JDBC Queries
		String username = tesDb.queryForObject("SELECT username FROM users where uid = ?", String.class, 1);
		logger.info("####### Jdbc.username: {}\n", username);
        
		String[] params = { "1,", "2" };
		List<Map<String,Object>> tasks = eggsellerDb.queryForList(
				"SELECT * FROM tasks where uid >= ? and taskName <> ?", new Object[] {1, "vuejs"}
			);
		logger.info("####### tasks: {}\n", tasks);
		
		//Array to stream
		Arrays.stream(args).forEach(arg -> logger.info("###### array stream: {}", arg));
		//List to stream
		List<String> argList = Arrays.asList(args);
		argList.stream().forEach(arg -> logger.info("###### li stream: {}", arg));
		
		//Sorting Array primitive
		Integer[] numbers = { 2, 1, 3, 4, 5, 6, 7, 8 };
		Arrays.sort(numbers, Collections.reverseOrder());
		logger.info("####### Array.sort(Collections.reverseOrder): {}", Arrays.toString(numbers));
		//Sorting List primitive
		List<Integer> listNumbers = Arrays.asList(numbers);
		Collections.sort(listNumbers);
//		listNumbers.sort(Comparator.naturalOrder());
		logger.info("####### List sort by Collections.sort(list): {}", listNumbers.toString());
		listNumbers.sort(Comparator.reverseOrder());
		logger.info("####### list sort by Comparator.reverseOrder: {}\n", listNumbers.toString());
		
		//List to Set, Set to List
		Set<Integer> setNumbers = new HashSet<>(listNumbers);
		logger.info("###### List to set: {}", setNumbers);
		listNumbers = new ArrayList<>(setNumbers);
		logger.info("set to list: {}", listNumbers);
		//Array to set
//		Set<Integer> arrayToSet = Arrays.stream(numbers).collect(Collectors.toSet());
		TreeSet<Integer> arrayToSet = new TreeSet<Integer>(Arrays.stream(numbers).collect(Collectors.toSet()));
		//addAll an array and a list to the Set
		arrayToSet.addAll(Arrays.asList(new Integer[] {10, 20}));
		arrayToSet.addAll(List.of(1000, 2000));
		arrayToSet.addAll(new ArrayList<>() {
			private static final long serialVersionUID = 1L;
			{ add(100); add(200); }
		});
		logger.info("##### arrayToSet.addAll: {}\n", arrayToSet);
		
		//Sort Set after converting it to list
		listNumbers = new ArrayList<>(arrayToSet);
		Collections.sort(listNumbers, Comparator.reverseOrder());
		logger.info("##### Sort Set after converting it to list: {}\n", listNumbers);
//		Set<Integer> hashset = new HashSet<Integer>(listNumbers);
		Set<Integer> linkedSet = new LinkedHashSet<Integer>(listNumbers);
		logger.info("##### Keep the set order with LinkedHashSet: {}\n", linkedSet);
		
		//List<Map>
		List<Map<String,Object>> users = new ArrayList<>();
		Map<String, Object> user = new HashMap<>();
		user.put("name", "sungjae");
		user.put("name1", "sungjae1");
		logger.info("###### user: {}", user);
		users.add(user);
		users.add(new HashMap<>(Map.of("name", "yanghee")));
		users.add(new HashMap<>() {
			private static final long serialVersionUID = 1L;
			{ put("name", "piljae"); }
		});
		logger.info("###### users: {}\n", users.toString());
		
		//list to Iterator
		Iterator<Map<String,Object>> itUsers = users.iterator();
		while(itUsers.hasNext()) {
			logger.info("###### itListUsers: {}", itUsers.next());
		}
		
		//Set
		Set<Map<String, Object>> roles = new HashSet<>();
		roles.add(new HashMap<>(Map.of("role", "admin")));
		roles.add(new HashMap<>(Map.of("role", "manager")));
		roles.add(new HashMap<>(Map.of("role", "manager")));
		logger.info("##### rolesSet: {}\n", roles.toString());		
		roles.forEach(role -> logger.info("### setForeach: {}", role));
		//Check values of map contained
//		Boolean b = roles.contains(new HashMap<>() {
//			private static final long serialVersionUID = -4631120113875214548L;
//			{ put("role", "manager"); }
//		});
		Boolean b = roles.contains(new HashMap<>(Map.of("role", "manager")));
		logger.info("###### Is contained values in the set: {}\n ", b);
		
		//Set to Iterator
		Iterator<Map<String, Object>> itRoles = roles.iterator();
		while(itRoles.hasNext()) {
			logger.info("#### itSetRoles: {}", itRoles.next());
		}
		
	}

}
