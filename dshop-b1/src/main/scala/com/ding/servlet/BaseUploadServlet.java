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

import com.ding.util.PropsJavaBridge;
import com.ding.util.StringUtils;
import gwtupload.server.AbstractUploadListener;
import gwtupload.server.exceptions.UploadCanceledException;
import gwtupload.server.exceptions.UploadException;
import gwtupload.server.exceptions.UploadSizeLimitException;
import gwtupload.server.exceptions.UploadTimeoutException;
import java.io.StringBufferInputStream;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author tiger
 */
public class BaseUploadServlet extends UploadAction {

    protected static PropsJavaBridge props = new PropsJavaBridge();
    protected static String uploadDir = props.getProperty("upload.tmpdir");
    protected static String keyFileUploadNamesSession = props.getProperty("upload.sessionkey");
//    protected static String uploadDir = ((net.liftweb.util.Props$)(Class.forName("net.liftweb.util.Props$").newInstance())).get("upload.tmpdir").open_$bang();
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
//        System.out.println("executeAction being called!!!!!!!!!!!!");
//        System.out.println("sessionFiles List length is " + sessionFiles.size());
        for (FileItem item : sessionFiles) {
            if (false == item.isFormField()) {

                /// Create a new file based on the remote file name in the client
                // String saveName = item.getName().replaceAll("[\\\\/><\\|\\s\"'{}()\\[\\]]+", "_");
//                    System.out.println("FileItem is " + item.toString());
//                    System.out.println("upload files, field name is " + item.getFieldName() + " ,file name is " + item.getName());
                String originFileName = item.getName();
                try {
                    System.out.println("originName : " + new String(originFileName.getBytes(), "utf-8"));
                }catch (Exception ex) {
                    System.err.println("encoding error");
                }
                
                String displayName = new File(originFileName).getName();
                String realName = request.getSession(true).getId().toString() + System.currentTimeMillis();
                File file = new File(uploadDir + realName);
                try {
                    item.write(file);
                    validateFile(file);
                    receivedFilesMap.put(item.getFieldName(), file);
                    receivedContentTypesMap.put(item.getFieldName(), item.getContentType());
                    
                    /*
                     * 将文件名设置如session，方便后面Lift框架处理
                     */
                    HttpSession hs = request.getSession(true);
//                    List<String> fileNames = (List<String>) hs.getAttribute(keyFileUploadNamesSession);
                    ConcurrentHashMap<String, String> uploadFileMap = (ConcurrentHashMap<String, String>) hs.getAttribute(keyFileUploadNamesSession);
//                    if (fileNames == null) {
//                        fileNames = new Vector<String>();
//                    }
                    if(uploadFileMap == null) {
                        uploadFileMap = new ConcurrentHashMap<String, String>();
                    }
//                    fileNames.add(realName);
                    uploadFileMap.put(realName, displayName);
//                    hs.setAttribute(keyFileUploadNamesSession, fileNames);
                    hs.setAttribute(keyFileUploadNamesSession, uploadFileMap);

                } catch (Exception e) {
                    if (file != null && file.exists()) {
                        file.delete();
                    }
                    removeSessionFileItems(request);
                    throw new UploadActionException(e.getMessage());
//                    throw e;
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
//        List<String> fileNames = (List<String>) hs.getAttribute(keyFileUploadNamesSession);
        ConcurrentHashMap<String, String> uploadFileMap = (ConcurrentHashMap<String, String>) hs.getAttribute(keyFileUploadNamesSession);
//        if (fileNames != null) {
//            fileNames.remove(file.getName());
//            hs.setAttribute(keyFileUploadNamesSession, fileNames);
//        }
        if(uploadFileMap != null) {
            uploadFileMap.remove(file.getName());
            hs.setAttribute(keyFileUploadNamesSession, uploadFileMap);
        }
    }

    @Override
    protected FileItemFactory getFileItemFactory(int requestSize) {
        return new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, new File(uploadDir));
    }

    protected void validateFile(File f) throws UploadActionException{
    }

      /**
   * This method parses the submit action, puts in session a listener where the
   * progress status is updated, and eventually stores the received data in
   * the user session.
   *
   * returns null in the case of success or a string with the error
   *
   */
  @Override
  protected String parsePostRequest(HttpServletRequest request, HttpServletResponse response) {

    try {
      String delay = request.getParameter("delay");
      uploadDelay = Integer.parseInt(delay);
    } catch (Exception e) { }

    HttpSession session = request.getSession();

    logger.debug("UPLOAD-SERVLET (" + session.getId() + ") new upload request received.");

    AbstractUploadListener listener = getCurrentListener(request);
    if (listener != null) {
      if (listener.isFrozen() || listener.isCanceled() || listener.getPercent() >= 100) {
        removeCurrentListener(request);
      } else {
        String error = "The request has been rejected because the server is already receiving another file.";
        logger.error("UPLOAD-SERVLET (" + session.getId() + ") " + error);
        return error;
      }
    }

    List<FileItem> uploadedItems;
    try {
      // set file upload progress listener, and put it into user session,
      // so the browser can use ajax to query status of the upload process
      listener = createNewListener(request);

      // Call to a method which the user can override
      checkRequest(request);

      // Create the factory used for uploading files,
      FileItemFactory factory = getFileItemFactory(request.getContentLength());
      ServletFileUpload uploader = new ServletFileUpload(factory);
      uploader.setHeaderEncoding("utf-8");
      uploader.setSizeMax(maxSize);
      uploader.setProgressListener(listener);

      // Receive the files
      logger.debug("UPLOAD-SERVLET (" + session.getId() + ") parsing HTTP POST request ");
      uploadedItems = uploader.parseRequest(request);
      logger.debug("UPLOAD-SERVLET (" + session.getId() + ") parsed request, " + uploadedItems.size() + " items received.");

      // Received files are put in session
      Vector<FileItem> sessionFiles = (Vector<FileItem>) getSessionFileItems(request);
      if (sessionFiles == null && uploadedItems.size() > 0) {
        sessionFiles = new Vector<FileItem>();
      }

      String error = "";

      for (FileItem fileItem : uploadedItems) {
        if (fileItem.isFormField() || fileItem.getSize() > 0) {
          sessionFiles.add(fileItem);
        } else {
          logger.error("UPLOAD-SERVLET (" + session.getId() + ") error File empty: " + fileItem);
          error += "\nError, the reception of the file " + fileItem.getName() + " was unsuccesful.\nPlease verify that the file exists and you have enough permissions to read it";
        }
      }

      if (sessionFiles.size() > 0) {
        String msg = "";
        for (FileItem i : sessionFiles) {
          msg += i.getFieldName() + " => " + i.getName() + "(" + i.getSize() + " bytes),";
        }
        logger.debug("UPLOAD-SERVLET (" + session.getId() + ") puting items in session: " + msg);
        session.setAttribute(ATTR_FILES, sessionFiles);
      } else {
        logger.error("UPLOAD-SERVLET (" + session.getId() + ") error NO DATA received ");
        error += "\nError, your browser has not sent any information.\nPlease try again or try it using another browser\n";
      }

      return error.length() > 0 ? error : null;

    } catch (SizeLimitExceededException e) {
      RuntimeException ex = new UploadSizeLimitException(e.getPermittedSize(), e.getActualSize());
      listener.setException(ex);
      throw ex;
    } catch (UploadSizeLimitException e) {
      listener.setException(e);
      throw e;
    } catch (UploadCanceledException e) {
      listener.setException(e);
      throw e;
    } catch (UploadTimeoutException e) {
      listener.setException(e);
      throw e;
    } catch (Exception e) {
      logger.error("UPLOAD-SERVLET (" + request.getSession().getId() + ") Unexpected Exception -> " + e.getMessage() + "\n" + stackTraceToString(e));
      System.out.println(stackTraceToString(e));
      e.printStackTrace();
      RuntimeException ex = new UploadException(e);
      listener.setException(ex);
      throw ex;
    }
  }
}
