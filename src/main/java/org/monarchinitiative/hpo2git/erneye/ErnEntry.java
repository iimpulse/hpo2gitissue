package org.monarchinitiative.hpo2git.erneye;

public class ErnEntry {

    String action;
    Term parent;
    String message;
    Term term;

    public ErnEntry(String action, Term term, Term parent, String message) {
        this.action=action;
        this.parent=parent;
        this.message=message;
        this.term=term;
    }


    public String toString() {

        String termAndParent=null;
        if (term != null && parent != null && term.level==parent.level+1) {
            termAndParent=String.format("Term: %s (parent: %s)", term.name,parent.name);
        } else if (term==null) {
            return String.format("No term; %s",message);
        } else {
            termAndParent=String.format("Term: %s (parent not provided)", term.name);
        }
        if (action==null || action.length()==0) {action="No action";}

        return String.format("%s) %s [%s]", action, termAndParent,message);
    }


}
