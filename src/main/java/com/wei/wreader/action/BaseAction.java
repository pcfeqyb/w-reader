package com.wei.wreader.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.wei.wreader.pojo.Settings;
import com.wei.wreader.service.CacheService;
import com.wei.wreader.utils.ConfigYaml;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public abstract class BaseAction extends AnAction {

    protected CacheService cacheService;
    protected ConfigYaml configYaml;
    protected Settings settings;
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        cacheService = CacheService.getInstance();
        configYaml = ConfigYaml.getInstance();
        settings = cacheService.getSettings();
        if (settings == null) {
            settings = configYaml.getSettings();
            cacheService.setSettings(settings);
        }

        if (StringUtils.isBlank(settings.getCharset())) {
            settings.setCharset(configYaml.getSettings().getCharset());
            cacheService.setSettings(settings);
        }
    }
}
