/******************************************************************************
 SessionLog OmegaT plugin - Plugin for OmegaT (htpp://www.omegat.org) to track
                            the actions of a user during translation and storing
                            the log in an XML file.
                            This plugin keeps track of all the editions
                            performed by a user during the translation of a
                            project. The plugin works in a transparent way for
                            the user and all the information obtained is stored
                            in an XML file which can be created when the tool
                            is closed.

 Copyright (C) 2013-2014 Universitat d'Alacant [www.ua.es]

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 **************************************************************************/

package org.omegat.plugins.sessionlog.loggers;

import org.omegat.plugins.sessionlog.IntrospectionTools;
import org.omegat.plugins.sessionlog.SessionLogPlugin;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.omegat.core.Core;
import org.omegat.core.data.SourceTextEntry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Miquel Esplà Gomis [mespla@dlsi.ua.es]
 */
public class XMLLogger implements BaseLogger{
    private Document log_document;
    
    private Element root_node;
        
    private Element current_project_node;
    
    private Element current_file_node;
    
    private Element current_entry_node;
    
    private Element current_editions_node;
    
    private int edition_idx;
    
    private UndoManager undomanager;
    
    private long chosen_entry_time;
    
    private int caretupdates_to_ignore;
    
    private String last_edited_text;
    
    private SessionLogPlugin sessionlog;
    
    private boolean emtpy_glossary_proposals;
    
    private boolean emtpy_mt_proposals;
    
    private boolean emtpy_tm_proposals;
    
    private int current_tm_proposal;
    
    private int current_segment_number;
    
    private int current_glossary_entries;
    
    private int current_MT_entries;
    
    private int current_TM_entries;
    
    static private DecimalFormat df = new DecimalFormat("#.###s", new DecimalFormatSymbols(Locale.ENGLISH));
    
    public XMLLogger(SessionLogPlugin sessionlog){
        this.sessionlog=sessionlog;
        Reset();
    }
    
    @Override
    public final void Reset(){
        root_node=null;
        current_file_node=null;
        current_entry_node=null;
        current_editions_node=null;
        current_segment_number=-1;
        current_glossary_entries=current_MT_entries=current_TM_entries=0;
        try {
            log_document=DocumentBuilderFactory.newInstance(
                    ).newDocumentBuilder().newDocument();
            
            Element rootElement = log_document.createElement("log");
            log_document.appendChild(rootElement);
            root_node=rootElement;
            
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace(System.err);
        }
    }
    
    
    @Override
    public int getCurrentTMProposals() {
        return current_tm_proposal;
    }

    @Override
    public void setCurrentTMProposals(int current_tm_proposal) {
        if(current_editions_node!=null && this.current_tm_proposal!=current_tm_proposal){
            Element element = NewElement("NewTMRecommendationSelected", true);
            element.setAttribute("former", Integer.toString(this.current_tm_proposal));
            element.setAttribute("current", Integer.toString(current_tm_proposal));
            current_editions_node.appendChild(element);
        }
        this.current_tm_proposal = current_tm_proposal;
    }

    @Override
    public void setEmtpyGlossaryProposals(boolean emtpy_glossary_proposals) {
        if(current_entry_node!=null && this.emtpy_glossary_proposals==true &&
                emtpy_glossary_proposals==false){
            current_glossary_entries=IntrospectionTools.getGlossaryEntries().size();
            /*Element element = NewElement("glossaryRecommendations", false);
            element.setAttribute("number", Integer.toString(
                    IntrospectionTools.getGlossaryEntries().size()));
            current_entry_node.appendChild(element);*/
        }
        this.emtpy_glossary_proposals=emtpy_glossary_proposals;
    }

    /**
     *
     * @param emtpy_mt_proposals
     */
    @Override
    public void setEmtpyMTProposals(boolean emtpy_mt_proposals) {
        if(current_entry_node!=null && this.emtpy_mt_proposals==true &&
                emtpy_mt_proposals==false){
            current_MT_entries=IntrospectionTools.getMTEntriesSize();
            /*Element element = NewElement("MTRecommendations", false);
            element.setAttribute("number", Integer.toString(
                    IntrospectionTools.getMTEntriesSize()));
            current_entry_node.appendChild(element);*/
        }
        this.emtpy_mt_proposals = emtpy_mt_proposals;
    }

