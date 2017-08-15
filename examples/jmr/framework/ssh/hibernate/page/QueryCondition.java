package ssh.hibernate.page;
import java.util.Map;

import org.hibernate.Query;


/**
 * 分页参数接口
 */
public interface QueryCondition {
   
    public String getBaseHql();
   
    public void setBaseHql(String baseSql);

   
    public String getCountHql();

    /**
     * 统计查询hql语句
     * @return
     */
    public void setCountHql(String CountHql);

    
    public String getPrepareHql();

    /**
     * 查询hql语句
     * @return
     */
    public void setPrepareHql(String prepareSql);
    
    /**
     * 设置参数
     * @param parameters
     */
    public void setParameters(Map parameters);
    
    /**
     * 封装参数
     * @param query
     */
    public void launchParamValues(Query query);
}