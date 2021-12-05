package com.sp.framework.orm.lark.orm.aop;

import java.util.Properties;

import com.sp.framework.orm.lark.common.dao.PageSort;
import com.sp.framework.orm.lark.common.dao.Sort;
import com.sp.framework.orm.lark.orm.dialect.Dialect;
import com.sp.framework.orm.lark.orm.util.PropertiesUtil;
import com.sp.framework.orm.lark.orm.util.ReflectionUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class }) })
public class OffsetLimitInterceptor implements Interceptor {
    static int MAPPED_STATEMENT_INDEX = 0;
    static int PARAMETER_INDEX = 1;
    static int ROWBOUNDS_INDEX = 2;
    static int RESULT_HANDLER_INDEX = 3;
    private static final Logger logger = LoggerFactory.getLogger(OffsetLimitInterceptor.class);
    private static final String FIELD_NAME_METAPARAMETERS = "metaParameters";
    private static final String FIELD_NAME_ADDITIONALPARAMETERS = "additionalParameters";

    Dialect dialect;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        processIntercept(invocation.getArgs());
        return invocation.proceed();
    }

    void processIntercept(final Object[] queryArgs) {
        //queryArgs = query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler)
        MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
        Object parameter = queryArgs[PARAMETER_INDEX];

        //特殊处理分页
        if(parameter instanceof PageSort){
            processHasPageParameter(queryArgs);
        }
        //常规分页
        else{
            processNormalPage(queryArgs);
        }
    }
    
    /**
     * 处理包含page对象的分页处理
     * @param queryArgs
     */
    void processHasPageParameter(final Object[] queryArgs){
        MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
        Object parameter = queryArgs[PARAMETER_INDEX];
        final RowBounds rowBounds = (RowBounds) queryArgs[ROWBOUNDS_INDEX];
        int offset = rowBounds.getOffset();
        int limit = rowBounds.getLimit();
        
        PageSort pageSort=(PageSort)parameter;
        
        //搜索条件
        BoundSql boundSql = ms.getBoundSql(pageSort.getSearchFilter());
        String sql = boundSql.getSql().trim();
        
        //排序
        if(pageSort.getSorts()!=null){
            sql=sql+" order by "+Sort.toSortStr(pageSort.getSorts());
        }
        
        //分页
        if (dialect.supportsLimit() && (offset != RowBounds.NO_ROW_OFFSET || limit != RowBounds.NO_ROW_LIMIT)) {
            if (dialect.supportsLimitOffset()) {
                sql = dialect.getLimitString(sql, offset, limit);
                offset = RowBounds.NO_ROW_OFFSET;
            } else {
                sql = dialect.getLimitString(sql, 0, limit);
            }
            limit = RowBounds.NO_ROW_LIMIT;
            queryArgs[ROWBOUNDS_INDEX] = new RowBounds(offset, limit);
        }

        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(),
                boundSql.getParameterObject());
        
        //设置了一些其他值
        ReflectionUtils.setFieldValue(newBoundSql, FIELD_NAME_METAPARAMETERS,
                ReflectionUtils.getFieldValue(boundSql, FIELD_NAME_METAPARAMETERS));
        
        ReflectionUtils.setFieldValue(newBoundSql, FIELD_NAME_ADDITIONALPARAMETERS,
            ReflectionUtils.getFieldValue(boundSql, FIELD_NAME_ADDITIONALPARAMETERS));
        
        MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
        queryArgs[MAPPED_STATEMENT_INDEX] = newMs;
        queryArgs[PARAMETER_INDEX]=pageSort.getSearchFilter();
    }
    
    /**
     * 处理包含没有page对象的分页处理
     * @param queryArgs
     */
    void processNormalPage(final Object[] queryArgs){
        MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
        Object parameter = queryArgs[PARAMETER_INDEX];
        final RowBounds rowBounds = (RowBounds) queryArgs[ROWBOUNDS_INDEX];
        int offset = rowBounds.getOffset();
        int limit = rowBounds.getLimit();
        //需要分页
        if (dialect.supportsLimit() && (offset != RowBounds.NO_ROW_OFFSET || limit != RowBounds.NO_ROW_LIMIT)) {    
            BoundSql boundSql = ms.getBoundSql(parameter);
            
            String sql = boundSql.getSql().trim();
            if (dialect.supportsLimitOffset()) {
                sql = dialect.getLimitString(sql, offset, limit);
                offset = RowBounds.NO_ROW_OFFSET;
            } else {
                sql = dialect.getLimitString(sql, 0, limit);
            }
            limit = RowBounds.NO_ROW_LIMIT;
    
            queryArgs[ROWBOUNDS_INDEX] = new RowBounds(offset, limit);
            BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(),
                    boundSql.getParameterObject());
            //设置了一些其他值
            ReflectionUtils.setFieldValue(newBoundSql, FIELD_NAME_METAPARAMETERS,
                    ReflectionUtils.getFieldValue(boundSql, FIELD_NAME_METAPARAMETERS));
            
            ReflectionUtils.setFieldValue(newBoundSql, FIELD_NAME_ADDITIONALPARAMETERS,
                ReflectionUtils.getFieldValue(boundSql, FIELD_NAME_ADDITIONALPARAMETERS));
            
            MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
            queryArgs[MAPPED_STATEMENT_INDEX] = newMs;
        }
    }

    //see: MapperBuilderAssistant
    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource,
                ms.getSqlCommandType());

        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        //builder.keyProperty(ms.getKeyProperty());     //because can't find ms.getKeyProperty() method
        builder.keyProperty(StringUtils.join(ms.getKeyProperties(), ","));
        
        //setStatementTimeout()
        builder.timeout(ms.getTimeout());

        //setStatementResultMap()
        builder.parameterMap(ms.getParameterMap());

        //setStatementResultMap()
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());

        //setStatementCache()
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
        String dialectClass = new PropertiesUtil(properties).getRequiredString("dialectClass");
        try {
            dialect = (Dialect) Class.forName(dialectClass).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("cannot create com.yh.common.lark.orm.Dialect instance by dialectClass:" + dialectClass, e);
        }
        logger.info(OffsetLimitInterceptor.class.getSimpleName() + ".com.yh.common.lark.orm.Dialect=" + dialectClass);
    }

    public static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }
}
