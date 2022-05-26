package com.sjb.gyl.databus.core.msg;

import com.sjb.gyl.databus.core.binlog.Binlog;
import lombok.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MsgWrapper implements Serializable {
    private static final long serialVersionUID = 1L;
    private Binlog binlog;
    private Retry retry;
    private Map<String, String> originalData;
    private Map<String, Object> mappedData;

    public static MsgWrapper warp(Binlog binlog) {
        return warp(binlog, 0, "", 0);
    }

    public static MsgWrapper warp(Binlog binlog, int retryCount, String retryCause, long retryTimestamp) {
        return new MsgWrapper(binlog, new Retry(retryCount, retryTimestamp, retryCause), new HashMap<>(32),new HashMap<>(32));
    }
}
