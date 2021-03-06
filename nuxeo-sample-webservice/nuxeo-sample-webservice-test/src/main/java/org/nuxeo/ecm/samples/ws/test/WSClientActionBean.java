/*
 * (C) Copyright 2011 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     ataillefer
 */

package org.nuxeo.ecm.samples.ws.test;

import java.io.Serializable;
import java.util.List;

import net.restfulwebservices.servicecontracts._2008._01.IWeatherForecastService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.nuxeo.ecm.platform.ws.NuxeoRemotingInterface;
import org.nuxeo.ecm.samples.ws.client.WSClientService;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;
import org.nuxeo.runtime.api.Framework;

import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfstring;

/**
 * Seam action bean to test WebService client samples. Based on a Nuxeo IDE Seam
 * Action Bean artifact.
 * 
 * @author ataillefer
 */
@Name("wSClientAction")
@Scope(ScopeType.EVENT)
public class WSClientActionBean implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant log. */
    private static final Log log = LogFactory.getLog(WSClientActionBean.class);

    /** The document manager. */
    @In(create = true, required = false)
    protected transient CoreSession documentManager;

    /** The navigation context. */
    @In(create = true)
    protected NavigationContext navigationContext;

    /** The faces messages. */
    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;

    /** The resources accessor. */
    @In(create = true)
    protected transient ResourcesAccessor resourcesAccessor;

    /** The current nuxeo principal. */
    @In(create = true, required = false)
    protected NuxeoPrincipal currentNuxeoPrincipal;

    /** The documents lists manager. */
    @In(create = true)
    protected DocumentsListsManager documentsListsManager;

    // Sample code to show how to retrieve the list of selected documents in the
    // content listing view
    /**
     * Gets the currently selected documents.
     * 
     * @return the currently selected documents
     */
    protected List<DocumentModel> getCurrentlySelectedDocuments() {

        if (navigationContext.getCurrentDocument().isFolder()) {
            return documentsListsManager.getWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
        } else {
            return null;
        }
    }

    /**
     * Tests Weather forecast ws.
     * 
     * @return the string
     * @throws Exception the exception
     */
    public String testWeatherForecastWS() throws Exception {

        // Get the WSClientService
        WSClientService wsClientService = Framework.getService(WSClientService.class);

        // Make a call to weather forecast webservice (good example because
        // external webservice)
        IWeatherForecastService iwfs = wsClientService.getAdapter(IWeatherForecastService.class);
        ArrayOfstring cities = iwfs.getCitiesByCountry("FRANCE");
        String message = "Cities of country FRANCE: " + cities.getString();

        facesMessages.add(StatusMessage.Severity.INFO, message);

        return null;
    }

    /**
     * Tests nuxeo remoting ws.
     * 
     * @return the string
     * @throws Exception the exception
     */
    public String testNuxeoRemotingWS() throws Exception {

        // Get the WSClientService
        WSClientService wsClientService = Framework.getService(WSClientService.class);

        // Make a call to nuxeoremoting webservice (poor example because local
        // webservice...)
        NuxeoRemotingInterface nri = wsClientService.getAdapter(NuxeoRemotingInterface.class);
        String sessionId = nri.connect("Administrator", "Administrator");
        List<String> groupUsers = nri.getUsers(sessionId, "administrators");
        String message = "Users from group 'administrators': " + groupUsers;

        facesMessages.add(StatusMessage.Severity.INFO, message);

        return null;
    }

    // this method will be called by the action system to determine if the
    // action should be available
    //
    // the return value can depend on the context,
    // you can use the navigationContext to get the currentDocument,
    // currentWorkspace ...
    // you can cache the value in a member variable as long as the Bean stays
    // Event scoped
    //
    // if you don't need this, you should remove the filter in the associated
    // action contribution
    /**
     * Accept.
     * 
     * @return true, if successful
     */
    public boolean accept() {
        return true;
    }

}
