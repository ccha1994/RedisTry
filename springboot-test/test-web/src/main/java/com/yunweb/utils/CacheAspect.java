package com.yunweb.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * @author wangyunlong
 * @date 2018/6/29 11:36
 */
@Component
@Aspect
public class CacheAspect {
    /**
     * 缓存实体.
     */
    @Resource
    private Cache cache;

    /**
     * MyCacheable注解，用于缓存获取和缓存写入.
     *
     * @param joinPoint
     * @param cacheable
     * @return
     * @throws Throwable
     */
    @Around("execution(@MyCacheAble * *.*(..)) && @annotation(cacheable)")
    public Object aroundCacheable(ProceedingJoinPoint joinPoint, MyCacheAble cacheable) throws Throwable {
        String key; //缓存key
        if (cacheable.key().equals("")) {
            //根据方法签名生成key
            key = generateKey(joinPoint);
        } else {
            //使用注解中的key, 支持SpEL表达式
            String spEL = cacheable.key();
            key = generateKeyBySpEL(spEL, joinPoint);
        }
        //尝试从缓存中获取
        Object result = cache.get(key);
        if (null != result) {
            //缓存中存在即直接返回该值
            System.out.println("cache hit! key = " + key);
            return result;
        }
        //缓存中不存在则执行该方法
        System.out.println("cache miss! put into cache...");
        result = joinPoint.proceed();
        //将通过执行方法获取到的值放入缓存并返回结果
        cache.put(key, result);
        return result;
    }

    /**
     * MyCacheEvict注解，用于缓存清除.
     *
     * @param joinPoint
     * @param cacheEvict
     * @return
     * @throws Throwable
     */
    @Around("execution(@MyCacheEvict * *.*(..)) && @annotation(cacheEvict)")
    public Object aroundCacheEvict(ProceedingJoinPoint joinPoint, MyCacheEvict cacheEvict) throws Throwable {
        //首先执行方法
        Object result = joinPoint.proceed();
        //删除对应缓存
        String key; //缓存key
        if (cacheEvict.key().equals("")) {
            //默认根据方法签名生成key
            key = generateKey(joinPoint);
        } else {
            //使用注解中的key, 支持SpEL表达式
            String spEL = cacheEvict.key();
            key = generateKeyBySpEL(spEL, joinPoint);
        }
        System.out.println("evict cache! remove key " + key);
        cache.evict(key);
        //返回结果
        return result;
    }


    /**
     * 默认缓存key生成器.
     * 注解中key不传参，根据方法签名和参数生成key.
     *
     * @param joinPoint
     * @return
     */
    private String generateKey(ProceedingJoinPoint joinPoint) {
        Class itsClass = joinPoint.getTarget().getClass();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(itsClass.getName());
        keyBuilder.append(".").append(methodSignature.getName());
        keyBuilder.append("(");
        for(Object arg : joinPoint.getArgs()) {
            keyBuilder.append(arg.getClass().getSimpleName() + arg + ";");
        }
        keyBuilder.append(")");
        return keyBuilder.toString();
    }

    /**
     * 用于SpEL表达式解析.
     */
    private SpelExpressionParser parser = new SpelExpressionParser();
    /**
     * 用于获取方法参数定义名字.
     */
    private DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    /**
     * SpEL表达式缓存Key生成器.
     * 注解中传入key参数，则使用此生成器生成缓存.
     *
     * @param spELString
     * @param joinPoint
     * @return
     */
    private String generateKeyBySpEL(String spELString, ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = nameDiscoverer.getParameterNames(methodSignature.getMethod());
        Expression expression = parser.parseExpression(spELString);
        EvaluationContext context = new StandardEvaluationContext();
        Object[] args = joinPoint.getArgs();
        for(int i = 0 ; i < args.length ; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        return expression.getValue(context).toString();
    }

}