    public boolean isEmtpyTMProposals() {
        return emtpy_tm_proposals;
    }

    @Override
    public void setEmtpyTMProposals(boolean emtpy_tm_proposals) {
        if(current_editions_node!=null && this.emtpy_tm_proposals==true &&
                emtpy_tm_proposals==false){
            current_TM_entries=IntrospectionTools.getMatches().size();
            /*Element element = NewElement("TMRecommendations", false);
            element.setAttribute("number", Integer.toString(
                    IntrospectionTools.getMatches().size()));
            current_entry_node.appendChild(element);*/
            this.current_tm_proposal=1;
        }
        this.emtpy_tm_proposals = emtpy_tm_proposals;
    }
    
    @Override
    public void LoggerEvent(String code, String param, String message){
        if(current_editions_node!=null){
            Element element = NewElement("loggerMessage", true);
            if(code!=null)
                element.setAttribute("code", code);
                if(param!=null)
                    element.setAttribute("object_name", param);
            element.appendChild(log_document.createTextNode(message));
            current_editions_node.appendChild(element);
            edition_idx++;
        }
    }
    
    @Override
    public void Undo(){
        if(current_editions_node!=null){
            int editionid = undomanager.Undo();
            if(editionid>=0){
                Element element = NewElement("undo", true);
                StringBuilder sb=new StringBuilder("ID");
                element.setAttribute("id", sb.append(Integer.toString(
                        edition_idx)).toString());
                sb=new StringBuilder("ID");
                element.setAttribute("on_event", sb.append(Integer.toString(
                        editionid)).toString());
                current_editions_node.appendChild(element);
                edition_idx++;
            }
        }
    }

    @Override
    public void Redo(){
        if(current_editions_node!=null){
            int editionid = undomanager.Redo();
            if(editionid>=0){
                Element element = NewElement("redo", true);
                StringBuilder sb=new StringBuilder("ID");
                element.setAttribute("id", sb.append(
                        Integer.toString(edition_idx)).toString());
                sb=new StringBuilder("ID");
                element.setAttribute("on_event", sb.append(
                        Integer.toString(editionid)).toString());
                current_editions_node.appendChild(element);
                edition_idx++;
            }
        }
    }
    
    @Override
    public void SetLastEditedText(String newtext){
        this.last_edited_text=newtext;
    }
        
    @Override
    public void SetPause(long increment){
        if(current_editions_node!=null){
            Element element=NewElement("pause", true);
            element.setAttribute("duration", df.format(increment/1000000000.0));
            current_editions_node.appendChild(element);
        }
    }
    
    private Element NewElement(String nodename, boolean time){
        Element element = log_document.createElement(nodename);
        if(time){
            Date d=new Date();
            SimpleDateFormat dt = new SimpleDateFormat("yyyyMMddHHmmssSS");
            dt.setTimeZone(TimeZone.getTimeZone("GMT"));
            StringBuilder sb=new StringBuilder(dt.format(d));
            sb.append("UTC");
            element.setAttribute("time", sb.toString());
            element.setAttribute("timestamp", Long.toString(
                    System.currentTimeMillis()));
        }
        return element;
    }
    
    @Override
    public void NewProject() throws FileNotFoundException{
        emtpy_mt_proposals=true;
        emtpy_glossary_proposals=true;
        current_tm_proposal=1;
        emtpy_tm_proposals=true;
        caretupdates_to_ignore=0;
        edition_idx=0;
        chosen_entry_time = -1;
        edition_idx=0;
        undomanager=new UndoManager();

        //Starting the root node
        Element rootElement = log_document.createElement("project");
        rootElement.setAttribute("name", 
                Core.getProject().getProjectProperties().getProjectName());
        rootElement.setAttribute("sl", 
                Core.getProject().getProjectProperties().getSourceLanguage(
                ).getLanguageCode());
        rootElement.setAttribute("tl", 
                Core.getProject().getProjectProperties().getTargetLanguage(
                ).getLanguageCode());

        root_node.appendChild(rootElement);
        current_project_node=rootElement;
        current_file_node=null;
        current_entry_node=null;
        current_editions_node=null;
    }
    
