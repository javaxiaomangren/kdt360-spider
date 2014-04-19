package com.amap.task.taskMgr.impl;

import com.amap.task.beans.Task;
import com.amap.task.taskMgr.Persistence;
import com.amap.task.utils.DBUtils;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by yang.hua on 14-4-1.
 */
public class PersistenceMysql implements Persistence {
    //    private JdbcTemplate template = DBUtils.getMongoDb().jdbcTemplate();
    private NamedParameterJdbcTemplate namedTemplate = DBUtils.getInstance().namedParameterJdbcTemplate();

    @Override
    public Task getTask(String id) {
        return null;
    }

    public List<Task> getTasks(int limit) {
        String sql = "SELECT id, lat, lng FROM location where status=0 limit :LIMITS";
        List<Task> result = namedTemplate.query(sql, ImmutableMap.of("LIMITS", limit), new RowMapper<Task>() {
            @Override
            public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
                Task t = new Task();
                t.setId(rs.getString("id"));
                t.setTaskPart(ImmutableMap.<String, Object>of("lat", rs.getString("lat"), "lng", rs.getString("lng")));
                return t;
            }
        });
        return result;
    }

    public void assignTask(Task t) {
        String update = "update location set status=:status where id=:id";
        namedTemplate.update(update, ImmutableMap.of("status", t.getStatus().value, "id", t.getId()));
    }

    public void save(List<Map<String, Object>> params, final Task task) {
        String insert = "INSERT INTO " +
                "kdt360(courier_code,company_code, company_name, courier_name," +
                " orderflag, _type, tel, sitecode, sitename, " +
                "sendarea, att, distance, img, avestart," +
                " aveleave, lat, lng) " +
                "VALUES (:COURIERCODE,:COMPANYCODE,:COMPANYNAME,:COURIERNAME," +
                ":ORDERFLAG,:TYPE,:TELEPHONE,:SITECODE,:SITENAME," +
                ":SENDAREA,:ATT,:DISTANCE,:IMG,:AVESTART," +
                ":AVELEAVE,:LAT, :LNG) " +
                "ON DUPLICATE KEY UPDATE update_time=NOW()";
        Collection<Map<String, Object>> c = Collections2.transform(params,
                new Function<Map<String, Object>, Map<String, Object>>() {
                    @Override
                    public Map<String, Object> apply(Map<String, Object> input) {
                        input.put("LAT", task.getTaskPart().get("lat"));
                        input.put("LNG", task.getTaskPart().get("lng"));
                        return input;
                    }
                }) ;
        namedTemplate.batchUpdate(insert,c.toArray(new Map[c.size()]));
    }
}
