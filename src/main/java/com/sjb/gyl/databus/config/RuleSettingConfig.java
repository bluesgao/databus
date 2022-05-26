package com.sjb.gyl.databus.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.sjb.gyl.databus.common.Constants;
import com.sjb.gyl.databus.core.rule.RuleCfgHolder;
import com.sjb.gyl.databus.core.rule.entity.RuleCfg;
import com.sjb.gyl.databus.util.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;

/**
 * 加载规则配置文件
 * 在DataSource之后加载，验证规则中是否有未配置的DataSource
 * @author bei.wu
 */
@Slf4j
@Configuration
@AutoConfigureAfter(DataSourceSettingConfig.class)
public class RuleSettingConfig {

    @Value("${gyl.databus.setting.rule}")
    protected String rulePath;

    @PostConstruct
    public void init() {
        log.info("*** 开始-加载规则配置文件 ***");
        List<String> ruleList = FileUtil.listFileNames(rulePath);
        if (CollectionUtils.isEmpty(ruleList)) {
            log.warn("{}, 未发现配置文件", rulePath);
        }
        ruleList.forEach(rule -> {
            if (rule.endsWith(Constants.SETTING_SUFFIX)) {
                File file = FileUtil.file(this.rulePath, rule);
                log.info("配置文件路径：{}", file.toString());
                String content = FileUtil.readUtf8String(file);
                if (StrUtil.isNotBlank(content)) {
                    RuleCfg ruleCfg = JSON.parseObject(content, RuleCfg.class);
                    log.info("{}", ruleCfg.toString());
                    // 验证配置是否合法
                    ValidationUtils.isValid(ruleCfg);
                    RuleCfgHolder.add(ruleCfg);
                }
            }
        });
    }
}
