package com.wei.wreader.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.wei.wreader.utils.OperateActionUtil;

public class SearchBookNameAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        OperateActionUtil operateAction = OperateActionUtil.getInstance();
        operateAction.buildSearchBookDialog();
    }


}
