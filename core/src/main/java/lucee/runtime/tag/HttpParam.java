/**
 *
 * Copyright (c) 2014, the Railo Company Ltd. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 **/
package lucee.runtime.tag;

import javax.servlet.jsp.tagext.Tag;

import lucee.commons.io.res.util.ResourceUtil;
import lucee.runtime.exp.ApplicationException;
import lucee.runtime.ext.tag.TagImpl;

/**
* Required for cfhttp POST operations, cfhttpparam is used to specify the parameters necessary to 
* 	 build a cfhttp POST.
*
*
*
**/
public final class HttpParam extends TagImpl {
	
	HttpParamBean param=new HttpParamBean();

    /**
     * Applies to FormField and CGI types; ignored for all other types. 
     * Specifies whether to URLEncode the form field or header.
     * @param encoded
     */
    public void setEncoded(boolean encoded) {
        param.setEncoded(encoded);
    }

    /**
     * Applies to File type; invalid for all other types. 
     * Specifies the MIME media type of the file contents. 
     * The content type can include an identifier for the character encoding of the file; 
     * for example, text/html; charset=ISO-8859-1 indicates that the file is HTML text in 
     * the ISO Latin-1 character encoding.
     * @param mimetype
     */
    public void setMimetype(String mimetype) {
        param.setMimeType(mimetype);
    }
    
    
	/** set the value value
	*  Specifies the value of the URL, FormField, Cookie, File, or CGI variable being passed.
	* @param value value to set
	**/
	public void setValue(Object value)	{
		param.setValue(value);
	}

	/** set the value type
	*  The transaction type.
	* @param type value to set
	**/
	public void setType(String type)	{
		param.setType(type);
	}

	/** set the value file
	*  Required for type = "File".
	* @param file value to set
	**/
	public void setFile(String file)	{
		param.setFile(ResourceUtil.toResourceNotExisting(pageContext,file));
	}

	/** set the value name
	*  A variable name for the data being passed.
	* @param name value to set
	**/
	public void setName(String name)	{
		param.setName(name);
	}


	@Override
	public int doStartTag() throws ApplicationException	{
        if(param.getName()==null &&
                (!"body".equalsIgnoreCase(param.getType()) &&
                !"xml".equalsIgnoreCase(param.getType()))) {
            throw new ApplicationException("attribute [name] is required for tag [httpparam] if type is not [body or xml]");
        }
        
        if("file".equalsIgnoreCase(param.getType()) && param.getFile()==null)
        	throw new ApplicationException("attribute [file] is required for tag [httpparam] if type is [file]");
        
		// get HTTP Tag
		Tag parent=getParent();
		while(parent!=null && !(parent instanceof Http)) {
			parent=parent.getParent();
		}
		
		if(parent instanceof Http) {
			Http http = (Http)parent;
			http.setParam(param);
		}
		else {
			throw new ApplicationException("Wrong Context, tag HttpParam must be inside a Http tag");	
		}
		return SKIP_BODY;
	}

	@Override
	public int doEndTag()	{
		return EVAL_PAGE;
	}

	@Override
	public void release()	{
		super.release();
		param=new HttpParamBean();
	}
}