    @Override
    public void CloseProject(){
        CloseEntry();
        current_file_node=null;
        current_editions_node=null;
    }
    
    @Override
    public void NewFile(String doc_name){
        if(current_project_node!=null){
            Element element = NewElement("file", true);
            element.setAttribute("name", doc_name);
            current_file_node=element;
            current_project_node.appendChild(current_file_node);
            current_editions_node=null;
            current_segment_number=-1;
        }
    }
    
    @Override
    public void CloseEntry(){
        if(current_entry_node!=null){
            long time_consumed_entry=System.nanoTime()-chosen_entry_time;
            current_entry_node.setAttribute("duration", df.format(
                    time_consumed_entry/1000000000.0));
            Element element = NewElement("glossaryRecommendations", false);
            element.setAttribute("number", Integer.toString(this.current_TM_entries));
            current_entry_node.appendChild(element);
            element = NewElement("MTRecommendations", false);
            element.setAttribute("number", Integer.toString(this.current_MT_entries));
            current_entry_node.appendChild(element);
            element = NewElement("TMRecommendations", false);
            element.setAttribute("number", Integer.toString(this.current_glossary_entries));
            current_entry_node.appendChild(element);
            if(last_edited_text!=null){
                Element target_element = NewElement("finalTarget", false);
                target_element.appendChild(log_document.createTextNode(
                        last_edited_text));
                current_entry_node.appendChild(target_element);
            }
            else{
                if(Core.getEditor().getCurrentTranslation()!=null){
                    Element target_element = NewElement("finalTarget", false);
                    target_element.appendChild(log_document.createTextNode(
                            Core.getEditor().getCurrentTranslation()));
                    current_entry_node.appendChild(target_element);
                }
            }
            if(current_editions_node!=null &&
                    current_editions_node.getChildNodes().getLength()>0)
                current_entry_node.appendChild(current_editions_node);
            current_glossary_entries=current_MT_entries=current_TM_entries=0;
        }
    }
    
    @Override
    public void NewEntry(SourceTextEntry active_entry){
        if(current_file_node!=null && active_entry!=null){
            last_edited_text=Core.getEditor().getCurrentTranslation();
            caretupdates_to_ignore=1;
            sessionlog.GetMenu().setPauseTimestamp(0);
            sessionlog.GetMenu().getPausetiming().setSelected(false);
            Element element = NewElement("segment", true);
            element.setAttribute("number", Integer.toString(
                    Core.getEditor().getCurrentEntry().entryNum()));
            Element source_element = NewElement("source", false); 
            source_element.appendChild(log_document.createTextNode(
                    Core.getEditor().getCurrentEntry().getSrcText()));
            element.appendChild(source_element);
            Element target_element = NewElement("initialTarget", false);
            target_element.appendChild(log_document.createTextNode(
                    Core.getEditor().getCurrentTranslation()));
            element.appendChild(target_element);
            current_entry_node = element;
            current_file_node.appendChild(current_entry_node);
            current_editions_node = NewElement("events", false);
            chosen_entry_time = System.nanoTime();
            current_segment_number=Core.getEditor().getCurrentEntry().entryNum();
        }
    }
    
    @Override
    public int GetCurrentSegmentNumber(){
        return current_segment_number;
    }
    
