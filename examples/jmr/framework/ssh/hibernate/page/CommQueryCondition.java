
package ssh.hibernate.page;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.Query;

/**
 * 分页参数
 *
 */
public class CommQueryCondition implements QueryCondition  {
	protected Map conditions = new HashMap();
	protected String countHql = null;
	protected String prepareHql = null;
	
	public String getBaseHql() {
		return null;
	}

	public void setBaseHql(String baseSql) {
		
	}

	public String getCountHql() {
		return this.countHql;
	}

	public void setCountHql(String countHql) {
		this.countHql = countHql;
	}

	public String getPrepareHql() {
		return this.prepareHql;
	}

	public void setPrepareHql(String prepareSql) {
		this.prepareHql = prepareSql;
	}

	public void setParameters(Map parameters) {
	}

	public Map getConditions() {
		return conditions;
	}

	public void setConditions(Map conditions) {
		if (conditions != null) {
			this.conditions = conditions;
		}
	}

	public void launchParamValues(Query query) {
		for (Iterator it = conditions.keySet().iterator(); it.hasNext(); ) {
			String name = (String)it.next();
			Object value = conditions.get(name);
			if (value instanceof String) {
				query.setString(name , (String)value);
			} else if (value instanceof Integer) {
				query.setInteger(name , ( (Integer)value ).intValue());	
			} else if (value instanceof Long) {
				query.setLong(name, ( (Long)value ).longValue());
			} else if (value instanceof Float) {
				query.setFloat(name, ( (Float)value ).floatValue());
			} else if (value instanceof Double) {
				query.setDouble(name, ( (Double)value ).doubleValue());
			} else if (value instanceof Timestamp) {
				query.setTimestamp(name, (Timestamp)value);
			} else if (value instanceof Date) {
				query.setDate(name, (Date)value);
			} else if (value instanceof Collection) {
				query.setParameterList(name, (Collection)value);
			} else if (value instanceof Object[]) {
				query.setParameterList(name, (Object[])value);
			} else {
				query.setParameter(name, value);
			}
		}
	}

}
