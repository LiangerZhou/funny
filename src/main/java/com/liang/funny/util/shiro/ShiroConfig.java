package com.liang.funny.util.shiro;

import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {


    /**
     * 注入自定义的realm，告诉shiro如何获取用户信息来做登录或权限控制
     */
    @Bean
    public MyRealm realm() {
        return new MyRealm();
    }

    /**
     * 定义拦截的规则
     *
     * 这里统一做鉴权，即判断哪些请求路径需要用户登录，哪些请求路径不需要用户登录。
     * 这里只做鉴权，不做权限控制，因为权限用注解来做。
     *
     * @return
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chain = new DefaultShiroFilterChainDefinition();
        //哪些请求可以匿名访问
        //注意此处使用的是LinkedHashMap，是有顺序的，shiro会按从上到下的顺序匹配验证，匹配了就不再继续验证
        //所以上面的url要苛刻，宽松的url要放在下面，尤其是"/**"要放到最下面，如果放前面的话其后的验证规则就没作用了
        chain.addPathDefinition("/user/login", "anon");
        chain.addPathDefinition("/page/401", "anon");
        chain.addPathDefinition("/page/403", "anon");
        chain.addPathDefinition("/t5/hello", "anon");
        chain.addPathDefinition("/t5/guest", "anon");

        //除了以上的请求外，其它请求都需要登录
        chain.addPathDefinition("/**", "authc");
        return chain;
    }


    /*
    创建安全管理器对象,关联自定义Realm
     */
    @Bean
    public DefaultWebSecurityManager securityManager(MyRealm myRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myRealm);
        return securityManager;
    }
}