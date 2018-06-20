package com.chenhj.mytool.util.sql;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat.*;
import com.alibaba.druid.stat.*;
import com.alibaba.druid.util.JdbcConstants;
public class SqlParserUtil {
	
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
    	String sql = "select aa,cc from bb where aa>0 and bb<1 and cc='123' or dd!=1  group by aa,bb ORDER BY date asc,aa desc";
        String dbType = JdbcConstants.MYSQL;
 
        //格式化输出
        String result = SQLUtils.format(sql, dbType);
        System.out.println(result); // 缺省大写格式
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
 
        //解析出的独立语句的个数
        System.out.println("size is:" + stmtList.size());
        for (int i = 0; i < stmtList.size(); i++) {
 
            SQLStatement stmt = stmtList.get(i);
            
            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
            
            stmt.accept(visitor);
            List<Column> order =visitor.getOrderByColumns();
            for(Column column:order) {
            	System.out.println(column.getName()+"__________"+column.getAttributes().get(ORDER_KEY));
            }
            //聚合的数量
           // Map<String, String> aliasmap = visitor.getTables();
//            for (Iterator iterator = aliasmap.keySet().iterator(); iterator.hasNext();) {
//                String key = iterator.next().toString();
//                System.out.println("[ALIAS]" + key + " - " + aliasmap.get(key));
//            }
            Set<Column> groupby_col = visitor.getGroupByColumns();
            Iterator<Column> iterator = groupby_col.iterator();
            //
            while (iterator.hasNext()) {
                Column column = (Column) iterator.next();
                System.out.println("[GROUP]" + column.toString());
            }
            //获取表名称
            System.out.println("table names:");
            Map<Name, TableStat> tabmap = visitor.getTables();
            Iterator<Name> iterator1 = tabmap.keySet().iterator();
            while ( iterator1.hasNext()) {
                Name name = (Name) iterator1.next();
                System.out.println(name.toString() + " - " + tabmap.get(name).toString());
            }
            List<Condition> conditions = visitor.getConditions();
            for(Condition c:conditions) {
            	System.out.println(c.getColumn()+"___"+c.getValues()+"___"+c.getOperator());
            }
            //System.out.println("Tables : " + visitor.getCurrentTable());
            //获取操作方法名称,依赖于表名称
            System.out.println("Manipulation : " + visitor.getTables());
            //获取字段名称
            System.out.println("fields : " + visitor.getColumns());
        }
 
    }
   // public void
}
