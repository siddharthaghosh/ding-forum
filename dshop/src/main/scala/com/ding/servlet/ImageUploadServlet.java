/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.servlet;

import gwtupload.server.exceptions.UploadActionException;
import java.io.File;
import com.ding.util.*;


/**
 *
 * @author tiger
 */
public class ImageUploadServlet extends BaseUploadServlet{

    @Override
    protected void validateFile(File f) throws UploadActionException {
        if(!FileValidator.isImageFile(f))
            throw new UploadActionException("file is not an image!");
    }

}
