package com.bcinfo.tripaway.utils;

import java.util.Stack;
import java.util.Vector;
 
/**
 * 目标： 返回一个xml文本
 * 原因： 用字符串拼凑太麻烦， Dom太重量级， 提供一个介于中间的生成xml工具
 * 
 * @version V5.0
 * @author wuzq
 * @date   2014-11-6
 * @see http://www.codeproject.com/Articles/13498/XMLWriter-A-simple-reusable-class
 */
public class StringXmlWriter {
     
    private StringBuilder m_xml;//输出端
    private int m_iLevel;//用来处理缩进
    private Vector<String> m_attrVect;//存放属性，只存放当前标签的属性， 输出标签后清除
    private Stack<String> m_tagStack;//存放标签， 用来自动输出关闭标签
     
    public static void main(String[] args) {
        //Example 
        StringXmlWriter billXml = new StringXmlWriter();
        billXml.addAttributes("img", "xxx");//在添加标签前， 你可以添加任意个属性
        billXml.addAttributes("text", "123");//在添加标签前， 你可以添加任意个属性
        billXml.createTag("music");//添加标签
        billXml.closeLastTag();
        System.out.println(billXml);
    }
     
    public StringXmlWriter()
    {
        super();
        m_xml = new StringBuilder();
        m_attrVect = new Vector<String>();
        m_tagStack = new Stack<String>();
        m_xml.append("<p>");
//        String encoding = "utf-8";
//        m_xml.append("<?xml version=\"1.0\"  encoding=\"" + encoding + "\"?>");
    }
     
    /**
     * 添加xml标签， 如果标签有属性， 则必须先添加属性
     * @param tag
     * @author wuzq
     * @date 2014-11-6 下午3:33:48
     */
    public void createTag(String tag){
//        m_xml.append("\n");
        for(int i =0;i<m_iLevel;i++)
            m_xml.append("\t");
        m_xml.append("<").append(tag);
        addAttrToTag();
        m_xml.append(">");
        m_tagStack.push(tag);
        m_iLevel++;
    }
 
    private void addAttrToTag() {
        while(0 < m_attrVect.size()/2){
            String attr = m_attrVect.remove(m_attrVect.size()-1);
            m_xml.append(" ").append(attr).append("=");
            attr = m_attrVect.remove(m_attrVect.size()-1);
            m_xml.append("\"").append(attr).append("\"");
        }
        m_attrVect.clear();
    }
     
    /**
     * 关闭标签
     * 
     * @author wuzq
     * @date 2014-11-6 下午3:38:28
     */
    public void closeLastTag(){
//        m_xml.append("\n");
        m_iLevel--;
        for(int i=0; i<m_iLevel; i++){
            m_xml.append("\t");
        }
        m_xml.append("</").append(m_tagStack.pop()).append(">");
    }
     
    
    /**
     * 关闭标签
     * 
     * @author wuzq
     * @date 2014-11-6 下午3:38:28
     */
    public void closeLastTag(String value){
//        m_xml.append("\n");
        m_iLevel--;
        for(int i=0; i<m_iLevel; i++){
            m_xml.append("\t");
        }
        m_xml.append(value).append("</").append(m_tagStack.pop()).append(">");
    }
    
    /**
     * 关闭所有标签
     * 
     * @author wuzq
     * @date 2014-11-6 下午3:49:44
     */
    public void closeAllTags(){
        while(m_tagStack.size() != 0){
            m_xml.append("\n");
            m_iLevel--;
            for(int i=0;i<m_iLevel; i++){
                m_xml.append("\t");
            }
            m_xml.append("</").append(m_tagStack.pop()).append(">");
        }
    }
     
    /**
     * 添加子标签， 如果子标签有属性， 则必须先添加属性
     * @param tag
     * @param value
     * @author wuzq
     * @date 2014-11-6 下午3:50:37
     */
    public void createTextTag(String tag, String value){
//        m_xml.append("\n");
        for(int i=0; i<m_iLevel; i++){
            m_xml.append("\t");
        }
        m_xml.append("<").append(tag);
        addAttrToTag();
        m_xml.append(">").append(value).append("</").append(tag).append(">");
    }
     
    /**
     * 添加属性
     * @param key
     * @param value
     * @author wuzq
     * @date 2014-11-6 下午3:56:04
     */
    public void addAttributes(String key, String value){
        m_attrVect.addElement(value);
        m_attrVect.addElement(key);
    }
     
    /**
     * 添加注释
     * @param comment
     * @author wuzq
     * @date 2014-11-6 下午3:58:48
     */
    public void addComment(String comment){
        m_xml.append("\n");
        for(int i=0; i<m_iLevel; i++){
            m_xml.append("\t");
        }
        m_xml.append("<!--").append(comment).append("-->");
    }
     
    
    /**
     * 关闭标签
     * 
     * @author wuzq
     * @date 2014-11-6 下午3:38:28
     */
    public void close(){
    	m_xml.append("</p>");
    }
    
    @Override
    public String toString() {
        return m_xml.toString();
    }
 
}