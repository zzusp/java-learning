import java.util.ArrayList;
import java.util.List;

public class Test {

	public static void main(String[] args) {
		Test test = new Test();
		for (User user : test.test1()) {
			System.out.println("test1 --> " + user.toString());
		}
		for (User user : test.test2()) {
			System.out.println("test2 --> " + user.toString());
		}
	}

	public List<User> test1() {
		List<User> users = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			User user = new User();
			user.setName("name" + i);
			user.setAge(10 + i);
			users.add(user);
		}
		return users;
	}

	public List<User> test2() {
		List<User> users = new ArrayList<>();
		User user;
		for (int i = 0; i < 10; i++) {
			user = new User();
			user.setName("name" + i);
			user.setAge(10 + i);
			users.add(user);
		}
		return users;
	}

	class User {
		private String name;
		private Integer age;

		public User() {
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

		public User(String name, Integer age) {
			this.name = name;
			this.age = age;
		}

		@Override
		public String toString() {
			return "User{" +
					"name='" + name + '\'' +
					", age=" + age +
					'}';
		}
	}

}
