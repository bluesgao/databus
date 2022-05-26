package com.sjb.gyl.databus.core.rule;

import com.sjb.gyl.databus.core.rule.entity.Rule;
import com.sjb.gyl.databus.core.rule.entity.RuleCfg;
import com.sjb.gyl.databus.core.rule.entity.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author bei.wu
 */
public class RuleCfgHolder {

    private static final List<RuleCfg> HOLDER = Collections.synchronizedList(new ArrayList<>(10));

    private RuleCfgHolder(){}

    public static void add(RuleCfg ruleCfg){
        HOLDER.add(ruleCfg);
    }

    public static Table getTable(String database, String name) {
        for (RuleCfg ruleCfg: HOLDER) {
            for (Table table: ruleCfg.getTable()) {
                if (table.getDatabase().equals(database) && table.getName().equals(name)) {
                    return table;
                }
            }
        }
        //TODO 抛出没有table规则异常
        return null;
    }

    public static Rule getTableRule(Table table) {
        for (RuleCfg ruleCfg: HOLDER) {
            for (Rule rule: ruleCfg.getRule()) {
                if (table.getRuleId().equals(rule.getId())) {
                    return rule;
                }
            }
        }
        return null;
    }
}
