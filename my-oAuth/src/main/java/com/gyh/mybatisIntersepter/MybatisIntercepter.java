package com.gyh.mybatisIntersepter;


import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedDeque;

//@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class })
//@Intercepts({@Signature(
//        type = Executor.class,
//        method = "update",
//        args = {MappedStatement.class, Object.class})})
@Intercepts(value = {
        @Signature(type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class,
                        CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class MybatisIntercepter implements Interceptor {
    private Logger logger = Logger.getLogger(MybatisIntercepter.class.getSimpleName());

    @SuppressWarnings("unused")
    private Properties properties;

    private ConcurrentLinkedDeque<String> list = new ConcurrentLinkedDeque<String>();


    public MybatisIntercepter() {

    }

    public Object intercept(Invocation invocation) throws Throwable {


//        Object[] args = invocation.getArgs();
//        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
//        Object parameter = null;
//        if (invocation.getArgs().length > 1) {
//            parameter = invocation.getArgs()[1];
//        }
//        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
//
//        String sql=boundSql.getSql();
//        MetaObject metaObject= SystemMetaObject.forObject(mappedStatement);
//
//        if(!sql.contains("mycat")) {
//            metaObject.setValue("sqlSource.sqlSource.sql", MyThreadLocal.getThreadLocal().get() + sql);
//        }else {
//            String[] s=sql.split("/*/");
//            sql=MyThreadLocal.getThreadLocal().get()+s[2].toString();
//            metaObject.setValue("sqlSource.sqlSource.sql", sql);
//        }
//        MyThreadLocal.getThreadLocal().remove();
        Object returnValue = invocation.proceed();
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }
        BoundSql boundSql = mappedStatement.getSqlSource().getBoundSql(parameter);

        String sql=boundSql.getSql();
        MetaObject metaObject= SystemMetaObject.forObject(mappedStatement);

        String tendId = MyThreadLocal.getThreadLocal().get().toString();
        String mycatSchema = "/*!mycat:dataNode=" + tendId + "*/ ";

//		metaObject.setValue("sqlSource.sqlSource.sql",mycatSchema+sql);
        SqlSource sqlSource=mappedStatement.getSqlSource();

        if(sqlSource instanceof DynamicSqlSource){
            SqlNode sqlNode = (SqlNode) metaObject
                    .getValue("sqlSource.rootSqlNode");
            if (!sql.contains("mycat")) {
                boundSql=getBoundSql(mappedStatement.getConfiguration(),boundSql.getParameterObject(),sqlNode);
                logger.info(boundSql.getSql());
            } else {
                String[] s = sql.split("/*/");
                sql = mycatSchema + s[2].toString();
                boundSql=getBoundSql(mappedStatement.getConfiguration(),boundSql.getParameterObject(),sqlNode);
            }
        }else if(sqlSource instanceof RawSqlSource){
            if (!sql.contains("mycat")) {
                metaObject.setValue("sqlSource.sqlSource.sql", mycatSchema + sql);
            } else {
                String[] s = sql.split("/*/");
                sql = mycatSchema + s[2].toString();
                metaObject.setValue("sqlSource.sqlSource.sql", sql);
            }
        }else if(sqlSource instanceof StaticSqlSource){
            if (!sql.contains("mycat")) {
                metaObject.setValue("sqlSource.sqlSource.sql", mycatSchema + sql);
            } else {
                String[] s = sql.split("/*/");
                sql = mycatSchema + s[2].toString();
                metaObject.setValue("sqlSource.sqlSource.sql", sql);
            }
        }
        return invocation.proceed();
    }
    public static BoundSql getBoundSql(Configuration configuration, Object parameterObject, SqlNode sqlNode) {
        DynamicContext context = new DynamicContext(configuration, parameterObject);
        //DynamicContext context = new DynamicContext(mappedStatement.getConfiguration(), boundSql.getParameterObject());
        //mappedStatement.getSqlSource().

        sqlNode.apply(context);
        String countextSql=context.getSql();
//		System.out.println("context.getSql():"+countextSql);


        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
        Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
        String tendId = MyThreadLocal.getThreadLocal().get().toString();
        String mycatSchema = "/*!mycat:dataNode=" + tendId + "*/ ";
        String sql=mycatSchema+countextSql;
        SqlSource sqlSource = sqlSourceParser.parse(sql, parameterType, context.getBindings());


        BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
        for (Map.Entry<String, Object> entry : context.getBindings().entrySet()) {
            boundSql.setAdditionalParameter(entry.getKey(), entry.getValue());
        }

        return boundSql;
    }
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties0) {
        this.properties = properties0;
    }
}