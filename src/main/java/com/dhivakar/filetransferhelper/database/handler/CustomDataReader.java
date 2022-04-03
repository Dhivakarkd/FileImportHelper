package com.dhivakar.filetransferhelper.database.handler;

import com.dhivakar.filetransferhelper.database.model.BatchInfo;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CustomDataReader extends JdbcCursorItemReader<BatchInfo> implements ItemReader<BatchInfo> {

    public CustomDataReader(DataSource dataSource) {
        setDataSource(dataSource);
        setSql("SELECT * FROM BATCH_INFO WHERE BATCH_STATUS = 'PENDING' order by BATCH_ID desc limit 1");
        setFetchSize(2);
        setRowMapper(new BatchInfoRowMapper());
    }

    public class BatchInfoRowMapper implements RowMapper<BatchInfo> {

        @Override
        public BatchInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            BatchInfo info = new BatchInfo();
            info.setBatchId(rs.getInt("BATCH_ID"));
            info.setImportDate(rs.getDate("IMPORT_DATE").toLocalDate());
            info.setBatchStatus(rs.getString("BATCH_STATUS"));
            info.setRecordUpdatedTimeStamp(rs.getTimestamp("RECORD_UPDATED_TS").toLocalDateTime());

            return info;

        }
    }


}
