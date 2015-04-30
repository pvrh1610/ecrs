package com.novartis.ecrs.model.view;

import oracle.jbo.RowSetIterator;
import oracle.jbo.server.ViewObjectImpl;
import oracle.jbo.server.ViewRowSetImpl;

import org.w3c.dom.Element;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Wed Apr 29 09:43:24 IST 2015
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class HierarchyChildDetailVOImpl extends ViewObjectImpl {
    /**
     * This is the default constructor (do not remove).
     */
    public HierarchyChildDetailVOImpl() {
    }

    /**
     * Returns the bind variable value for bContentId.
     * @return bind variable value for bContentId
     */
    public String getBind_DictContentId() {
        return (String)getNamedWhereClauseParam("Bind_DictContentId");
    }

    /**
     * Sets <code>value</code> for bind variable bContentId.
     * @param value value to bind as bContentId
     */
    public void setBind_DictContentId(String value) {
        setNamedWhereClauseParam("Bind_DictContentId", value);
    }
    
//    @Override  
//     public void prepareRowSetForQuery(ViewRowSetImpl vrs) {  
//      RowSetIterator[] parentRows = vrs.getMasterRowSetIterators();  
//      /**  
//      * Check for parent Rows. If its not null, then query execution is   
//      * triggered through ViewLink, get the required attributes from   
//      * parent row( as per the use case ), and set the bind variable value  
//      */  
//      if (parentRows != null && parentRows.length > 0 &&  
//          parentRows[0].getCurrentRow() != null) {  
//          String contentId =  
//               (String)parentRows[0].getCurrentRow().getAttribute("DictContentId");  
//               vrs.ensureVariableManager().setVariableValue("bContentId",  
//                                                                       contentId);  
//      }  
//      super.prepareRowSetForQuery(vrs);  
//     }
}
