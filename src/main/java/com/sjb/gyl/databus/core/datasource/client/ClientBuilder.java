package com.sjb.gyl.databus.core.datasource.client;

import com.sjb.gyl.databus.core.datasource.cfg.DataSourceCfg;

public interface ClientBuilder {
    Object build(DataSourceCfg<?> ds);
}
