/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ding.servlet;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

import org.apache.commons.fileupload.FileItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

/**
 *
 * @author tiger
 */
public class BaseUploadServlet extends UploadAction {

    protected static String uploadDir = "d:/temp/uploadfiles/";
    protected static String uploadContentType = "";
    private static final long serialVersionUID = 1L;
//    Hashtable<String, String> receivedContentTypes = new Hashtable<String, String>();
    ConcurrentHashMap<String, String> receivedContentTypesMap = new ConcurrentHashMap<String, String>();
    /**
     * Maintain a list with received files and their content types.
     */
//    Hashtable<String, File> receivedFiles = new Hashtable<String, File>();
    ConcurrentHashMap<String, File> receivedFilesMap = new ConcurrentHashMap<String, File>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String parameter = request.getParameter(PARAM_REMOVE);
        if (parameter != null) {
            try {
                removeItem(request, parameter);
                FileItem item = super.findFileItem(getSessionFileItems(request), parameter);
                if (item != null) {
                    removeItem(request, item);
                }
            } catch (Exception e) {
                renderXmlResponse(request, response, "<error>" + e.getMessage() + "<error>");
                return;
            }
        } else {
            super.doGet(request, response);
        }
    }

    /**
     * Override executeAction to save the received files in a custom place
     * and delete this items from session.
     */
    @Override
    public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
        if (false) {
            return null;
        }
        System.out.println("executeAction being called!!!!!!!!!!!!");
        System.out.println("sessionFiles List length is " + sessionFiles.size());
        for (FileItem item : sessionFiles) {
            if (false == item.isFormField()) {
                try {
                    /// Create a new file based on the remote file name in the client
                    // String saveName = item.getName().replaceAll("[\\\\/><\\|\\s\"'{}()\\[\\]]+", "_");
                    System.out.println("FileItem is " + item.toString());
                    System.out.println("upload files, field name is " + item.getFieldName() + " ,file name is " + item.getName());
                    String originFileName = item.getName();
                    String fileName = new File(originFileName).getName();
                    String filePrefix = request.getSession(true).getId().toString() + System.currentTimeMillis();
                    File file = new File(uploadDir + filePrefix + fileName);
                    item.write(file);
                    receivedFilesMap.put(item.getFieldName(), file);
                    receivedContentTypesMap.put(item.getFieldName(), item.getContentType());
                    /*
                     * 将文件名设置如session，方便后面Lift框架处理
                     */

                    HttpSession hs = request.getSession(true);
                    List<String> fileNames = (List<String>) hs.getAttribute("uploadFileNames");
                    if(fileNames == null)
                        fileNames = new Vector<String>();
                    fileNames.add(filePrefix + fileName);
                    hs.setAttribute("uploadFileNames", fileNames);
                    hs.setAttribute("uploadFileName", filePrefix + fileName);

                } catch (Exception e) {
                    throw new UploadActionException(e.getMessage());
                }
            }
            removeSessionFileItems(request);
        }
//        if (sessionFiles.isEmpty()) {
//            request.getSession().removeAttribute(this.ATTR_FILES);
//        } else {
//            request.getSession().setAttribute(this.ATTR_FILES, sessionFiles);
//        }
        return null;
    }

    /**
     * Get the content of an uploaded file.
     */
    @Override
    public void getUploadedFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fieldName = request.getParameter(PARAM_SHOW);
        File f = receivedFilesMap.get(fieldName);
        if (f != null) {
            response.setContentType(receivedContentTypesMap.get(fieldName));
            FileInputStream is = new FileInputStream(f);
            copyFromInputStreamToOutputStream(is, response.getOutputStream());
        } else {
            renderXmlResponse(request, response, ERROR_ITEM_NOT_FOUND);
        }
    }

    /**
     * Remove a file when the user sends a delete request.
     */
    @Override
    public void removeItem(HttpServletRequest request, String fieldName) throws UploadActionException {
        File file = receivedFilesMap.get(fieldName);
        receivedFilesMap.remove(fieldName);
        receivedContentTypesMap.remove(fieldName);
        if (file != null) {
            file.delete();
        }
        HttpSession hs = request.getSession(true);
        List<String> fileNames = (List<String>) hs.getAttribute("uploadFileNames");
        if (fileNames != null) {
            fileNames.remove(file.getName());
            hs.setAttribute("uploadFileNames", fileNames);
        }            
        hs.removeAttribute("uploadFileName");
    }

    @Override
    protected FileItemFactory getFileItemFactory(int requestSize) {
        return new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, new File(uploadDir));
    }
}
