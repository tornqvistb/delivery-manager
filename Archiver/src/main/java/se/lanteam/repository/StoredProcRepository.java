package se.lanteam.repository;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

@Repository
public class StoredProcRepository {
	private SimpleJdbcCall simpleJdbcCall;
    @Autowired
    public void setDataSource(DataSource dataSource) {
       this.simpleJdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("copy_to_archive_db");
    }
    public String doArchiving(){
		Map<String,Object> out = simpleJdbcCall.execute();
		String result = (String)out.get("result");
		return result;
    }
}
