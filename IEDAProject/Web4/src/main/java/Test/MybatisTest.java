package Test;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.io.InputStream;
import java.util.List;

public class MybatisTest {
    public static SqlSessionFactory sqlSessionFactory;
    public static void main(String[] args) throws Exception{

        //inputStream要及时释放以及防止IO读取过程出错而不能即使释放

        setSqlSessionFactory();
        try(SqlSession sqlSession = sqlSessionFactory.openSession())
        {
            User one = new User();
            one.setAge(30);
            one.setId(20);
            one.setClass_id(3);
            one.setHobbies("playgame");
            one.setName("bob");

            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            userMapper.insertuser(one);
            List<User> users = userMapper.selectusers();
            for (User i : users){
                System.out.println("---------------");
                System.out.println("User:"+i);
                System.out.println("age:"+i.getAge());
                System.out.println("class_id:"+i.getClass_id());
                System.out.println("hobbies:"+i.getHobbies());
                System.out.println("id:"+i.getId());
                System.out.println("name:"+i.getName());
                System.out.println("---------------");
            }
            //增删改查必须提交事务
            sqlSession.commit();

        }

    }
    /**
     * 关于SqlSessionFactoryBuilder,
     * 这个类可以被实例化、使用和丢弃，一旦创建了 SqlSessionFactory，
     *就不再需要它了。 因此 SqlSessionFactoryBuilder 实例的最佳作用域是方法作用域
     * （也就是局部方法变量）。 你可以重用 SqlSessionFactoryBuilder 来创建多个 SqlSessionFactory 实例，
     *但最好还是不要一直保留着它，以保证所有的 XML 解析资源可以被释放给更重要的事情。*/
    public static void setSqlSessionFactory() throws Exception{
        String resource = "mybatis-config.xml";
        try(InputStream inputStream = Resources.getResourceAsStream(resource)){
            //以下语句临时创建SqlsessionFactoryBuilder,然后调用build方法创建sqlsessionfactory,
            //创建完后立刻舍弃掉SqlsessionFactoryBuilder
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        }
    }
}
