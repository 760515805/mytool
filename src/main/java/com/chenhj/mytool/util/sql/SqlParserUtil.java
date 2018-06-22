package com.chenhj.mytool.util.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLOrderingSpecification;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.stat.TableStat.Column;
import com.alibaba.druid.stat.TableStat.Condition;
import com.alibaba.druid.stat.TableStat.Name;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.util.JdbcUtils;
import com.chenhj.mytool.util.StringUtils;
public class SqlParserUtil {
	private  final static Logger logger = LoggerFactory.getLogger(SqlParserUtil.class);

	private static String ORDER_KEY="orderBy.type";
    public static void main(String[] args) {
    	 
//        String sql= ""
//                + "insert into tar select * from boss_table bo, ("
//                    + "select a.f1, ff from emp_table a "
//                    + "inner join log_table b "
//                    + "on a.f2 = b.f3"
//                    + ") f "
//                    + "where bo.f4 = f.f5 "
//                    + "group by bo.f6 , f.f7 having count(bo.f8) > 0 "
//                    + "order by bo.f9, f.f10;"
//                    + "select func(f) from test1; "
//                    + "";
    	String sql = "select * from bb where aa>0 and bb<1 and cc='123' or dd!=1  group by aa,bb ORDER BY aa desc";
    	
    	 System.out.println(aa); 
 
    }
    /*********表名**********/
    private static String tableName;
    /*********排序**********/
    private static Map<String,String> orderByMap = null;
    /*********聚合**********/
    private static List<String> groupByList = null;
    /*********查询字段**********/
    private static List<String> columnList =null;
    /*********条件**********/
    private static List<Condition> conditions = null;
    /*********查询方法**********/
    private static String sqlMethod;
    /**
     * 格式化SQL语句,只支持Mysql
     * @param sql
     */
    public static void  format(String sql){
    	orderByMap = new HashMap<String,String>();
    	groupByList = new ArrayList<String>();
    	columnList = new ArrayList<String>();
    	conditions = new ArrayList<Condition>();
    	String dbType = JdbcConstants.MYSQL;
        //格式化输出
        String result = SQLUtils.format(sql, dbType);
        logger.info("缺省大写格式:"+result); // 缺省大写格式
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        //解析出的独立语句的个数
        logger.info("解析出的独立语句的个数:" + stmtList.size());
        for (int i = 0; i < stmtList.size(); i++) {
            SQLStatement stmt = stmtList.get(i);
            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
            stmt.accept(visitor);
                        
           System.out.println( stmt.toString());
            List<Column> order =visitor.getOrderByColumns();
            for(Column column:order) {
            	SQLOrderingSpecification order1 = (SQLOrderingSpecification) column.getAttributes().get(ORDER_KEY);
            	String orderOperator ="ASC"; 
            	try {
            		orderOperator = order1.toString();
				} catch (Exception e) {
					orderOperator ="ASC";
				}
            	orderByMap.put(column.getName(),orderOperator);
            }
            Set<Column> groupby_col = visitor.getGroupByColumns();
            Iterator<Column> iterator = groupby_col.iterator();
            //
            while (iterator.hasNext()) {
                Column column = (Column) iterator.next();
                groupByList.add(column.getName());
                logger.info("[GROUP]" + column.toString());
            }
            //获取表名称
            Map<Name, TableStat> tabmap = visitor.getTables();
            Iterator<Name> iterator1 = tabmap.keySet().iterator();
            while ( iterator1.hasNext()) {
                Name name = (Name) iterator1.next();
                tableName = name.toString();
                sqlMethod = tabmap.get(name).toString();
                logger.info("table names:"+name.toString() + " - " + tabmap.get(name).toString());
            }
            List<Condition> conditions = visitor.getConditions();

            for(Condition c:conditions) {
            	logger.info(c.toString());
            	logger.info(c.getColumn().getName()+"___"+c.getValues()+"___"+c.getOperator());
            }
            //System.out.println("Tables : " + visitor.getCurrentTable());
            //获取操作方法名称,依赖于表名称
            logger.info("Manipulation : " + visitor.getTables());
            //获取字段名称
            logger.info("fields : " + visitor.getColumns());
               
        }
    }
    public static String[] getWhere(String sql){
    	StringBuffer where = new StringBuffer();   
    	  // parser得到AST   
    	List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, JdbcUtils.MYSQL);
    	 // 将AST通过visitor输出   
    	 SQLASTOutputVisitor whereVisitor = SQLUtils.createFormatOutputVisitor(where, stmtList, JdbcUtils.MYSQL);   
    	 for (SQLStatement stmt : stmtList) {   
	    	 if(stmt instanceof SQLSelectStatement){   
	    		 SQLSelectStatement sstmt = (SQLSelectStatement)stmt;   
	    		 SQLSelect sqlselect = sstmt.getSelect();   
	    		 SQLSelectQueryBlock query = (SQLSelectQueryBlock)sqlselect.getQuery();  
	    		 query.getWhere().accept(whereVisitor);   
	    	 }else if(stmt instanceof SQLInsertStatement){
	    		 SQLInsertStatement istmt = (SQLInsertStatement)stmt;
	    		 System.out.println(istmt.getColumns());
	    	 }else if(stmt instanceof SQLUpdateStatement){
	    		 SQLUpdateStatement ustmt = (SQLUpdateStatement) stmt;
	    		 ustmt.getWhere().accept(whereVisitor);
	    	 }
    	 }   
    	if(StringUtils.isNotEmpty(where.toString())){
    		String aa[] = where.toString().split("\n");
    		return aa;
    	}
    	return null;
    }
	public static String getTableName() {
		return tableName;
	}

	public static Map<String, String> getOrderByMap() {
		return orderByMap;
	}

	public static List<String> getGroupByList() {
		return groupByList;
	}

	public static List<String> getColumnList() {
		return columnList;
	}

	public static List<Condition> getConditions() {
		return conditions;
	}

	public static String getSqlMethod() {
		return sqlMethod;
	}
    
}
