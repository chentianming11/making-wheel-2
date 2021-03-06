package com.chen.making.wheel.framework.spring.context;

import com.chen.making.wheel.framework.spring.aop.autoproxy.CustomAutoProxyCreator;
import com.chen.making.wheel.framework.spring.beans.DefinitionReader;
import com.chen.making.wheel.framework.spring.beans.factory.DefaultListableBeanFactory;
import strman.Strman;

import java.util.Properties;

/**
 * @author 陈添明
 * @date 2019/5/12
 */
public class ApplicationContext extends AbstractApplicationContext {

    /**
     * 配置文件路径
     */
    private String[] configLocations;

    private DefaultListableBeanFactory beanFactory;

    private DefinitionReader reader;


    public ApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        refresh();
    }

    /**
     * 为容器的某些子类指定特殊的BeanPost事件处理器
     *
     * @param beanFactory
     */
    @Override
    protected void postProcessBeanFactory(DefaultListableBeanFactory beanFactory) {

    }

    /**
     * 调用所有注册的BeanFactoryPostProcessor的Bean
     *
     * @param beanFactory
     */
    @Override
    protected void invokeBeanFactoryPostProcessors(DefaultListableBeanFactory beanFactory) {
        PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());
    }

    /**
     * 注册Bean后置处理器
     * @param beanFactory
     */
    @Override
    protected void registerBeanPostProcessors(DefaultListableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new CustomAutoProxyCreator(reader.getConfig()));
    }

    @Override
    protected DefaultListableBeanFactory obtainFreshBeanFactory() {
        beanFactory = new DefaultListableBeanFactory();
        loadBeanDefinitions(beanFactory);
        return beanFactory;
    }

    @Override
    public int getBeanDefinitionCount() {
        return beanFactory.getBeanDefinitionCount();
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanFactory.getBeanDefinitionNames();
    }

    /**
     * 载入Bean定义
     *
     * @param beanFactory
     */
    public void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        // 创建Bean定义reader
        reader = new DefinitionReader(beanFactory);
        for (String configLocation : configLocations) {
            reader.loadBeanDefinitions(configLocation);
        }
    }

    public Properties getConfig() {
        return reader.getConfig();
    }


    /**
     * 根据bean的名字，获取在IOC容器中得到bean实例
     *
     * @param name
     * @return
     */
    @Override
    public Object getBean(String name) {
       return beanFactory.getBean(name);
    }

    @Override
    public <T> T getBean(Class<T> clz) {
        String simpleName = clz.getSimpleName();
        Object bean = getBean(Strman.lowerFirst(simpleName));
        return (T) bean;
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons) {
        return beanFactory.getBeanNamesForType(type, includeNonSingletons);
    }


}