    @Override
    public void DumpToWriter(PrintWriter pw){
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            tFactory.setAttribute("indent-number", 2);
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,
                    "-//OmegaT SessionLog//DTD XML SessionLog 0.9//EN");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
                    "http://www.dlsi.ua.es/~mespla/DTD/OmegaT/sessionlog-omegat-0.9.dtd");
            DOMSource source = new DOMSource(log_document);
            StreamResult result = new StreamResult(pw); 
            transformer.transform(source, result);
            pw.close();
        } catch (TransformerConfigurationException ex) {
            ex.printStackTrace(System.err);
        } catch (TransformerException ex) {
            ex.printStackTrace(System.err);
        }
    }
    
    private void NewEdition(int offset, String text, Element element){
        //This is called since the method "isEditMode" in EditorController cannot be accessed
        if(!IntrospectionTools.undoInProgress() &&
                Core.getEditor().getCurrentTranslation()!=null){
            caretupdates_to_ignore++;
            StringBuilder sb=new StringBuilder("ID");
            element.setAttribute("id", sb.append(
                    Integer.toString(edition_idx)).toString());
            element.setAttribute("length", Integer.toString(text.length()));
            element.setAttribute("offset", Integer.toString(offset));
            element.appendChild(log_document.createTextNode(text));
            current_editions_node.appendChild(element);
            undomanager.getUndoableIdx().push(edition_idx);
            
            edition_idx++;
        }
    }
    
    @Override
    public void NewInsertion(int offset, String text){
        if(current_editions_node!=null)
            NewEdition(offset, text, NewElement("insert", true));
    }
    
    @Override
    public void NewDeletion(int offset, String text){
        if(current_editions_node!=null)
            NewEdition(offset, text, NewElement("delete", true));
    }

    @Override
    public void InsertFromTM(int offset, int tu_pos, String text,
            int fms_stemming_onlywords, int fms_onlywords, int fms){
        if(current_editions_node!=null){
            Element element = NewElement("insert", true);
            element.setAttribute("from", "TM");
            element.setAttribute("fms_stemming_onlywords", Integer.toString(
                    fms_stemming_onlywords));
            element.setAttribute("fms_onlywords", Integer.toString(fms_onlywords));
            element.setAttribute("fms", Integer.toString(fms));
            element.setAttribute("proposal_number", Integer.toString(tu_pos+1));
            NewEdition(offset, text, element);
        }
    }
    
    @Override
    public void ReplaceFromTM(int offset, int tu_pos, String removedtext,
            String insertedtext, int fms_stemming_onlywords, int fms_onlywords,
            int fms){
        if(current_editions_node!=null){
            this.current_editions_node.removeChild(
                    this.current_editions_node.getLastChild());
            Element element = NewElement("delete", true);
            element.setAttribute("from", "TM");
            element.setAttribute("proposal_number", Integer.toString(tu_pos+1));
            NewEdition(offset, removedtext, element);
            InsertFromTM(offset, tu_pos, insertedtext, fms_stemming_onlywords,
                    fms_onlywords, fms);
        }
    }

    @Override
    public void ReplaceFromMT(int offset, String removedtext, String newtext){
        if(current_editions_node!=null){
            Element element = NewElement("delete", true);
            element.setAttribute("from", "MT");
            NewEdition(offset, removedtext, element);
            element = NewElement("insert", true);
            element.setAttribute("from", "MT");
            NewEdition(offset, newtext, element);
        }
    }

    @Override
    public void InsertFromGlossary(int offset, String newtext){
        if(current_editions_node!=null){
            Element element = (Element)current_editions_node.getLastChild();
            current_editions_node.removeChild(current_editions_node.getLastChild());
            element.setAttribute("from", "GLOSSARY");

            //If it was a replacement
            Element element2 = (Element)current_editions_node.getLastChild();
            if(element2.getNodeName().equals("delete") &&
                    element.getAttribute("offset").equals(element2.getAttribute("offset"))){
                element2.setAttribute("from", "GLOSSARY");
            }
            current_editions_node.appendChild(element);
        }
    }

    @Override
    public void CaretUpdate(int init_selection, int end_selection){
        if(current_editions_node!=null && !IntrospectionTools.undoInProgress()){
            if(caretupdates_to_ignore>0)
                caretupdates_to_ignore--;
            else{
                int global_offset=IntrospectionTools.getEditorTextArea().getOmDocument().getTranslationStart();
                if(init_selection==end_selection){
                    int offset=init_selection-global_offset-1;
                    Element element = NewElement("cursorMovement", true);
                    element.setAttribute("offset", Integer.toString(offset));
                    current_editions_node.appendChild(element);
                }
                else{
                    int init_offset=init_selection-global_offset-1;
                    int end_offset=end_selection-global_offset-1;
                    Element element = NewElement("textSelection", true);
                    element.setAttribute("init_offset", Integer.toString(init_offset));
                    element.setAttribute("end_offset", Integer.toString(end_offset));
                    element.appendChild(log_document.createTextNode(
                            Core.getEditor().getSelectedText()));
                    current_editions_node.appendChild(element);
                }
            }
        }
    }
}
