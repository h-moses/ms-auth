package com.ms.system.utils;

import com.ms.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

public class MenuHelper {

    public static List<SysMenu> buildTree(List<SysMenu> list) {
        List<SysMenu> tree = new ArrayList<>();
        for (SysMenu sysMenu:
             list) {
            if (sysMenu.getParentId().longValue() == 0) {
                tree.add(findChildren(sysMenu, list));
            }
        }
        return tree;
    }

    private static SysMenu findChildren(SysMenu sysMenu, List<SysMenu> list) {
        sysMenu.setChildren(new ArrayList<SysMenu>());
        for (SysMenu item: list) {
            if (Long.parseLong(sysMenu.getId()) == item.getParentId()) {
                if (sysMenu.getChildren() == null) {
                    sysMenu.setChildren(new ArrayList<>());
                }
                sysMenu.getChildren().add(findChildren(item, list));
            }
        }
        return sysMenu;
    }
}
