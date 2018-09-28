package com.gyh.mycatDemo;

import java.io.*;
import java.sql.*;


/**
 * MySQL Blob tools
 * @author
 * @since
 * @version
 */
public class MySQLBlobTools {
    private static String VERSION = "0.0.1";
    private final static int MODE_INVALID = -1;
    private final static int MODE_READ = 0;
    private final static int MODE_WRITE = 1;
    private final static int MODE_UPDATE = 2;

//    public static void main(String[] args) throws  Exception{
//        String DBUrl = null;
//        String File = null;
//        String SQL = null;
//        String Id = null;
//        int mode = MODE_INVALID;
//
//        GetOpt getopt = new GetOpt(args, "hvrwuf:s:i:d:");
//        int c;
//        while((c = getopt.getNextOption()) != -1){
//            switch(c){
//            case 'h':
//                printHelpMessage();
//                System.exit(0);
//            case 'v':
//                printVersionMessage();
//                System.exit(0);
//            case 'r':
//            case 'w':
//            case 'u':
//                if (mode != MODE_INVALID){
//                    printHelpMessage();
//                    System.out.println();
//                    System.out.println("Only a mode parameter can specified. r = read, w = write, u = update.");
//                    System.exit(0);
//                }
//                if (c == 'r'){
//                    mode = MODE_READ;
//                }else if(c == 'w'){
//                    mode = MODE_WRITE;
//                }else if(c == 'u'){
//                    mode = MODE_UPDATE;
//                }
//                break;
//            case 'f':
//                File = getopt.getOptionArg();
//                break;
//            case 's':
//                SQL = getopt.getOptionArg();
//                break;
//            case 'i':
//                Id = getopt.getOptionArg();
//                break;
//            case 'd':
//                DBUrl = getopt.getOptionArg();
//                break;
//            default:
//                printHelpMessage();
//                System.exit(0);
//            }
//        }
//
//        if (File == null || DBUrl == null || SQL == null){
//            printHelpMessage();
//            System.out.println();
//            System.out.println("Need -f, -d -s parameter specified.");
//            System.exit(0);
//        }
//
//        int rv = 0;
//        switch(mode){
//        case MODE_READ:
//            rv = readBlob(File, DBUrl, SQL, Id);
//            break;
//        case MODE_WRITE:
//            rv = writeBlob(File, DBUrl, SQL);
//            break;
//        case MODE_UPDATE:
//            rv = updateBlob(File, DBUrl, SQL, Id);
//            break;
//        }
//
//        System.exit(rv);
//    }

    private static int updateBlob(String filename, String dBUrl, String SQL, String id) {
        FileInputStream fis = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName("org.gjt.mm.mysql.Driver").newInstance();
            conn = DriverManager.getConnection(dBUrl);

            File file = new File(filename);
            fis = new FileInputStream(file);
            pstmt = conn.prepareStatement(SQL);
            // //这种方法原理上会丢数据，因为file.length()返回的是long型
            pstmt.setBinaryStream(1, fis, fis.available()); // 第二个参数为文件的内容
            pstmt.setInt(2, Integer.parseInt(id));
            pstmt.executeUpdate();
            System.out.println("Success to write a blob data.");
            return 1;
        } catch (Exception ex) {
            System.out.println("[blobInsert error : ]" + ex.toString());
        } finally {
            // 关闭所打开的对像//
            try {
                pstmt.close();
                fis.close();
                conn.close();
            } catch (IOException e) {
            } catch (SQLException e) {
            }
        }

        return 0;
    }

    private static int writeBlob(String filename, String dBUrl, String SQL) {
        FileInputStream fis = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName("org.gjt.mm.mysql.Driver").newInstance();
            conn = DriverManager.getConnection(dBUrl);

            File file = new File(filename);
            fis = new FileInputStream(file);
            pstmt = conn.prepareStatement(SQL);
            // //这种方法原理上会丢数据，因为file.length()返回的是long型
            pstmt.setBinaryStream(1, fis, fis.available()); // 第二个参数为文件的内容
            pstmt.executeUpdate();
            pstmt.close();
            pstmt = conn.prepareStatement("SELECT LAST_INSERT_ID()");
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            rs.close();
            pstmt.close();
            System.out.println("Success to insert a blob record.");
            return rs.getInt(1);
        } catch (Exception ex) {
            System.out.println("[blobInsert error : ]" + ex.toString());
        } finally {
            // 关闭所打开的对像//
            try {
                pstmt.close();
                fis.close();
                conn.close();
            } catch (IOException e) {
            } catch (SQLException e) {
            }
        }

        return 0;
    }

    private static int readBlob(String filename, String dBUrl, String SQL, String id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        byte[] Buffer = new byte[4096];

        try {
            Class.forName("org.gjt.mm.mysql.Driver").newInstance();
            conn = DriverManager.getConnection(dBUrl);

            pstmt = conn.prepareStatement(SQL);
            // //这种方法原理上会丢数据，因为file.length()返回的是long型
            pstmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = pstmt.executeQuery();
            rs.next();

            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile(); // 如果文件不存在，则创建
            }
            FileOutputStream fos = new FileOutputStream(file);
            InputStream is = rs.getBinaryStream(1);
            int size = 0;
            long totalLen = 0;
            while ((size = is.read(Buffer)) != -1) {
                totalLen += size;
                fos.write(Buffer, 0, size);
            }
            fos.close();
            System.out.println("Read blob data success, total length:" + totalLen);
            return 1;
        } catch (Exception ex) {
            System.out.println("Read blob data error : ]" + ex.toString());
        } finally {
            // 关闭所打开的对像//
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
            }
        }

        return 0;
    }

    private static void printVersionMessage() {
        System.out.println("MySQLBlobTools was make to operate blob field in mysql database. The tools just can process only a record.");
        System.out.println("Author: yinhaibo Email: yhb_yinhaibo@163.com");
        System.out.println("Version: " + VERSION);
    }

    private static void printHelpMessage() {
        printVersionMessage();
        System.out.println("-r read mode, need d, f, s, i parameters");
        System.out.println("-w write mode, need d, f, s, i parameters");
        System.out.println("-u update mode, need d, f, s, i parameters");
        System.out.println("-d <MySQL URI> MySQL database uri, jdbc:mysql://localhost:3306/user?user=root&password=12345678");
        System.out.println("-f <Filename> file for reading or writing");
        System.out.println("-s <Operate SQL>, select blob from images where id=?, ? for i paramter");
        System.out.println("-i <Record Id>");
        System.out.println("For example:");
        System.out.println("java MySQBlobTools.jar -r -f Blob.png -s \"select blob from images where id = ?\" -i 123");
        System.out.println("java MySQBlobTools.jar -w -f Blob.png -s \"insert into images (blob) values(?) \"");
        System.out.println("java MySQBlobTools.jar -u -f Blob.png -s \"update images set blob=? where id=?\" -i 123");
    }
}