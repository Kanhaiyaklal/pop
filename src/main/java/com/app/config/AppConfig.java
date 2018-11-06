package com.app.config;

import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.app.model.UOM;

@EnableWebMvc
@EnableTransactionManagement
@Configuration
@PropertySource("classpath:App.properties")
@ComponentScan(basePackages="com.app")
public class AppConfig {
	 @Autowired
     Environment evn;	
     @Bean
     public BasicDataSource dataSource()
     {
   	  BasicDataSource bs=new BasicDataSource();
   	  bs.setDriverClassName(evn.getProperty("driver"));
   	  bs.setUrl(evn.getProperty("url"));
   	  bs.setUsername(evn.getProperty("un"));
   	  bs.setPassword(evn.getProperty("pwd"));
   	  bs.setInitialSize(5);
   	  bs.setMaxTotal(5);
   	  bs.setMaxIdle(4);
   	  bs.setMinIdle(3);
   	  return bs;
     }
      @Bean
     public LocalSessionFactoryBean localBean()
     {
   	  LocalSessionFactoryBean lb=new LocalSessionFactoryBean();
   	  lb.setDataSource(dataSource()); 
   	  lb.setAnnotatedClasses(UOM.class);
   	  lb.setHibernateProperties(props());
   	  return  lb;
     }
     @Bean
     public Properties props() {
       Properties p=new Properties();
       p.put("hibernate.dailect", evn.getProperty("dialect"));
       p.put("hibernate.show_sql",evn.getProperty("showsql"));
       p.put("hibernate.format_sql", evn.getProperty("formatsql"));
       p.put("hibernate.hbm2ddl.auto", evn.getProperty("dbh"));  
		return p;
	}
   @Bean
	public HibernateTemplate temp()
     {
   	  HibernateTemplate t=new HibernateTemplate();
   	  t.setSessionFactory(localBean().getObject());
   	  return  t;
     }
     @Bean
     public HibernateTransactionManager manager()
     {
   	  HibernateTransactionManager m=new HibernateTransactionManager();
   	  m.setSessionFactory(localBean().getObject());
   	  return m;
     }
     
@Bean
public InternalResourceViewResolver res()
{
	InternalResourceViewResolver v=new InternalResourceViewResolver();
	v.setPrefix(evn.getProperty("mvc.prefix"));
	v.setSuffix(evn.getProperty("mvc.suffix"));
	
	return v;
}
}
