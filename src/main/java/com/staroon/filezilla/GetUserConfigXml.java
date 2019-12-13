package com.staroon.filezilla;

import com.staroon.filezilla.util.ExcelUtil;
import com.staroon.filezilla.util.Md5Util;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.ArrayList;

import static com.staroon.filezilla.util.ExcelUtil.getCellFormatValue;

/**
 * 用于批量创建FileZilla Server用户
 * 仅适用于FileZilla Server 0.9.46版本
 * <p>
 * Created with IntelliJ IDEA.
 * User: Staroon
 * Date: 2019-02-12
 * Time: 11:40:00
 */
public class GetUserConfigXml {

    public int getXmlAndCreateDir(String configFilePath, String ftpHome) throws Exception {
        ArrayList<User> users = new ArrayList<>(0);
        writeXMLData(configFilePath, ftpHome, users);
        createDir(users);
        return users.size();
    }

    /**
     * 生成最终的filezilla server xml配置文件
     */
    private void writeXMLData(String configFilePath, String ftpHome, ArrayList<User> users) throws Exception {
        System.out.println("读取FTP账号配置表...");
//        getUserFromTxt(configFilePath,ftpHome,users);
        getUserFromExcel(configFilePath, ftpHome, users);
        System.out.println("正在生成xml配置文件...");
        String writeStr = null;
        FileOutputStream fos = null;
        try {
            writeStr = createConfig(users);

            // 保存xml文件到桌面
            File file = new File(FileSystemView.getFileSystemView().getHomeDirectory() + "\\FileZilla Server.xml");

            fos = new FileOutputStream(file);
            fos.write(writeStr.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("xml配置文件生成完毕!!!");

    }

    /**
     * 创建用户文件夹
     */
    private static void createDir(ArrayList<User> users) {
        System.out.println("正在创建文件夹...");
        File file = null;
        for (User user : users) {
            file = new File(user.getFtpDir());
            file.mkdirs();
        }
        System.out.println("文件夹创建完毕!!!");
    }

    /**
     * 循环生成用户配置信息
     *
     * @return xml文档
     */
    private String createConfig(ArrayList<User> users) throws Exception {
        StringBuffer reStr = new StringBuffer();
        for (User user : users) {
            System.out.println(user.toString());
            reStr.append(produceUserConfigPart(user));
        }
        return joinConfig(reStr.toString());
    }

    /**
     * 从FTP账号配置文件中读取信息(txt文档)
     * 每行一个账号信息，包含3列数据：文件夹名称,FTP账号,FTP密码,文件夹分组，每列使用英文逗号","分割
     * 示例：
     * 文件夹一,user1,user1_pwd,分组一
     *
     * @param filePath FTP账号配置文件路径
     */
    private static void getUserFromTxt(String filePath, String ftpHome, ArrayList<User> users) {
        try {
            File file = new File(filePath);
            InputStreamReader inputReader = null;
            inputReader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bf = new BufferedReader(inputReader);
            // 按行读取字符串
            String str;
            while ((str = bf.readLine()) != null) {
                User user = new User();
                String[] strs = str.split(",");
                user.setUnitName(strs[0].trim());
                user.setFtpAccount(strs[1].trim());
                user.setFtpPassword(strs[2].trim());
                user.setMd5Password(Md5Util.getMd5(user.getFtpPassword()));
                user.setRegion(strs[3].trim());
                user.setFtpDir(ftpHome + user.getRegion() + "\\\\" + user.getUnitName());
                users.add(user);
            }
            bf.close();
            inputReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从FTP账号配置表中读取信息
     * Excel模板文件在resources下
     *
     * @param filePath FTP账号配置文件路径
     */
    private static void getUserFromExcel(String filePath, String ftpHome, ArrayList<User> users) throws Exception {

        Workbook wb = null;
        Sheet sheet = null;
        Row row = null;
        wb = ExcelUtil.readExcel(filePath);
        if (wb != null) {
            //获取"用户配置表"sheet
            sheet = wb.getSheet("用户配置表");
            //获取最大行数
            int rowNum = sheet.getPhysicalNumberOfRows();
            for (int i = 1; i < rowNum; i++) {
                User user = new User();
                row = sheet.getRow(i);
                if (row == null || row.getCell(1) == null || row.getCell(1).toString().trim().equals("")) {
//                    System.out.println(row.getCell(1).toString() + "+++");
                    break;
                } else {
//                    System.out.println(row.getCell(1).toString() + "+++");
                    user.setUnitName(getCellFormatValue(row.getCell(1)));// 文件夹名称
                    user.setFtpAccount(getCellFormatValue(row.getCell(2)));// FTP账号
                    user.setFtpPassword(getCellFormatValue(row.getCell(3)));// FTP密码
                    user.setRegion(getCellFormatValue(row.getCell(4)));// 文件夹分组
                    user.setFtpDir(ftpHome + user.getRegion() + "\\\\" + user.getUnitName());
                    user.setMd5Password(Md5Util.getMd5(user.getFtpPassword()));
                }
                users.add(user);
            }
        }
    }

    /**
     * 拼接xml片段
     *
     * @param insertStr xml片段
     * @return xml片段
     */
    private String joinConfig(String insertStr) throws Exception {
        String returnStr = "";
        InputStream inputStream = this.getClass().getResourceAsStream("/ftp.xml");
        byte[] by = new byte[inputStream.available()];
        inputStream.read(by);
        String outStr = new String(by, 0, by.length, "gb2312");
        if (outStr.contains("</Users>\r\n</FileZillaServer>")) {
            outStr = outStr.replaceAll("</Users>\r\n</FileZillaServer>", insertStr);
        }
        returnStr = returnStr + outStr;
        returnStr += "    </Users>\n</FileZillaServer>";

        return returnStr;
    }

    /**
     * 生成用户配置xml片段
     *
     * @param user ftp账号信息
     * @return xml片段
     */
    private static String produceUserConfigPart(User user) {
        String reStr =
                "        <User Name=\"" + user.getFtpAccount() + "\">\n"
                        + "            <Option Name=\"Pass\">" + user.getMd5Password() + "</Option>\n"
                        + "            <Option Name=\"Group\" />\n"
                        + "            <Option Name=\"Bypass server userlimit\">0</Option>\n"
                        + "            <Option Name=\"User Limit\">0</Option>\n"
                        + "            <Option Name=\"IP Limit\">0</Option>\n"
                        + "            <Option Name=\"Enabled\">1</Option>\n"
                        + "            <Option Name=\"Comments\" />\n"
                        + "            <Option Name=\"ForceSsl\">0</Option>\n"
                        + "            <Option Name=\"8plus3\">0</Option>\n"
                        + "            <IpFilter>\n"
                        + "                <Disallowed />\n"
                        + "                <Allowed />\n"
                        + "            </IpFilter>\n"
                        + "            <Permissions>\n"
                        + "                <Permission Dir=\"" + user.getFtpDir() + "\">\n"
                        + "                    <Option Name=\"FileRead\">1</Option>\n" // 文件下载权限
                        + "                    <Option Name=\"FileWrite\">1</Option>\n" // 文件上传权限
                        + "                    <Option Name=\"FileDelete\">0</Option>\n" // 文件删除权限
                        + "                    <Option Name=\"FileAppend\">1</Option>\n" // 文件编辑权限
                        + "                    <Option Name=\"DirCreate\">1</Option>\n" // 创建子目录权限
                        + "                    <Option Name=\"DirDelete\">0</Option>\n" // 删除子目录权限
                        + "                    <Option Name=\"DirList\">1</Option>\n" // 查看文件列表权限
                        + "                    <Option Name=\"DirSubdirs\">1</Option>\n" // 查看子目录文件权限
                        + "                    <Option Name=\"IsHome\">1</Option>\n" // 是否为主目录
                        + "                    <Option Name=\"AutoCreate\">0</Option>\n" // 是否自动创建目录
                        + "                </Permission>\n"
                        + "            </Permissions>\n"
                        + "            <SpeedLimits DlType=\"0\" DlLimit=\"10\" ServerDlLimitBypass=\"0\" UlType=\"0\" UlLimit=\"10\" ServerUlLimitBypass=\"0\">\n"
                        + "            <Download />\n"
                        + "            <Upload />\n"
                        + "            </SpeedLimits>\n"
                        + "        </User>\n";
        return reStr;
    }
}
