package com.wdcloud.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * treeId生成
 *
 * @author csf
 */
public class TreeIdUtils {

    /**
     * 生成 TreeId(传入的treeId是兄弟节点最大的treeId)
     *
     * @param treeId    传入兄弟节点最大的treeId
     * @param treeIdNum tree递增位数
     * @return 返回新treeId
     */
    public static String produceTreeId(String treeId, int treeIdNum) {
        assert StringUtil.isNotEmpty(treeId);
        assert treeIdNum > 0;
        String parentTreeId = treeId.substring(0, treeId.length() - treeIdNum);
        String subTreeId = treeId.substring(treeId.length() - treeIdNum);
        String temp = (Integer.parseInt(subTreeId) + 1) + "";
        StringBuilder newTreeId = new StringBuilder();
        for (int index = temp.length(); index < treeIdNum; index++) {
            newTreeId.append(0);
        }
        return parentTreeId + newTreeId + temp;
    }

    /**
     * 生成 TreeId
     *
     * @param parentTreeId 传入父TreeId
     * @param treeIdNum    tree递增位数
     * @return 返回新treeId
     */
    public static String produceTreeIdByParentId(String parentTreeId, int treeIdNum) {
        assert StringUtil.isNotEmpty(parentTreeId);
        assert treeIdNum > 0;
        StringBuilder newTreeId = new StringBuilder();
        for (int index = 0; index < (treeIdNum - 1); index++) {
            newTreeId.append(0);
        }
        newTreeId.append(1);
        return parentTreeId + newTreeId;
    }

    /**
     * 生成 TreeId
     *
     * @param parentTreeId 传入父TreeId
     * @param treeIdNum    tree递增位数
     * @param count        生成个数
     * @return 返回新treeId
     */
    public static List<String> produceTreeIdByParentId(String parentTreeId, int treeIdNum, int count) {
        assert StringUtil.isNotEmpty(parentTreeId);
        assert treeIdNum > 0;

        List<String> treeIdList = new ArrayList<>();
        if (count < 1) {
            return treeIdList;
        }
        StringBuilder newTreeId = new StringBuilder();
        for (int index = 0; index < (treeIdNum - 1); index++) {
            newTreeId.append(0);
        }
        String treeId = parentTreeId + newTreeId + 1;
        treeIdList.add(treeId);
        for (int index = 1; index < count; index++) {
            treeId = produceTreeId(treeId, treeIdNum);
            treeIdList.add(treeId);
        }
        return treeIdList;
    }
}